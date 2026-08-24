package got.client.npc;

import com.mojang.blaze3d.vertex.PoseStack;
import got.GOTMod;
import got.npc.GOTUlthosSpiderEntity;
import net.minecraft.client.model.SpiderModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

/** Scale- and status-aware renderer for the three legacy Ulthos spider textures. */
public final class GOTUlthosSpiderRenderer
        extends MobRenderer<GOTUlthosSpiderEntity, SpiderModel<GOTUlthosSpiderEntity>> {
    public GOTUlthosSpiderRenderer(EntityRendererProvider.Context context) {
        super(context, new SpiderModel<>(context.bakeLayer(ModelLayers.SPIDER)), 0.8F);
    }

    @Override
    public ResourceLocation getTextureLocation(GOTUlthosSpiderEntity entity) {
        String texture = switch (entity.getVariant()) {
            case SLOWNESS -> "spiderSlowness.png";
            case POISON -> "spiderPoison.png";
            default -> "spider.png";
        };
        return new ResourceLocation(GOTMod.MOD_ID, "textures/entity/ulthos/" + texture);
    }

    @Override
    protected void scale(GOTUlthosSpiderEntity entity, PoseStack poseStack, float partialTick) {
        float scale = entity.getVisualScale();
        poseStack.scale(scale, scale, scale);
    }
}
