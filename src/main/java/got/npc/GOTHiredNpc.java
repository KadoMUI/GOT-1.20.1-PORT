package got.npc;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.player.Player;

import javax.annotation.Nullable;
import java.util.UUID;

/** Small shared ownership contract for directly hired mercenaries. */
public interface GOTHiredNpc {
    @Nullable UUID getHiredOwnerUUID();
    void setHiredOwner(Player owner);
    boolean isFollowingHiredOwner();
    void setFollowingHiredOwner(boolean following);

    default boolean isHired() {
        return getHiredOwnerUUID() != null;
    }

    default boolean isHiredOwner(Entity entity) {
        UUID owner = getHiredOwnerUUID();
        return owner != null && owner.equals(entity.getUUID());
    }

    default boolean isHiredAlly(Entity entity) {
        if (isHiredOwner(entity)) return true;
        if (!(entity instanceof GOTHiredNpc other)) return false;
        UUID owner = getHiredOwnerUUID();
        return owner != null && owner.equals(other.getHiredOwnerUUID());
    }

    /** Follow the owner and defend either side of the hireling-owner pair. */
    default void tickHiredBehavior(PathfinderMob mob) {
        UUID ownerId = getHiredOwnerUUID();
        if (ownerId == null || !(mob.level() instanceof ServerLevel serverLevel)) return;
        Player owner = serverLevel.getPlayerByUUID(ownerId);
        if (owner == null || owner.isSpectator() || !owner.isAlive()) return;

        LivingEntity threat = owner.getLastHurtByMob();
        if (threat == null || !threat.isAlive() || isHiredAlly(threat)) {
            threat = owner.getLastHurtMob();
        }
        if (threat != null && threat.isAlive() && !isHiredAlly(threat)
                && (mob.getTarget() == null || !mob.getTarget().isAlive())) {
            mob.setTarget(threat);
        }

        if (!isFollowingHiredOwner() || mob.getTarget() != null) return;
        double distance = mob.distanceToSqr(owner);
        if (distance > 1024.0D) {
            mob.teleportTo(owner.getX(), owner.getY(), owner.getZ());
            mob.getNavigation().stop();
        } else if (distance > 36.0D && serverLevel.getGameTime() % 10L == 0L) {
            mob.getNavigation().moveTo(owner, 1.25D);
        } else if (distance < 9.0D) {
            mob.getNavigation().stop();
        }
    }
}
