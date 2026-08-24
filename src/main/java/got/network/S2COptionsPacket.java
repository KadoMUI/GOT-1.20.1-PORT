package got.network;

import got.client.player.GOTClientOptionsState;
import got.client.speech.GOTSpeechClientSettings;
import got.player.GOTPlayerOptions;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import java.util.function.Supplier;

public record S2COptionsPacket(GOTPlayerOptions.Snapshot state) {
    public static void encode(S2COptionsPacket p, FriendlyByteBuf b) {
        var s=p.state;
        b.writeBoolean(s.friendlyFire()); b.writeBoolean(s.hiredDeathMessages());
        b.writeBoolean(s.showAlignment()); b.writeBoolean(s.showMapLocation());
        b.writeBoolean(s.conquestKills()); b.writeBoolean(s.feminineRanks());
        b.writeBoolean(s.immersiveSpeech()); b.writeBoolean(s.immersiveSpeechChat());
    }
    public static S2COptionsPacket decode(FriendlyByteBuf b) {
        return new S2COptionsPacket(new GOTPlayerOptions.Snapshot(
                b.readBoolean(),b.readBoolean(),b.readBoolean(),b.readBoolean(),
                b.readBoolean(),b.readBoolean(),b.readBoolean(),b.readBoolean()));
    }
    public static void handle(S2COptionsPacket p, Supplier<NetworkEvent.Context> s) {
        s.get().enqueueWork(() -> {
            GOTClientOptionsState.set(p.state);
            GOTSpeechClientSettings.setImmersiveSpeech(p.state.immersiveSpeech());
            GOTSpeechClientSettings.setImmersiveSpeechChatLog(p.state.immersiveSpeechChat());
        });
        s.get().setPacketHandled(true);
    }
}
