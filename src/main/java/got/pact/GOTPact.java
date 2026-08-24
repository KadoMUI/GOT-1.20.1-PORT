package got.pact;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;

import java.util.*;

/**
 * Server-authoritative player group. This is the 1.20.1 successor to the
 * 1.7.10 GOT Fellowship object, renamed Pact for Project Thrones.
 */
public final class GOTPact {
    public static final int DEFAULT_MAX_MEMBERS = 64;

    private final UUID id;
    private UUID owner;
    private String name;
    private ResourceLocation icon;

    private final LinkedHashSet<UUID> members = new LinkedHashSet<>();
    private final HashSet<UUID> admins = new HashSet<>();
    private final HashSet<UUID> mapSharers = new HashSet<>();
    private final HashMap<UUID, String> titles = new HashMap<>();

    // Faithful legacy defaults.
    private boolean preventPvp = true;
    private boolean preventHiredFriendlyFire = true;
    private boolean showMapLocations = true;

    public GOTPact(UUID id, UUID owner, String name) {
        this.id = Objects.requireNonNull(id);
        this.owner = Objects.requireNonNull(owner);
        this.name = sanitizeName(name);
        members.add(owner);
        mapSharers.add(owner);
    }

    public UUID id() { return id; }
    public UUID owner() { return owner; }
    public String name() { return name; }
    public Optional<ResourceLocation> icon() { return Optional.ofNullable(icon); }
    public boolean preventPvp() { return preventPvp; }
    public boolean preventHiredFriendlyFire() { return preventHiredFriendlyFire; }
    public boolean showMapLocations() { return showMapLocations; }
    public Set<UUID> members() { return Collections.unmodifiableSet(members); }
    public Set<UUID> admins() { return Collections.unmodifiableSet(admins); }
    public Set<UUID> mapSharers() { return Collections.unmodifiableSet(mapSharers); }
    public Map<UUID, String> titles() { return Collections.unmodifiableMap(titles); }
    public int size() { return members.size(); }

    public boolean contains(UUID player) { return members.contains(player); }
    public boolean isOwner(UUID player) { return owner.equals(player); }
    public boolean isAdmin(UUID player) { return isOwner(player) || admins.contains(player); }
    public boolean canManage(UUID player) { return isAdmin(player); }

    void setName(String name) { this.name = sanitizeName(name); }
    void setIcon(ResourceLocation icon) { this.icon = icon; }
    void setPreventPvp(boolean value) { this.preventPvp = value; }
    void setPreventHiredFriendlyFire(boolean value) { this.preventHiredFriendlyFire = value; }
    void setShowMapLocations(boolean value) { this.showMapLocations = value; }

    boolean addMember(UUID player) {
        if (members.size() >= DEFAULT_MAX_MEMBERS || !members.add(player)) return false;
        if (showMapLocations) mapSharers.add(player);
        return true;
    }

    boolean removeMember(UUID player) {
        if (isOwner(player)) return false;
        admins.remove(player);
        mapSharers.remove(player);
        titles.remove(player);
        return members.remove(player);
    }

    boolean setAdmin(UUID player, boolean admin) {
        if (!members.contains(player) || isOwner(player)) return false;
        return admin ? admins.add(player) : admins.remove(player);
    }

    boolean transferOwnership(UUID newOwner) {
        if (!members.contains(newOwner) || owner.equals(newOwner)) return false;
        UUID previous = owner;
        owner = newOwner;
        admins.remove(newOwner);
        admins.add(previous);
        return true;
    }

    boolean setMapSharing(UUID player, boolean sharing) {
        if (!members.contains(player)) return false;
        return sharing ? mapSharers.add(player) : mapSharers.remove(player);
    }

    void setTitle(UUID player, String title) {
        if (!members.contains(player)) return;
        String clean = title == null ? "" : title.trim();
        if (clean.isEmpty()) titles.remove(player);
        else titles.put(player, clean.substring(0, Math.min(32, clean.length())));
    }

    public String title(UUID player) { return titles.getOrDefault(player, ""); }

    private static String sanitizeName(String name) {
        String n = name == null ? "" : name.trim();
        if (n.isEmpty()) n = "Unnamed Pact";
        return n.substring(0, Math.min(32, n.length()));
    }

    public CompoundTag save() {
        CompoundTag tag = new CompoundTag();
        tag.putUUID("Id", id);
        tag.putUUID("Owner", owner);
        tag.putString("Name", name);
        if (icon != null) tag.putString("Icon", icon.toString());
        tag.putBoolean("PreventPvp", preventPvp);
        tag.putBoolean("PreventHiredFF", preventHiredFriendlyFire);
        tag.putBoolean("ShowMapLocations", showMapLocations);
        tag.put("Members", uuidList(members));
        tag.put("Admins", uuidList(admins));
        tag.put("MapSharers", uuidList(mapSharers));

        ListTag titleList = new ListTag();
        titles.forEach((uuid, title) -> {
            CompoundTag t = new CompoundTag();
            t.putUUID("Player", uuid);
            t.putString("Title", title);
            titleList.add(t);
        });
        tag.put("Titles", titleList);
        return tag;
    }

    public static GOTPact load(CompoundTag tag) {
        UUID id = tag.getUUID("Id");
        UUID owner = tag.getUUID("Owner");
        GOTPact pact = new GOTPact(id, owner, tag.getString("Name"));
        pact.members.clear();
        readUuidList(tag.getList("Members", Tag.TAG_STRING), pact.members);
        pact.members.add(owner);
        readUuidList(tag.getList("Admins", Tag.TAG_STRING), pact.admins);
        pact.admins.remove(owner);
        readUuidList(tag.getList("MapSharers", Tag.TAG_STRING), pact.mapSharers);

        if (tag.contains("Icon", Tag.TAG_STRING)) {
            ResourceLocation parsed = ResourceLocation.tryParse(tag.getString("Icon"));
            pact.icon = parsed;
        }
        if (tag.contains("PreventPvp")) pact.preventPvp = tag.getBoolean("PreventPvp");
        if (tag.contains("PreventHiredFF")) pact.preventHiredFriendlyFire = tag.getBoolean("PreventHiredFF");
        if (tag.contains("ShowMapLocations")) pact.showMapLocations = tag.getBoolean("ShowMapLocations");

        ListTag titleList = tag.getList("Titles", Tag.TAG_COMPOUND);
        for (int i = 0; i < titleList.size(); i++) {
            CompoundTag t = titleList.getCompound(i);
            if (t.hasUUID("Player") && pact.members.contains(t.getUUID("Player"))) {
                pact.setTitle(t.getUUID("Player"), t.getString("Title"));
            }
        }
        pact.admins.retainAll(pact.members);
        pact.mapSharers.retainAll(pact.members);
        return pact;
    }

    private static ListTag uuidList(Collection<UUID> values) {
        ListTag list = new ListTag();
        for (UUID uuid : values) list.add(StringTag.valueOf(uuid.toString()));
        return list;
    }

    private static void readUuidList(ListTag list, Collection<UUID> output) {
        for (int i = 0; i < list.size(); i++) {
            try { output.add(UUID.fromString(list.getString(i))); }
            catch (IllegalArgumentException ignored) { }
        }
    }
}
