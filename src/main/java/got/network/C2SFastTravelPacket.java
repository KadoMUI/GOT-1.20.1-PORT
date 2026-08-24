package got.network;

import got.common.fasttravel.GOTFastTravelManager;
import got.common.world.map.GOTWaypoint;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record C2SFastTravelPacket(int waypointId) {
    public static void encode(C2SFastTravelPacket message, FriendlyByteBuf buffer) {
        buffer.writeVarInt(message.waypointId);
    }

    public static C2SFastTravelPacket decode(FriendlyByteBuf buffer) {
        return new C2SFastTravelPacket(buffer.readVarInt());
    }

    public static void handle(C2SFastTravelPacket message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            if (player == null) return;

            GOTWaypoint waypoint = GOTWaypoint.byId(message.waypointId);
            if (waypoint != null) {
                GOTFastTravelManager.fastTravel(player, waypoint);
            }
        });
        context.setPacketHandled(true);
    }
}
