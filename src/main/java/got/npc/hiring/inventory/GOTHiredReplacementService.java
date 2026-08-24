package got.npc.hiring.inventory;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

/**
 * Restores NPC default/replaced equipment when player-provided equipment is
 * removed, using the replacement store from the inventory sub-pass.
 */
public final class GOTHiredReplacementService {
    private GOTHiredReplacementService() {}

    public static void restoreIfEmpty(LivingEntity npc, int hiredSlot) {
        EquipmentSlot equipment = switch (hiredSlot) {
            case 0 -> EquipmentSlot.MAINHAND;
            case 1 -> EquipmentSlot.HEAD;
            case 2 -> EquipmentSlot.CHEST;
            case 3 -> EquipmentSlot.LEGS;
            case 4 -> EquipmentSlot.FEET;
            default -> null;
        };
        if (equipment == null) return;

        GOTHiredInventory inventory =
            new GOTHiredInventory(npc, GOTHiredInventoryData.WARRIOR_SIZE);

        if (!inventory.getItem(hiredSlot).isEmpty()) return;

        GOTHiredReplacedItems replaced = new GOTHiredReplacedItems(npc);
        ItemStack restore = replaced.take(hiredSlot);
        if (restore.isEmpty()) return;

        inventory.setItem(hiredSlot, restore.copy());
        npc.setItemSlot(equipment, restore.copy());
    }

    public static void restoreAllMissing(LivingEntity npc) {
        for (int slot = 0; slot <= 4; slot++) {
            restoreIfEmpty(npc, slot);
        }
    }
}
