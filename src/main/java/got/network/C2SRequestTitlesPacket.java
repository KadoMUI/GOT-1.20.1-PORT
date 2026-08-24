package got.network;

import got.player.GOTPlayerTitleData;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;
import java.util.function.Supplier;

public record C2SRequestTitlesPacket() {
    public static void encode(C2SRequestTitlesPacket p, FriendlyByteBuf b) {}
    public static C2SRequestTitlesPacket decode(FriendlyByteBuf b) { return new C2SRequestTitlesPacket(); }
    public static void handle(C2SRequestTitlesPacket p, Supplier<NetworkEvent.Context> s) {
        NetworkEvent.Context ctx=s.get(); ServerPlayer player=ctx.getSender();
        if (player != null) ctx.enqueueWork(() -> GOTNetwork.CHANNEL.send(
                PacketDistributor.PLAYER.with(() -> player),
                new S2CTitlesPacket(GOTPlayerTitleData.selectedId(player),
                        GOTPlayerTitleData.selectedColor(player),
                        GOTPlayerTitleData.unlockedIds(player))));
        ctx.setPacketHandled(true);
    }
}
