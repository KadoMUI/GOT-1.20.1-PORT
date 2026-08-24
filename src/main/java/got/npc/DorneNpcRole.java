package got.npc;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Dorne population catalogue recovered from the original 1.7.10 entity
 * hierarchy and its fixed-waypoint registrations.
 */
public enum DorneNpcRole {
    DORNE_MAN("dorne_man", "Dorne Civilian", Gender.RANDOM, Combat.PASSIVE, Trade.NONE, 1, false, null, "", "", 1.0F, 1.0F),
    DORNE_LEVYMAN("dorne_levyman", "Dorne Levyman", Gender.MALE, Combat.MELEE, Trade.NONE, 2, false, null, "", "", 1.0F, 1.0F),
    DORNE_LEVYMAN_ARCHER("dorne_levyman_archer", "Dorne Levyman Archer", Gender.MALE, Combat.ARCHER, Trade.NONE, 2, false, null, "", "", 1.0F, 1.0F),
    DORNE_SOLDIER("dorne_soldier", "Dorne Soldier", Gender.MALE, Combat.MELEE, Trade.NONE, 2, false, null, "", "", 1.0F, 1.0F),
    DORNE_SOLDIER_ARCHER("dorne_soldier_archer", "Dorne Soldier Archer", Gender.MALE, Combat.ARCHER, Trade.NONE, 2, false, null, "", "", 1.0F, 1.0F),
    DORNE_BANNER_BEARER("dorne_banner_bearer", "Dorne Banner Bearer", Gender.MALE, Combat.MELEE, Trade.NONE, 2, false, null, "", "", 1.0F, 1.0F),
    DORNE_CAPTAIN("dorne_captain", "Dorne Captain", Gender.MALE, Combat.MELEE, Trade.UNITS, 5, false, null, "", "", 1.0F, 1.0F),
    DORNE_BLACKSMITH("dorne_blacksmith", "Dorne Blacksmith", Gender.MALE, Combat.PASSIVE, Trade.BLACKSMITH, 2, false, null, "", "", 1.0F, 1.0F),
    DORNE_GOLDSMITH("dorne_goldsmith", "Dorne Goldsmith", Gender.RANDOM, Combat.PASSIVE, Trade.GOLDSMITH, 2, false, null, "", "", 1.0F, 1.0F),
    DORNE_FARMER("dorne_farmer", "Dorne Farmer", Gender.MALE, Combat.PASSIVE, Trade.FARMER, 2, false, null, "", "", 1.0F, 1.0F),
    DORNE_FARMHAND("dorne_farmhand", "Dorne Farmhand", Gender.RANDOM, Combat.PASSIVE, Trade.NONE, 1, false, null, "", "", 1.0F, 1.0F),
    DORNE_BARTENDER("dorne_bartender", "Dorne Bartender", Gender.MALE, Combat.PASSIVE, Trade.BARTENDER, 2, false, null, "", "", 1.0F, 1.0F),
    DORNE_MINER("dorne_miner", "Dorne Miner", Gender.RANDOM, Combat.PASSIVE, Trade.MINER, 2, false, null, "", "", 1.0F, 1.0F),
    DORNE_LUMBERMAN("dorne_lumberman", "Dorne Lumberman", Gender.RANDOM, Combat.PASSIVE, Trade.LUMBERMAN, 2, false, null, "", "", 1.0F, 1.0F),
    DORNE_MASON("dorne_mason", "Dorne Mason", Gender.RANDOM, Combat.PASSIVE, Trade.MASON, 2, false, null, "", "", 1.0F, 1.0F),
    DORNE_BREWER("dorne_brewer", "Dorne Brewer", Gender.RANDOM, Combat.PASSIVE, Trade.BREWER, 2, false, null, "", "", 1.0F, 1.0F),
    DORNE_FLORIST("dorne_florist", "Dorne Florist", Gender.RANDOM, Combat.PASSIVE, Trade.FLORIST, 2, false, null, "", "", 1.0F, 1.0F),
    DORNE_BUTCHER("dorne_butcher", "Dorne Butcher", Gender.RANDOM, Combat.PASSIVE, Trade.BUTCHER, 2, false, null, "", "", 1.0F, 1.0F),
    DORNE_FISHMONGER("dorne_fishmonger", "Dorne Fishmonger", Gender.RANDOM, Combat.PASSIVE, Trade.FISHMONGER, 2, false, null, "", "", 1.0F, 1.0F),
    DORNE_BAKER("dorne_baker", "Dorne Baker", Gender.RANDOM, Combat.PASSIVE, Trade.BAKER, 2, false, null, "", "", 1.0F, 1.0F),

