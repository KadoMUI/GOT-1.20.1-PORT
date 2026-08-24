package got.npc;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/** Complete Ghiscari civic, military, specialist, and fixed-character catalogue. */
public enum GhiscarNpcRole {
    GHISCAR_MAN("ghiscar_man", "Ghiscari Man", Gender.RANDOM, Combat.PASSIVE, Trade.NONE, 1, false, null, "", "", 1.0F, 1.0F),
    GHISCAR_LEVYMAN("ghiscar_levyman", "Ghiscari Levyman", Gender.MALE, Combat.MELEE, Trade.NONE, 2, false, null, "", "", 1.0F, 1.0F),
    GHISCAR_LEVYMAN_ARCHER("ghiscar_levyman_archer", "Ghiscari Levyman Archer", Gender.MALE, Combat.ARCHER, Trade.NONE, 2, false, null, "", "", 1.0F, 1.0F),
    GHISCAR_SOLDIER("ghiscar_soldier", "Ghiscari Soldier", Gender.MALE, Combat.MELEE, Trade.NONE, 2, false, null, "", "", 1.0F, 1.0F),
    GHISCAR_SOLDIER_ARCHER("ghiscar_soldier_archer", "Ghiscari Soldier Archer", Gender.MALE, Combat.ARCHER, Trade.NONE, 2, false, null, "", "", 1.0F, 1.0F),
    GHISCAR_BANNER_BEARER("ghiscar_banner_bearer", "Ghiscari Banner Bearer", Gender.MALE, Combat.MELEE, Trade.NONE, 2, false, null, "", "", 1.0F, 1.0F),
    GHISCAR_CAPTAIN("ghiscar_captain", "Ghiscari Captain", Gender.MALE, Combat.MELEE, Trade.UNITS, 5, false, null, "", "", 1.0F, 1.0F),
    GHISCAR_BLACKSMITH("ghiscar_blacksmith", "Ghiscari Blacksmith", Gender.MALE, Combat.PASSIVE, Trade.BLACKSMITH, 2, false, null, "", "", 1.0F, 1.0F),
    GHISCAR_GOLDSMITH("ghiscar_goldsmith", "Ghiscari Goldsmith", Gender.RANDOM, Combat.PASSIVE, Trade.GOLDSMITH, 2, false, null, "", "", 1.0F, 1.0F),
    GHISCAR_SLAVER("ghiscar_slaver", "Ghiscari Slaver", Gender.RANDOM, Combat.PASSIVE, Trade.SLAVER, 2, false, null, "", "", 1.0F, 1.0F),
    GHISCAR_SLAVE("ghiscar_slave", "Ghiscari Slave", Gender.RANDOM, Combat.PASSIVE, Trade.NONE, 1, false, null, "", "", 1.0F, 1.0F),
    GHISCAR_BARTENDER("ghiscar_bartender", "Ghiscari Bartender", Gender.MALE, Combat.PASSIVE, Trade.BARTENDER, 2, false, null, "", "", 1.0F, 1.0F),
    GHISCAR_MINER("ghiscar_miner", "Ghiscari Miner", Gender.RANDOM, Combat.PASSIVE, Trade.MINER, 2, false, null, "", "", 1.0F, 1.0F),
    GHISCAR_LUMBERMAN("ghiscar_lumberman", "Ghiscari Lumberman", Gender.RANDOM, Combat.PASSIVE, Trade.LUMBERMAN, 2, false, null, "", "", 1.0F, 1.0F),
    GHISCAR_MASON("ghiscar_mason", "Ghiscari Mason", Gender.RANDOM, Combat.PASSIVE, Trade.MASON, 2, false, null, "", "", 1.0F, 1.0F),
    GHISCAR_BREWER("ghiscar_brewer", "Ghiscari Brewer", Gender.RANDOM, Combat.PASSIVE, Trade.BREWER, 2, false, null, "", "", 1.0F, 1.0F),
    GHISCAR_FLORIST("ghiscar_florist", "Ghiscari Florist", Gender.RANDOM, Combat.PASSIVE, Trade.FLORIST, 2, false, null, "", "", 1.0F, 1.0F),
    GHISCAR_BUTCHER("ghiscar_butcher", "Ghiscari Butcher", Gender.RANDOM, Combat.PASSIVE, Trade.BUTCHER, 2, false, null, "", "", 1.0F, 1.0F),
    GHISCAR_FISHMONGER("ghiscar_fishmonger", "Ghiscari Fishmonger", Gender.RANDOM, Combat.PASSIVE, Trade.FISHMONGER, 2, false, null, "", "", 1.0F, 1.0F),
    GHISCAR_BAKER("ghiscar_baker", "Ghiscari Baker", Gender.RANDOM, Combat.PASSIVE, Trade.BAKER, 2, false, null, "", "", 1.0F, 1.0F),
    GHISCAR_GLADIATOR("ghiscar_gladiator", "Ghiscari Gladiator", Gender.MALE, Combat.MELEE, Trade.NONE, 0, false, null, "", "", 1.0F, 1.0F),
    GHISCAR_HARPY("ghiscar_harpy", "Son of the Harpy", Gender.MALE, Combat.MELEE, Trade.NONE, 1, false, null, "", "", 1.0F, 1.0F),
    GHISCAR_UNSULLIED("ghiscar_unsullied", "Unsullied", Gender.MALE, Combat.MELEE, Trade.NONE, 2, false, null, "", "", 1.0F, 1.0F),

