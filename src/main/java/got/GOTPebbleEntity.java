package got;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;

public final class GOTPebbleEntity extends ThrowableItemProjectile {
    public GOTPebbleEntity(EntityType<? extends GOTPebbleEntity> type, Level level) {
        super(type, level);
    }

    public GOTPebbleEntity(Level level, LivingEntity owner) {
        super(GOTEntities.PEBBLE_PROJECTILE.get(), owner, level);
    }

    @Override
    protected Item getDefaultItem() {
        return GOTItems.PEBBLE.get();
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        if (!level().isClientSide) {
            boolean hit = result.getEntity().hurt(
                    damageSources().thrown(this, getOwner()),
                    3.0F
            );
            if (hit
                    && result.getEntity() instanceof LivingEntity living
                    && !living.isAlive()
                    && getOwner() instanceof ServerPlayer player
                    && (living.getBbWidth() >= 1.4F || living.getBbHeight() >= 2.4F)) {
                got.achievement.GOTAchievementHooks.award(player, "KILL_LARGE_MOB_WITH_SLINGSHOT");
            }
            discard();
        }
    }
}
