package got.network;
import got.cape.GOTCape; import got.client.cape.GOTClientCapeState; import net.minecraft.network.FriendlyByteBuf; import net.minecraftforge.network.NetworkEvent; import java.util.*; import java.util.function.Supplier;
public record S2CCapeDataPacket(GOTCape selected,Set<GOTCape> unlocked){
 public static void encode(S2CCapeDataPacket p,FriendlyByteBuf b){b.writeUtf(p.selected==null?"":p.selected.id());b.writeVarInt(p.unlocked.size());for(GOTCape c:p.unlocked)b.writeUtf(c.id());}
 public static S2CCapeDataPacket decode(FriendlyByteBuf b){GOTCape s=GOTCape.byId(b.readUtf());int n=b.readVarInt();EnumSet<GOTCape>u=EnumSet.noneOf(GOTCape.class);for(int i=0;i<n;i++){GOTCape c=GOTCape.byId(b.readUtf());if(c!=null)u.add(c);}return new S2CCapeDataPacket(s,u);}
 public static void handle(S2CCapeDataPacket p,Supplier<NetworkEvent.Context>s){NetworkEvent.Context c=s.get();c.enqueueWork(()->GOTClientCapeState.setMenuData(p.selected,p.unlocked));c.setPacketHandled(true);}
}
