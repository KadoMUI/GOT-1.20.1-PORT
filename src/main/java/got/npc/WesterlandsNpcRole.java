package got.npc;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Westerlands population catalogue recovered from the original 1.7.10 entity
 * hierarchy and its fixed-waypoint registrations.
 */
public enum WesterlandsNpcRole {
    WESTERLANDS_MAN("westerlands_man", "Westerlands Civilian", Gender.RANDOM, Combat.PASSIVE, Trade.NONE, 1, false, null, "", "", 1.0F, 1.0F),
    WESTERLANDS_LEVYMAN("westerlands_levyman", "Westerlands Levyman", Gender.MALE, Combat.MELEE, Trade.NONE, 2, false, null, "", "", 1.0F, 1.0F),
    WESTERLANDS_LEVYMAN_ARCHER("westerlands_levyman_archer", "Westerlands Levyman Archer", Gender.MALE, Combat.ARCHER, Trade.NONE, 2, false, null, "", "", 1.0F, 1.0F),
    WESTERLANDS_SOLDIER("westerlands_soldier", "Westerlands Soldier", Gender.MALE, Combat.MELEE, Trade.NONE, 2, false, null, "", "", 1.0F, 1.0F),
    WESTERLANDS_SOLDIER_ARCHER("westerlands_soldier_archer", "Westerlands Soldier Archer", Gender.MALE, Combat.ARCHER, Trade.NONE, 2, false, null, "", "", 1.0F, 1.0F),
    WESTERLANDS_GUARD("westerlands_guard", "Lannisport Guard", Gender.MALE, Combat.MELEE, Trade.NONE, 2, false, null, "", "", 1.0F, 1.0F),
    WESTERLANDS_BANNER_BEARER("westerlands_banner_bearer", "Westerlands Banner Bearer", Gender.MALE, Combat.MELEE, Trade.NONE, 2, false, null, "", "", 1.0F, 1.0F),
    WESTERLANDS_CAPTAIN("westerlands_captain", "Westerlands Captain", Gender.MALE, Combat.MELEE, Trade.UNITS, 5, false, null, "", "", 1.0F, 1.0F),
    WESTERLANDS_BLACKSMITH("westerlands_blacksmith", "Westerlands Blacksmith", Gender.MALE, Combat.PASSIVE, Trade.BLACKSMITH, 2, false, null, "", "", 1.0F, 1.0F),
    WESTERLANDS_GOLDSMITH("westerlands_goldsmith", "Westerlands Goldsmith", Gender.RANDOM, Combat.PASSIVE, Trade.GOLDSMITH, 2, false, null, "", "", 1.0F, 1.0F),
    WESTERLANDS_FARMER("westerlands_farmer", "Westerlands Farmer", Gender.MALE, Combat.PASSIVE, Trade.FARMER, 2, false, null, "", "", 1.0F, 1.0F),
    WESTERLANDS_FARMHAND("westerlands_farmhand", "Westerlands Farmhand", Gender.RANDOM, Combat.PASSIVE, Trade.NONE, 1, false, null, "", "", 1.0F, 1.0F),
    WESTERLANDS_BARTENDER("westerlands_bartender", "Westerlands Bartender", Gender.MALE, Combat.PASSIVE, Trade.BARTENDER, 2, false, null, "", "", 1.0F, 1.0F),
    WESTERLANDS_MINER("westerlands_miner", "Westerlands Miner", Gender.RANDOM, Combat.PASSIVE, Trade.MINER, 2, false, null, "", "", 1.0F, 1.0F),
    WESTERLANDS_LUMBERMAN("westerlands_lumberman", "Westerlands Lumberman", Gender.RANDOM, Combat.PASSIVE, Trade.LUMBERMAN, 2, false, null, "", "", 1.0F, 1.0F),
    WESTERLANDS_MASON("westerlands_mason", "Westerlands Mason", Gender.RANDOM, Combat.PASSIVE, Trade.MASON, 2, false, null, "", "", 1.0F, 1.0F),
    WESTERLANDS_BREWER("westerlands_brewer", "Westerlands Brewer", Gender.RANDOM, Combat.PASSIVE, Trade.BREWER, 2, false, null, "", "", 1.0F, 1.0F),
    WESTERLANDS_FLORIST("westerlands_florist", "Westerlands Florist", Gender.RANDOM, Combat.PASSIVE, Trade.FLORIST, 2, false, null, "", "", 1.0F, 1.0F),
    WESTERLANDS_BUTCHER("westerlands_butcher", "Westerlands Butcher", Gender.RANDOM, Combat.PASSIVE, Trade.BUTCHER, 2, false, null, "", "", 1.0F, 1.0F),
    WESTERLANDS_FISHMONGER("westerlands_fishmonger", "Westerlands Fishmonger", Gender.RANDOM, Combat.PASSIVE, Trade.FISHMONGER, 2, false, null, "", "", 1.0F, 1.0F),
    WESTERLANDS_BAKER("westerlands_baker", "Westerlands Baker", Gender.RANDOM, Combat.PASSIVE, Trade.BAKER, 2, false, null, "", "", 1.0F, 1.0F),

