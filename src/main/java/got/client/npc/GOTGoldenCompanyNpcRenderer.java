package got.client.npc;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import got.GOTMod;
import got.npc.GOTGoldenCompanyNpcEntity;
import got.npc.GoldenCompanyNpcRole;
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

/** Free Cities troop skins plus Harry Strickland's recovered two-layer texture. */
public final class GOTGoldenCompanyNpcRenderer
        extends HumanoidMobRenderer<GOTGoldenCompanyNpcEntity, GOTGoldenCompanyNpcModel> {
    public GOTGoldenCompanyNpcRenderer(EntityRendererProvider.Context context) {
        super(context, new GOTGoldenCompanyNpcModel(
                context.bakeLayer(GOTGoldenCompanyNpcLayers.BASE)), 0.5F);
        addLayer(new LegendaryOverlayLayer(this,
                new GOTGoldenCompanyNpcModel(context.bakeLayer(GOTGoldenCompanyNpcLayers.OUTFIT))));
        addLayer(new HumanoidArmorLayer<>(this,
                new HumanoidModel<>(context.bakeLayer(GOTGoldenCompanyNpcLayers.ARMOR_INNER)),
                new HumanoidModel<>(context.bakeLayer(GOTGoldenCompanyNpcLayers.ARMOR_OUTER)),
                context.getModelManager()));
    }

    @Override public ResourceLocation getTextureLocation(GOTGoldenCompanyNpcEntity entity) {
        GoldenCompanyNpcRole role = entity.getRole();
        if (role.legendary()) {
            return new ResourceLocation(GOTMod.MOD_ID, "textures/entity/legendary/"
                    + role.legendaryTexture() + role.legendaryBaseSuffix() + ".png");
        }
        int skin = Math.floorMod(entity.getSkinIndex(), 10);
        return new ResourceLocation(GOTMod.MOD_ID,
                "textures/entity/essos/free/male/" + skin + ".png");
    }

    @Override protected void scale(GOTGoldenCompanyNpcEntity entity, PoseStack poseStack,
                                   float partialTick) {
        float scale = 0.9375F * entity.getRole().scale();
        poseStack.scale(scale, scale, scale);
    }

    private static final class LegendaryOverlayLayer
            extends RenderLayer<GOTGoldenCompanyNpcEntity, GOTGoldenCompanyNpcModel> {
        private final GOTGoldenCompanyNpcModel outfitModel;
        private LegendaryOverlayLayer(GOTGoldenCompanyNpcRenderer parent,
                                      GOTGoldenCompanyNpcModel outfitModel) {
            super(parent);
            this.outfitModel = outfitModel;
        }
        @Override public void render(PoseStack poseStack, MultiBufferSource buffers, int light,
                                     GOTGoldenCompanyNpcEntity entity, float limbSwing,
                                     float limbSwingAmount, float partialTick, float ageInTicks,
                                     float netHeadYaw, float headPitch) {
            GoldenCompanyNpcRole role = entity.getRole();
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
