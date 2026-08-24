package got.claim;

import com.mojang.authlib.GameProfile;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.util.Mth;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/** Persistent, versioned claim state stored on and dropped with a banner. */
public final class GOTBannerClaim {
    public static final float MIN_ALIGNMENT = 1.0F;
    public static final float MAX_ALIGNMENT = 10_000.0F;
    public static final int DEFAULT_LIST_SIZE = 16;
    public static final int MAX_ENTRIES = 4_000;
    public static final int MAX_RANGE = 64;
    private static final int DATA_VERSION = 1;

    private boolean playerSpecific;
    private boolean structureProtection;
    private boolean selfProtection = true;
    private boolean configured;
    private float alignmentRequired = MIN_ALIGNMENT;
    private int customRange;
    private final EnumSet<GOTBannerPermission> defaultPermissions =
            EnumSet.noneOf(GOTBannerPermission.class);
    private final LinkedHashMap<String, GOTBannerWhitelistEntry> entries = new LinkedHashMap<>();

    public boolean playerSpecific() { return playerSpecific; }
    public boolean structureProtection() { return structureProtection; }
    public boolean selfProtection() { return selfProtection; }
    public boolean configured() { return configured; }
    public float alignmentRequired() { return alignmentRequired; }
    public int customRange() { return customRange; }
    public int defaultPermissionBits() { return GOTBannerPermission.encode(defaultPermissions); }
    public List<GOTBannerWhitelistEntry> entries() { return List.copyOf(entries.values()); }

    public void setPlayerSpecific(boolean value) { playerSpecific = value; configured = true; }
    public void setStructureProtection(boolean value) { structureProtection = value; configured = true; }
    public void setSelfProtection(boolean value) { selfProtection = value; configured = true; }
    public void markConfigured() { configured = true; }

    public void setAlignmentRequired(float value) {
        if (!Float.isFinite(value)) value = MIN_ALIGNMENT;
        alignmentRequired = Mth.clamp(value, MIN_ALIGNMENT, MAX_ALIGNMENT);
        configured = true;
    }

    public void setCustomRange(int value) {
        customRange = Mth.clamp(value, 0, MAX_RANGE);
        configured = true;
    }

    public void setDefaultPermissions(int bits) {
        defaultPermissions.clear();
        defaultPermissions.addAll(GOTBannerPermission.decode(bits));
        defaultPermissions.remove(GOTBannerPermission.FULL);
        configured = true;
    }

    public boolean defaultAllows(GOTBannerPermission permission) {
        return defaultPermissions.contains(permission);
    }

    public boolean addEntry(GOTBannerWhitelistEntry entry) {
        if (entries.size() >= MAX_ENTRIES && !entries.containsKey(entry.stableKey())) return false;
        entries.put(entry.stableKey(), entry);
        configured = true;
        return true;
    }

    public boolean removeEntry(String key) {
        boolean changed = entries.remove(key) != null;
        configured |= changed;
        return changed;
    }

    public boolean setEntryPermissions(String key, int permissions) {
        GOTBannerWhitelistEntry entry = entries.get(key);
        if (entry == null) return false;
        entries.put(key, entry.withPermissions(permissions));
        configured = true;
        return true;
    }

    @Nullable
    public GOTBannerWhitelistEntry entry(String key) {
        return entries.get(key);
    }

    public CompoundTag save(@Nullable UUID ownerId, @Nullable String ownerName) {
        CompoundTag tag = new CompoundTag();
        tag.putInt("DataVersion", DATA_VERSION);
        tag.putBoolean("Configured", configured);
        tag.putBoolean("PlayerProtection", playerSpecific);
        tag.putBoolean("StructureProtection", structureProtection);
        tag.putBoolean("SelfProtection", selfProtection);
        tag.putFloat("AlignmentProtection", alignmentRequired);
        tag.putFloat("AlignProtectF", alignmentRequired);
        tag.putInt("CustomRange", customRange);
        tag.putInt("DefaultPermissionBits", defaultPermissionBits());
        tag.putInt("WhitelistLength", Math.max(DEFAULT_LIST_SIZE, entries.size() + 1));
        if (ownerId != null) tag.putUUID("Owner", ownerId);
        if (ownerName != null && !ownerName.isBlank()) tag.putString("OwnerName", ownerName);

        ListTag modern = new ListTag();
        for (GOTBannerWhitelistEntry entry : entries.values()) modern.add(entry.save());
        tag.put("Entries", modern);

        // Preserve the legacy keys so a dropped banner remains intelligible to
        // tools and future importers that know the 1.7.10 format.
        ListTag defaultPerms = new ListTag();
        for (GOTBannerPermission permission : defaultPermissions) {
            defaultPerms.add(StringTag.valueOf(permission.name()));
        }
        if (!defaultPerms.isEmpty()) tag.put("DefaultPerms", defaultPerms);
        ListTag legacyEntries = new ListTag();
        int index = 1;
        for (GOTBannerWhitelistEntry entry : entries.values()) {
            CompoundTag row = new CompoundTag();
            row.putInt("Index", index++);
            row.putBoolean("Fellowship", entry.kind() == GOTBannerWhitelistEntry.Kind.GROUP);
            if (entry.kind() == GOTBannerWhitelistEntry.Kind.GROUP) {
                if (entry.id() != null) row.putString("FellowshipID", entry.id().toString());
                row.putString("FellowshipName", entry.name());
            } else {
                row.put("Profile", NbtUtils.writeGameProfile(new CompoundTag(),
                        new GameProfile(entry.id(), entry.name())));
            }
            row.putBoolean("PermsSaved", true);
            ListTag perms = new ListTag();
            for (GOTBannerPermission permission : GOTBannerPermission.decode(entry.permissions())) {
                perms.add(StringTag.valueOf(permission.name()));
            }
            row.put("Perms", perms);
            legacyEntries.add(row);
        }
        tag.put("AllowedPlayers", legacyEntries);
        return tag;
    }

