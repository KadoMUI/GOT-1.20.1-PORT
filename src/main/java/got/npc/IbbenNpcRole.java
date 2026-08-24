package got.npc;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/** Complete Ibbenese civic and military catalogue from the final 1.7.10 build. */
public enum IbbenNpcRole {
    IBBEN_MAN("ibben_man", "Ibbenese Man", Gender.RANDOM, Combat.PASSIVE, Trade.NONE, 1),
    IBBEN_LEVYMAN("ibben_levyman", "Ibbenese Levyman", Gender.MALE, Combat.MELEE, Trade.NONE, 2),
    IBBEN_LEVYMAN_ARCHER("ibben_levyman_archer", "Ibbenese Levyman Archer", Gender.MALE, Combat.ARCHER, Trade.NONE, 2),
    IBBEN_SOLDIER("ibben_soldier", "Ibbenese Soldier", Gender.MALE, Combat.MELEE, Trade.NONE, 2),
    IBBEN_SOLDIER_ARCHER("ibben_soldier_archer", "Ibbenese Soldier Archer", Gender.MALE, Combat.ARCHER, Trade.NONE, 2),
    IBBEN_BANNER_BEARER("ibben_banner_bearer", "Ibbenese Banner Bearer", Gender.MALE, Combat.MELEE, Trade.NONE, 2),
    IBBEN_CAPTAIN("ibben_captain", "Ibbenese Captain", Gender.MALE, Combat.MELEE, Trade.UNITS, 5),
    IBBEN_BLACKSMITH("ibben_blacksmith", "Ibbenese Blacksmith", Gender.MALE, Combat.PASSIVE, Trade.BLACKSMITH, 2),
    IBBEN_GOLDSMITH("ibben_goldsmith", "Ibbenese Goldsmith", Gender.RANDOM, Combat.PASSIVE, Trade.GOLDSMITH, 2),
    IBBEN_FARMER("ibben_farmer", "Ibbenese Farmer", Gender.MALE, Combat.PASSIVE, Trade.FARMER, 2),
    IBBEN_FARMHAND("ibben_farmhand", "Ibbenese Farmhand", Gender.RANDOM, Combat.PASSIVE, Trade.NONE, 1),
    IBBEN_BARTENDER("ibben_bartender", "Ibbenese Bartender", Gender.RANDOM, Combat.PASSIVE, Trade.BARTENDER, 2),
    IBBEN_MINER("ibben_miner", "Ibbenese Miner", Gender.RANDOM, Combat.PASSIVE, Trade.MINER, 2),
    IBBEN_LUMBERMAN("ibben_lumberman", "Ibbenese Lumberman", Gender.RANDOM, Combat.PASSIVE, Trade.LUMBERMAN, 2),
    IBBEN_MASON("ibben_mason", "Ibbenese Mason", Gender.RANDOM, Combat.PASSIVE, Trade.MASON, 2),
    IBBEN_BREWER("ibben_brewer", "Ibbenese Brewer", Gender.RANDOM, Combat.PASSIVE, Trade.BREWER, 2),
    IBBEN_FLORIST("ibben_florist", "Ibbenese Florist", Gender.RANDOM, Combat.PASSIVE, Trade.FLORIST, 2),
    IBBEN_BUTCHER("ibben_butcher", "Ibbenese Butcher", Gender.RANDOM, Combat.PASSIVE, Trade.BUTCHER, 2),
    IBBEN_FISHMONGER("ibben_fishmonger", "Ibbenese Fishmonger", Gender.RANDOM, Combat.PASSIVE, Trade.FISHMONGER, 2),
    IBBEN_BAKER("ibben_baker", "Ibbenese Baker", Gender.RANDOM, Combat.PASSIVE, Trade.BAKER, 2);

    public enum Gender { RANDOM, MALE, FEMALE }
    public enum Combat { PASSIVE, MELEE, ARCHER, HYBRID }
    public enum Trade { NONE, BAKER, BARTENDER, BLACKSMITH, BREWER, BUTCHER, FARMER, FISHMONGER, FLORIST, GOLDSMITH, LUMBERMAN, MASON, MINER, UNITS }

    private static final Map<String, IbbenNpcRole> BY_ID = index();
    private static final List<IbbenNpcRole> SPAWNER_ORDER = List.of(values());
    private final String id;
    private final String displayName;
    private final Gender gender;
    private final Combat combat;
    private final Trade trade;
    private final int alignmentBonus;

    IbbenNpcRole(String id, String displayName, Gender gender, Combat combat, Trade trade, int alignmentBonus) {
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
    public boolean ordinaryCivilian() { return this == IBBEN_MAN || this == IBBEN_FARMHAND; }
    public boolean activeCombatant() { return combat != Combat.PASSIVE; }
    public boolean usesOutfitOverlay() { return this != IBBEN_SOLDIER && this != IBBEN_SOLDIER_ARCHER && this != IBBEN_BANNER_BEARER; }
    public static IbbenNpcRole byId(String id) { return id == null ? IBBEN_MAN : BY_ID.getOrDefault(id.toLowerCase(Locale.ROOT), IBBEN_MAN); }
    public static IbbenNpcRole findById(String id) { return id == null ? null : BY_ID.get(id.toLowerCase(Locale.ROOT)); }
    public static List<IbbenNpcRole> spawnerOrder() { return SPAWNER_ORDER; }
    private static Map<String, IbbenNpcRole> index() {
        Map<String, IbbenNpcRole> roles = new LinkedHashMap<>();
        for (IbbenNpcRole role : values()) roles.put(role.id, role);
        return Map.copyOf(roles);
    }
}
