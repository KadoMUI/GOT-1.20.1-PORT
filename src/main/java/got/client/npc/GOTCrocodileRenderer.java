package got.client.npc;

import got.GOTMod;
import got.npc.GOTCrocodileEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public final class GOTCrocodileRenderer extends MobRenderer<GOTCrocodileEntity, GOTCrocodileModel> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(GOTMod.MOD_ID, "textures/entity/animal/crocodile.png");
    public GOTCrocodileRenderer(EntityRendererProvider.Context context) {
        super(context, new GOTCrocodileModel(context.bakeLayer(GOTCrocodileLayers.BASE)), 0.6F);
    }
    @Override public ResourceLocation getTextureLocation(GOTCrocodileEntity entity) { return TEXTURE; }
}
