package got;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.Optional;
import java.util.Set;

public enum GOTSmithingModifier {
    STRONG_1("strong1", "got.enchant.strong1", GOTSmithingEffect.DAMAGE_ADD, 0.5F, 10, false, Family.DAMAGE, EnumSet.of(GOTSmithingItemType.MELEE, GOTSmithingItemType.THROWING_AXE)),
    STRONG_2("strong2", "got.enchant.strong2", GOTSmithingEffect.DAMAGE_ADD, 1.0F, 5, false, Family.DAMAGE, EnumSet.of(GOTSmithingItemType.MELEE, GOTSmithingItemType.THROWING_AXE)),
    STRONG_3("strong3", "got.enchant.strong3", GOTSmithingEffect.DAMAGE_ADD, 2.0F, 2, true, Family.DAMAGE, EnumSet.of(GOTSmithingItemType.MELEE, GOTSmithingItemType.THROWING_AXE)),
    STRONG_4("strong4", "got.enchant.strong4", GOTSmithingEffect.DAMAGE_ADD, 3.0F, 1, true, Family.DAMAGE, EnumSet.of(GOTSmithingItemType.MELEE, GOTSmithingItemType.THROWING_AXE)),
    DURABLE_1("durable1", "got.enchant.durable1", GOTSmithingEffect.DURABILITY_FACTOR, 1.25F, 15, false, Family.DURABILITY, EnumSet.of(GOTSmithingItemType.BREAKABLE)),
    DURABLE_2("durable2", "got.enchant.durable2", GOTSmithingEffect.DURABILITY_FACTOR, 1.5F, 8, false, Family.DURABILITY, EnumSet.of(GOTSmithingItemType.BREAKABLE)),
    DURABLE_3("durable3", "got.enchant.durable3", GOTSmithingEffect.DURABILITY_FACTOR, 2.0F, 4, true, Family.DURABILITY, EnumSet.of(GOTSmithingItemType.BREAKABLE)),
    MELEE_SPEED_1("meleeSpeed1", "got.enchant.meleeSpeed1", GOTSmithingEffect.MELEE_SPEED_FACTOR, 1.25F, 6, false, Family.MELEE_SPEED, EnumSet.of(GOTSmithingItemType.MELEE)),
    MELEE_REACH_1("meleeReach1", "got.enchant.meleeReach1", GOTSmithingEffect.MELEE_REACH_FACTOR, 1.25F, 6, false, Family.MELEE_REACH, EnumSet.of(GOTSmithingItemType.MELEE)),
    KNOCKBACK_1("knockback1", "got.enchant.knockback1", GOTSmithingEffect.KNOCKBACK_ADD, 1.0F, 6, false, Family.KNOCKBACK, EnumSet.of(GOTSmithingItemType.MELEE, GOTSmithingItemType.THROWING_AXE)),
    KNOCKBACK_2("knockback2", "got.enchant.knockback2", GOTSmithingEffect.KNOCKBACK_ADD, 2.0F, 2, true, Family.KNOCKBACK, EnumSet.of(GOTSmithingItemType.MELEE, GOTSmithingItemType.THROWING_AXE)),
    TOOL_SPEED_1("toolSpeed1", "got.enchant.toolSpeed1", GOTSmithingEffect.TOOL_SPEED_FACTOR, 1.5F, 20, false, Family.TOOL_SPEED, EnumSet.of(GOTSmithingItemType.TOOL, GOTSmithingItemType.SHEARS)),
    TOOL_SPEED_2("toolSpeed2", "got.enchant.toolSpeed2", GOTSmithingEffect.TOOL_SPEED_FACTOR, 2.0F, 10, false, Family.TOOL_SPEED, EnumSet.of(GOTSmithingItemType.TOOL, GOTSmithingItemType.SHEARS)),
    TOOL_SPEED_3("toolSpeed3", "got.enchant.toolSpeed3", GOTSmithingEffect.TOOL_SPEED_FACTOR, 3.0F, 5, true, Family.TOOL_SPEED, EnumSet.of(GOTSmithingItemType.TOOL, GOTSmithingItemType.SHEARS)),
    TOOL_SPEED_4("toolSpeed4", "got.enchant.toolSpeed4", GOTSmithingEffect.TOOL_SPEED_FACTOR, 4.0F, 2, true, Family.TOOL_SPEED, EnumSet.of(GOTSmithingItemType.TOOL, GOTSmithingItemType.SHEARS)),
    TOOL_SILK("toolSilk", "got.enchant.toolSilk", GOTSmithingEffect.SILK_TOUCH, 1.0F, 10, true, Family.SILK, EnumSet.of(GOTSmithingItemType.TOOL)),
    LOOTING_1("looting1", "got.enchant.looting1", GOTSmithingEffect.LOOTING_ADD, 1.0F, 6, false, Family.LOOTING, EnumSet.of(GOTSmithingItemType.TOOL, GOTSmithingItemType.MELEE)),
    LOOTING_2("looting2", "got.enchant.looting2", GOTSmithingEffect.LOOTING_ADD, 2.0F, 2, true, Family.LOOTING, EnumSet.of(GOTSmithingItemType.TOOL, GOTSmithingItemType.MELEE)),
    LOOTING_3("looting3", "got.enchant.looting3", GOTSmithingEffect.LOOTING_ADD, 3.0F, 1, true, Family.LOOTING, EnumSet.of(GOTSmithingItemType.TOOL, GOTSmithingItemType.MELEE)),
    PROTECT_1("protect1", "got.enchant.protect1", GOTSmithingEffect.ARMOR_PROTECTION_ADD, 1.0F, 10, false, Family.PROTECT, EnumSet.of(GOTSmithingItemType.ARMOR)),
    PROTECT_2("protect2", "got.enchant.protect2", GOTSmithingEffect.ARMOR_PROTECTION_ADD, 2.0F, 3, true, Family.PROTECT, EnumSet.of(GOTSmithingItemType.ARMOR)),
    PROTECT_FIRE_1("protectFire1", "got.enchant.protectFire1", GOTSmithingEffect.FIRE_PROTECTION_ADD, 1.0F, 5, false, Family.PROTECT_FIRE, EnumSet.of(GOTSmithingItemType.ARMOR)),
    PROTECT_FIRE_2("protectFire2", "got.enchant.protectFire2", GOTSmithingEffect.FIRE_PROTECTION_ADD, 2.0F, 2, true, Family.PROTECT_FIRE, EnumSet.of(GOTSmithingItemType.ARMOR)),
    PROTECT_FIRE_3("protectFire3", "got.enchant.protectFire3", GOTSmithingEffect.FIRE_PROTECTION_ADD, 3.0F, 1, true, Family.PROTECT_FIRE, EnumSet.of(GOTSmithingItemType.ARMOR)),
    PROTECT_FALL_1("protectFall1", "got.enchant.protectFall1", GOTSmithingEffect.FALL_PROTECTION_ADD, 1.0F, 5, false, Family.PROTECT_FALL, EnumSet.of(GOTSmithingItemType.ARMOR_FEET)),
    PROTECT_FALL_2("protectFall2", "got.enchant.protectFall2", GOTSmithingEffect.FALL_PROTECTION_ADD, 2.0F, 2, true, Family.PROTECT_FALL, EnumSet.of(GOTSmithingItemType.ARMOR_FEET)),
    PROTECT_FALL_3("protectFall3", "got.enchant.protectFall3", GOTSmithingEffect.FALL_PROTECTION_ADD, 3.0F, 1, true, Family.PROTECT_FALL, EnumSet.of(GOTSmithingItemType.ARMOR_FEET)),
    PROTECT_RANGED_1("protectRanged1", "got.enchant.protectRanged1", GOTSmithingEffect.RANGED_PROTECTION_ADD, 1.0F, 5, false, Family.PROTECT_RANGED, EnumSet.of(GOTSmithingItemType.ARMOR)),
    PROTECT_RANGED_2("protectRanged2", "got.enchant.protectRanged2", GOTSmithingEffect.RANGED_PROTECTION_ADD, 2.0F, 2, true, Family.PROTECT_RANGED, EnumSet.of(GOTSmithingItemType.ARMOR)),
    PROTECT_RANGED_3("protectRanged3", "got.enchant.protectRanged3", GOTSmithingEffect.RANGED_PROTECTION_ADD, 3.0F, 1, true, Family.PROTECT_RANGED, EnumSet.of(GOTSmithingItemType.ARMOR)),
    RANGED_STRONG_1("rangedStrong1", "got.enchant.rangedStrong1", GOTSmithingEffect.RANGED_DAMAGE_FACTOR, 1.1F, 10, false, Family.RANGED_DAMAGE, EnumSet.of(GOTSmithingItemType.RANGED_LAUNCHER)),
    RANGED_STRONG_2("rangedStrong2", "got.enchant.rangedStrong2", GOTSmithingEffect.RANGED_DAMAGE_FACTOR, 1.2F, 3, false, Family.RANGED_DAMAGE, EnumSet.of(GOTSmithingItemType.RANGED_LAUNCHER)),
    RANGED_STRONG_3("rangedStrong3", "got.enchant.rangedStrong3", GOTSmithingEffect.RANGED_DAMAGE_FACTOR, 1.3F, 1, true, Family.RANGED_DAMAGE, EnumSet.of(GOTSmithingItemType.RANGED_LAUNCHER)),
    RANGED_KNOCKBACK_1("rangedKnockback1", "got.enchant.rangedKnockback1", GOTSmithingEffect.RANGED_KNOCKBACK_ADD, 1.0F, 6, false, Family.RANGED_KNOCKBACK, EnumSet.of(GOTSmithingItemType.RANGED_LAUNCHER)),
    RANGED_KNOCKBACK_2("rangedKnockback2", "got.enchant.rangedKnockback2", GOTSmithingEffect.RANGED_KNOCKBACK_ADD, 2.0F, 2, true, Family.RANGED_KNOCKBACK, EnumSet.of(GOTSmithingItemType.RANGED_LAUNCHER)),

