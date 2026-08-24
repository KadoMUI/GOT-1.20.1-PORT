package got.menu.hiring;

import got.npc.hiring.GOTHiredData;
import got.npc.hiring.inventory.GOTHiredInventory;
import got.npc.hiring.inventory.GOTHiredInventoryData;
import net.minecraft.world.entity.Entity;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;


/**
 * Hired farmer work inventory.
 */
public final class GOTContainerHiredFarmerInventory extends AbstractContainerMenu {
    private final Entity npc;
    private final GOTHiredInventory hired;

    public GOTContainerHiredFarmerInventory(int id, Inventory playerInv, FriendlyByteBuf buffer) {
        this(id, playerInv, playerInv.player.level().getEntity(buffer.readVarInt()));
    }

    public GOTContainerHiredFarmerInventory(int id, Inventory playerInv, Entity npc) {
        super(got.GOTMenus.HIRED_FARMER_INVENTORY.get(), id);
        if (npc == null) throw new IllegalArgumentException("Missing hired farmer entity");
        this.npc = npc;
        this.hired = new GOTHiredInventory(npc, GOTHiredInventoryData.FARMER_SIZE);

        int slot = 0;
        for (int row=0; row<2; row++) {
            for (int col=0; col<9; col++) {
                addSlot(new Slot(hired, slot++, 8 + col*18, 18 + row*18) {
                    @Override
                    public boolean mayPlace(ItemStack stack) {
                        return GOTHiredSlotRules.isFarmerWorkItem(stack);
                    }
                });
            }
        }

        addPlayerInventory(playerInv, 8, 72);
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
        Slot slot = slots.get(index);
        if (!slot.hasItem()) return ItemStack.EMPTY;

        ItemStack stack = slot.getItem();
        ItemStack copy = stack.copy();
        int hiredSlots = hired.getContainerSize();

        if (index < hiredSlots) {
            if (!moveItemStackTo(stack, hiredSlots, slots.size(), true)) return ItemStack.EMPTY;
        } else {
            if (!GOTHiredSlotRules.isFarmerWorkItem(stack)) return ItemStack.EMPTY;
            if (!moveItemStackTo(stack, 0, hiredSlots, false)) return ItemStack.EMPTY;
        }

        if (stack.isEmpty()) slot.set(ItemStack.EMPTY);
        else slot.setChanged();

        return copy;
    }
}
