package got.client.model;

import got.GOTLegacyHelmetItem;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;

public final class GOTLegacyHelmetModels {
    private GOTLegacyHelmetModels() {}

    public static HumanoidModel<?> model(GOTLegacyHelmetItem.Shape shape) {
        return shape == GOTLegacyHelmetItem.Shape.REACH ? buildReach() : buildNorth();
    }

    private static HumanoidModel<?> buildNorth() {
        MeshDefinition mesh = humanoidMesh();
        PartDefinition root = mesh.getRoot();
        CubeListBuilder h = CubeListBuilder.create()
                .texOffs(0,0).addBox(-4,-8,-4,8,8,8,new CubeDeformation(0.5F))
                .texOffs(0,16).addBox(-1.5F,-9,-3.5F,3,1,7,new CubeDeformation(0.5F))
                .texOffs(20,16).addBox(-0.5F,-10,-3.5F,1,1,7,new CubeDeformation(0.5F))
                .texOffs(24,0).addBox(-1.5F,-11,-5,3,4,1)
                .texOffs(24,5).addBox(-0.5F,-12,-5,1,1,1)
                .texOffs(28,5).addBox(-0.5F,-7,-5,1,1,1)
                .texOffs(32,0).addBox(-1.5F,-10,4,3,3,1)
                .texOffs(32,4).addBox(-0.5F,-11,4,1,1,1)
                .texOffs(36,4).addBox(-0.5F,-7,4,1,1,1);
        root.addOrReplaceChild("head",h,PartPose.ZERO);
        return new HumanoidModel<>(LayerDefinition.create(mesh,64,32).bakeRoot());
    }

    private static HumanoidModel<?> buildReach() {
        MeshDefinition mesh = humanoidMesh();
        PartDefinition root = mesh.getRoot();
        PartDefinition head = root.addOrReplaceChild("head",CubeListBuilder.create()
                .texOffs(0,0).addBox(-4,-8,-4,8,8,8,new CubeDeformation(0.5F))
                .texOffs(0,16).addBox(-1,-11.5F,-4.5F,2,7,6),PartPose.ZERO);

        // Legacy GOTModelReachHelmet created three 0-thickness 14x12 mane
        // planes and fanned them slightly around the helmet crest.
        head.addOrReplaceChild("mane0",CubeListBuilder.create().texOffs(32,0)
                .addBox(0,-11,-1,0,14,12),PartPose.rotation(0,-0.17F,0));
        head.addOrReplaceChild("mane1",CubeListBuilder.create().texOffs(32,0)
                .addBox(0,-11,-1,0,14,12),PartPose.rotation(0.22F,0,0));
        head.addOrReplaceChild("mane2",CubeListBuilder.create().texOffs(32,0)
                .addBox(0,-11,-1,0,14,12),PartPose.rotation(0,0.17F,0));
        return new HumanoidModel<>(LayerDefinition.create(mesh,64,32).bakeRoot());
    }

    private static MeshDefinition humanoidMesh() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        CubeListBuilder none = CubeListBuilder.create();
        root.addOrReplaceChild("head",none,PartPose.ZERO);
        root.addOrReplaceChild("hat",none,PartPose.ZERO);
        root.addOrReplaceChild("body",none,PartPose.ZERO);
        root.addOrReplaceChild("right_arm",none,PartPose.ZERO);
        root.addOrReplaceChild("left_arm",none,PartPose.ZERO);
        root.addOrReplaceChild("right_leg",none,PartPose.ZERO);
        root.addOrReplaceChild("left_leg",none,PartPose.ZERO);
        return mesh;
    }
}
