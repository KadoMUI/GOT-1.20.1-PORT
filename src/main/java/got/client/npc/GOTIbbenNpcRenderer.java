package got.client.npc;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import got.GOTMod;
import got.npc.GOTIbbenNpcEntity;
import got.npc.IbbenNpcRole;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

/** Recovered Ibbenese layered civilian and military renderer. */
public final class GOTIbbenNpcRenderer
        extends HumanoidMobRenderer<GOTIbbenNpcEntity, GOTIbbenNpcModel> {
    public GOTIbbenNpcRenderer(EntityRendererProvider.Context context) {
        super(context, new GOTIbbenNpcModel(
                context.bakeLayer(GOTIbbenNpcLayers.BASE)), 0.5F);
        addLayer(new IbbenOutfitLayer(this,
                new GOTIbbenNpcModel(context.bakeLayer(GOTIbbenNpcLayers.OUTFIT))));
        addLayer(new HumanoidArmorLayer<>(this,
                new HumanoidModel<>(context.bakeLayer(GOTIbbenNpcLayers.ARMOR_INNER)),
                new HumanoidModel<>(context.bakeLayer(GOTIbbenNpcLayers.ARMOR_OUTER)),
                context.getModelManager()));
    }

    @Override
    public ResourceLocation getTextureLocation(GOTIbbenNpcEntity entity) {
        IbbenNpcRole role = entity.getRole();
        String gender = entity.isFemale() ? "female" : "male";
        if (entity.isBaby()) gender += "child";
        int variants = entity.isBaby()
                ? (entity.isFemale() ? 4 : 3)
                : (entity.isFemale() ? 4 : 3);
        int skin = Math.floorMod(entity.getSkinIndex(), variants);
        return new ResourceLocation(GOTMod.MOD_ID,
                "textures/entity/essos/ibben/" + gender + '/' + skin + ".png");
    }

    @Override
    protected void scale(GOTIbbenNpcEntity entity, PoseStack poseStack, float partialTick) {
        float scale = 0.9375F * entity.getRole().scale();
        poseStack.scale(scale, scale, scale);
    }

    private static final class IbbenOutfitLayer
            extends RenderLayer<GOTIbbenNpcEntity, GOTIbbenNpcModel> {
        private final GOTIbbenNpcModel outfitModel;

        private IbbenOutfitLayer(GOTIbbenNpcRenderer parent,
                                GOTIbbenNpcModel outfitModel) {
            super(parent);
            this.outfitModel = outfitModel;
        }

        @Override
        public void render(PoseStack poseStack, MultiBufferSource buffers, int light,
                           GOTIbbenNpcEntity entity, float limbSwing, float limbSwingAmount,
                           float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {
            IbbenNpcRole role = entity.getRole();
            if (!role.usesOutfitOverlay()) return;
            ResourceLocation texture = new ResourceLocation(GOTMod.MOD_ID,
                    "textures/entity/essos/ibben/outfit.png");
            getParentModel().copyPropertiesTo(outfitModel);
            outfitModel.setupAnim(entity, limbSwing, limbSwingAmount,
                    ageInTicks, netHeadYaw, headPitch);
            VertexConsumer consumer = ItemRenderer.getArmorFoilBuffer(buffers,
                    RenderType.entityCutoutNoCull(texture), false, false);
            outfitModel.renderToBuffer(poseStack, consumer, light, OverlayTexture.NO_OVERLAY,
                    1.0F, 1.0F, 1.0F, 1.0F);
        }
    }
}
