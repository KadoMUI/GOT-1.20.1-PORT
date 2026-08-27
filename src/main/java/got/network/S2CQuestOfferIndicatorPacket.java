package got.network;

import got.client.quest.GOTQuestOfferIndicators;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

/** Per-player quest-offer marker state, mirroring the legacy NPC quest indicator packet. */
public record S2CQuestOfferIndicatorPacket(int entityId, boolean offering, int color) {
    public static void encode(S2CQuestOfferIndicatorPacket msg, FriendlyByteBuf buf) {
        buf.writeVarInt(msg.entityId);
        buf.writeBoolean(msg.offering);
        buf.writeInt(msg.color);
    }

    public static S2CQuestOfferIndicatorPacket decode(FriendlyByteBuf buf) {
        return new S2CQuestOfferIndicatorPacket(buf.readVarInt(), buf.readBoolean(), buf.readInt());
    }

    public static void handle(S2CQuestOfferIndicatorPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT,
                () -> () -> GOTQuestOfferIndicators.update(msg.entityId, msg.offering, msg.color)));
        ctx.get().setPacketHandled(true);
    }
}
