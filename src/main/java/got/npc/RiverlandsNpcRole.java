package got.npc;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Riverlands population catalogue recovered from the original 1.7.10 entity
 * hierarchy and its fixed-waypoint registrations.
 */
public enum RiverlandsNpcRole {
    RIVERLANDS_MAN("riverlands_man", "Riverlands Civilian", Gender.RANDOM, Combat.PASSIVE, Trade.NONE, 1, false, null, "", "", 1.0F, 1.0F),
    RIVERLANDS_LEVYMAN("riverlands_levyman", "Riverlands Levyman", Gender.MALE, Combat.MELEE, Trade.NONE, 2, false, null, "", "", 1.0F, 1.0F),
    RIVERLANDS_LEVYMAN_ARCHER("riverlands_levyman_archer", "Riverlands Levyman Archer", Gender.MALE, Combat.ARCHER, Trade.NONE, 2, false, null, "", "", 1.0F, 1.0F),
    RIVERLANDS_SOLDIER("riverlands_soldier", "Riverlands Soldier", Gender.MALE, Combat.MELEE, Trade.NONE, 2, false, null, "", "", 1.0F, 1.0F),
    RIVERLANDS_SOLDIER_ARCHER("riverlands_soldier_archer", "Riverlands Soldier Archer", Gender.MALE, Combat.ARCHER, Trade.NONE, 2, false, null, "", "", 1.0F, 1.0F),
    RIVERLANDS_BANNER_BEARER("riverlands_banner_bearer", "Riverlands Banner Bearer", Gender.MALE, Combat.MELEE, Trade.NONE, 2, false, null, "", "", 1.0F, 1.0F),
    RIVERLANDS_CAPTAIN("riverlands_captain", "Riverlands Captain", Gender.MALE, Combat.MELEE, Trade.UNITS, 5, false, null, "", "", 1.0F, 1.0F),
    RIVERLANDS_BLACKSMITH("riverlands_blacksmith", "Riverlands Blacksmith", Gender.MALE, Combat.PASSIVE, Trade.BLACKSMITH, 2, false, null, "", "", 1.0F, 1.0F),
    RIVERLANDS_GOLDSMITH("riverlands_goldsmith", "Riverlands Goldsmith", Gender.RANDOM, Combat.PASSIVE, Trade.GOLDSMITH, 2, false, null, "", "", 1.0F, 1.0F),
    RIVERLANDS_FARMER("riverlands_farmer", "Riverlands Farmer", Gender.MALE, Combat.PASSIVE, Trade.FARMER, 2, false, null, "", "", 1.0F, 1.0F),
    RIVERLANDS_FARMHAND("riverlands_farmhand", "Riverlands Farmhand", Gender.RANDOM, Combat.PASSIVE, Trade.NONE, 1, false, null, "", "", 1.0F, 1.0F),
    RIVERLANDS_BARTENDER("riverlands_bartender", "Riverlands Bartender", Gender.MALE, Combat.PASSIVE, Trade.BARTENDER, 2, false, null, "", "", 1.0F, 1.0F),
    RIVERLANDS_MINER("riverlands_miner", "Riverlands Miner", Gender.RANDOM, Combat.PASSIVE, Trade.MINER, 2, false, null, "", "", 1.0F, 1.0F),
    RIVERLANDS_LUMBERMAN("riverlands_lumberman", "Riverlands Lumberman", Gender.RANDOM, Combat.PASSIVE, Trade.LUMBERMAN, 2, false, null, "", "", 1.0F, 1.0F),
    RIVERLANDS_MASON("riverlands_mason", "Riverlands Mason", Gender.RANDOM, Combat.PASSIVE, Trade.MASON, 2, false, null, "", "", 1.0F, 1.0F),
    RIVERLANDS_BREWER("riverlands_brewer", "Riverlands Brewer", Gender.RANDOM, Combat.PASSIVE, Trade.BREWER, 2, false, null, "", "", 1.0F, 1.0F),
    RIVERLANDS_FLORIST("riverlands_florist", "Riverlands Florist", Gender.RANDOM, Combat.PASSIVE, Trade.FLORIST, 2, false, null, "", "", 1.0F, 1.0F),
    RIVERLANDS_BUTCHER("riverlands_butcher", "Riverlands Butcher", Gender.RANDOM, Combat.PASSIVE, Trade.BUTCHER, 2, false, null, "", "", 1.0F, 1.0F),
    RIVERLANDS_FISHMONGER("riverlands_fishmonger", "Riverlands Fishmonger", Gender.RANDOM, Combat.PASSIVE, Trade.FISHMONGER, 2, false, null, "", "", 1.0F, 1.0F),
    RIVERLANDS_BAKER("riverlands_baker", "Riverlands Baker", Gender.RANDOM, Combat.PASSIVE, Trade.BAKER, 2, false, null, "", "", 1.0F, 1.0F),

    HOT_PIE("hot_pie", "Hot Pie", Gender.MALE, Combat.PASSIVE, Trade.BAKER, 1, true, "hot_pie", "_1", "_2", 0.9F, 0.9F),

