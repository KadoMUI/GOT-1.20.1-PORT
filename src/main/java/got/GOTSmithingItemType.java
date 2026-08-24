package got;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.*;
import net.minecraft.world.entity.ai.attributes.Attributes;

public enum GOTSmithingItemType {
    BREAKABLE,
    ARMOR,
    ARMOR_FEET,
    MELEE,
    TOOL,
    SHEARS,
    RANGED_LAUNCHER,
    THROWING_AXE;

    public boolean matches(ItemStack stack) {
        if (stack == null || stack.isEmpty()) return false;
        Item item = stack.getItem();

        return switch (this) {
            case BREAKABLE -> stack.isDamageableItem();
            case ARMOR -> item instanceof ArmorItem;
            case ARMOR_FEET -> item instanceof ArmorItem armor
                    && armor.getEquipmentSlot() == EquipmentSlot.FEET;
            case TOOL -> item instanceof DiggerItem;
            case SHEARS -> item instanceof ShearsItem;
            case RANGED_LAUNCHER -> item instanceof BowItem || item instanceof CrossbowItem;
            case THROWING_AXE -> item.getClass().getSimpleName().toLowerCase().contains("throwingaxe")
                    || item.getClass().getSimpleName().toLowerCase().contains("throwing_axe");
            case MELEE -> {
                if (item instanceof BowItem || item instanceof CrossbowItem) yield false;
                if (item.getClass().getSimpleName().contains("CommandSword")) yield false;
                var modifiers = stack.getAttributeModifiers(EquipmentSlot.MAINHAND)
                        .get(Attributes.ATTACK_DAMAGE);
                yield modifiers != null && !modifiers.isEmpty();
            }
        };
    }
}
