package got;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.SidedInvWrapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public final class OvenBlockEntity extends BlockEntity implements MenuProvider, WorldlyContainer {
    public static final int SLOT_COUNT = 19;
    public static final int FUEL_SLOT = 18;
    public static final int COOK_TIME_TOTAL = 200;

    private NonNullList<ItemStack> items = NonNullList.withSize(SLOT_COUNT, ItemStack.EMPTY);
    private int burnTime;
    private int burnDuration;
    private int cookTime;

    private LazyOptional<IItemHandler> topHandler = LazyOptional.empty();
    private LazyOptional<IItemHandler> sideHandler = LazyOptional.empty();
    private LazyOptional<IItemHandler> bottomHandler = LazyOptional.empty();

    protected final ContainerData data = new ContainerData() {
        @Override
        public int get(int index) {
            return switch (index) {
                case 0 -> burnTime;
                case 1 -> burnDuration;
                case 2 -> cookTime;
                case 3 -> COOK_TIME_TOTAL;
                default -> 0;
            };
        }

        @Override
        public void set(int index, int value) {
            switch (index) {
                case 0 -> burnTime = value;
                case 1 -> burnDuration = value;
                case 2 -> cookTime = value;
                default -> { }
            }
        }

        @Override
        public int getCount() {
            return 4;
        }
    };

    public OvenBlockEntity(BlockPos pos, BlockState state) {
        super(GOTBlockEntities.OVEN.get(), pos, state);
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, OvenBlockEntity oven) {
        boolean wasLit = oven.isLit();
        boolean changed = false;

        if (oven.burnTime > 0) {
            oven.burnTime--;
        }

        boolean canCook = oven.canCookAny(level);
        if (!oven.isLit() && canCook) {
            ItemStack fuel = oven.items.get(FUEL_SLOT);
            int fuelValue = ForgeHooks.getBurnTime(fuel, RecipeType.SMELTING);
            if (fuelValue > 0) {
                oven.burnTime = fuelValue;
                oven.burnDuration = fuelValue;
                oven.consumeFuel(fuel);
                changed = true;
            }
        }

        if (oven.isLit() && canCook) {
            oven.cookTime++;
            if (oven.cookTime >= COOK_TIME_TOTAL) {
                oven.cookTime = 0;
                oven.cookAll(level);
                changed = true;
            }
        } else if (oven.cookTime != 0) {
            oven.cookTime = 0;
            changed = true;
        }

        if (wasLit != oven.isLit()) {
            level.setBlock(pos, state.setValue(OvenBlock.LIT, oven.isLit()), 3);
            changed = true;
        }

        if (changed) {
            setChanged(level, pos, state);
        }
    }

    private void consumeFuel(ItemStack fuel) {
        if (fuel.isEmpty()) return;
        ItemStack remainder = fuel.getCraftingRemainingItem();
        fuel.shrink(1);
        if (fuel.isEmpty()) {
            items.set(FUEL_SLOT, remainder);
        }
    }

    private boolean isLit() {
        return burnTime > 0;
    }

    private boolean canCookAny(Level level) {
        for (int input = 0; input < 9; input++) {
            if (canCookSlot(level, input)) return true;
        }
        return false;
    }

    private boolean canCookSlot(Level level, int inputSlot) {
        ItemStack input = items.get(inputSlot);
        if (input.isEmpty()) return false;
        Optional<ItemStack> result = getFoodResult(level, input);
        if (result.isEmpty()) return false;

        ItemStack output = items.get(inputSlot + 9);
        ItemStack resultStack = result.get();
        if (output.isEmpty()) return true;
        if (!ItemStack.isSameItemSameTags(output, resultStack)) return false;
        return output.getCount() + resultStack.getCount() <= output.getMaxStackSize();
    }

    private void cookAll(Level level) {
        for (int inputSlot = 0; inputSlot < 9; inputSlot++) {
            if (!canCookSlot(level, inputSlot)) continue;
            ItemStack input = items.get(inputSlot);
            ItemStack result = getFoodResult(level, input).orElse(ItemStack.EMPTY);
            if (result.isEmpty()) continue;

            ItemStack output = items.get(inputSlot + 9);
            if (output.isEmpty()) {
                items.set(inputSlot + 9, result.copy());
            } else {
                output.grow(result.getCount());
            }
            input.shrink(1);
        }
    }

    public static boolean isCookableFood(Level level, ItemStack stack) {
        return getFoodResult(level, stack).isPresent();
    }

    private static Optional<ItemStack> getFoodResult(Level level, ItemStack stack) {
        if (level == null || stack.isEmpty()) return Optional.empty();
        SimpleContainer single = new SimpleContainer(stack.copyWithCount(1));

        var smoking = level.getRecipeManager().getRecipeFor(RecipeType.SMOKING, single, level);
        if (smoking.isPresent()) {
            ItemStack result = smoking.get().assemble(single, level.registryAccess());
            if (!result.isEmpty() && result.isEdible()) return Optional.of(result);
        }

        var smelting = level.getRecipeManager().getRecipeFor(RecipeType.SMELTING, single, level);
        if (smelting.isPresent()) {
            ItemStack result = smelting.get().assemble(single, level.registryAccess());
            if (!result.isEmpty() && result.isEdible()) return Optional.of(result);
        }

        return Optional.empty();
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("container.got.oven");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory inventory, Player player) {
        return new OvenMenu(containerId, inventory, this, data);
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        ContainerHelper.saveAllItems(tag, items);
        tag.putInt("BurnTime", burnTime);
        tag.putInt("BurnDuration", burnDuration);
        tag.putInt("CookTime", cookTime);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        items = NonNullList.withSize(SLOT_COUNT, ItemStack.EMPTY);
        ContainerHelper.loadAllItems(tag, items);
        burnTime = tag.getInt("BurnTime");
        burnDuration = tag.getInt("BurnDuration");
        cookTime = tag.getInt("CookTime");
    }

    @Override
    public int getContainerSize() {
        return SLOT_COUNT;
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
        ItemStack result = ContainerHelper.removeItem(items, slot, amount);
        if (!result.isEmpty()) setChanged();
        return result;
    }

    @Override
    public ItemStack removeItemNoUpdate(int slot) {
        return ContainerHelper.takeItem(items, slot);
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        items.set(slot, stack);
        if (stack.getCount() > getMaxStackSize()) stack.setCount(getMaxStackSize());
        setChanged();
    }

    @Override
    public boolean stillValid(Player player) {
        if (level == null || level.getBlockEntity(worldPosition) != this) return false;
        return player.distanceToSqr(worldPosition.getX() + 0.5, worldPosition.getY() + 0.5, worldPosition.getZ() + 0.5) <= 64.0;
    }

    @Override
    public boolean canPlaceItem(int slot, ItemStack stack) {
        if (slot >= 9 && slot < 18) return false;
        if (slot == FUEL_SLOT) return ForgeHooks.getBurnTime(stack, RecipeType.SMELTING) > 0;
        return level != null && isCookableFood(level, stack);
    }

    @Override
    public void clearContent() {
        items.clear();
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        topHandler.invalidate();
        sideHandler.invalidate();
        bottomHandler.invalidate();
    }

    @Override
    public void reviveCaps() {
        super.reviveCaps();
        topHandler = LazyOptional.of(() -> new SidedInvWrapper(this, Direction.UP));
        sideHandler = LazyOptional.of(() -> new SidedInvWrapper(this, Direction.NORTH));
        bottomHandler = LazyOptional.of(() -> new SidedInvWrapper(this, Direction.DOWN));
    }

    @Override
    public <T> @NotNull LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (!isRemoved() && cap == ForgeCapabilities.ITEM_HANDLER) {
            if (side == Direction.UP) return topHandler.cast();
            if (side == Direction.DOWN) return bottomHandler.cast();
            return sideHandler.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public int[] getSlotsForFace(Direction side) {
        if (side == Direction.UP) return new int[]{0,1,2,3,4,5,6,7,8};
        if (side == Direction.DOWN) return new int[]{9,10,11,12,13,14,15,16,17,18};
        return new int[]{18};
    }

    @Override
    public boolean canPlaceItemThroughFace(int slot, ItemStack stack, @Nullable Direction direction) {
        return canPlaceItem(slot, stack);
    }

    @Override
    public boolean canTakeItemThroughFace(int slot, ItemStack stack, Direction direction) {
        return slot >= 9 && slot < 18;
    }
}
