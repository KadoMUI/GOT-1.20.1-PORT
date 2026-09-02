package got.conquest.pact;

import got.faction.GOTFaction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;

import java.util.Locale;
import java.util.Objects;

/** Stable NPC-role membership entry for an NPC-controlled canonical Pact. */
public record GOTCanonicalPactMember(String roleId, String displayName, GOTFaction faction) {
    public GOTCanonicalPactMember {
        roleId = normalizeRole(roleId);
        displayName = sanitizeDisplayName(displayName, roleId);
        faction = Objects.requireNonNullElse(faction, GOTFaction.UNALIGNED);
    }

    public CompoundTag save() {
        CompoundTag tag = new CompoundTag();
        tag.putString("Role", roleId);
        tag.putString("Name", displayName);
        tag.putString("Faction", faction.id());
        return tag;
    }

    public static GOTCanonicalPactMember load(CompoundTag tag) {
        String role = normalizeRole(tag.getString("Role"));
        if (role.isBlank()) return null;
        String name = tag.contains("Name", Tag.TAG_STRING) ? tag.getString("Name") : role;
        GOTFaction faction = GOTFaction.byId(tag.getString("Faction")).orElse(GOTFaction.UNALIGNED);
        return new GOTCanonicalPactMember(role, name, faction);
    }

    public static String normalizeRole(String raw) {
        return raw == null ? "" : raw.trim().toLowerCase(Locale.ROOT);
    }

    private static String sanitizeDisplayName(String raw, String fallback) {
        String value = raw == null ? "" : raw.trim();
        if (value.isBlank()) value = fallback;
        return value.substring(0, Math.min(64, value.length()));
    }
}
