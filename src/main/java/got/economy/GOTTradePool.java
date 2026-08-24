package got.economy;

import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Exact core randomization rules recovered from GOTTradeEntries#getRandomTrades.
 */
public final class GOTTradePool {
    public record Template(ItemStack item, int baseCost, boolean frozenPrice) {}

    private final List<Template> templates = new ArrayList<>();

    public GOTTradePool add(ItemStack item, int cost) {
        return add(item, cost, false);
    }

    public GOTTradePool add(ItemStack item, int cost, boolean frozenPrice) {
        templates.add(new Template(item.copy(), Math.max(1, cost), frozenPrice));
        return this;
    }

    public List<GOTTradeEntryState> randomTrades(Random random) {
        int count = 3 + random.nextInt(3) + random.nextInt(3) + random.nextInt(3);
        count = Math.min(count, templates.size());

        List<Template> shuffled = new ArrayList<>(templates);
        Collections.shuffle(shuffled, random);

        List<GOTTradeEntryState> out = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            Template template = shuffled.get(i);

            float randomized = template.baseCost() *
                (0.75F + random.nextFloat() * 0.50F);

            int price = template.frozenPrice()
                ? template.baseCost()
                : Math.max(1, Math.round(randomized));

            out.add(new GOTTradeEntryState(template.item(), price, template.frozenPrice()));
        }
        return out;
    }
}
