package got.claim;

import net.minecraft.nbt.CompoundTag;

import javax.annotation.Nullable;
import java.util.Locale;
import java.util.UUID;

/** A player or future Pact/Fellowship entry in a banner's access list. */
public record GOTBannerWhitelistEntry(Kind kind, @Nullable UUID id, String name, int permissions) {
    public enum Kind { PLAYER, GROUP }

    public GOTBannerWhitelistEntry {
        name = name == null ? "" : name.trim();
        permissions &= 0xFF;
        if (kind == Kind.PLAYER && id == null) {
            throw new IllegalArgumentException("Player claim entries require a UUID");
        }
        if (name.isBlank()) throw new IllegalArgumentException("Claim entry names cannot be blank");
        if (name.length() > 64) throw new IllegalArgumentException("Claim entry names are limited to 64 characters");
    }

    public static GOTBannerWhitelistEntry player(UUID id, String name, int permissions) {
        return new GOTBannerWhitelistEntry(Kind.PLAYER, id, name, permissions);
    }

    public static GOTBannerWhitelistEntry group(@Nullable UUID id, String name, int permissions) {
        String clean = name.toLowerCase(Locale.ROOT).startsWith("f/") ? name.substring(2) : name;
        return new GOTBannerWhitelistEntry(Kind.GROUP, id, clean, permissions);
    }

    public boolean allows(GOTBannerPermission permission) {
        return (permissions & GOTBannerPermission.FULL.bit()) != 0
                || (permissions & permission.bit()) != 0;
    }

    public String displayName() {
        return kind == Kind.GROUP ? "f/" + name : name;
    }

    public String stableKey() {
        if (kind == Kind.PLAYER) return "p/" + id;
        return "f/" + (id == null ? name.toLowerCase(Locale.ROOT) : id.toString());
    }

    public GOTBannerWhitelistEntry withPermissions(int bits) {
        return new GOTBannerWhitelistEntry(kind, id, name, bits);
    }

    public CompoundTag save() {
        CompoundTag tag = new CompoundTag();
        tag.putString("Kind", kind.name());
        if (id != null) tag.putUUID("Id", id);
        tag.putString("Name", name);
        tag.putInt("Permissions", permissions);
        return tag;
    }

    @Nullable
    public static GOTBannerWhitelistEntry load(CompoundTag tag) {
        try {
            Kind kind = Kind.valueOf(tag.getString("Kind"));
            UUID id = tag.hasUUID("Id") ? tag.getUUID("Id") : null;
            String name = tag.getString("Name");
            int permissions = tag.contains("Permissions")
                    ? tag.getInt("Permissions") : GOTBannerPermission.FULL.bit();
            return new GOTBannerWhitelistEntry(kind, id, name, permissions);
        } catch (IllegalArgumentException ignored) {
            return null;
        }
    }
}
