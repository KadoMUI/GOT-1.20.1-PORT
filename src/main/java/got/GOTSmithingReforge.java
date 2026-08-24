package got;

import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;

import java.util.ArrayList;
import java.util.List;

public final class GOTSmithingReforge {
    private GOTSmithingReforge() {}

    /**
     * Original physical-anvil reforge cost:
     * 2 base, 3 for armor, plus one per quarter-durability repair step.
     */
    public static int materialCost(ItemStack stack) {
        if (stack == null || stack.isEmpty() || !isReforgeable(stack)) return 0;

        int cost = stack.getItem() instanceof ArmorItem ? 3 : 2;
        if (stack.isDamageableItem() && stack.getDamageValue() > 0) {
            int quarter = Math.max(1, stack.getMaxDamage() / 4);
            cost += (stack.getDamageValue() + quarter - 1) / quarter;
        }
        return cost;
    }

    public static boolean isReforgeable(ItemStack stack) {
        if (stack == null || stack.isEmpty()) return false;
        if (!GOTSmithingModifierData.getModifiers(stack).isEmpty()) return true;
        for (GOTSmithingModifier modifier : GOTSmithingModifier.values()) {
            if (modifier.randomReforgeEligible() && modifier.canApply(stack)) return true;
        }
        return false;
    }

    /**
     * Mirrors 1.7.10 reforgeItem -> applyRandomEnchantments(stack, random, true, true):
     * 15% two modifiers, 65% one, 20% none.
     * Skilful modifiers are allowed and beneficial results are strongly favoured.
     */
    public static ItemStack reforge(ItemStack input, RandomSource random) {
        ItemStack result = input.copy();
        result.setCount(1);
        if (result.isDamageableItem()) result.setDamageValue(0);

        // Reforge replaces old GOT modifiers.
        GOTSmithingModifierData.clear(result);
        // Remove the compatibility Silk Touch enchant if Masterful had provided it.
        if (result.getEnchantmentLevel(Enchantments.SILK_TOUCH) > 0) {
            result.getOrCreateTag().remove("Enchantments");
        }

        float roll = random.nextFloat();
        int targetCount = roll < 0.15F ? 2 : roll < 0.80F ? 1 : 0;

        List<GOTSmithingModifier> pool = new ArrayList<>();
        for (GOTSmithingModifier modifier : GOTSmithingModifier.values()) {
            if (!modifier.randomReforgeEligible() || !modifier.canApply(result)) continue;
            pool.add(modifier);
        }

        List<GOTSmithingModifier> selected = new ArrayList<>();
        for (int i = 0; i < targetCount && !pool.isEmpty(); i++) {
            GOTSmithingModifier chosen = weightedPick(pool, result, random);
            if (chosen == null) break;
            selected.add(chosen);
            pool.removeIf(other -> !chosen.compatibleWith(other) || !other.compatibleWith(chosen));
        }

        GOTSmithingModifierData.set(result, selected);
        for (GOTSmithingModifier modifier : selected) {
            if (modifier.effect() == GOTSmithingEffect.SILK_TOUCH) {
                result.enchant(Enchantments.SILK_TOUCH, 1);
            }
        }

        result.getOrCreateTag().putInt("GOTRepairCost", 0);
        result.getOrCreateTag().putBoolean("GOTRandomReforged", true);
        return result;
    }

    private static GOTSmithingModifier weightedPick(List<GOTSmithingModifier> pool,
                                                    ItemStack item,
                                                    RandomSource random) {
        int total = 0;
        List<Integer> weights = new ArrayList<>(pool.size());
        for (GOTSmithingModifier modifier : pool) {
            int weight = skilfulWeight(modifier);

            // Exact old helper penalty: ItemTool receiving a modifier that is
            // neither TOOL nor BREAKABLE gets one-third weight.
            if (item.getItem() instanceof DiggerItem
                    && !modifier.itemTypes().contains(GOTSmithingItemType.TOOL)
                    && !modifier.itemTypes().contains(GOTSmithingItemType.BREAKABLE)) {
                weight = Math.max(1, weight / 3);
            }
            weights.add(weight);
            total += weight;
        }
        if (total <= 0) return null;

        int roll = random.nextInt(total);
        for (int i = 0; i < pool.size(); i++) {
            roll -= weights.get(i);
            if (roll < 0) return pool.get(i);
        }
        return pool.get(pool.size() - 1);
    }

    /**
     * Exact 1.7.10 getSkilfulWeight behavior.
     */
    private static int skilfulWeight(GOTSmithingModifier modifier) {
        double weight = modifier.enchantWeight();
        if (modifier.beneficial()) {
            weight = Math.pow(weight, 0.3D);
        }
        weight *= 100.0D;
        if (!modifier.beneficial()) {
            weight *= 0.15D;
        }
        return Math.max(1, (int)Math.round(weight));
    }
}
