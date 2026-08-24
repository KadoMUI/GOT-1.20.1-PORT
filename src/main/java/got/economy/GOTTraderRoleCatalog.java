package got.economy;

import java.util.Locale;
import java.util.Map;

/**
 * Shared role-to-original-trade-pool mapping.
 * Regional NPC classes should identify profession; they should not each own
 * duplicated copies of the same economy table.
 */
public final class GOTTraderRoleCatalog {
    public record Pools(String sells, String buys) {}

    private static final Map<String, Pools> STANDARD = Map.ofEntries(
        Map.entry("baker", new Pools("BAKER_SELLS", "BAKER_BUYS")),
        Map.entry("brewer", new Pools("BREWER_SELLS", "BREWER_BUYS")),
        Map.entry("butcher", new Pools("BUTCHER_SELLS", "BUTCHER_BUYS")),
        Map.entry("goldsmith", new Pools("GOLDSMITH_SELLS", "GOLDSMITH_BUYS")),
        Map.entry("miner", new Pools("MINER_SELLS", "MINER_BUYS")),
        Map.entry("fishmonger", new Pools("FISHMONGER_SELLS", "FISHMONGER_BUYS")),
        Map.entry("maester", new Pools("MAESTER_SELLS", "MAESTER_BUYS")),
        Map.entry("tramp", new Pools("TRAMP_SELLS", "TRAMP_BUYS")),
        Map.entry("lumberman", new Pools("LUMBERMAN_SELLS", "LUMBERMAN_BUYS")),
        Map.entry("florist", new Pools("FLORIST_SELLS", "FLORIST_BUYS")),
        Map.entry("farmer", new Pools("FARMER_SELLS", "FARMER_BUYS")),
        Map.entry("mason", new Pools("MASON_SELLS", "MASON_BUYS")),
        Map.entry("alchemist", new Pools("ALCHEMIST_SELLS", "ALCHEMIST_BUYS")),
        Map.entry("bartender", new Pools("BARTENDER_SELLS", "BARTENDER_BUYS")),
        Map.entry("blacksmith", new Pools("BLACKSMITH_SELLS", "BLACKSMITH_BUYS"))
    );

    private static final Map<String, Pools> SPECIAL = Map.of(
        "petyr_baelish", new Pools("BAELISH_SELLS", "EMPTY_BUYS"),
        "davos_seaworth", new Pools("DAVOS_SELLS", "EMPTY_BUYS"),
        "tobho_mott", new Pools("TOBHO_SELLS", "EMPTY_BUYS")
    );

    private GOTTraderRoleCatalog() {}

    public static Pools forRole(String roleId) {
        if (roleId == null) return null;
        String id = roleId.toLowerCase(Locale.ROOT);
        Pools special = SPECIAL.get(id);
        return special != null ? special : STANDARD.get(id);
    }

    public static Pools exoticLumberman() {
        return new Pools("LUMBERMAN_EXOTIC_SELLS", "LUMBERMAN_EXOTIC_BUYS");
    }

    public static Pools exoticFarmer() {
        return new Pools("FARMER_EXOTIC_SELLS", "FARMER_EXOTIC_BUYS");
    }

    public static Pools crownlandsAlchemist() {
        return new Pools("CROWNLANDS_ALCHEMIST_SELLS", "CROWNLANDS_ALCHEMIST_BUYS");
    }
}
