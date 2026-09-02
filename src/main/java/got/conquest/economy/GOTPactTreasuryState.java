package got.conquest.economy;

import net.minecraft.nbt.CompoundTag;

import java.util.UUID;

/**
 * Persistent monetary account for any Conquest Pact, canonical or player-run.
 *
 * The treasury stores abstract coin value, not physical ItemStacks. Physical
 * GOT coins are converted into this value only when a player explicitly
 * deposits them. Strategic systems should spend from this account rather than
 * fabricating/removing inventory stacks from individual Pact members.
 */
public final class GOTPactTreasuryState {
    private final UUID pactId;
    private long balance;
    private long seededCapital;
    private long totalPlayerDeposits;
    private long totalGeneratedIncome;
    private long totalSpent;
    private long lastIncomeGameTime;
    private long lastUpdatedGameTime;

    public GOTPactTreasuryState(UUID pactId, long startingBalance, long gameTime) {
        if (pactId == null) throw new IllegalArgumentException("Pact id cannot be null");
        this.pactId = pactId;
        this.balance = Math.max(0L, startingBalance);
        this.seededCapital = this.balance;
        this.lastIncomeGameTime = Math.max(0L, gameTime);
        this.lastUpdatedGameTime = Math.max(0L, gameTime);
    }

    private GOTPactTreasuryState(UUID pactId) {
        this.pactId = pactId;
    }

    public UUID pactId() { return pactId; }
    public long balance() { return balance; }
    public long seededCapital() { return seededCapital; }
    public long totalPlayerDeposits() { return totalPlayerDeposits; }
    public long totalGeneratedIncome() { return totalGeneratedIncome; }
    public long totalSpent() { return totalSpent; }
    public long lastIncomeGameTime() { return lastIncomeGameTime; }
    public long lastUpdatedGameTime() { return lastUpdatedGameTime; }

    public void depositPlayer(long amount, long gameTime) {
        amount = positive(amount);
        if (amount == 0L) return;
        balance = addSaturated(balance, amount);
        totalPlayerDeposits = addSaturated(totalPlayerDeposits, amount);
        touch(gameTime);
    }

    public void generateIncome(long amount, long newIncomeClock, long gameTime) {
        amount = positive(amount);
        if (amount > 0L) {
            balance = addSaturated(balance, amount);
            totalGeneratedIncome = addSaturated(totalGeneratedIncome, amount);
            touch(gameTime);
        }
        lastIncomeGameTime = Math.max(0L, newIncomeClock);
    }

    public boolean spend(long amount, long gameTime) {
        amount = positive(amount);
        if (amount == 0L) return true;
        if (balance < amount) return false;
        balance -= amount;
        totalSpent = addSaturated(totalSpent, amount);
        touch(gameTime);
        return true;
    }

    /** Admin/migration hook. This intentionally does not rewrite accounting totals. */
    public void setBalance(long value, long gameTime) {
        balance = Math.max(0L, value);
        touch(gameTime);
    }

    /** Advances the passive-income clock without granting money. */
    public void setIncomeClock(long incomeGameTime, long gameTime) {
        lastIncomeGameTime = Math.max(0L, incomeGameTime);
        touch(gameTime);
    }

    private void touch(long gameTime) {
        lastUpdatedGameTime = Math.max(0L, gameTime);
    }

    public CompoundTag save() {
        CompoundTag tag = new CompoundTag();
        tag.putUUID("PactId", pactId);
        tag.putLong("Balance", balance);
        tag.putLong("SeededCapital", seededCapital);
        tag.putLong("TotalPlayerDeposits", totalPlayerDeposits);
        tag.putLong("TotalGeneratedIncome", totalGeneratedIncome);
        tag.putLong("TotalSpent", totalSpent);
        tag.putLong("LastIncomeGameTime", lastIncomeGameTime);
        tag.putLong("LastUpdatedGameTime", lastUpdatedGameTime);
        return tag;
    }

    public static GOTPactTreasuryState load(CompoundTag tag) {
        if (tag == null || !tag.hasUUID("PactId")) return null;
        GOTPactTreasuryState state = new GOTPactTreasuryState(tag.getUUID("PactId"));
        state.balance = positive(tag.getLong("Balance"));
        state.seededCapital = positive(tag.getLong("SeededCapital"));
        state.totalPlayerDeposits = positive(tag.getLong("TotalPlayerDeposits"));
        state.totalGeneratedIncome = positive(tag.getLong("TotalGeneratedIncome"));
        state.totalSpent = positive(tag.getLong("TotalSpent"));
        state.lastIncomeGameTime = positive(tag.getLong("LastIncomeGameTime"));
        state.lastUpdatedGameTime = positive(tag.getLong("LastUpdatedGameTime"));
        return state;
    }

    private static long positive(long value) {
        return Math.max(0L, value);
    }

    private static long addSaturated(long left, long right) {
        if (right <= 0L) return left;
        if (Long.MAX_VALUE - left < right) return Long.MAX_VALUE;
        return left + right;
    }
}
