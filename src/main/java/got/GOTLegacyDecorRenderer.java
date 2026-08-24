package got;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BedPart;
import net.minecraftforge.registries.ForgeRegistries;

/** Renders the original Beacon, Unsmeltery and animal-rug Java models. */
public final class GOTLegacyDecorRenderer implements BlockEntityRenderer<GOTLegacyDecorBlockEntity> {
    private static final ResourceLocation BEACON = texture("model/beacon.png");
    private static final ResourceLocation UNSMELTERY_IDLE = texture("model/unsmeltery/idle.png");
    private static final ResourceLocation UNSMELTERY_ACTIVE = texture("model/unsmeltery/active.png");
    private static final ResourceLocation BEAR_BLACK = texture("entity/animal/bear/black.png");
    private static final ResourceLocation BEAR_DARK = texture("entity/animal/bear/dark.png");
    private static final ResourceLocation BEAR_LIGHT = texture("entity/animal/bear/light.png");
    private static final ResourceLocation GIRAFFE = texture("entity/animal/giraffe/giraffe.png");
    private static final ResourceLocation LION = texture("entity/animal/lion/lion.png");
    private static final ResourceLocation LIONESS = texture("entity/animal/lion/lioness.png");

    private final ModelPart beacon;
    private final ModelPart unsmeltery;
    private final ModelPart bearRug;
    private final ModelPart giraffeRug;
    private final ModelPart lionRug;

    public GOTLegacyDecorRenderer(BlockEntityRendererProvider.Context context) {
        beacon = context.bakeLayer(LegacyDecorLayers.BEACON);
        unsmeltery = context.bakeLayer(LegacyDecorLayers.UNSMELTERY);
        bearRug = context.bakeLayer(LegacyDecorLayers.BEAR_RUG);
        giraffeRug = context.bakeLayer(LegacyDecorLayers.GIRAFFE_RUG);
        lionRug = context.bakeLayer(LegacyDecorLayers.LION_RUG);
    }

    @Override
    public void render(GOTLegacyDecorBlockEntity entity, float partialTick, PoseStack pose,
                       MultiBufferSource buffers, int packedLight, int packedOverlay) {
        BlockState state = entity.getBlockState();
        Block block = state.getBlock();
        if (block == GOTDecorativeFunctionalBlocks.BEACON.get()) {
            renderBeacon(pose, buffers, packedLight);
        } else if (block == GOTDecorativeFunctionalBlocks.UNSMELTERY.get()) {
            renderUnsmeltery(entity, partialTick, pose, buffers, packedLight);
        } else if (block instanceof GOTRugBlock
                && state.getValue(GOTRugBlock.PART) == BedPart.FOOT) {
            renderRug(state, pose, buffers, packedLight);
        }
    }

    private void renderBeacon(PoseStack pose, MultiBufferSource buffers, int light) {
        pose.pushPose();
        pose.translate(0.5D, 1.5D, 0.5D);
        pose.scale(1.0F, -1.0F, 1.0F);
        render(beacon, BEACON, pose, buffers, light);
        pose.popPose();
    }

    private void renderUnsmeltery(GOTLegacyDecorBlockEntity entity, float partialTick,
                                  PoseStack pose, MultiBufferSource buffers, int light) {
        BlockState state = entity.getBlockState();
        Direction facing = state.getValue(GOTUnsmelteryBlock.FACING);
        boolean lit = state.getValue(GOTUnsmelteryBlock.LIT);
        float time = entity.getLevel() == null ? 0.0F : entity.getLevel().getGameTime() + partialTick;
        unsmeltery.getChild("body").xRot = lit
                ? (float) Math.sin(time * 0.45F) * 20.0F * ((float) Math.PI / 180.0F)
                : 0.0F;

        pose.pushPose();
        pose.translate(0.5D, 1.5D, 0.5D);
        pose.scale(1.0F, -1.0F, -1.0F);
        pose.mulPose(Axis.YP.rotationDegrees(facing.toYRot()));
        render(unsmeltery, lit ? UNSMELTERY_ACTIVE : UNSMELTERY_IDLE, pose, buffers, light);
        pose.popPose();
    }

    private void renderRug(BlockState state, PoseStack pose, MultiBufferSource buffers, int light) {
        String id = ForgeRegistries.BLOCKS.getKey(state.getBlock()).getPath();
        Direction facing = state.getValue(GOTRugBlock.FACING);
        pose.pushPose();
        pose.translate(0.5D, 0.0D, 0.5D);
        pose.scale(-1.0F, -1.0F, 1.0F);
        pose.mulPose(Axis.YP.rotationDegrees(180.0F - facing.toYRot()));

        if (id.startsWith("bear_rug_")) {
            pose.scale(1.2F, 1.2F, 1.2F);
            ResourceLocation texture = id.endsWith("black") ? BEAR_BLACK
                    : id.endsWith("dark") ? BEAR_DARK : BEAR_LIGHT;
            renderBear(texture, pose, buffers, light);
        } else if (id.equals("giraffe_rug")) {
            renderGiraffe(pose, buffers, light);
        } else if (id.equals("lion_rug") || id.equals("lioness_rug")) {
            renderLion(id.equals("lioness_rug") ? LIONESS : LION, pose, buffers, light);
        }
        pose.popPose();
    }

