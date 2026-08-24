package got.client.npc;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import got.GOTMod;
import got.npc.GOTGhiscarNpcEntity;
import got.npc.GhiscarNpcRole;
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

/** Recovered Ghiscari, slave, legendary, and equipment renderer. */
public final class GOTGhiscarNpcRenderer
        extends HumanoidMobRenderer<GOTGhiscarNpcEntity, GOTGhiscarNpcModel> {
    public GOTGhiscarNpcRenderer(EntityRendererProvider.Context context) {
        super(context, new GOTGhiscarNpcModel(
                context.bakeLayer(GOTGhiscarNpcLayers.BASE)), 0.5F);
        addLayer(new LegendaryOverlayLayer(this,
                new GOTGhiscarNpcModel(context.bakeLayer(GOTGhiscarNpcLayers.OUTFIT))));
        addLayer(new HumanoidArmorLayer<>(this,
                new HumanoidModel<>(context.bakeLayer(GOTGhiscarNpcLayers.ARMOR_INNER)),
                new HumanoidModel<>(context.bakeLayer(GOTGhiscarNpcLayers.ARMOR_OUTER)),
                context.getModelManager()));
    }

    @Override
    public ResourceLocation getTextureLocation(GOTGhiscarNpcEntity entity) {
        GhiscarNpcRole role = entity.getRole();
        if (role.legendary()) {
            return new ResourceLocation(GOTMod.MOD_ID, "textures/entity/legendary/"
                    + role.legendaryTexture() + role.legendaryBaseSuffix() + ".png");
        }

        String gender = entity.isFemale() ? "female" : "male";
        if (role == GhiscarNpcRole.GHISCAR_SLAVE) {
            int variants = entity.isFemale() ? 3 : 4;
            int skin = Math.floorMod(entity.getSkinIndex(), variants);
            return new ResourceLocation(GOTMod.MOD_ID,
                    "textures/entity/essos/slave/" + gender + '/' + skin + ".png");
        }
        if (role == GhiscarNpcRole.GHISCAR_UNSULLIED) {
            int skin = Math.floorMod(entity.getSkinIndex(), 6);
            return new ResourceLocation(GOTMod.MOD_ID,
                    "textures/entity/essos/unsullied/" + skin + ".png");
        }
        if (entity.isBaby()) gender += "child";
        int variants = entity.isBaby()
                ? 3
                : (entity.isFemale() ? 3 : 5);
        int skin = Math.floorMod(entity.getSkinIndex(), variants);
        return new ResourceLocation(GOTMod.MOD_ID,
                "textures/entity/essos/ghiscar/" + gender + '/' + skin + ".png");
    }

    @Override
    protected void scale(GOTGhiscarNpcEntity entity, PoseStack poseStack, float partialTick) {
        float scale = 0.9375F * entity.getRole().scale();
        poseStack.scale(scale, scale, scale);
    }

    private static final class LegendaryOverlayLayer
            extends RenderLayer<GOTGhiscarNpcEntity, GOTGhiscarNpcModel> {
        private final GOTGhiscarNpcModel outfitModel;

        private LegendaryOverlayLayer(GOTGhiscarNpcRenderer parent,
                                      GOTGhiscarNpcModel outfitModel) {
            super(parent);
            this.outfitModel = outfitModel;
        }

        @Override
        public void render(PoseStack poseStack, MultiBufferSource buffers, int light,
                           GOTGhiscarNpcEntity entity, float limbSwing, float limbSwingAmount,
                           float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {
            GhiscarNpcRole role = entity.getRole();
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
