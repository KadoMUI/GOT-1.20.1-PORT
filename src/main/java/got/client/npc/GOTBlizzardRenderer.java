package got.client.npc;

import got.GOTMod;
import got.npc.GOTBlizzardEntity;
import net.minecraft.client.model.BlazeModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;

/** Full-bright Blaze geometry with the recovered Ulthos Blizzard texture. */
public final class GOTBlizzardRenderer
        extends MobRenderer<GOTBlizzardEntity, BlazeModel<GOTBlizzardEntity>> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(
            GOTMod.MOD_ID, "textures/entity/ulthos/blizzard.png");

    public GOTBlizzardRenderer(EntityRendererProvider.Context context) {
        super(context, new BlazeModel<>(context.bakeLayer(ModelLayers.BLAZE)), 0.5F);
    }

    @Override public ResourceLocation getTextureLocation(GOTBlizzardEntity entity) { return TEXTURE; }
    @Override protected int getBlockLightLevel(GOTBlizzardEntity entity, BlockPos pos) { return 15; }
}
