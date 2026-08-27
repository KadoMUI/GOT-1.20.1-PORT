package got.npc;

import got.GOTEntities;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;

/** Visible projectile used by the legacy Northern hillman axe thrower. */
public final class GOTThrownAxeEntity extends ThrowableItemProjectile {
    private float damage = 6.0F;
    public GOTThrownAxeEntity(EntityType<? extends GOTThrownAxeEntity> type, Level level) {
        super(type, level);
    }

    public GOTThrownAxeEntity(Level level, LivingEntity owner) {
        super(GOTEntities.THROWN_AXE.get(), owner, level);
        setItem(GOTNorthNpcLoadouts.stack("got:iron_throwing_axe"));
    }

    public GOTThrownAxeEntity(Level level, LivingEntity owner, ItemStack stack, float damage) {
        super(GOTEntities.THROWN_AXE.get(), owner, level);
        setItem(stack.copyWithCount(1));
        this.damage = damage;
    }

    @Override
    protected Item getDefaultItem() {
        return GOTNorthNpcLoadouts.stack("got:iron_throwing_axe").getItem();
    }

    @Override
    protected void onHitEntity(EntityHitResult hit) {
        super.onHitEntity(hit);
        if (!level().isClientSide) {
            hit.getEntity().hurt(damageSources().thrown(this, getOwner()), damage);
            dropAndDiscard();
        }
    }

    @Override
    protected void onHitBlock(BlockHitResult hit) {
        super.onHitBlock(hit);
        if (!level().isClientSide) dropAndDiscard();
    }

    private void dropAndDiscard() {
        ItemStack stack = getItem().copyWithCount(1);
        if (!stack.isEmpty()) spawnAtLocation(stack);
        discard();
    }

    @Override public void addAdditionalSaveData(CompoundTag tag) { super.addAdditionalSaveData(tag); tag.putFloat("LegacyDamage", damage); }
    @Override public void readAdditionalSaveData(CompoundTag tag) { super.readAdditionalSaveData(tag); if (tag.contains("LegacyDamage")) damage = tag.getFloat("LegacyDamage"); }
}

