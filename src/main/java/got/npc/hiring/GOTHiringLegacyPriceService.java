package got.npc.hiring;

import net.minecraft.server.level.ServerPlayer;

/**
 * Exact reconstruction of GOTUnitTradeEntry#getCost from 24.08.29.
 *
 * Legacy algorithm:
 *
 * cost = initialCost
 * excessAlignment = max(playerAlignment - requiredAlignment, 0)
 *
 * if pledged to NPC faction:
 *     discount = excessAlignment / 1500
 * else:
 *     cost *= 2
 *     discount = excessAlignment / 2000
 *
 * discount clamped 0..1
 * cost *= 1 - discount * 0.5
 * round; minimum 1
 *
 * Consequences:
 * - unpledged players pay double base price
 * - sufficiently high alignment can reduce that by at most 50%
 * - pledged players begin at base price and can reach 50% of base price
 */
public final class GOTHiringLegacyPriceService {
    private GOTHiringLegacyPriceService() {}

    public static int price(
        ServerPlayer player,
        String factionId,
        int initialCost,
        float requiredAlignment
    ) {
        float cost = initialCost;
        float alignment = GOTHiringPlayerRules.alignment(player, factionId);
        boolean pledged = GOTHiringPlayerRules.pledgedTo(player, factionId);
        float excess = Math.max(alignment - requiredAlignment, 0.0F);

        float discount;
        if (pledged) {
            discount = excess / 1500.0F;
        } else {
            cost *= 2.0F;
            discount = excess / 2000.0F;
        }

        discount = Math.max(0.0F, Math.min(1.0F, discount));
        cost *= 1.0F - discount * 0.5F;

        return Math.max(1, Math.round(cost));
    }
}
