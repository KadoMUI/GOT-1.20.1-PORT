package got.npc;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/** Complete Qohori civic and military catalogue from the final 1.7.10 build. */
public enum QohorNpcRole {
    QOHOR_MAN("qohor_man", "Qohor Man", Gender.RANDOM, Combat.PASSIVE, Trade.NONE, 1),
    QOHOR_LEVYMAN("qohor_levyman", "Qohor Levyman", Gender.MALE, Combat.MELEE, Trade.NONE, 2),
    QOHOR_LEVYMAN_ARCHER("qohor_levyman_archer", "Qohor Levyman Archer", Gender.MALE, Combat.ARCHER, Trade.NONE, 2),
    QOHOR_SOLDIER("qohor_soldier", "Qohor Warrior", Gender.MALE, Combat.MELEE, Trade.NONE, 2),
    QOHOR_SOLDIER_ARCHER("qohor_soldier_archer", "Qohor Warrior Archer", Gender.MALE, Combat.ARCHER, Trade.NONE, 2),
    QOHOR_BANNER_BEARER("qohor_banner_bearer", "Qohor Banner Bearer", Gender.MALE, Combat.MELEE, Trade.NONE, 2),
    QOHOR_CAPTAIN("qohor_captain", "Qohor Captain", Gender.MALE, Combat.MELEE, Trade.UNITS, 5),
    QOHOR_BLACKSMITH("qohor_blacksmith", "Qohor Blacksmith", Gender.MALE, Combat.PASSIVE, Trade.BLACKSMITH, 2),
    QOHOR_GOLDSMITH("qohor_goldsmith", "Qohor Goldsmith", Gender.RANDOM, Combat.PASSIVE, Trade.GOLDSMITH, 2),
    QOHOR_FARMER("qohor_farmer", "Qohor Farmer", Gender.MALE, Combat.PASSIVE, Trade.FARMER, 2),
    QOHOR_FARMHAND("qohor_farmhand", "Qohor Farmhand", Gender.RANDOM, Combat.PASSIVE, Trade.NONE, 1),
    QOHOR_BARTENDER("qohor_bartender", "Qohor Bartender", Gender.MALE, Combat.PASSIVE, Trade.BARTENDER, 2),
    QOHOR_MINER("qohor_miner", "Qohor Miner", Gender.RANDOM, Combat.PASSIVE, Trade.MINER, 2),
    QOHOR_LUMBERMAN("qohor_lumberman", "Qohor Lumberman", Gender.RANDOM, Combat.PASSIVE, Trade.LUMBERMAN, 2),
    QOHOR_MASON("qohor_mason", "Qohor Mason", Gender.RANDOM, Combat.PASSIVE, Trade.MASON, 2),
    QOHOR_BREWER("qohor_brewer", "Qohor Brewer", Gender.RANDOM, Combat.PASSIVE, Trade.BREWER, 2),
    QOHOR_FLORIST("qohor_florist", "Qohor Florist", Gender.RANDOM, Combat.PASSIVE, Trade.FLORIST, 2),
    QOHOR_BUTCHER("qohor_butcher", "Qohor Butcher", Gender.RANDOM, Combat.PASSIVE, Trade.BUTCHER, 2),
    QOHOR_FISHMONGER("qohor_fishmonger", "Qohor Fishmonger", Gender.RANDOM, Combat.PASSIVE, Trade.FISHMONGER, 2),
    QOHOR_BAKER("qohor_baker", "Qohor Baker", Gender.RANDOM, Combat.PASSIVE, Trade.BAKER, 2),
    QOHOR_UNSULLIED("qohor_unsullied", "Qohor Unsullied", Gender.MALE, Combat.MELEE, Trade.NONE, 2);

    public enum Gender { RANDOM, MALE, FEMALE }
    public enum Combat { PASSIVE, MELEE, ARCHER, HYBRID }
    public enum Trade { NONE, BAKER, BARTENDER, BLACKSMITH, BREWER, BUTCHER, FARMER, FISHMONGER, FLORIST, GOLDSMITH, LUMBERMAN, MASON, MINER, UNITS }

    private static final Map<String, QohorNpcRole> BY_ID = index();
    private static final List<QohorNpcRole> SPAWNER_ORDER = List.of(values());
    private final String id;
    private final String displayName;
    private final Gender gender;
    private final Combat combat;
    private final Trade trade;
    private final int alignmentBonus;

    QohorNpcRole(String id, String displayName, Gender gender, Combat combat, Trade trade, int alignmentBonus) {
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
    public boolean ordinaryCivilian() { return this == QOHOR_MAN || this == QOHOR_FARMHAND; }
    public boolean activeCombatant() { return combat != Combat.PASSIVE; }
    public boolean usesOutfitOverlay() { return this != QOHOR_SOLDIER && this != QOHOR_SOLDIER_ARCHER && this != QOHOR_BANNER_BEARER; }
    public static QohorNpcRole byId(String id) { return id == null ? QOHOR_MAN : BY_ID.getOrDefault(id.toLowerCase(Locale.ROOT), QOHOR_MAN); }
    public static QohorNpcRole findById(String id) { return id == null ? null : BY_ID.get(id.toLowerCase(Locale.ROOT)); }
    public static List<QohorNpcRole> spawnerOrder() { return SPAWNER_ORDER; }
    private static Map<String, QohorNpcRole> index() {
        Map<String, QohorNpcRole> roles = new LinkedHashMap<>();
        for (QohorNpcRole role : values()) roles.put(role.id, role);
        return Map.copyOf(roles);
    }
}
