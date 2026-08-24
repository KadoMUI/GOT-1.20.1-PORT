package got.client.npc;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import got.GOTMod;
import got.npc.GOTQohorNpcEntity;
import got.npc.QohorNpcRole;
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

/** Recovered Qohorik civilian, Unsullied, and equipment renderer. */
public final class GOTQohorNpcRenderer
        extends HumanoidMobRenderer<GOTQohorNpcEntity, GOTQohorNpcModel> {
    public GOTQohorNpcRenderer(EntityRendererProvider.Context context) {
        super(context, new GOTQohorNpcModel(
                context.bakeLayer(GOTQohorNpcLayers.BASE)), 0.5F);
        addLayer(new LegendaryOverlayLayer(this,
                new GOTQohorNpcModel(context.bakeLayer(GOTQohorNpcLayers.OUTFIT))));
        addLayer(new HumanoidArmorLayer<>(this,
                new HumanoidModel<>(context.bakeLayer(GOTQohorNpcLayers.ARMOR_INNER)),
                new HumanoidModel<>(context.bakeLayer(GOTQohorNpcLayers.ARMOR_OUTER)),
                context.getModelManager()));
    }

    @Override
    public ResourceLocation getTextureLocation(GOTQohorNpcEntity entity) {
        QohorNpcRole role = entity.getRole();
        if (role.legendary()) {
            return new ResourceLocation(GOTMod.MOD_ID, "textures/entity/legendary/"
                    + role.legendaryTexture() + role.legendaryBaseSuffix() + ".png");
        }
        if (role == QohorNpcRole.QOHOR_UNSULLIED) {
            int skin = Math.floorMod(entity.getSkinIndex(), 6);
            return new ResourceLocation(GOTMod.MOD_ID,
                    "textures/entity/essos/unsullied/" + skin + ".png");
        }

        String gender = entity.isFemale() ? "female" : "male";
        if (entity.isBaby()) gender += "child";
        int variants = entity.isBaby()
                ? (entity.isFemale() ? 9 : 5)
                : (entity.isFemale() ? 9 : 10);
        int skin = Math.floorMod(entity.getSkinIndex(), variants);
        return new ResourceLocation(GOTMod.MOD_ID,
                "textures/entity/essos/free/" + gender + '/' + skin + ".png");
    }

    @Override
    protected void scale(GOTQohorNpcEntity entity, PoseStack poseStack, float partialTick) {
        float scale = 0.9375F * entity.getRole().scale();
        poseStack.scale(scale, scale, scale);
    }

    private static final class LegendaryOverlayLayer
            extends RenderLayer<GOTQohorNpcEntity, GOTQohorNpcModel> {
        private final GOTQohorNpcModel outfitModel;

        private LegendaryOverlayLayer(GOTQohorNpcRenderer parent,
                                      GOTQohorNpcModel outfitModel) {
            super(parent);
            this.outfitModel = outfitModel;
        }

        @Override
        public void render(PoseStack poseStack, MultiBufferSource buffers, int light,
                           GOTQohorNpcEntity entity, float limbSwing, float limbSwingAmount,
                           float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {
            QohorNpcRole role = entity.getRole();
            if (!role.hasLegendaryOverlay()) return;
            ResourceLocation texture = new ResourceLocation(GOTMod.MOD_ID,
                    "textures/entity/legendary/" + role.legendaryTexture()
                            + role.legendaryOverlaySuffix() + ".png");
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
