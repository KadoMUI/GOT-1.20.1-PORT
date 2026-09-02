package got.conquest.economy;

import got.conquest.GOTConquestSavedData;
import got.conquest.pact.GOTCanonicalPactState;
import got.economy.GOTCoinValueService;
import got.pact.GOTPact;
import got.pact.GOTPactSavedData;
import got.pact.GOTPactService;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

import java.util.Optional;
import java.util.UUID;

/** Server-authoritative Pact treasury and passive canonical-income API. */
public final class GOTPactEconomyService {
    public static final long GAME_TICKS_PER_INCOME_PERIOD = 24_000L;

    private GOTPactEconomyService() {}

    public static Optional<GOTPactTreasuryState> treasury(MinecraftServer server, UUID pactId) {
        return GOTConquestSavedData.get(server).treasury(pactId);
    }

    public static long balance(MinecraftServer server, UUID pactId) {
        return treasury(server, pactId).map(GOTPactTreasuryState::balance).orElse(0L);
    }

    /**
     * Converts physical GOT coin items from a player inventory into Pact value.
     * Any Pact member may contribute. There is deliberately no player withdrawal
     * command in Pass 5; later strategic systems spend this money collectively.
     */
    public static TransactionResult deposit(ServerPlayer player, int requestedValue) {
        if (requestedValue <= 0) return TransactionResult.fail("Deposit value must be positive.");
        Optional<GOTPact> pact = GOTPactService.forPlayer(player.server, player.getUUID());
        if (pact.isEmpty()) return TransactionResult.fail("You are not in a Pact.");

        int available = GOTCoinValueService.inventoryValue(player);
        if (available < requestedValue) {
            return TransactionResult.fail("You only have " + GOTCoinValueService.format(available) + " coin value available.");
        }
        if (!takePhysicalCoins(player, requestedValue)) {
            return TransactionResult.fail("Could not remove the requested coins from your inventory.");
        }

        long now = player.server.overworld().getGameTime();
        GOTConquestSavedData data = GOTConquestSavedData.get(player.server);
        data.depositTreasury(pact.get().id(), requestedValue, now);
        long balance = data.treasury(pact.get().id()).map(GOTPactTreasuryState::balance).orElse(0L);
        return TransactionResult.ok(requestedValue, balance);
    }

    public static TransactionResult depositAll(ServerPlayer player) {
        int available = GOTCoinValueService.inventoryValue(player);
        if (available <= 0) return TransactionResult.fail("You do not have any GOT coins to deposit.");
        return deposit(player, available);
    }

    /** Generic spend hook for future armies, claims, diplomacy and upkeep. */
    public static boolean spend(MinecraftServer server, UUID pactId, long value) {
        if (value <= 0L) return true;
        return GOTConquestSavedData.get(server).spendTreasury(
                pactId, value, server.overworld().getGameTime());
    }

    /**
     * Generates the fixed Pass-5 canonical income. Territory-derived taxes are
     * intentionally not part of this calculation yet.
     */
    public static long processCanonicalIncome(MinecraftServer server) {
        long now = server.overworld().getGameTime();
        GOTConquestSavedData data = GOTConquestSavedData.get(server);
        long generated = 0L;

        for (GOTCanonicalEconomySeeds.Seed seed : GOTCanonicalEconomySeeds.seeds()) {
            GOTPactTreasuryState treasury = data.treasury(seed.pactId()).orElse(null);
            if (treasury == null) continue;

            long last = treasury.lastIncomeGameTime();
            if (now < last) {
                data.setTreasuryIncomeClock(seed.pactId(), now, now);
                continue;
            }

            Optional<GOTCanonicalPactState> pact = data.canonicalPact(seed.pactId());
            if (pact.isEmpty() || !pact.get().active()) {
                // Dead/inactive canonical Pacts do not accumulate even partial retroactive time.
                data.setTreasuryIncomeClock(seed.pactId(), now, now);
                continue;
            }

            long elapsed = now - last;
            long periods = elapsed / GAME_TICKS_PER_INCOME_PERIOD;
            if (periods <= 0L) continue;

            long newClock = last + periods * GAME_TICKS_PER_INCOME_PERIOD;
            long amount = multiplySaturated(seed.incomePerMinecraftDay(), periods);
            data.generateTreasuryIncome(seed.pactId(), amount, newClock, now);
            generated = addSaturated(generated, amount);
        }
        return generated;
    }

    /** Remove stale player-Pact accounts after the Pact itself has been disbanded. */
    public static int cleanupMissingPlayerPacts(MinecraftServer server) {
        GOTPactSavedData pacts = GOTPactSavedData.get(server);
        return GOTConquestSavedData.get(server).clearMissingPlayerPactTreasuries(
                pactId -> pacts.pact(pactId).isPresent());
    }

    /**
     * Same exact-value behavior as ordinary coin spending, but intentionally
     * does not grant Creative players a free deposit. Physical coins are always
     * removed, and any excess inventory value is returned as change.
     */
    private static boolean takePhysicalCoins(ServerPlayer player, int value) {
        if (value <= 0) return true;
        int original = GOTCoinValueService.inventoryValue(player);
        if (original < value) return false;
        int remainder = original - value;

        for (int i = 0; i < player.getInventory().items.size(); i++) {
            ItemStack stack = player.getInventory().items.get(i);
            if (GOTCoinValueService.valueOf(stack) > 0) player.getInventory().items.set(i, ItemStack.EMPTY);
        }
        GOTCoinValueService.give(player, remainder);
        player.getInventory().setChanged();
        return true;
    }

    private static long multiplySaturated(long value, long multiplier) {
        if (value <= 0L || multiplier <= 0L) return 0L;
        if (value > Long.MAX_VALUE / multiplier) return Long.MAX_VALUE;
        return value * multiplier;
    }

    private static long addSaturated(long left, long right) {
        if (right <= 0L) return left;
        if (Long.MAX_VALUE - left < right) return Long.MAX_VALUE;
        return left + right;
    }

    public record TransactionResult(boolean success, String error, long transferred, long treasuryBalance) {
        public static TransactionResult ok(long transferred, long balance) {
            return new TransactionResult(true, "", transferred, balance);
        }

        public static TransactionResult fail(String error) {
            return new TransactionResult(false, error, 0L, 0L);
        }
    }
}
