package got.client.npc;

import got.npc.GOTNorthNpcEntity;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

/** Modern geometry port of the original GOTModelHuman chest and long headwear layer. */
public final class GOTNorthNpcModel extends HumanoidModel<GOTNorthNpcEntity> {
    private final ModelPart chest;

    public GOTNorthNpcModel(ModelPart root) {
        super(root);
        chest = body.getChild("north_chest");
    }

    public static LayerDefinition bodyLayer(float deformation) {
        MeshDefinition mesh = HumanoidModel.createMesh(new CubeDeformation(deformation), 0.0F);
        PartDefinition root = mesh.getRoot();
        // GOTModelHuman uses the lower half of the 64x64 skin for its long,
        // transparent hair/beard/hood layer.  This is deliberately 16 pixels
        // tall; using the vanilla hat UV (32, 0) makes the opaque face pixels
        // cover the whole cuboid and produces the giant heads seen in-game.
        root.addOrReplaceChild("hat", CubeListBuilder.create().texOffs(0, 32)
                        .addBox(-4.0F, -8.0F, -4.0F, 8.0F, 16.0F, 8.0F,
                                new CubeDeformation(0.5F + deformation)),
                PartPose.ZERO);
        root.getChild("body").addOrReplaceChild("north_chest",
                CubeListBuilder.create().texOffs(24, 0)
                        .addBox(-3.0F, 2.0F, -4.0F, 6.0F, 3.0F, 2.0F,
                                new CubeDeformation(deformation)),
                PartPose.ZERO);
        return LayerDefinition.create(mesh, 64, 64);
    }

    public static LayerDefinition armorLayer(float deformation) {
        return LayerDefinition.create(HumanoidModel.createMesh(new CubeDeformation(deformation), 0.0F), 64, 32);
    }

    @Override
    public void setupAnim(GOTNorthNpcEntity entity, float limbSwing, float limbSwingAmount,
                          float ageInTicks, float netHeadYaw, float headPitch) {
        rightArmPose = ArmPose.EMPTY;
        leftArmPose = ArmPose.EMPTY;
        if (entity.isAimingBow()) {
            if (entity.getMainArm() == HumanoidArm.RIGHT) {
                rightArmPose = ArmPose.BOW_AND_ARROW;
            } else {
                leftArmPose = ArmPose.BOW_AND_ARROW;
            }
        }
        super.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        chest.visible = entity.isFemale() && !entity.isBaby();
    }
}
