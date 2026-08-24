package got;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

/** Held and inventory rendering based on the original GOTRenderBannerItem. */
public final class GOTBannerItemRenderer extends BlockEntityWithoutLevelRenderer {
    private static final ResourceLocation STAND = ResourceLocation.fromNamespaceAndPath(
            GOTMod.MOD_ID, "textures/entity/banner/stand.png");
    private final GOTStandingBannerModel model;

    public GOTBannerItemRenderer(BlockEntityRenderDispatcher dispatcher, EntityModelSet models) {
        super(dispatcher, models);
        model = new GOTStandingBannerModel(models.bakeLayer(GOTBannerLayers.STANDING));
    }

    @Override
    public void renderByItem(ItemStack stack, ItemDisplayContext context, PoseStack pose,
                             MultiBufferSource buffers, int packedLight, int packedOverlay) {
        pose.pushPose();
        applyDisplayTransform(context, pose);
        model.renderHeldFrame(pose, buffers.getBuffer(RenderType.entityCutoutNoCull(STAND)),
                packedLight, OverlayTexture.NO_OVERLAY);
        model.renderCloth(pose, buffers.getBuffer(RenderType.entityCutoutNoCull(
                        GOTBannerItem.getBannerType(stack).texture())),
                packedLight, OverlayTexture.NO_OVERLAY);
        pose.popPose();
    }

    private static void applyDisplayTransform(ItemDisplayContext context, PoseStack pose) {
        switch (context) {
            case GUI -> {
                pose.translate(0.5D, 0.87D, 0.5D);
                pose.mulPose(Axis.XP.rotationDegrees(12.0F));
                pose.mulPose(Axis.YP.rotationDegrees(205.0F));
                pose.scale(0.30F, -0.30F, 0.30F);
            }
            case GROUND -> {
                pose.translate(0.5D, 0.25D, 0.5D);
                pose.scale(0.24F, -0.24F, 0.24F);
            }
            case FIXED -> {
                pose.translate(0.5D, 0.82D, 0.5D);
                pose.mulPose(Axis.YP.rotationDegrees(180.0F));
                pose.scale(0.30F, -0.30F, 0.30F);
            }
            case FIRST_PERSON_LEFT_HAND -> {
                pose.translate(0.75D, 0.8D, 0.45D);
                pose.mulPose(Axis.YP.rotationDegrees(35.0F));
                pose.scale(-0.48F, -0.48F, 0.48F);
            }
            case FIRST_PERSON_RIGHT_HAND -> {
                pose.translate(0.25D, 0.8D, 0.45D);
                pose.mulPose(Axis.YP.rotationDegrees(-35.0F));
                pose.scale(0.48F, -0.48F, 0.48F);
            }
            case THIRD_PERSON_LEFT_HAND -> {
                pose.translate(0.65D, 0.65D, 0.5D);
                pose.mulPose(Axis.ZP.rotationDegrees(-75.0F));
                pose.scale(-0.36F, -0.36F, 0.36F);
            }
            case THIRD_PERSON_RIGHT_HAND -> {
                pose.translate(0.35D, 0.65D, 0.5D);
                pose.mulPose(Axis.ZP.rotationDegrees(75.0F));
                pose.scale(0.36F, -0.36F, 0.36F);
            }
            case HEAD -> {
                pose.translate(0.5D, 0.65D, 0.5D);
                pose.scale(0.25F, -0.25F, 0.25F);
            }
            default -> {
                pose.translate(0.5D, 0.8D, 0.5D);
                pose.scale(0.30F, -0.30F, 0.30F);
            }
        }
    }
}