    WILLIAM_MOOTON("william_mooton", "William Mooton", Gender.MALE, Combat.MELEE, Trade.UNITS, 100, true, "william_mooton", "", "", 1.0F, 1.0F),
    CLEMENT_PIPER("clement_piper", "Clement Piper", Gender.MALE, Combat.MELEE, Trade.UNITS, 100, true, "clement_piper", "", "", 1.0F, 1.0F),
    TYTOS_BLACKWOOD("tytos_blackwood", "Tytos Blackwood", Gender.MALE, Combat.MELEE, Trade.UNITS, 100, true, "tytos_blackwood", "", "", 1.0F, 1.0F),
    HOSTER_TULLY("hoster_tully", "Hoster Tully", Gender.MALE, Combat.MELEE, Trade.UNITS, 500, true, "hoster_tully", "", "", 1.0F, 1.0F),
    BRYNDEN_TULLY("brynden_tully", "Brynden Tully", Gender.MALE, Combat.MELEE, Trade.NONE, 300, true, "brynden_tully", "_1", "_2", 1.0F, 1.0F),
    EDMURE_TULLY("edmure_tully", "Edmure Tully", Gender.MALE, Combat.HYBRID, Trade.NONE, 200, true, "edmure_tully", "_1", "_2", 1.0F, 1.0F),
    JASON_MALLISTER("jason_mallister", "Jason Mallister", Gender.MALE, Combat.MELEE, Trade.UNITS, 100, true, "jason_mallister", "", "", 1.0F, 1.0F),
    JONOS_BRACKEN("jonos_bracken", "Jonos Bracken", Gender.MALE, Combat.MELEE, Trade.UNITS, 100, true, "jonos_bracken", "", "", 1.0F, 1.0F),
    BLACK_WALDER_FREY("black_walder_frey", "Black Walder Frey", Gender.MALE, Combat.MELEE, Trade.NONE, 100, true, "black_walder_frey", "", "", 1.0F, 1.0F),
    LOTHAR_FREY("lothar_frey", "Lothar Frey", Gender.MALE, Combat.HYBRID, Trade.NONE, 100, true, "lothar_frey", "", "", 1.0F, 1.0F),
    WALDER_FREY("walder_frey", "Walder Frey", Gender.MALE, Combat.MELEE, Trade.UNITS, 300, true, "walder_frey", "_1", "_2", 1.0F, 1.0F);

    public enum Gender { RANDOM, MALE, FEMALE }
    public enum Combat { PASSIVE, MELEE, ARCHER, HYBRID }
    public enum Trade {
        NONE, BAKER, BARTENDER, BLACKSMITH, BREWER, BUTCHER, FARMER,
        FISHMONGER, FLORIST, GOLDSMITH, LUMBERMAN, MASON, MINER,
        UNITS, MAESTER
    }

    private static final Map<String, RiverlandsNpcRole> BY_ID = index();
    private static final List<RiverlandsNpcRole> SPAWNER_ORDER = List.of(values());

    private final String id;
    private final String displayName;
    private final Gender gender;
    private final Combat combat;
    private final Trade trade;
    private final int alignmentBonus;
    private final boolean legendary;
    private final String legendaryTexture;
    private final String legendaryBaseSuffix;
    private final String legendaryOverlaySuffix;
    private final float scale;
    private final float collisionScale;

    RiverlandsNpcRole(String id, String displayName, Gender gender, Combat combat, Trade trade,
                       int alignmentBonus, boolean legendary, String legendaryTexture,
                       String legendaryBaseSuffix, String legendaryOverlaySuffix,
                       float scale, float collisionScale) {
        this.id = id;
        this.displayName = displayName;
        this.gender = gender;
        this.combat = combat;
        this.trade = trade;
        this.alignmentBonus = alignmentBonus;
        this.legendary = legendary;
        this.legendaryTexture = legendaryTexture;
        this.legendaryBaseSuffix = legendaryBaseSuffix;
        this.legendaryOverlaySuffix = legendaryOverlaySuffix;
        this.scale = scale;
        this.collisionScale = collisionScale;
    }

    public String id() { return id; }
    public String displayName() { return displayName; }
    public Gender gender() { return gender; }
    public Combat combat() { return combat; }
    public Trade trade() { return trade; }
    public int alignmentBonus() { return alignmentBonus; }
    public boolean legendary() { return legendary; }
    public String legendaryTexture() { return legendaryTexture; }
    public String legendaryBaseSuffix() { return legendaryBaseSuffix; }
    public String legendaryOverlaySuffix() { return legendaryOverlaySuffix; }
    public boolean hasLegendaryOverlay() { return !legendaryOverlaySuffix.isEmpty(); }
    public float scale() { return scale; }
    public float collisionScale() { return collisionScale; }
    public boolean ordinaryCivilian() { return this == RIVERLANDS_MAN; }
    public boolean activeCombatant() { return combat != Combat.PASSIVE; }

    public static RiverlandsNpcRole byId(String id) {
        if (id == null) return RIVERLANDS_MAN;
        return BY_ID.getOrDefault(id.toLowerCase(Locale.ROOT), RIVERLANDS_MAN);
    }

    public static RiverlandsNpcRole findById(String id) {
        return id == null ? null : BY_ID.get(id.toLowerCase(Locale.ROOT));
    }

    public static List<RiverlandsNpcRole> spawnerOrder() { return SPAWNER_ORDER; }

    private static Map<String, RiverlandsNpcRole> index() {
        Map<String, RiverlandsNpcRole> roles = new LinkedHashMap<>();
        for (RiverlandsNpcRole role : values()) roles.put(role.id, role);
        return Map.copyOf(roles);
    }
}
