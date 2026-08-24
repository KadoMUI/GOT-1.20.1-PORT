package got.npc;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Dragonstone population catalogue recovered from the original 1.7.10 entity
 * hierarchy and its fixed-waypoint registrations.
 */
public enum DragonstoneNpcRole {
    DRAGONSTONE_MAN("dragonstone_man", "Dragonstone Civilian", Gender.RANDOM, Combat.PASSIVE, Trade.NONE, 1, false, null, "", "", 1.0F, 1.0F),
    DRAGONSTONE_LEVYMAN("dragonstone_levyman", "Dragonstone Levyman", Gender.MALE, Combat.MELEE, Trade.NONE, 2, false, null, "", "", 1.0F, 1.0F),
    DRAGONSTONE_LEVYMAN_ARCHER("dragonstone_levyman_archer", "Dragonstone Levyman Archer", Gender.MALE, Combat.ARCHER, Trade.NONE, 2, false, null, "", "", 1.0F, 1.0F),
    DRAGONSTONE_SOLDIER("dragonstone_soldier", "Dragonstone Soldier", Gender.MALE, Combat.MELEE, Trade.NONE, 2, false, null, "", "", 1.0F, 1.0F),
    DRAGONSTONE_SOLDIER_ARCHER("dragonstone_soldier_archer", "Dragonstone Soldier Archer", Gender.MALE, Combat.ARCHER, Trade.NONE, 2, false, null, "", "", 1.0F, 1.0F),
    DRAGONSTONE_BANNER_BEARER("dragonstone_banner_bearer", "Dragonstone Banner Bearer", Gender.MALE, Combat.MELEE, Trade.NONE, 2, false, null, "", "", 1.0F, 1.0F),
    DRAGONSTONE_CAPTAIN("dragonstone_captain", "Dragonstone Captain", Gender.MALE, Combat.MELEE, Trade.UNITS, 5, false, null, "", "", 1.0F, 1.0F),
    DRAGONSTONE_BLACKSMITH("dragonstone_blacksmith", "Dragonstone Blacksmith", Gender.MALE, Combat.PASSIVE, Trade.BLACKSMITH, 2, false, null, "", "", 1.0F, 1.0F),
    DRAGONSTONE_GOLDSMITH("dragonstone_goldsmith", "Dragonstone Goldsmith", Gender.RANDOM, Combat.PASSIVE, Trade.GOLDSMITH, 2, false, null, "", "", 1.0F, 1.0F),
    DRAGONSTONE_FARMER("dragonstone_farmer", "Dragonstone Farmer", Gender.MALE, Combat.PASSIVE, Trade.FARMER, 2, false, null, "", "", 1.0F, 1.0F),
    DRAGONSTONE_FARMHAND("dragonstone_farmhand", "Dragonstone Farmhand", Gender.RANDOM, Combat.PASSIVE, Trade.NONE, 1, false, null, "", "", 1.0F, 1.0F),
    DRAGONSTONE_BARTENDER("dragonstone_bartender", "Dragonstone Bartender", Gender.MALE, Combat.PASSIVE, Trade.BARTENDER, 2, false, null, "", "", 1.0F, 1.0F),
    DRAGONSTONE_MINER("dragonstone_miner", "Dragonstone Miner", Gender.RANDOM, Combat.PASSIVE, Trade.MINER, 2, false, null, "", "", 1.0F, 1.0F),
    DRAGONSTONE_LUMBERMAN("dragonstone_lumberman", "Dragonstone Lumberman", Gender.RANDOM, Combat.PASSIVE, Trade.LUMBERMAN, 2, false, null, "", "", 1.0F, 1.0F),
    DRAGONSTONE_MASON("dragonstone_mason", "Dragonstone Mason", Gender.RANDOM, Combat.PASSIVE, Trade.MASON, 2, false, null, "", "", 1.0F, 1.0F),
    DRAGONSTONE_BREWER("dragonstone_brewer", "Dragonstone Brewer", Gender.RANDOM, Combat.PASSIVE, Trade.BREWER, 2, false, null, "", "", 1.0F, 1.0F),
    DRAGONSTONE_FLORIST("dragonstone_florist", "Dragonstone Florist", Gender.RANDOM, Combat.PASSIVE, Trade.FLORIST, 2, false, null, "", "", 1.0F, 1.0F),
    DRAGONSTONE_BUTCHER("dragonstone_butcher", "Dragonstone Butcher", Gender.RANDOM, Combat.PASSIVE, Trade.BUTCHER, 2, false, null, "", "", 1.0F, 1.0F),
    DRAGONSTONE_FISHMONGER("dragonstone_fishmonger", "Dragonstone Fishmonger", Gender.RANDOM, Combat.PASSIVE, Trade.FISHMONGER, 2, false, null, "", "", 1.0F, 1.0F),
    DRAGONSTONE_BAKER("dragonstone_baker", "Dragonstone Baker", Gender.RANDOM, Combat.PASSIVE, Trade.BAKER, 2, false, null, "", "", 1.0F, 1.0F),

