package got;

import net.minecraft.world.item.ItemStack;

import java.util.Map;
import java.util.Optional;

public final class GOTSmithScrollUpgrades {
    private static final Map<GOTSmithingModifier, GOTSmithingModifier> NEXT = Map.ofEntries(
        Map.entry(GOTSmithingModifier.STRONG_1, GOTSmithingModifier.STRONG_2),
        Map.entry(GOTSmithingModifier.STRONG_2, GOTSmithingModifier.STRONG_3),
        Map.entry(GOTSmithingModifier.STRONG_3, GOTSmithingModifier.STRONG_4),
        Map.entry(GOTSmithingModifier.DURABLE_1, GOTSmithingModifier.DURABLE_2),
        Map.entry(GOTSmithingModifier.DURABLE_2, GOTSmithingModifier.DURABLE_3),
        Map.entry(GOTSmithingModifier.KNOCKBACK_1, GOTSmithingModifier.KNOCKBACK_2),
        Map.entry(GOTSmithingModifier.TOOL_SPEED_1, GOTSmithingModifier.TOOL_SPEED_2),
        Map.entry(GOTSmithingModifier.TOOL_SPEED_2, GOTSmithingModifier.TOOL_SPEED_3),
        Map.entry(GOTSmithingModifier.TOOL_SPEED_3, GOTSmithingModifier.TOOL_SPEED_4),
        Map.entry(GOTSmithingModifier.LOOTING_1, GOTSmithingModifier.LOOTING_2),
        Map.entry(GOTSmithingModifier.LOOTING_2, GOTSmithingModifier.LOOTING_3),
        Map.entry(GOTSmithingModifier.PROTECT_1, GOTSmithingModifier.PROTECT_2),
        Map.entry(GOTSmithingModifier.PROTECT_FIRE_1, GOTSmithingModifier.PROTECT_FIRE_2),
        Map.entry(GOTSmithingModifier.PROTECT_FIRE_2, GOTSmithingModifier.PROTECT_FIRE_3),
        Map.entry(GOTSmithingModifier.PROTECT_FALL_1, GOTSmithingModifier.PROTECT_FALL_2),
        Map.entry(GOTSmithingModifier.PROTECT_FALL_2, GOTSmithingModifier.PROTECT_FALL_3),
        Map.entry(GOTSmithingModifier.PROTECT_RANGED_1, GOTSmithingModifier.PROTECT_RANGED_2),
        Map.entry(GOTSmithingModifier.PROTECT_RANGED_2, GOTSmithingModifier.PROTECT_RANGED_3),
        Map.entry(GOTSmithingModifier.RANGED_STRONG_1, GOTSmithingModifier.RANGED_STRONG_2),
        Map.entry(GOTSmithingModifier.RANGED_STRONG_2, GOTSmithingModifier.RANGED_STRONG_3),
        Map.entry(GOTSmithingModifier.RANGED_KNOCKBACK_1, GOTSmithingModifier.RANGED_KNOCKBACK_2)
    );

    private GOTSmithScrollUpgrades() {}

    public static Optional<GOTSmithingModifier> next(GOTSmithingModifier modifier) {
        return Optional.ofNullable(NEXT.get(modifier));
    }

    public static boolean canCombine(ItemStack first, ItemStack second) {
        if (first.getItem() != GOTItems.SMITH_SCROLL.get()
                || second.getItem() != GOTItems.SMITH_SCROLL.get()) return false;

        var a = GOTSmithScrollItem.getModifier(first);
        var b = GOTSmithScrollItem.getModifier(second);
        if (a.isEmpty() || b.isEmpty() || a.get() != b.get()) return false;

        return NEXT.containsKey(a.get());
    }

    public static ItemStack combine(ItemStack first, ItemStack second) {
        if (!canCombine(first, second)) return ItemStack.EMPTY;
        var current = GOTSmithScrollItem.getModifier(first).orElseThrow();
        var next = NEXT.get(current);
        return next == null ? ItemStack.EMPTY : GOTSmithScrollItem.create(next);
    }

    public static int upgradeCount() {
        return NEXT.size();
    }
}
