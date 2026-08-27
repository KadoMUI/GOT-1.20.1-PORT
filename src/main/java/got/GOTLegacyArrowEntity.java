package got;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;

/** One projectile type for fire arrows, poisoned arrows and crossbow bolts. */
public final class GOTLegacyArrowEntity extends Arrow {
    public enum Kind { FIRE_ARROW, POISON_ARROW, BOLT, POISON_BOLT }
    private Kind kind = Kind.FIRE_ARROW;
    private ItemStack pickup = ItemStack.EMPTY;

    public GOTLegacyArrowEntity(EntityType<? extends GOTLegacyArrowEntity> type, Level level) { super(type, level); }
    public GOTLegacyArrowEntity(Level level, LivingEntity owner, ItemStack stack, Kind kind) {
        super(GOTEntities.LEGACY_ARROW_PROJECTILE.get(), level);
        setOwner(owner);
        setPos(owner.getX(), owner.getEyeY() - 0.1D, owner.getZ());
        this.kind = kind;
        this.pickup = stack.copyWithCount(1);
        setBaseDamage(kind == Kind.BOLT || kind == Kind.POISON_BOLT ? 4.0D : 2.0D);
        if (kind == Kind.FIRE_ARROW) setSecondsOnFire(100);
    }

    @Override
    protected void onHitEntity(EntityHitResult hit) {
        super.onHitEntity(hit);
        if (hit.getEntity() instanceof LivingEntity living && !level().isClientSide) {
            if (kind == Kind.POISON_ARROW || kind == Kind.POISON_BOLT) GOTLegacyCombatEffects.applyStandardPoison(living);
            if (kind == Kind.FIRE_ARROW) GOTLegacyCombatEffects.applyStandardFire(living);
        }
    }

    @Override protected ItemStack getPickupItem() { return pickup.isEmpty() ? new ItemStack(GOTEquipment.ARROW_FIRE.get()) : pickup.copy(); }
    @Override public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putString("GOTLegacyArrowKind", kind.name());
        if (!pickup.isEmpty()) tag.put("GOTLegacyPickup", pickup.save(new CompoundTag()));
    }
    @Override public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains("GOTLegacyArrowKind")) {
            try { kind = Kind.valueOf(tag.getString("GOTLegacyArrowKind")); } catch (IllegalArgumentException ignored) {}
        }
        if (tag.contains("GOTLegacyPickup")) pickup = ItemStack.of(tag.getCompound("GOTLegacyPickup"));
        setBaseDamage(kind == Kind.BOLT || kind == Kind.POISON_BOLT ? 4.0D : 2.0D);
    }
}
