package got.network;

import got.world.GOTDimensions;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * Admin/cheat teleport requested from the GOT map.
 *
 * Security is server-authoritative: clients may send the packet, but only
 * players with command permission level 2+ (singleplayer cheats / server OP)
 * are allowed to teleport.
 */
public record C2SMapTeleportPacket(int x, int z) {
    public static void encode(C2SMapTeleportPacket packet, FriendlyByteBuf buf) {
        buf.writeInt(packet.x);
        buf.writeInt(packet.z);
    }

    public static C2SMapTeleportPacket decode(FriendlyByteBuf buf) {
        return new C2SMapTeleportPacket(buf.readInt(), buf.readInt());
    }

    public static void handle(C2SMapTeleportPacket packet, Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            if (player == null || !player.hasPermissions(2)) {
                return;
            }

            ServerLevel planetos = player.server.getLevel(GOTDimensions.PLANETOS);
            if (planetos == null) {
                return;
            }

            // Reject coordinates outside the actual world border rather than
            // allowing a modified client to use this packet as an arbitrary TP.
            BlockPos horizontal = new BlockPos(packet.x, planetos.getSeaLevel(), packet.z);
            if (!planetos.getWorldBorder().isWithinBounds(horizontal)) {
                return;
            }

            int groundY = planetos.getHeight(
                    Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                    packet.x,
                    packet.z
            );
            int y = Math.min(planetos.getMaxBuildHeight() - 2,
                    Math.max(planetos.getMinBuildHeight() + 1, groundY + 1));

            player.stopRiding();
            player.teleportTo(
                    planetos,
                    packet.x + 0.5D,
                    y,
                    packet.z + 0.5D,
                    player.getYRot(),
                    player.getXRot()
            );
            player.fallDistance = 0.0F;
        });
        context.setPacketHandled(true);
    }
}
