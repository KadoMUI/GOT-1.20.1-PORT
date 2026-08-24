package got.network;

import got.GOTSmithingMenu;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record C2SReforgeItemPacket() {
    public static void encode(C2SReforgeItemPacket packet, FriendlyByteBuf buffer) {}
    public static C2SReforgeItemPacket decode(FriendlyByteBuf buffer) {
        return new C2SReforgeItemPacket();
    }

    public static void handle(C2SReforgeItemPacket packet,
                              Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        ServerPlayer player = context.getSender();
        if (player != null) {
            context.enqueueWork(() -> {
                if (player.containerMenu instanceof GOTSmithingMenu menu) {
                    menu.reforge(player);
                }
            });
        }
        context.setPacketHandled(true);
    }
}
