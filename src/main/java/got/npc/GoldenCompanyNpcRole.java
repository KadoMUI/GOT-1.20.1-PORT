package got.npc;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/** The four Golden Company troop types plus Harry Strickland. */
public enum GoldenCompanyNpcRole {
    GOLDEN_COMPANY_WARRIOR("golden_company_warrior", "Golden Company Warrior", Combat.MELEE, Trade.NONE, false),
    GOLDEN_COMPANY_SPEARMAN("golden_company_spearman", "Golden Company Spearman", Combat.MELEE, Trade.NONE, false),
    GOLDEN_COMPANY_BANNER_BEARER("golden_company_banner_bearer", "Golden Company Banner Bearer", Combat.MELEE, Trade.NONE, false),
    GOLDEN_COMPANY_CAPTAIN("golden_company_captain", "Golden Company Captain", Combat.PASSIVE, Trade.UNITS, false),
    HARRY_STRICKLAND("harry_strickland", "Harry Strickland", Combat.PASSIVE, Trade.UNITS, true);

    public enum Combat { PASSIVE, MELEE }
    public enum Trade { NONE, UNITS }

    private static final Map<String, GoldenCompanyNpcRole> BY_ID = index();
    private static final List<GoldenCompanyNpcRole> SPAWNER_ORDER = List.of(values());
    private final String id;
    private final String displayName;
    private final Combat combat;
    private final Trade trade;
    private final boolean legendary;

    GoldenCompanyNpcRole(String id, String displayName, Combat combat, Trade trade,
                         boolean legendary) {
        this.id = id;
        this.displayName = displayName;
        this.combat = combat;
        this.trade = trade;
        this.legendary = legendary;
    }

    public String id() { return id; }
    public String displayName() { return displayName; }
    public Combat combat() { return combat; }
    public Trade trade() { return trade; }
    public boolean legendary() { return legendary; }
    public boolean activeCombatant() { return combat != Combat.PASSIVE; }
    public String legendaryTexture() { return legendary ? "harry_strickland" : null; }
    public String legendaryBaseSuffix() { return legendary ? "_1" : ""; }
    public String legendaryOverlaySuffix() { return legendary ? "_2" : ""; }
    public boolean hasLegendaryOverlay() { return legendary; }
    public float scale() { return 1.0F; }

    public static GoldenCompanyNpcRole byId(String id) {
        return id == null ? GOLDEN_COMPANY_WARRIOR
                : BY_ID.getOrDefault(id.toLowerCase(Locale.ROOT), GOLDEN_COMPANY_WARRIOR);
    }

    public static GoldenCompanyNpcRole findById(String id) {
        return id == null ? null : BY_ID.get(id.toLowerCase(Locale.ROOT));
    }

    public static List<GoldenCompanyNpcRole> spawnerOrder() { return SPAWNER_ORDER; }

    private static Map<String, GoldenCompanyNpcRole> index() {
        Map<String, GoldenCompanyNpcRole> roles = new LinkedHashMap<>();
        for (GoldenCompanyNpcRole role : values()) roles.put(role.id, role);
        return Map.copyOf(roles);
    }
}
