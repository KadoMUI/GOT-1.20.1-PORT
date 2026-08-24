package got;

import net.minecraft.ChatFormatting;
import net.minecraft.world.item.ItemStack;

public final class GOTAnvilNameColors {
    private GOTAnvilNameColors() {}

    public static ChatFormatting colorFor(ItemStack stack) {
        if (stack == null || stack.isEmpty()) return null;
        var item = stack.getItem();

        if (item == GOTItems.AMBER.get()) return ChatFormatting.YELLOW;
        if (item == GOTItems.AMETHYST.get()) return ChatFormatting.LIGHT_PURPLE;
        if (item == GOTItems.DIAMOND.get()) return ChatFormatting.GRAY;
        if (item == GOTItems.EMERALD.get()) return ChatFormatting.GREEN;
        if (item == GOTItems.OPAL.get()) return ChatFormatting.AQUA;
        if (item == GOTItems.RUBY.get()) return ChatFormatting.RED;
        if (item == GOTItems.SAPPHIRE.get()) return ChatFormatting.BLUE;
        if (item == GOTItems.TOPAZ.get()) return ChatFormatting.GOLD;
        return null;
    }

    public static boolean isNameColorGem(ItemStack stack) {
        return colorFor(stack) != null;
    }
}
