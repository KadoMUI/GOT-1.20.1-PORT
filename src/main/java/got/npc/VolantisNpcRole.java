package got.npc;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Complete Volantene civic and military catalogue recovered from 1.7.10.
 */
public enum VolantisNpcRole {
    VOLANTIS_MAN("volantis_man", "Volantis Man", Gender.RANDOM, Combat.PASSIVE, Trade.NONE, 1, false, null, "", "", 1.0F, 1.0F),
    VOLANTIS_LEVYMAN("volantis_levyman", "Volantis Levyman", Gender.MALE, Combat.MELEE, Trade.NONE, 2, false, null, "", "", 1.0F, 1.0F),
    VOLANTIS_LEVYMAN_ARCHER("volantis_levyman_archer", "Volantis Levyman Archer", Gender.MALE, Combat.ARCHER, Trade.NONE, 2, false, null, "", "", 1.0F, 1.0F),
    VOLANTIS_SOLDIER("volantis_soldier", "Volantis Warrior", Gender.MALE, Combat.MELEE, Trade.NONE, 2, false, null, "", "", 1.0F, 1.0F),
    VOLANTIS_SOLDIER_ARCHER("volantis_soldier_archer", "Volantis Warrior Archer", Gender.MALE, Combat.ARCHER, Trade.NONE, 2, false, null, "", "", 1.0F, 1.0F),
    VOLANTIS_BANNER_BEARER("volantis_banner_bearer", "Volantis Banner Bearer", Gender.MALE, Combat.MELEE, Trade.NONE, 2, false, null, "", "", 1.0F, 1.0F),
    VOLANTIS_CAPTAIN("volantis_captain", "Volantis Captain", Gender.MALE, Combat.MELEE, Trade.UNITS, 5, false, null, "", "", 1.0F, 1.0F),
    VOLANTIS_BLACKSMITH("volantis_blacksmith", "Volantis Blacksmith", Gender.MALE, Combat.PASSIVE, Trade.BLACKSMITH, 2, false, null, "", "", 1.0F, 1.0F),
    VOLANTIS_GOLDSMITH("volantis_goldsmith", "Volantis Goldsmith", Gender.RANDOM, Combat.PASSIVE, Trade.GOLDSMITH, 2, false, null, "", "", 1.0F, 1.0F),
    VOLANTIS_SLAVER("volantis_slaver", "Volantis Slaver", Gender.RANDOM, Combat.PASSIVE, Trade.SLAVER, 2, false, null, "", "", 1.0F, 1.0F),
    VOLANTIS_SLAVE("volantis_slave", "Volantis Slave", Gender.RANDOM, Combat.PASSIVE, Trade.NONE, 1, false, null, "", "", 1.0F, 1.0F),
    VOLANTIS_BARTENDER("volantis_bartender", "Volantis Bartender", Gender.MALE, Combat.PASSIVE, Trade.BARTENDER, 2, false, null, "", "", 1.0F, 1.0F),
    VOLANTIS_MINER("volantis_miner", "Volantis Miner", Gender.RANDOM, Combat.PASSIVE, Trade.MINER, 2, false, null, "", "", 1.0F, 1.0F),
    VOLANTIS_LUMBERMAN("volantis_lumberman", "Volantis Lumberman", Gender.RANDOM, Combat.PASSIVE, Trade.LUMBERMAN, 2, false, null, "", "", 1.0F, 1.0F),
    VOLANTIS_MASON("volantis_mason", "Volantis Mason", Gender.RANDOM, Combat.PASSIVE, Trade.MASON, 2, false, null, "", "", 1.0F, 1.0F),
    VOLANTIS_BREWER("volantis_brewer", "Volantis Brewer", Gender.RANDOM, Combat.PASSIVE, Trade.BREWER, 2, false, null, "", "", 1.0F, 1.0F),
    VOLANTIS_FLORIST("volantis_florist", "Volantis Florist", Gender.RANDOM, Combat.PASSIVE, Trade.FLORIST, 2, false, null, "", "", 1.0F, 1.0F),
    VOLANTIS_BUTCHER("volantis_butcher", "Volantis Butcher", Gender.RANDOM, Combat.PASSIVE, Trade.BUTCHER, 2, false, null, "", "", 1.0F, 1.0F),
    VOLANTIS_FISHMONGER("volantis_fishmonger", "Volantis Fishmonger", Gender.RANDOM, Combat.PASSIVE, Trade.FISHMONGER, 2, false, null, "", "", 1.0F, 1.0F),
    VOLANTIS_BAKER("volantis_baker", "Volantis Baker", Gender.RANDOM, Combat.PASSIVE, Trade.BAKER, 2, false, null, "", "", 1.0F, 1.0F);

    public enum Gender { RANDOM, MALE, FEMALE }
    public enum Combat { PASSIVE, MELEE, ARCHER, HYBRID }
    public enum Trade {
        NONE, BAKER, BARTENDER, BLACKSMITH, BREWER, BUTCHER, FARMER,
        FISHMONGER, FLORIST, GOLDSMITH, LUMBERMAN, MASON, MINER,
        UNITS, MAESTER, SLAVER
    }

    private static final Map<String, VolantisNpcRole> BY_ID = index();
    private static final List<VolantisNpcRole> SPAWNER_ORDER = List.of(values());

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

    VolantisNpcRole(String id, String displayName, Gender gender, Combat combat, Trade trade,
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
    public boolean ordinaryCivilian() { return this == VOLANTIS_MAN || this == VOLANTIS_SLAVE; }
    public boolean activeCombatant() { return combat != Combat.PASSIVE; }

    public static VolantisNpcRole byId(String id) {
        if (id == null) return VOLANTIS_MAN;
        return BY_ID.getOrDefault(id.toLowerCase(Locale.ROOT), VOLANTIS_MAN);
    }

    public static VolantisNpcRole findById(String id) {
        return id == null ? null : BY_ID.get(id.toLowerCase(Locale.ROOT));
    }

    public static List<VolantisNpcRole> spawnerOrder() { return SPAWNER_ORDER; }

    private static Map<String, VolantisNpcRole> index() {
        Map<String, VolantisNpcRole> roles = new LinkedHashMap<>();
        for (VolantisNpcRole role : values()) roles.put(role.id, role);
        return Map.copyOf(roles);
    }
}
