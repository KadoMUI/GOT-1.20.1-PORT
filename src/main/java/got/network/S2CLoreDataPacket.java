package got.network;

import got.client.lore.GOTClientLoreState;
import got.client.lore.GOTLoreView;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public record S2CLoreDataPacket(List<GOTLoreView> entries) {
    public static void encode(S2CLoreDataPacket p, FriendlyByteBuf b) {
        b.writeVarInt(p.entries.size());
        for (GOTLoreView e : p.entries) {
            b.writeUtf(e.id()); b.writeUtf(e.category()); b.writeUtf(e.title());
            b.writeUtf(e.author() == null ? "" : e.author());
            b.writeVarInt(e.types().size()); for (String t : e.types()) b.writeUtf(t);
            b.writeUtf(e.text(), 262144); b.writeBoolean(e.rewardable()); b.writeBoolean(e.discovered());
        }
    }
    public static S2CLoreDataPacket decode(FriendlyByteBuf b) {
        int count=b.readVarInt(); List<GOTLoreView> list=new ArrayList<>(count);
        for(int i=0;i<count;i++){
            String id=b.readUtf(), cat=b.readUtf(), title=b.readUtf(), author=b.readUtf();
            int tc=b.readVarInt(); List<String> types=new ArrayList<>(tc); for(int j=0;j<tc;j++)types.add(b.readUtf());
            list.add(new GOTLoreView(id,cat,title,author,types,b.readUtf(262144),b.readBoolean(),b.readBoolean()));
        }
        return new S2CLoreDataPacket(list);
    }
    public static void handle(S2CLoreDataPacket p, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> GOTClientLoreState.set(p.entries));
        ctx.get().setPacketHandled(true);
    }
}
