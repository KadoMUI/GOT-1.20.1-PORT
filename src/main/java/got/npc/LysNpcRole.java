package got.npc;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Complete Lysene civic, military, and fixed-character catalogue recovered
 * from 1.7.10.
 */
public enum LysNpcRole {
    LYS_MAN("lys_man", "Lys Man", Gender.RANDOM, Combat.PASSIVE, Trade.NONE, 1, false, null, "", "", 1.0F, 1.0F),
    LYS_LEVYMAN("lys_levyman", "Lys Levyman", Gender.MALE, Combat.MELEE, Trade.NONE, 2, false, null, "", "", 1.0F, 1.0F),
    LYS_LEVYMAN_ARCHER("lys_levyman_archer", "Lys Levyman Archer", Gender.MALE, Combat.ARCHER, Trade.NONE, 2, false, null, "", "", 1.0F, 1.0F),
    LYS_SOLDIER("lys_soldier", "Lys Warrior", Gender.MALE, Combat.MELEE, Trade.NONE, 2, false, null, "", "", 1.0F, 1.0F),
    LYS_SOLDIER_ARCHER("lys_soldier_archer", "Lys Warrior Archer", Gender.MALE, Combat.ARCHER, Trade.NONE, 2, false, null, "", "", 1.0F, 1.0F),
    LYS_BANNER_BEARER("lys_banner_bearer", "Lys Banner Bearer", Gender.MALE, Combat.MELEE, Trade.NONE, 2, false, null, "", "", 1.0F, 1.0F),
    LYS_CAPTAIN("lys_captain", "Lys Captain", Gender.MALE, Combat.MELEE, Trade.UNITS, 5, false, null, "", "", 1.0F, 1.0F),
    LYS_BLACKSMITH("lys_blacksmith", "Lys Blacksmith", Gender.MALE, Combat.PASSIVE, Trade.BLACKSMITH, 2, false, null, "", "", 1.0F, 1.0F),
    LYS_GOLDSMITH("lys_goldsmith", "Lys Goldsmith", Gender.RANDOM, Combat.PASSIVE, Trade.GOLDSMITH, 2, false, null, "", "", 1.0F, 1.0F),
    LYS_SLAVER("lys_slaver", "Lys Slaver", Gender.RANDOM, Combat.PASSIVE, Trade.SLAVER, 2, false, null, "", "", 1.0F, 1.0F),
    LYS_SLAVE("lys_slave", "Lys Slave", Gender.RANDOM, Combat.PASSIVE, Trade.NONE, 1, false, null, "", "", 1.0F, 1.0F),
    LYS_BARTENDER("lys_bartender", "Lys Bartender", Gender.MALE, Combat.PASSIVE, Trade.BARTENDER, 2, false, null, "", "", 1.0F, 1.0F),
    LYS_MINER("lys_miner", "Lys Miner", Gender.RANDOM, Combat.PASSIVE, Trade.MINER, 2, false, null, "", "", 1.0F, 1.0F),
    LYS_LUMBERMAN("lys_lumberman", "Lys Lumberman", Gender.RANDOM, Combat.PASSIVE, Trade.LUMBERMAN, 2, false, null, "", "", 1.0F, 1.0F),
    LYS_MASON("lys_mason", "Lys Mason", Gender.RANDOM, Combat.PASSIVE, Trade.MASON, 2, false, null, "", "", 1.0F, 1.0F),
    LYS_BREWER("lys_brewer", "Lys Brewer", Gender.RANDOM, Combat.PASSIVE, Trade.BREWER, 2, false, null, "", "", 1.0F, 1.0F),
    LYS_FLORIST("lys_florist", "Lys Florist", Gender.RANDOM, Combat.PASSIVE, Trade.FLORIST, 2, false, null, "", "", 1.0F, 1.0F),
    LYS_BUTCHER("lys_butcher", "Lys Butcher", Gender.RANDOM, Combat.PASSIVE, Trade.BUTCHER, 2, false, null, "", "", 1.0F, 1.0F),
    LYS_FISHMONGER("lys_fishmonger", "Lys Fishmonger", Gender.RANDOM, Combat.PASSIVE, Trade.FISHMONGER, 2, false, null, "", "", 1.0F, 1.0F),
    LYS_BAKER("lys_baker", "Lys Baker", Gender.RANDOM, Combat.PASSIVE, Trade.BAKER, 2, false, null, "", "", 1.0F, 1.0F),

    SALLADHOR_SAAN("salladhor_saan", "Salladhor Saan", Gender.MALE, Combat.MELEE,
            Trade.UNITS, 100, true, "salladhor_saan", "", "", 1.0F, 1.0F);

    public enum Gender { RANDOM, MALE, FEMALE }
    public enum Combat { PASSIVE, MELEE, ARCHER, HYBRID }
    public enum Trade {
        NONE, BAKER, BARTENDER, BLACKSMITH, BREWER, BUTCHER, FARMER,
        FISHMONGER, FLORIST, GOLDSMITH, LUMBERMAN, MASON, MINER,
        UNITS, MAESTER, SLAVER
    }

    private static final Map<String, LysNpcRole> BY_ID = index();
    private static final List<LysNpcRole> SPAWNER_ORDER = List.of(values());

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

    LysNpcRole(String id, String displayName, Gender gender, Combat combat, Trade trade,
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
    public boolean ordinaryCivilian() { return this == LYS_MAN || this == LYS_SLAVE; }
    public boolean activeCombatant() { return combat != Combat.PASSIVE; }

    public static LysNpcRole byId(String id) {
        if (id == null) return LYS_MAN;
        return BY_ID.getOrDefault(id.toLowerCase(Locale.ROOT), LYS_MAN);
    }

    public static LysNpcRole findById(String id) {
        return id == null ? null : BY_ID.get(id.toLowerCase(Locale.ROOT));
    }

    public static List<LysNpcRole> spawnerOrder() { return SPAWNER_ORDER; }

    private static Map<String, LysNpcRole> index() {
        Map<String, LysNpcRole> roles = new LinkedHashMap<>();
        for (LysNpcRole role : values()) roles.put(role.id, role);
        return Map.copyOf(roles);
    }
}
