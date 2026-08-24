package got.network;

import got.GOTSmithingMenu;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record C2SRenameSmithingItemPacket(String name) {
    public static void encode(C2SRenameSmithingItemPacket packet, FriendlyByteBuf buffer) {
        buffer.writeUtf(packet.name, 50);
    }

    public static C2SRenameSmithingItemPacket decode(FriendlyByteBuf buffer) {
        return new C2SRenameSmithingItemPacket(buffer.readUtf(50));
    }

    public static void handle(C2SRenameSmithingItemPacket packet, Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context ctx = supplier.get();
        ServerPlayer player = ctx.getSender();
        if (player != null) ctx.enqueueWork(() -> {
            if (player.containerMenu instanceof GOTSmithingMenu menu) menu.rename(player, packet.name);
        });
        ctx.setPacketHandled(true);
    }
}
