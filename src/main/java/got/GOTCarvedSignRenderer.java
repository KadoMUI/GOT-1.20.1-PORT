package got;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;

public class GOTCarvedSignRenderer implements BlockEntityRenderer<GOTCarvedSignBlockEntity> {
    private final Font font;

    public GOTCarvedSignRenderer(BlockEntityRendererProvider.Context context) {
        font = context.getFont();
    }

    @Override
    public void render(GOTCarvedSignBlockEntity sign, float partialTick, PoseStack poseStack,
                       MultiBufferSource buffer, int packedLight, int packedOverlay) {
        if (sign.getText().getString().isEmpty()) {
            return;
        }
        float rotation = sign.getBlockState().getValue(GOTCarvedSignBlock.FACING).toYRot();
        boolean glowing = sign.getBlockState().getBlock() instanceof GOTCarvedSignBlock block && block.isGlowing();
        int light = glowing ? LightTexture.FULL_BRIGHT : packedLight;
        int color = glowing ? 0xFFF2A84B : 0xFF2B170B;
        poseStack.pushPose();
        poseStack.translate(0.5D, 0.52D, 0.5D);
        poseStack.mulPose(Axis.YP.rotationDegrees(-rotation));
        poseStack.translate(0.0D, 0.0D, -0.255D);
        poseStack.scale(-0.010F, -0.010F, 0.010F);
        float x = -font.width(sign.getText()) / 2.0F;
        font.drawInBatch(sign.getText(), x, -4.0F, color, false, poseStack.last().pose(), buffer,
                Font.DisplayMode.POLYGON_OFFSET, 0, light);
        poseStack.popPose();
    }
}
