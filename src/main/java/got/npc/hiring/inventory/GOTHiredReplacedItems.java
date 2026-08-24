package got.npc.hiring.inventory;

import net.minecraft.core.NonNullList;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;

/**
 * Legacy-equivalent replaced-item store.
 *
 * When a hired NPC's default loadout is replaced by player-supplied gear,
 * the displaced gear can be retained here instead of silently deleted.
 */
public final class GOTHiredReplacedItems {
    private final Entity entity;
    private final NonNullList<ItemStack> items;

    public GOTHiredReplacedItems(Entity entity) {
        this.entity = entity;
        this.items = GOTHiredInventoryData.loadReplaced(entity);
    }

    public ItemStack get(int slot) {
        return items.get(slot);
    }

    public void set(int slot, ItemStack stack) {
        items.set(slot, stack);
        save();
    }

    public ItemStack take(int slot) {
        ItemStack stack = items.get(slot);
        items.set(slot, ItemStack.EMPTY);
        save();
        return stack;
    }

    public boolean add(ItemStack stack) {
        if (stack.isEmpty()) return true;
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).isEmpty()) {
                items.set(i, stack.copy());
                save();
                return true;
            }
        }
        return false;
    }

    private void save() {
        GOTHiredInventoryData.saveReplaced(entity, items);
    }
}
