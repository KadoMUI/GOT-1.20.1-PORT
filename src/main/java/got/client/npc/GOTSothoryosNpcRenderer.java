package got.client.npc;

import com.mojang.blaze3d.vertex.PoseStack;
import got.GOTMod;
import got.npc.GOTSothoryosNpcEntity;
import got.npc.SothoryosNpcRole;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.resources.ResourceLocation;

/** Sothoryosi skin, shaman and equipment renderer. */
public final class GOTSothoryosNpcRenderer
        extends HumanoidMobRenderer<GOTSothoryosNpcEntity, GOTSouthernNpcModel<GOTSothoryosNpcEntity>> {
    public GOTSothoryosNpcRenderer(EntityRendererProvider.Context context) {
        super(context, new GOTSouthernNpcModel<>(context.bakeLayer(GOTNorvosNpcLayers.BASE)), 0.5F);
        addLayer(new HumanoidArmorLayer<>(this,
                new HumanoidModel<>(context.bakeLayer(GOTNorvosNpcLayers.ARMOR_INNER)),
                new HumanoidModel<>(context.bakeLayer(GOTNorvosNpcLayers.ARMOR_OUTER)),
                context.getModelManager()));
    }

    @Override
    public ResourceLocation getTextureLocation(GOTSothoryosNpcEntity entity) {
        if (entity.getSothoryosRole() == SothoryosNpcRole.SOTHORYOS_SHAMAN) {
            return new ResourceLocation(GOTMod.MOD_ID,
                    "textures/entity/sothoryos/sothoryos/shaman.png");
        }
        String gender = entity.isFemale() ? "female" : "male";
        if (entity.isBaby()) gender += "child";
        int variants = entity.isFemale() ? 3 : 4;
        int skin = Math.floorMod(entity.getSkinIndex(), variants);
        return new ResourceLocation(GOTMod.MOD_ID,
                "textures/entity/sothoryos/sothoryos/" + gender + '/' + skin + ".png");
    }

    @Override
    protected void scale(GOTSothoryosNpcEntity entity, PoseStack poseStack, float partialTick) {
        poseStack.scale(0.9375F, 0.9375F, 0.9375F);
    }
}
