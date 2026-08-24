package got.client.npc;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import got.GOTMod;
import got.npc.GOTNightWatchNpcEntity;
import got.npc.NightWatchNpcRole;
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

/** Night Watch skin, legendary overlay and equipment renderer. */
public final class GOTNightWatchNpcRenderer
        extends HumanoidMobRenderer<GOTNightWatchNpcEntity, GOTNightWatchNpcModel> {
    public GOTNightWatchNpcRenderer(EntityRendererProvider.Context context) {
        super(context, new GOTNightWatchNpcModel(
                context.bakeLayer(GOTNightWatchNpcLayers.BASE)), 0.5F);
        addLayer(new LegendaryOverlayLayer(this,
                new GOTNightWatchNpcModel(context.bakeLayer(GOTNightWatchNpcLayers.OUTFIT))));
        addLayer(new HumanoidArmorLayer<>(this,
                new HumanoidModel<>(context.bakeLayer(GOTNightWatchNpcLayers.ARMOR_INNER)),
                new HumanoidModel<>(context.bakeLayer(GOTNightWatchNpcLayers.ARMOR_OUTER)),
                context.getModelManager()));
    }

    @Override
    public ResourceLocation getTextureLocation(GOTNightWatchNpcEntity entity) {
        NightWatchNpcRole role = entity.getRole();
        if (role.legendary()) {
            return new ResourceLocation(GOTMod.MOD_ID, "textures/entity/legendary/"
                    + role.legendaryTexture() + role.legendaryBaseSuffix() + ".png");
        }

        String gender = entity.isFemale() ? "female" : "male";
        if (entity.isBaby()) gender += "child";
        int variants = 3;
        int skin = Math.floorMod(entity.getSkinIndex(), variants);
        return new ResourceLocation(GOTMod.MOD_ID,
                "textures/entity/westeros/gift/" + gender + '/' + skin + ".png");
    }

    @Override
    protected void scale(GOTNightWatchNpcEntity entity, PoseStack poseStack, float partialTick) {
        float scale = 0.9375F * entity.getRole().scale();
        poseStack.scale(scale, scale, scale);
    }

    private static final class LegendaryOverlayLayer
            extends RenderLayer<GOTNightWatchNpcEntity, GOTNightWatchNpcModel> {
        private final GOTNightWatchNpcModel outfitModel;

        private LegendaryOverlayLayer(GOTNightWatchNpcRenderer parent,
                                      GOTNightWatchNpcModel outfitModel) {
            super(parent);
            this.outfitModel = outfitModel;
        }

        @Override
        public void render(PoseStack poseStack, MultiBufferSource buffers, int light,
                           GOTNightWatchNpcEntity entity, float limbSwing, float limbSwingAmount,
                           float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {
            NightWatchNpcRole role = entity.getRole();
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
