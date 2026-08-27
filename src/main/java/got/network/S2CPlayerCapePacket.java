package got.network;
import got.cape.GOTCape; import got.client.cape.GOTClientCapeState; import net.minecraft.network.FriendlyByteBuf; import net.minecraftforge.network.NetworkEvent; import java.util.UUID; import java.util.function.Supplier;
public record S2CPlayerCapePacket(UUID playerId,GOTCape cape){
 public static void encode(S2CPlayerCapePacket p,FriendlyByteBuf b){b.writeUUID(p.playerId);b.writeUtf(p.cape==null?"":p.cape.id());}
 public static S2CPlayerCapePacket decode(FriendlyByteBuf b){return new S2CPlayerCapePacket(b.readUUID(),GOTCape.byId(b.readUtf()));}
 public static void handle(S2CPlayerCapePacket p,Supplier<NetworkEvent.Context>s){NetworkEvent.Context c=s.get();c.enqueueWork(()->GOTClientCapeState.setPlayer(p.playerId,p.cape));c.setPacketHandled(true);}
}