    public void load(@Nullable CompoundTag tag) {
        entries.clear();
        defaultPermissions.clear();
        playerSpecific = false;
        structureProtection = false;
        selfProtection = true;
        configured = false;
        alignmentRequired = MIN_ALIGNMENT;
        customRange = 0;
        if (tag == null || tag.isEmpty()) return;

        configured = tag.getBoolean("Configured") || tag.contains("PlayerProtection")
                || tag.contains("AlignmentProtection") || tag.contains("AlignProtectF");
        playerSpecific = tag.getBoolean("PlayerProtection");
        structureProtection = tag.getBoolean("StructureProtection");
        selfProtection = !tag.contains("SelfProtection") || tag.getBoolean("SelfProtection");
        if (tag.contains("AlignmentProtection", Tag.TAG_ANY_NUMERIC)) {
            setAlignmentRequired(tag.getFloat("AlignmentProtection"));
        } else if (tag.contains("AlignProtectF", Tag.TAG_ANY_NUMERIC)) {
            setAlignmentRequired(tag.getFloat("AlignProtectF"));
        }
        customRange = Mth.clamp(tag.getInt("CustomRange"), 0, MAX_RANGE);

        if (tag.contains("DefaultPermissionBits", Tag.TAG_ANY_NUMERIC)) {
            defaultPermissions.addAll(GOTBannerPermission.decode(tag.getInt("DefaultPermissionBits")));
        } else if (tag.contains("DefaultPerms", Tag.TAG_LIST)) {
            ListTag permissions = tag.getList("DefaultPerms", Tag.TAG_STRING);
            for (Tag value : permissions) addPermissionName(defaultPermissions, value.getAsString());
        }
        defaultPermissions.remove(GOTBannerPermission.FULL);

        if (tag.contains("Entries", Tag.TAG_LIST)) {
            for (Tag value : tag.getList("Entries", Tag.TAG_COMPOUND)) {
                GOTBannerWhitelistEntry entry = GOTBannerWhitelistEntry.load((CompoundTag) value);
                if (entry != null) addEntry(entry);
            }
        } else {
            loadLegacyEntries(tag.getList("AllowedPlayers", Tag.TAG_COMPOUND));
        }
        configured = configured || !entries.isEmpty() || !defaultPermissions.isEmpty();
    }

    private void loadLegacyEntries(ListTag list) {
        for (Tag value : list) {
            CompoundTag row = (CompoundTag) value;
            int permissionBits = GOTBannerPermission.FULL.bit();
            if (row.getBoolean("PermsSaved")) {
                EnumSet<GOTBannerPermission> permissions = EnumSet.noneOf(GOTBannerPermission.class);
                for (Tag permission : row.getList("Perms", Tag.TAG_STRING)) {
                    addPermissionName(permissions, permission.getAsString());
                }
                permissionBits = GOTBannerPermission.encode(permissions);
            }
            if (row.getBoolean("Fellowship")) {
                UUID id = parseUuid(row.getString("FellowshipID"));
                String name = row.getString("FellowshipName");
                if (name.isBlank() && id != null) name = id.toString();
                if (!name.isBlank()) addEntry(GOTBannerWhitelistEntry.group(id, name, permissionBits));
            } else if (row.contains("Profile", Tag.TAG_COMPOUND)) {
                GameProfile profile = NbtUtils.readGameProfile(row.getCompound("Profile"));
                if (profile != null && profile.getId() != null && profile.getName() != null) {
                    addEntry(GOTBannerWhitelistEntry.player(profile.getId(), profile.getName(), permissionBits));
                }
            }
        }
    }

    private static void addPermissionName(EnumSet<GOTBannerPermission> target, String name) {
        try {
            target.add(GOTBannerPermission.valueOf(name));
        } catch (IllegalArgumentException ignored) {
        }
    }

    @Nullable
    private static UUID parseUuid(String value) {
        try {
            return value == null || value.isBlank() ? null : UUID.fromString(value);
        } catch (IllegalArgumentException ignored) {
            return null;
        }
    }
}
