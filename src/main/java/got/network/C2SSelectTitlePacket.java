package got.network;

import got.player.GOTPlayerTitleData;
import net.minecraft.ChatFormatting;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;
import java.util.function.Supplier;

public record C2SSelectTitlePacket(String id, ChatFormatting color) {
    public static void encode(C2SSelectTitlePacket p, FriendlyByteBuf b) {
        b.writeUtf(p.id == null ? "" : p.id, 128);
        b.writeUtf(p.color == null ? "white" : p.color.getName(), 32);
    }
    public static C2SSelectTitlePacket decode(FriendlyByteBuf b) {
        String id=b.readUtf(128);
        ChatFormatting color=ChatFormatting.getByName(b.readUtf(32));
        return new C2SSelectTitlePacket(id, color == null ? ChatFormatting.WHITE : color);
    }
    public static void handle(C2SSelectTitlePacket p, Supplier<NetworkEvent.Context> s) {
        NetworkEvent.Context ctx=s.get(); ServerPlayer player=ctx.getSender();
        if (player != null) ctx.enqueueWork(() -> {
            GOTPlayerTitleData.select(player, p.id, p.color);
            GOTNetwork.CHANNEL.send(PacketDistributor.PLAYER.with(() -> player),
                    new S2CTitlesPacket(GOTPlayerTitleData.selectedId(player),
                            GOTPlayerTitleData.selectedColor(player),
                            GOTPlayerTitleData.unlockedIds(player)));
        });
        ctx.setPacketHandled(true);
    }
}
