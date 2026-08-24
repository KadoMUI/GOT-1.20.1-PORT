package got.npc.hiring.inventory;

import got.npc.hiring.GOTHiredData;
import got.npc.hiring.GOTHiredTask;
import net.minecraft.core.NonNullList;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

/**
 * Synchronizes the hired warrior inventory with actual visible/combat equipment.
 *
 * Inventory slot mapping:
 * 0 = MAINHAND
 * 1 = HEAD
 * 2 = CHEST
 * 3 = LEGS
 * 4 = FEET
 */
public final class GOTHiredEquipmentSync {
    private GOTHiredEquipmentSync() {}

    public static void sync(LivingEntity npc) {
        if (!GOTHiredData.isHired(npc)) return;
        if (GOTHiredData.task(npc) != GOTHiredTask.WARRIOR) return;

        NonNullList<ItemStack> items =
            GOTHiredInventoryData.loadItems(npc, GOTHiredInventoryData.WARRIOR_SIZE);

        setIfChanged(npc, EquipmentSlot.MAINHAND, items.get(0));
        setIfChanged(npc, EquipmentSlot.HEAD, items.get(1));
        setIfChanged(npc, EquipmentSlot.CHEST, items.get(2));
        setIfChanged(npc, EquipmentSlot.LEGS, items.get(3));
        setIfChanged(npc, EquipmentSlot.FEET, items.get(4));
    }

    private static void setIfChanged(LivingEntity npc, EquipmentSlot slot, ItemStack desired) {
        ItemStack current = npc.getItemBySlot(slot);
        if (ItemStack.matches(current, desired)) return;
        npc.setItemSlot(slot, desired.copy());
    }
}
