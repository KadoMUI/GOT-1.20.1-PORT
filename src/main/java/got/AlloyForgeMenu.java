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
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.common.ForgeHooks;

public final class AlloyForgeMenu extends AbstractContainerMenu {
    private final Container container; private final ContainerData data;
    public AlloyForgeMenu(int id,Inventory inv,FriendlyByteBuf buf){this(id,inv,get(inv,buf),new SimpleContainerData(4));}
    private static Container get(Inventory inv,FriendlyByteBuf buf){var be=inv.player.level().getBlockEntity(buf.readBlockPos());return be instanceof AlloyForgeBlockEntity f?f:new SimpleContainer(AlloyForgeBlockEntity.SLOT_COUNT);}
    public AlloyForgeMenu(int id,Inventory inv,Container c,ContainerData d){super(GOTMenus.ALLOY_FORGE.get(),id);container=c;data=d;checkContainerSize(c,13);checkContainerDataCount(d,4);c.startOpen(inv.player);
        for(int i=0;i<4;i++) addSlot(new Slot(c,i,53+i*18,21));
        for(int i=0;i<4;i++) addSlot(new Slot(c,4+i,53+i*18,39));
        for(int i=0;i<4;i++) addSlot(new Result(inv.player,c,8+i,53+i*18,85));
        addSlot(new Slot(c,12,80,125){@Override public boolean mayPlace(ItemStack s){return ForgeHooks.getBurnTime(s, RecipeType.SMELTING)>0;}});
        for(int r=0;r<3;r++)for(int col=0;col<9;col++)addSlot(new Slot(inv,col+r*9+9,8+col*18,151+r*18));
        for(int col=0;col<9;col++)addSlot(new Slot(inv,col,8+col*18,209)); addDataSlots(d);
    }
    public boolean isLit(){return data.get(0)>0;} public int burnPixels(){int total=data.get(1);if(total<=0)total=200;return data.get(0)*13/total;} public int progressPixels(){int total=data.get(3);return total<=0?0:data.get(2)*24/total;}
    @Override public boolean stillValid(Player p){return container.stillValid(p);}
    @Override public ItemStack quickMoveStack(Player p,int index){Slot slot=slots.get(index);if(!slot.hasItem())return ItemStack.EMPTY;ItemStack stack=slot.getItem(),copy=stack.copy();int ps=13,pe=49;
        if(index>=8&&index<12){if(!moveItemStackTo(stack,ps,pe,true))return ItemStack.EMPTY;slot.onQuickCraft(stack,copy);}else if(index<13){if(!moveItemStackTo(stack,ps,pe,false))return ItemStack.EMPTY;}else if(ForgeHooks.getBurnTime(stack,RecipeType.SMELTING)>0){if(!moveItemStackTo(stack,12,13,false))return ItemStack.EMPTY;}else if(!moveItemStackTo(stack,0,8,false))return ItemStack.EMPTY;
        if(stack.isEmpty())slot.set(ItemStack.EMPTY);else slot.setChanged();if(stack.getCount()==copy.getCount())return ItemStack.EMPTY;slot.onTake(p,stack);return copy;}
    @Override public void removed(Player p){super.removed(p);container.stopOpen(p);}
    private static final class Result extends Slot { private final Player player; Result(Player p,Container c,int slot,int x,int y){super(c,slot,x,y);player=p;}@Override public boolean mayPlace(ItemStack s){return false;}@Override public void onTake(Player p,ItemStack s){super.onTake(p,s);if(p instanceof ServerPlayer sp)GOTAchievementHooks.award(sp,"USE_ALLOY_FORGE");}}
}
