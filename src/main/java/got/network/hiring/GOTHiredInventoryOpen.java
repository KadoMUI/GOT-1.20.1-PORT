package got.network.hiring;

import got.menu.hiring.GOTHiredMenuProvider;
import got.npc.hiring.GOTHiredData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.NetworkHooks;

/**
 * Server-only bridge used by C2SHiredNpcActionPacket.OPEN_INVENTORY.
 */
public final class GOTHiredInventoryOpen {
    private GOTHiredInventoryOpen() {}

    public static void open(ServerPlayer player, Entity npc) {
        if (!GOTHiredData.isOwner(npc, player.getUUID())) return;
        if (player.distanceToSqr(npc) > 64.0D) return;

        NetworkHooks.openScreen(
            player,
            new GOTHiredMenuProvider(npc),
            buf -> buf.writeVarInt(npc.getId())
        );
    }
}
