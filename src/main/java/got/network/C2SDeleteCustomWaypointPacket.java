package got.network;
import got.common.fasttravel.GOTFastTravelData;
import got.common.fasttravel.GOTFastTravelManager;
import got.pact.GOTPactSharing;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import java.util.function.Supplier;
public record C2SDeleteCustomWaypointPacket(int id) {
    public static void encode(C2SDeleteCustomWaypointPacket p,FriendlyByteBuf b){b.writeVarInt(p.id);} public static C2SDeleteCustomWaypointPacket decode(FriendlyByteBuf b){return new C2SDeleteCustomWaypointPacket(b.readVarInt());}
    public static void handle(C2SDeleteCustomWaypointPacket p,Supplier<NetworkEvent.Context>s){var c=s.get();c.enqueueWork(()->{ServerPlayer pl=c.getSender();if(pl!=null&&GOTFastTravelData.deleteCustom(pl,p.id)){GOTPactSharing.deleteWaypoint(pl,p.id);GOTFastTravelManager.sync(pl);}});c.setPacketHandled(true);}
}
