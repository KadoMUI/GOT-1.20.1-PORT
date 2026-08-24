package got;

import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

/**
 * Direct 1.20.1 translation of the original 1.7.10 vessel model bytecode.
 *
 * Source classes: GOTModelMug, GOTModelGoblet, GOTModelAleHorn,
 * GOTModelWineGlass and GOTModelSkullCup. Coordinates, UV offsets,
 * pivots, child hierarchy and rotations are preserved exactly.
 */
public final class LegacyVesselGeometry {
    private LegacyVesselGeometry() {}

    public static LayerDefinition mug() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        CubeListBuilder body = CubeListBuilder.create()
                .texOffs(0, 0).addBox(-3, -8, -2, 1, 8, 4)
                .texOffs(10, 3).addBox(-3, -8, -3, 6, 8, 1)
                .texOffs(24, 0).addBox(2, -8, -2, 1, 8, 4)
                .texOffs(34, 3).addBox(-3, -8, 2, 6, 8, 1)
                .texOffs(0, 12).addBox(-2, -1, -2, 4, 1, 4)
                .texOffs(0, 17).addBox(3, -7, -0.5f, 2, 1, 1)
                .texOffs(0, 19).addBox(4, -6, -0.5f, 1, 4, 1)
                .texOffs(0, 24).addBox(3, -2, -0.5f, 2, 1, 1);
        root.addOrReplaceChild("mug", body, PartPose.ZERO);
        return LayerDefinition.create(mesh, 64, 32);
    }

    public static LayerDefinition goblet() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        root.addOrReplaceChild("base", CubeListBuilder.create()
                .texOffs(0, 0).addBox(-2.5f, 0, -2.5f, 5, 1, 5)
                .texOffs(0, 6).addBox(-0.5f, -3, -0.5f, 1, 3, 1), PartPose.offset(0, -1, 0));
        root.addOrReplaceChild("cup", CubeListBuilder.create()
                .texOffs(0, 12).addBox(-2.5f, 0, -2.5f, 5, 1, 5)
                .texOffs(0, 18).addBox(-2.5f, -4, -2.5f, 1, 4, 5)
                .texOffs(12, 22).addBox(-1.5f, -4, -2.5f, 3, 4, 1)
                .texOffs(20, 18).addBox(1.5f, -4, -2.5f, 1, 4, 5)
                .texOffs(32, 22).addBox(-1.5f, -4, 1.5f, 3, 4, 1), PartPose.offset(0, -5, 0));
        return LayerDefinition.create(mesh, 64, 32);
    }

    public static LayerDefinition wineGlass() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        root.addOrReplaceChild("base", CubeListBuilder.create()
                .texOffs(0, 0).addBox(-2, 0, -2, 4, 1, 4)
                .texOffs(0, 5).addBox(-0.5f, -4, -0.5f, 1, 4, 1), PartPose.offset(0, -1, 0));
        root.addOrReplaceChild("cup", CubeListBuilder.create()
                .texOffs(0, 16).addBox(-1.5f, 0, -1.5f, 3, 1, 3)
                .texOffs(0, 20).addBox(-2.5f, -4, -1.5f, 1, 4, 3)
                .texOffs(8, 22).addBox(-1.5f, -4, -2.5f, 3, 4, 1)
                .texOffs(16, 20).addBox(1.5f, -4, -1.5f, 1, 4, 3)
                .texOffs(24, 22).addBox(-1.5f, -4, 1.5f, 3, 4, 1), PartPose.offset(0, -6, 0));
        return LayerDefinition.create(mesh, 64, 32);
    }

    public static LayerDefinition skullCup() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        root.addOrReplaceChild("base", CubeListBuilder.create()
                .texOffs(0, 0).addBox(-3, 0, -3, 6, 1, 6)
                .texOffs(0, 7).addBox(-1, -3, -1, 2, 3, 2), PartPose.offset(0, -1, 0));
        root.addOrReplaceChild("cup", CubeListBuilder.create()
                .texOffs(32, 0).addBox(-4, 0, -4, 8, 1, 8)
                .texOffs(0, 16).addBox(-4, -5, -4, 1, 5, 8)
                .texOffs(18, 23).addBox(-3, -5, -4, 6, 5, 1)
                .texOffs(32, 16).addBox(3, -5, -4, 1, 5, 8)
                .texOffs(50, 23).addBox(-3, -5, 3, 6, 5, 1), PartPose.offset(0, -5, 0));
        return LayerDefinition.create(mesh, 64, 32);
    }

    public static LayerDefinition aleHorn() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        PartDefinition horn = root.addOrReplaceChild("horn", CubeListBuilder.create()
                .texOffs(28, 16).addBox(-1, -1, -1, 2, 6, 2), PartPose.offsetAndRotation(-4, -5, 0, 0, 0, (float)Math.PI / 2));
        PartDefinition horn1 = horn.addOrReplaceChild("horn1", CubeListBuilder.create()
                .texOffs(16, 16).addBox(-1.5f, -6, -1.5f, 3, 6, 3), PartPose.offsetAndRotation(0, 0, 0, 0, 0, (float)Math.toRadians(-20)));
        PartDefinition horn2 = horn1.addOrReplaceChild("horn2", CubeListBuilder.create()
                .texOffs(0, 16).addBox(-2, -6, -2, 4, 6, 4), PartPose.offsetAndRotation(0, -5, 0, 0, 0, (float)Math.toRadians(-20)));
        horn2.addOrReplaceChild("horn3", CubeListBuilder.create()
                .texOffs(0, 0).addBox(-2.5f, -1, -2.5f, 5, 1, 5)
                .texOffs(0, 6).addBox(-2.5f, -6, -1.5f, 1, 5, 3)
                .texOffs(8, 8).addBox(-2.5f, -6, -2.5f, 5, 5, 1)
                .texOffs(20, 6).addBox(1.5f, -6, -1.5f, 1, 5, 3)
                .texOffs(28, 8).addBox(-2.5f, -6, 1.5f, 5, 5, 1), PartPose.offsetAndRotation(0, -5, 0, 0, 0, (float)Math.toRadians(-20)));
        root.addOrReplaceChild("stand", CubeListBuilder.create()
                .texOffs(40, 16).addBox(1.5f, -8, -2.5f, 1, 9, 1)
                .texOffs(40, 16).addBox(1.5f, -8, 1.5f, 1, 9, 1)
                .texOffs(44, 16).addBox(-2.5f, -6, -0.5f, 1, 7, 1), PartPose.offset(0, -1, 0));
        return LayerDefinition.create(mesh, 64, 32);
    }
}
