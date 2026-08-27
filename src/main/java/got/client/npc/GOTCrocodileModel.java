package got.client.npc;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import got.npc.GOTCrocodileEntity;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;

/** 1.20.1 reconstruction of the legacy GOTModelCrocodile 128x128 geometry. */
public final class GOTCrocodileModel extends EntityModel<GOTCrocodileEntity> {
    private final ModelPart root;
    private final ModelPart head;
    private final ModelPart jaw;
    private final ModelPart tail1;
    private final ModelPart tail2;
    private final ModelPart tail3;
    private final ModelPart frontLeft;
    private final ModelPart frontRight;
    private final ModelPart backLeft;
    private final ModelPart backRight;

    public GOTCrocodileModel(ModelPart root) {
        this.root = root;
        head = root.getChild("head"); jaw = root.getChild("jaw");
        tail1 = root.getChild("tail1"); tail2 = root.getChild("tail2"); tail3 = root.getChild("tail3");
        frontLeft = root.getChild("front_left"); frontRight = root.getChild("front_right");
        backLeft = root.getChild("back_left"); backRight = root.getChild("back_right");
    }

    public static LayerDefinition layer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        root.addOrReplaceChild("body", CubeListBuilder.create().texOffs(18,83).addBox(-8,-5,0,16,9,36), PartPose.offset(0,17,-16));
        root.addOrReplaceChild("tail1", CubeListBuilder.create().texOffs(0,28).addBox(-7,0,0,14,7,19), PartPose.offset(0,13,18));
        root.addOrReplaceChild("tail2", CubeListBuilder.create().texOffs(0,55).addBox(-6,1.5F,17,12,5,16), PartPose.offset(0,13,18));
        root.addOrReplaceChild("tail3", CubeListBuilder.create().texOffs(0,77).addBox(-5,3,31,10,3,14), PartPose.offset(0,13,18));
        root.addOrReplaceChild("jaw", CubeListBuilder.create().texOffs(58,18).addBox(-6.5F,0.3F,-19,13,4,19), PartPose.offset(0,17,-16));
        root.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0,0).addBox(-7.5F,-6,-21,15,6,21), PartPose.offset(0,18.5F,-16));
        root.addOrReplaceChild("front_left", CubeListBuilder.create().texOffs(2,104).addBox(0,0,-3,16,3,6), PartPose.offset(6,15,-11));
        root.addOrReplaceChild("back_left", CubeListBuilder.create().texOffs(2,104).addBox(0,0,-3,16,3,6), PartPose.offset(6,15,15));
        root.addOrReplaceChild("front_right", CubeListBuilder.create().texOffs(2,104).mirror().addBox(-16,0,-3,16,3,6), PartPose.offset(-6,15,-11));
        root.addOrReplaceChild("back_right", CubeListBuilder.create().texOffs(2,104).mirror().addBox(-16,0,-3,16,3,6), PartPose.offset(-6,15,15));
        root.addOrReplaceChild("spines", CubeListBuilder.create().texOffs(46,45).addBox(-5,0,0,10,4,32), PartPose.offsetAndRotation(0,9.5F,-14,-0.034906585F,0,0));
        return LayerDefinition.create(mesh,128,128);
    }

    @Override
    public void setupAnim(GOTCrocodileEntity entity, float limbSwing, float limbSwingAmount,
                          float ageInTicks, float netHeadYaw, float headPitch) {
        float walk = Mth.cos(limbSwing * 0.6662F) * limbSwingAmount;
        frontLeft.yRot = walk; frontRight.yRot = walk;
        backLeft.yRot = walk; backRight.yRot = walk;
        tail1.yRot = walk * 0.5F; tail2.yRot = walk * 0.5625F; tail3.yRot = walk * 0.59375F;
        head.xRot = headPitch * Mth.DEG_TO_RAD * 0.3F;
        float snap = entity.getSnapProgress(0.0F);
        jaw.xRot = 0.15F + snap * 0.45F;
        head.xRot -= snap * 0.12F;
        frontLeft.zRot = backLeft.zRot = 0.43633232F;
        frontRight.zRot = backRight.zRot = -0.43633232F;
    }

    @Override
    public void renderToBuffer(PoseStack pose, VertexConsumer consumer, int light, int overlay,
                               float red, float green, float blue, float alpha) {
        root.render(pose, consumer, light, overlay, red, green, blue, alpha);
    }
}
