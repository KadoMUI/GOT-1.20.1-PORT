package got;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class PlacedDrinkVesselBlockEntity extends BlockEntity {
    private ItemStack drink = ItemStack.EMPTY;
    private GOTDrinkVessel vessel = GOTDrinkVessel.MUG;

    public PlacedDrinkVesselBlockEntity(BlockPos pos, BlockState state) {
        super(GOTBlockEntities.PLACED_DRINK_VESSEL.get(), pos, state);
    }

    public ItemStack getDrink() { return drink; }
    public GOTDrinkVessel getVessel() { return vessel; }
    public boolean isFilled() { return !drink.isEmpty(); }

    public void setDrink(ItemStack stack) {
        this.drink = stack.copy();
        this.drink.setCount(1);
        this.vessel = GOTDrinkItem.getVessel(stack);
        sync();
    }

    /** Used only during block-entity construction; does not attempt network sync. */
    public void setInitialVessel(GOTDrinkVessel vessel) {
        this.vessel = vessel;
    }

    public void setEmptyVessel(GOTDrinkVessel vessel) {
        this.drink = ItemStack.EMPTY;
        this.vessel = vessel;
        sync();
    }

    /** Removes only the liquid; the physical vessel remains placed. */
    public ItemStack removeDrink() {
        ItemStack result = drink;
        drink = ItemStack.EMPTY;
        sync();
        return result;
    }

    private void sync() {
        setChanged();
        if (level != null) level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putString("Vessel", vessel.serializedName());
        if (!drink.isEmpty()) tag.put("Drink", drink.save(new CompoundTag()));
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        drink = tag.contains("Drink") ? ItemStack.of(tag.getCompound("Drink")) : ItemStack.EMPTY;
        vessel = tag.contains("Vessel")
                ? GOTDrinkVessel.byName(tag.getString("Vessel"))
                : (!drink.isEmpty() ? GOTDrinkItem.getVessel(drink) : GOTDrinkVessel.MUG);
    }

    @Override public CompoundTag getUpdateTag() { return saveWithoutMetadata(); }
    @Override public ClientboundBlockEntityDataPacket getUpdatePacket() { return ClientboundBlockEntityDataPacket.create(this); }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        CompoundTag tag = pkt.getTag();
        if (tag != null) load(tag);
    }
}
