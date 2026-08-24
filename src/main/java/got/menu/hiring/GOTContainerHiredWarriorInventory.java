package got.menu.hiring;

import got.npc.hiring.GOTHiredData;
import got.npc.hiring.inventory.GOTHiredInventory;
import got.npc.hiring.inventory.GOTHiredInventoryData;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;


/**
 * Hired warrior inventory.
 *
 * Layout:
 * 0 weapon
 * 1-4 armor
 * 5-17 general hired inventory
 */
public final class GOTContainerHiredWarriorInventory extends AbstractContainerMenu {
    private final Entity npc;
    private final GOTHiredInventory hired;

    public GOTContainerHiredWarriorInventory(int id, Inventory playerInv, FriendlyByteBuf buffer) {
        this(id, playerInv, playerInv.player.level().getEntity(buffer.readVarInt()));
    }

    public GOTContainerHiredWarriorInventory(int id, Inventory playerInv, Entity npc) {
        super(got.GOTMenus.HIRED_WARRIOR_INVENTORY.get(), id);
        if (npc == null) throw new IllegalArgumentException("Missing hired warrior entity");
        this.npc = npc;
        this.hired = new GOTHiredInventory(npc, GOTHiredInventoryData.WARRIOR_SIZE);

        addSlot(new GOTSlotHiredReplaceItem(npc, hired, 0, 26, 26, GOTHiredSlotRules::isWeapon));
        addSlot(new GOTSlotHiredReplaceItem(npc, hired, 1, 62, 18,
            stack -> GOTHiredSlotRules.isArmor(stack, EquipmentSlot.HEAD)));
        addSlot(new GOTSlotHiredReplaceItem(npc, hired, 2, 62, 36,
            stack -> GOTHiredSlotRules.isArmor(stack, EquipmentSlot.CHEST)));
        addSlot(new GOTSlotHiredReplaceItem(npc, hired, 3, 62, 54,
            stack -> GOTHiredSlotRules.isArmor(stack, EquipmentSlot.LEGS)));
        addSlot(new GOTSlotHiredReplaceItem(npc, hired, 4, 62, 72,
            stack -> GOTHiredSlotRules.isArmor(stack, EquipmentSlot.FEET)));

        int slot = 5;
        for (int row = 0; row < 2; row++) {
            for (int col = 0; col < 6 && slot < hired.getContainerSize(); col++) {
                addSlot(new Slot(hired, slot++, 98 + col * 18, 18 + row * 18));
            }
        }
        while (slot < hired.getContainerSize()) {
            addSlot(new Slot(hired, slot, 98 + ((slot - 5) % 6) * 18,
                54 + ((slot - 5) / 6) * 18));
            slot++;
        }

        addPlayerInventory(playerInv, 8, 108);
    }

    private void addPlayerInventory(Inventory inv, int left, int top) {
        for (int row=0; row<3; row++)
            for (int col=0; col<9; col++)
                addSlot(new Slot(inv, col + row*9 + 9, left + col*18, top + row*18));
        for (int col=0; col<9; col++)
            addSlot(new Slot(inv, col, left + col*18, top + 58));
    }

    @Override
    public boolean stillValid(Player player) {
        return npc.isAlive()
            && GOTHiredData.isOwner(npc, player.getUUID())
            && hired.stillValid(player);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack empty = ItemStack.EMPTY;
        Slot slot = slots.get(index);
        if (!slot.hasItem()) return empty;

        ItemStack stack = slot.getItem();
        ItemStack copy = stack.copy();
        int hiredSlots = hired.getContainerSize();

        if (index < hiredSlots) {
            if (!moveItemStackTo(stack, hiredSlots, slots.size(), true)) return ItemStack.EMPTY;
        } else {
            // Try weapon, then armor, then general hired inventory.
            boolean moved = false;
            if (GOTHiredSlotRules.isWeapon(stack))
                moved = moveItemStackTo(stack, 0, 1, false);

            if (!moved) {
                EquipmentSlot[] armor = {EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET};
                for (int i=0; i<armor.length && !moved; i++) {
                    if (GOTHiredSlotRules.isArmor(stack, armor[i]))
                        moved = moveItemStackTo(stack, 1+i, 2+i, false);
                }
            }

            if (!moved)
                moved = moveItemStackTo(stack, 5, hiredSlots, false);

            if (!moved) return ItemStack.EMPTY;
        }

        if (stack.isEmpty()) slot.set(ItemStack.EMPTY);
        else slot.setChanged();

        return copy;
    }
}
