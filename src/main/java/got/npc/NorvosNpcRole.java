package got.npc;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/** Complete Norvosi civic and military catalogue from the final 1.7.10 build. */
public enum NorvosNpcRole {
    NORVOS_MAN("norvos_man", "Norvos Man", Gender.RANDOM, Combat.PASSIVE, Trade.NONE, 1),
    NORVOS_LEVYMAN("norvos_levyman", "Norvos Levyman", Gender.MALE, Combat.MELEE, Trade.NONE, 2),
    NORVOS_LEVYMAN_ARCHER("norvos_levyman_archer", "Norvos Levyman Archer", Gender.MALE, Combat.ARCHER, Trade.NONE, 2),
    NORVOS_SOLDIER("norvos_soldier", "Norvos Warrior", Gender.MALE, Combat.MELEE, Trade.NONE, 2),
    NORVOS_SOLDIER_ARCHER("norvos_soldier_archer", "Norvos Warrior Archer", Gender.MALE, Combat.ARCHER, Trade.NONE, 2),
    NORVOS_BANNER_BEARER("norvos_banner_bearer", "Norvos Banner Bearer", Gender.MALE, Combat.MELEE, Trade.NONE, 2),
    NORVOS_CAPTAIN("norvos_captain", "Norvos Captain", Gender.MALE, Combat.MELEE, Trade.UNITS, 5),
    NORVOS_BLACKSMITH("norvos_blacksmith", "Norvos Blacksmith", Gender.MALE, Combat.PASSIVE, Trade.BLACKSMITH, 2),
    NORVOS_GOLDSMITH("norvos_goldsmith", "Norvos Goldsmith", Gender.RANDOM, Combat.PASSIVE, Trade.GOLDSMITH, 2),
    NORVOS_FARMER("norvos_farmer", "Norvos Farmer", Gender.MALE, Combat.PASSIVE, Trade.FARMER, 2),
    NORVOS_FARMHAND("norvos_farmhand", "Norvos Farmhand", Gender.RANDOM, Combat.PASSIVE, Trade.NONE, 1),
    NORVOS_BARTENDER("norvos_bartender", "Norvos Bartender", Gender.MALE, Combat.PASSIVE, Trade.BARTENDER, 2),
    NORVOS_MINER("norvos_miner", "Norvos Miner", Gender.RANDOM, Combat.PASSIVE, Trade.MINER, 2),
    NORVOS_LUMBERMAN("norvos_lumberman", "Norvos Lumberman", Gender.RANDOM, Combat.PASSIVE, Trade.LUMBERMAN, 2),
    NORVOS_MASON("norvos_mason", "Norvos Mason", Gender.RANDOM, Combat.PASSIVE, Trade.MASON, 2),
    NORVOS_BREWER("norvos_brewer", "Norvos Brewer", Gender.RANDOM, Combat.PASSIVE, Trade.BREWER, 2),
    NORVOS_FLORIST("norvos_florist", "Norvos Florist", Gender.RANDOM, Combat.PASSIVE, Trade.FLORIST, 2),
    NORVOS_BUTCHER("norvos_butcher", "Norvos Butcher", Gender.RANDOM, Combat.PASSIVE, Trade.BUTCHER, 2),
    NORVOS_FISHMONGER("norvos_fishmonger", "Norvos Fishmonger", Gender.RANDOM, Combat.PASSIVE, Trade.FISHMONGER, 2),
    NORVOS_BAKER("norvos_baker", "Norvos Baker", Gender.RANDOM, Combat.PASSIVE, Trade.BAKER, 2);

    public enum Gender { RANDOM, MALE, FEMALE }
    public enum Combat { PASSIVE, MELEE, ARCHER, HYBRID }
    public enum Trade { NONE, BAKER, BARTENDER, BLACKSMITH, BREWER, BUTCHER, FARMER, FISHMONGER, FLORIST, GOLDSMITH, LUMBERMAN, MASON, MINER, UNITS }

    private static final Map<String, NorvosNpcRole> BY_ID = index();
    private static final List<NorvosNpcRole> SPAWNER_ORDER = List.of(values());
    private final String id;
    private final String displayName;
    private final Gender gender;
    private final Combat combat;
    private final Trade trade;
    private final int alignmentBonus;

    NorvosNpcRole(String id, String displayName, Gender gender, Combat combat, Trade trade, int alignmentBonus) {
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
    public boolean ordinaryCivilian() { return this == NORVOS_MAN || this == NORVOS_FARMHAND; }
    public boolean activeCombatant() { return combat != Combat.PASSIVE; }
    public boolean usesOutfitOverlay() { return this != NORVOS_SOLDIER && this != NORVOS_SOLDIER_ARCHER && this != NORVOS_BANNER_BEARER; }
    public static NorvosNpcRole byId(String id) { return id == null ? NORVOS_MAN : BY_ID.getOrDefault(id.toLowerCase(Locale.ROOT), NORVOS_MAN); }
    public static NorvosNpcRole findById(String id) { return id == null ? null : BY_ID.get(id.toLowerCase(Locale.ROOT)); }
    public static List<NorvosNpcRole> spawnerOrder() { return SPAWNER_ORDER; }
    private static Map<String, NorvosNpcRole> index() {
        Map<String, NorvosNpcRole> roles = new LinkedHashMap<>();
        for (NorvosNpcRole role : values()) roles.put(role.id, role);
        return Map.copyOf(roles);
    }
}
