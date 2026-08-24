package got.network.hiring;

import got.npc.hiring.GOTHireSnapshotFactory;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;

import java.util.function.Supplier;

import got.network.GOTNetwork;

public record C2SRequestHiredNpcGuiPacket(int entityId) {
    public static void encode(C2SRequestHiredNpcGuiPacket p, FriendlyByteBuf b) { b.writeVarInt(p.entityId); }
    public static C2SRequestHiredNpcGuiPacket decode(FriendlyByteBuf b) { return new C2SRequestHiredNpcGuiPacket(b.readVarInt()); }

    public static void handle(C2SRequestHiredNpcGuiPacket p, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer sender=ctx.get().getSender();
            if(sender==null) return;
            Entity entity=sender.level().getEntity(p.entityId);
            if(entity==null || sender.distanceToSqr(entity)>64.0D) return;
            GOTNetwork.CHANNEL.send(
                PacketDistributor.PLAYER.with(() -> sender),
                new S2CHiredNpcGuiPacket(GOTHireSnapshotFactory.build(sender,entity))
            );
        });
        ctx.get().setPacketHandled(true);
    }
}