    private void renderBear(ResourceLocation texture, PoseStack pose,
                            MultiBufferSource buffers, int light) {
        VertexConsumer out = buffers.getBuffer(RenderType.entityCutoutNoCull(texture));
        pose.translate(0.0D, -0.35D, 0.0D);
        renderPart(bearRug, "body", pose, out, light, 0.0F, 0.0F, 0.0F, 1.5F, 0.4F, 1.0F);
        renderPart(bearRug, "head", pose, out, light, 0.0F, -0.4F, 0.1F, 1.0F, 1.0F, 1.0F);
        renderPart(bearRug, "leg1", pose, out, light, -0.3F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        renderPart(bearRug, "leg3", pose, out, light, -0.3F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        renderPart(bearRug, "leg2", pose, out, light, 0.3F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        renderPart(bearRug, "leg4", pose, out, light, 0.3F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
    }

    private void renderGiraffe(PoseStack pose, MultiBufferSource buffers, int light) {
        VertexConsumer out = buffers.getBuffer(RenderType.entityCutoutNoCull(GIRAFFE));
        pose.translate(0.0D, 0.1D, 0.0D);
        renderPart(giraffeRug, "body", pose, out, light, 0.0F, 0.0F, 0.0F, 1.5F, 0.4F, 1.0F);
        renderPart(giraffeRug, "tail", pose, out, light, 0.0F, 0.0F, 0.0F, 1.5F, 0.4F, 1.0F);
        renderPart(giraffeRug, "head", pose, out, light, 0.0F, 0.6F, -0.2F, 1.0F, 1.0F, 1.0F);
        renderPart(giraffeRug, "neck", pose, out, light, 0.0F, 0.6F, -0.2F, 1.0F, 1.0F, 1.0F);
        renderPart(giraffeRug, "leg1", pose, out, light, -0.25F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        renderPart(giraffeRug, "leg3", pose, out, light, -0.25F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        renderPart(giraffeRug, "leg2", pose, out, light, 0.25F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        renderPart(giraffeRug, "leg4", pose, out, light, 0.25F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
    }

    private void renderLion(ResourceLocation texture, PoseStack pose,
                            MultiBufferSource buffers, int light) {
        VertexConsumer out = buffers.getBuffer(RenderType.entityCutoutNoCull(texture));
        pose.translate(0.0D, -0.4D, 0.0D);
        renderPart(lionRug, "body", pose, out, light, 0.0F, 0.0F, 0.0F, 1.5F, 0.4F, 1.0F);
        renderPart(lionRug, "tail", pose, out, light, 0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        renderPart(lionRug, "head", pose, out, light, 0.0F, -0.1F, 0.1F, 1.0F, 1.0F, 1.0F);
        renderPart(lionRug, "mane", pose, out, light, 0.0F, -0.1F, 0.1F, 1.0F, 1.0F, 1.0F);
        pose.translate(0.0D, 0.15D, 0.0D);
        renderPart(lionRug, "leg1", pose, out, light, -0.4F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        renderPart(lionRug, "leg3", pose, out, light, -0.4F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        renderPart(lionRug, "leg2", pose, out, light, 0.4F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        renderPart(lionRug, "leg4", pose, out, light, 0.4F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
    }

    private static void renderPart(ModelPart root, String name, PoseStack pose, VertexConsumer out,
                                   int light, float x, float y, float z,
                                   float xScale, float yScale, float zScale) {
        pose.pushPose();
        pose.translate(x, y, z);
        pose.scale(xScale, yScale, zScale);
        root.getChild(name).render(pose, out, light, OverlayTexture.NO_OVERLAY);
        pose.popPose();
    }

    private static void render(ModelPart model, ResourceLocation texture, PoseStack pose,
                               MultiBufferSource buffers, int light) {
        model.render(pose, buffers.getBuffer(RenderType.entityCutoutNoCull(texture)),
                light, OverlayTexture.NO_OVERLAY);
    }

    private static ResourceLocation texture(String path) {
        return ResourceLocation.fromNamespaceAndPath(GOTMod.MOD_ID, "textures/" + path);
    }

    @Override
    public boolean shouldRenderOffScreen(GOTLegacyDecorBlockEntity entity) {
        return entity.getBlockState().getBlock() instanceof GOTRugBlock;
    }
}
