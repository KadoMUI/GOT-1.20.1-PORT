package got;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * The legacy bottle of poison, adapted to the port's NBT-backed drink system.
 * It can poison a drink in the opposite hand or a placed filled vessel; if
 * deliberately consumed it applies the same fifteen-second poison duration as
 * the original item and leaves a glass bottle behind.
 */
public final class GOTPoisonBottleItem extends Item {
    public GOTPoisonBottleItem(Properties properties) {
        super(properties);
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.DRINK;
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 32;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        InteractionHand otherHand = hand == InteractionHand.MAIN_HAND
                ? InteractionHand.OFF_HAND
                : InteractionHand.MAIN_HAND;
        ItemStack otherStack = player.getItemInHand(otherHand);
        if (otherStack.getItem() instanceof GOTDrinkItem && !GOTDrinkItem.isPoisoned(otherStack)) {
            if (!level.isClientSide) {
                GOTDrinkItem.setPoisoned(otherStack, true);
                consumeBottle(player, hand, player.getItemInHand(hand));
            }
            return InteractionResultHolder.sidedSuccess(player.getItemInHand(hand), level.isClientSide);
        }
        return ItemUtils.startUsingInstantly(level, player, hand);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        if (!(level.getBlockEntity(context.getClickedPos()) instanceof PlacedDrinkVesselBlockEntity vessel)
                || !vessel.isFilled()
                || GOTDrinkItem.isPoisoned(vessel.getDrink())) {
            return InteractionResult.PASS;
        }

        if (!level.isClientSide) {
            ItemStack poisoned = vessel.getDrink().copy();
            GOTDrinkItem.setPoisoned(poisoned, true);
            vessel.setDrink(poisoned);
            Player player = context.getPlayer();
            if (player != null) {
                consumeBottle(player, context.getHand(), context.getItemInHand());
            }
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        if (!level.isClientSide) {
            entity.addEffect(new MobEffectInstance(MobEffects.POISON, 15 * 20, 0));
        }
        if (entity instanceof Player player && player.getAbilities().instabuild) {
            return stack;
        }
        stack.shrink(1);
        ItemStack empty = new ItemStack(Items.GLASS_BOTTLE);
        if (stack.isEmpty()) {
            return empty;
        }
        if (entity instanceof Player player && !player.getInventory().add(empty)) {
            player.drop(empty, false);
        }
        return stack;
    }

    @Override
    public boolean hasCraftingRemainingItem(ItemStack stack) {
        return true;
    }

    @Override
    public ItemStack getCraftingRemainingItem(ItemStack stack) {
        return new ItemStack(Items.GLASS_BOTTLE);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level,
                                List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable("tooltip.got.bottle_poison")
                .withStyle(ChatFormatting.DARK_RED));
    }

    private static void consumeBottle(Player player, InteractionHand hand, ItemStack poison) {
        if (player.getAbilities().instabuild) {
            return;
        }
        poison.shrink(1);
        ItemStack empty = new ItemStack(Items.GLASS_BOTTLE);
        if (poison.isEmpty()) {
            player.setItemInHand(hand, empty);
        } else if (!player.getInventory().add(empty)) {
            player.drop(empty, false);
        }
    }
}
