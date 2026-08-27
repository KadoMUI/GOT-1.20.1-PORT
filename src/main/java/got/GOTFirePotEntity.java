package got;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;

public final class GOTFirePotEntity extends ThrowableItemProjectile {
    public GOTFirePotEntity(EntityType<? extends GOTFirePotEntity> type, Level level) { super(type, level); }
    public GOTFirePotEntity(Level level, LivingEntity owner) { super(GOTEntities.FIRE_POT_PROJECTILE.get(), owner, level); }
    @Override protected Item getDefaultItem() { return GOTEquipment.FIRE_POT.get(); }
    @Override protected float getGravity() { return 0.04F; }

    @Override protected void onHit(HitResult hit) {
        super.onHit(hit);
        if (!level().isClientSide) {
            var direct = hit.getType() == HitResult.Type.ENTITY ? ((net.minecraft.world.phys.EntityHitResult) hit).getEntity() : null;
            double r = 3.0D;
            for (LivingEntity target : level().getEntitiesOfClass(LivingEntity.class, getBoundingBox().inflate(r))) {
                float damage = target == direct ? 3.0F : 1.0F;
                if (target.hurt(damageSources().thrown(this, getOwner()), damage)) {
                    int fire = 2 + random.nextInt(3);
                    if (target == direct) fire += 2 + random.nextInt(3);
                    target.setSecondsOnFire(fire);
                }
            }
            if (hit.getType() == HitResult.Type.BLOCK) {
                var pos = ((net.minecraft.world.phys.BlockHitResult)hit).getBlockPos();
                if (level().getBlockState(pos).getBlock() instanceof GOTWildFireJarBlock jar) {
                    jar.explode(level(), pos);
                }
            }
            discard();
        }
        for (int i=0;i<16;i++) level().addParticle(random.nextBoolean()? ParticleTypes.FLAME:ParticleTypes.SMOKE, getX(),getY(),getZ(), (random.nextDouble()-.5)*.2,.2+random.nextDouble()*.1,(random.nextDouble()-.5)*.2);
    }
}
