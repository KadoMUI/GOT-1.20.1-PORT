package got.faction;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;

import java.util.EnumMap;
import java.util.Map;

/** Persistent per-player alignment and faction membership data. */
public final class GOTFactionPlayerData {
    private static final String PERSISTED_KEY = "got_faction_data";
    private static final String ALIGNMENTS = "alignments";
    private static final String STATS = "stats";

    private final Player player;
    private final CompoundTag root;

    private GOTFactionPlayerData(Player player) {
        this.player = player;
        CompoundTag persisted = player.getPersistentData().getCompound(Player.PERSISTED_NBT_TAG);
        this.root = persisted.getCompound(PERSISTED_KEY).copy();
    }

    public static GOTFactionPlayerData get(Player player) {
        return new GOTFactionPlayerData(player);
    }

    public float alignment(GOTFaction faction) {
        return root.getCompound(ALIGNMENTS).getFloat(faction.id());
    }

    public void setAlignment(GOTFaction faction, float value) {
        if (!faction.isPlayable()) return;
        CompoundTag alignments = root.getCompound(ALIGNMENTS);
        alignments.putFloat(faction.id(), Math.max(-1_000_000.0F, Math.min(1_000_000.0F, value)));
        root.put(ALIGNMENTS, alignments);
        save();
        if (player instanceof net.minecraft.server.level.ServerPlayer serverPlayer) {
            got.achievement.GOTAchievementHooks.awardRanksForAlignment(serverPlayer, faction, value);
        }
    }

    public float addAlignment(GOTFaction faction, float amount) {
        float before = alignment(faction);
        setAlignment(faction, before + amount);
        return alignment(faction) - before;
    }

    public GOTFaction membership() {
        return GOTFaction.byId(root.getString("membership")).orElse(GOTFaction.UNALIGNED);
    }

    public void setMembership(GOTFaction faction) {
        if (faction == null || faction == GOTFaction.UNALIGNED) root.remove("membership");
        else root.putString("membership", faction.id());
        root.putInt("pledge_kill_cooldown", 0);
        save();
    }

    public GOTFaction brokenFaction() {
        return GOTFaction.byId(root.getString("broken_faction")).orElse(GOTFaction.UNALIGNED);
    }

    public void setBrokenFaction(GOTFaction faction) {
        if (faction == null || faction == GOTFaction.UNALIGNED) root.remove("broken_faction");
        else root.putString("broken_faction", faction.id());
        save();
    }

    public int pledgeBreakCooldown() {
        return root.getInt("pledge_break_cooldown");
    }

    public int pledgeBreakCooldownStart() {
        return root.getInt("pledge_break_cooldown_start");
    }

    public void setPledgeBreakCooldown(int ticks) {
        int clamped = Math.max(0, ticks);
        root.putInt("pledge_break_cooldown", clamped);
        if (clamped > root.getInt("pledge_break_cooldown_start")) {
            root.putInt("pledge_break_cooldown_start", clamped);
        }
        if (clamped == 0) {
            root.putInt("pledge_break_cooldown_start", 0);
            root.remove("broken_faction");
        }
        save();
    }

    public int pledgeKillCooldown() {
        return root.getInt("pledge_kill_cooldown");
    }

    public void setPledgeKillCooldown(int ticks) {
        root.putInt("pledge_kill_cooldown", Math.max(0, ticks));
        save();
    }

    public boolean tickCooldowns() {
        boolean changed = false;
        int kill = pledgeKillCooldown();
        if (kill > 0) {
            root.putInt("pledge_kill_cooldown", kill - 1);
            changed = true;
        }
        int broken = pledgeBreakCooldown();
        if (broken > 0) {
            broken--;
            root.putInt("pledge_break_cooldown", broken);
            changed = true;
            if (broken == 0) {
                root.putInt("pledge_break_cooldown_start", 0);
                root.remove("broken_faction");
            }
        }
        if (changed) save();
        return changed;
    }

    public FactionStats stats(GOTFaction faction) {
        CompoundTag tag = root.getCompound(STATS).getCompound(faction.id());
        return new FactionStats(tag.getInt("npc_kills"), tag.getInt("enemy_kills"));
    }

    public void addNpcKill(GOTFaction faction) {
        editStats(faction, true);
    }

    public void addEnemyKill(GOTFaction faction) {
        editStats(faction, false);
    }

    private void editStats(GOTFaction faction, boolean npcKill) {
        CompoundTag allStats = root.getCompound(STATS);
        CompoundTag factionStats = allStats.getCompound(faction.id());
        String key = npcKill ? "npc_kills" : "enemy_kills";
        factionStats.putInt(key, factionStats.getInt(key) + 1);
        allStats.put(faction.id(), factionStats);
        root.put(STATS, allStats);
        save();
    }

    public Snapshot snapshot() {
        Map<GOTFaction, Float> alignments = new EnumMap<>(GOTFaction.class);
        Map<GOTFaction, FactionStats> statistics = new EnumMap<>(GOTFaction.class);
        for (GOTFaction faction : GOTFaction.playableFactions()) {
            alignments.put(faction, alignment(faction));
            statistics.put(faction, stats(faction));
        }
        return new Snapshot(Map.copyOf(alignments), membership(), brokenFaction(),
                pledgeBreakCooldown(), pledgeBreakCooldownStart(), Map.copyOf(statistics));
    }

    public static void copyPersisted(Player original, Player clone) {
        CompoundTag originalPersisted = original.getPersistentData().getCompound(Player.PERSISTED_NBT_TAG);
        CompoundTag clonePersisted = clone.getPersistentData().getCompound(Player.PERSISTED_NBT_TAG);
        if (originalPersisted.contains(PERSISTED_KEY)) {
            clonePersisted.put(PERSISTED_KEY, originalPersisted.getCompound(PERSISTED_KEY).copy());
            clone.getPersistentData().put(Player.PERSISTED_NBT_TAG, clonePersisted);
        }
    }

    private void save() {
        CompoundTag persistentData = player.getPersistentData();
        CompoundTag persisted = persistentData.getCompound(Player.PERSISTED_NBT_TAG);
        persisted.put(PERSISTED_KEY, root.copy());
        persistentData.put(Player.PERSISTED_NBT_TAG, persisted);
    }

    public record FactionStats(int npcKills, int enemyKills) {}

    public record Snapshot(Map<GOTFaction, Float> alignments,
                           GOTFaction membership,
                           GOTFaction brokenFaction,
                           int pledgeBreakCooldown,
                           int pledgeBreakCooldownStart,
                           Map<GOTFaction, FactionStats> stats) {}
}
