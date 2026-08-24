package got.network;

import got.player.GOTPlayerOptions;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;
import java.util.function.Supplier;

public record C2SRequestOptionsPacket() {
    public static void encode(C2SRequestOptionsPacket p, FriendlyByteBuf b) {}
    public static C2SRequestOptionsPacket decode(FriendlyByteBuf b) { return new C2SRequestOptionsPacket(); }
    public static void handle(C2SRequestOptionsPacket p, Supplier<NetworkEvent.Context> s) {
        NetworkEvent.Context ctx=s.get(); ServerPlayer player=ctx.getSender();
        if (player != null) ctx.enqueueWork(() -> GOTNetwork.CHANNEL.send(
                PacketDistributor.PLAYER.with(() -> player),
                new S2COptionsPacket(GOTPlayerOptions.snapshot(player))));
        ctx.setPacketHandled(true);
    }
}
