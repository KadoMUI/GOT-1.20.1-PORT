package got;

import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/** Legacy small/medium/large GOT pouch: 9/18/27 persistent slots stored on the item stack. */
public final class GOTPouchItem extends Item implements net.minecraft.world.item.DyeableLeatherItem {
    public static final String DATA = "GOTPouchData";
    private final int capacity;
    public GOTPouchItem(int capacity, Properties p) { super(p.stacksTo(1)); this.capacity = capacity; }
    public int capacity() { return capacity; }

    public static SimpleContainer load(ItemStack pouch, int size) {
        SimpleContainer inv = new SimpleContainer(size);
        CompoundTag root = pouch.getTag();
        if (root == null || !root.contains(DATA, Tag.TAG_COMPOUND)) return inv;
        ListTag items = root.getCompound(DATA).getList("Items", Tag.TAG_COMPOUND);
        for (int i=0;i<items.size();i++) {
            CompoundTag t=items.getCompound(i); int slot=t.getByte("Slot") & 255;
            if (slot < size) inv.setItem(slot, ItemStack.of(t));
        }
        return inv;
    }
    public static void save(ItemStack pouch, SimpleContainer inv) {
        CompoundTag data=new CompoundTag(); ListTag list=new ListTag();
        for(int i=0;i<inv.getContainerSize();i++) if(!inv.getItem(i).isEmpty()) {
            CompoundTag t=new CompoundTag(); t.putByte("Slot",(byte)i); inv.getItem(i).save(t); list.add(t);
        }
        data.put("Items",list); pouch.getOrCreateTag().put(DATA,data);
    }
    @Override public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack=player.getItemInHand(hand);
        if(!level.isClientSide && player instanceof ServerPlayer sp) {
            int slot = hand == InteractionHand.MAIN_HAND ? sp.getInventory().selected : 40;
            NetworkHooks.openScreen(sp, new GOTPouchMenu.Provider(slot, capacity), buf->{buf.writeVarInt(slot);buf.writeVarInt(capacity);});
        }
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
    }
    @Override public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> out, TooltipFlag flag) {
        SimpleContainer inv=load(stack,capacity); int used=0; for(int i=0;i<capacity;i++) if(!inv.getItem(i).isEmpty()) used++;
        out.add(Component.translatable("item.got.pouch.slots", used, capacity).withStyle(ChatFormatting.GRAY));
    }
}
