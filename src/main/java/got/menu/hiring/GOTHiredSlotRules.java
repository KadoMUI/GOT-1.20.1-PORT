package got.menu.hiring;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.TridentItem;
import net.minecraft.world.item.AxeItem;

/**
 * Central slot-validation rules.
 *
 * These are intentionally broad enough for the current port.
 * Exact legacy weapon classes can be added here without changing the menus.
 */
public final class GOTHiredSlotRules {
    private GOTHiredSlotRules() {}

    public static boolean isWeapon(ItemStack stack) {
        if (stack.isEmpty()) return true;
        return stack.getItem() instanceof SwordItem
            || stack.getItem() instanceof AxeItem
            || stack.getItem() instanceof BowItem
            || stack.getItem() instanceof CrossbowItem
            || stack.getItem() instanceof TridentItem;
    }

    public static boolean isArmor(ItemStack stack, EquipmentSlot slot) {
        if (stack.isEmpty()) return true;
        if (!(stack.getItem() instanceof ArmorItem armor)) return false;
        return armor.getEquipmentSlot() == slot;
    }

    public static boolean isFarmerWorkItem(ItemStack stack) {
        if (stack.isEmpty()) return true;

        // Keep deliberately permissive until the exact legacy farmer whitelist
        // is transcribed. This still provides a dedicated hook for parity.
        return true;
    }
}
