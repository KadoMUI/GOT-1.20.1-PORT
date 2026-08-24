package got.economy;

import got.GOTItems;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Shared modern equivalent of the legacy GOTItemCoin value helpers.
 *
 * Exact denominations: 1, 4, 16, 64, 256, 1024, 4096, 16384.
 */
public final class GOTCoinValueService {
    private GOTCoinValueService() {}

    public static LinkedHashMap<Item, Integer> denominationsDescending() {
        LinkedHashMap<Item, Integer> map = new LinkedHashMap<>();
        map.put(GOTItems.COIN_16384.get(), 16384);
        map.put(GOTItems.COIN_4096.get(), 4096);
        map.put(GOTItems.COIN_1024.get(), 1024);
        map.put(GOTItems.COIN_256.get(), 256);
        map.put(GOTItems.COIN_64.get(), 64);
        map.put(GOTItems.COIN_16.get(), 16);
        map.put(GOTItems.COIN_4.get(), 4);
        map.put(GOTItems.COIN_1.get(), 1);
        return map;
    }

    public static int valueOf(ItemStack stack) {
        if (stack == null || stack.isEmpty()) return 0;
        Integer value = denominationsDescending().get(stack.getItem());
        return value == null ? 0 : value * stack.getCount();
    }

    public static int inventoryValue(Player player) {
        int total = 0;
        for (ItemStack stack : player.getInventory().items) {
            total += valueOf(stack);
        }
        return total;
    }

    public static boolean canAfford(Player player, int value) {
        return value <= 0 || player.getAbilities().instabuild || inventoryValue(player) >= value;
    }

    public static boolean take(ServerPlayer player, int value) {
        if (value <= 0 || player.getAbilities().instabuild) return true;
        if (!canAfford(player, value)) return false;

        int original = inventoryValue(player);
        int remainder = original - value;

        for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
            ItemStack stack = player.getInventory().getItem(i);
            if (valueOf(stack) > 0) player.getInventory().setItem(i, ItemStack.EMPTY);
        }

        give(player, remainder);
        player.getInventory().setChanged();
        return true;
    }

    public static void give(ServerPlayer player, int value) {
        if (value <= 0) return;

        for (Map.Entry<Item, Integer> entry : denominationsDescending().entrySet()) {
            int denomination = entry.getValue();
            int count = value / denomination;
            value %= denomination;

            while (count > 0) {
                int batch = Math.min(count, entry.getKey().getMaxStackSize());
                ItemStack stack = new ItemStack(entry.getKey(), batch);
                if (!player.getInventory().add(stack)) player.drop(stack, false);
                count -= batch;
            }
        }
    }

    public static String format(int value) {
        return Integer.toString(Math.max(0, value));
    }
}
