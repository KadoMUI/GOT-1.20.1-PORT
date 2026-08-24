package got.npc;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Complete Pentoshi civic and military catalogue recovered from 1.7.10.
 */
public enum PentosNpcRole {
    PENTOS_MAN("pentos_man", "Pentos Man", Gender.RANDOM, Combat.PASSIVE, Trade.NONE, 1, false, null, "", "", 1.0F, 1.0F),
    PENTOS_LEVYMAN("pentos_levyman", "Pentos Levyman", Gender.MALE, Combat.MELEE, Trade.NONE, 2, false, null, "", "", 1.0F, 1.0F),
    PENTOS_LEVYMAN_ARCHER("pentos_levyman_archer", "Pentos Levyman Archer", Gender.MALE, Combat.ARCHER, Trade.NONE, 2, false, null, "", "", 1.0F, 1.0F),
    PENTOS_SOLDIER("pentos_soldier", "Pentos Warrior", Gender.MALE, Combat.MELEE, Trade.NONE, 2, false, null, "", "", 1.0F, 1.0F),
    PENTOS_SOLDIER_ARCHER("pentos_soldier_archer", "Pentos Warrior Archer", Gender.MALE, Combat.ARCHER, Trade.NONE, 2, false, null, "", "", 1.0F, 1.0F),
    PENTOS_BANNER_BEARER("pentos_banner_bearer", "Pentos Banner Bearer", Gender.MALE, Combat.MELEE, Trade.NONE, 2, false, null, "", "", 1.0F, 1.0F),
    PENTOS_CAPTAIN("pentos_captain", "Pentos Captain", Gender.MALE, Combat.MELEE, Trade.UNITS, 5, false, null, "", "", 1.0F, 1.0F),
    PENTOS_BLACKSMITH("pentos_blacksmith", "Pentos Blacksmith", Gender.MALE, Combat.PASSIVE, Trade.BLACKSMITH, 2, false, null, "", "", 1.0F, 1.0F),
    PENTOS_GOLDSMITH("pentos_goldsmith", "Pentos Goldsmith", Gender.RANDOM, Combat.PASSIVE, Trade.GOLDSMITH, 2, false, null, "", "", 1.0F, 1.0F),
    PENTOS_FARMER("pentos_farmer", "Pentos Farmer", Gender.MALE, Combat.PASSIVE, Trade.FARMER, 2, false, null, "", "", 1.0F, 1.0F),
    PENTOS_FARMHAND("pentos_farmhand", "Pentos Farmhand", Gender.RANDOM, Combat.PASSIVE, Trade.NONE, 1, false, null, "", "", 1.0F, 1.0F),
    PENTOS_BARTENDER("pentos_bartender", "Pentos Bartender", Gender.MALE, Combat.PASSIVE, Trade.BARTENDER, 2, false, null, "", "", 1.0F, 1.0F),
    PENTOS_MINER("pentos_miner", "Pentos Miner", Gender.RANDOM, Combat.PASSIVE, Trade.MINER, 2, false, null, "", "", 1.0F, 1.0F),
    PENTOS_LUMBERMAN("pentos_lumberman", "Pentos Lumberman", Gender.RANDOM, Combat.PASSIVE, Trade.LUMBERMAN, 2, false, null, "", "", 1.0F, 1.0F),
    PENTOS_MASON("pentos_mason", "Pentos Mason", Gender.RANDOM, Combat.PASSIVE, Trade.MASON, 2, false, null, "", "", 1.0F, 1.0F),
    PENTOS_BREWER("pentos_brewer", "Pentos Brewer", Gender.RANDOM, Combat.PASSIVE, Trade.BREWER, 2, false, null, "", "", 1.0F, 1.0F),
    PENTOS_FLORIST("pentos_florist", "Pentos Florist", Gender.RANDOM, Combat.PASSIVE, Trade.FLORIST, 2, false, null, "", "", 1.0F, 1.0F),
    PENTOS_BUTCHER("pentos_butcher", "Pentos Butcher", Gender.RANDOM, Combat.PASSIVE, Trade.BUTCHER, 2, false, null, "", "", 1.0F, 1.0F),
    PENTOS_FISHMONGER("pentos_fishmonger", "Pentos Fishmonger", Gender.RANDOM, Combat.PASSIVE, Trade.FISHMONGER, 2, false, null, "", "", 1.0F, 1.0F),
    PENTOS_BAKER("pentos_baker", "Pentos Baker", Gender.RANDOM, Combat.PASSIVE, Trade.BAKER, 2, false, null, "", "", 1.0F, 1.0F),

    ILLYRIO_MOPATIS("illyrio_mopatis", "Illyrio Mopatis", Gender.MALE, Combat.PASSIVE,
            Trade.GOLDSMITH, 500, true, "illyrio_mopatis", "_1", "_2", 1.0F, 1.0F);

    public enum Gender { RANDOM, MALE, FEMALE }
    public enum Combat { PASSIVE, MELEE, ARCHER, HYBRID }
    public enum Trade {
        NONE, BAKER, BARTENDER, BLACKSMITH, BREWER, BUTCHER, FARMER,
        FISHMONGER, FLORIST, GOLDSMITH, LUMBERMAN, MASON, MINER,
        UNITS, MAESTER
    }

    private static final Map<String, PentosNpcRole> BY_ID = index();
    private static final List<PentosNpcRole> SPAWNER_ORDER = List.of(values());

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

    PentosNpcRole(String id, String displayName, Gender gender, Combat combat, Trade trade,
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
    public boolean ordinaryCivilian() { return this == PENTOS_MAN; }
    public boolean activeCombatant() { return combat != Combat.PASSIVE; }

    public static PentosNpcRole byId(String id) {
        if (id == null) return PENTOS_MAN;
        return BY_ID.getOrDefault(id.toLowerCase(Locale.ROOT), PENTOS_MAN);
    }

    public static PentosNpcRole findById(String id) {
        return id == null ? null : BY_ID.get(id.toLowerCase(Locale.ROOT));
    }

    public static List<PentosNpcRole> spawnerOrder() { return SPAWNER_ORDER; }

    private static Map<String, PentosNpcRole> index() {
        Map<String, PentosNpcRole> roles = new LinkedHashMap<>();
        for (PentosNpcRole role : values()) roles.put(role.id, role);
        return Map.copyOf(roles);
    }
}
