package got.client.quest;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import got.GOTMod;
import got.client.speech.GOTSpeechClient;
import got.quest.GOTQuestGiver;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.joml.Matrix4f;

/** Restores the legacy floating exclamation mark shown over NPCs with an available miniquest. */
@Mod.EventBusSubscriber(modid = GOTMod.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class GOTQuestOfferIndicatorRenderer {
    private static final ResourceLocation ICON = ResourceLocation.fromNamespaceAndPath(GOTMod.MOD_ID, "textures/item/quest_offer.png");
    private GOTQuestOfferIndicatorRenderer() {}

    @SubscribeEvent
    public static void render(RenderLivingEvent.Post<?, ?> event) {
        LivingEntity entity = event.getEntity();
        if (!(entity instanceof GOTQuestGiver)) return;

        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || entity.distanceToSqr(mc.player) > 256.0D) return;
        if (GOTSpeechClient.get(entity.getUUID()) != null) return; // legacy behavior: speech takes visual priority

        int rgb = GOTQuestOfferIndicators.color(entity.getId());
        if (rgb < 0) return;

        float red = ((rgb >> 16) & 255) / 255.0F;
        float green = ((rgb >> 8) & 255) / 255.0F;
        float blue = (rgb & 255) / 255.0F;

        PoseStack pose = event.getPoseStack();
        MultiBufferSource buffers = event.getMultiBufferSource();
        pose.pushPose();
        pose.translate(0.0D, entity.getBbHeight() + 0.95D, 0.0D);
        pose.mulPose(mc.getEntityRenderDispatcher().cameraOrientation());
        pose.scale(0.65F, 0.65F, 0.65F);

        Matrix4f matrix = pose.last().pose();
        VertexConsumer out = buffers.getBuffer(RenderType.entityCutoutNoCull(ICON));
        // Texture is a transparent 32x32 legacy quest-offer icon. Render it fullbright and billboarded.
        vertex(out, matrix, -0.5F, -0.5F, 0.0F, 1.0F, red, green, blue);
        vertex(out, matrix,  0.5F, -0.5F, 1.0F, 1.0F, red, green, blue);
        vertex(out, matrix,  0.5F,  0.5F, 1.0F, 0.0F, red, green, blue);
        vertex(out, matrix, -0.5F,  0.5F, 0.0F, 0.0F, red, green, blue);
        pose.popPose();
    }

    private static void vertex(VertexConsumer out, Matrix4f matrix, float x, float y, float u, float v,
                               float r, float g, float b) {
        out.vertex(matrix, x, y, 0.0F).color(r, g, b, 1.0F).uv(u, v)
                .overlayCoords(0).uv2(15728880).normal(0.0F, 0.0F, 1.0F).endVertex();
    }

    @SubscribeEvent
    public static void tick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END && Minecraft.getInstance().level == null) {
            GOTQuestOfferIndicators.clear();
        }
    }
}
