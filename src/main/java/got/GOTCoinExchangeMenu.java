package got;

import got.economy.GOTCoinValueService;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkHooks;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/** Modern reconstruction of the legacy GOTContainerCoinExchange. */
public final class GOTCoinExchangeMenu extends AbstractContainerMenu {
    private final SimpleContainer input = new SimpleContainer(1) {
        @Override public void setChanged() { super.setChanged(); slotsChanged(this); }
    };
    private final SimpleContainer results = new SimpleContainer(2) {
        @Override public void setChanged() { super.setChanged(); slotsChanged(this); }
    };
    private final DataSlot exchanged = DataSlot.standalone();
    private final int traderEntityId;
    private boolean internalChange;

    public GOTCoinExchangeMenu(int id, Inventory player, FriendlyByteBuf buf) {
        this(id, player, buf.readVarInt());
    }

    public GOTCoinExchangeMenu(int id, Inventory player, int traderEntityId) {
        super(GOTMenus.COIN_EXCHANGE.get(), id);
        this.traderEntityId = traderEntityId;
        addDataSlot(exchanged);

        addSlot(new Slot(input, 0, 80, 46) {
            @Override public boolean mayPlace(ItemStack stack) { return isCoin(stack); }
            @Override public int getMaxStackSize() { return 64; }
        });
        addSlot(new ResultSlot(results, 0, 26, 46));
        addSlot(new ResultSlot(results, 1, 134, 46));

        for (int row = 0; row < 3; row++)
            for (int col = 0; col < 9; col++)
                addSlot(new Slot(player, col + row * 9 + 9, 8 + col * 18, 106 + row * 18));
        for (int col = 0; col < 9; col++)
            addSlot(new Slot(player, col, 8 + col * 18, 164));
    }

    public static void open(ServerPlayer player, int traderEntityId) {
        NetworkHooks.openScreen(player, new Provider(traderEntityId), buf -> buf.writeVarInt(traderEntityId));
    }

    public boolean isExchanged() { return exchanged.get() != 0; }
    public boolean canBreakDown() { return !isExchanged() && computeBreakDown(input.getItem(0)) != null; }
    public boolean canConsolidate() { return !isExchanged() && computeConsolidate(input.getItem(0)) != null; }

    @Override public boolean clickMenuButton(Player player, int id) {
        if (player.level().isClientSide || isExchanged()) return false;
        Exchange ex = id == 0 ? computeBreakDown(input.getItem(0)) : id == 1 ? computeConsolidate(input.getItem(0)) : null;
        if (ex == null) return false;
        internalChange = true;
        input.removeItem(0, ex.inputConsumed);
        results.setItem(id, ex.output.copy());
        results.setItem(1 - id, ItemStack.EMPTY);
        exchanged.set(1);
        internalChange = false;
        broadcastChanges();
        if (traderEntityId >= 0 && player.level().getEntity(traderEntityId) instanceof net.minecraft.world.entity.LivingEntity living) {
            living.playSound(net.minecraft.sounds.SoundEvents.VILLAGER_YES, 1.0F, 1.0F);
        }
        return true;
    }

    @Override public void slotsChanged(Container container) {
        if (!internalChange && container == results && isExchanged() && results.isEmpty()) exchanged.set(0);
        super.slotsChanged(container);
    }

    @Override public ItemStack quickMoveStack(Player player, int index) {
        Slot slot = slots.get(index);
        if (!slot.hasItem()) return ItemStack.EMPTY;
        ItemStack stack = slot.getItem();
        ItemStack copy = stack.copy();
        if (index < 3) {
            if (!moveItemStackTo(stack, 3, slots.size(), true)) return ItemStack.EMPTY;
        } else if (isCoin(stack)) {
            if (!moveItemStackTo(stack, 0, 1, false)) return ItemStack.EMPTY;
        } else return ItemStack.EMPTY;
        if (stack.isEmpty()) slot.set(ItemStack.EMPTY); else slot.setChanged();
        return copy;
    }

    @Override public void removed(Player player) {
        super.removed(player);
        if (!player.level().isClientSide) {
            clearContainer(player, input);
            clearContainer(player, results);
        }
    }

    @Override public boolean stillValid(Player player) {
        if (traderEntityId < 0) return true;
        var entity = player.level().getEntity(traderEntityId);
        return entity != null && entity.isAlive() && player.distanceToSqr(entity) <= 64.0D;
    }

    private final class ResultSlot extends Slot {
        ResultSlot(Container c, int slot, int x, int y) { super(c, slot, x, y); }
        @Override public boolean mayPlace(ItemStack stack) { return false; }
        @Override public void onTake(Player player, ItemStack stack) {
            super.onTake(player, stack);
            if (results.isEmpty()) exchanged.set(0);
        }
    }

    private static boolean isCoin(ItemStack stack) { return !stack.isEmpty() && denominationIndex(stack.getItem()) >= 0; }

    private static int denominationIndex(Item item) {
        List<Item> ascending = denominationsAscending();
        return ascending.indexOf(item);
    }

    private static List<Item> denominationsAscending() {
        ArrayList<Item> list = new ArrayList<>();
        for (Map.Entry<Item,Integer> e : GOTCoinValueService.denominationsDescending().entrySet()) list.add(0, e.getKey());
        return list;
    }

    private static Exchange computeBreakDown(ItemStack in) {
        if (!isCoin(in)) return null;
        List<Item> d = denominationsAscending();
        int idx = d.indexOf(in.getItem());
        if (idx <= 0) return null;
        int consume = Math.min(in.getCount(), 16); // 16 * 4 = vanilla stack limit 64, matching legacy clamp.
        if (consume <= 0) return null;
        return new Exchange(consume, new ItemStack(d.get(idx - 1), consume * 4));
    }

    private static Exchange computeConsolidate(ItemStack in) {
        if (!isCoin(in)) return null;
        List<Item> d = denominationsAscending();
        int idx = d.indexOf(in.getItem());
        if (idx < 0 || idx >= d.size() - 1 || in.getCount() < 4) return null;
        int out = in.getCount() / 4;
        return new Exchange(out * 4, new ItemStack(d.get(idx + 1), out));
    }

    private record Exchange(int inputConsumed, ItemStack output) {}

    public static final class Provider implements MenuProvider {
        private final int traderEntityId;
        public Provider(int traderEntityId) { this.traderEntityId = traderEntityId; }
        @Override public Component getDisplayName() { return Component.translatable("got.container.coinExchange"); }
        @Override public AbstractContainerMenu createMenu(int id, Inventory inv, Player player) { return new GOTCoinExchangeMenu(id, inv, traderEntityId); }
    }
}
