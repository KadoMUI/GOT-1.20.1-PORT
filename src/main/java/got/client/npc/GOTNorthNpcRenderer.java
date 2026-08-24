package got.client.npc;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import got.GOTMod;
import got.npc.GOTNorthNpcEntity;
import got.npc.NorthNpcRole;
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

public final class GOTNorthNpcRenderer extends HumanoidMobRenderer<GOTNorthNpcEntity, GOTNorthNpcModel> {
    private static final ResourceLocation HILLMAN_OUTFIT =
            new ResourceLocation(GOTMod.MOD_ID, "textures/entity/westeros/wild/outfit.png");

    public GOTNorthNpcRenderer(EntityRendererProvider.Context context) {
        super(context, new GOTNorthNpcModel(context.bakeLayer(GOTNorthNpcLayers.BASE)), 0.5F);
        // Skin/outfit passes precede equipment so helmets and armor remain the
        // visible outer layer, matching the legacy render-pass ordering.
        addLayer(new LegacySecondSkinLayer(this,
                new GOTNorthNpcModel(context.bakeLayer(GOTNorthNpcLayers.OUTFIT))));
        addLayer(new HumanoidArmorLayer<>(this,
                new HumanoidModel<>(context.bakeLayer(GOTNorthNpcLayers.ARMOR_INNER)),
                new HumanoidModel<>(context.bakeLayer(GOTNorthNpcLayers.ARMOR_OUTER)),
                context.getModelManager()));
    }

    @Override
    public ResourceLocation getTextureLocation(GOTNorthNpcEntity entity) {
        NorthNpcRole role = entity.getRole();
        if (role.legendary()) {
            // Legacy "_1" and "_2" textures are two simultaneous render
            // layers, never random skin alternatives.
            String suffix = role.legendaryLayered() ? "_1" : "";
            return new ResourceLocation(GOTMod.MOD_ID,
                    "textures/entity/legendary/" + role.legendaryTexture() + suffix + ".png");
        }

        boolean wild = role.hillman();
        String gender = entity.isFemale() ? "female" : "male";
        if (entity.isBaby()) gender += "child";
        int variants = wild
                ? (entity.isFemale() ? 4 : 3)
                : entity.isBaby() ? (entity.isFemale() ? 6 : 3) : (entity.isFemale() ? 7 : 8);
        int skin = Math.floorMod(entity.getSkinIndex(), variants);
        return new ResourceLocation(GOTMod.MOD_ID, "textures/entity/westeros/"
                + (wild ? "wild/" : "north/") + gender + '/' + skin + ".png");
    }

    @Override
    protected void scale(GOTNorthNpcEntity entity, PoseStack poseStack, float partialTick) {
        // The original GOTRenderBiped applies 0.9375 to every human.  Child
        // head/body proportions are already handled by HumanoidModel's young
        // render path and must not be scaled a second time here.
        float scale = 0.9375F * entity.getRole().scale();
        poseStack.scale(scale, scale, scale);
    }

    private static final class LegacySecondSkinLayer extends RenderLayer<GOTNorthNpcEntity, GOTNorthNpcModel> {
        private final GOTNorthNpcModel outfitModel;

        private LegacySecondSkinLayer(GOTNorthNpcRenderer parent, GOTNorthNpcModel outfitModel) {
            super(parent);
            this.outfitModel = outfitModel;
        }

        @Override
        public void render(PoseStack poseStack, MultiBufferSource buffers, int light,
                           GOTNorthNpcEntity entity, float limbSwing, float limbSwingAmount,
                           float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {
            NorthNpcRole role = entity.getRole();
            ResourceLocation texture;
            if (role == NorthNpcRole.NORTH_HILLMAN) {
                texture = HILLMAN_OUTFIT;
            } else if (role.legendaryLayered()) {
                texture = new ResourceLocation(GOTMod.MOD_ID,
                        "textures/entity/legendary/" + role.legendaryTexture() + "_2.png");
            } else {
                return;
            }
            getParentModel().copyPropertiesTo(outfitModel);
            outfitModel.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
            VertexConsumer consumer = ItemRenderer.getArmorFoilBuffer(buffers,
                    RenderType.entityCutoutNoCull(texture), false, false);
            outfitModel.renderToBuffer(poseStack, consumer, light, OverlayTexture.NO_OVERLAY,
                    1.0F, 1.0F, 1.0F, 1.0F);
        }
    }
}
