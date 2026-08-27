package got.special;

import net.minecraft.world.entity.*;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

/** Giant-thrown rock; legacy default damage was 10. */
public final class GOTThrownRockEntity extends ThrowableItemProjectile {
    public GOTThrownRockEntity(EntityType<? extends GOTThrownRockEntity> type, Level level) { super(type, level); }
    public GOTThrownRockEntity(EntityType<? extends GOTThrownRockEntity> type, Level level, LivingEntity owner) { super(type, owner, level); }
    @Override protected Item getDefaultItem() { return Items.COBBLESTONE; }
    @Override protected void onHitEntity(EntityHitResult hit) { super.onHitEntity(hit); Entity owner=getOwner(); hit.getEntity().hurt(damageSources().thrown(this, owner),10.0F); }
    @Override protected void onHit(HitResult hit) { super.onHit(hit); if(!level().isClientSide) discard(); }
}
