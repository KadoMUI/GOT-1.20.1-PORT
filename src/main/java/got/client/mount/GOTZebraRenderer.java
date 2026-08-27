package got.client.mount;

import got.mount.GOTZebraEntity;
import net.minecraft.client.model.HorseModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

/** Zebra renderer using vanilla horse geometry with the recovered GOT zebra skin. */
public final class GOTZebraRenderer extends MobRenderer<GOTZebraEntity, HorseModel<GOTZebraEntity>> {
    private static final ResourceLocation TEX = new ResourceLocation("got", "textures/entity/animal/zebra.png");

    public GOTZebraRenderer(EntityRendererProvider.Context context) {
        super(context, new HorseModel<>(context.bakeLayer(ModelLayers.HORSE)), 0.75F);
    }

    @Override
    public ResourceLocation getTextureLocation(GOTZebraEntity entity) {
        return TEX;
    }
}
