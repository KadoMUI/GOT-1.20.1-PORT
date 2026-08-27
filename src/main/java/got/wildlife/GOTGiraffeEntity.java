package got.wildlife;
import got.mount.GOTMountEntity;
import net.minecraft.world.entity.*; import net.minecraft.world.entity.ai.attributes.*; import net.minecraft.world.level.Level;
public class GOTGiraffeEntity extends GOTMountEntity {
 public GOTGiraffeEntity(EntityType<? extends net.minecraft.world.entity.animal.horse.Horse> t, Level l){super(t,l);xpReward=5;}
 public static AttributeSupplier.Builder createAttributes(){return GOTMountEntity.createGOTMountAttributes().add(Attributes.MAX_HEALTH,30).add(Attributes.MOVEMENT_SPEED,.24).add(Attributes.JUMP_STRENGTH,.55);}
}