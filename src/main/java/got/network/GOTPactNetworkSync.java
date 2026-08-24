package got.network;

import got.pact.GOTPact;
import got.pact.GOTPactService;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.PacketDistributor;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public final class GOTPactNetworkSync {
    private GOTPactNetworkSync() {}

    public static void sync(ServerPlayer player) {
        GOTNetwork.CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), S2CPactDataPacket.forPlayer(player));
    }

    /** Sync actor plus every online member of their Pact after a mutation. */
    public static void syncRelevant(ServerPlayer actor) {
        Set<UUID> targets = new HashSet<>();
        targets.add(actor.getUUID());
        GOTPactService.forPlayer(actor.server, actor.getUUID()).ifPresent(p -> targets.addAll(p.members()));
        for (UUID id : targets) {
            ServerPlayer online = actor.server.getPlayerList().getPlayer(id);
            if (online != null) sync(online);
        }
    }
}
