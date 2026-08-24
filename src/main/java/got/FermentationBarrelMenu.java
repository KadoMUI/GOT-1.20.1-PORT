package got;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.*;

public final class FermentationBarrelMenu extends AbstractContainerMenu {
 private final Container container; private final ContainerData data;
 public FermentationBarrelMenu(int id, Inventory inv, FriendlyByteBuf buf){this(id,inv,get(inv,buf),new SimpleContainerData(4));}
 private static Container get(Inventory inv,FriendlyByteBuf b){var be=inv.player.level().getBlockEntity(b.readBlockPos());return be instanceof FermentationBarrelBlockEntity f?f:new SimpleContainer(10);}
 public FermentationBarrelMenu(int id,Inventory inv,Container c,ContainerData d){super(GOTMenus.FERMENTATION_BARREL.get(),id);container=c;data=d;checkContainerSize(c,10);checkContainerDataCount(d,4);c.startOpen(inv.player);
  for(int r=0;r<2;r++)for(int col=0;col<3;col++)addSlot(new Slot(c,col+r*3,17+col*18,31+r*18));
  for(int col=0;col<3;col++)addSlot(new Slot(c,6+col,17+col*18,75){public boolean mayPlace(ItemStack s){return s.is(Items.WATER_BUCKET);}});
  addSlot(new Slot(c,9,93,48){public boolean mayPlace(ItemStack s){return false;} public boolean mayPickup(Player p){return false;}});
  for(int r=0;r<3;r++)for(int col=0;col<9;col++)addSlot(new Slot(inv,col+r*9+9,26+col*18,138+r*18)); for(int col=0;col<9;col++)addSlot(new Slot(inv,col,26+col*18,196)); addDataSlots(d);
 }
 public boolean stillValid(Player p){return container.stillValid(p);} public int mode(){return data.get(0);} public int progress(){return data.get(1)*68/FermentationBarrelBlockEntity.STAGE_TICKS;} public int strength(){return data.get(2);} public int servings(){return data.get(3);}
 @Override public boolean clickMenuButton(Player p,int id){if(id==0&&container instanceof FermentationBarrelBlockEntity b)return b.startOrStop();return false;}
 public ItemStack quickMoveStack(Player p,int idx){Slot s=slots.get(idx);if(!s.hasItem())return ItemStack.EMPTY;ItemStack st=s.getItem(),copy=st.copy();if(idx<10){if(!moveItemStackTo(st,10,46,true))return ItemStack.EMPTY;}else if(st.is(Items.WATER_BUCKET)){if(!moveItemStackTo(st,6,9,false))return ItemStack.EMPTY;}else if(!moveItemStackTo(st,0,6,false))return ItemStack.EMPTY;if(st.isEmpty())s.set(ItemStack.EMPTY);else s.setChanged();return copy;}
 @Override public void removed(Player p){super.removed(p);container.stopOpen(p);}
}
