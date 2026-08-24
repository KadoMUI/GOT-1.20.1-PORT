package got.economy;

import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Runtime bridge that applies recovered 1.7.10 offer-selection and value rules
 * to the already-proven modern regional item tables.
 */
public final class GOTMerchantOfferAdapter {
    private GOTMerchantOfferAdapter() {}

    public static MerchantOffers legacyize(MerchantOffers source, RandomSource random) {
        if (source == null || source.isEmpty()) return new MerchantOffers();

        List<MerchantOffer> shuffled = new ArrayList<>(source);
        Collections.shuffle(shuffled, new Random(random.nextLong()));
        int wanted = Math.min(shuffled.size(),
                3 + random.nextInt(3) + random.nextInt(3) + random.nextInt(3));

        MerchantOffers result = new MerchantOffers();
        for (int i = 0; i < wanted; i++) {
            result.add(randomize(shuffled.get(i), random));
        }
        return result;
    }

    private static MerchantOffer randomize(MerchantOffer offer, RandomSource random) {
        ItemStack a = randomizeCoin(offer.getBaseCostA(), random);
        ItemStack b = randomizeCoin(offer.getCostB(), random);
        ItemStack out = randomizeCoin(offer.getResult(), random);
        return new MerchantOffer(a, b, out, 0, 12, offer.getXp(), offer.getPriceMultiplier(), 0);
    }

    private static ItemStack randomizeCoin(ItemStack stack, RandomSource random) {
        int value = GOTCoinValueService.valueOf(stack);
        if (value <= 0) return stack.copy();
        int randomized = Math.max(1, Math.round(value * (0.75F + random.nextFloat() * 0.50F)));
        return representValue(randomized);
    }

    /** Largest exact denomination that fits a single vanilla merchant slot. */
    public static ItemStack representValue(int value) {
        value = Math.max(1, value);
        for (var entry : GOTCoinValueService.denominationsDescending().entrySet()) {
            int denomination = entry.getValue();
            if (value % denomination != 0) continue;
            int count = value / denomination;
            if (count <= entry.getKey().getMaxStackSize()) {
                return new ItemStack(entry.getKey(), count);
            }
        }
        return new ItemStack(got.GOTItems.COIN_1.get(), Math.min(64, value));
    }
}