    KRAZNYS_MO_NAKLOZ("kraznys_mo_nakloz", "Kraznys mo Nakloz", Gender.MALE, Combat.PASSIVE, Trade.UNITS, 500, true, "kraznys_mo_nakloz", "_1", "_2", 1.0F, 1.0F),
    MISSANDEI("missandei", "Missandei", Gender.FEMALE, Combat.PASSIVE, Trade.NONE, 1, true, "missandei", "", "", 1.0F, 1.0F),
    GREY_WORM("grey_worm", "Grey Worm", Gender.MALE, Combat.MELEE, Trade.NONE, 1, true, "grey_worm", "", "", 1.0F, 1.0F),
    HIZDAHR_ZO_LORAQ("hizdahr_zo_loraq", "Hizdahr zo Loraq", Gender.MALE, Combat.PASSIVE, Trade.UNITS, 100, true, "hizdahr_zo_loraq", "", "", 1.0F, 1.0F),
    DAARIO_NAHARIS("daario_naharis", "Daario Naharis", Gender.MALE, Combat.MELEE, Trade.NONE, 1, true, "daario_naharis", "", "", 1.0F, 1.0F),
    RAZDAL_MO_ERAZ("razdal_mo_eraz", "Razdal mo Eraz", Gender.MALE, Combat.PASSIVE, Trade.UNITS, 100, true, "razdal_mo_eraz", "", "", 1.0F, 1.0F);

    public enum Gender { RANDOM, MALE, FEMALE }
    public enum Combat { PASSIVE, MELEE, ARCHER, HYBRID }
    public enum Trade { NONE, BAKER, BARTENDER, BLACKSMITH, BREWER, BUTCHER, FARMER, FISHMONGER, FLORIST, GOLDSMITH, LUMBERMAN, MASON, MINER, UNITS, MAESTER, SLAVER }

    private static final Map<String, GhiscarNpcRole> BY_ID = index();
    private static final List<GhiscarNpcRole> SPAWNER_ORDER = List.of(values());
    private final String id, displayName, legendaryTexture, legendaryBaseSuffix, legendaryOverlaySuffix;
    private final Gender gender;
    private final Combat combat;
    private final Trade trade;
    private final int alignmentBonus;
    private final boolean legendary;
    private final float scale, collisionScale;

    GhiscarNpcRole(String id, String displayName, Gender gender, Combat combat, Trade trade, int alignmentBonus,
                   boolean legendary, String legendaryTexture, String legendaryBaseSuffix,
                   String legendaryOverlaySuffix, float scale, float collisionScale) {
        this.id = id; this.displayName = displayName; this.gender = gender; this.combat = combat;
        this.trade = trade; this.alignmentBonus = alignmentBonus; this.legendary = legendary;
        this.legendaryTexture = legendaryTexture; this.legendaryBaseSuffix = legendaryBaseSuffix;
        this.legendaryOverlaySuffix = legendaryOverlaySuffix; this.scale = scale; this.collisionScale = collisionScale;
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
    public boolean ordinaryCivilian() { return this == GHISCAR_MAN || this == GHISCAR_SLAVE; }
    public boolean activeCombatant() { return combat != Combat.PASSIVE; }
    public static GhiscarNpcRole byId(String id) { return id == null ? GHISCAR_MAN : BY_ID.getOrDefault(id.toLowerCase(Locale.ROOT), GHISCAR_MAN); }
    public static GhiscarNpcRole findById(String id) { return id == null ? null : BY_ID.get(id.toLowerCase(Locale.ROOT)); }
    public static List<GhiscarNpcRole> spawnerOrder() { return SPAWNER_ORDER; }
    private static Map<String, GhiscarNpcRole> index() { Map<String, GhiscarNpcRole> roles = new LinkedHashMap<>(); for (GhiscarNpcRole role : values()) roles.put(role.id, role); return Map.copyOf(roles); }
}
