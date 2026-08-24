package got;

import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.InteractionResult;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Consumer;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;

/**
 * A filled GOT drink. The item registry identifies the liquid while NBT stores
 * the selected vessel and brew strength. This replaces the packed metadata used
 * by the 1.7.10 GOTItemMug class without multiplying registry entries.
 */
public class GOTDrinkItem extends Item {
    public static final String TAG_VESSEL = "GOTDrinkVessel";
    public static final String TAG_STRENGTH = "GOTDrinkStrength";
    public static final String TAG_POISONED = "GOTDrinkPoisoned";
    public static final String TAG_WILDFIRE_BURN = "GOTWildfireBurn";

    private final GOTDrinkDefinition definition;

    public GOTDrinkItem(GOTDrinkDefinition definition, Properties properties) {
        super(properties);
        this.definition = definition;
    }

    public GOTDrinkDefinition definition() {
        return definition;
    }

    public static GOTDrinkVessel getVessel(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        return tag == null ? GOTDrinkVessel.MUG : GOTDrinkVessel.byName(tag.getString(TAG_VESSEL));
    }

    public static void setVessel(ItemStack stack, GOTDrinkVessel vessel) {
        CompoundTag tag = stack.getOrCreateTag();
        tag.putString(TAG_VESSEL, vessel.serializedName());
        // Keep the visual item model synchronized with the stored vessel.
        // Vanilla model overrides can read CustomModelData even though they
        // cannot directly inspect our custom vessel string.
        if (vessel.modelData() == 0) {
            tag.remove("CustomModelData");
        } else {
            tag.putInt("CustomModelData", vessel.modelData());
        }
    }

    public static float getStrength(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        if (tag == null || !tag.contains(TAG_STRENGTH)) {
            return 1.0F;
        }
        return Math.max(0.25F, Math.min(2.0F, tag.getFloat(TAG_STRENGTH)));
    }

    public static void setStrength(ItemStack stack, float strength) {
        stack.getOrCreateTag().putFloat(TAG_STRENGTH, Math.max(0.25F, Math.min(2.0F, strength)));
    }

    public static boolean isPoisoned(ItemStack stack) {
        return stack.hasTag() && stack.getTag().getBoolean(TAG_POISONED);
    }

    public static void setPoisoned(ItemStack stack, boolean poisoned) {
        stack.getOrCreateTag().putBoolean(TAG_POISONED, poisoned);
    }

