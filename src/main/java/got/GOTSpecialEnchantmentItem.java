package got;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

public final class GOTSpecialEnchantmentItem extends Item {
    private final GOTSmithingModifier modifier;

    public GOTSpecialEnchantmentItem(GOTSmithingModifier modifier, Properties properties) {
        super(properties);
        this.modifier = modifier;
    }

    public GOTSmithingModifier modifier() {
        return modifier;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level,
                                List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(modifier.displayName().copy().withStyle(ChatFormatting.GOLD));
        tooltip.add(modifier.effectDescription().copy().withStyle(ChatFormatting.GRAY));
        tooltip.add(modifier.appliesToDescription().copy().withStyle(ChatFormatting.DARK_GRAY));
    }
}
