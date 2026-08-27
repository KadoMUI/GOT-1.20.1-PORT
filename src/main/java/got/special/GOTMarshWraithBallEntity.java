package got.special;

import net.minecraft.world.effect.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.*;

/** Legacy Marsh Wraith ball: no gravity, 5 damage, five seconds of slowness, 200 tick lifetime. */
public final class GOTMarshWraithBallEntity extends ThrowableProjectile {
    private LivingEntity target;
    public GOTMarshWraithBallEntity(EntityType<? extends GOTMarshWraithBallEntity> type,Level level){super(type,level);}
    public GOTMarshWraithBallEntity(EntityType<? extends GOTMarshWraithBallEntity> type,Level level,LivingEntity owner,LivingEntity target){
        super(type,owner,level);this.target=target;setPos(owner.getX(),owner.getEyeY()-.1,owner.getZ());shootAt(target);
    }
    private void shootAt(LivingEntity t){Vec3 d=new Vec3(t.getX()-getX(),t.getY()+t.getBbHeight()/2-getY(),t.getZ()-getZ()).normalize().scale(.5);setDeltaMovement(d);}
    @Override protected float getGravity(){return 0F;}
    @Override public void tick(){super.tick();if(tickCount>=200&&!level().isClientSide)discard();}
    @Override protected void onHit(HitResult hit){
        super.onHit(hit); if(level().isClientSide)return;
        if(hit instanceof EntityHitResult ehr && ehr.getEntity()==target){
            Entity owner=getOwner(); if(ehr.getEntity().hurt(damageSources().mobProjectile(this, owner instanceof LivingEntity l?l:null),5F) && ehr.getEntity() instanceof LivingEntity living)
                living.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN,100,0),owner);
        }
        discard();
    }
    @Override protected void defineSynchedData(){}
}
