package got;

import got.achievement.GOTAchievementHooks;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public final class MillstoneMenu extends AbstractContainerMenu {
    private final Container container;private final ContainerData data;
    public MillstoneMenu(int id,Inventory inv,FriendlyByteBuf buf){this(id,inv,get(inv,buf),new SimpleContainerData(2));}
    private static Container get(Inventory inv,FriendlyByteBuf buf){var be=inv.player.level().getBlockEntity(buf.readBlockPos());return be instanceof MillstoneBlockEntity m?m:new SimpleContainer(2);}
    public MillstoneMenu(int id,Inventory inv,Container c,ContainerData d){super(GOTMenus.MILLSTONE.get(),id);container=c;data=d;checkContainerSize(c,2);checkContainerDataCount(d,2);c.startOpen(inv.player);
        addSlot(new Slot(c,0,52,35){@Override public boolean mayPlace(ItemStack s){return GOTMachineRecipes.canMill(s);}});addSlot(new Result(inv.player,c,1,112,35));
        for(int r=0;r<3;r++)for(int col=0;col<9;col++)addSlot(new Slot(inv,col+r*9+9,8+col*18,84+r*18));for(int col=0;col<9;col++)addSlot(new Slot(inv,col,8+col*18,142));addDataSlots(d);
    }
    public int progressPixels(){int total=data.get(1);return total<=0?0:data.get(0)*24/total;}
    @Override public boolean stillValid(Player p){return container.stillValid(p);}
    @Override public ItemStack quickMoveStack(Player p,int index){Slot slot=slots.get(index);if(!slot.hasItem())return ItemStack.EMPTY;ItemStack s=slot.getItem(),copy=s.copy();if(index==1){if(!moveItemStackTo(s,2,38,true))return ItemStack.EMPTY;slot.onQuickCraft(s,copy);}else if(index==0){if(!moveItemStackTo(s,2,38,false))return ItemStack.EMPTY;}else if(GOTMachineRecipes.canMill(s)){if(!moveItemStackTo(s,0,1,false))return ItemStack.EMPTY;}else if(index<29){if(!moveItemStackTo(s,29,38,false))return ItemStack.EMPTY;}else if(!moveItemStackTo(s,2,29,false))return ItemStack.EMPTY;if(s.isEmpty())slot.set(ItemStack.EMPTY);else slot.setChanged();if(s.getCount()==copy.getCount())return ItemStack.EMPTY;slot.onTake(p,s);return copy;}
    @Override public void removed(Player p){super.removed(p);container.stopOpen(p);}
    private static final class Result extends Slot{Result(Player p,Container c,int i,int x,int y){super(c,i,x,y);}@Override public boolean mayPlace(ItemStack s){return false;}@Override public void onTake(Player p,ItemStack s){super.onTake(p,s);if(p instanceof ServerPlayer sp)GOTAchievementHooks.award(sp,"USE_MILLSTONE");}}
}
