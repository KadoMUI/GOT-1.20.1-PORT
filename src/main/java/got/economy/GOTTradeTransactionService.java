package got.economy;

import got.speech.GOTSpeechService;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;

/**
 * Server-authoritative economy transaction API.
 *
 * BUY = player pays coins, trader gives item.
 * SELL = player gives matching item stack(s), trader pays coins.
 */
public final class GOTTradeTransactionService {
    private GOTTradeTransactionService() {}

    public static boolean buy(ServerPlayer player, Entity trader,
                              GOTTraderState state, GOTTradeEntryState trade) {
        if (!trade.available(state)) return false;
        if (!GOTCoinValueService.take(player, trade.cost())) return false;

        ItemStack result = trade.item();
        if (!player.getInventory().add(result.copy())) {
            player.drop(result.copy(), false);
        }

        state.onTrade(trade, trade.cost());
        GOTSpeechService.speak(trader, player, "default_friendly_trader_default");
        return true;
    }

    public static boolean sell(ServerPlayer player, Entity trader,
                               GOTTraderState state, GOTTradeEntryState trade) {
        if (!trade.available(state)) return false;

        ItemStack wanted = trade.item();
        int needed = wanted.getCount();
        int found = 0;

        for (ItemStack stack : player.getInventory().items) {
            if (ItemStack.isSameItemSameTags(stack, wanted)) found += stack.getCount();
        }
        if (found < needed) return false;

        int remaining = needed;
        for (ItemStack stack : player.getInventory().items) {
            if (remaining <= 0) break;
            if (!ItemStack.isSameItemSameTags(stack, wanted)) continue;
            int remove = Math.min(stack.getCount(), remaining);
            stack.shrink(remove);
            remaining -= remove;
        }

        GOTCoinValueService.give(player, trade.cost());
        state.onTrade(trade, trade.cost());
        GOTSpeechService.speak(trader, player, "default_friendly_trader_default");
        return true;
    }
}
