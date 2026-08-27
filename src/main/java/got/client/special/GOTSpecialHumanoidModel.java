package got.client.special;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.world.entity.Mob;
public final class GOTSpecialHumanoidModel<T extends Mob> extends HumanoidModel<T> {
 public GOTSpecialHumanoidModel(ModelPart root){super(root);}
 public static LayerDefinition stoneLayer(){return LayerDefinition.create(HumanoidModel.createMesh(CubeDeformation.NONE,0F),64,64);}
 public static LayerDefinition werewolfLayer(){return LayerDefinition.create(HumanoidModel.createMesh(new CubeDeformation(0.15F),0F),128,256);}
}
