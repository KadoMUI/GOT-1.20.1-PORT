package got;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public final class GOTPouchMenu extends AbstractContainerMenu {
    private final int playerSlot; private final int size; private final SimpleContainer pouch;
    public GOTPouchMenu(int id, Inventory player, FriendlyByteBuf buf){this(id,player,buf.readVarInt(),buf.readVarInt());}
    public GOTPouchMenu(int id, Inventory player, int playerSlot, int size){
        super(GOTMenus.POUCH.get(),id);this.playerSlot=playerSlot;this.size=size;
        ItemStack stack=getStack(player,playerSlot); this.pouch=GOTPouchItem.load(stack,size);
        int rows=(size+8)/9;
        for(int i=0;i<size;i++){int r=i/9,c=i%9;addSlot(new Slot(pouch,i,8+c*18,18+r*18){@Override public boolean mayPlace(ItemStack s){return !(s.getItem() instanceof GOTPouchItem);}});}
        int y=31+rows*18;
        for(int r=0;r<3;r++)for(int c=0;c<9;c++)addSlot(new Slot(player,c+r*9+9,8+c*18,y+r*18));
        for(int c=0;c<9;c++)addSlot(new Slot(player,c,8+c*18,y+58));
    }
    private static ItemStack getStack(Inventory inv,int slot){return slot==40?inv.player.getOffhandItem():inv.getItem(slot);}
    @Override public boolean stillValid(Player p){ItemStack s=getStack(p.getInventory(),playerSlot);return s.getItem() instanceof GOTPouchItem;}
    @Override public void removed(Player p){super.removed(p);ItemStack s=getStack(p.getInventory(),playerSlot);if(s.getItem() instanceof GOTPouchItem)GOTPouchItem.save(s,pouch);}
    @Override public ItemStack quickMoveStack(Player p,int index){ItemStack empty=ItemStack.EMPTY;Slot slot=slots.get(index);if(!slot.hasItem())return empty;ItemStack src=slot.getItem(),copy=src.copy();if(index<size){if(!moveItemStackTo(src,size,slots.size(),true))return empty;}else{if(src.getItem() instanceof GOTPouchItem||!moveItemStackTo(src,0,size,false))return empty;}if(src.isEmpty())slot.set(ItemStack.EMPTY);else slot.setChanged();return copy;}
    public int rows(){return (size+8)/9;}
    public static final class Provider implements MenuProvider{private final int slot,size;public Provider(int s,int z){slot=s;size=z;}public Component getDisplayName(){return Component.translatable("container.got.pouch");}public AbstractContainerMenu createMenu(int id,Inventory inv,Player p){return new GOTPouchMenu(id,inv,slot,size);}}
}
