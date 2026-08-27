package got.crime;

import got.faction.GOTFaction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;

import java.util.*;

/** Persistent legacy-style faction bounty ledger. Each qualifying kill remains active for 3,456,000 ticks. */
public final class GOTCrimeData extends SavedData {
    private static final String NAME = "got_crime_bounties";
    public static final int KILL_LIFETIME = 3_456_000;
    public static final int RECENT_BOUNTY_KILLED = 864_000;

    private final Map<String, Map<UUID, PlayerCrime>> byFaction = new HashMap<>();

    public static GOTCrimeData get(MinecraftServer server) {
        ServerLevel level = server.overworld();
        return level.getDataStorage().computeIfAbsent(GOTCrimeData::load, GOTCrimeData::new, NAME);
    }

    public void recordKill(GOTFaction faction, UUID player) {
        crime(faction, player).killTimers.add(KILL_LIFETIME);
        setDirty();
    }

    public int activeKills(GOTFaction faction, UUID player) {
        PlayerCrime crime = find(faction, player);
        return crime == null ? 0 : crime.killTimers.size();
    }

    public boolean hasBounty(GOTFaction faction, UUID player, int minimumKills) {
        PlayerCrime crime = find(faction, player);
        return crime != null && crime.recentBountyKilled <= 0 && crime.killTimers.size() >= Math.max(1, minimumKills);
    }

    public List<UUID> targets(GOTFaction faction, int minimumKills) {
        Map<UUID, PlayerCrime> map = byFaction.get(faction.id());
        if (map == null) return List.of();
        return map.entrySet().stream()
                .filter(e -> e.getValue().recentBountyKilled <= 0 && e.getValue().killTimers.size() >= minimumKills)
                .sorted((a,b) -> Integer.compare(b.getValue().killTimers.size(), a.getValue().killTimers.size()))
                .map(Map.Entry::getKey).toList();
    }

    public void recordBountyClaimed(GOTFaction faction, UUID target) {
        PlayerCrime crime = crime(faction, target);
        crime.recentBountyKilled = RECENT_BOUNTY_KILLED;
        setDirty();
    }

    public boolean recentlyClaimed(GOTFaction faction, UUID target) {
        PlayerCrime crime = find(faction, target);
        return crime != null && crime.recentBountyKilled > 0;
    }

    /** Called once per server tick; marks dirty only at coarse intervals or when records expire. */
    public void tick(long gameTime) {
        boolean changed = false;
        for (Iterator<Map.Entry<String, Map<UUID, PlayerCrime>>> fit = byFaction.entrySet().iterator(); fit.hasNext();) {
            Map<UUID, PlayerCrime> players = fit.next().getValue();
            for (Iterator<Map.Entry<UUID, PlayerCrime>> pit = players.entrySet().iterator(); pit.hasNext();) {
                PlayerCrime crime = pit.next().getValue();
                if (crime.recentBountyKilled > 0) { crime.recentBountyKilled--; changed = true; }
                for (ListIterator<Integer> it = crime.killTimers.listIterator(); it.hasNext();) {
                    int v = it.next() - 1;
                    if (v <= 0) it.remove(); else it.set(v);
                    changed = true;
                }
                if (crime.killTimers.isEmpty() && crime.recentBountyKilled <= 0) pit.remove();
            }
            if (players.isEmpty()) fit.remove();
        }
        if (changed && gameTime % 600L == 0L) setDirty();
    }

    private PlayerCrime crime(GOTFaction faction, UUID player) {
        return byFaction.computeIfAbsent(faction.id(), k -> new HashMap<>()).computeIfAbsent(player, k -> new PlayerCrime());
    }
    private PlayerCrime find(GOTFaction faction, UUID player) {
        Map<UUID, PlayerCrime> m = byFaction.get(faction.id()); return m == null ? null : m.get(player);
    }

    private static GOTCrimeData load(CompoundTag root) {
        GOTCrimeData data = new GOTCrimeData();
        ListTag factions = root.getList("Factions", 10);
        for (int i=0;i<factions.size();i++) {
            CompoundTag ft = factions.getCompound(i); String faction = ft.getString("Faction");
            Map<UUID, PlayerCrime> players = new HashMap<>();
            ListTag list = ft.getList("Players", 10);
            for (int j=0;j<list.size();j++) {
                CompoundTag pt=list.getCompound(j); if (!pt.hasUUID("UUID")) continue;
                PlayerCrime pc=new PlayerCrime(); pc.recentBountyKilled=pt.getInt("RecentBountyKilled");
                int[] timers=pt.getIntArray("KillTimers"); for(int t:timers) if(t>0) pc.killTimers.add(t);
                players.put(pt.getUUID("UUID"),pc);
            }
            data.byFaction.put(faction,players);
        }
        return data;
    }

    @Override public CompoundTag save(CompoundTag root) {
        ListTag factions=new ListTag();
        for(var fe:byFaction.entrySet()) {
            CompoundTag ft=new CompoundTag(); ft.putString("Faction",fe.getKey()); ListTag players=new ListTag();
            for(var pe:fe.getValue().entrySet()) {
                CompoundTag pt=new CompoundTag(); pt.putUUID("UUID",pe.getKey()); PlayerCrime pc=pe.getValue();
                pt.putInt("RecentBountyKilled",pc.recentBountyKilled); pt.putIntArray("KillTimers",pc.killTimers.stream().mapToInt(Integer::intValue).toArray()); players.add(pt);
            }
            ft.put("Players",players); factions.add(ft);
        }
        root.put("Factions",factions); return root;
    }

    private static final class PlayerCrime { final List<Integer> killTimers=new ArrayList<>(); int recentBountyKilled; }
}
