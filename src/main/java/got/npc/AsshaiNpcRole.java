package got.npc;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/** Complete Asshai military, mystic, civic, and fixed-character catalogue. */
public enum AsshaiNpcRole {
    ASSHAI_MAN("asshai_man", "Asshai Man", Gender.RANDOM, Combat.PASSIVE, Trade.NONE, 1, false, null, 1.0F, 1.0F),
    ASSHAI_WARRIOR("asshai_warrior", "Asshai Warrior", Gender.MALE, Combat.MELEE, Trade.NONE, 2, false, null, 1.0F, 1.0F),
    ASSHAI_SHADOWBINDER("asshai_shadowbinder", "Asshai Shadowbinder", Gender.MALE, Combat.ARCHER, Trade.NONE, 3, false, null, 1.0F, 1.0F),
    ASSHAI_SPHEREBINDER("asshai_spherebinder", "Asshai Spherebinder", Gender.MALE, Combat.MELEE, Trade.NONE, 3, false, null, 1.0F, 1.0F),
    ASSHAI_BANNER_BEARER("asshai_banner_bearer", "Asshai Banner Bearer", Gender.MALE, Combat.MELEE, Trade.NONE, 2, false, null, 1.0F, 1.0F),
    ASSHAI_CAPTAIN("asshai_captain", "Asshai Captain", Gender.MALE, Combat.MELEE, Trade.UNITS, 5, false, null, 1.0F, 1.0F),
    ASSHAI_ALCHEMIST("asshai_alchemist", "Asshai Alchemist", Gender.RANDOM, Combat.PASSIVE, Trade.ALCHEMIST, 2, false, null, 1.0F, 1.0F),
    ASSHAI_ARCHMAG("asshai_archmag", "Asshai Archmag", Gender.MALE, Combat.ARCHER, Trade.NONE, 500, true, "archmag", 1.0F, 1.0F),
    MOQORRO("moqorro", "Moqorro", Gender.MALE, Combat.PASSIVE, Trade.ALCHEMIST, 100, true, "moqorro", "_1", "_2", 1.0F, 1.0F);

    public enum Gender { RANDOM, MALE, FEMALE }
    public enum Combat { PASSIVE, MELEE, ARCHER, HYBRID }
    public enum Trade { NONE, ALCHEMIST, UNITS }

    private static final Map<String, AsshaiNpcRole> BY_ID = index();
    private static final List<AsshaiNpcRole> SPAWNER_ORDER = List.of(values());
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

    AsshaiNpcRole(String id, String displayName, Gender gender, Combat combat, Trade trade,
                  int alignmentBonus, boolean legendary, String legendaryTexture,
                  float scale, float collisionScale) {
        this(id, displayName, gender, combat, trade, alignmentBonus, legendary, legendaryTexture, "", "", scale, collisionScale);
    }

    AsshaiNpcRole(String id, String displayName, Gender gender, Combat combat, Trade trade,
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
    public boolean ordinaryCivilian() { return this == ASSHAI_MAN; }
    public boolean activeCombatant() { return combat != Combat.PASSIVE; }
    public static AsshaiNpcRole byId(String id) { return id == null ? ASSHAI_MAN : BY_ID.getOrDefault(id.toLowerCase(Locale.ROOT), ASSHAI_MAN); }
    public static AsshaiNpcRole findById(String id) { return id == null ? null : BY_ID.get(id.toLowerCase(Locale.ROOT)); }
    public static List<AsshaiNpcRole> spawnerOrder() { return SPAWNER_ORDER; }
    private static Map<String, AsshaiNpcRole> index() {
        Map<String, AsshaiNpcRole> roles = new LinkedHashMap<>();
        for (AsshaiNpcRole role : values()) roles.put(role.id, role);
        return Map.copyOf(roles);
    }
}
