package got.conquest.pact;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/** Persistent mutable state for one NPC-controlled canonical Pact. */
public final class GOTCanonicalPactState {
    private final ResourceLocation key;
    private final UUID id;
    private String name;
    private String leaderRoleId;
    private final LinkedHashMap<String, GOTCanonicalPactMember> members = new LinkedHashMap<>();
    private boolean active = true;
    private int definitionVersion;
    private long lastUpdatedGameTime;

    public GOTCanonicalPactState(GOTCanonicalPactDefinition definition) {
        this.key = definition.key();
        this.id = definition.id();
        this.name = definition.name();
        this.leaderRoleId = definition.leaderRoleId();
        for (GOTCanonicalPactMember member : definition.members()) members.put(member.roleId(), member);
        this.definitionVersion = GOTCanonicalPacts.DEFINITION_VERSION;
    }

    private GOTCanonicalPactState(ResourceLocation key, UUID id) {
        this.key = key;
        this.id = id;
    }

    public ResourceLocation key() { return key; }
    public UUID id() { return id; }
    public String name() { return name; }
    public String leaderRoleId() { return leaderRoleId; }
    public boolean active() { return active; }
    public int definitionVersion() { return definitionVersion; }
    public long lastUpdatedGameTime() { return lastUpdatedGameTime; }
    public Collection<GOTCanonicalPactMember> members() { return Collections.unmodifiableCollection(members.values()); }
    public Optional<GOTCanonicalPactMember> member(String roleId) { return Optional.ofNullable(members.get(GOTCanonicalPactMember.normalizeRole(roleId))); }
    public boolean containsRole(String roleId) { return members.containsKey(GOTCanonicalPactMember.normalizeRole(roleId)); }
    public Set<got.faction.GOTFaction> memberFactions() {
        return members.values().stream().map(GOTCanonicalPactMember::faction).collect(Collectors.toUnmodifiableSet());
    }

    /** Removes a Legendary role because its political allegiance changed. */
    public boolean removeMember(String roleId, long gameTime) {
        String normalized = GOTCanonicalPactMember.normalizeRole(roleId);
        if (normalized.isBlank() || members.remove(normalized) == null) return false;
        if (normalized.equals(leaderRoleId)) {
            leaderRoleId = members.isEmpty() ? "" : members.keySet().iterator().next();
        }
        active = !members.isEmpty();
        lastUpdatedGameTime = Math.max(0L, gameTime);
        return true;
    }

    /** Restores a role to this canonical Pact, primarily for migration/admin recovery. */
    public boolean addMember(GOTCanonicalPactMember member, boolean makeLeader, long gameTime) {
        if (member == null || members.containsKey(member.roleId())) return false;
        members.put(member.roleId(), member);
        if (makeLeader || leaderRoleId == null || leaderRoleId.isBlank()) leaderRoleId = member.roleId();
        active = true;
        lastUpdatedGameTime = Math.max(0L, gameTime);
        return true;
    }

    public CompoundTag save() {
        CompoundTag tag = new CompoundTag();
        tag.putString("Key", key.toString());
        tag.putUUID("Id", id);
        tag.putString("Name", name);
        tag.putString("LeaderRole", leaderRoleId);
        tag.putBoolean("Active", active);
        tag.putInt("DefinitionVersion", definitionVersion);
        tag.putLong("LastUpdatedGameTime", lastUpdatedGameTime);
        ListTag memberList = new ListTag();
        for (GOTCanonicalPactMember member : members.values()) memberList.add(member.save());
        tag.put("Members", memberList);
        return tag;
    }

    public static GOTCanonicalPactState load(CompoundTag tag) {
        ResourceLocation key = ResourceLocation.tryParse(tag.getString("Key"));
        if (key == null) return null;
        UUID storedId = tag.hasUUID("Id") ? tag.getUUID("Id") : null;
        UUID id = GOTCanonicalPacts.byKey(key).map(GOTCanonicalPactDefinition::id).orElse(storedId);
        if (id == null) return null;
        GOTCanonicalPactState state = new GOTCanonicalPactState(key, id);
        state.name = tag.contains("Name", Tag.TAG_STRING) ? tag.getString("Name") : key.getPath();
        state.leaderRoleId = GOTCanonicalPactMember.normalizeRole(tag.getString("LeaderRole"));
        state.active = !tag.contains("Active") || tag.getBoolean("Active");
        state.definitionVersion = Math.max(0, tag.getInt("DefinitionVersion"));
        state.lastUpdatedGameTime = Math.max(0L, tag.getLong("LastUpdatedGameTime"));

        ListTag memberList = tag.getList("Members", Tag.TAG_COMPOUND);
        for (int i = 0; i < memberList.size(); i++) {
            GOTCanonicalPactMember member = GOTCanonicalPactMember.load(memberList.getCompound(i));
            if (member != null) state.members.put(member.roleId(), member);
        }
        if (!state.members.containsKey(state.leaderRoleId) && !state.members.isEmpty()) {
            state.leaderRoleId = state.members.keySet().iterator().next();
        }
        return state;
    }
}
