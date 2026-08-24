package got;

import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class GOTSmithingModifierData {
    public static final String NBT_ENCHANTS = "GOTEnch";

    private GOTSmithingModifierData() {}

    public static List<GOTSmithingModifier> getModifiers(ItemStack stack) {
        if (stack == null || stack.isEmpty() || !stack.hasTag()) return List.of();
        ListTag tags = stack.getTag().getList(NBT_ENCHANTS, Tag.TAG_STRING);
        List<GOTSmithingModifier> result = new ArrayList<>();
        for (int i = 0; i < tags.size(); i++) {
            GOTSmithingModifier.byLegacyName(tags.getString(i)).ifPresent(result::add);
        }
        return Collections.unmodifiableList(result);
    }


    public static void clear(ItemStack stack) {
        if (stack == null || stack.isEmpty() || !stack.hasTag()) return;
        stack.getTag().remove(NBT_ENCHANTS);
    }

    public static void set(ItemStack stack, Iterable<GOTSmithingModifier> modifiers) {
        ListTag tags = new ListTag();
        for (GOTSmithingModifier modifier : modifiers) {
            tags.add(StringTag.valueOf(modifier.legacyName()));
        }
        stack.getOrCreateTag().put(NBT_ENCHANTS, tags);
    }

    public static boolean has(ItemStack stack, GOTSmithingModifier modifier) {
        return getModifiers(stack).contains(modifier);
    }

    public static boolean canAdd(ItemStack stack, GOTSmithingModifier modifier) {
        if (modifier == null || !modifier.canApply(stack) || has(stack, modifier)) return false;
        for (GOTSmithingModifier existing : getModifiers(stack)) {
            if (!existing.compatibleWith(modifier) || !modifier.compatibleWith(existing)) return false;
        }
        return true;
    }

    public static boolean add(ItemStack stack, GOTSmithingModifier modifier) {
        if (!canAdd(stack, modifier)) return false;
        ListTag tags = stack.getOrCreateTag().getList(NBT_ENCHANTS, Tag.TAG_STRING);
        tags.add(StringTag.valueOf(modifier.legacyName()));
        stack.getOrCreateTag().put(NBT_ENCHANTS, tags);
        return true;
    }

    public static float baseMeleeDamageBoost(ItemStack stack) {
        return sum(stack, GOTSmithingEffect.DAMAGE_ADD);
    }

    public static float durabilityFactor(ItemStack stack) {
        return product(stack, GOTSmithingEffect.DURABILITY_FACTOR);
    }

    public static float meleeSpeedFactor(ItemStack stack) {
        return product(stack, GOTSmithingEffect.MELEE_SPEED_FACTOR);
    }

    public static float meleeReachFactor(ItemStack stack) {
        return product(stack, GOTSmithingEffect.MELEE_REACH_FACTOR);
    }

    public static int meleeKnockback(ItemStack stack) {
        return Math.round(sum(stack, GOTSmithingEffect.KNOCKBACK_ADD));
    }

    public static float toolSpeedFactor(ItemStack stack) {
        return product(stack, GOTSmithingEffect.TOOL_SPEED_FACTOR);
    }

    public static boolean silkTouch(ItemStack stack) {
        return getModifiers(stack).stream().anyMatch(m -> m.effect() == GOTSmithingEffect.SILK_TOUCH);
    }

    public static int lootingLevel(ItemStack stack) {
        return Math.round(sum(stack, GOTSmithingEffect.LOOTING_ADD));
    }

    public static int commonArmorProtection(ItemStack stack) {
        return Math.round(sum(stack, GOTSmithingEffect.ARMOR_PROTECTION_ADD));
    }

    public static int fireProtection(ItemStack stack) {
        return Math.round(sum(stack, GOTSmithingEffect.FIRE_PROTECTION_ADD));
    }

    public static int fallProtection(ItemStack stack) {
        return Math.round(sum(stack, GOTSmithingEffect.FALL_PROTECTION_ADD));
    }

    public static int rangedProtection(ItemStack stack) {
        return Math.round(sum(stack, GOTSmithingEffect.RANGED_PROTECTION_ADD));
    }

    public static float rangedDamageFactor(ItemStack stack) {
        return product(stack, GOTSmithingEffect.RANGED_DAMAGE_FACTOR);
    }

    public static int rangedKnockback(ItemStack stack) {
        return Math.round(sum(stack, GOTSmithingEffect.RANGED_KNOCKBACK_ADD));
    }

    private static float sum(ItemStack stack, GOTSmithingEffect effect) {
        float result = 0.0F;
        for (GOTSmithingModifier modifier : getModifiers(stack)) {
            if (modifier.effect() == effect) result += modifier.effectValue();
        }
        return result;
    }

    private static float product(ItemStack stack, GOTSmithingEffect effect) {
        float result = 1.0F;
        for (GOTSmithingModifier modifier : getModifiers(stack)) {
            if (modifier.effect() == effect) result *= modifier.effectValue();
        }
        return result;
    }
}
