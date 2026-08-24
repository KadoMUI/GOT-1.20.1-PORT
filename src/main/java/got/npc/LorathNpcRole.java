package got.npc;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/** Complete Lorathi civic and military catalogue from the final 1.7.10 build. */
public enum LorathNpcRole {
    LORATH_MAN("lorath_man", "Lorath Man", Gender.RANDOM, Combat.PASSIVE, Trade.NONE, 1),
    LORATH_LEVYMAN("lorath_levyman", "Lorath Levyman", Gender.MALE, Combat.MELEE, Trade.NONE, 2),
    LORATH_LEVYMAN_ARCHER("lorath_levyman_archer", "Lorath Levyman Archer", Gender.MALE, Combat.ARCHER, Trade.NONE, 2),
    LORATH_SOLDIER("lorath_soldier", "Lorath Warrior", Gender.MALE, Combat.MELEE, Trade.NONE, 2),
    LORATH_SOLDIER_ARCHER("lorath_soldier_archer", "Lorath Warrior Archer", Gender.MALE, Combat.ARCHER, Trade.NONE, 2),
    LORATH_BANNER_BEARER("lorath_banner_bearer", "Lorath Banner Bearer", Gender.MALE, Combat.MELEE, Trade.NONE, 2),
    LORATH_CAPTAIN("lorath_captain", "Lorath Captain", Gender.MALE, Combat.MELEE, Trade.UNITS, 5),
    LORATH_BLACKSMITH("lorath_blacksmith", "Lorath Blacksmith", Gender.MALE, Combat.PASSIVE, Trade.BLACKSMITH, 2),
    LORATH_GOLDSMITH("lorath_goldsmith", "Lorath Goldsmith", Gender.RANDOM, Combat.PASSIVE, Trade.GOLDSMITH, 2),
    LORATH_FARMER("lorath_farmer", "Lorath Farmer", Gender.MALE, Combat.PASSIVE, Trade.FARMER, 2),
    LORATH_FARMHAND("lorath_farmhand", "Lorath Farmhand", Gender.RANDOM, Combat.PASSIVE, Trade.NONE, 1),
    LORATH_BARTENDER("lorath_bartender", "Lorath Bartender", Gender.MALE, Combat.PASSIVE, Trade.BARTENDER, 2),
    LORATH_MINER("lorath_miner", "Lorath Miner", Gender.RANDOM, Combat.PASSIVE, Trade.MINER, 2),
    LORATH_LUMBERMAN("lorath_lumberman", "Lorath Lumberman", Gender.RANDOM, Combat.PASSIVE, Trade.LUMBERMAN, 2),
    LORATH_MASON("lorath_mason", "Lorath Mason", Gender.RANDOM, Combat.PASSIVE, Trade.MASON, 2),
    LORATH_BREWER("lorath_brewer", "Lorath Brewer", Gender.RANDOM, Combat.PASSIVE, Trade.BREWER, 2),
    LORATH_FLORIST("lorath_florist", "Lorath Florist", Gender.RANDOM, Combat.PASSIVE, Trade.FLORIST, 2),
    LORATH_BUTCHER("lorath_butcher", "Lorath Butcher", Gender.RANDOM, Combat.PASSIVE, Trade.BUTCHER, 2),
    LORATH_FISHMONGER("lorath_fishmonger", "Lorath Fishmonger", Gender.RANDOM, Combat.PASSIVE, Trade.FISHMONGER, 2),
    LORATH_BAKER("lorath_baker", "Lorath Baker", Gender.RANDOM, Combat.PASSIVE, Trade.BAKER, 2);

    public enum Gender { RANDOM, MALE, FEMALE }
    public enum Combat { PASSIVE, MELEE, ARCHER, HYBRID }
    public enum Trade { NONE, BAKER, BARTENDER, BLACKSMITH, BREWER, BUTCHER, FARMER, FISHMONGER, FLORIST, GOLDSMITH, LUMBERMAN, MASON, MINER, UNITS }

    private static final Map<String, LorathNpcRole> BY_ID = index();
    private static final List<LorathNpcRole> SPAWNER_ORDER = List.of(values());
    private final String id;
    private final String displayName;
    private final Gender gender;
    private final Combat combat;
    private final Trade trade;
    private final int alignmentBonus;

    LorathNpcRole(String id, String displayName, Gender gender, Combat combat, Trade trade, int alignmentBonus) {
        this.id = id;
        this.displayName = displayName;
        this.gender = gender;
        this.combat = combat;
        this.trade = trade;
        this.alignmentBonus = alignmentBonus;
    }

    public String id() { return id; }
    public String displayName() { return displayName; }
    public Gender gender() { return gender; }
    public Combat combat() { return combat; }
    public Trade trade() { return trade; }
    public int alignmentBonus() { return alignmentBonus; }
    public boolean legendary() { return false; }
    public String legendaryTexture() { return null; }
    public String legendaryBaseSuffix() { return ""; }
    public String legendaryOverlaySuffix() { return ""; }
    public boolean hasLegendaryOverlay() { return false; }
    public float scale() { return 1.0F; }
    public float collisionScale() { return 1.0F; }
    public boolean ordinaryCivilian() { return this == LORATH_MAN || this == LORATH_FARMHAND; }
    public boolean activeCombatant() { return combat != Combat.PASSIVE; }
    public boolean usesOutfitOverlay() { return this != LORATH_SOLDIER && this != LORATH_SOLDIER_ARCHER && this != LORATH_BANNER_BEARER; }
    public static LorathNpcRole byId(String id) { return id == null ? LORATH_MAN : BY_ID.getOrDefault(id.toLowerCase(Locale.ROOT), LORATH_MAN); }
    public static LorathNpcRole findById(String id) { return id == null ? null : BY_ID.get(id.toLowerCase(Locale.ROOT)); }
    public static List<LorathNpcRole> spawnerOrder() { return SPAWNER_ORDER; }
    private static Map<String, LorathNpcRole> index() {
        Map<String, LorathNpcRole> roles = new LinkedHashMap<>();
        for (LorathNpcRole role : values()) roles.put(role.id, role);
        return Map.copyOf(roles);
    }
}
