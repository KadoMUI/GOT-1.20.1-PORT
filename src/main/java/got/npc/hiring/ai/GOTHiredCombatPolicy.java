package got.npc.hiring.ai;

import got.npc.hiring.GOTHiredData;
import got.npc.hiring.GOTHiredOrder;
import got.npc.hiring.command.GOTHiredCommandState;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;

/**
 * Shared rules for hired-unit combat and movement boundaries.
 *
 * HOLD:
 * - may fight inside guard radius
 * - must not chase indefinitely outside the guard area
 *
 * PATROL:
 * - may fight near the current patrol segment/point
 *
 * HALT:
 * - no movement
 * - no autonomous target
 *
 * FOLLOW:
 * - may defend owner and self normally
 */
public final class GOTHiredCombatPolicy {
    private GOTHiredCombatPolicy() {}

    public static boolean mayAcquireTarget(PathfinderMob mob, LivingEntity target) {
        if (!target.isAlive()) return false;
        if (GOTHiredCommandState.halted(mob)) return false;

        return switch (GOTHiredData.order(mob)) {
            case HOLD -> insideGuardArea(mob, target.blockPosition(), 4);
            case PATROL -> insidePatrolArea(mob, target.blockPosition(), 8);
            case FOLLOW, WANDER -> true;
        };
    }

    public static boolean shouldDropCurrentTarget(PathfinderMob mob) {
        LivingEntity target = mob.getTarget();
        if (target == null) return false;
        if (!target.isAlive()) return true;
        if (GOTHiredCommandState.halted(mob)) return true;

        return switch (GOTHiredData.order(mob)) {
            case HOLD -> !insideGuardArea(mob, target.blockPosition(), 6);
            case PATROL -> !insidePatrolArea(mob, target.blockPosition(), 12);
            case FOLLOW, WANDER -> false;
        };
    }

    private static boolean insideGuardArea(PathfinderMob mob, BlockPos target, int padding) {
        BlockPos guard = GOTHiredData.guardPoint(mob);
        int radius = GOTHiredData.guardRange(mob) + padding;
        return guard.distSqr(target) <= (double) radius * radius;
    }

    private static boolean insidePatrolArea(PathfinderMob mob, BlockPos target, int padding) {
        BlockPos patrol = GOTHiredPatrolData.current(mob);
        int radius = Math.max(8, GOTHiredData.guardRange(mob)) + padding;
        return patrol.distSqr(target) <= (double) radius * radius;
    }
}
