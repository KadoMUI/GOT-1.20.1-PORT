package got.network;

import got.player.GOTPlayerOptions;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;
import java.util.function.Supplier;

public record C2SToggleOptionPacket(GOTPlayerOptions.Option option) {
    public static void encode(C2SToggleOptionPacket p, FriendlyByteBuf b) { b.writeEnum(p.option); }
    public static C2SToggleOptionPacket decode(FriendlyByteBuf b) {
        return new C2SToggleOptionPacket(b.readEnum(GOTPlayerOptions.Option.class));
    }
    public static void handle(C2SToggleOptionPacket p, Supplier<NetworkEvent.Context> s) {
        NetworkEvent.Context ctx=s.get(); ServerPlayer player=ctx.getSender();
        if (player != null) ctx.enqueueWork(() -> {
            GOTPlayerOptions.toggle(player, p.option);
            GOTNetwork.CHANNEL.send(PacketDistributor.PLAYER.with(() -> player),
                    new S2COptionsPacket(GOTPlayerOptions.snapshot(player)));
        });
        ctx.setPacketHandled(true);
    }
}
