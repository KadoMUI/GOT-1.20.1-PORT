package got.client.special;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Mob;

/** 1.20.1 recreation of the legacy 128x128 GOT giant model/UV layout. */
public final class GOTGiantModel<T extends Mob> extends EntityModel<T> {
    private final ModelPart root, head, body, rightArm, leftArm, rightLeg, leftLeg;

    public GOTGiantModel(ModelPart root) {
        this.root = root;
        this.head = root.getChild("head");
        this.body = root.getChild("body");
        this.rightArm = root.getChild("right_arm");
        this.leftArm = root.getChild("left_arm");
        this.rightLeg = root.getChild("right_leg");
        this.leftLeg = root.getChild("left_leg");
    }

    public static LayerDefinition layer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        PartDefinition head = root.addOrReplaceChild("head", CubeListBuilder.create()
                .texOffs(0,0).addBox(-6,-6,-12,12,12,12)
                .texOffs(0,0).addBox(-1,-1,-14,2,3,2), PartPose.offset(0,-27,-6));
        head.addOrReplaceChild("right_ear", CubeListBuilder.create().texOffs(40,0).addBox(0,0,0,1,4,3), PartPose.offset(6,-2,-8));
        head.addOrReplaceChild("left_ear", CubeListBuilder.create().texOffs(40,0).mirror().addBox(-1,0,0,1,4,3), PartPose.offset(-6,-2,-8));
        root.addOrReplaceChild("body", CubeListBuilder.create().texOffs(48,0)
                .addBox(-12,-28,-8,24,28,16), PartPose.ZERO);
        root.addOrReplaceChild("right_arm", CubeListBuilder.create().mirror()
                .texOffs(0,24).addBox(-12,-3,-6,12,12,12)
                .texOffs(0,48).addBox(-11,9,-5,10,20,10), PartPose.offset(-12,-23,0));
        root.addOrReplaceChild("left_arm", CubeListBuilder.create()
                .texOffs(0,24).addBox(0,-3,-6,12,12,12)
                .texOffs(0,48).addBox(1,9,-5,10,20,10), PartPose.offset(12,-23,0));
        root.addOrReplaceChild("right_leg", CubeListBuilder.create().mirror()
                .texOffs(0,78).addBox(-6,0,-6,11,12,12)
                .texOffs(0,102).addBox(-5.5F,12,-5,10,12,10), PartPose.offset(-6,0,0));
        root.addOrReplaceChild("left_leg", CubeListBuilder.create()
                .texOffs(0,78).addBox(-5,0,-6,11,12,12)
                .texOffs(0,102).addBox(-4.5F,12,-5,10,12,10), PartPose.offset(6,0,0));
        return LayerDefinition.create(mesh,128,128);
    }

    @Override public void setupAnim(T e,float limbSwing,float limbSwingAmount,float age,float yaw,float pitch){
        head.yRot=yaw*((float)Math.PI/180F); head.xRot=pitch*((float)Math.PI/180F);
        rightArm.xRot=Mth.cos(limbSwing*.6662F+(float)Math.PI)*2F*limbSwingAmount*.5F;
        leftArm.xRot=Mth.cos(limbSwing*.6662F)*2F*limbSwingAmount*.5F;
        rightLeg.xRot=Mth.cos(limbSwing*.6662F)*1.4F*limbSwingAmount;
        leftLeg.xRot=Mth.cos(limbSwing*.6662F+(float)Math.PI)*1.4F*limbSwingAmount;
        rightArm.zRot=leftArm.zRot=0F;
        if(e.isAggressive()) { rightArm.xRot=-1.35F; leftArm.xRot=-1.15F; }
    }

    @Override public void renderToBuffer(PoseStack ps, VertexConsumer vc,int light,int overlay,float r,float g,float b,float a){
        root.render(ps,vc,light,overlay,r,g,b,a);
    }
}
