package got.client.npc;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import got.GOTMod;
import got.npc.GOTVolantisNpcEntity;
import got.npc.VolantisNpcRole;
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

/** Recovered Volantene, slave, and equipment renderer. */
public final class GOTVolantisNpcRenderer
        extends HumanoidMobRenderer<GOTVolantisNpcEntity, GOTVolantisNpcModel> {
    public GOTVolantisNpcRenderer(EntityRendererProvider.Context context) {
        super(context, new GOTVolantisNpcModel(
                context.bakeLayer(GOTVolantisNpcLayers.BASE)), 0.5F);
        addLayer(new LegendaryOverlayLayer(this,
                new GOTVolantisNpcModel(context.bakeLayer(GOTVolantisNpcLayers.OUTFIT))));
        addLayer(new HumanoidArmorLayer<>(this,
                new HumanoidModel<>(context.bakeLayer(GOTVolantisNpcLayers.ARMOR_INNER)),
                new HumanoidModel<>(context.bakeLayer(GOTVolantisNpcLayers.ARMOR_OUTER)),
                context.getModelManager()));
    }

    @Override
    public ResourceLocation getTextureLocation(GOTVolantisNpcEntity entity) {
        VolantisNpcRole role = entity.getRole();
        if (role.legendary()) {
            return new ResourceLocation(GOTMod.MOD_ID, "textures/entity/legendary/"
                    + role.legendaryTexture() + role.legendaryBaseSuffix() + ".png");
        }

        String gender = entity.isFemale() ? "female" : "male";
        if (role == VolantisNpcRole.VOLANTIS_SLAVE) {
            int variants = entity.isFemale() ? 3 : 4;
            int skin = Math.floorMod(entity.getSkinIndex(), variants);
            return new ResourceLocation(GOTMod.MOD_ID,
                    "textures/entity/essos/slave/" + gender + '/' + skin + ".png");
        }
        if (entity.isBaby()) gender += "child";
        int variants = entity.isBaby()
                ? (entity.isFemale() ? 10 : 6)
                : (entity.isFemale() ? 10 : 12);
        int skin = Math.floorMod(entity.getSkinIndex(), variants);
        return new ResourceLocation(GOTMod.MOD_ID,
                "textures/entity/essos/violet/" + gender + '/' + skin + ".png");
    }

    @Override
    protected void scale(GOTVolantisNpcEntity entity, PoseStack poseStack, float partialTick) {
        float scale = 0.9375F * entity.getRole().scale();
        poseStack.scale(scale, scale, scale);
    }

    private static final class LegendaryOverlayLayer
            extends RenderLayer<GOTVolantisNpcEntity, GOTVolantisNpcModel> {
        private final GOTVolantisNpcModel outfitModel;

        private LegendaryOverlayLayer(GOTVolantisNpcRenderer parent,
                                      GOTVolantisNpcModel outfitModel) {
            super(parent);
            this.outfitModel = outfitModel;
        }

        @Override
        public void render(PoseStack poseStack, MultiBufferSource buffers, int light,
                           GOTVolantisNpcEntity entity, float limbSwing, float limbSwingAmount,
                           float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {
            VolantisNpcRole role = entity.getRole();
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