    WEAK_1("weak1", "got.enchant.weak1", GOTSmithingEffect.DAMAGE_ADD, -0.5F, 6, false, Family.DAMAGE, EnumSet.of(GOTSmithingItemType.MELEE, GOTSmithingItemType.THROWING_AXE)),
    WEAK_2("weak2", "got.enchant.weak2", GOTSmithingEffect.DAMAGE_ADD, -1.0F, 4, false, Family.DAMAGE, EnumSet.of(GOTSmithingItemType.MELEE, GOTSmithingItemType.THROWING_AXE)),
    WEAK_3("weak3", "got.enchant.weak3", GOTSmithingEffect.DAMAGE_ADD, -2.0F, 2, false, Family.DAMAGE, EnumSet.of(GOTSmithingItemType.MELEE, GOTSmithingItemType.THROWING_AXE)),
    MELEE_SLOW_1("meleeSlow1", "got.enchant.meleeSlow1", GOTSmithingEffect.MELEE_SPEED_FACTOR, 0.75F, 4, false, Family.MELEE_SPEED, EnumSet.of(GOTSmithingItemType.MELEE)),
    MELEE_UNREACH_1("meleeUnreach1", "got.enchant.meleeUnreach1", GOTSmithingEffect.MELEE_REACH_FACTOR, 0.75F, 4, false, Family.MELEE_REACH, EnumSet.of(GOTSmithingItemType.MELEE)),
    TOOL_SLOW_1("toolSlow1", "got.enchant.toolSlow1", GOTSmithingEffect.TOOL_SPEED_FACTOR, 0.75F, 10, false, Family.TOOL_SPEED, EnumSet.of(GOTSmithingItemType.TOOL, GOTSmithingItemType.SHEARS)),
    PROTECT_WEAK_1("protectWeak1", "got.enchant.protectWeak1", GOTSmithingEffect.ARMOR_PROTECTION_ADD, -1.0F, 5, false, Family.PROTECT, EnumSet.of(GOTSmithingItemType.ARMOR)),
    PROTECT_WEAK_2("protectWeak2", "got.enchant.protectWeak2", GOTSmithingEffect.ARMOR_PROTECTION_ADD, -2.0F, 2, false, Family.PROTECT, EnumSet.of(GOTSmithingItemType.ARMOR)),
    RANGED_WEAK_1("rangedWeak1", "got.enchant.rangedWeak1", GOTSmithingEffect.RANGED_DAMAGE_FACTOR, 0.75F, 8, false, Family.RANGED_DAMAGE, EnumSet.of(GOTSmithingItemType.RANGED_LAUNCHER)),
    RANGED_WEAK_2("rangedWeak2", "got.enchant.rangedWeak2", GOTSmithingEffect.RANGED_DAMAGE_FACTOR, 0.5F, 3, false, Family.RANGED_DAMAGE, EnumSet.of(GOTSmithingItemType.RANGED_LAUNCHER)),

