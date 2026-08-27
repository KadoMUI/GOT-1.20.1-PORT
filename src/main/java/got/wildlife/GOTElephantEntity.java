package got.wildlife;
import got.mount.GOTMountEntity;
import net.minecraft.world.entity.*; import net.minecraft.world.entity.ai.attributes.*; import net.minecraft.world.level.Level;
public class GOTElephantEntity extends GOTMountEntity {
 public GOTElephantEntity(EntityType<? extends net.minecraft.world.entity.animal.horse.Horse> t, Level l){super(t,l);xpReward=8;}
 public static AttributeSupplier.Builder createAttributes(){return GOTMountEntity.createGOTMountAttributes().add(Attributes.MAX_HEALTH,50).add(Attributes.MOVEMENT_SPEED,.18).add(Attributes.ATTACK_DAMAGE,5).add(Attributes.JUMP_STRENGTH,.45);}
 @Override protected double clampHealth(double v){return net.minecraft.util.Mth.clamp(v,40.0D,70.0D);}
 @Override protected boolean hasMountAttack(){return true;} @Override protected double mountAttackReachSqr(){return 12.0;} @Override protected int mountAttackInterval(){return 24;}
 @Override protected void dropCustomDeathLoot(net.minecraft.world.damagesource.DamageSource s,int looting,boolean hit){super.dropCustomDeathLoot(s,looting,hit);for(int i=0;i<2+random.nextInt(4)+random.nextInt(looting+1);i++)spawnAtLocation(isOnFire()?got.GOTItems.ELEPHANT_COOKED.get():got.GOTItems.ELEPHANT_RAW.get());}
 @Override public boolean doHurtTarget(Entity e){boolean h=super.doHurtTarget(e); if(h){double dx=e.getX()-getX(),dz=e.getZ()-getZ(),d=Math.max(.001,Math.sqrt(dx*dx+dz*dz));e.push(dx/d*.65,.28,dz/d*.65);}return h;}
}