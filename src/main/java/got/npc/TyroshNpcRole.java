package got.npc;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Complete Tyroshi civic, military, and fixed-character catalogue recovered
 * from 1.7.10.
 */
public enum TyroshNpcRole {
    TYROSH_MAN("tyrosh_man", "Tyrosh Man", Gender.RANDOM, Combat.PASSIVE, Trade.NONE, 1, false, null, "", "", 1.0F, 1.0F),
    TYROSH_LEVYMAN("tyrosh_levyman", "Tyrosh Levyman", Gender.MALE, Combat.MELEE, Trade.NONE, 2, false, null, "", "", 1.0F, 1.0F),
    TYROSH_LEVYMAN_ARCHER("tyrosh_levyman_archer", "Tyrosh Levyman Archer", Gender.MALE, Combat.ARCHER, Trade.NONE, 2, false, null, "", "", 1.0F, 1.0F),
    TYROSH_SOLDIER("tyrosh_soldier", "Tyrosh Warrior", Gender.MALE, Combat.MELEE, Trade.NONE, 2, false, null, "", "", 1.0F, 1.0F),
    TYROSH_SOLDIER_ARCHER("tyrosh_soldier_archer", "Tyrosh Warrior Archer", Gender.MALE, Combat.ARCHER, Trade.NONE, 2, false, null, "", "", 1.0F, 1.0F),
    TYROSH_BANNER_BEARER("tyrosh_banner_bearer", "Tyrosh Banner Bearer", Gender.MALE, Combat.MELEE, Trade.NONE, 2, false, null, "", "", 1.0F, 1.0F),
    TYROSH_CAPTAIN("tyrosh_captain", "Tyrosh Captain", Gender.MALE, Combat.MELEE, Trade.UNITS, 5, false, null, "", "", 1.0F, 1.0F),
    TYROSH_BLACKSMITH("tyrosh_blacksmith", "Tyrosh Blacksmith", Gender.MALE, Combat.PASSIVE, Trade.BLACKSMITH, 2, false, null, "", "", 1.0F, 1.0F),
    TYROSH_GOLDSMITH("tyrosh_goldsmith", "Tyrosh Goldsmith", Gender.RANDOM, Combat.PASSIVE, Trade.GOLDSMITH, 2, false, null, "", "", 1.0F, 1.0F),
    TYROSH_SLAVER("tyrosh_slaver", "Tyrosh Slaver", Gender.RANDOM, Combat.PASSIVE, Trade.SLAVER, 2, false, null, "", "", 1.0F, 1.0F),
    TYROSH_SLAVE("tyrosh_slave", "Tyrosh Slave", Gender.RANDOM, Combat.PASSIVE, Trade.NONE, 1, false, null, "", "", 1.0F, 1.0F),
    TYROSH_BARTENDER("tyrosh_bartender", "Tyrosh Bartender", Gender.MALE, Combat.PASSIVE, Trade.BARTENDER, 2, false, null, "", "", 1.0F, 1.0F),
    TYROSH_MINER("tyrosh_miner", "Tyrosh Miner", Gender.RANDOM, Combat.PASSIVE, Trade.MINER, 2, false, null, "", "", 1.0F, 1.0F),
    TYROSH_LUMBERMAN("tyrosh_lumberman", "Tyrosh Lumberman", Gender.RANDOM, Combat.PASSIVE, Trade.LUMBERMAN, 2, false, null, "", "", 1.0F, 1.0F),
    TYROSH_MASON("tyrosh_mason", "Tyrosh Mason", Gender.RANDOM, Combat.PASSIVE, Trade.MASON, 2, false, null, "", "", 1.0F, 1.0F),
    TYROSH_BREWER("tyrosh_brewer", "Tyrosh Brewer", Gender.RANDOM, Combat.PASSIVE, Trade.BREWER, 2, false, null, "", "", 1.0F, 1.0F),
    TYROSH_FLORIST("tyrosh_florist", "Tyrosh Florist", Gender.RANDOM, Combat.PASSIVE, Trade.FLORIST, 2, false, null, "", "", 1.0F, 1.0F),
    TYROSH_BUTCHER("tyrosh_butcher", "Tyrosh Butcher", Gender.RANDOM, Combat.PASSIVE, Trade.BUTCHER, 2, false, null, "", "", 1.0F, 1.0F),
    TYROSH_FISHMONGER("tyrosh_fishmonger", "Tyrosh Fishmonger", Gender.RANDOM, Combat.PASSIVE, Trade.FISHMONGER, 2, false, null, "", "", 1.0F, 1.0F),
    TYROSH_BAKER("tyrosh_baker", "Tyrosh Baker", Gender.RANDOM, Combat.PASSIVE, Trade.BAKER, 2, false, null, "", "", 1.0F, 1.0F),

    JON_CONNINGTON("jon_connington", "Jon Connington", Gender.MALE, Combat.MELEE,
            Trade.NONE, 1, true, "jon_connington", "", "", 1.0F, 1.0F),
    YOUNG_GRIFF("young_griff", "Young Griff", Gender.MALE, Combat.MELEE,
            Trade.NONE, 500, true, "young_griff", "", "", 0.9F, 0.9F);

    public enum Gender { RANDOM, MALE, FEMALE }
    public enum Combat { PASSIVE, MELEE, ARCHER, HYBRID }
    public enum Trade {
        NONE, BAKER, BARTENDER, BLACKSMITH, BREWER, BUTCHER, FARMER,
        FISHMONGER, FLORIST, GOLDSMITH, LUMBERMAN, MASON, MINER,
        UNITS, MAESTER, SLAVER
    }

    private static final Map<String, TyroshNpcRole> BY_ID = index();
    private static final List<TyroshNpcRole> SPAWNER_ORDER = List.of(values());

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

    TyroshNpcRole(String id, String displayName, Gender gender, Combat combat, Trade trade,
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
    public boolean ordinaryCivilian() { return this == TYROSH_MAN || this == TYROSH_SLAVE; }
    public boolean activeCombatant() { return combat != Combat.PASSIVE; }

    public static TyroshNpcRole byId(String id) {
        if (id == null) return TYROSH_MAN;
        return BY_ID.getOrDefault(id.toLowerCase(Locale.ROOT), TYROSH_MAN);
    }

    public static TyroshNpcRole findById(String id) {
        return id == null ? null : BY_ID.get(id.toLowerCase(Locale.ROOT));
    }

    public static List<TyroshNpcRole> spawnerOrder() { return SPAWNER_ORDER; }

    private static Map<String, TyroshNpcRole> index() {
        Map<String, TyroshNpcRole> roles = new LinkedHashMap<>();
        for (TyroshNpcRole role : values()) roles.put(role.id, role);
        return Map.copyOf(roles);
    }
}
