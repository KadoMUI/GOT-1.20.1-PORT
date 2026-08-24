package got.npc.hiring;

import got.economy.GOTCoinValueService;
import net.minecraft.server.level.ServerPlayer;

/**
 * Hiring now shares the same authoritative coin-value economy as ordinary trade.
 */
public final class GOTHiringCoinService {
    private GOTHiringCoinService() {}

    public static int inventoryValue(ServerPlayer player) {
        return GOTCoinValueService.inventoryValue(player);
    }

    public static boolean canAfford(ServerPlayer player, int value) {
        return GOTCoinValueService.canAfford(player, value);
    }

    public static boolean take(ServerPlayer player, int value) {
        return GOTCoinValueService.take(player, value);
    }

    public static void giveValue(ServerPlayer player, int value) {
        GOTCoinValueService.give(player, value);
    }
}
