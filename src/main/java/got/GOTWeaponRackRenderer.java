package got;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class GOTWeaponRackRenderer implements BlockEntityRenderer<GOTWeaponRackBlockEntity> {
    public GOTWeaponRackRenderer(BlockEntityRendererProvider.Context context) {}

    @Override
    public void render(GOTWeaponRackBlockEntity rack, float partialTick, PoseStack poseStack,
                       MultiBufferSource buffer, int packedLight, int packedOverlay) {
        ItemStack stack = rack.getDisplayedItem();
        if (stack.isEmpty()) {
            return;
        }

        float rotation = rack.getBlockState().getValue(GOTWeaponRackBlock.FACING).toYRot();
        poseStack.pushPose();
        poseStack.translate(0.5D, 0.58D, 0.5D);
        poseStack.mulPose(Axis.YP.rotationDegrees(-rotation));
        poseStack.mulPose(Axis.ZP.rotationDegrees(-45.0F));
        poseStack.scale(0.75F, 0.75F, 0.75F);
        Minecraft.getInstance().getItemRenderer().renderStatic(
                stack, ItemDisplayContext.FIXED, packedLight, OverlayTexture.NO_OVERLAY,
                poseStack, buffer, rack.getLevel(), 0);
        poseStack.popPose();
    }
}
