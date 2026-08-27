package got.client.mount;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import got.mount.GOTCamelEntity;
import got.mount.GOTMountEntity;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;

/** Legacy 1.7.10 saddle/carpet render passes. The old renderers used separate inflated mount models. */
public final class GOTLegacyMountEquipmentLayer<T extends GOTMountEntity> extends RenderLayer<T, GOTLegacyMountModel<T>> {
    public enum Mode { SADDLE, CAMEL_CARPET }
    private final GOTLegacyMountModel<T> model;
    private final ResourceLocation texture;
    private final Mode mode;
    public GOTLegacyMountEquipmentLayer(RenderLayerParent<T,GOTLegacyMountModel<T>> parent, GOTLegacyMountModel<T> model, String texture, Mode mode) {
        super(parent); this.model=model; this.texture=new ResourceLocation("got", texture); this.mode=mode;
    }
    @Override public void render(PoseStack ps, MultiBufferSource buffers, int light, T e, float limbSwing, float limbAmount, float partial, float age, float yaw, float pitch) {
        if (mode == Mode.SADDLE && !e.isSaddled()) return;
        if (mode == Mode.CAMEL_CARPET && (!(e instanceof GOTCamelEntity camel) || camel.getCarpetColor() < 0)) return;
        getParentModel().copyPropertiesTo(model);
        model.prepareMobModel(e, limbSwing, limbAmount, partial);
        model.setupAnim(e, limbSwing, limbAmount, age, yaw, pitch);
        ps.pushPose();
        // Legacy equipment models used +0.5/+0.6 box inflation. A tiny render scale gives the
        // same separation from the skin and prevents coplanar z-fighting in the modern model.
        ps.scale(mode == Mode.CAMEL_CARPET ? 1.025F : 1.02F, mode == Mode.CAMEL_CARPET ? 1.025F : 1.02F, mode == Mode.CAMEL_CARPET ? 1.025F : 1.02F);
        if (mode == Mode.CAMEL_CARPET) {
            int rgb=((GOTCamelEntity)e).getCarpetRgb();
            ResourceLocation base=new ResourceLocation("got","textures/entity/animal/camel/carpet_base.png");
            VertexConsumer baseVc=buffers.getBuffer(RenderType.entityCutoutNoCull(base));
            model.renderToBuffer(ps,baseVc,light,net.minecraft.client.renderer.texture.OverlayTexture.NO_OVERLAY,((rgb>>16)&255)/255f,((rgb>>8)&255)/255f,(rgb&255)/255f,1f);
            VertexConsumer overlayVc=buffers.getBuffer(RenderType.entityCutoutNoCull(texture));
            model.renderToBuffer(ps,overlayVc,light,net.minecraft.client.renderer.texture.OverlayTexture.NO_OVERLAY,1,1,1,1);
        } else {
            VertexConsumer vc=buffers.getBuffer(RenderType.entityCutoutNoCull(texture));
            model.renderToBuffer(ps,vc,light,net.minecraft.client.renderer.texture.OverlayTexture.NO_OVERLAY,1,1,1,1);
        }
        ps.popPose();
    }
}
