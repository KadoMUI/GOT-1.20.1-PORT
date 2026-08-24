package got;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.ForgeHooks;

public final class OvenMenu extends AbstractContainerMenu {
    public static final int INPUT_START = 0;
    public static final int INPUT_COUNT = 9;
    public static final int OUTPUT_START = 9;
    public static final int OUTPUT_COUNT = 9;
    public static final int FUEL_SLOT = 18;
    private static final int OVEN_SLOT_COUNT = 19;

    private final Container container;
    private final ContainerData data;

    public OvenMenu(int containerId, Inventory inventory, FriendlyByteBuf buffer) {
        this(containerId, inventory, getContainer(inventory, buffer), new SimpleContainerData(4));
    }

    private static Container getContainer(Inventory inventory, FriendlyByteBuf buffer) {
        var blockEntity = inventory.player.level().getBlockEntity(buffer.readBlockPos());
        return blockEntity instanceof OvenBlockEntity oven ? oven : new SimpleContainer(OVEN_SLOT_COUNT);
    }

    public OvenMenu(int containerId, Inventory inventory, Container container, ContainerData data) {
        super(GOTMenus.OVEN.get(), containerId);
        checkContainerSize(container, OVEN_SLOT_COUNT);
        checkContainerDataCount(data, 4);
        this.container = container;
        this.data = data;
        container.startOpen(inventory.player);

        for (int i = 0; i < 9; i++) {
            addSlot(new Slot(container, INPUT_START + i, 8 + i * 18, 21));
        }
        for (int i = 0; i < 9; i++) {
            addSlot(new OvenResultSlot(inventory.player, container, OUTPUT_START + i, 8 + i * 18, 67));
        }
        addSlot(new Slot(container, FUEL_SLOT, 80, 111) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return ForgeHooks.getBurnTime(stack, null) > 0;
            }
        });

        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                addSlot(new Slot(inventory, col + row * 9 + 9, 8 + col * 18, 133 + row * 18));
            }
        }
        for (int col = 0; col < 9; col++) {
            addSlot(new Slot(inventory, col, 8 + col * 18, 191));
        }

        addDataSlots(data);
    }

    @Override
    public boolean stillValid(Player player) {
        return container.stillValid(player);
    }

    public boolean isLit() {
        return data.get(0) > 0;
    }

    public int getBurnProgress() {
        int duration = data.get(1);
        if (duration <= 0) duration = 200;
        return data.get(0) * 13 / duration;
    }

    public int getCookProgress() {
        int total = data.get(3);
        return total <= 0 ? 0 : data.get(2) * 24 / total;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        Slot slot = slots.get(index);
        if (!slot.hasItem()) return ItemStack.EMPTY;

        ItemStack stack = slot.getItem();
        ItemStack copy = stack.copy();
        int playerStart = OVEN_SLOT_COUNT;
        int playerEnd = playerStart + 36;

        if (index >= OUTPUT_START && index < OUTPUT_START + OUTPUT_COUNT) {
            if (!moveItemStackTo(stack, playerStart, playerEnd, true)) return ItemStack.EMPTY;
            slot.onQuickCraft(stack, copy);
        } else if (index < OVEN_SLOT_COUNT) {
            if (!moveItemStackTo(stack, playerStart, playerEnd, false)) return ItemStack.EMPTY;
        } else if (ForgeHooks.getBurnTime(stack, null) > 0) {
            if (!moveItemStackTo(stack, FUEL_SLOT, FUEL_SLOT + 1, false)) return ItemStack.EMPTY;
        } else if (OvenBlockEntity.isCookableFood(player.level(), stack)) {
            if (!moveItemStackTo(stack, INPUT_START, INPUT_START + INPUT_COUNT, false)) return ItemStack.EMPTY;
        } else if (index < playerStart + 27) {
            if (!moveItemStackTo(stack, playerStart + 27, playerEnd, false)) return ItemStack.EMPTY;
        } else if (!moveItemStackTo(stack, playerStart, playerStart + 27, false)) {
            return ItemStack.EMPTY;
        }

        if (stack.isEmpty()) slot.set(ItemStack.EMPTY); else slot.setChanged();
        if (stack.getCount() == copy.getCount()) return ItemStack.EMPTY;
        slot.onTake(player, stack);
        return copy;
    }

    @Override
    public void removed(Player player) {
        super.removed(player);
        container.stopOpen(player);
    }
}
