package got.npc;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Reach population catalogue recovered from the original 1.7.10 entity
 * hierarchy and its fixed-waypoint registrations.
 */
public enum ReachNpcRole {
    REACH_MAN("reach_man", "Reach Civilian", Gender.RANDOM, Combat.PASSIVE, Trade.NONE, 1, false, null, "", "", 1.0F, 1.0F),
    REACH_LEVYMAN("reach_levyman", "Reach Levyman", Gender.MALE, Combat.MELEE, Trade.NONE, 2, false, null, "", "", 1.0F, 1.0F),
    REACH_LEVYMAN_ARCHER("reach_levyman_archer", "Reach Levyman Archer", Gender.MALE, Combat.ARCHER, Trade.NONE, 2, false, null, "", "", 1.0F, 1.0F),
    REACH_SOLDIER("reach_soldier", "Reach Soldier", Gender.MALE, Combat.MELEE, Trade.NONE, 2, false, null, "", "", 1.0F, 1.0F),
    REACH_SOLDIER_ARCHER("reach_soldier_archer", "Reach Soldier Archer", Gender.MALE, Combat.ARCHER, Trade.NONE, 2, false, null, "", "", 1.0F, 1.0F),
    REACH_GUARD("reach_guard", "Oldtown Guard", Gender.MALE, Combat.MELEE, Trade.NONE, 2, false, null, "", "", 1.0F, 1.0F),
    REACH_BANNER_BEARER("reach_banner_bearer", "Reach Banner Bearer", Gender.MALE, Combat.MELEE, Trade.NONE, 2, false, null, "", "", 1.0F, 1.0F),
    REACH_CAPTAIN("reach_captain", "Reach Captain", Gender.MALE, Combat.MELEE, Trade.UNITS, 5, false, null, "", "", 1.0F, 1.0F),
    REACH_BLACKSMITH("reach_blacksmith", "Reach Blacksmith", Gender.MALE, Combat.PASSIVE, Trade.BLACKSMITH, 2, false, null, "", "", 1.0F, 1.0F),
    REACH_GOLDSMITH("reach_goldsmith", "Reach Goldsmith", Gender.RANDOM, Combat.PASSIVE, Trade.GOLDSMITH, 2, false, null, "", "", 1.0F, 1.0F),
    REACH_FARMER("reach_farmer", "Reach Farmer", Gender.MALE, Combat.PASSIVE, Trade.FARMER, 2, false, null, "", "", 1.0F, 1.0F),
    REACH_FARMHAND("reach_farmhand", "Reach Farmhand", Gender.RANDOM, Combat.PASSIVE, Trade.NONE, 1, false, null, "", "", 1.0F, 1.0F),
    REACH_BARTENDER("reach_bartender", "Reach Bartender", Gender.MALE, Combat.PASSIVE, Trade.BARTENDER, 2, false, null, "", "", 1.0F, 1.0F),
    REACH_MINER("reach_miner", "Reach Miner", Gender.RANDOM, Combat.PASSIVE, Trade.MINER, 2, false, null, "", "", 1.0F, 1.0F),
    REACH_LUMBERMAN("reach_lumberman", "Reach Lumberman", Gender.RANDOM, Combat.PASSIVE, Trade.LUMBERMAN, 2, false, null, "", "", 1.0F, 1.0F),
    REACH_MASON("reach_mason", "Reach Mason", Gender.RANDOM, Combat.PASSIVE, Trade.MASON, 2, false, null, "", "", 1.0F, 1.0F),
    REACH_BREWER("reach_brewer", "Reach Brewer", Gender.RANDOM, Combat.PASSIVE, Trade.BREWER, 2, false, null, "", "", 1.0F, 1.0F),
    REACH_FLORIST("reach_florist", "Reach Florist", Gender.RANDOM, Combat.PASSIVE, Trade.FLORIST, 2, false, null, "", "", 1.0F, 1.0F),
    REACH_BUTCHER("reach_butcher", "Reach Butcher", Gender.RANDOM, Combat.PASSIVE, Trade.BUTCHER, 2, false, null, "", "", 1.0F, 1.0F),
    REACH_FISHMONGER("reach_fishmonger", "Reach Fishmonger", Gender.RANDOM, Combat.PASSIVE, Trade.FISHMONGER, 2, false, null, "", "", 1.0F, 1.0F),
    REACH_BAKER("reach_baker", "Reach Baker", Gender.RANDOM, Combat.PASSIVE, Trade.BAKER, 2, false, null, "", "", 1.0F, 1.0F),

