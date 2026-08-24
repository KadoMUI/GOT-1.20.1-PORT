package got.network;

import got.client.quest.GOTClientQuestState;
import got.quest.GOTQuestView;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public record S2CQuestOfferPacket(GOTQuestView offer, UUID giverId) {
    public static void encode(S2CQuestOfferPacket message, FriendlyByteBuf buffer) {
        GOTQuestView.encode(message.offer, buffer);
        buffer.writeUUID(message.giverId);
    }

    public static S2CQuestOfferPacket decode(FriendlyByteBuf buffer) {
        return new S2CQuestOfferPacket(GOTQuestView.decode(buffer), buffer.readUUID());
    }

    public static void handle(S2CQuestOfferPacket message,
                              Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT,
                () -> () -> GOTClientQuestState.openOffer(message)));
        context.setPacketHandled(true);
    }
}
