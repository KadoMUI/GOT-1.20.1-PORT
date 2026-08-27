package got.network;
import got.common.fasttravel.GOTFastTravelData;
import got.common.fasttravel.GOTFastTravelManager;
import got.pact.GOTPactSharing;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import java.util.function.Supplier;
public record C2SRenameCustomWaypointPacket(int id, String name) {
    public static void encode(C2SRenameCustomWaypointPacket p, FriendlyByteBuf b){ b.writeVarInt(p.id); b.writeUtf(p.name,32); }
    public static C2SRenameCustomWaypointPacket decode(FriendlyByteBuf b){ return new C2SRenameCustomWaypointPacket(b.readVarInt(),b.readUtf(32)); }
    public static void handle(C2SRenameCustomWaypointPacket p, Supplier<NetworkEvent.Context> s){ var c=s.get(); c.enqueueWork(()->{ ServerPlayer pl=c.getSender(); if(pl!=null&&GOTFastTravelData.renameCustom(pl,p.id,p.name)){ GOTPactSharing.renameWaypoint(pl,p.id,p.name); GOTFastTravelManager.sync(pl); }}); c.setPacketHandled(true); }
}
