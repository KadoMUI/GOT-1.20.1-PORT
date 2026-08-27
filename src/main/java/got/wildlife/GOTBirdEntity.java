package got.wildlife;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.animal.Parrot;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import javax.annotation.Nullable;
public class GOTBirdEntity extends Parrot {
 public enum BirdType { COMMON, CROW, MAGPIE, SOTHORYOS }
 private BirdType birdType=BirdType.COMMON; private int skin=0;
 public GOTBirdEntity(EntityType<? extends Parrot> t, Level l){super(t,l);}
 public BirdType getBirdType(){return birdType;} public int getSkin(){return skin;}
 @Override public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor l, net.minecraft.world.DifficultyInstance d, net.minecraft.world.entity.MobSpawnType r, @Nullable SpawnGroupData g, @Nullable CompoundTag tag){RandomSource x=l.getRandom(); birdType=BirdType.COMMON; skin=x.nextInt(9); return super.finalizeSpawn(l,d,r,g,tag);}
 public void setBirdType(BirdType t){birdType=t; skin=0;}
 @Override public void addAdditionalSaveData(CompoundTag n){super.addAdditionalSaveData(n);n.putString("GOTBirdType",birdType.name());n.putInt("GOTBirdSkin",skin);}
 @Override public void readAdditionalSaveData(CompoundTag n){super.readAdditionalSaveData(n);try{birdType=BirdType.valueOf(n.getString("GOTBirdType"));}catch(Exception e){birdType=BirdType.COMMON;}skin=n.getInt("GOTBirdSkin");}
}