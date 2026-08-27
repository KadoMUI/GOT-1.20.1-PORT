package got.wildlife;
import got.mount.GOTMountEntity;
import net.minecraft.world.entity.*; import net.minecraft.world.entity.ai.attributes.*; import net.minecraft.world.level.Level;
public class GOTMammothEntity extends GOTMountEntity {
 public GOTMammothEntity(EntityType<? extends net.minecraft.world.entity.animal.horse.Horse> t, Level l){super(t,l);xpReward=10;}
 public static AttributeSupplier.Builder createAttributes(){return GOTMountEntity.createGOTMountAttributes().add(Attributes.MAX_HEALTH,60).add(Attributes.MOVEMENT_SPEED,.16).add(Attributes.ATTACK_DAMAGE,7).add(Attributes.JUMP_STRENGTH,.40);}
 @Override protected double clampHealth(double v){return net.minecraft.util.Mth.clamp(v,48.0D,80.0D);}
 @Override protected boolean hasMountAttack(){return true;} @Override protected double mountAttackReachSqr(){return 13.0;} @Override protected int mountAttackInterval(){return 26;}
 @Override protected void dropCustomDeathLoot(net.minecraft.world.damagesource.DamageSource s,int looting,boolean hit){super.dropCustomDeathLoot(s,looting,hit);for(int i=0;i<3+random.nextInt(5)+random.nextInt(looting+1);i++)spawnAtLocation(got.GOTItems.FUR.get());for(int i=0;i<2+random.nextInt(4)+random.nextInt(looting+1);i++)spawnAtLocation(isOnFire()?got.GOTItems.MUTTON_COOKED.get():got.GOTItems.MUTTON_RAW.get());}
 @Override public boolean canFreeze(){return false;}
 @Override public boolean doHurtTarget(Entity e){boolean h=super.doHurtTarget(e); if(h){double dx=e.getX()-getX(),dz=e.getZ()-getZ(),d=Math.max(.001,Math.sqrt(dx*dx+dz*dz));e.push(dx/d*.8,.32,dz/d*.8);}return h;}
}