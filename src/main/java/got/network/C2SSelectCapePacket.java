package got.network;
import got.cape.*; import net.minecraft.network.FriendlyByteBuf; import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.*; import net.minecraftforge.network.PacketDistributor; import java.util.*; import java.util.function.Supplier;
public record C2SSelectCapePacket(String capeId){
 public static void encode(C2SSelectCapePacket p,FriendlyByteBuf b){b.writeUtf(p.capeId);} public static C2SSelectCapePacket decode(FriendlyByteBuf b){return new C2SSelectCapePacket(b.readUtf());}
 public static void handle(C2SSelectCapePacket p,Supplier<NetworkEvent.Context>s){NetworkEvent.Context c=s.get();c.enqueueWork(()->{ServerPlayer sp=c.getSender();if(sp==null)return;GOTCape x=GOTCape.byId(p.capeId);if(x==null||x.unlocked(sp)){GOTCapeData.setSelected(sp,x);GOTNetwork.CHANNEL.send(PacketDistributor.ALL.noArg(),new S2CPlayerCapePacket(sp.getUUID(),x));}EnumSet<GOTCape>u=EnumSet.noneOf(GOTCape.class);for(GOTCape z:GOTCape.values())if(z.unlocked(sp))u.add(z);GOTNetwork.CHANNEL.send(PacketDistributor.PLAYER.with(()->sp),new S2CCapeDataPacket(GOTCapeData.selected(sp),u));});c.setPacketHandled(true);}
}
