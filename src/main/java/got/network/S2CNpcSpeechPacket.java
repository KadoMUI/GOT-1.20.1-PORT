package got.network;

import got.client.speech.GOTSpeechClient;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

/** Modern equivalent of GOTPacketNPCSpeech. */
public record S2CNpcSpeechPacket(
    int entityId,
    UUID entityUuid,
    String npcName,
    String speech,
    boolean forceChat
) {
    public static void encode(S2CNpcSpeechPacket p, FriendlyByteBuf b) {
        b.writeVarInt(p.entityId);
        b.writeUUID(p.entityUuid);
        b.writeUtf(p.npcName, 256);
        b.writeUtf(p.speech, 32767);
        b.writeBoolean(p.forceChat);
    }

    public static S2CNpcSpeechPacket decode(FriendlyByteBuf b) {
        return new S2CNpcSpeechPacket(b.readVarInt(), b.readUUID(), b.readUtf(256), b.readUtf(32767), b.readBoolean());
    }

    public static void handle(S2CNpcSpeechPacket p, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> GOTSpeechClient.receive(p)));
        ctx.get().setPacketHandled(true);
    }
}
