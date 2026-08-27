package got.npc;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Arryn population catalogue recovered from the original 1.7.10 entity
 * hierarchy and its fixed-waypoint registrations.
 */
public enum ArrynNpcRole {
    ARRYN_MAN("arryn_man", "Arryn Civilian", Gender.RANDOM, Combat.PASSIVE, Trade.NONE, 1, false, null, "", "", 1.0F, 1.0F),
    ARRYN_LEVYMAN("arryn_levyman", "Arryn Levyman", Gender.MALE, Combat.MELEE, Trade.NONE, 2, false, null, "", "", 1.0F, 1.0F),
    ARRYN_LEVYMAN_ARCHER("arryn_levyman_archer", "Arryn Levyman Archer", Gender.MALE, Combat.ARCHER, Trade.NONE, 2, false, null, "", "", 1.0F, 1.0F),
    ARRYN_SOLDIER("arryn_soldier", "Arryn Soldier", Gender.MALE, Combat.MELEE, Trade.NONE, 2, false, null, "", "", 1.0F, 1.0F),
    ARRYN_SOLDIER_ARCHER("arryn_soldier_archer", "Arryn Soldier Archer", Gender.MALE, Combat.ARCHER, Trade.NONE, 2, false, null, "", "", 1.0F, 1.0F),
    ARRYN_GUARD("arryn_guard", "Gulltown Guard", Gender.MALE, Combat.MELEE, Trade.NONE, 2, false, null, "", "", 1.0F, 1.0F),
    ARRYN_BANNER_BEARER("arryn_banner_bearer", "Arryn Banner Bearer", Gender.MALE, Combat.MELEE, Trade.NONE, 2, false, null, "", "", 1.0F, 1.0F),
    ARRYN_CAPTAIN("arryn_captain", "Arryn Captain", Gender.MALE, Combat.MELEE, Trade.UNITS, 5, false, null, "", "", 1.0F, 1.0F),
    ARRYN_BLACKSMITH("arryn_blacksmith", "Arryn Blacksmith", Gender.MALE, Combat.PASSIVE, Trade.BLACKSMITH, 2, false, null, "", "", 1.0F, 1.0F),
    ARRYN_GOLDSMITH("arryn_goldsmith", "Arryn Goldsmith", Gender.RANDOM, Combat.PASSIVE, Trade.GOLDSMITH, 2, false, null, "", "", 1.0F, 1.0F),
    ARRYN_FARMER("arryn_farmer", "Arryn Farmer", Gender.MALE, Combat.PASSIVE, Trade.FARMER, 2, false, null, "", "", 1.0F, 1.0F),
    ARRYN_FARMHAND("arryn_farmhand", "Arryn Farmhand", Gender.RANDOM, Combat.PASSIVE, Trade.NONE, 1, false, null, "", "", 1.0F, 1.0F),
    ARRYN_BARTENDER("arryn_bartender", "Arryn Bartender", Gender.MALE, Combat.PASSIVE, Trade.BARTENDER, 2, false, null, "", "", 1.0F, 1.0F),
    ARRYN_MINER("arryn_miner", "Arryn Miner", Gender.RANDOM, Combat.PASSIVE, Trade.MINER, 2, false, null, "", "", 1.0F, 1.0F),
    ARRYN_LUMBERMAN("arryn_lumberman", "Arryn Lumberman", Gender.RANDOM, Combat.PASSIVE, Trade.LUMBERMAN, 2, false, null, "", "", 1.0F, 1.0F),
    ARRYN_MASON("arryn_mason", "Arryn Mason", Gender.RANDOM, Combat.PASSIVE, Trade.MASON, 2, false, null, "", "", 1.0F, 1.0F),
    ARRYN_BREWER("arryn_brewer", "Arryn Brewer", Gender.RANDOM, Combat.PASSIVE, Trade.BREWER, 2, false, null, "", "", 1.0F, 1.0F),
    ARRYN_FLORIST("arryn_florist", "Arryn Florist", Gender.RANDOM, Combat.PASSIVE, Trade.FLORIST, 2, false, null, "", "", 1.0F, 1.0F),
    ARRYN_BUTCHER("arryn_butcher", "Arryn Butcher", Gender.RANDOM, Combat.PASSIVE, Trade.BUTCHER, 2, false, null, "", "", 1.0F, 1.0F),
    ARRYN_FISHMONGER("arryn_fishmonger", "Arryn Fishmonger", Gender.RANDOM, Combat.PASSIVE, Trade.FISHMONGER, 2, false, null, "", "", 1.0F, 1.0F),
    ARRYN_BAKER("arryn_baker", "Arryn Baker", Gender.RANDOM, Combat.PASSIVE, Trade.BAKER, 2, false, null, "", "", 1.0F, 1.0F),

