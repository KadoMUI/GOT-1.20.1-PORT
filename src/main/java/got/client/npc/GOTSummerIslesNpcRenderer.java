package got.client.npc;

import com.mojang.blaze3d.vertex.PoseStack;
import got.GOTMod;
import got.npc.GOTSummerIslesNpcEntity;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.resources.ResourceLocation;

/** Summer Islander civilian and armor renderer using the recovered regional skins. */
public final class GOTSummerIslesNpcRenderer
        extends HumanoidMobRenderer<GOTSummerIslesNpcEntity, GOTSouthernNpcModel<GOTSummerIslesNpcEntity>> {
    public GOTSummerIslesNpcRenderer(EntityRendererProvider.Context context) {
        super(context, new GOTSouthernNpcModel<>(context.bakeLayer(GOTNorvosNpcLayers.BASE)), 0.5F);
        addLayer(new HumanoidArmorLayer<>(this,
                new HumanoidModel<>(context.bakeLayer(GOTNorvosNpcLayers.ARMOR_INNER)),
                new HumanoidModel<>(context.bakeLayer(GOTNorvosNpcLayers.ARMOR_OUTER)),
                context.getModelManager()));
    }

    @Override
    public ResourceLocation getTextureLocation(GOTSummerIslesNpcEntity entity) {
        String gender = entity.isFemale() ? "female" : "male";
        if (entity.isBaby()) gender += "child";
        int variants = entity.isFemale() ? 4 : 5;
        int skin = Math.floorMod(entity.getSkinIndex(), variants);
        return new ResourceLocation(GOTMod.MOD_ID,
                "textures/entity/sothoryos/summer/" + gender + '/' + skin + ".png");
    }

    @Override
    protected void scale(GOTSummerIslesNpcEntity entity, PoseStack poseStack, float partialTick) {
        poseStack.scale(0.9375F, 0.9375F, 0.9375F);
    }
}
