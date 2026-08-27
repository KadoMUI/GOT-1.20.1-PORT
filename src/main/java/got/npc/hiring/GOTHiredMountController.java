package got.npc.hiring;

import got.mount.GOTMountEntity;
import got.npc.hiring.ai.GOTHiredPatrolData;
import got.npc.hiring.command.GOTHiredCommandState;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;

import java.util.UUID;

/**
 * Movement bridge for mounted hired units.
 *
 * A passenger Mob's own navigation cannot move its horse, so FOLLOW/HOLD/PATROL
 * must be applied to the GOT mount itself.  This also centralizes mounted
 * teleports so Command Horn / long-distance follow never strand a horse.
 */
public final class GOTHiredMountController {
    private GOTHiredMountController() {}

    /** @return true when hired-order movement owns this mount for the tick. */
    public static boolean tick(GOTMountEntity mount, Mob rider) {
        if (!GOTHiredData.isHired(rider)) return false;
        if (!(rider.level() instanceof ServerLevel level)) return true;

        if (rider instanceof PathfinderMob pathfinder && GOTHiredCommandState.halted(pathfinder)) {
            mount.getNavigation().stop();
            return true;
        }

        // Combat steering remains the mount's normal responsibility.
        if (rider.getTarget() != null && rider.getTarget().isAlive()) return false;

        return switch (GOTHiredData.order(rider)) {
            case FOLLOW -> {
                UUID ownerId = GOTHiredData.owner(rider);
                ServerPlayer owner = ownerId == null ? null : level.getServer().getPlayerList().getPlayer(ownerId);
                if (owner == null || !owner.isAlive() || owner.level() != level) {
                    mount.getNavigation().stop();
                    yield true;
                }
                double d = mount.distanceToSqr(owner);
                if (d > 1024.0D && GOTHiredData.teleportAutomatically(rider)) {
                    teleportUnit(rider, owner.getX(), owner.getY(), owner.getZ());
                } else if (d > 36.0D && mount.tickCount % 10 == 0) {
                    mount.getNavigation().moveTo(owner, 1.25D);
                } else if (d < 9.0D) {
                    mount.getNavigation().stop();
                }
                yield true;
            }
            case HOLD -> {
                moveToArea(mount, GOTHiredData.guardPoint(rider), GOTHiredData.guardRange(rider), 1.10D);
                yield true;
            }
            case PATROL -> {
                var points = GOTHiredPatrolData.points(rider);
                if (points.isEmpty()) {
                    // Match foot-unit patrol fallback: remain in a local circuit
                    // around the stored order/guard position.
                    moveToArea(mount, GOTHiredData.guardPoint(rider), 4, 1.05D);
                    yield true;
                }
                BlockPos point = GOTHiredPatrolData.current(rider);
                if (mount.blockPosition().distSqr(point) <= 4.0D) {
                    if (mount.tickCount % 20 == 0) GOTHiredPatrolData.advance(rider);
                    mount.getNavigation().stop();
                } else if (mount.tickCount % 10 == 0) {
                    mount.getNavigation().moveTo(point.getX()+0.5D, point.getY(), point.getZ()+0.5D, 1.05D);
                }
                yield true;
            }
            case WANDER -> {
                mount.getNavigation().stop();
                yield true;
            }
        };
    }

    private static void moveToArea(GOTMountEntity mount, BlockPos center, int radius, double speed) {
        if (mount.blockPosition().distSqr(center) > (double)radius * radius && mount.tickCount % 10 == 0) {
            mount.getNavigation().moveTo(center.getX()+0.5D, center.getY(), center.getZ()+0.5D, speed);
        } else if (mount.blockPosition().distSqr(center) <= 4.0D) {
            mount.getNavigation().stop();
        }
    }

    /** Moves a hired unit as one rider+mount object when possible. */
    public static void teleportUnit(Mob rider, double x, double y, double z) {
        if (rider.getVehicle() instanceof GOTMountEntity mount) {
            mount.teleportTo(x, y, z);
            mount.getNavigation().stop();
        } else {
            rider.teleportTo(x, y, z);
            rider.getNavigation().stop();
        }
    }
}