    FIRE("fire", "got.enchant.fire", GOTSmithingEffect.FIRE_ON_HIT, 3.0F, 0, false,
            Family.FIRE_SPECIAL, EnumSet.of(GOTSmithingItemType.MELEE, GOTSmithingItemType.THROWING_AXE, GOTSmithingItemType.RANGED_LAUNCHER)),
    CHILL("chill", "got.enchant.chill", GOTSmithingEffect.CHILL_ON_HIT, 3.0F, 0, false,
            Family.CHILL_SPECIAL, EnumSet.of(GOTSmithingItemType.MELEE, GOTSmithingItemType.THROWING_AXE, GOTSmithingItemType.RANGED_LAUNCHER)),
    HEADHUNTING("headhunting", "got.enchant.headhunting", GOTSmithingEffect.HEADHUNTING, 3.0F, 0, false,
            Family.HEADHUNTING_SPECIAL, EnumSet.of(GOTSmithingItemType.MELEE, GOTSmithingItemType.THROWING_AXE, GOTSmithingItemType.RANGED_LAUNCHER));

    public enum Family {
        DAMAGE, DURABILITY, MELEE_SPEED, MELEE_REACH, KNOCKBACK,
        TOOL_SPEED, SILK, LOOTING, PROTECT, PROTECT_FIRE, PROTECT_FALL,
        PROTECT_RANGED, RANGED_DAMAGE, RANGED_KNOCKBACK,
        FIRE_SPECIAL, CHILL_SPECIAL, HEADHUNTING_SPECIAL
    }

