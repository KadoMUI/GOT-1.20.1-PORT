package got.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;

import java.util.function.Supplier;

public final class C2SRequestPactDataPacket {
    public static void encode(C2SRequestPactDataPacket msg, FriendlyByteBuf buf) {}
    public static C2SRequestPactDataPacket decode(FriendlyByteBuf buf) { return new C2SRequestPactDataPacket(); }
    public static void handle(C2SRequestPactDataPacket msg, Supplier<NetworkEvent.Context> ctxSupplier) {
        NetworkEvent.Context ctx = ctxSupplier.get();
        ctx.enqueueWork(() -> {
            ServerPlayer player = ctx.getSender();
            if (player != null) GOTNetwork.CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), S2CPactDataPacket.forPlayer(player));
        });
        ctx.setPacketHandled(true);
    }
}