    ADDAM_MARBRAND("addam_marbrand", "Addam Marbrand", Gender.MALE, Combat.MELEE, Trade.UNITS, 100, true, "addam_marbrand", "", "", 1.0F, 1.0F),
    QUENTEN_BANEFORT("quenten_banefort", "Quenten Banefort", Gender.MALE, Combat.MELEE, Trade.UNITS, 100, true, "quenten_banefort", "", "", 1.0F, 1.0F),
    TYWIN_LANNISTER("tywin_lannister", "Tywin Lannister", Gender.MALE, Combat.MELEE, Trade.NONE, 500, true, "tywin_lannister", "", "", 1.0F, 1.0F),
    QYBURN("qyburn", "Qyburn", Gender.MALE, Combat.PASSIVE, Trade.MAESTER, 100, true, "qyburn", "", "", 1.0F, 1.0F),
    GREGOR_CLEGANE("gregor_clegane", "Gregor Clegane", Gender.MALE, Combat.MELEE, Trade.NONE, 300, true, "gregor_clegane", "_1", "_3", 1.3F, 1.3F),
    POLLIVER("polliver", "Polliver", Gender.MALE, Combat.MELEE, Trade.NONE, 50, true, "polliver", "", "", 1.0F, 1.0F),
    HARYS_SWYFT("harys_swyft", "Harys Swyft", Gender.MALE, Combat.MELEE, Trade.UNITS, 100, true, "harys_swyft", "", "", 1.0F, 1.0F),
    LYLE_CRAKEHALL("lyle_crakehall", "Lyle Crakehall", Gender.MALE, Combat.MELEE, Trade.NONE, 200, true, "lyle_crakehall", "", "", 1.0F, 1.0F),
    SEBASTON_FARMAN("sebaston_farman", "Sebaston Farman", Gender.MALE, Combat.MELEE, Trade.UNITS, 100, true, "sebaston_farman", "", "", 1.0F, 1.0F),
    FORLEY_PRESTER("forley_prester", "Forley Prester", Gender.MALE, Combat.MELEE, Trade.UNITS, 100, true, "forley_prester", "", "", 1.0F, 1.0F),
    LEO_LEFFORD("leo_lefford", "Leo Lefford", Gender.MALE, Combat.MELEE, Trade.UNITS, 100, true, "leo_lefford", "", "", 1.0F, 1.0F),
    TYTOS_BRAX("tytos_brax", "Tytos Brax", Gender.MALE, Combat.MELEE, Trade.UNITS, 100, true, "tytos_brax", "", "", 1.0F, 1.0F),
    KEVAN_LANNISTER("kevan_lannister", "Kevan Lannister", Gender.MALE, Combat.MELEE, Trade.UNITS, 300, true, "kevan_lannister", "", "", 1.0F, 1.0F),
    DAVEN_LANNISTER("daven_lannister", "Daven Lannister", Gender.MALE, Combat.MELEE, Trade.NONE, 100, true, "daven_lannister", "", "", 1.0F, 1.0F),
    AMORY_LORCH("amory_lorch", "Amory Lorch", Gender.MALE, Combat.MELEE, Trade.NONE, 100, true, "amory_lorch", "", "", 1.0F, 1.0F);

    public enum Gender { RANDOM, MALE, FEMALE }
    public enum Combat { PASSIVE, MELEE, ARCHER }
    public enum Trade {
        NONE, BAKER, BARTENDER, BLACKSMITH, BREWER, BUTCHER, FARMER,
        FISHMONGER, FLORIST, GOLDSMITH, LUMBERMAN, MASON, MINER,
        UNITS, MAESTER
    }

    private static final Map<String, WesterlandsNpcRole> BY_ID = index();
    private static final List<WesterlandsNpcRole> SPAWNER_ORDER = List.of(values());

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

    WesterlandsNpcRole(String id, String displayName, Gender gender, Combat combat, Trade trade,
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
    public boolean ordinaryCivilian() { return this == WESTERLANDS_MAN; }
    public boolean activeCombatant() { return combat != Combat.PASSIVE; }

    public static WesterlandsNpcRole byId(String id) {
        if (id == null) return WESTERLANDS_MAN;
        return BY_ID.getOrDefault(id.toLowerCase(Locale.ROOT), WESTERLANDS_MAN);
    }

    public static WesterlandsNpcRole findById(String id) {
        return id == null ? null : BY_ID.get(id.toLowerCase(Locale.ROOT));
    }

    public static List<WesterlandsNpcRole> spawnerOrder() { return SPAWNER_ORDER; }

    private static Map<String, WesterlandsNpcRole> index() {
        Map<String, WesterlandsNpcRole> roles = new LinkedHashMap<>();
        for (WesterlandsNpcRole role : values()) roles.put(role.id, role);
        return Map.copyOf(roles);
    }
}
