package got.conquest.pact;

import got.faction.GOTFaction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.UUID;

/**
 * Persistent political assignment for a stable Legendary NPC role.
 *
 * The role id is authoritative rather than a spawned entity UUID so ordinary
 * death/respawn, chunk reloads and entity recreation never erase Pact loyalty.
 */
public final class GOTLegendaryPactMembership {
    private final String roleId;
    private String displayName;
    private GOTFaction faction;
    @Nullable private ResourceLocation originCanonicalPact;
    @Nullable private UUID currentPactId;
    @Nullable private UUID recruitedBy;
    private long recruitedAtGameTime;
    private long lastUpdatedGameTime;

    public GOTLegendaryPactMembership(String roleId, String displayName, GOTFaction faction,
                                      @Nullable ResourceLocation originCanonicalPact,
                                      @Nullable UUID currentPactId) {
        this.roleId = GOTCanonicalPactMember.normalizeRole(roleId);
        this.displayName = sanitizeName(displayName, this.roleId);
        this.faction = faction == null ? GOTFaction.UNALIGNED : faction;
        this.originCanonicalPact = originCanonicalPact;
        this.currentPactId = currentPactId;
    }

    public String roleId() { return roleId; }
    public String displayName() { return displayName; }
    public GOTFaction faction() { return faction; }
    public Optional<ResourceLocation> originCanonicalPact() { return Optional.ofNullable(originCanonicalPact); }
    public Optional<UUID> currentPactId() { return Optional.ofNullable(currentPactId); }
    public Optional<UUID> recruitedBy() { return Optional.ofNullable(recruitedBy); }
    public long recruitedAtGameTime() { return recruitedAtGameTime; }
    public long lastUpdatedGameTime() { return lastUpdatedGameTime; }

    public boolean isRecruited() {
        if (currentPactId == null) return false;
        return originCanonicalPact == null
                || !GOTCanonicalPacts.byKey(originCanonicalPact)
                    .map(definition -> definition.id().equals(currentPactId))
                    .orElse(false);
    }

    public void refreshIdentity(String displayName, GOTFaction faction) {
        this.displayName = sanitizeName(displayName, roleId);
        if (faction != null && faction != GOTFaction.UNALIGNED) this.faction = faction;
    }

    public void rememberOrigin(@Nullable ResourceLocation originCanonicalPact) {
        if (this.originCanonicalPact == null && originCanonicalPact != null) {
            this.originCanonicalPact = originCanonicalPact;
        }
    }

    public void assign(UUID pactId, @Nullable UUID recruiter, long gameTime) {
        this.currentPactId = pactId;
        this.recruitedBy = recruiter;
        this.recruitedAtGameTime = recruiter == null ? 0L : Math.max(0L, gameTime);
        this.lastUpdatedGameTime = Math.max(0L, gameTime);
    }

    public void clearAssignment(long gameTime) {
        this.currentPactId = null;
        this.recruitedBy = null;
        this.recruitedAtGameTime = 0L;
        this.lastUpdatedGameTime = Math.max(0L, gameTime);
    }

    public CompoundTag save() {
        CompoundTag tag = new CompoundTag();
        tag.putString("Role", roleId);
        tag.putString("Name", displayName);
        tag.putString("Faction", faction.id());
        if (originCanonicalPact != null) tag.putString("OriginCanonicalPact", originCanonicalPact.toString());
        if (currentPactId != null) tag.putUUID("CurrentPact", currentPactId);
        if (recruitedBy != null) tag.putUUID("RecruitedBy", recruitedBy);
        tag.putLong("RecruitedAtGameTime", recruitedAtGameTime);
        tag.putLong("LastUpdatedGameTime", lastUpdatedGameTime);
        return tag;
    }

    public static GOTLegendaryPactMembership load(CompoundTag tag) {
        String role = GOTCanonicalPactMember.normalizeRole(tag.getString("Role"));
        if (role.isBlank()) return null;
        String name = tag.contains("Name", Tag.TAG_STRING) ? tag.getString("Name") : role;
        GOTFaction faction = GOTFaction.byId(tag.getString("Faction")).orElse(GOTFaction.UNALIGNED);
        ResourceLocation origin = null;
        if (tag.contains("OriginCanonicalPact", Tag.TAG_STRING)) {
            origin = ResourceLocation.tryParse(tag.getString("OriginCanonicalPact"));
        }
        UUID pact = tag.hasUUID("CurrentPact") ? tag.getUUID("CurrentPact") : null;
        GOTLegendaryPactMembership state = new GOTLegendaryPactMembership(role, name, faction, origin, pact);
        state.recruitedBy = tag.hasUUID("RecruitedBy") ? tag.getUUID("RecruitedBy") : null;
        state.recruitedAtGameTime = Math.max(0L, tag.getLong("RecruitedAtGameTime"));
        state.lastUpdatedGameTime = Math.max(0L, tag.getLong("LastUpdatedGameTime"));
        return state;
    }

    private static String sanitizeName(String raw, String fallback) {
        String value = raw == null ? "" : raw.trim();
        if (value.isBlank()) value = fallback;
        return value.substring(0, Math.min(64, value.length()));
    }
}