    // Separate HILL_TRIBES faction roster from the original Vale hillmen hierarchy.
    HILLMAN_WARRIOR("hillman_warrior", "Hillman Warrior", Gender.MALE, Combat.MELEE, Trade.NONE, 2, false, null, "", "", 1.0F, 1.0F),
    HILLMAN_ARCHER("hillman_archer", "Hillman Archer", Gender.MALE, Combat.ARCHER, Trade.NONE, 2, false, null, "", "", 1.0F, 1.0F),
    HILLMAN_AXE_THROWER("hillman_axe_thrower", "Hillman Axe Thrower", Gender.MALE, Combat.AXE_THROWER, Trade.NONE, 2, false, null, "", "", 1.0F, 1.0F),
    HILLMAN_BANNER_BEARER("hillman_banner_bearer", "Hillman Banner Bearer", Gender.MALE, Combat.MELEE, Trade.NONE, 2, false, null, "", "", 1.0F, 1.0F),
    HILLMAN_BERSERKER("hillman_berserker", "Hillman Berserker", Gender.MALE, Combat.MELEE, Trade.NONE, 2, false, null, "", "", 1.0F, 1.0F),

    // Original GOTEntityProstitute is an unaffiliated female human NPC.
    PROSTITUTE("prostitute", "Prostitute", Gender.FEMALE, Combat.PASSIVE, Trade.NONE, 0, false, null, "", "", 1.0F, 1.0F),

    GEROLD_GRAFTON("gerold_grafton", "Gerold Grafton", Gender.MALE, Combat.MELEE, Trade.UNITS, 100, true, "gerold_grafton", "", "", 1.0F, 1.0F),
    LYN_CORBRAY("lyn_corbray", "Lyn Corbray", Gender.MALE, Combat.MELEE, Trade.UNITS, 100, true, "lyn_corbray", "", "", 1.0F, 1.0F),
    HARROLD_HARDYNG("harrold_hardyng", "Harrold Hardyng", Gender.MALE, Combat.MELEE, Trade.NONE, 100, true, "harrold_hardyng", "", "", 1.0F, 1.0F),
    ANYA_WAYNWOOD("anya_waynwood", "Anya Waynwood", Gender.FEMALE, Combat.MELEE, Trade.UNITS, 100, true, "anya_waynwood", "", "", 1.0F, 1.0F),
    GILWOOD_HUNTER("gilwood_hunter", "Gilwood Hunter", Gender.MALE, Combat.MELEE, Trade.UNITS, 100, true, "gilwood_hunter", "", "", 1.0F, 1.0F),
    SYMOND_TEMPLETON("symond_templeton", "Symond Templeton", Gender.MALE, Combat.MELEE, Trade.UNITS, 100, true, "symond_templeton", "", "", 1.0F, 1.0F),
    HORTON_REDFORT("horton_redfort", "Horton Redfort", Gender.MALE, Combat.MELEE, Trade.UNITS, 100, true, "horton_redfort", "", "", 1.0F, 1.0F),
    YOHN_ROYCE("yohn_royce", "Yohn Royce", Gender.MALE, Combat.MELEE, Trade.UNITS, 100, true, "yohn_royce", "", "", 1.0F, 1.0F),
    BENEDAR_BELMORE("benedar_belmore", "Benedar Belmore", Gender.MALE, Combat.MELEE, Trade.UNITS, 100, true, "benedar_belmore", "_1", "_2", 1.0F, 1.0F),
    ROBIN_ARRYN("robin_arryn", "Robin Arryn", Gender.MALE, Combat.PASSIVE, Trade.NONE, 100, true, "robin_arryn", "", "", 0.75F, 0.75F),
    LYSA_ARRYN("lysa_arryn", "Lysa Arryn", Gender.FEMALE, Combat.PASSIVE, Trade.NONE, 100, true, "lysa_arryn", "", "", 1.0F, 1.0F);

    public enum Gender { RANDOM, MALE, FEMALE }
    public enum Combat { PASSIVE, MELEE, ARCHER, AXE_THROWER }
    public enum Trade {
        NONE, BAKER, BARTENDER, BLACKSMITH, BREWER, BUTCHER, FARMER,
        FISHMONGER, FLORIST, GOLDSMITH, LUMBERMAN, MASON, MINER,
        UNITS, MAESTER
    }

    private static final Map<String, ArrynNpcRole> BY_ID = index();
    private static final List<ArrynNpcRole> SPAWNER_ORDER = List.of(values());

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

    ArrynNpcRole(String id, String displayName, Gender gender, Combat combat, Trade trade,
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
    public boolean ordinaryCivilian() { return this == ARRYN_MAN || this == PROSTITUTE; }
    public boolean hillman() { return name().startsWith("HILLMAN_"); }
    public boolean prostitute() { return this == PROSTITUTE; }
    public boolean activeCombatant() { return combat != Combat.PASSIVE; }

    public static ArrynNpcRole byId(String id) {
        if (id == null) return ARRYN_MAN;
        return BY_ID.getOrDefault(id.toLowerCase(Locale.ROOT), ARRYN_MAN);
    }

    public static ArrynNpcRole findById(String id) {
        return id == null ? null : BY_ID.get(id.toLowerCase(Locale.ROOT));
    }

    public static List<ArrynNpcRole> spawnerOrder() { return SPAWNER_ORDER; }

    private static Map<String, ArrynNpcRole> index() {
        Map<String, ArrynNpcRole> roles = new LinkedHashMap<>();
        for (ArrynNpcRole role : values()) roles.put(role.id, role);
        return Map.copyOf(roles);
    }
}
