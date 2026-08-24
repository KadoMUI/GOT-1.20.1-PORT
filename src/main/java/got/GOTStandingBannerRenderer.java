package got;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.AABB;

/** Original two-pass frame/cloth renderer for a freestanding banner. */
public final class GOTStandingBannerRenderer extends EntityRenderer<GOTStandingBannerEntity> {
    private static final ResourceLocation STAND = ResourceLocation.fromNamespaceAndPath(
            GOTMod.MOD_ID, "textures/entity/banner/stand.png");
    private final GOTStandingBannerModel model;

    public GOTStandingBannerRenderer(EntityRendererProvider.Context context) {
        super(context);
        model = new GOTStandingBannerModel(context.bakeLayer(GOTBannerLayers.STANDING));
        shadowRadius = 0.0F;
    }

    @Override
    public void render(GOTStandingBannerEntity banner, float yaw, float partialTick, PoseStack pose,
                       MultiBufferSource buffers, int packedLight) {
        pose.pushPose();
        pose.translate(0.0D, 1.5D, 0.0D);
        pose.scale(-1.0F, -1.0F, 1.0F);
        pose.mulPose(Axis.YP.rotationDegrees(180.0F - yaw));
        model.renderWorldFrame(pose, buffers.getBuffer(RenderType.entityCutoutNoCull(STAND)),
                packedLight, OverlayTexture.NO_OVERLAY);
        model.renderCloth(pose, buffers.getBuffer(RenderType.entityCutoutNoCull(
                        banner.getBannerType().texture())),
                packedLight, OverlayTexture.NO_OVERLAY);
        pose.popPose();
        if (Minecraft.getInstance().options.renderDebug && banner.isClaimActive()) {
            AABB bounds = banner.getClaimBounds().move(-banner.getX(), -banner.getY(), -banner.getZ());
            LevelRenderer.renderLineBox(pose, buffers.getBuffer(RenderType.lines()),
                    bounds, 0.0F, 1.0F, 0.0F, 1.0F);
        }
        super.render(banner, yaw, partialTick, pose, buffers, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(GOTStandingBannerEntity banner) {
        return STAND;
    }
}
