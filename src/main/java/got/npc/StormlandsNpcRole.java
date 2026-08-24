package got.npc;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Stormlands population catalogue recovered from the original 1.7.10 entity
 * hierarchy and its fixed-waypoint registrations.
 */
public enum StormlandsNpcRole {
    STORMLANDS_MAN("stormlands_man", "Stormlands Civilian", Gender.RANDOM, Combat.PASSIVE, Trade.NONE, 1, false, null, "", "", 1.0F, 1.0F),
    STORMLANDS_LEVYMAN("stormlands_levyman", "Stormlands Levyman", Gender.MALE, Combat.MELEE, Trade.NONE, 2, false, null, "", "", 1.0F, 1.0F),
    STORMLANDS_LEVYMAN_ARCHER("stormlands_levyman_archer", "Stormlands Levyman Archer", Gender.MALE, Combat.ARCHER, Trade.NONE, 2, false, null, "", "", 1.0F, 1.0F),
    STORMLANDS_SOLDIER("stormlands_soldier", "Stormlands Soldier", Gender.MALE, Combat.MELEE, Trade.NONE, 2, false, null, "", "", 1.0F, 1.0F),
    STORMLANDS_SOLDIER_ARCHER("stormlands_soldier_archer", "Stormlands Soldier Archer", Gender.MALE, Combat.ARCHER, Trade.NONE, 2, false, null, "", "", 1.0F, 1.0F),
    STORMLANDS_BANNER_BEARER("stormlands_banner_bearer", "Stormlands Banner Bearer", Gender.MALE, Combat.MELEE, Trade.NONE, 2, false, null, "", "", 1.0F, 1.0F),
    STORMLANDS_CAPTAIN("stormlands_captain", "Stormlands Captain", Gender.MALE, Combat.MELEE, Trade.UNITS, 5, false, null, "", "", 1.0F, 1.0F),
    STORMLANDS_BLACKSMITH("stormlands_blacksmith", "Stormlands Blacksmith", Gender.MALE, Combat.PASSIVE, Trade.BLACKSMITH, 2, false, null, "", "", 1.0F, 1.0F),
    STORMLANDS_GOLDSMITH("stormlands_goldsmith", "Stormlands Goldsmith", Gender.RANDOM, Combat.PASSIVE, Trade.GOLDSMITH, 2, false, null, "", "", 1.0F, 1.0F),
    STORMLANDS_FARMER("stormlands_farmer", "Stormlands Farmer", Gender.MALE, Combat.PASSIVE, Trade.FARMER, 2, false, null, "", "", 1.0F, 1.0F),
    STORMLANDS_FARMHAND("stormlands_farmhand", "Stormlands Farmhand", Gender.RANDOM, Combat.PASSIVE, Trade.NONE, 1, false, null, "", "", 1.0F, 1.0F),
    STORMLANDS_BARTENDER("stormlands_bartender", "Stormlands Bartender", Gender.MALE, Combat.PASSIVE, Trade.BARTENDER, 2, false, null, "", "", 1.0F, 1.0F),
    STORMLANDS_MINER("stormlands_miner", "Stormlands Miner", Gender.RANDOM, Combat.PASSIVE, Trade.MINER, 2, false, null, "", "", 1.0F, 1.0F),
    STORMLANDS_LUMBERMAN("stormlands_lumberman", "Stormlands Lumberman", Gender.RANDOM, Combat.PASSIVE, Trade.LUMBERMAN, 2, false, null, "", "", 1.0F, 1.0F),
    STORMLANDS_MASON("stormlands_mason", "Stormlands Mason", Gender.RANDOM, Combat.PASSIVE, Trade.MASON, 2, false, null, "", "", 1.0F, 1.0F),
    STORMLANDS_BREWER("stormlands_brewer", "Stormlands Brewer", Gender.RANDOM, Combat.PASSIVE, Trade.BREWER, 2, false, null, "", "", 1.0F, 1.0F),
    STORMLANDS_FLORIST("stormlands_florist", "Stormlands Florist", Gender.RANDOM, Combat.PASSIVE, Trade.FLORIST, 2, false, null, "", "", 1.0F, 1.0F),
    STORMLANDS_BUTCHER("stormlands_butcher", "Stormlands Butcher", Gender.RANDOM, Combat.PASSIVE, Trade.BUTCHER, 2, false, null, "", "", 1.0F, 1.0F),
    STORMLANDS_FISHMONGER("stormlands_fishmonger", "Stormlands Fishmonger", Gender.RANDOM, Combat.PASSIVE, Trade.FISHMONGER, 2, false, null, "", "", 1.0F, 1.0F),
    STORMLANDS_BAKER("stormlands_baker", "Stormlands Baker", Gender.RANDOM, Combat.PASSIVE, Trade.BAKER, 2, false, null, "", "", 1.0F, 1.0F),

    SELWYN_TARTH("selwyn_tarth", "Selwyn Tarth", Gender.MALE, Combat.MELEE, Trade.UNITS, 100, true, "selwyn_tarth", "", "", 1.0F, 1.0F),
    ELDON_ESTERMONT("eldon_estermont", "Eldon Estermont", Gender.MALE, Combat.MELEE, Trade.UNITS, 100, true, "eldon_estermont", "", "", 1.0F, 1.0F),
    GULIAN_SWANN("gulian_swann", "Gulian Swann", Gender.MALE, Combat.MELEE, Trade.UNITS, 100, true, "gulian_swann", "", "", 1.0F, 1.0F),
    RENLY_BARATHEON("renly_baratheon", "Renly Baratheon", Gender.MALE, Combat.MELEE, Trade.NONE, 500, true, "renly_baratheon", "", "", 1.0F, 1.0F),
    BRIENNE_TARTH("brienne_tarth", "Brienne Tarth", Gender.FEMALE, Combat.MELEE, Trade.NONE, 200, true, "brienne_tarth", "_1", "_2", 1.0F, 1.0F);

    public enum Gender { RANDOM, MALE, FEMALE }
    public enum Combat { PASSIVE, MELEE, ARCHER, HYBRID }
    public enum Trade {
        NONE, BAKER, BARTENDER, BLACKSMITH, BREWER, BUTCHER, FARMER,
        FISHMONGER, FLORIST, GOLDSMITH, LUMBERMAN, MASON, MINER,
        UNITS, MAESTER
    }

    private static final Map<String, StormlandsNpcRole> BY_ID = index();
    private static final List<StormlandsNpcRole> SPAWNER_ORDER = List.of(values());

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

    StormlandsNpcRole(String id, String displayName, Gender gender, Combat combat, Trade trade,
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
    public boolean ordinaryCivilian() { return this == STORMLANDS_MAN; }
    public boolean activeCombatant() { return combat != Combat.PASSIVE; }

    public static StormlandsNpcRole byId(String id) {
        if (id == null) return STORMLANDS_MAN;
        return BY_ID.getOrDefault(id.toLowerCase(Locale.ROOT), STORMLANDS_MAN);
    }

    public static StormlandsNpcRole findById(String id) {
        return id == null ? null : BY_ID.get(id.toLowerCase(Locale.ROOT));
    }

    public static List<StormlandsNpcRole> spawnerOrder() { return SPAWNER_ORDER; }

    private static Map<String, StormlandsNpcRole> index() {
        Map<String, StormlandsNpcRole> roles = new LinkedHashMap<>();
        for (StormlandsNpcRole role : values()) roles.put(role.id, role);
        return Map.copyOf(roles);
    }
}
