package got;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

/** Modern port of 1.7.10 GOTItemModifierTemplate. */
public final class GOTSmithScrollItem extends Item {
    public static final String NBT_MODIFIER = "ScrollModifier";

    public GOTSmithScrollItem(Properties properties) {
        super(properties.stacksTo(1));
    }

    public static ItemStack create(GOTSmithingModifier modifier) {
        ItemStack stack = new ItemStack(GOTItems.SMITH_SCROLL.get());
        setModifier(stack, modifier);
        return stack;
    }

    public static void setModifier(ItemStack stack, GOTSmithingModifier modifier) {
        stack.getOrCreateTag().putString(NBT_MODIFIER, modifier.legacyName());
    }

    public static Optional<GOTSmithingModifier> getModifier(ItemStack stack) {
        if (!stack.hasTag()) return Optional.empty();
        return GOTSmithingModifier.byLegacyName(stack.getTag().getString(NBT_MODIFIER));
    }

    @Override
    public Component getName(ItemStack stack) {
        return getModifier(stack)
                .<Component>map(mod -> Component.translatable("item.got.smith_scroll", mod.displayName()))
                .orElseGet(() -> Component.translatable("item.got.smith_scroll.unbound"));
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, level, tooltip, flag);
        getModifier(stack).ifPresent(mod -> {
            tooltip.add(Component.translatable("item.got.smith_scroll.modifier", mod.displayName())
                    .withStyle(ChatFormatting.GRAY));
            tooltip.add(mod.effectDescription().copy().withStyle(ChatFormatting.DARK_GRAY));
            tooltip.add(mod.appliesToDescription().copy().withStyle(ChatFormatting.DARK_GRAY));
            if (mod.skilful()) {
                tooltip.add(Component.translatable("got.smithing.skilful").withStyle(ChatFormatting.GOLD));
            }
        });
    }
}
