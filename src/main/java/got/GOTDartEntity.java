package got;
import net.minecraft.nbt.CompoundTag;import net.minecraft.world.effect.MobEffectInstance;import net.minecraft.world.effect.MobEffects;import net.minecraft.world.entity.EntityType;import net.minecraft.world.entity.LivingEntity;import net.minecraft.world.entity.projectile.ThrowableItemProjectile;import net.minecraft.world.item.Item;import net.minecraft.world.item.ItemStack;import net.minecraft.world.level.Level;import net.minecraft.world.phys.EntityHitResult;
public final class GOTDartEntity extends ThrowableItemProjectile{
 private boolean poisoned;private float damageFactor=1F;
 public GOTDartEntity(EntityType<? extends GOTDartEntity> t,Level l){super(t,l);}public GOTDartEntity(Level l,LivingEntity o,boolean poison){super(GOTEntities.DART_PROJECTILE.get(),o,l);this.poisoned=poison;setItem(new ItemStack(poison?GOTEquipment.DART_POISONED.get():GOTEquipment.DART.get()));}
 protected Item getDefaultItem(){return poisoned?GOTEquipment.DART_POISONED.get():GOTEquipment.DART.get();}
 protected void onHitEntity(EntityHitResult r){super.onHitEntity(r);float speed=(float)getDeltaMovement().length();r.getEntity().hurt(damageSources().thrown(this,getOwner()),Math.max(1F,speed*3F*damageFactor));if(poisoned&&r.getEntity() instanceof LivingEntity le)le.addEffect(new MobEffectInstance(MobEffects.POISON,100,0));discard();}
 public void addAdditionalSaveData(CompoundTag t){super.addAdditionalSaveData(t);t.putBoolean("Poisoned",poisoned);t.putFloat("DartDamage",damageFactor);}public void readAdditionalSaveData(CompoundTag t){super.readAdditionalSaveData(t);poisoned=t.getBoolean("Poisoned");if(t.contains("DartDamage"))damageFactor=t.getFloat("DartDamage");setItem(new ItemStack(poisoned?GOTEquipment.DART_POISONED.get():GOTEquipment.DART.get()));}
}
