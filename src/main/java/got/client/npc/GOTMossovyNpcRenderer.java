package got.client.npc;

import com.mojang.blaze3d.vertex.PoseStack;
import got.GOTMod;
import got.npc.GOTMossovyNpcEntity;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.resources.ResourceLocation;

/** Exact three-variant Mossovite civilian and Witcher renderer. */
public final class GOTMossovyNpcRenderer
        extends HumanoidMobRenderer<GOTMossovyNpcEntity, GOTMossovyNpcModel> {
    public GOTMossovyNpcRenderer(EntityRendererProvider.Context context) {
        super(context, new GOTMossovyNpcModel(context.bakeLayer(GOTMossovyNpcLayers.BASE)), 0.5F);
        addLayer(new HumanoidArmorLayer<>(this,
                new HumanoidModel<>(context.bakeLayer(GOTMossovyNpcLayers.ARMOR_INNER)),
                new HumanoidModel<>(context.bakeLayer(GOTMossovyNpcLayers.ARMOR_OUTER)),
                context.getModelManager()));
    }

    @Override public ResourceLocation getTextureLocation(GOTMossovyNpcEntity entity) {
        String gender = entity.isFemale() ? "female" : "male";
        if (entity.isBaby()) gender += "child";
        int skin = Math.floorMod(entity.getSkinIndex(), 3);
        return new ResourceLocation(GOTMod.MOD_ID,
                "textures/entity/essos/mossovy/" + gender + '/' + skin + ".png");
    }

    @Override protected void scale(GOTMossovyNpcEntity entity, PoseStack poseStack, float partialTick) {
        float scale = 0.9375F * entity.getRole().scale();
        poseStack.scale(scale, scale, scale);
    }
}