    public ItemStack createFilled(GOTDrinkVessel vessel, float strength) {
        ItemStack stack = new ItemStack(this);
        setVessel(stack, vessel);
        setStrength(stack, strength);
        return stack;
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(GOTVesselItemClientExtensions.INSTANCE);
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
        return ItemUtils.startUsingInstantly(level, player, hand);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        ItemStack stack = context.getItemInHand();
        GOTDrinkVessel vessel = getVessel(stack);
        if (!vessel.isPlaceable() || context.getClickedFace() != net.minecraft.core.Direction.UP) {
            return InteractionResult.PASS;
        }
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos().above();
        if (!level.getBlockState(pos).canBeReplaced()) return InteractionResult.FAIL;
        net.minecraft.world.level.block.state.BlockState state = placedBlockFor(vessel).defaultBlockState();
        Player player = context.getPlayer();
        if (player != null) state = state.setValue(PlacedDrinkVesselBlock.FACING, player.getDirection().getOpposite());
        if (!state.canSurvive(level, pos)) return InteractionResult.FAIL;
        if (!level.isClientSide) {
            level.setBlock(pos, state, 3);
            if (level.getBlockEntity(pos) instanceof PlacedDrinkVesselBlockEntity placed) {
                placed.setDrink(stack);
            }
            if (player == null || !player.getAbilities().instabuild) stack.shrink(1);
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    public static net.minecraft.world.level.block.Block placedBlockFor(GOTDrinkVessel vessel) {
        return switch (vessel) {
            case MUG -> GOTBlocks.WOODEN_MUG.get();
            case CLAY_MUG -> GOTBlocks.CLAY_MUG.get();
            case CERAMIC_MUG -> GOTBlocks.CERAMIC_MUG.get();
            case GOLD_GOBLET -> GOTBlocks.GOLD_GOBLET.get();
            case SILVER_GOBLET -> GOTBlocks.SILVER_GOBLET.get();
            case COPPER_GOBLET -> GOTBlocks.COPPER_GOBLET.get();
            case WOODEN_GOBLET -> GOTBlocks.WOODEN_GOBLET.get();
            case BRONZE_GOBLET -> GOTBlocks.BRONZE_GOBLET.get();
            case VALYRIAN_GOBLET -> GOTBlocks.VALYRIAN_GOBLET.get();
            case SKULL_CUP -> GOTBlocks.SKULL_CUP.get();
            case WINE_GLASS -> GOTBlocks.WINE_GLASS.get();
            case BOTTLE -> GOTBlocks.GLASS_BOTTLE_VESSEL.get();
            case DRINKING_HORN -> GOTBlocks.ALE_HORN.get();
            case GOLD_DRINKING_HORN -> GOTBlocks.GOLDEN_ALE_HORN.get();
            default -> GOTBlocks.PLACED_DRINK_VESSEL.get();
        };
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        if (!level.isClientSide) {
            applyDrink(entity, stack);
        }

        if (entity instanceof Player player && player.getAbilities().instabuild) {
            return stack;
        }

        GOTDrinkVessel vessel = getVessel(stack);
        stack.shrink(1);
        ItemStack empty = vessel.emptyStack();

        if (stack.isEmpty()) {
            return empty;
        }

        if (entity instanceof Player player && !player.getInventory().add(empty)) {
            player.drop(empty, false);
        }
        return stack;
    }

    public void applyDrink(LivingEntity entity, ItemStack stack) {
        float strength = getStrength(stack);

        if (entity instanceof Player player && definition.nutrition() > 0) {
            player.getFoodData().eat(
                    Math.max(0, Math.round(definition.nutrition() * strength)),
                    definition.saturation());
        }

        if (definition.curesEffects()) {
            entity.removeAllEffects();
        }

        for (MobEffectInstance effect : definition.createEffects(strength)) {
            entity.addEffect(effect);
        }

        applyAlcohol(entity, definition.alcoholicity() * strength);

        if (isPoisoned(stack)) {
            entity.addEffect(new MobEffectInstance(MobEffects.POISON, 20 * 20, 0));
        }

        if ("mug_wild_fire".equals(definition.id())) {
            // Wildfire cannot be extinguished normally. The Forge tick handler
            // keeps reigniting the player until that entity dies.
            entity.getPersistentData().putBoolean(TAG_WILDFIRE_BURN, true);
            entity.setSecondsOnFire(2);
        } else if (definition.damage() > 0) {
            entity.hurt(entity.damageSources().magic(), definition.damage());
        }
    }

    private static void applyAlcohol(LivingEntity entity, float alcoholicity) {
        if (alcoholicity <= 0.0F) {
            return;
        }

        int addedDuration = Math.max(10 * 20, Math.round(30.0F * 20.0F * alcoholicity));
        int amplifier = alcoholicity >= 1.25F ? 2 : alcoholicity >= 0.75F ? 1 : 0;

        addOrExtendEffect(entity, MobEffects.CONFUSION, addedDuration, 4);
        addOrExtendEffect(entity, GOTEffects.DRUNKENNESS.get(), addedDuration, amplifier);
    }

    private static void addOrExtendEffect(LivingEntity entity,
                                          net.minecraft.world.effect.MobEffect effect,
                                          int addedDuration,
                                          int amplifier) {
        MobEffectInstance existing = entity.getEffect(effect);
        int duration = addedDuration;
        int finalAmplifier = amplifier;
        if (existing != null) {
            duration = Math.min(5 * 60 * 20, existing.getDuration() + addedDuration);
            finalAmplifier = Math.max(existing.getAmplifier(), amplifier);
        }
        entity.addEffect(new MobEffectInstance(effect, duration, finalAmplifier, false, true, true));
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        GOTDrinkVessel vessel = getVessel(stack);
        tooltip.add(Component.translatable("tooltip.got.drink.vessel", vessel.displayName())
                .withStyle(ChatFormatting.GRAY));

        float strength = getStrength(stack);
        if (definition.brewable()) {
            tooltip.add(Component.translatable("tooltip.got.drink.strength", strength)
                    .withStyle(ChatFormatting.DARK_AQUA));
        }

        if (definition.alcoholicity() > 0.0F) {
            tooltip.add(Component.translatable("tooltip.got.drink.alcoholic")
                    .withStyle(ChatFormatting.GOLD));
        } else {
            tooltip.add(Component.translatable("tooltip.got.drink.non_alcoholic")
                    .withStyle(ChatFormatting.GREEN));
        }

        if (isPoisoned(stack)) {
            tooltip.add(Component.translatable("tooltip.got.drink.poisoned")
                    .withStyle(ChatFormatting.DARK_RED));
        }
    }
}
