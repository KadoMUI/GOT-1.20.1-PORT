package got.network;

import got.quest.GOTQuestService;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public final class C2SRequestQuestDataPacket {
    public static void encode(C2SRequestQuestDataPacket message, FriendlyByteBuf buffer) {}

    public static C2SRequestQuestDataPacket decode(FriendlyByteBuf buffer) {
        return new C2SRequestQuestDataPacket();
    }

    public static void handle(C2SRequestQuestDataPacket message,
                              Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        ServerPlayer player = context.getSender();
        context.enqueueWork(() -> {
            if (player != null) GOTQuestService.sync(player);
        });
        context.setPacketHandled(true);
    }
}
