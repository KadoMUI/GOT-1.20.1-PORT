package got.npc.hiring.inventory;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.NonNullList;

/**
 * Persistent hired-NPC inventory storage backed by entity persistent NBT.
 *
 * Warrior:
 *   equipment/replacement storage
 *
 * Farmer:
 *   work inventory / replaced-item storage
 *
 * Keeping this attached to the entity mirrors the legacy GOTHireableInfo model
 * and avoids having to add fields to every regional NPC class.
 */
public final class GOTHiredInventoryData {
    private static final String ROOT = "GOTHiredInventory";
    private static final String ITEMS = "Items";
    private static final String REPLACED = "Replaced";
    private static final String SIZE = "Size";

    public static final int WARRIOR_SIZE = 18;
    public static final int FARMER_SIZE = 18;
    public static final int REPLACED_SIZE = 9;

    private GOTHiredInventoryData() {}

    public static NonNullList<ItemStack> loadItems(Entity entity, int size) {
        CompoundTag root = entity.getPersistentData().getCompound(ROOT);
        NonNullList<ItemStack> items = NonNullList.withSize(size, ItemStack.EMPTY);
        if (root.contains(ITEMS, Tag.TAG_LIST)) {
            ContainerHelper.loadAllItems(root, items);
        }
        return items;
    }

    public static void initializeWarriorEquipment(net.minecraft.world.entity.LivingEntity entity) {
        CompoundTag existing = entity.getPersistentData().getCompound(ROOT);
        if (existing.getBoolean("Initialized")) return;

        NonNullList<ItemStack> items = NonNullList.withSize(WARRIOR_SIZE, ItemStack.EMPTY);
        items.set(0, entity.getItemBySlot(net.minecraft.world.entity.EquipmentSlot.MAINHAND).copy());
        items.set(1, entity.getItemBySlot(net.minecraft.world.entity.EquipmentSlot.HEAD).copy());
        items.set(2, entity.getItemBySlot(net.minecraft.world.entity.EquipmentSlot.CHEST).copy());
        items.set(3, entity.getItemBySlot(net.minecraft.world.entity.EquipmentSlot.LEGS).copy());
        items.set(4, entity.getItemBySlot(net.minecraft.world.entity.EquipmentSlot.FEET).copy());

        CompoundTag root = entity.getPersistentData().getCompound(ROOT);
        root.putBoolean("Initialized", true);
        ContainerHelper.saveAllItems(root, items);
        entity.getPersistentData().put(ROOT, root);
    }

    public static void saveItems(Entity entity, NonNullList<ItemStack> items) {
        CompoundTag root = entity.getPersistentData().getCompound(ROOT);
        root.putInt(SIZE, items.size());
        ContainerHelper.saveAllItems(root, items);
        entity.getPersistentData().put(ROOT, root);
    }

    public static NonNullList<ItemStack> loadReplaced(Entity entity) {
        CompoundTag root = entity.getPersistentData().getCompound(ROOT);
        NonNullList<ItemStack> items = NonNullList.withSize(REPLACED_SIZE, ItemStack.EMPTY);

        if (root.contains(REPLACED, Tag.TAG_LIST)) {
            ListTag list = root.getList(REPLACED, Tag.TAG_COMPOUND);
            for (int i = 0; i < list.size(); i++) {
                CompoundTag entry = list.getCompound(i);
                int slot = entry.getByte("Slot") & 255;
                if (slot >= 0 && slot < items.size()) {
                    items.set(slot, ItemStack.of(entry));
                }
            }
        }
        return items;
    }

    public static void saveReplaced(Entity entity, NonNullList<ItemStack> items) {
        CompoundTag root = entity.getPersistentData().getCompound(ROOT);
        ListTag list = new ListTag();

        for (int i = 0; i < items.size(); i++) {
            ItemStack stack = items.get(i);
            if (stack.isEmpty()) continue;

            CompoundTag tag = new CompoundTag();
            tag.putByte("Slot", (byte)i);
            stack.save(tag);
            list.add(tag);
        }

        root.put(REPLACED, list);
        entity.getPersistentData().put(ROOT, root);
    }
}
