package got;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;

public final class GOTLegacySpearEntity extends ThrowableItemProjectile {
    private float damage = 4.0F;

    public GOTLegacySpearEntity(EntityType<? extends GOTLegacySpearEntity> type, Level level) { super(type, level); }
    public GOTLegacySpearEntity(Level level, LivingEntity owner, ItemStack stack, float damage) {
        super(GOTEntities.SPEAR_PROJECTILE.get(), owner, level);
        setItem(stack.copyWithCount(1));
        this.damage = damage;
    }
    @Override protected Item getDefaultItem() { return GOTEquipment.IRON_SPEAR.get(); }

    @Override protected void onHitEntity(EntityHitResult hit) {
        super.onHitEntity(hit);
        if (!level().isClientSide) {
            hit.getEntity().hurt(damageSources().thrown(this, getOwner()), damage);
            dropAndDiscard();
        }
    }
    @Override protected void onHitBlock(BlockHitResult hit) {
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
