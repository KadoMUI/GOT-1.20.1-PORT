package got.npc.hiring.ai;

import got.npc.hiring.GOTHiredData;
import got.npc.hiring.GOTHiredOrder;
import got.npc.hiring.command.GOTHiredCommandState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.PathfinderMob;

import java.util.List;

/**
 * Patrol movement implementation for Pass 6.
 */
public final class GOTHiredPatrolAi {
    private GOTHiredPatrolAi() {}

    /**
     * @return true if patrol logic consumed normal hired movement this tick.
     */
    public static boolean tick(PathfinderMob mob) {
        if (GOTHiredData.order(mob) != GOTHiredOrder.PATROL) return false;
        if (GOTHiredCommandState.halted(mob)) return true;

        List<BlockPos> points = GOTHiredPatrolData.points(mob);
        if (points.isEmpty()) {
            // A PATROL without explicit points behaves like a local guard patrol
            // around the order position, rather than wandering away forever.
            BlockPos guard = GOTHiredData.guardPoint(mob);
            if (mob.blockPosition().distSqr(guard) > 16.0D && mob.tickCount % 10 == 0) {
                mob.getNavigation().moveTo(
                    guard.getX() + 0.5D,
                    guard.getY(),
                    guard.getZ() + 0.5D,
                    1.05D
                );
            }
            return true;
        }

        if (mob.getTarget() != null && mob.getTarget().isAlive()) return true;

        BlockPos target = GOTHiredPatrolData.current(mob);
        double distanceSq = mob.blockPosition().distSqr(target);

        if (distanceSq <= 4.0D) {
            if (mob.tickCount % 20 == 0) {
                GOTHiredPatrolData.advance(mob);
            }
            return true;
        }

        if (mob.tickCount % 10 == 0) {
            mob.getNavigation().moveTo(
                target.getX() + 0.5D,
                target.getY(),
                target.getZ() + 0.5D,
                1.05D
            );
        }
        return true;
    }
}
