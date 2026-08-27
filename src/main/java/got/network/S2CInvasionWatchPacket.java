package got.network;

import got.client.invasion.GOTClientInvasionState;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import java.util.UUID;
import java.util.function.Supplier;

/** Modern equivalent of 1.7.10 GOTPacketInvasionWatch plus watched spawner data. */
public record S2CInvasionWatchPacket(UUID id, String type, BlockPos center, int size, int remaining, boolean clear, boolean override) {
    public static S2CInvasionWatchPacket clear(UUID id){ return new S2CInvasionWatchPacket(id,"",BlockPos.ZERO,0,0,true,false); }
    public static void encode(S2CInvasionWatchPacket p, FriendlyByteBuf b){b.writeUUID(p.id);b.writeUtf(p.type);b.writeBlockPos(p.center);b.writeVarInt(p.size);b.writeVarInt(p.remaining);b.writeBoolean(p.clear);b.writeBoolean(p.override);}
    public static S2CInvasionWatchPacket decode(FriendlyByteBuf b){return new S2CInvasionWatchPacket(b.readUUID(),b.readUtf(),b.readBlockPos(),b.readVarInt(),b.readVarInt(),b.readBoolean(),b.readBoolean());}
    public static void handle(S2CInvasionWatchPacket p, Supplier<NetworkEvent.Context> s){NetworkEvent.Context c=s.get();c.enqueueWork(()->GOTClientInvasionState.accept(p));c.setPacketHandled(true);}
}
