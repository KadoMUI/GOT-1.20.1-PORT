package got.npc;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/** Complete Free Folk catalogue, including the nested Thenn culture and giants. */
public enum WildlingNpcRole {
    WILDLING("wildling", "Free Folk", Gender.RANDOM, Combat.PASSIVE, Trade.NONE, 1, false, null, "", "", 1.0F, 1.0F),
    WILDLING_WARRIOR("wildling_warrior", "Wildling Warrior", Gender.MALE, Combat.MELEE, Trade.NONE, 2, false, null, "", "", 1.0F, 1.0F),
    WILDLING_ARCHER("wildling_archer", "Wildling Archer", Gender.MALE, Combat.ARCHER, Trade.NONE, 2, false, null, "", "", 1.0F, 1.0F),
    WILDLING_AXE_THROWER("wildling_axe_thrower", "Wildling Axe Thrower", Gender.MALE, Combat.AXE_THROWER, Trade.NONE, 2, false, null, "", "", 1.0F, 1.0F),
    WILDLING_BANNER_BEARER("wildling_banner_bearer", "Wildling Banner Bearer", Gender.MALE, Combat.MELEE, Trade.NONE, 2, false, null, "", "", 1.0F, 1.0F),
    WILDLING_CHIEFTAIN("wildling_chieftain", "Wildling Chieftain", Gender.MALE, Combat.MELEE, Trade.UNITS, 5, false, null, "", "", 1.0F, 1.0F),
    GIANT("giant", "Giant", Gender.MALE, Combat.MELEE, Trade.NONE, 10, false, null, "", "", 3.5F, 3.0F),

    THENN("thenn", "Thenn", Gender.RANDOM, Combat.PASSIVE, Trade.NONE, 1, false, null, "", "", 1.0F, 1.0F),
    THENN_WARRIOR("thenn_warrior", "Thenn Warrior", Gender.MALE, Combat.MELEE, Trade.NONE, 2, false, null, "", "", 1.0F, 1.0F),
    THENN_ARCHER("thenn_archer", "Thenn Archer", Gender.MALE, Combat.ARCHER, Trade.NONE, 2, false, null, "", "", 1.0F, 1.0F),
    THENN_AXE_THROWER("thenn_axe_thrower", "Thenn Axe Thrower", Gender.MALE, Combat.AXE_THROWER, Trade.NONE, 2, false, null, "", "", 1.0F, 1.0F),
    THENN_BANNER_BEARER("thenn_banner_bearer", "Thenn Banner Bearer", Gender.MALE, Combat.MELEE, Trade.NONE, 2, false, null, "", "", 1.0F, 1.0F),
    THENN_BLACKSMITH("thenn_blacksmith", "Thenn Blacksmith", Gender.MALE, Combat.PASSIVE, Trade.BLACKSMITH, 2, false, null, "", "", 1.0F, 1.0F),
    THENN_CHIEFTAIN("thenn_chieftain", "Thenn Chieftain", Gender.MALE, Combat.MELEE, Trade.THENN_UNITS, 5, false, null, "", "", 1.0F, 1.0F),

    MANCE_RAYDER("mance_rayder", "Mance Rayder", Gender.MALE, Combat.MELEE, Trade.UNITS, 500, true, "mance_rayder", "_1", "_2", 1.0F, 1.0F),
    TORMUND("tormund", "Tormund Giantsbane", Gender.MALE, Combat.MELEE, Trade.NONE, 300, true, "tormund", "_1", "_2", 1.1F, 1.0F),
    YGRITTE("ygritte", "Ygritte", Gender.FEMALE, Combat.ARCHER, Trade.NONE, 200, true, "ygritte", "", "", 1.0F, 1.0F),
    CRASTER("craster", "Craster", Gender.MALE, Combat.MELEE, Trade.CRASTER, 10, true, "craster", "_1", "_2", 1.0F, 1.0F),
    CRASTER_WIFE("craster_wife", "Craster's Wife", Gender.FEMALE, Combat.PASSIVE, Trade.NONE, 1, false, null, "", "", 1.0F, 1.0F);

    public enum Gender { RANDOM, MALE, FEMALE }
    public enum Combat { PASSIVE, MELEE, ARCHER, AXE_THROWER }
    public enum Trade { NONE, BLACKSMITH, UNITS, THENN_UNITS, CRASTER }

    private static final Map<String, WildlingNpcRole> BY_ID = index();
    private static final List<WildlingNpcRole> SPAWNER_ORDER = List.of(values());
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

    WildlingNpcRole(String id, String displayName, Gender gender, Combat combat, Trade trade,
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
    public boolean hasLegendaryOverlay() { return legendaryOverlaySuffix != null && !legendaryOverlaySuffix.isEmpty(); }
    public float scale() { return scale; }
    public float collisionScale() { return collisionScale; }
    public boolean thenn() { return name().startsWith("THENN"); }
    public boolean giant() { return this == GIANT; }
    public boolean ordinaryCivilian() { return this == WILDLING || this == THENN || this == CRASTER_WIFE; }
    public boolean activeCombatant() { return combat != Combat.PASSIVE; }

    public static WildlingNpcRole byId(String id) {
        return id == null ? WILDLING : BY_ID.getOrDefault(id.toLowerCase(Locale.ROOT), WILDLING);
    }
    public static WildlingNpcRole findById(String id) {
        return id == null ? null : BY_ID.get(id.toLowerCase(Locale.ROOT));
    }
    public static List<WildlingNpcRole> spawnerOrder() { return SPAWNER_ORDER; }
    private static Map<String, WildlingNpcRole> index() {
        Map<String, WildlingNpcRole> roles = new LinkedHashMap<>();
        for (WildlingNpcRole role : values()) roles.put(role.id, role);
        return Map.copyOf(roles);
    }
}
