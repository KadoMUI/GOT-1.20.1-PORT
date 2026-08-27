package got;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.WorldlyContainer;
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
import org.jetbrains.annotations.Nullable;

public final class AlloyForgeBlockEntity extends BlockEntity implements MenuProvider, WorldlyContainer {
    public static final int SLOT_COUNT=13, FUEL_SLOT=12, COOK_TOTAL=200;
    private static final int[] TOP={4,5,6,7};
    private static final int[] BOTTOM={8,9,10,11,12};
    private static final int[] SIDES={12};
    private NonNullList<ItemStack> items=NonNullList.withSize(SLOT_COUNT,ItemStack.EMPTY);
    private int burnTime,burnDuration,cookTime;
    final ContainerData data=new ContainerData(){
        @Override public int get(int i){return switch(i){case 0->burnTime;case 1->burnDuration;case 2->cookTime;case 3->COOK_TOTAL;default->0;};}
        @Override public void set(int i,int v){switch(i){case 0->burnTime=v;case 1->burnDuration=v;case 2->cookTime=v;}}
        @Override public int getCount(){return 4;}
    };
    public AlloyForgeBlockEntity(BlockPos pos,BlockState state){super(GOTBlockEntities.ALLOY_FORGE.get(),pos,state);}

    public static void serverTick(Level level,BlockPos pos,BlockState state,AlloyForgeBlockEntity forge){
        boolean wasLit=forge.burnTime>0,changed=false;
        if(forge.burnTime>0)forge.burnTime--;
        boolean can=forge.canProcess(level);
        if(forge.burnTime<=0&&can){ItemStack fuel=forge.items.get(FUEL_SLOT);int val=ForgeHooks.getBurnTime(fuel, RecipeType.SMELTING);if(val>0){forge.burnTime=forge.burnDuration=val;forge.consumeFuel();changed=true;}}
        if(forge.burnTime>0&&can){forge.cookTime++;if(forge.cookTime>=COOK_TOTAL){forge.cookTime=0;forge.processAll(level);changed=true;}}
        else if(forge.cookTime!=0){forge.cookTime=0;changed=true;}
        boolean lit=forge.burnTime>0;
        if(wasLit!=lit&&state.hasProperty(AlloyForgeBlock.LIT)){level.setBlock(pos,state.setValue(AlloyForgeBlock.LIT,lit),3);changed=true;}
        if(changed)setChanged(level,pos,state);
    }
    private void consumeFuel(){ItemStack fuel=items.get(FUEL_SLOT);if(fuel.isEmpty())return;ItemStack rem=fuel.getCraftingRemainingItem();fuel.shrink(1);if(fuel.isEmpty())items.set(FUEL_SLOT,rem);}
    private boolean canProcess(Level level){for(int lane=0;lane<4;lane++)if(resultFor(level,lane).isEmpty()==false&&canAccept(lane,resultFor(level,lane)))return true;return false;}
    private ItemStack resultFor(Level level,int lane){
        ItemStack catalyst=items.get(lane),input=items.get(lane+4);if(input.isEmpty())return ItemStack.EMPTY;
        ItemStack alloy=GOTMachineRecipes.alloy(input,catalyst);if(!alloy.isEmpty())return alloy;
        return GOTMachineRecipes.smelt(level,input);
    }
    private boolean usesCatalyst(int lane){return !GOTMachineRecipes.alloy(items.get(lane+4),items.get(lane)).isEmpty();}
    private boolean canAccept(int lane,ItemStack result){if(result.isEmpty())return false;ItemStack out=items.get(lane+8);return out.isEmpty()||(ItemStack.isSameItemSameTags(out,result)&&out.getCount()+result.getCount()<=out.getMaxStackSize());}
    private void processAll(Level level){for(int lane=0;lane<4;lane++){ItemStack r=resultFor(level,lane);if(!canAccept(lane,r))continue;boolean catalyst=usesCatalyst(lane);ItemStack out=items.get(lane+8);if(out.isEmpty())items.set(lane+8,r.copy());else out.grow(r.getCount());items.get(lane+4).shrink(1);if(items.get(lane+4).isEmpty())items.set(lane+4,ItemStack.EMPTY);if(catalyst){items.get(lane).shrink(1);if(items.get(lane).isEmpty())items.set(lane,ItemStack.EMPTY);}}}

    @Override public Component getDisplayName(){return Component.translatable("got.container.alloyForge");}
    @Nullable @Override public AbstractContainerMenu createMenu(int id,Inventory inv,Player player){return new AlloyForgeMenu(id,inv,this,data);}
    @Override protected void saveAdditional(CompoundTag tag){super.saveAdditional(tag);ContainerHelper.saveAllItems(tag,items);tag.putInt("BurnTime",burnTime);tag.putInt("BurnDuration",burnDuration);tag.putInt("CookTime",cookTime);}
    @Override public void load(CompoundTag tag){super.load(tag);items=NonNullList.withSize(SLOT_COUNT,ItemStack.EMPTY);ContainerHelper.loadAllItems(tag,items);burnTime=tag.getInt("BurnTime");burnDuration=tag.getInt("BurnDuration");cookTime=tag.getInt("CookTime");}
    @Override public int getContainerSize(){return SLOT_COUNT;} @Override public boolean isEmpty(){for(ItemStack s:items)if(!s.isEmpty())return false;return true;}
    @Override public ItemStack getItem(int i){return items.get(i);} @Override public ItemStack removeItem(int i,int n){ItemStack r=ContainerHelper.removeItem(items,i,n);if(!r.isEmpty())setChanged();return r;} @Override public ItemStack removeItemNoUpdate(int i){return ContainerHelper.takeItem(items,i);}
    @Override public void setItem(int i,ItemStack s){items.set(i,s);if(s.getCount()>getMaxStackSize())s.setCount(getMaxStackSize());setChanged();}
    @Override public boolean stillValid(Player p){return level!=null&&level.getBlockEntity(worldPosition)==this&&p.distanceToSqr(worldPosition.getX()+.5,worldPosition.getY()+.5,worldPosition.getZ()+.5)<=64;}
    @Override public void clearContent(){items.clear();}
    @Override public int[] getSlotsForFace(Direction d){return d==Direction.UP?TOP:d==Direction.DOWN?BOTTOM:SIDES;}
    @Override public boolean canPlaceItemThroughFace(int slot,ItemStack stack,@Nullable Direction d){if(slot==FUEL_SLOT)return ForgeHooks.getBurnTime(stack,RecipeType.SMELTING)>0;return slot>=0&&slot<8;}
    @Override public boolean canTakeItemThroughFace(int slot,ItemStack stack,Direction d){return slot>=8&&slot<12;}
}
