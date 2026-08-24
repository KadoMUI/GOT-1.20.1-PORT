package got;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

/** Original two-pass crossbar/cloth renderer for a wall-mounted banner. */
public final class GOTWallBannerRenderer extends EntityRenderer<GOTWallBannerEntity> {
    private static final ResourceLocation STAND = ResourceLocation.fromNamespaceAndPath(
            GOTMod.MOD_ID, "textures/entity/banner/stand.png");
    private final GOTWallBannerModel model;

    public GOTWallBannerRenderer(EntityRendererProvider.Context context) {
        super(context);
        model = new GOTWallBannerModel(context.bakeLayer(GOTBannerLayers.WALL));
        shadowRadius = 0.0F;
    }

    @Override
    public void render(GOTWallBannerEntity banner, float yaw, float partialTick, PoseStack pose,
                       MultiBufferSource buffers, int packedLight) {
        pose.pushPose();
        pose.translate(0.0D, 1.5D, 0.0D);
        pose.scale(-1.0F, -1.0F, 1.0F);
        pose.mulPose(Axis.YP.rotationDegrees(banner.getDirection().toYRot()));
        model.renderPost(pose, buffers.getBuffer(RenderType.entityCutoutNoCull(STAND)),
                packedLight, OverlayTexture.NO_OVERLAY);
        model.renderBanner(pose, buffers.getBuffer(RenderType.entityCutoutNoCull(
                        banner.getBannerType().texture())),
                packedLight, OverlayTexture.NO_OVERLAY);
        pose.popPose();
        super.render(banner, yaw, partialTick, pose, buffers, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(GOTWallBannerEntity banner) {
        return STAND;
    }
}