    HARMEN_ULLER("harmen_uller", "Harmen Uller", Gender.MALE, Combat.MELEE, Trade.UNITS, 100, true, "harmen_uller", "", "", 1.0F, 1.0F),
    GEROLD_DAYNE("gerold_dayne", "Gerold Dayne", Gender.MALE, Combat.MELEE, Trade.NONE, 0, true, "gerold_dayne", "", "", 1.0F, 1.0F),
    QUENTYN_QORGYLE("quentyn_qorgyle", "Quentyn Qorgyle", Gender.MALE, Combat.MELEE, Trade.UNITS, 100, true, "quentyn_qorgyle", "", "", 1.0F, 1.0F),
    FRANKLYN_FOWLER("franklyn_fowler", "Franklyn Fowler", Gender.MALE, Combat.MELEE, Trade.UNITS, 100, true, "franklyn_fowler", "", "", 1.0F, 1.0F),
    BERIC_DAYNE("beric_dayne", "Beric Dayne", Gender.MALE, Combat.MELEE, Trade.NONE, 100, true, "beric_dayne", "", "", 1.0F, 1.0F),
    OBERYN_MARTELL("oberyn_martell", "Oberyn Martell", Gender.MALE, Combat.MELEE, Trade.NONE, 500, true, "oberyn_martell", "", "", 1.0F, 1.0F),
    DORAN_MARTELL("doran_martell", "Doran Martell", Gender.MALE, Combat.MELEE, Trade.NONE, 300, true, "doran_martell", "", "", 1.0F, 1.0F),
    ELLARYA_SAND("ellarya_sand", "Ellarya Sand", Gender.FEMALE, Combat.MELEE, Trade.NONE, 300, true, "ellarya_sand", "", "", 1.0F, 1.0F),
    AREO_HOTAH("areo_hotah", "Areo Hotah", Gender.MALE, Combat.MELEE, Trade.NONE, 100, true, "areo_hotah", "", "", 1.2F, 1.2F),
    TRYSTANE_MARTELL("trystane_martell", "Trystane Martell", Gender.MALE, Combat.MELEE, Trade.NONE, 500, true, "trystane_martell", "", "", 0.9F, 0.9F),
    ARIANNE_MARTELL("arianne_martell", "Arianne Martell", Gender.FEMALE, Combat.MELEE, Trade.NONE, 500, true, "arianne_martell", "", "", 1.0F, 1.0F),
    MANFREY_MARTELL("manfrey_martell", "Manfrey Martell", Gender.MALE, Combat.MELEE, Trade.NONE, 100, true, "manfrey_martell", "", "", 1.0F, 1.0F),
    QUENTYN_MARTELL("quentyn_martell", "Quentyn Martell", Gender.MALE, Combat.MELEE, Trade.NONE, 100, true, "quentyn_martell", "", "", 1.0F, 1.0F),
    ANDERS_YRONWOOD("anders_yronwood", "Anders Yronwood", Gender.MALE, Combat.MELEE, Trade.UNITS, 100, true, "anders_yronwood", "", "", 1.0F, 1.0F);

    public enum Gender { RANDOM, MALE, FEMALE }
    public enum Combat { PASSIVE, MELEE, ARCHER, HYBRID }
    public enum Trade {
        NONE, BAKER, BARTENDER, BLACKSMITH, BREWER, BUTCHER, FARMER,
        FISHMONGER, FLORIST, GOLDSMITH, LUMBERMAN, MASON, MINER,
        UNITS, MAESTER
    }

    private static final Map<String, DorneNpcRole> BY_ID = index();
    private static final List<DorneNpcRole> SPAWNER_ORDER = List.of(values());

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

    DorneNpcRole(String id, String displayName, Gender gender, Combat combat, Trade trade,
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
    public boolean ordinaryCivilian() { return this == DORNE_MAN; }
    public boolean activeCombatant() { return combat != Combat.PASSIVE; }

    public static DorneNpcRole byId(String id) {
        if (id == null) return DORNE_MAN;
        return BY_ID.getOrDefault(id.toLowerCase(Locale.ROOT), DORNE_MAN);
    }

    public static DorneNpcRole findById(String id) {
        return id == null ? null : BY_ID.get(id.toLowerCase(Locale.ROOT));
    }

    public static List<DorneNpcRole> spawnerOrder() { return SPAWNER_ORDER; }

    private static Map<String, DorneNpcRole> index() {
        Map<String, DorneNpcRole> roles = new LinkedHashMap<>();
        for (DorneNpcRole role : values()) roles.put(role.id, role);
        return Map.copyOf(roles);
    }
}
