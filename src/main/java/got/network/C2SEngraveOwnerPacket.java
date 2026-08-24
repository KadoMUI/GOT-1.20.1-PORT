package got.network;

import got.GOTSmithingMenu;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record C2SEngraveOwnerPacket() {
    public static void encode(C2SEngraveOwnerPacket packet, FriendlyByteBuf buffer) {}
    public static C2SEngraveOwnerPacket decode(FriendlyByteBuf buffer) { return new C2SEngraveOwnerPacket(); }

    public static void handle(C2SEngraveOwnerPacket packet, Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context ctx = supplier.get();
        ServerPlayer player = ctx.getSender();
        if (player != null) ctx.enqueueWork(() -> {
            if (player.containerMenu instanceof GOTSmithingMenu menu) menu.engraveOwner(player);
        });
        ctx.setPacketHandled(true);
    }
}
