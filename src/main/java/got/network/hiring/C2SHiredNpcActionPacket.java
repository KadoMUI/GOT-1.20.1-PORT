package got.network.hiring;

import got.network.GOTNetwork;
import got.npc.hiring.*;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;

import java.util.function.Supplier;

public record C2SHiredNpcActionPacket(int entityId, Action action, String text, int number) {
    public enum Action {
        HIRE,
        FOLLOW,
        HOLD,
        PATROL,
        WANDER,
        TOGGLE_TELEPORT,
        SET_GUARD_RANGE,
        SET_SQUADRON,
        DISMISS,
        OPEN_INVENTORY
    }

    public static void encode(C2SHiredNpcActionPacket p, FriendlyByteBuf b) {
        b.writeVarInt(p.entityId);
        b.writeEnum(p.action);
        b.writeUtf(p.text, 200);
        b.writeInt(p.number);
    }

    public static C2SHiredNpcActionPacket decode(FriendlyByteBuf b) {
        return new C2SHiredNpcActionPacket(
                b.readVarInt(),
                b.readEnum(Action.class),
                b.readUtf(200),
                b.readInt()
        );
    }

    public static void handle(C2SHiredNpcActionPacket p, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (player == null) return;

            Entity entity = player.level().getEntity(p.entityId);
            if (entity == null || player.distanceToSqr(entity) > 256.0D) return;

            if (p.action == Action.HIRE) {
                GOTHiringTransactionService.hire(player, entity);
            } else {
                if (!GOTHiredData.isOwner(entity, player.getUUID())) return;

                switch (p.action) {
                    case FOLLOW -> GOTHiredData.setOrder(entity, GOTHiredOrder.FOLLOW);
                    case HOLD -> GOTHiredData.setOrder(entity, GOTHiredOrder.HOLD);
                    case PATROL -> GOTHiredData.setOrder(entity, GOTHiredOrder.PATROL);
                    case WANDER -> GOTHiredData.setOrder(entity, GOTHiredOrder.WANDER);
                    case TOGGLE_TELEPORT ->
                            GOTHiredData.setTeleportAutomatically(entity, !GOTHiredData.teleportAutomatically(entity));
                    case SET_GUARD_RANGE -> GOTHiredData.setGuardRange(entity, p.number);
                    case SET_SQUADRON -> GOTHiredData.setSquadron(entity, p.text);
                    case DISMISS -> GOTHiredData.dismiss(entity);
                    case OPEN_INVENTORY -> {
                        GOTHiredInventoryOpen.open(player, entity);
                        return;
                    }
                    default -> { }
                }
            }

            GOTNetwork.CHANNEL.send(
                    PacketDistributor.PLAYER.with(() -> player),
                    new S2CHiredNpcGuiPacket(GOTHireSnapshotFactory.build(player, entity))
            );
        });

        ctx.get().setPacketHandled(true);
    }
}
