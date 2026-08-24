package got.client.npc;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import got.GOTMod;
import got.npc.GOTWildlingNpcEntity;
import got.npc.WildlingNpcRole;
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

/** Wildling skin, legendary overlay and equipment renderer. */
public final class GOTWildlingNpcRenderer
        extends HumanoidMobRenderer<GOTWildlingNpcEntity, GOTWildlingNpcModel> {
    public GOTWildlingNpcRenderer(EntityRendererProvider.Context context) {
        super(context, new GOTWildlingNpcModel(
                context.bakeLayer(GOTWildlingNpcLayers.BASE)), 0.5F);
        addLayer(new LegendaryOverlayLayer(this,
                new GOTWildlingNpcModel(context.bakeLayer(GOTWildlingNpcLayers.OUTFIT))));
        addLayer(new HumanoidArmorLayer<>(this,
                new HumanoidModel<>(context.bakeLayer(GOTWildlingNpcLayers.ARMOR_INNER)),
                new HumanoidModel<>(context.bakeLayer(GOTWildlingNpcLayers.ARMOR_OUTER)),
                context.getModelManager()));
    }

    @Override
    public ResourceLocation getTextureLocation(GOTWildlingNpcEntity entity) {
        WildlingNpcRole role = entity.getRole();
        if (role.legendary()) {
            return new ResourceLocation(GOTMod.MOD_ID, "textures/entity/legendary/"
                    + role.legendaryTexture() + role.legendaryBaseSuffix() + ".png");
        }

        if (role == WildlingNpcRole.GIANT) {
            return new ResourceLocation(GOTMod.MOD_ID, "textures/entity/westeros/giant/giant/"
                    + Math.floorMod(entity.getSkinIndex(), 2) + ".png");
        }

        String gender = entity.isFemale() ? "female" : "male";
        if (entity.isBaby()) gender += "child";
        int variants = entity.isFemale() ? 4 : 3;
        int skin = Math.floorMod(entity.getSkinIndex(), variants);
        return new ResourceLocation(GOTMod.MOD_ID,
                "textures/entity/westeros/" + (role.thenn() ? "thenn" : "wild")
                        + '/' + gender + '/' + skin + ".png");
    }

    @Override
    protected void scale(GOTWildlingNpcEntity entity, PoseStack poseStack, float partialTick) {
        float scale = 0.9375F * entity.getRole().scale();
        poseStack.scale(scale, scale, scale);
    }

    private static final class LegendaryOverlayLayer
            extends RenderLayer<GOTWildlingNpcEntity, GOTWildlingNpcModel> {
        private final GOTWildlingNpcModel outfitModel;

        private LegendaryOverlayLayer(GOTWildlingNpcRenderer parent,
                                      GOTWildlingNpcModel outfitModel) {
            super(parent);
            this.outfitModel = outfitModel;
        }

        @Override
        public void render(PoseStack poseStack, MultiBufferSource buffers, int light,
                           GOTWildlingNpcEntity entity, float limbSwing, float limbSwingAmount,
                           float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {
            WildlingNpcRole role = entity.getRole();
            boolean wildOutfit = !role.legendary() && !role.thenn() && !role.giant();
            if (!role.hasLegendaryOverlay() && !wildOutfit) return;
            ResourceLocation texture = wildOutfit
                    ? new ResourceLocation(GOTMod.MOD_ID, "textures/entity/westeros/wild/outfit.png")
                    : new ResourceLocation(GOTMod.MOD_ID, "textures/entity/legendary/"
                            + role.legendaryTexture() + role.legendaryOverlaySuffix() + ".png");
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
