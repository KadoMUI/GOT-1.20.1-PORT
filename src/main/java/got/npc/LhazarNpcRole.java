package got.npc;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/** Complete Lhazari civic and military catalogue from the final 1.7.10 build. */
public enum LhazarNpcRole {
    LHAZAR_MAN("lhazar_man", "Lhazar Man", Gender.RANDOM, Combat.PASSIVE, Trade.NONE, 1),
    LHAZAR_LEVYMAN("lhazar_levyman", "Lhazar Levyman", Gender.MALE, Combat.MELEE, Trade.NONE, 2),
    LHAZAR_LEVYMAN_ARCHER("lhazar_levyman_archer", "Lhazar Levyman Archer", Gender.MALE, Combat.ARCHER, Trade.NONE, 2),
    LHAZAR_SOLDIER("lhazar_soldier", "Lhazar Warrior", Gender.MALE, Combat.MELEE, Trade.NONE, 2),
    LHAZAR_SOLDIER_ARCHER("lhazar_soldier_archer", "Lhazar Warrior Archer", Gender.MALE, Combat.ARCHER, Trade.NONE, 2),
    LHAZAR_BANNER_BEARER("lhazar_banner_bearer", "Lhazar Banner Bearer", Gender.MALE, Combat.MELEE, Trade.NONE, 2),
    LHAZAR_CAPTAIN("lhazar_captain", "Lhazar Captain", Gender.MALE, Combat.MELEE, Trade.UNITS, 5),
    LHAZAR_BLACKSMITH("lhazar_blacksmith", "Lhazar Blacksmith", Gender.MALE, Combat.PASSIVE, Trade.BLACKSMITH, 2),
    LHAZAR_GOLDSMITH("lhazar_goldsmith", "Lhazar Goldsmith", Gender.RANDOM, Combat.PASSIVE, Trade.GOLDSMITH, 2),
    LHAZAR_FARMER("lhazar_farmer", "Lhazar Farmer", Gender.MALE, Combat.PASSIVE, Trade.FARMER, 2),
    LHAZAR_FARMHAND("lhazar_farmhand", "Lhazar Farmhand", Gender.RANDOM, Combat.PASSIVE, Trade.NONE, 1),
    LHAZAR_BARTENDER("lhazar_bartender", "Lhazar Bartender", Gender.MALE, Combat.PASSIVE, Trade.BARTENDER, 2),
    LHAZAR_MINER("lhazar_miner", "Lhazar Miner", Gender.RANDOM, Combat.PASSIVE, Trade.MINER, 2),
    LHAZAR_LUMBERMAN("lhazar_lumberman", "Lhazar Lumberman", Gender.RANDOM, Combat.PASSIVE, Trade.LUMBERMAN, 2),
    LHAZAR_MASON("lhazar_mason", "Lhazar Mason", Gender.RANDOM, Combat.PASSIVE, Trade.MASON, 2),
    LHAZAR_BREWER("lhazar_brewer", "Lhazar Brewer", Gender.RANDOM, Combat.PASSIVE, Trade.BREWER, 2),
    LHAZAR_FLORIST("lhazar_florist", "Lhazar Florist", Gender.RANDOM, Combat.PASSIVE, Trade.FLORIST, 2),
    LHAZAR_BUTCHER("lhazar_butcher", "Lhazar Butcher", Gender.RANDOM, Combat.PASSIVE, Trade.BUTCHER, 2),
    LHAZAR_FISHMONGER("lhazar_fishmonger", "Lhazar Fishmonger", Gender.RANDOM, Combat.PASSIVE, Trade.FISHMONGER, 2),
    LHAZAR_BAKER("lhazar_baker", "Lhazar Baker", Gender.RANDOM, Combat.PASSIVE, Trade.BAKER, 2);

    public enum Gender { RANDOM, MALE, FEMALE }
    public enum Combat { PASSIVE, MELEE, ARCHER, HYBRID }
    public enum Trade { NONE, BAKER, BARTENDER, BLACKSMITH, BREWER, BUTCHER, FARMER, FISHMONGER, FLORIST, GOLDSMITH, LUMBERMAN, MASON, MINER, UNITS }

    private static final Map<String, LhazarNpcRole> BY_ID = index();
    private static final List<LhazarNpcRole> SPAWNER_ORDER = List.of(values());
    private final String id;
    private final String displayName;
    private final Gender gender;
    private final Combat combat;
    private final Trade trade;
    private final int alignmentBonus;

    LhazarNpcRole(String id, String displayName, Gender gender, Combat combat, Trade trade, int alignmentBonus) {
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
    public boolean ordinaryCivilian() { return this == LHAZAR_MAN || this == LHAZAR_FARMHAND; }
    public boolean activeCombatant() { return combat != Combat.PASSIVE; }
    public boolean usesOutfitOverlay() { return this != LHAZAR_SOLDIER && this != LHAZAR_SOLDIER_ARCHER && this != LHAZAR_BANNER_BEARER; }
    public static LhazarNpcRole byId(String id) { return id == null ? LHAZAR_MAN : BY_ID.getOrDefault(id.toLowerCase(Locale.ROOT), LHAZAR_MAN); }
    public static LhazarNpcRole findById(String id) { return id == null ? null : BY_ID.get(id.toLowerCase(Locale.ROOT)); }
    public static List<LhazarNpcRole> spawnerOrder() { return SPAWNER_ORDER; }
    private static Map<String, LhazarNpcRole> index() {
        Map<String, LhazarNpcRole> roles = new LinkedHashMap<>();
        for (LhazarNpcRole role : values()) roles.put(role.id, role);
        return Map.copyOf(roles);
    }
}
