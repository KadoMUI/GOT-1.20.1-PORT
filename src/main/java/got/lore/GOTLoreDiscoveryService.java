package got.lore;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

/**
 * Single entry point for lore discovery.  Rewardable status is preserved from
 * the original text metadata, but no unproven reward type/value is invented.
 */
public final class GOTLoreDiscoveryService {
    private GOTLoreDiscoveryService() {}

    public static boolean discover(ServerPlayer player, String id) {
        var entry = GOTLoreRegistry.get(id);
        if (entry.isEmpty()) return false;
        if (!GOTLorePlayerData.discover(player, id)) return false;

        player.displayClientMessage(
                Component.translatable("got.lore.discovered", entry.get().title()), false);

        if (entry.get().rewardable()) {
            GOTLorePlayerData.setRewardPending(player, id, true);
        }
        return true;
    }
}
