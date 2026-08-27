package got.npc;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/** Complete Qarthi civic and military catalogue from the final 1.7.10 build. */
public enum QarthNpcRole {
    QARTH_MAN("qarth_man", "Qarth Man", Gender.RANDOM, Combat.PASSIVE, Trade.NONE, 1),
    QARTH_LEVYMAN("qarth_levyman", "Qarth Levyman", Gender.MALE, Combat.MELEE, Trade.NONE, 2),
    QARTH_LEVYMAN_ARCHER("qarth_levyman_archer", "Qarth Levyman Archer", Gender.MALE, Combat.ARCHER, Trade.NONE, 2),
    QARTH_SOLDIER("qarth_soldier", "Qarth Warrior", Gender.MALE, Combat.MELEE, Trade.NONE, 2),
    QARTH_SOLDIER_ARCHER("qarth_soldier_archer", "Qarth Warrior Archer", Gender.MALE, Combat.ARCHER, Trade.NONE, 2),
    QARTH_BANNER_BEARER("qarth_banner_bearer", "Qarth Banner Bearer", Gender.MALE, Combat.MELEE, Trade.NONE, 2),
    QARTH_CAPTAIN("qarth_captain", "Qarth Captain", Gender.MALE, Combat.MELEE, Trade.UNITS, 5),
    QARTH_BLACKSMITH("qarth_blacksmith", "Qarth Blacksmith", Gender.MALE, Combat.PASSIVE, Trade.BLACKSMITH, 2),
    QARTH_GOLDSMITH("qarth_goldsmith", "Qarth Goldsmith", Gender.RANDOM, Combat.PASSIVE, Trade.GOLDSMITH, 2),
    QARTH_FARMER("qarth_farmer", "Qarth Farmer", Gender.MALE, Combat.PASSIVE, Trade.FARMER, 2),
    QARTH_FARMHAND("qarth_farmhand", "Qarth Farmhand", Gender.RANDOM, Combat.PASSIVE, Trade.NONE, 1),
    QARTH_BARTENDER("qarth_bartender", "Qarth Bartender", Gender.MALE, Combat.PASSIVE, Trade.BARTENDER, 2),
    QARTH_MINER("qarth_miner", "Qarth Miner", Gender.RANDOM, Combat.PASSIVE, Trade.MINER, 2),
    QARTH_LUMBERMAN("qarth_lumberman", "Qarth Lumberman", Gender.RANDOM, Combat.PASSIVE, Trade.LUMBERMAN, 2),
    QARTH_MASON("qarth_mason", "Qarth Mason", Gender.RANDOM, Combat.PASSIVE, Trade.MASON, 2),
    QARTH_BREWER("qarth_brewer", "Qarth Brewer", Gender.RANDOM, Combat.PASSIVE, Trade.BREWER, 2),
    QARTH_FLORIST("qarth_florist", "Qarth Florist", Gender.RANDOM, Combat.PASSIVE, Trade.FLORIST, 2),
    QARTH_BUTCHER("qarth_butcher", "Qarth Butcher", Gender.RANDOM, Combat.PASSIVE, Trade.BUTCHER, 2),
    QARTH_FISHMONGER("qarth_fishmonger", "Qarth Fishmonger", Gender.RANDOM, Combat.PASSIVE, Trade.FISHMONGER, 2),
    QARTH_BAKER("qarth_baker", "Qarth Baker", Gender.RANDOM, Combat.PASSIVE, Trade.BAKER, 2),
    QARTH_WARLOCK("qarth_warlock", "Qarth Warlock", Gender.MALE, Combat.PASSIVE, Trade.ALCHEMIST, 2),
    XARO_XHOAN_DAXOS("xaro_xhoan_daxos", "Xaro Xhoan Daxos", Gender.MALE, Combat.PASSIVE, Trade.GOLDSMITH, 500, true, "xaro_xhoan_daxos", "_1", "_2", 1.0F, 1.0F);

    public enum Gender { RANDOM, MALE, FEMALE }
    public enum Combat { PASSIVE, MELEE, ARCHER, HYBRID }
    public enum Trade { NONE, ALCHEMIST, BAKER, BARTENDER, BLACKSMITH, BREWER, BUTCHER, FARMER, FISHMONGER, FLORIST, GOLDSMITH, LUMBERMAN, MASON, MINER, UNITS }

    private static final Map<String, QarthNpcRole> BY_ID = index();
    private static final List<QarthNpcRole> SPAWNER_ORDER = List.of(values());
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

    QarthNpcRole(String id, String displayName, Gender gender, Combat combat, Trade trade, int alignmentBonus) {
        this(id, displayName, gender, combat, trade, alignmentBonus, false, null, "", "", 1.0F, 1.0F);
    }

    QarthNpcRole(String id, String displayName, Gender gender, Combat combat, Trade trade, int alignmentBonus,
                 boolean legendary, String legendaryTexture, String legendaryBaseSuffix,
                 String legendaryOverlaySuffix, float scale, float collisionScale) {
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
    public boolean ordinaryCivilian() { return this == QARTH_MAN || this == QARTH_FARMHAND; }
    public boolean activeCombatant() { return combat != Combat.PASSIVE; }
    public boolean usesOutfitOverlay() { return this != QARTH_SOLDIER && this != QARTH_SOLDIER_ARCHER && this != QARTH_BANNER_BEARER; }
    public static QarthNpcRole byId(String id) { return id == null ? QARTH_MAN : BY_ID.getOrDefault(id.toLowerCase(Locale.ROOT), QARTH_MAN); }
    public static QarthNpcRole findById(String id) { return id == null ? null : BY_ID.get(id.toLowerCase(Locale.ROOT)); }
    public static List<QarthNpcRole> spawnerOrder() { return SPAWNER_ORDER; }
    private static Map<String, QarthNpcRole> index() {
        Map<String, QarthNpcRole> roles = new LinkedHashMap<>();
        for (QarthNpcRole role : values()) roles.put(role.id, role);
        return Map.copyOf(roles);
    }
}