    GARLAN_TYRELL("garlan_tyrell", "Garlan Tyrell", Gender.MALE, Combat.MELEE, Trade.UNITS, 300, true, "garlan_tyrell", "", "", 1.0F, 1.0F),
    MATHIS_ROWAN("mathis_rowan", "Mathis Rowan", Gender.MALE, Combat.MELEE, Trade.UNITS, 100, true, "mathis_rowan", "", "", 1.0F, 1.0F),
    MORIBALD_CHESTER("moribald_chester", "Moribald Chester", Gender.MALE, Combat.MELEE, Trade.UNITS, 100, true, "moribald_chester", "", "", 1.0F, 1.0F),
    MACE_TYRELL("mace_tyrell", "Mace Tyrell", Gender.MALE, Combat.MELEE, Trade.UNITS, 500, true, "mace_tyrell", "_1", "_2", 1.0F, 1.0F),
    OLENNA_TYRELL("olenna_tyrell", "Olenna Tyrell", Gender.FEMALE, Combat.MELEE, Trade.NONE, 500, true, "olenna_tyrell", "", "", 1.0F, 1.0F),
    MARGAERY_TYRELL("margaery_tyrell", "Margaery Tyrell", Gender.FEMALE, Combat.PASSIVE, Trade.NONE, 300, true, "margaery_tyrell", "", "", 1.0F, 1.0F),
    WILLAS_TYRELL("willas_tyrell", "Willas Tyrell", Gender.MALE, Combat.MELEE, Trade.NONE, 200, true, "willas_tyrell", "", "", 1.0F, 1.0F),
    LEYTON_HIGHTOWER("leyton_hightower", "Leyton Hightower", Gender.MALE, Combat.MELEE, Trade.UNITS, 200, true, "leyton_hightower", "", "", 1.0F, 1.0F),
    RANDYLL_TARLY("randyll_tarly", "Randyll Tarly", Gender.MALE, Combat.MELEE, Trade.UNITS, 100, true, "randyll_tarly", "_1", "_2", 1.0F, 1.0F),
    ORTON_MERRYWEATHER("orton_merryweather", "Orton Merryweather", Gender.MALE, Combat.MELEE, Trade.UNITS, 100, true, "orton_merryweather", "", "", 1.0F, 1.0F),
    EBROSE("ebrose", "Ebrose", Gender.MALE, Combat.MELEE, Trade.MAESTER, 100, true, "ebrose", "", "", 1.0F, 1.0F),
    QUENN_ROXTON("quenn_roxton", "Quenn Roxton", Gender.MALE, Combat.MELEE, Trade.UNITS, 100, true, "quenn_roxton", "", "", 1.0F, 1.0F),
    PAXTER_REDWYNE("paxter_redwyne", "Paxter Redwyne", Gender.MALE, Combat.MELEE, Trade.UNITS, 100, true, "paxter_redwyne", "", "", 1.0F, 1.0F),
    LORAS_TYRELL("loras_tyrell", "Loras Tyrell", Gender.MALE, Combat.MELEE, Trade.NONE, 200, true, "loras_tyrell", "", "", 1.0F, 1.0F);

    public enum Gender { RANDOM, MALE, FEMALE }
    public enum Combat { PASSIVE, MELEE, ARCHER, HYBRID }
    public enum Trade {
        NONE, BAKER, BARTENDER, BLACKSMITH, BREWER, BUTCHER, FARMER,
        FISHMONGER, FLORIST, GOLDSMITH, LUMBERMAN, MASON, MINER,
        UNITS, MAESTER
    }

    private static final Map<String, ReachNpcRole> BY_ID = index();
    private static final List<ReachNpcRole> SPAWNER_ORDER = List.of(values());

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

    ReachNpcRole(String id, String displayName, Gender gender, Combat combat, Trade trade,
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
    public boolean ordinaryCivilian() { return this == REACH_MAN; }
    public boolean activeCombatant() { return combat != Combat.PASSIVE; }

    public static ReachNpcRole byId(String id) {
        if (id == null) return REACH_MAN;
        return BY_ID.getOrDefault(id.toLowerCase(Locale.ROOT), REACH_MAN);
    }

    public static ReachNpcRole findById(String id) {
        return id == null ? null : BY_ID.get(id.toLowerCase(Locale.ROOT));
    }

    public static List<ReachNpcRole> spawnerOrder() { return SPAWNER_ORDER; }

    private static Map<String, ReachNpcRole> index() {
        Map<String, ReachNpcRole> roles = new LinkedHashMap<>();
        for (ReachNpcRole role : values()) roles.put(role.id, role);
        return Map.copyOf(roles);
    }
}
