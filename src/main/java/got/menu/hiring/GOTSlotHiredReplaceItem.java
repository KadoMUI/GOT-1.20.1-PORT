package got.menu.hiring;

import got.npc.hiring.inventory.GOTHiredReplacedItems;
import net.minecraft.world.Container;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

import java.util.function.Predicate;

/**
 * Modern equivalent of the original GOTSlotHiredReplaceItem.
 *
 * Whenever an occupied slot is replaced, the displaced stack is retained in
 * the NPC's replacement-item store rather than being deleted.
 */
public final class GOTSlotHiredReplaceItem extends Slot {
    private final Entity npc;
    private final Predicate<ItemStack> validator;

    public GOTSlotHiredReplaceItem(
        Entity npc,
        Container container,
        int index,
        int x,
        int y,
        Predicate<ItemStack> validator
    ) {
        super(container, index, x, y);
        this.npc = npc;
        this.validator = validator;
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return validator.test(stack);
    }

    @Override
    public void set(ItemStack stack) {
        ItemStack old = getItem();
        if (!old.isEmpty() && !ItemStack.isSameItemSameTags(old, stack)) {
            GOTHiredReplacedItems replaced = new GOTHiredReplacedItems(npc);
            int slot = getSlotIndex();
            if (slot >= 0 && slot < got.npc.hiring.inventory.GOTHiredInventoryData.REPLACED_SIZE
                    && replaced.get(slot).isEmpty()) {
                replaced.set(slot, old.copy());
            }
        }
        super.set(stack);
        if (npc instanceof LivingEntity living) {
            got.npc.hiring.inventory.GOTHiredEquipmentSlotHook.changed(living, getSlotIndex());
        }
    }
}
