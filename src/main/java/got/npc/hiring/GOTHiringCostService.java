package got.npc.hiring;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.List;

/**
 * Inventory-backed coin payment.
 *
 * Creative-mode players bypass payment for development/testing.
 */
public final class GOTHiringCostService {
    private GOTHiringCostService() {}

    public static boolean canAfford(ServerPlayer player, List<GOTHiringCurrency> cost) {
        if (player.getAbilities().instabuild) return true;
        for (GOTHiringCurrency currency : cost) {
            Item item = BuiltInRegistries.ITEM.get(currency.itemId());
            int found = 0;
            for (ItemStack stack : player.getInventory().items) {
                if (stack.is(item)) found += stack.getCount();
            }
            if (found < currency.amount()) return false;
        }
        return true;
    }

    public static boolean charge(ServerPlayer player, List<GOTHiringCurrency> cost) {
        if (player.getAbilities().instabuild) return true;
        if (!canAfford(player, cost)) return false;

        for (GOTHiringCurrency currency : cost) {
            Item item = BuiltInRegistries.ITEM.get(currency.itemId());
            int remaining = currency.amount();

            for (ItemStack stack : player.getInventory().items) {
                if (remaining <= 0) break;
                if (!stack.is(item)) continue;

                int remove = Math.min(stack.getCount(), remaining);
                stack.shrink(remove);
                remaining -= remove;
            }
        }

        player.getInventory().setChanged();
        return true;
    }
}
