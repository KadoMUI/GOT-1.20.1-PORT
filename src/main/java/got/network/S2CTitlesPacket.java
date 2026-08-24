package got.network;

import got.client.player.GOTClientTitleState;
import net.minecraft.ChatFormatting;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public record S2CTitlesPacket(String selectedId, ChatFormatting color, List<String> unlocked) {
    public static void encode(S2CTitlesPacket p, FriendlyByteBuf b) {
        b.writeUtf(p.selectedId == null ? "" : p.selectedId, 128);
        b.writeUtf(p.color == null ? "white" : p.color.getName(), 32);
        b.writeVarInt(p.unlocked.size());
        for (String id : p.unlocked) b.writeUtf(id, 128);
    }
    public static S2CTitlesPacket decode(FriendlyByteBuf b) {
        String id=b.readUtf(128);
        ChatFormatting color=ChatFormatting.getByName(b.readUtf(32));
        int n=b.readVarInt(); List<String> ids=new ArrayList<>(n);
        for(int i=0;i<n;i++) ids.add(b.readUtf(128));
        return new S2CTitlesPacket(id, color == null ? ChatFormatting.WHITE : color, ids);
    }
    public static void handle(S2CTitlesPacket p, Supplier<NetworkEvent.Context> s) {
        s.get().enqueueWork(() -> GOTClientTitleState.set(p.selectedId,p.color,p.unlocked));
        s.get().setPacketHandled(true);
    }
}
