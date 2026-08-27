package got.network;

import got.common.fasttravel.GOTFastTravelManager;
import got.common.world.map.GOTWaypoint;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import java.util.function.Supplier;
import java.util.UUID;

public record C2SFastTravelPacket(boolean custom, UUID owner, int waypointId) {
    public C2SFastTravelPacket(int waypointId) { this(false, new UUID(0L,0L), waypointId); }
    public C2SFastTravelPacket(UUID owner, int waypointId) { this(true, owner, waypointId); }
    public static void encode(C2SFastTravelPacket p, FriendlyByteBuf b) { b.writeBoolean(p.custom); if(p.custom)b.writeUUID(p.owner); b.writeVarInt(p.waypointId); }
    public static C2SFastTravelPacket decode(FriendlyByteBuf b) { boolean c=b.readBoolean(); UUID o=c?b.readUUID():new UUID(0L,0L); return new C2SFastTravelPacket(c,o,b.readVarInt()); }
    public static void handle(C2SFastTravelPacket p, Supplier<NetworkEvent.Context> s) {
        var c=s.get(); c.enqueueWork(() -> { ServerPlayer player=c.getSender(); if(player==null)return; if(p.custom) GOTFastTravelManager.requestCustom(player,p.owner,p.waypointId); else { GOTWaypoint w=GOTWaypoint.byId(p.waypointId); if(w!=null)GOTFastTravelManager.requestFixed(player,w); }}); c.setPacketHandled(true);
    }
}
