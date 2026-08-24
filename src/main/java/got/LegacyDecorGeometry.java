package got;

import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

/**
 * Direct translations of the original 1.7.10 Java geometry.
 *
 * <p>The source classes are GOTModelBeacon, GOTModelUnsmeltery,
 * GOTModelBear/GOTModelBearRug, GOTModelGiraffe/GOTModelGiraffeRug and
 * GOTModelLion/GOTModelLionRug. Texture offsets, cube dimensions, pivots,
 * mirroring, child hierarchy and the rug pose rotations are preserved.</p>
 */
public final class LegacyDecorGeometry {
    private static final float DEG = (float) Math.PI / 180.0F;

    private LegacyDecorGeometry() {}

    public static LayerDefinition beacon() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        root.addOrReplaceChild("base", CubeListBuilder.create().texOffs(0, 0)
                        .addBox(-8.0F, -8.0F, -2.0F, 16.0F, 16.0F, 4.0F),
                PartPose.offsetAndRotation(0.0F, 22.0F, 0.0F, 90.0F * DEG, 0.0F, 0.0F));

        for (int layer = 0; layer < 3; layer++) {
            for (int log = 0; log < 4; log++) {
                float x = layer == 1 ? 0.0F : -6.0F + log * 4.0F;
                float y = 17.0F - layer * 3.0F;
                float z = layer == 1 ? -6.0F + log * 4.0F : 0.0F;
                float yRot = layer == 1 ? 90.0F * DEG : 0.0F;
                root.addOrReplaceChild("log_" + layer + "_" + log,
                        CubeListBuilder.create().texOffs(30, 15)
                                .addBox(-1.5F, 0.0F, -7.0F, 3.0F, 3.0F, 14.0F),
                        PartPose.offsetAndRotation(x, y, z, 0.0F, yRot, 0.0F));
            }
        }
        return LayerDefinition.create(mesh, 64, 32);
    }

    public static LayerDefinition unsmeltery() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        root.addOrReplaceChild("base", CubeListBuilder.create().texOffs(0, 0)
                        .addBox(-7.0F, 0.0F, -7.0F, 14.0F, 3.0F, 14.0F),
                PartPose.offset(0.0F, 21.0F, 0.0F));

        root.addOrReplaceChild("body", CubeListBuilder.create()
                        .texOffs(0, 17).addBox(-7.0F, -2.0F, -7.0F, 14.0F, 10.0F, 14.0F)
                        .texOffs(0, 41).addBox(-7.0F, -4.0F, -7.0F, 14.0F, 2.0F, 1.0F)
                        .texOffs(0, 41).addBox(-7.0F, -4.0F, 6.0F, 14.0F, 2.0F, 1.0F)
                        .texOffs(0, 44).addBox(-7.0F, -4.0F, -6.0F, 1.0F, 2.0F, 12.0F)
                        .texOffs(0, 44).addBox(6.0F, -4.0F, -6.0F, 1.0F, 2.0F, 12.0F),
                PartPose.offset(0.0F, 12.0F, 0.0F));

        PartDefinition right = root.addOrReplaceChild("stand_right",
                CubeListBuilder.create().texOffs(56, 6)
                        .addBox(-0.9F, -12.0F, -1.0F, 1.0F, 12.0F, 2.0F),
                PartPose.offset(-7.0F, 23.0F, 0.0F));
        right.addOrReplaceChild("handle", CubeListBuilder.create().texOffs(56, 0)
                        .addBox(-1.0F, -2.0F, -1.0F, 1.0F, 3.0F, 3.0F),
                PartPose.offsetAndRotation(0.0F, -11.0F, 0.0F, 45.0F * DEG, 0.0F, 0.0F));

        PartDefinition left = root.addOrReplaceChild("stand_left",
                CubeListBuilder.create().texOffs(56, 6).mirror()
                        .addBox(-0.1F, -12.0F, -1.0F, 1.0F, 12.0F, 2.0F),
                PartPose.offset(7.0F, 23.0F, 0.0F));
        left.addOrReplaceChild("handle", CubeListBuilder.create().texOffs(56, 0).mirror()
                        .addBox(0.0F, -2.0F, -1.0F, 1.0F, 3.0F, 3.0F),
                PartPose.offsetAndRotation(0.0F, -11.0F, 0.0F, 45.0F * DEG, 0.0F, 0.0F));
        return LayerDefinition.create(mesh, 64, 64);
    }

    public static LayerDefinition bearRug() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        PartDefinition head = root.addOrReplaceChild("head", CubeListBuilder.create()
                        .texOffs(32, 0).addBox(-4.0F, -5.0F, -4.0F, 8.0F, 9.0F, 6.0F)
                        .texOffs(0, 0).addBox(-4.5F, -5.5F, -11.0F, 9.0F, 10.0F, 7.0F),
                PartPose.offset(0.0F, 8.0F, -9.0F));
        head.addOrReplaceChild("nose", CubeListBuilder.create()
                        .texOffs(0, 17).addBox(-2.5F, -2.0F, -17.0F, 5.0F, 6.0F, 6.0F)
                        .texOffs(0, 29).addBox(-1.5F, -2.5F, -17.5F, 3.0F, 3.0F, 7.0F),
                PartPose.ZERO);
        head.addOrReplaceChild("ear_left", CubeListBuilder.create().texOffs(23, 17)
                        .addBox(-4.0F, -8.0F, -6.0F, 3.0F, 3.0F, 1.0F),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -15.0F * DEG));
        head.addOrReplaceChild("ear_right", CubeListBuilder.create().texOffs(23, 17).mirror()
                        .addBox(1.0F, -8.0F, -6.0F, 3.0F, 3.0F, 1.0F),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 15.0F * DEG));

        root.addOrReplaceChild("body", CubeListBuilder.create()
                        .texOffs(40, 0).addBox(-6.0F, -8.0F, -9.0F, 12.0F, 14.0F, 28.0F)
                        .texOffs(92, 0).addBox(-2.5F, -6.0F, 19.0F, 5.0F, 5.0F, 2.0F),
                PartPose.offset(0.0F, 10.0F, -2.0F));

        root.addOrReplaceChild("leg1", CubeListBuilder.create()
                        .texOffs(56, 44).addBox(-6.0F, -2.0F, -3.5F, 6.0F, 9.0F, 9.0F)
                        .texOffs(86, 44).addBox(-5.5F, 7.0F, -1.5F, 5.0F, 11.0F, 5.0F),
                PartPose.offsetAndRotation(-4.0F, 6.0F, 10.0F, 30.0F * DEG, 0.0F, 90.0F * DEG));
        root.addOrReplaceChild("leg2", CubeListBuilder.create().mirror()
                        .texOffs(56, 44).addBox(0.0F, -2.0F, -3.5F, 6.0F, 9.0F, 9.0F)
                        .texOffs(86, 44).addBox(0.5F, 7.0F, -1.5F, 5.0F, 11.0F, 5.0F),
                PartPose.offsetAndRotation(4.0F, 6.0F, 10.0F, 30.0F * DEG, 0.0F, -90.0F * DEG));
        root.addOrReplaceChild("leg3", CubeListBuilder.create()
                        .texOffs(0, 44).addBox(-6.0F, -2.0F, -3.0F, 6.0F, 9.0F, 8.0F)
                        .texOffs(28, 44).addBox(-5.5F, 7.0F, -1.5F, 5.0F, 12.0F, 5.0F),
                PartPose.offsetAndRotation(-3.0F, 6.0F, -5.0F, -20.0F * DEG, 0.0F, 90.0F * DEG));
        root.addOrReplaceChild("leg4", CubeListBuilder.create().mirror()
                        .texOffs(0, 44).addBox(0.0F, -2.0F, -3.0F, 6.0F, 9.0F, 8.0F)
                        .texOffs(28, 44).addBox(0.5F, 7.0F, -1.5F, 5.0F, 12.0F, 5.0F),
                PartPose.offsetAndRotation(3.0F, 6.0F, -5.0F, -20.0F * DEG, 0.0F, -90.0F * DEG));
        return LayerDefinition.create(mesh, 128, 64);
    }

    public static LayerDefinition giraffeRug() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        root.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0)
                        .addBox(-6.0F, -8.0F, -13.0F, 12.0F, 16.0F, 26.0F),
                PartPose.offset(0.0F, -11.0F, 0.0F));
        root.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(104, 0)
                        .addBox(-0.5F, 0.0F, 0.0F, 1.0F, 24.0F, 1.0F),
                PartPose.offset(0.0F, -12.0F, 13.0F));
        root.addOrReplaceChild("neck", CubeListBuilder.create()
                        .texOffs(0, 44).addBox(-4.5F, -13.0F, -4.5F, 9.0F, 11.0F, 9.0F)
                        .texOffs(78, 0).addBox(-3.0F, -37.0F, -3.0F, 6.0F, 40.0F, 6.0F),
                PartPose.offsetAndRotation(0.0F, -14.0F, -7.0F, 90.0F * DEG, 0.0F, 0.0F));
        root.addOrReplaceChild("head", CubeListBuilder.create()
                        .texOffs(96, 48).addBox(-3.0F, -43.0F, -6.0F, 6.0F, 6.0F, 10.0F)
                        .texOffs(10, 0).addBox(-4.0F, -45.0F, 1.5F, 1.0F, 3.0F, 2.0F)
                        .texOffs(17, 0).addBox(3.0F, -45.0F, 1.5F, 1.0F, 3.0F, 2.0F)
                        .texOffs(0, 0).addBox(-2.5F, -47.0F, 0.0F, 1.0F, 4.0F, 1.0F)
                        .texOffs(5, 0).addBox(1.5F, -47.0F, 0.0F, 1.0F, 4.0F, 1.0F)
                        .texOffs(76, 56).addBox(-2.0F, -41.0F, -11.0F, 4.0F, 3.0F, 5.0F),
                PartPose.offset(0.0F, 25.0F, -48.0F));

        addGiraffeLeg(root, "leg1", false, -3.9F, 8.0F, 30.0F, 90.0F);
        addGiraffeLeg(root, "leg2", true, 3.9F, 8.0F, 30.0F, -90.0F);
        addGiraffeLeg(root, "leg3", false, -3.9F, -7.0F, -20.0F, 90.0F);
        addGiraffeLeg(root, "leg4", true, 3.9F, -7.0F, -20.0F, -90.0F);
        return LayerDefinition.create(mesh, 128, 64);
    }

    private static void addGiraffeLeg(PartDefinition root, String name, boolean mirror,
                                      float x, float z, float xRot, float zRot) {
        CubeListBuilder cubes = CubeListBuilder.create().texOffs(112, 0);
        if (mirror) cubes.mirror();
        cubes.addBox(-2.0F, 0.0F, -2.0F, 4.0F, 27.0F, 4.0F);
        root.addOrReplaceChild(name, cubes,
                PartPose.offsetAndRotation(x, -3.0F, z, xRot * DEG, 0.0F, zRot * DEG));
    }

    public static LayerDefinition lionRug() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        root.addOrReplaceChild("head", CubeListBuilder.create()
                        .texOffs(48, 0).addBox(-5.0F, -6.0F, -10.0F, 10.0F, 10.0F, 10.0F)
                        .texOffs(78, 0).addBox(-3.0F, -1.0F, -14.0F, 6.0F, 5.0F, 4.0F)
                        .texOffs(98, 0).addBox(-1.0F, -2.0F, -14.2F, 2.0F, 2.0F, 5.0F)
                        .texOffs(0, 0).addBox(-4.0F, -9.0F, -7.5F, 3.0F, 3.0F, 1.0F)
                        .texOffs(0, 0).mirror().addBox(1.0F, -9.0F, -7.5F, 3.0F, 3.0F, 1.0F),
                PartPose.offset(0.0F, 3.0F, -10.0F));
        root.addOrReplaceChild("mane", CubeListBuilder.create().texOffs(0, 0)
                        .addBox(-8.0F, -10.0F, -6.0F, 16.0F, 16.0F, 8.0F),
                PartPose.offset(0.0F, 3.0F, -10.0F));
        root.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 24)
                        .addBox(-7.0F, -6.5F, -11.0F, 14.0F, 14.0F, 24.0F),
                PartPose.offset(0.0F, 6.0F, 1.0F));
        addLionLeg(root, "leg1", false, -4.0F, 4.0F, 11.0F, true, 30.0F, 90.0F);
        addLionLeg(root, "leg2", true, 4.0F, 4.0F, 11.0F, true, 30.0F, -90.0F);
        addLionLeg(root, "leg3", false, -4.0F, 5.0F, -5.0F, false, -20.0F, 90.0F);
        addLionLeg(root, "leg4", true, 4.0F, 5.0F, -5.0F, false, -20.0F, -90.0F);
        root.addOrReplaceChild("tail", CubeListBuilder.create()
                        .texOffs(100, 50).addBox(-1.0F, -1.0F, 0.0F, 2.0F, 2.0F, 12.0F)
                        .texOffs(86, 57).addBox(-1.5F, -1.5F, 12.0F, 3.0F, 3.0F, 4.0F),
                PartPose.offset(0.0F, 4.0F, 13.0F));
        return LayerDefinition.create(mesh, 128, 64);
    }

    private static void addLionLeg(PartDefinition root, String name, boolean mirror,
                                   float x, float y, float z, boolean front,
                                   float xRot, float zRot) {
        CubeListBuilder cubes = CubeListBuilder.create();
        if (mirror) cubes.mirror();
        cubes.texOffs(front ? 52 : 80, 24)
                .addBox(mirror ? 0.0F : -6.0F, -2.0F, -3.5F,
                        6.0F, front ? 10.0F : 9.0F, front ? 8.0F : 7.0F)
                .texOffs(106, 24)
                .addBox(mirror ? 0.5F : -5.5F, front ? 8.0F : 7.0F, -2.5F,
                        5.0F, 12.0F, 5.0F);
        root.addOrReplaceChild(name, cubes,
                PartPose.offsetAndRotation(x, y, z, xRot * DEG, 0.0F, zRot * DEG));
    }
}
