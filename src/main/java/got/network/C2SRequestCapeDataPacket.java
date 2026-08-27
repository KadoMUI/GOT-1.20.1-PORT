package got.network;
import got.cape.*; import net.minecraft.network.FriendlyByteBuf; import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.*; import net.minecraftforge.network.PacketDistributor; import java.util.*; import java.util.function.Supplier;
public final class C2SRequestCapeDataPacket {
 public static void encode(C2SRequestCapeDataPacket p,FriendlyByteBuf b){} public static C2SRequestCapeDataPacket decode(FriendlyByteBuf b){return new C2SRequestCapeDataPacket();}
 public static void handle(C2SRequestCapeDataPacket p,Supplier<NetworkEvent.Context>s){NetworkEvent.Context c=s.get();c.enqueueWork(()->{ServerPlayer sp=c.getSender();if(sp==null)return;EnumSet<GOTCape>u=EnumSet.noneOf(GOTCape.class);for(GOTCape x:GOTCape.values())if(x.unlocked(sp))u.add(x);GOTNetwork.CHANNEL.send(PacketDistributor.PLAYER.with(()->sp),new S2CCapeDataPacket(GOTCapeData.selected(sp),u));});c.setPacketHandled(true);}
}
