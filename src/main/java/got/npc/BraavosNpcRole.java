package got.npc;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Complete Braavosi civic and military catalogue recovered from 1.7.10.
 */
public enum BraavosNpcRole {
    BRAAVOS_MAN("braavos_man", "Braavos Man", Gender.RANDOM, Combat.PASSIVE, Trade.NONE, 1, false, null, "", "", 1.0F, 1.0F),
    BRAAVOS_LEVYMAN("braavos_levyman", "Braavos Levyman", Gender.MALE, Combat.MELEE, Trade.NONE, 2, false, null, "", "", 1.0F, 1.0F),
    BRAAVOS_LEVYMAN_ARCHER("braavos_levyman_archer", "Braavos Levyman Archer", Gender.MALE, Combat.ARCHER, Trade.NONE, 2, false, null, "", "", 1.0F, 1.0F),
    BRAAVOS_SOLDIER("braavos_soldier", "Braavos Warrior", Gender.MALE, Combat.MELEE, Trade.NONE, 2, false, null, "", "", 1.0F, 1.0F),
    BRAAVOS_SOLDIER_ARCHER("braavos_soldier_archer", "Braavos Warrior Archer", Gender.MALE, Combat.ARCHER, Trade.NONE, 2, false, null, "", "", 1.0F, 1.0F),
    BRAAVOS_BANNER_BEARER("braavos_banner_bearer", "Braavos Banner Bearer", Gender.MALE, Combat.MELEE, Trade.NONE, 2, false, null, "", "", 1.0F, 1.0F),
    BRAAVOS_CAPTAIN("braavos_captain", "Braavos Captain", Gender.MALE, Combat.MELEE, Trade.UNITS, 5, false, null, "", "", 1.0F, 1.0F),
    BRAAVOS_BLACKSMITH("braavos_blacksmith", "Braavos Blacksmith", Gender.MALE, Combat.PASSIVE, Trade.BLACKSMITH, 2, false, null, "", "", 1.0F, 1.0F),
    BRAAVOS_GOLDSMITH("braavos_goldsmith", "Braavos Goldsmith", Gender.RANDOM, Combat.PASSIVE, Trade.GOLDSMITH, 2, false, null, "", "", 1.0F, 1.0F),
    BRAAVOS_FARMER("braavos_farmer", "Braavos Farmer", Gender.MALE, Combat.PASSIVE, Trade.FARMER, 2, false, null, "", "", 1.0F, 1.0F),
    BRAAVOS_FARMHAND("braavos_farmhand", "Braavos Farmhand", Gender.RANDOM, Combat.PASSIVE, Trade.NONE, 1, false, null, "", "", 1.0F, 1.0F),
    BRAAVOS_BARTENDER("braavos_bartender", "Braavos Bartender", Gender.MALE, Combat.PASSIVE, Trade.BARTENDER, 2, false, null, "", "", 1.0F, 1.0F),
    BRAAVOS_MINER("braavos_miner", "Braavos Miner", Gender.RANDOM, Combat.PASSIVE, Trade.MINER, 2, false, null, "", "", 1.0F, 1.0F),
    BRAAVOS_LUMBERMAN("braavos_lumberman", "Braavos Lumberman", Gender.RANDOM, Combat.PASSIVE, Trade.LUMBERMAN, 2, false, null, "", "", 1.0F, 1.0F),
    BRAAVOS_MASON("braavos_mason", "Braavos Mason", Gender.RANDOM, Combat.PASSIVE, Trade.MASON, 2, false, null, "", "", 1.0F, 1.0F),
    BRAAVOS_BREWER("braavos_brewer", "Braavos Brewer", Gender.RANDOM, Combat.PASSIVE, Trade.BREWER, 2, false, null, "", "", 1.0F, 1.0F),
    BRAAVOS_FLORIST("braavos_florist", "Braavos Florist", Gender.RANDOM, Combat.PASSIVE, Trade.FLORIST, 2, false, null, "", "", 1.0F, 1.0F),
    BRAAVOS_BUTCHER("braavos_butcher", "Braavos Butcher", Gender.RANDOM, Combat.PASSIVE, Trade.BUTCHER, 2, false, null, "", "", 1.0F, 1.0F),
    BRAAVOS_FISHMONGER("braavos_fishmonger", "Braavos Fishmonger", Gender.RANDOM, Combat.PASSIVE, Trade.FISHMONGER, 2, false, null, "", "", 1.0F, 1.0F),
    BRAAVOS_BAKER("braavos_baker", "Braavos Baker", Gender.RANDOM, Combat.PASSIVE, Trade.BAKER, 2, false, null, "", "", 1.0F, 1.0F),

    TYCHO_NESTORIS("tycho_nestoris", "Tycho Nestoris", Gender.MALE, Combat.PASSIVE,
            Trade.GOLDSMITH, 300, true, "tycho_nestoris", "", "", 1.0F, 1.0F);

    public enum Gender { RANDOM, MALE, FEMALE }
    public enum Combat { PASSIVE, MELEE, ARCHER, HYBRID }
    public enum Trade {
        NONE, BAKER, BARTENDER, BLACKSMITH, BREWER, BUTCHER, FARMER,
        FISHMONGER, FLORIST, GOLDSMITH, LUMBERMAN, MASON, MINER,
        UNITS, MAESTER
    }

    private static final Map<String, BraavosNpcRole> BY_ID = index();
    private static final List<BraavosNpcRole> SPAWNER_ORDER = List.of(values());

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

    BraavosNpcRole(String id, String displayName, Gender gender, Combat combat, Trade trade,
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
    public boolean ordinaryCivilian() { return this == BRAAVOS_MAN; }
    public boolean activeCombatant() { return combat != Combat.PASSIVE; }

    public static BraavosNpcRole byId(String id) {
        if (id == null) return BRAAVOS_MAN;
        return BY_ID.getOrDefault(id.toLowerCase(Locale.ROOT), BRAAVOS_MAN);
    }

    public static BraavosNpcRole findById(String id) {
        return id == null ? null : BY_ID.get(id.toLowerCase(Locale.ROOT));
    }

    public static List<BraavosNpcRole> spawnerOrder() { return SPAWNER_ORDER; }

    private static Map<String, BraavosNpcRole> index() {
        Map<String, BraavosNpcRole> roles = new LinkedHashMap<>();
        for (BraavosNpcRole role : values()) roles.put(role.id, role);
        return Map.copyOf(roles);
    }
}
