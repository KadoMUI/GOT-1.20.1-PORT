package got.common.fasttravel;

import got.common.world.map.GOTWaypoint;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.player.Player;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.UUID;

/** Player-persistent fast-travel state. Mirrors the legacy region unlock/use-count model. */
public final class GOTFastTravelData {
    private static final String ROOT = "GOTFastTravel";
    private static final String UNLOCKED = "UnlockedRegions";
    private static final String CUSTOM = "CustomWaypoints";
    private static final String NEXT_CUSTOM = "NextCustomId";
    private static final String USE_COUNTS = "UseCounts";
    private static final String SINCE = "TicksSinceTravel";
    private static final String PENDING = "Pending";

    public record Custom(UUID owner, int id, String name, int x, int y, int z) {
        public boolean ownedBy(UUID player) { return owner.equals(player); }
    }

    private GOTFastTravelData() {}

    public static CompoundTag root(Player player) {
        CompoundTag persistent = player.getPersistentData();
        if (!persistent.contains(ROOT, Tag.TAG_COMPOUND)) {
            CompoundTag root = new CompoundTag();
            root.putInt(SINCE, 20 * 600); // new players may use their first unlocked region immediately
            root.putInt(NEXT_CUSTOM, 0);
            persistent.put(ROOT, root);
        }
        return persistent.getCompound(ROOT);
    }

    public static EnumSet<GOTWaypoint.Region> unlocked(Player player) {
        EnumSet<GOTWaypoint.Region> out = EnumSet.noneOf(GOTWaypoint.Region.class);
        ListTag list = root(player).getList(UNLOCKED, Tag.TAG_STRING);
        for (int i = 0; i < list.size(); i++) {
            try { out.add(GOTWaypoint.Region.valueOf(list.getString(i))); }
            catch (IllegalArgumentException ignored) {}
        }
        return out;
    }

    public static boolean unlock(Player player, GOTWaypoint.Region region) {
        if (region == null || region == GOTWaypoint.Region.HIDDEN) return false;
        EnumSet<GOTWaypoint.Region> set = unlocked(player);
        if (!set.add(region)) return false;
        ListTag list = new ListTag();
        for (GOTWaypoint.Region value : set) list.add(StringTag.valueOf(value.name()));
        root(player).put(UNLOCKED, list);
        return true;
    }

    public static boolean isUnlocked(Player player, GOTWaypoint waypoint) {
        if (waypoint == null || waypoint.isHidden()) return false;
        EnumSet<GOTWaypoint.Region> set = unlocked(player);
        for (GOTWaypoint.Region region : waypoint.getRegions()) if (set.contains(region)) return true;
        return false;
    }

    public static int ticksSinceTravel(Player player) { return root(player).getInt(SINCE); }
    public static void setTicksSinceTravel(Player player, int ticks) { root(player).putInt(SINCE, Math.max(0, ticks)); }
    public static void tickSinceTravel(Player player) {
        int value = ticksSinceTravel(player);
        if (value < Integer.MAX_VALUE - 2) setTicksSinceTravel(player, value + 1);
    }

    public static int useCount(Player player, String key) {
        return root(player).getCompound(USE_COUNTS).getInt(key);
    }
    public static void incrementUse(Player player, String key) {
        CompoundTag counts = root(player).getCompound(USE_COUNTS);
        counts.putInt(key, counts.getInt(key) + 1);
        root(player).put(USE_COUNTS, counts);
    }

    public static List<Custom> custom(Player player) {
        ArrayList<Custom> out = new ArrayList<>();
        ListTag list = root(player).getList(CUSTOM, Tag.TAG_COMPOUND);
        for (int i = 0; i < list.size(); i++) {
            CompoundTag tag = list.getCompound(i);
            out.add(new Custom(player.getUUID(), tag.getInt("Id"), tag.getString("Name"), tag.getInt("X"), tag.getInt("Y"), tag.getInt("Z")));
        }
        return out;
    }

    public static Custom customById(Player player, int id) {
        for (Custom c : custom(player)) if (c.id() == id) return c;
        return null;
    }

    public static Custom createCustom(Player player, String name, int x, int y, int z) {
        CompoundTag r = root(player);
        int id = r.getInt(NEXT_CUSTOM);
        r.putInt(NEXT_CUSTOM, id + 1);
        Custom c = new Custom(player.getUUID(), id, sanitizeName(name), x, y, z);
        writeCustomList(player, append(custom(player), c));
        return c;
    }

    public static boolean renameCustom(Player player, int id, String name) {
        List<Custom> values = custom(player);
        boolean changed = false;
        ArrayList<Custom> out = new ArrayList<>(values.size());
        for (Custom c : values) {
            if (c.id() == id) { out.add(new Custom(player.getUUID(), id, sanitizeName(name), c.x(), c.y(), c.z())); changed = true; }
            else out.add(c);
        }
        if (changed) writeCustomList(player, out);
        return changed;
    }

    public static boolean deleteCustom(Player player, int id) {
        List<Custom> values = custom(player);
        ArrayList<Custom> out = new ArrayList<>();
        for (Custom c : values) if (c.id() != id) out.add(c);
        if (out.size() == values.size()) return false;
        writeCustomList(player, out);
        return true;
    }

    private static List<Custom> append(List<Custom> input, Custom value) {
        ArrayList<Custom> out = new ArrayList<>(input);
        out.add(value);
        return out;
    }

    private static void writeCustomList(Player player, List<Custom> values) {
        ListTag list = new ListTag();
        for (Custom c : values) {
            CompoundTag tag = new CompoundTag();
            tag.putInt("Id", c.id()); tag.putString("Name", c.name());
            tag.putInt("X", c.x()); tag.putInt("Y", c.y()); tag.putInt("Z", c.z());
            list.add(tag);
        }
        root(player).put(CUSTOM, list);
    }

    private static String sanitizeName(String name) {
        String value = name == null ? "" : name.trim();
        if (value.isEmpty()) value = "Waypoint";
        return value.length() > 32 ? value.substring(0, 32) : value;
    }

    public static CompoundTag pending(Player player) { return root(player).getCompound(PENDING); }
    public static void setPending(Player player, CompoundTag pending) { root(player).put(PENDING, pending); }
    public static void clearPending(Player player) { root(player).remove(PENDING); }
    public static boolean hasPending(Player player) { return root(player).contains(PENDING, Tag.TAG_COMPOUND); }

    public static void copyPersisted(Player oldPlayer, Player newPlayer) {
        if (oldPlayer.getPersistentData().contains(ROOT, Tag.TAG_COMPOUND)) {
            CompoundTag copy = oldPlayer.getPersistentData().getCompound(ROOT).copy();
            copy.remove(PENDING);
            newPlayer.getPersistentData().put(ROOT, copy);
        }
    }
}
