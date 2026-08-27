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
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public final class MillstoneBlockEntity extends BlockEntity implements MenuProvider, WorldlyContainer {
    public static final int COOK_TOTAL=200;
    private NonNullList<ItemStack> items=NonNullList.withSize(2,ItemStack.EMPTY);
    private int cookTime;
    final ContainerData data=new ContainerData(){public int get(int i){return i==0?cookTime:i==1?COOK_TOTAL:0;}public void set(int i,int v){if(i==0)cookTime=v;}public int getCount(){return 2;}};
    public MillstoneBlockEntity(BlockPos pos,BlockState state){super(GOTBlockEntities.MILLSTONE.get(),pos,state);}
    public static void serverTick(Level level,BlockPos pos,BlockState state,MillstoneBlockEntity mill){
        boolean powered=state.hasProperty(MillstoneBlock.POWERED)&&state.getValue(MillstoneBlock.POWERED),changed=false;
        if(powered&&mill.canMill()){mill.cookTime++;if(mill.cookTime>=COOK_TOTAL){mill.cookTime=0;mill.mill(level);changed=true;}}
        else if(mill.cookTime!=0){mill.cookTime=0;changed=true;}
        if(changed)setChanged(level,pos,state);
    }
    private boolean canMill(){var r=GOTMachineRecipes.mill(items.get(0));if(r.isEmpty())return false;ItemStack result=r.get().stack(),out=items.get(1);return out.isEmpty()||(ItemStack.isSameItemSameTags(out,result)&&out.getCount()+result.getCount()<=out.getMaxStackSize());}
    private void mill(Level level){var opt=GOTMachineRecipes.mill(items.get(0));if(opt.isEmpty())return;var r=opt.get();if(level.random.nextFloat()<r.chance()){ItemStack out=items.get(1);if(out.isEmpty())items.set(1,r.stack().copy());else out.grow(r.stack().getCount());}items.get(0).shrink(1);if(items.get(0).isEmpty())items.set(0,ItemStack.EMPTY);}
    @Override public Component getDisplayName(){return Component.translatable("got.container.millstone");}
    @Nullable @Override public AbstractContainerMenu createMenu(int id,Inventory inv,Player p){return new MillstoneMenu(id,inv,this,data);}
    @Override protected void saveAdditional(CompoundTag tag){super.saveAdditional(tag);ContainerHelper.saveAllItems(tag,items);tag.putInt("MillTime",cookTime);}
    @Override public void load(CompoundTag tag){super.load(tag);items=NonNullList.withSize(2,ItemStack.EMPTY);ContainerHelper.loadAllItems(tag,items);cookTime=tag.getInt("MillTime");}
    @Override public int getContainerSize(){return 2;}@Override public boolean isEmpty(){return items.get(0).isEmpty()&&items.get(1).isEmpty();}@Override public ItemStack getItem(int i){return items.get(i);}@Override public ItemStack removeItem(int i,int n){ItemStack r=ContainerHelper.removeItem(items,i,n);if(!r.isEmpty())setChanged();return r;}@Override public ItemStack removeItemNoUpdate(int i){return ContainerHelper.takeItem(items,i);}@Override public void setItem(int i,ItemStack s){items.set(i,s);if(s.getCount()>getMaxStackSize())s.setCount(getMaxStackSize());setChanged();}@Override public boolean stillValid(Player p){return level!=null&&level.getBlockEntity(worldPosition)==this&&p.distanceToSqr(worldPosition.getX()+.5,worldPosition.getY()+.5,worldPosition.getZ()+.5)<=64;}@Override public void clearContent(){items.clear();}
    @Override public int[] getSlotsForFace(Direction d){return d==Direction.DOWN?new int[]{1}:new int[]{0};}@Override public boolean canPlaceItemThroughFace(int slot,ItemStack stack,@Nullable Direction d){return slot==0&&GOTMachineRecipes.canMill(stack);}@Override public boolean canTakeItemThroughFace(int slot,ItemStack stack,Direction d){return slot==1;}
}
