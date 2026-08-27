package got.npc.hiring;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.LivingEntity;

import java.util.UUID;

/**
 * Shared hired-unit movement/target behavior.
 *
 * Call once from the server-side tick of a hireable GOT NPC.
 * This is deliberately conservative for Pass 1: FOLLOW and HOLD are active,
 * while PATROL receives its route implementation in the command-AI pass.
 */
public final class GOTHiredAi {
    private GOTHiredAi() {}

    public static void tick(PathfinderMob mob) {
        if (!(mob.level() instanceof ServerLevel level)) return;
        UUID ownerId = GOTHiredData.owner(mob);
        if (ownerId == null) return;

        ServerPlayer owner = level.getServer().getPlayerList().getPlayer(ownerId);

        // Owner-driven target sharing only requires the owner to be online.
        // HOLD/PATROL/WANDER must continue functioning if the owner logs out.
        if (owner != null && owner.isAlive()) {
            LivingEntity ownerTarget = owner.getLastHurtMob();
            if (ownerTarget == null || !ownerTarget.isAlive()) {
                ownerTarget = owner.getLastHurtByMob();
            }

            if (ownerTarget != null && ownerTarget.isAlive() && ownerTarget != mob) {
                if (mob.getTarget() == null || !mob.getTarget().isAlive()) {
                    mob.setTarget(ownerTarget);
                }
            }
        }

        if (mob.getTarget() != null && mob.getTarget().isAlive()) return;

        switch (GOTHiredData.order(mob)) {
            case FOLLOW -> { if (owner != null && owner.isAlive()) follow(mob, owner); else mob.getNavigation().stop(); }
            case HOLD -> hold(mob);
            case PATROL, WANDER -> { }
        }
    }

    private static void follow(PathfinderMob mob, ServerPlayer owner) {
        if (mob.getVehicle() instanceof got.mount.GOTMountEntity) return;
        double dist = mob.distanceToSqr(owner);

        if (dist > 1024.0D && GOTHiredData.teleportAutomatically(mob)) {
            GOTHiredMountController.teleportUnit(mob, owner.getX(), owner.getY(), owner.getZ());
            return;
        }

        if (dist > 36.0D && mob.tickCount % 10 == 0) {
            mob.getNavigation().moveTo(owner, 1.25D);
        } else if (dist < 9.0D) {
            mob.getNavigation().stop();
        }
    }

    private static void hold(PathfinderMob mob) {
        if (mob.getVehicle() instanceof got.mount.GOTMountEntity) return;
        BlockPos guard = GOTHiredData.guardPoint(mob);
        int range = GOTHiredData.guardRange(mob);
        double max = (double) range * (double) range;

        if (mob.blockPosition().distSqr(guard) > max && mob.tickCount % 10 == 0) {
            mob.getNavigation().moveTo(
                guard.getX() + 0.5D,
                guard.getY(),
                guard.getZ() + 0.5D,
                1.1D
            );
        }
    }
}