    private final String legacyName;
    private final String translationKey;
    private final GOTSmithingEffect effect;
    private final float effectValue;
    private final int enchantWeight;
    private final boolean skilful;
    private final Family family;
    private final Set<GOTSmithingItemType> itemTypes;

    GOTSmithingModifier(String legacyName, String translationKey,
                        GOTSmithingEffect effect, float effectValue,
                        int enchantWeight, boolean skilful, Family family,
                        Set<GOTSmithingItemType> itemTypes) {
        this.legacyName = legacyName;
        this.translationKey = translationKey;
        this.effect = effect;
        this.effectValue = effectValue;
        this.enchantWeight = enchantWeight;
        this.skilful = skilful;
        this.family = family;
        this.itemTypes = Set.copyOf(itemTypes);
    }

    public String legacyName() { return legacyName; }
    public Component displayName() { return Component.translatable(translationKey); }
    public GOTSmithingEffect effect() { return effect; }
    public float effectValue() { return effectValue; }
    public int enchantWeight() { return enchantWeight; }
    public boolean skilful() { return skilful; }
    public Family family() { return family; }
    public Set<GOTSmithingItemType> itemTypes() { return itemTypes; }


    public boolean beneficial() {
        return switch (effect) {
            case DAMAGE_ADD, KNOCKBACK_ADD, LOOTING_ADD, ARMOR_PROTECTION_ADD,
                    FIRE_PROTECTION_ADD, FALL_PROTECTION_ADD, RANGED_PROTECTION_ADD,
                    RANGED_KNOCKBACK_ADD -> effectValue >= 0.0F;
            case DURABILITY_FACTOR, MELEE_SPEED_FACTOR, MELEE_REACH_FACTOR,
                    TOOL_SPEED_FACTOR, RANGED_DAMAGE_FACTOR -> effectValue >= 1.0F;
            case SILK_TOUCH, FIRE_ON_HIT, CHILL_ON_HIT, HEADHUNTING -> true;
        };
    }

    public boolean randomReforgeEligible() {
        return enchantWeight > 0;
    }

    public boolean canApply(ItemStack stack) {
        return itemTypes.stream().anyMatch(type -> type.matches(stack));
    }

    /**
     * Original compatibility rule: enchantments implemented by the same legacy
     * class are mutually exclusive. Silk Touch and Looting are also explicitly
     * incompatible. Fire and ranged protection are mutually exclusive special
     * protections; fall protection is explicitly compatible with other special
     * protection.
     */
    public boolean compatibleWith(GOTSmithingModifier other) {
        if (other == null || other == this) return false;
        if (family == other.family) return false;
        if ((family == Family.SILK && other.family == Family.LOOTING)
                || (family == Family.LOOTING && other.family == Family.SILK)) return false;
        if (isNonFallSpecialProtection(family) && isNonFallSpecialProtection(other.family)) return false;
        if (isWeaponSpecial(family) && isWeaponSpecial(other.family)) {
            return family == Family.HEADHUNTING_SPECIAL || other.family == Family.HEADHUNTING_SPECIAL;
        }
        return true;
    }

    private static boolean isWeaponSpecial(Family family) {
        return family == Family.FIRE_SPECIAL
                || family == Family.CHILL_SPECIAL
                || family == Family.HEADHUNTING_SPECIAL;
    }

    private static boolean isNonFallSpecialProtection(Family family) {
        return family == Family.PROTECT_FIRE || family == Family.PROTECT_RANGED;
    }


