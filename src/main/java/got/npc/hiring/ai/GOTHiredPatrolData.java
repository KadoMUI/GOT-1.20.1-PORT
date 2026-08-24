package got.npc.hiring.ai;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.Entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Persistent patrol route for hired NPCs.
 *
 * Route points are stored on the entity, matching the legacy per-NPC hired state
 * model. The current route index also persists across reloads.
 */
public final class GOTHiredPatrolData {
    private static final String ROOT = "GOTHiredPatrol";
    private static final String POINTS = "Points";
    private static final String INDEX = "Index";
    private static final String LOOP = "Loop";
    private static final String REVERSE = "Reverse";

    private GOTHiredPatrolData() {}

    private static CompoundTag data(Entity entity) {
        CompoundTag persistent = entity.getPersistentData();
        if (!persistent.contains(ROOT)) {
            CompoundTag tag = new CompoundTag();
            tag.putBoolean(LOOP, true);
            tag.putBoolean(REVERSE, false);
            persistent.put(ROOT, tag);
        }
        return persistent.getCompound(ROOT);
    }

    private static void save(Entity entity, CompoundTag tag) {
        entity.getPersistentData().put(ROOT, tag);
    }

    public static List<BlockPos> points(Entity entity) {
        CompoundTag tag = data(entity);
        ListTag list = tag.getList(POINTS, Tag.TAG_COMPOUND);
        List<BlockPos> out = new ArrayList<>(list.size());
        for (int i = 0; i < list.size(); i++) {
            CompoundTag p = list.getCompound(i);
            out.add(new BlockPos(p.getInt("X"), p.getInt("Y"), p.getInt("Z")));
        }
        return out;
    }

    public static void setPoints(Entity entity, List<BlockPos> points) {
        CompoundTag tag = data(entity);
        ListTag list = new ListTag();
        for (BlockPos pos : points) {
            CompoundTag p = new CompoundTag();
            p.putInt("X", pos.getX());
            p.putInt("Y", pos.getY());
            p.putInt("Z", pos.getZ());
            list.add(p);
        }
        tag.put(POINTS, list);
        tag.putInt(INDEX, 0);
        tag.putBoolean(REVERSE, false);
        save(entity, tag);
    }

    public static void addPoint(Entity entity, BlockPos point) {
        List<BlockPos> points = points(entity);
        points.add(point);
        setPoints(entity, points);
    }

    public static void clear(Entity entity) {
        CompoundTag tag = data(entity);
        tag.remove(POINTS);
        tag.putInt(INDEX, 0);
        tag.putBoolean(REVERSE, false);
        save(entity, tag);
    }

    public static boolean isLooping(Entity entity) {
        return data(entity).getBoolean(LOOP);
    }

    public static void setLooping(Entity entity, boolean looping) {
        CompoundTag tag = data(entity);
        tag.putBoolean(LOOP, looping);
        save(entity, tag);
    }

    public static int index(Entity entity) {
        List<BlockPos> points = points(entity);
        if (points.isEmpty()) return 0;
        return Math.max(0, Math.min(points.size() - 1, data(entity).getInt(INDEX)));
    }

    public static BlockPos current(Entity entity) {
        List<BlockPos> points = points(entity);
        return points.isEmpty() ? entity.blockPosition() : points.get(index(entity));
    }

    public static void advance(Entity entity) {
        List<BlockPos> points = points(entity);
        if (points.size() <= 1) return;

        CompoundTag tag = data(entity);
        int idx = index(entity);
        boolean reverse = tag.getBoolean(REVERSE);

        if (tag.getBoolean(LOOP)) {
            idx = (idx + 1) % points.size();
        } else {
            if (!reverse) {
                idx++;
                if (idx >= points.size()) {
                    idx = points.size() - 2;
                    reverse = true;
                }
            } else {
                idx--;
                if (idx < 0) {
                    idx = 1;
                    reverse = false;
                }
            }
        }

        tag.putInt(INDEX, idx);
        tag.putBoolean(REVERSE, reverse);
        save(entity, tag);
    }
}
