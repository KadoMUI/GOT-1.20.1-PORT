package got.speech;

import got.network.GOTNetwork;
import got.network.S2CNpcSpeechPacket;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.PacketDistributor;

/** Public server-side API for NPC speech, quests, hiring and future trading. */
public final class GOTSpeechService {
    private GOTSpeechService() {}

    public static String line(Entity npc, ServerPlayer player, String bank) {
        return line(npc, player, bank, null, null);
    }

    public static String line(Entity npc, ServerPlayer player, String bank, CharSequence arg1, CharSequence arg2) {
        String raw = GOTSpeechBankRegistry.random(bank);
        return GOTSpeechFormatter.format(raw, player, arg1, arg2);
    }

    public static void speak(Entity npc, ServerPlayer player, String bank) {
        speak(npc, player, bank, false, null, null);
    }

    public static void speakAndLog(Entity npc, ServerPlayer player, String bank) {
        speak(npc, player, bank, true, null, null);
    }

    public static void speak(Entity npc, ServerPlayer player, String bank, boolean forceChat,
                             CharSequence arg1, CharSequence arg2) {
        String speech = line(npc, player, bank, arg1, arg2);
        GOTNetwork.CHANNEL.send(
            PacketDistributor.PLAYER.with(() -> player),
            new S2CNpcSpeechPacket(npc.getId(), npc.getUUID(), npc.getDisplayName().getString(), speech, forceChat)
        );
        GOTSpeechBehaviorData.markSpoken(npc);
    }

    public static void speakLine(Entity npc, ServerPlayer player, String bank, int line) {
        String raw = GOTSpeechBankRegistry.at(bank, line);
        String speech = GOTSpeechFormatter.format(raw, player, null, null);
        GOTNetwork.CHANNEL.send(
            PacketDistributor.PLAYER.with(() -> player),
            new S2CNpcSpeechPacket(npc.getId(), npc.getUUID(), npc.getDisplayName().getString(), speech, false)
        );
        GOTSpeechBehaviorData.markSpoken(npc);
    }

    public static void speakDefault(Entity npc, ServerPlayer player) {
        speak(npc, player, GOTSpeechSelector.defaultBank(npc, player));
    }
}