    /**
     * Legacy physical-anvil material cost.
     *
     * GOTContainerAnvil added max(1, (int)getValueModifier()) for each
     * beneficial modifier. These formulas reproduce the recovered
     * valueModifier behavior of the original enchantment subclasses.
     */
    public int materialCost() {
        float valueModifier = switch (effect) {
            case DAMAGE_ADD -> (7.0F + effectValue * 5.0F) / 7.0F;
            case DURABILITY_FACTOR, MELEE_SPEED_FACTOR, MELEE_REACH_FACTOR,
                    TOOL_SPEED_FACTOR -> effectValue;
            case KNOCKBACK_ADD, RANGED_KNOCKBACK_ADD -> (effectValue + 2.0F) / 2.0F;
            case SILK_TOUCH -> 3.0F;
            case LOOTING_ADD -> 1.0F + effectValue;
            case ARMOR_PROTECTION_ADD, FIRE_PROTECTION_ADD,
                    FALL_PROTECTION_ADD, RANGED_PROTECTION_ADD
                    -> (2.0F + effectValue) / 2.0F;
            case RANGED_DAMAGE_FACTOR -> effectValue > 1.0F ? effectValue * 2.0F : effectValue;
            case FIRE_ON_HIT, CHILL_ON_HIT, HEADHUNTING -> 3.0F;
        };
        return Math.max(1, (int) valueModifier);
    }

    public Component effectDescription() {
        String value = switch (effect) {
            case DAMAGE_ADD -> signed(effectValue);
            case DURABILITY_FACTOR, MELEE_SPEED_FACTOR, MELEE_REACH_FACTOR, TOOL_SPEED_FACTOR,
                    RANGED_DAMAGE_FACTOR -> multiplier(effectValue);
            case KNOCKBACK_ADD, LOOTING_ADD, ARMOR_PROTECTION_ADD, FIRE_PROTECTION_ADD,
                    FALL_PROTECTION_ADD, RANGED_PROTECTION_ADD, RANGED_KNOCKBACK_ADD
                    -> signed((int) effectValue);
            case SILK_TOUCH, FIRE_ON_HIT, CHILL_ON_HIT, HEADHUNTING -> "";
        };

        return switch (effect) {
            case DAMAGE_ADD -> Component.translatable("got.enchant.damage.desc", value);
            case DURABILITY_FACTOR -> Component.translatable("got.enchant.durable.desc", value);
            case MELEE_SPEED_FACTOR -> Component.translatable("got.enchant.meleeSpeed.desc", value);
            case MELEE_REACH_FACTOR -> Component.translatable("got.enchant.meleeReach.desc", value);
            case KNOCKBACK_ADD -> Component.translatable("got.enchant.knockback.desc", value);
            case TOOL_SPEED_FACTOR -> Component.translatable("got.enchant.toolSpeed.desc", value);
            case SILK_TOUCH -> Component.translatable("got.enchant.toolSilk.desc");
            case LOOTING_ADD -> Component.translatable("got.enchant.looting.desc", value);
            case ARMOR_PROTECTION_ADD -> Component.translatable("got.enchant.protect.desc", value);
            case FIRE_PROTECTION_ADD -> Component.translatable("got.enchant.protectFire.desc", value);
            case FALL_PROTECTION_ADD -> Component.translatable("got.enchant.protectFall.desc", value);
            case RANGED_PROTECTION_ADD -> Component.translatable("got.enchant.protectRanged.desc", value);
            case RANGED_DAMAGE_FACTOR -> Component.translatable("got.enchant.rangedDamage.desc", value);
            case RANGED_KNOCKBACK_ADD -> Component.translatable("got.enchant.rangedKnockback.desc", value);
            case FIRE_ON_HIT -> Component.translatable("got.enchant.fire.desc");
            case CHILL_ON_HIT -> Component.translatable("got.enchant.chill.desc");
            case HEADHUNTING -> Component.translatable("got.enchant.headhunting.desc");
        };
    }

    public Component appliesToDescription() {
        String joined = itemTypes.stream()
                .map(t -> Component.translatable("got.smithing.type." + t.name().toLowerCase()).getString())
                .sorted()
                .reduce((a,b) -> a + ", " + b)
                .orElse("");
        return Component.translatable("got.smithing.applies_to", joined);
    }

    private static String signed(float v) {
        return (v >= 0 ? "+" : "") + (v == Math.rint(v) ? Integer.toString((int)v) : Float.toString(v));
    }

    private static String signed(int v) {
        return (v >= 0 ? "+" : "") + v;
    }

    private static String multiplier(float v) {
        return "x" + (v == Math.rint(v) ? Integer.toString((int)v) : Float.toString(v));
    }

    public static Optional<GOTSmithingModifier> byLegacyName(String name) {
        return Arrays.stream(values()).filter(v -> v.legacyName.equals(name)).findFirst();
    }
}
