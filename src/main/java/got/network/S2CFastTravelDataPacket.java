package got.network;

import got.client.fasttravel.GOTClientFastTravelState;
import got.common.fasttravel.GOTFastTravelData;
import got.common.world.map.GOTWaypoint;
import got.pact.GOTPactSharing;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.function.Supplier;

public record S2CFastTravelDataPacket(EnumSet<GOTWaypoint.Region> unlocked, List<GOTFastTravelData.Custom> custom) {
    public static S2CFastTravelDataPacket from(ServerPlayer player) {
        return new S2CFastTravelDataPacket(GOTPactSharing.effectiveUnlocked(player), GOTPactSharing.effectiveWaypoints(player));
    }
    public static void encode(S2CFastTravelDataPacket p, FriendlyByteBuf b) {
        b.writeVarInt(p.unlocked.size());
        for (GOTWaypoint.Region r : p.unlocked) b.writeUtf(r.name());
        b.writeVarInt(p.custom.size());
        for (GOTFastTravelData.Custom c : p.custom) {
            b.writeUUID(c.owner()); b.writeVarInt(c.id()); b.writeUtf(c.name(), 32); b.writeInt(c.x()); b.writeInt(c.y()); b.writeInt(c.z());
        }
    }
    public static S2CFastTravelDataPacket decode(FriendlyByteBuf b) {
        EnumSet<GOTWaypoint.Region> regions = EnumSet.noneOf(GOTWaypoint.Region.class);
        int rc = b.readVarInt();
        for (int i = 0; i < rc; i++) { try { regions.add(GOTWaypoint.Region.valueOf(b.readUtf())); } catch (IllegalArgumentException ignored) {} }
        int cc = b.readVarInt();
        ArrayList<GOTFastTravelData.Custom> customs = new ArrayList<>();
        for (int i = 0; i < cc; i++) customs.add(new GOTFastTravelData.Custom(b.readUUID(), b.readVarInt(), b.readUtf(32), b.readInt(), b.readInt(), b.readInt()));
        return new S2CFastTravelDataPacket(regions, customs);
    }
    public static void handle(S2CFastTravelDataPacket p, Supplier<NetworkEvent.Context> s) {
        s.get().enqueueWork(() -> GOTClientFastTravelState.set(p.unlocked, p.custom));
        s.get().setPacketHandled(true);
    }
}