    ARDRIAN_CELTIGAR("ardrian_celtigar", "Ardrian Celtigar", Gender.MALE, Combat.MELEE, Trade.UNITS, 100, true, "ardrian_celtigar", "", "", 1.0F, 1.0F),
    STANNIS_BARATHEON("stannis_baratheon", "Stannis Baratheon", Gender.MALE, Combat.MELEE, Trade.NONE, 500, true, "stannis_baratheon", "", "", 1.0F, 1.0F),
    DAVOS_SEAWORTH("davos_seaworth", "Davos Seaworth", Gender.MALE, Combat.MELEE, Trade.UNITS, 300, true, "davos_seaworth", "_1", "_2", 1.0F, 1.0F),
    MELISANDRA("melisandra", "Melisandra", Gender.FEMALE, Combat.PASSIVE, Trade.NONE, 300, true, "melisandra", "", "", 1.0F, 1.0F),
    SHIREEN_BARATHEON("shireen_baratheon", "Shireen Baratheon", Gender.FEMALE, Combat.PASSIVE, Trade.NONE, 500, true, "shireen_baratheon", "", "", 0.75F, 0.75F),
    SELYSE_BARATHEON("selyse_baratheon", "Selyse Baratheon", Gender.FEMALE, Combat.MELEE, Trade.NONE, 500, true, "selyse_baratheon", "", "", 1.0F, 1.0F),
    MATTHOS_SEAWORTH("matthos_seaworth", "Matthos Seaworth", Gender.MALE, Combat.MELEE, Trade.NONE, 100, true, "matthos_seaworth", "_1", "_2", 1.0F, 1.0F),
    MONFORD_VELARYON("monford_velaryon", "Monford Velaryon", Gender.MALE, Combat.MELEE, Trade.UNITS, 100, true, "monford_velaryon", "", "", 1.0F, 1.0F),
    AURANE_WATERS("aurane_waters", "Aurane Waters", Gender.MALE, Combat.MELEE, Trade.NONE, 100, true, "aurane_waters", "", "", 1.0F, 1.0F);

    public enum Gender { RANDOM, MALE, FEMALE }
    public enum Combat { PASSIVE, MELEE, ARCHER, HYBRID }
    public enum Trade {
        NONE, BAKER, BARTENDER, BLACKSMITH, BREWER, BUTCHER, FARMER,
        FISHMONGER, FLORIST, GOLDSMITH, LUMBERMAN, MASON, MINER,
        UNITS, MAESTER
    }

    private static final Map<String, DragonstoneNpcRole> BY_ID = index();
    private static final List<DragonstoneNpcRole> SPAWNER_ORDER = List.of(values());

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

    DragonstoneNpcRole(String id, String displayName, Gender gender, Combat combat, Trade trade,
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
    public boolean ordinaryCivilian() { return this == DRAGONSTONE_MAN; }
    public boolean activeCombatant() { return combat != Combat.PASSIVE; }

    public static DragonstoneNpcRole byId(String id) {
        if (id == null) return DRAGONSTONE_MAN;
        return BY_ID.getOrDefault(id.toLowerCase(Locale.ROOT), DRAGONSTONE_MAN);
    }

    public static DragonstoneNpcRole findById(String id) {
        return id == null ? null : BY_ID.get(id.toLowerCase(Locale.ROOT));
    }

    public static List<DragonstoneNpcRole> spawnerOrder() { return SPAWNER_ORDER; }

    private static Map<String, DragonstoneNpcRole> index() {
        Map<String, DragonstoneNpcRole> roles = new LinkedHashMap<>();
        for (DragonstoneNpcRole role : values()) roles.put(role.id, role);
        return Map.copyOf(roles);
    }
}
