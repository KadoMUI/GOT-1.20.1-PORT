package got.pact;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.saveddata.SavedData;

import java.util.*;

/** One world-global pact database, persisted in the server's overworld data storage. */
public final class GOTPactSavedData extends SavedData {
    private static final String FILE_NAME = "got_pacts";
    private final Map<UUID, GOTPact> pacts = new HashMap<>();
    private final Map<UUID, UUID> playerToPact = new HashMap<>();
    private final Map<UUID, GOTPactInvite> invitesByPlayer = new HashMap<>();

    public static GOTPactSavedData get(MinecraftServer server) {
        return server.overworld().getDataStorage().computeIfAbsent(GOTPactSavedData::load, GOTPactSavedData::new, FILE_NAME);
    }

    public Collection<GOTPact> all() { return Collections.unmodifiableCollection(pacts.values()); }
    public Optional<GOTPact> pact(UUID id) { return Optional.ofNullable(pacts.get(id)); }
    public Optional<GOTPact> pactFor(UUID player) {
        UUID id = playerToPact.get(player);
        return id == null ? Optional.empty() : pact(id);
    }
    public Optional<GOTPactInvite> inviteFor(UUID player) { return Optional.ofNullable(invitesByPlayer.get(player)); }

    void put(GOTPact pact) {
        pacts.put(pact.id(), pact);
        for (UUID member : pact.members()) playerToPact.put(member, pact.id());
        setDirty();
    }

    void indexPlayer(UUID player, UUID pact) { playerToPact.put(player, pact); setDirty(); }
    void unindexPlayer(UUID player) { playerToPact.remove(player); setDirty(); }
    void removePact(UUID pactId) {
        GOTPact removed = pacts.remove(pactId);
        if (removed != null) removed.members().forEach(playerToPact::remove);
        invitesByPlayer.values().removeIf(i -> i.pactId().equals(pactId));
        setDirty();
    }
    void invite(GOTPactInvite invite) { invitesByPlayer.put(invite.invitedPlayerId(), invite); setDirty(); }
    void clearInvite(UUID player) { if (invitesByPlayer.remove(player) != null) setDirty(); }

    @Override public CompoundTag save(CompoundTag tag) {
        ListTag pactList = new ListTag();
        pacts.values().stream().sorted(Comparator.comparing(p -> p.id().toString())).forEach(p -> pactList.add(p.save()));
        tag.put("Pacts", pactList);
        ListTag inviteList = new ListTag();
        invitesByPlayer.values().forEach(i -> inviteList.add(i.save()));
        tag.put("Invites", inviteList);
        return tag;
    }

    public static GOTPactSavedData load(CompoundTag tag) {
        GOTPactSavedData data = new GOTPactSavedData();
        ListTag pacts = tag.getList("Pacts", Tag.TAG_COMPOUND);
        for (int i = 0; i < pacts.size(); i++) {
            try {
                GOTPact pact = GOTPact.load(pacts.getCompound(i));
                data.pacts.put(pact.id(), pact);
                for (UUID member : pact.members()) data.playerToPact.put(member, pact.id());
            } catch (RuntimeException ignored) { }
        }
        ListTag invites = tag.getList("Invites", Tag.TAG_COMPOUND);
        for (int i = 0; i < invites.size(); i++) {
            try {
                GOTPactInvite invite = GOTPactInvite.load(invites.getCompound(i));
                data.invitesByPlayer.put(invite.invitedPlayerId(), invite);
            } catch (RuntimeException ignored) { }
        }
        return data;
    }
}
