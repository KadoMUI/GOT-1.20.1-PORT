package got.npc;

import got.GOTEntities;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;

/** Visible projectile used by the legacy Northern hillman axe thrower. */
public final class GOTThrownAxeEntity extends ThrowableItemProjectile {
    public GOTThrownAxeEntity(EntityType<? extends GOTThrownAxeEntity> type, Level level) {
        super(type, level);
    }

    public GOTThrownAxeEntity(Level level, LivingEntity owner) {
        super(GOTEntities.THROWN_AXE.get(), owner, level);
        setItem(GOTNorthNpcLoadouts.stack("got:iron_throwing_axe"));
    }

    @Override
    protected Item getDefaultItem() {
        return GOTNorthNpcLoadouts.stack("got:iron_throwing_axe").getItem();
    }

    @Override
    protected void onHitEntity(EntityHitResult hit) {
        super.onHitEntity(hit);
        hit.getEntity().hurt(damageSources().thrown(this, getOwner()), 6.0F);
        discard();
    }

    @Override
    protected void onHitBlock(BlockHitResult hit) {
        super.onHitBlock(hit);
        if (!level().isClientSide) discard();
    }
}
