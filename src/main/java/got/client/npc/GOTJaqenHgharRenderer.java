package got.client.npc;

import got.GOTMod;
import got.npc.GOTJaqenHgharEntity;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.resources.ResourceLocation;

public final class GOTJaqenHgharRenderer extends HumanoidMobRenderer<GOTJaqenHgharEntity, GOTJaqenHgharModel> {
    private static final ResourceLocation TEX = ResourceLocation.fromNamespaceAndPath(GOTMod.MOD_ID, "textures/entity/legendary/jaqen_hghar.png");
    public GOTJaqenHgharRenderer(EntityRendererProvider.Context context) {
        super(context, new GOTJaqenHgharModel(context.bakeLayer(GOTBraavosNpcLayers.BASE)), 0.5F);
        addLayer(new HumanoidArmorLayer<>(this,
                new HumanoidModel<>(context.bakeLayer(GOTBraavosNpcLayers.ARMOR_INNER)),
                new HumanoidModel<>(context.bakeLayer(GOTBraavosNpcLayers.ARMOR_OUTER)), context.getModelManager()));
    }
    @Override public ResourceLocation getTextureLocation(GOTJaqenHgharEntity entity) { return TEX; }
}
