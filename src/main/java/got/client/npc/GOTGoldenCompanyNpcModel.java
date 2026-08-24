package got.client.npc;

import got.npc.GOTGoldenCompanyNpcEntity;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

/** Standard legacy human geometry for Golden Company troops. */
public final class GOTGoldenCompanyNpcModel extends HumanoidModel<GOTGoldenCompanyNpcEntity> {
    public GOTGoldenCompanyNpcModel(ModelPart root) { super(root); }

    public static LayerDefinition bodyLayer(float deformation) {
        MeshDefinition mesh = HumanoidModel.createMesh(new CubeDeformation(deformation), 0.0F);
        PartDefinition root = mesh.getRoot();
        root.addOrReplaceChild("hat", CubeListBuilder.create().texOffs(0, 32)
                        .addBox(-4.0F, -8.0F, -4.0F, 8.0F, 16.0F, 8.0F,
                                new CubeDeformation(0.5F + deformation)), PartPose.ZERO);
        return LayerDefinition.create(mesh, 64, 64);
    }

    public static LayerDefinition armorLayer(float deformation) {
        return LayerDefinition.create(
                HumanoidModel.createMesh(new CubeDeformation(deformation), 0.0F), 64, 32);
    }
}
