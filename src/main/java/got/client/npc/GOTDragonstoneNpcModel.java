package got.client.npc;

import got.npc.GOTDragonstoneNpcEntity;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.world.entity.HumanoidArm;

/** Original GOT human body geometry shared by ordinary and named Westerlanders. */
public final class GOTDragonstoneNpcModel extends HumanoidModel<GOTDragonstoneNpcEntity> {
    private final ModelPart chest;

    public GOTDragonstoneNpcModel(ModelPart root) {
        super(root);
        chest = body.getChild("dragonstone_chest");
    }

    public static LayerDefinition bodyLayer(float deformation) {
        MeshDefinition mesh = HumanoidModel.createMesh(new CubeDeformation(deformation), 0.0F);
        PartDefinition root = mesh.getRoot();
        root.addOrReplaceChild("hat", CubeListBuilder.create().texOffs(0, 32)
                        .addBox(-4.0F, -8.0F, -4.0F, 8.0F, 16.0F, 8.0F,
                                new CubeDeformation(0.5F + deformation)),
                PartPose.ZERO);
        root.getChild("body").addOrReplaceChild("dragonstone_chest",
                CubeListBuilder.create().texOffs(24, 0)
                        .addBox(-3.0F, 2.0F, -4.0F, 6.0F, 3.0F, 2.0F,
                                new CubeDeformation(deformation)),
                PartPose.ZERO);
        return LayerDefinition.create(mesh, 64, 64);
    }

    public static LayerDefinition armorLayer(float deformation) {
        return LayerDefinition.create(
                HumanoidModel.createMesh(new CubeDeformation(deformation), 0.0F), 64, 32);
    }

    @Override
    public void setupAnim(GOTDragonstoneNpcEntity entity, float limbSwing, float limbSwingAmount,
                          float ageInTicks, float netHeadYaw, float headPitch) {
        rightArmPose = ArmPose.EMPTY;
        leftArmPose = ArmPose.EMPTY;
        if (entity.isAimingBow()) {
            if (entity.getMainArm() == HumanoidArm.RIGHT) rightArmPose = ArmPose.BOW_AND_ARROW;
            else leftArmPose = ArmPose.BOW_AND_ARROW;
        }
        super.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        chest.visible = entity.isFemale() && !entity.isBaby();
    }
}
