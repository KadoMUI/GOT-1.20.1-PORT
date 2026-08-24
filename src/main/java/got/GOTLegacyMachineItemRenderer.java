package got;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

/** Original 1.7.10 inventory rendering for Beacon and Unsmeltery block items. */
public final class GOTLegacyMachineItemRenderer extends BlockEntityWithoutLevelRenderer {
    private static final ResourceLocation BEACON_TEXTURE = texture("model/beacon.png");
    private static final ResourceLocation UNSMELTERY_TEXTURE = texture("model/unsmeltery/idle.png");

    private final ModelPart beacon;
    private final ModelPart unsmeltery;

    public GOTLegacyMachineItemRenderer(BlockEntityRenderDispatcher dispatcher, EntityModelSet models) {
        super(dispatcher, models);
        beacon = models.bakeLayer(LegacyDecorLayers.BEACON);
        unsmeltery = models.bakeLayer(LegacyDecorLayers.UNSMELTERY);
    }

    @Override
    public void renderByItem(ItemStack stack, ItemDisplayContext context, PoseStack pose,
                             MultiBufferSource buffers, int packedLight, int packedOverlay) {
        if (!(stack.getItem() instanceof GOTLegacyMachineBlockItem item)) return;
        boolean isBeacon = item.kind() == GOTLegacyMachineBlockItem.Kind.BEACON;

        pose.pushPose();
        applyDisplayTransform(context, pose);
        pose.translate(0.5D, 1.5D, 0.5D);
        pose.scale(1.0F, -1.0F, isBeacon ? 1.0F : -1.0F);
        ModelPart model = isBeacon ? beacon : unsmeltery;
        ResourceLocation texture = isBeacon ? BEACON_TEXTURE : UNSMELTERY_TEXTURE;
        model.render(pose, buffers.getBuffer(RenderType.entityCutoutNoCull(texture)),
                packedLight, OverlayTexture.NO_OVERLAY);
        pose.popPose();
    }

    private static void applyDisplayTransform(ItemDisplayContext context, PoseStack pose) {
        switch (context) {
            case GUI -> {
                pose.translate(0.5D, 0.5D, 0.5D);
                pose.mulPose(Axis.XP.rotationDegrees(25.0F));
                pose.mulPose(Axis.YP.rotationDegrees(225.0F));
                pose.translate(-0.5D, -0.5D, -0.5D);
                pose.scale(0.75F, 0.75F, 0.75F);
            }
            case GROUND -> pose.scale(0.55F, 0.55F, 0.55F);
            case FIXED -> {
                pose.translate(0.5D, 0.5D, 0.5D);
                pose.mulPose(Axis.YP.rotationDegrees(180.0F));
                pose.translate(-0.5D, -0.5D, -0.5D);
                pose.scale(0.75F, 0.75F, 0.75F);
            }
            case FIRST_PERSON_LEFT_HAND, FIRST_PERSON_RIGHT_HAND -> pose.scale(0.8F, 0.8F, 0.8F);
            case THIRD_PERSON_LEFT_HAND, THIRD_PERSON_RIGHT_HAND -> pose.scale(0.65F, 0.65F, 0.65F);
            case HEAD -> pose.scale(0.7F, 0.7F, 0.7F);
            default -> {}
        }
    }

    private static ResourceLocation texture(String path) {
        return ResourceLocation.fromNamespaceAndPath(GOTMod.MOD_ID, "textures/" + path);
    }
}
