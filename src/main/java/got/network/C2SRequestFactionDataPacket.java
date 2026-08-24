package got.network;

import got.faction.GOTFactionService;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public final class C2SRequestFactionDataPacket {
    public static void encode(C2SRequestFactionDataPacket message, FriendlyByteBuf buffer) {}

    public static C2SRequestFactionDataPacket decode(FriendlyByteBuf buffer) {
        return new C2SRequestFactionDataPacket();
    }

    public static void handle(C2SRequestFactionDataPacket message,
                              Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            if (player != null) GOTFactionService.sync(player);
        });
        context.setPacketHandled(true);
    }
}
