package got.economy;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

/**
 * Stateful modern equivalent of legacy GOTTradeEntry.
 */
public final class GOTTradeEntryState {
    private final ItemStack item;
    private final boolean frozenPrice;
    private int cost;
    private int recentTradeValue;
    private int lockedTicks;

    public GOTTradeEntryState(ItemStack item, int cost, boolean frozenPrice) {
        this.item = item.copy();
        this.cost = Math.max(1, cost);
        this.frozenPrice = frozenPrice;
    }

    public ItemStack item() { return item.copy(); }
    public int cost() { return cost; }
    public boolean frozenPrice() { return frozenPrice; }
    public int recentTradeValue() { return recentTradeValue; }
    public int lockedTicks() { return lockedTicks; }

    public void transaction(int value) {
        recentTradeValue += Math.max(0, value);
    }

    public boolean available(GOTTraderState trader) {
        return !trader.lockTrades()
            || (recentTradeValue < trader.lockTradeAtValue() && lockedTicks <= 0);
    }

    public boolean tick(GOTTraderState trader, int entityTick) {
        boolean before = available(trader);
        int beforeProgress = lockProgress(trader);

        if (entityTick % trader.valueDecayTicks() == 0 && recentTradeValue > 0) {
            recentTradeValue--;
        }
        if (lockedTicks > 0) lockedTicks--;

        return before != available(trader) || beforeProgress != lockProgress(trader);
    }

    public int lockProgress(GOTTraderState trader) {
        if (!trader.lockTrades() || trader.lockTradeAtValue() <= 0) return 0;
        return Math.round((recentTradeValue / (float) trader.lockTradeAtValue()) * 16.0F);
    }

    public void lockFor(int ticks) {
        lockedTicks = Math.max(0, ticks);
    }

    public CompoundTag save() {
        CompoundTag tag = new CompoundTag();
        tag.put("Item", item.save(new CompoundTag()));
        tag.putInt("Cost", cost);
        tag.putBoolean("FrozenPrice", frozenPrice);
        tag.putInt("RecentTradeValue", recentTradeValue);
        tag.putInt("LockedTicks", lockedTicks);
        return tag;
    }

    public static GOTTradeEntryState load(CompoundTag tag) {
        GOTTradeEntryState state = new GOTTradeEntryState(
            ItemStack.of(tag.getCompound("Item")),
            tag.getInt("Cost"),
            tag.getBoolean("FrozenPrice")
        );
        state.recentTradeValue = tag.getInt("RecentTradeValue");
        state.lockedTicks = tag.getInt("LockedTicks");
        return state;
    }
}
