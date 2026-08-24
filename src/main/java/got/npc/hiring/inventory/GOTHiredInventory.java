package got.npc.hiring.inventory;

import net.minecraft.core.NonNullList;
import net.minecraft.world.Container;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

/**
 * Entity-backed Container. Changes are immediately persisted to the NPC.
 */
public final class GOTHiredInventory implements Container {
    private final Entity entity;
    private final NonNullList<ItemStack> items;

    public GOTHiredInventory(Entity entity, int size) {
        this.entity = entity;
        this.items = GOTHiredInventoryData.loadItems(entity, size);
    }

    @Override
    public int getContainerSize() {
        return items.size();
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack stack : items) if (!stack.isEmpty()) return false;
        return true;
    }

    @Override
    public ItemStack getItem(int slot) {
        return items.get(slot);
    }

    @Override
    public ItemStack removeItem(int slot, int amount) {
        ItemStack out = net.minecraft.world.ContainerHelper.removeItem(items, slot, amount);
        if (!out.isEmpty()) setChanged();
        return out;
    }

    @Override
    public ItemStack removeItemNoUpdate(int slot) {
        ItemStack out = net.minecraft.world.ContainerHelper.takeItem(items, slot);
        if (!out.isEmpty()) setChanged();
        return out;
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        items.set(slot, stack);
        if (stack.getCount() > getMaxStackSize()) stack.setCount(getMaxStackSize());
        setChanged();
    }

    @Override
    public void setChanged() {
        GOTHiredInventoryData.saveItems(entity, items);
    }

    @Override
    public boolean stillValid(Player player) {
        return entity.isAlive() && player.distanceToSqr(entity) <= 64.0D;
    }

    @Override
    public void clearContent() {
        items.clear();
        setChanged();
    }
}
