package got.client.speech;

import got.GOTMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.joml.Matrix4f;

import java.util.ArrayList;
import java.util.List;

/** Immersive overhead speech rendering, equivalent in purpose to GOTSpeechClient. */
@Mod.EventBusSubscriber(modid = GOTMod.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class GOTSpeechClientEvents {
    private GOTSpeechClientEvents() {}

    @SubscribeEvent
    public static void clientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        if (Minecraft.getInstance().level == null) GOTSpeechClient.clear();
        else GOTSpeechClient.tick();
    }

    @SubscribeEvent
    public static void renderSpeech(RenderLivingEvent.Post<?, ?> event) {
        LivingEntity entity = event.getEntity();
        GOTSpeechClient.TimedSpeech speech = GOTSpeechClient.get(entity.getUUID());
        if (speech == null || speech.text().isBlank()) return;

        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || entity.distanceToSqr(mc.player) > 1024.0D) return;

        var pose = event.getPoseStack();
        MultiBufferSource buffers = event.getMultiBufferSource();
        Font font = mc.font;
        EntityRenderDispatcher dispatcher = mc.getEntityRenderDispatcher();

        List<String> lines = wrap(font, speech.text(), 180);
        pose.pushPose();
        pose.translate(0.0D, entity.getBbHeight() + 0.65D + lines.size() * 0.13D, 0.0D);
        pose.mulPose(dispatcher.cameraOrientation());
        pose.scale(-0.025F, -0.025F, 0.025F);

        Matrix4f matrix = pose.last().pose();
        int y = -(lines.size() * 10);
        for (String line : lines) {
            float x = -font.width(line) / 2.0F;
            font.drawInBatch(line, x, y, 0xFFFFFFFF, false, matrix, buffers,
                Font.DisplayMode.NORMAL, 0x66000000, 15728880);
            y += 10;
        }
        pose.popPose();
    }

    private static List<String> wrap(Font font, String text, int maxWidth) {
        List<String> result = new ArrayList<>();
        StringBuilder line = new StringBuilder();
        for (String word : text.split("\\s+")) {
            String candidate = line.isEmpty() ? word : line + " " + word;
            if (!line.isEmpty() && font.width(candidate) > maxWidth) {
                result.add(line.toString());
                line = new StringBuilder(word);
            } else {
                line = new StringBuilder(candidate);
            }
        }
        if (!line.isEmpty()) result.add(line.toString());
        return result;
    }
}
