package got.network;

import got.common.fasttravel.GOTFastTravelData;
import got.common.fasttravel.GOTFastTravelManager;
import got.pact.GOTPactSharing;
import got.world.GOTDimensions;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import java.util.function.Supplier;

public record C2SCreateCustomWaypointPacket(String name) {
    public static void encode(C2SCreateCustomWaypointPacket p, FriendlyByteBuf b) { b.writeUtf(p.name, 32); }
    public static C2SCreateCustomWaypointPacket decode(FriendlyByteBuf b) { return new C2SCreateCustomWaypointPacket(b.readUtf(32)); }
    public static void handle(C2SCreateCustomWaypointPacket p, Supplier<NetworkEvent.Context> s) {
        var c = s.get(); c.enqueueWork(() -> {
            ServerPlayer player = c.getSender(); if (player == null || !player.level().dimension().equals(GOTDimensions.PLANETOS)) return;
            GOTFastTravelData.Custom created = GOTFastTravelData.createCustom(player, p.name, player.blockPosition().getX(), player.blockPosition().getY(), player.blockPosition().getZ());
            GOTPactSharing.publishWaypoint(player, created);
            GOTFastTravelManager.sync(player);
        }); c.setPacketHandled(true);
    }
}
