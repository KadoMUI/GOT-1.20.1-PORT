package got.economy;

import got.faction.GOTFaction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;

/**
 * Modern home for the two pieces of bookkeeping performed by the original
 * GOTTraderInfo#onTrade:
 *
 * 1. increment the player's trade count for the trader's faction
 * 2. award the one-time "Start a business" / TRADE achievement
 *
 * The full legacy achievement GUI is a later checklist system, so this stores
 * an achievement-compatible flag now rather than inventing a second UI.
 */
public final class GOTTradeProgress {
    private static final String ROOT = "GOTEconomyProgress";

    private GOTTradeProgress() {}

    public static void record(ServerPlayer player, GOTFaction faction) {
        CompoundTag root = player.getPersistentData().getCompound(ROOT);
        String factionId = faction == null ? "unaligned" : faction.id();
        root.putInt("Trades_" + factionId, root.getInt("Trades_" + factionId) + 1);
        root.putInt("TradesTotal", root.getInt("TradesTotal") + 1);
        root.putBoolean("AchievementTrade", true);
        got.achievement.GOTAchievementHooks.award(player, "TRADE");
        player.getPersistentData().put(ROOT, root);
    }

    public static int trades(ServerPlayer player, GOTFaction faction) {
        CompoundTag root = player.getPersistentData().getCompound(ROOT);
        return root.getInt("Trades_" + (faction == null ? "unaligned" : faction.id()));
    }

    public static int totalTrades(ServerPlayer player) {
        return player.getPersistentData().getCompound(ROOT).getInt("TradesTotal");
    }

    public static boolean hasTradeAchievement(ServerPlayer player) {
        return player.getPersistentData().getCompound(ROOT).getBoolean("AchievementTrade");
    }
}
