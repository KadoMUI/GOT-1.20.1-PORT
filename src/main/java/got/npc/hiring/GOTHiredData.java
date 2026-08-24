package got.npc.hiring;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;

import javax.annotation.Nullable;
import java.util.UUID;

/**
 * Server-authoritative hired-NPC state stored directly in entity persistent NBT.
 *
 * This deliberately avoids adding fields to every regional NPC class.
 * All GOTFactionNpc implementations can therefore participate in hiring
 * without duplicating ownership/squadron/command persistence code.
 */
public final class GOTHiredData {
    private static final String ROOT = "GOTHired";
    private static final String OWNER = "Owner";
    private static final String TASK = "Task";
    private static final String ORDER = "Order";
    private static final String SQUADRON = "Squadron";
    private static final String GUARD_X = "GuardX";
    private static final String GUARD_Y = "GuardY";
    private static final String GUARD_Z = "GuardZ";
    private static final String GUARD_RANGE = "GuardRange";
    private static final String TELEPORT = "Teleport";
    private static final String MOB_KILLS = "MobKills";
    private static final String XP = "XP";
    private static final String XP_LEVEL = "XPLevel";

    private static final int DEFAULT_GUARD_RANGE = 8;
    private static final int MIN_GUARD_RANGE = 1;
    private static final int MAX_GUARD_RANGE = 64;

    private GOTHiredData() {}

    private static CompoundTag data(Entity entity) {
        CompoundTag persistent = entity.getPersistentData();
        if (!persistent.contains(ROOT)) {
            CompoundTag tag = new CompoundTag();
            tag.putString(TASK, GOTHiredTask.WARRIOR.name());
            tag.putString(ORDER, GOTHiredOrder.FOLLOW.name());
            tag.putInt(GUARD_RANGE, DEFAULT_GUARD_RANGE);
            tag.putBoolean(TELEPORT, true);
            tag.putInt(XP_LEVEL, 1);
            persistent.put(ROOT, tag);
        }
        return persistent.getCompound(ROOT);
    }

    private static void save(Entity entity, CompoundTag tag) {
        entity.getPersistentData().put(ROOT, tag);
    }

    public static boolean isHired(Entity entity) {
        return owner(entity) != null;
    }

    @Nullable
    public static UUID owner(Entity entity) {
        CompoundTag tag = data(entity);
        return tag.hasUUID(OWNER) ? tag.getUUID(OWNER) : null;
    }

    public static boolean isOwner(Entity entity, UUID player) {
        UUID owner = owner(entity);
        return owner != null && owner.equals(player);
    }

    public static void hire(Entity entity, UUID player, GOTHiredTask task) {
        CompoundTag tag = data(entity);
        tag.putUUID(OWNER, player);
        tag.putString(TASK, task.name());
        tag.putString(ORDER, GOTHiredOrder.FOLLOW.name());
        tag.remove(SQUADRON);
        BlockPos pos = entity.blockPosition();
        tag.putInt(GUARD_X, pos.getX());
        tag.putInt(GUARD_Y, pos.getY());
        tag.putInt(GUARD_Z, pos.getZ());
        save(entity, tag);
        if (task == GOTHiredTask.WARRIOR && entity instanceof net.minecraft.world.entity.LivingEntity living) {
            got.npc.hiring.inventory.GOTHiredInventoryData.initializeWarriorEquipment(living);
        }
    }

    public static void dismiss(Entity entity) {
        CompoundTag tag = data(entity);
        tag.remove(OWNER);
        tag.remove(SQUADRON);
        tag.putString(ORDER, GOTHiredOrder.WANDER.name());
        save(entity, tag);
    }

    public static GOTHiredTask task(Entity entity) {
        try {
            return GOTHiredTask.valueOf(data(entity).getString(TASK));
        } catch (Exception ignored) {
            return GOTHiredTask.WARRIOR;
        }
    }

    public static GOTHiredOrder order(Entity entity) {
        try {
            return GOTHiredOrder.valueOf(data(entity).getString(ORDER));
        } catch (Exception ignored) {
            return GOTHiredOrder.FOLLOW;
        }
    }

    public static void setOrder(Entity entity, GOTHiredOrder order) {
        CompoundTag tag = data(entity);
        tag.putString(ORDER, order.name());
        if (order == GOTHiredOrder.HOLD) {
            BlockPos pos = entity.blockPosition();
            tag.putInt(GUARD_X, pos.getX());
            tag.putInt(GUARD_Y, pos.getY());
            tag.putInt(GUARD_Z, pos.getZ());
        }
        save(entity, tag);
    }

    public static String squadron(Entity entity) {
        return data(entity).getString(SQUADRON);
    }

    public static void setSquadron(Entity entity, @Nullable String squadron) {
        CompoundTag tag = data(entity);
        if (squadron == null || squadron.isBlank()) {
            tag.remove(SQUADRON);
        } else {
            tag.putString(SQUADRON, normalizeSquadron(squadron));
        }
        save(entity, tag);
    }

    public static String normalizeSquadron(String value) {
        String s = value.trim();
        if (s.length() > 32) s = s.substring(0, 32);
        return s;
    }

    public static BlockPos guardPoint(Entity entity) {
        CompoundTag tag = data(entity);
        if (!tag.contains(GUARD_X)) return entity.blockPosition();
        return new BlockPos(tag.getInt(GUARD_X), tag.getInt(GUARD_Y), tag.getInt(GUARD_Z));
    }

    public static int guardRange(Entity entity) {
        return Math.max(MIN_GUARD_RANGE, Math.min(MAX_GUARD_RANGE, data(entity).getInt(GUARD_RANGE)));
    }

    public static void setGuardRange(Entity entity, int value) {
        CompoundTag tag = data(entity);
        tag.putInt(GUARD_RANGE, Math.max(MIN_GUARD_RANGE, Math.min(MAX_GUARD_RANGE, value)));
        save(entity, tag);
    }

    public static boolean teleportAutomatically(Entity entity) {
        return data(entity).getBoolean(TELEPORT);
    }

    public static void setTeleportAutomatically(Entity entity, boolean value) {
        CompoundTag tag = data(entity);
        tag.putBoolean(TELEPORT, value);
        save(entity, tag);
    }

    public static int mobKills(Entity entity) {
        return data(entity).getInt(MOB_KILLS);
    }

    public static void addMobKill(Entity entity) {
        CompoundTag tag = data(entity);
        tag.putInt(MOB_KILLS, tag.getInt(MOB_KILLS) + 1);
        save(entity, tag);
    }

    public static int xp(Entity entity) {
        return data(entity).getInt(XP);
    }

    public static int xpLevel(Entity entity) {
        return Math.max(1, data(entity).getInt(XP_LEVEL));
    }

    public static int totalXPForLevel(int level) {
        if (level <= 1) return 0;
        return (int)Math.floor(3.0D * (level - 1) * Math.pow(1.08D, level - 2));
    }

    public static void addExperience(Entity entity, int amount) {
        if (amount <= 0) return;
        CompoundTag tag = data(entity);
        int xp = tag.getInt(XP) + amount;
        int level = Math.max(1, tag.getInt(XP_LEVEL));
        while (xp >= totalXPForLevel(level + 1)) {
            level++;
        }
        tag.putInt(XP, xp);
        tag.putInt(XP_LEVEL, level);
        save(entity, tag);
    }
}
