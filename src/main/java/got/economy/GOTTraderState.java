package got.economy;

/**
 * Recovered default GOTTraderInfo tuning values from 1.7.10.
 */
public final class GOTTraderState {
    public static final int DEFAULT_LOCK_TRADE_AT_VALUE = 200;
    public static final int DEFAULT_VALUE_DECAY_TICKS = 60;
    public static final int DEFAULT_REFRESH_AT_VALUE = 5000;
    public static final int DEFAULT_LOCK_TICKS_AFTER_REFRESH = 6000;

    private boolean lockTrades = true;
    private boolean refreshTrades = true;
    private int lockTradeAtValue = DEFAULT_LOCK_TRADE_AT_VALUE;
    private int valueDecayTicks = DEFAULT_VALUE_DECAY_TICKS;
    private int refreshAtValue = DEFAULT_REFRESH_AT_VALUE;
    private int lockTicksAfterRefresh = DEFAULT_LOCK_TICKS_AFTER_REFRESH;
    private int valueSinceRefresh;
    private int timeSinceTrade;

    public boolean lockTrades() { return lockTrades; }
    public boolean refreshTrades() { return refreshTrades; }
    public int lockTradeAtValue() { return lockTradeAtValue; }
    public int valueDecayTicks() { return valueDecayTicks; }
    public int refreshAtValue() { return refreshAtValue; }
    public int lockTicksAfterRefresh() { return lockTicksAfterRefresh; }
    public int valueSinceRefresh() { return valueSinceRefresh; }
    public int timeSinceTrade() { return timeSinceTrade; }

    public void tick() { timeSinceTrade++; }

    public void onTrade(GOTTradeEntryState entry, int value) {
        int positive = Math.max(0, value);
        entry.transaction(positive);
        valueSinceRefresh += positive;
        timeSinceTrade = 0;
    }

    public boolean shouldRefreshNow() {
        return refreshTrades && valueSinceRefresh >= refreshAtValue;
    }

    public void markRefreshed() {
        valueSinceRefresh = 0;
    }
}
