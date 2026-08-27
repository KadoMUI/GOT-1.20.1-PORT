package got.economy;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * Exact/role-correct legendary trader overrides recovered from the final 1.7.10 build.
 * Returning null means the regional profession pool should be used unchanged.
 */
public final class GOTLegendaryTraderOffers {
    private GOTLegendaryTraderOffers() {}

    public static MerchantOffers forRole(String roleId) {
        if (roleId == null) return null;
        return switch (roleId) {
            case "petyr_baelish" -> petyr();
            case "davos_seaworth" -> davos();
            case "tobho_mott" -> tobho();
            // Legacy class pool corrections where the modern role label had drifted.
            case "harmune", "mullin" -> maester();
            case "craster" -> butcher();
            case "aeron_greyjoy" -> alchemist();
            default -> null;
        };
    }

    private static MerchantOffers petyr() {
        MerchantOffers o = new MerchantOffers();
        sell(o, 4096, "got:petyr_baelish_dagger", 1);
        return o;
    }

    private static MerchantOffers davos() {
        MerchantOffers o = new MerchantOffers();
        sell(o, 5, "got:leek", 1);
        // The old leekCrop block had an obtainable BlockItem. The modern crop is no-item;
        // use a second leek stack rather than expose an unobtainable technical block.
        sell(o, 5, "got:leek", 1);
        sell(o, 5, "got:leek_soup", 1);
        appendMaesterBuys(o);
        return o;
    }

    private static MerchantOffers tobho() {
        MerchantOffers o = new MerchantOffers();
        sell(o, 512, "got:valyrian_dagger", 1);
        sell(o, 512, "got:valyrian_dagger_poisoned", 1);
        sell(o, 2048, "got:valyrian_hammer", 1);
        sell(o, 512, "got:valyrian_spear", 1);
        sell(o, 1024, "got:valyrian_sword", 1);
        sell(o, 2560, "got:valyrian_helmet", 1);
        sell(o, 4096, "got:valyrian_chestplate", 1);
        sell(o, 3584, "got:valyrian_leggings", 1);
        sell(o, 2048, "got:valyrian_boots", 1);
        sell(o, 1536, "got:valyrian_chainmail_helmet", 1);
        sell(o, 2560, "got:valyrian_chainmail_chestplate", 1);
        sell(o, 2560, "got:valyrian_chainmail_leggings", 1);
        sell(o, 1024, "got:valyrian_chainmail_boots", 1);
        return o;
    }

    private static MerchantOffers maester() {
        MerchantOffers o = new MerchantOffers();
        appendMaesterBuys(o);
        sell(o, 4, "minecraft:book", 3);
        sell(o, 8, "minecraft:compass", 1);
        sell(o, 8, "got:mug_poppy_milk", 1);
        return o;
    }

    private static void appendMaesterBuys(MerchantOffers o) {
        buy(o, "minecraft:paper", 20, 3);
    }

    private static MerchantOffers butcher() {
        MerchantOffers o = new MerchantOffers();
        buy(o, "minecraft:beef", 12, 2);
        buy(o, "minecraft:porkchop", 12, 2);
        buy(o, "minecraft:mutton", 12, 2);
        sell(o, 3, "minecraft:cooked_beef", 5);
        sell(o, 3, "minecraft:cooked_porkchop", 5);
        sell(o, 3, "minecraft:cooked_mutton", 5);
        return o;
    }

    private static MerchantOffers alchemist() {
        MerchantOffers o = new MerchantOffers();
        buy(o, "minecraft:redstone", 16, 3);
        buy(o, "minecraft:gunpowder", 8, 4);
        sell(o, 8, "got:bottle_poison", 1);
        return o;
    }

    private static void buy(MerchantOffers o, String input, int inputCount, int value) {
        ItemStack in = stack(input, inputCount);
        ItemStack coin = coinStack(value);
        if (!in.isEmpty() && !coin.isEmpty()) o.add(new MerchantOffer(in, coin, 12, 2, 0.05F));
    }

    private static void sell(MerchantOffers o, int value, String output, int outputCount) {
        ItemStack out = stack(output, outputCount);
        if (out.isEmpty()) return;
        ItemStack[] cost = splitCoins(value);
        if (cost[0].isEmpty()) return;
        o.add(new MerchantOffer(cost[0], cost[1], out, 12, 2, 0.05F));
    }

    private static ItemStack[] splitCoins(int value) {
        for (var e : GOTCoinValueService.denominationsDescending().entrySet()) {
            int denom = e.getValue();
            int count = value / denom;
            if (count > 0 && count <= e.getKey().getMaxStackSize() && value % denom == 0) {
                return new ItemStack[]{new ItemStack(e.getKey(), count), ItemStack.EMPTY};
            }
        }
        for (var first : GOTCoinValueService.denominationsDescending().entrySet()) {
            int countA = Math.min(value / first.getValue(), first.getKey().getMaxStackSize());
            if (countA <= 0) continue;
            int rem = value - countA * first.getValue();
            if (rem == 0) return new ItemStack[]{new ItemStack(first.getKey(), countA), ItemStack.EMPTY};
            for (var second : GOTCoinValueService.denominationsDescending().entrySet()) {
                if (rem % second.getValue() != 0) continue;
                int countB = rem / second.getValue();
                if (countB > 0 && countB <= second.getKey().getMaxStackSize()) {
                    return new ItemStack[]{new ItemStack(first.getKey(), countA), new ItemStack(second.getKey(), countB)};
                }
            }
        }
        return new ItemStack[]{ItemStack.EMPTY, ItemStack.EMPTY};
    }

    private static ItemStack coinStack(int value) {
        ItemStack[] split = splitCoins(value);
        return split[1].isEmpty() ? split[0] : ItemStack.EMPTY;
    }

    private static ItemStack stack(String id, int count) {
        Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(id));
        return item == null || item == Items.AIR ? ItemStack.EMPTY : new ItemStack(item, count);
    }
}
