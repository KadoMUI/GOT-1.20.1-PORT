package got;

import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

/** Direct 1.20.1 translations of GOTModelBanner and GOTModelBannerWall. */
public final class GOTBannerGeometry {
    private GOTBannerGeometry() {}

    public static LayerDefinition standing() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        root.addOrReplaceChild("stand", CubeListBuilder.create().texOffs(0, 0)
                        .addBox(-6.0F, -2.0F, -6.0F, 12.0F, 2.0F, 12.0F),
                PartPose.offset(0.0F, 24.0F, 0.0F));
        root.addOrReplaceChild("post", CubeListBuilder.create()
                        .texOffs(0, 14).addBox(-0.5F, -48.0F, -0.5F, 1.0F, 47.0F, 1.0F)
                        .texOffs(4, 14).addBox(-8.0F, -43.0F, -1.5F, 16.0F, 1.0F, 3.0F),
                PartPose.offset(0.0F, 24.0F, 0.0F));
        root.addOrReplaceChild("lower_post", CubeListBuilder.create().texOffs(0, 14)
                        .addBox(-0.5F, -1.0F, -0.5F, 1.0F, 24.0F, 1.0F),
                PartPose.offset(0.0F, 24.0F, 0.0F));
        root.addOrReplaceChild("banner_front", CubeListBuilder.create().texOffs(0, 0)
                        .addBox(-8.0F, 0.0F, -1.0F, 16.0F, 32.0F, 0.0F),
                PartPose.offset(0.0F, -18.0F, 0.0F));
        root.addOrReplaceChild("banner_back", CubeListBuilder.create().texOffs(0, 0)
                        .addBox(-8.0F, 0.0F, -1.0F, 16.0F, 32.0F, 0.0F),
                PartPose.offsetAndRotation(0.0F, -18.0F, 0.0F, 0.0F, (float) Math.PI, 0.0F));
        return LayerDefinition.create(mesh, 64, 64);
    }

    public static LayerDefinition wall() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        root.addOrReplaceChild("post", CubeListBuilder.create().texOffs(4, 18)
                        .addBox(-8.0F, 0.0F, -0.5F, 16.0F, 1.0F, 1.0F),
                PartPose.offset(0.0F, -8.0F, 0.0F));
        root.addOrReplaceChild("banner", CubeListBuilder.create().texOffs(0, 0)
                        .addBox(-8.0F, 0.0F, 0.0F, 16.0F, 32.0F, 0.0F),
                PartPose.offset(0.0F, -7.0F, 0.0F));
        return LayerDefinition.create(mesh, 64, 64);
    }
}
