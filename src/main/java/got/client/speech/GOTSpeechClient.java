package got.client.speech;

import got.network.S2CNpcSpeechPacket;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Client cache matching the original GOTSpeechClient behavior.
 *
 * Legacy display lifetime: 200 ticks.
 *
 * Legacy chat behavior:
 * - immersive speech ON -> render overhead
 * - immersive + chat log OFF -> do not duplicate into chat
 * - forceChat -> always put the line in chat
 * - immersive speech OFF -> use chat as the fallback presentation
 */
public final class GOTSpeechClient {
    public static final int DISPLAY_TIME = 200;
    private static final Map<UUID, TimedSpeech> SPEECH = new HashMap<>();

    private GOTSpeechClient() {}

    public static void receive(S2CNpcSpeechPacket packet) {
        if (GOTSpeechClientSettings.immersiveSpeech()) {
            SPEECH.put(packet.entityUuid(), new TimedSpeech(packet.speech(), DISPLAY_TIME));
        }

        boolean showInChat =
            !GOTSpeechClientSettings.immersiveSpeech()
            || GOTSpeechClientSettings.immersiveSpeechChatLog()
            || packet.forceChat();

        if (showInChat) {
            Minecraft mc = Minecraft.getInstance();
            if (mc.player != null) {
                Component msg = Component.literal("<" + packet.npcName() + "> ")
                    .withStyle(ChatFormatting.YELLOW)
                    .append(Component.literal(packet.speech()).withStyle(ChatFormatting.WHITE));
                mc.player.displayClientMessage(msg, false);
            }
        }
    }

    public static TimedSpeech get(UUID npc) {
        if (!GOTSpeechClientSettings.immersiveSpeech()) return null;
        return SPEECH.get(npc);
    }

    public static void tick() {
        if (!GOTSpeechClientSettings.immersiveSpeech()) {
            SPEECH.clear();
            return;
        }

        SPEECH.replaceAll((uuid, timed) -> new TimedSpeech(timed.text(), timed.ticks() - 1));
        SPEECH.entrySet().removeIf(e -> e.getValue().ticks() <= 0);
    }

    public static void clear() {
        SPEECH.clear();
    }

    public record TimedSpeech(String text, int ticks) {}
}
