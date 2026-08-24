package got.npc;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/** Complete Summer Isles civic and military catalogue from the final 1.7.10 build. */
public enum SummerIslesNpcRole {
    SUMMER_MAN("summer_man", "Summer Islander", Gender.RANDOM, Combat.PASSIVE, Trade.NONE, 1, NorvosNpcRole.NORVOS_MAN),
    SUMMER_LEVYMAN("summer_levyman", "Summer Levyman", Gender.MALE, Combat.MELEE, Trade.NONE, 2, NorvosNpcRole.NORVOS_LEVYMAN),
    SUMMER_LEVYMAN_ARCHER("summer_levyman_archer", "Summer Levyman Archer", Gender.MALE, Combat.ARCHER, Trade.NONE, 2, NorvosNpcRole.NORVOS_LEVYMAN_ARCHER),
    SUMMER_SOLDIER("summer_soldier", "Summer Soldier", Gender.MALE, Combat.MELEE, Trade.NONE, 2, NorvosNpcRole.NORVOS_SOLDIER),
    SUMMER_SOLDIER_ARCHER("summer_soldier_archer", "Summer Soldier Archer", Gender.MALE, Combat.ARCHER, Trade.NONE, 2, NorvosNpcRole.NORVOS_SOLDIER_ARCHER),
    SUMMER_BANNER_BEARER("summer_banner_bearer", "Summer Banner Bearer", Gender.MALE, Combat.MELEE, Trade.NONE, 2, NorvosNpcRole.NORVOS_BANNER_BEARER),
    SUMMER_CAPTAIN("summer_captain", "Summer Captain", Gender.MALE, Combat.MELEE, Trade.UNITS, 5, NorvosNpcRole.NORVOS_CAPTAIN),
    SUMMER_BLACKSMITH("summer_blacksmith", "Summer Blacksmith", Gender.MALE, Combat.PASSIVE, Trade.BLACKSMITH, 2, NorvosNpcRole.NORVOS_BLACKSMITH),
    SUMMER_BARTENDER("summer_bartender", "Summer Bartender", Gender.MALE, Combat.PASSIVE, Trade.BARTENDER, 2, NorvosNpcRole.NORVOS_BARTENDER),
    SUMMER_LUMBERMAN("summer_lumberman", "Summer Lumberman", Gender.RANDOM, Combat.PASSIVE, Trade.LUMBERMAN, 2, NorvosNpcRole.NORVOS_LUMBERMAN),
    SUMMER_MASON("summer_mason", "Summer Mason", Gender.RANDOM, Combat.PASSIVE, Trade.MASON, 2, NorvosNpcRole.NORVOS_MASON),
    SUMMER_BUTCHER("summer_butcher", "Summer Butcher", Gender.RANDOM, Combat.PASSIVE, Trade.BUTCHER, 2, NorvosNpcRole.NORVOS_BUTCHER),
    SUMMER_BREWER("summer_brewer", "Summer Brewer", Gender.RANDOM, Combat.PASSIVE, Trade.BREWER, 2, NorvosNpcRole.NORVOS_BREWER),
    SUMMER_FISHMONGER("summer_fishmonger", "Summer Fishmonger", Gender.RANDOM, Combat.PASSIVE, Trade.FISHMONGER, 2, NorvosNpcRole.NORVOS_FISHMONGER),
    SUMMER_BAKER("summer_baker", "Summer Baker", Gender.RANDOM, Combat.PASSIVE, Trade.BAKER, 2, NorvosNpcRole.NORVOS_BAKER),
    SUMMER_MINER("summer_miner", "Summer Miner", Gender.RANDOM, Combat.PASSIVE, Trade.MINER, 2, NorvosNpcRole.NORVOS_MINER),
    SUMMER_FARMHAND("summer_farmhand", "Summer Farmhand", Gender.RANDOM, Combat.PASSIVE, Trade.NONE, 1, NorvosNpcRole.NORVOS_FARMHAND),
    SUMMER_FARMER("summer_farmer", "Summer Farmer", Gender.MALE, Combat.PASSIVE, Trade.FARMER, 2, NorvosNpcRole.NORVOS_FARMER),
    SUMMER_FLORIST("summer_florist", "Summer Florist", Gender.RANDOM, Combat.PASSIVE, Trade.FLORIST, 2, NorvosNpcRole.NORVOS_FLORIST),
    SUMMER_GOLDSMITH("summer_goldsmith", "Summer Goldsmith", Gender.RANDOM, Combat.PASSIVE, Trade.GOLDSMITH, 2, NorvosNpcRole.NORVOS_GOLDSMITH);

    public enum Gender { RANDOM, MALE, FEMALE }
    public enum Combat { PASSIVE, MELEE, ARCHER }
    public enum Trade { NONE, BAKER, BARTENDER, BLACKSMITH, BREWER, BUTCHER, FARMER, FISHMONGER, FLORIST, GOLDSMITH, LUMBERMAN, MASON, MINER, UNITS }

    private static final Map<String, SummerIslesNpcRole> BY_ID = index();
    private static final List<SummerIslesNpcRole> SPAWNER_ORDER = List.of(values());
    private final String id;
    private final String displayName;
    private final Gender gender;
    private final Combat combat;
    private final Trade trade;
    private final int alignmentBonus;
    private final NorvosNpcRole parentRole;

    SummerIslesNpcRole(String id, String displayName, Gender gender, Combat combat, Trade trade,
                       int alignmentBonus, NorvosNpcRole parentRole) {
        this.id = id;
        this.displayName = displayName;
        this.gender = gender;
        this.combat = combat;
        this.trade = trade;
        this.alignmentBonus = alignmentBonus;
        this.parentRole = parentRole;
    }

    public String id() { return id; }
    public String displayName() { return displayName; }
    public Gender gender() { return gender; }
    public Combat combat() { return combat; }
    public Trade trade() { return trade; }
    public int alignmentBonus() { return alignmentBonus; }
    public NorvosNpcRole parentRole() { return parentRole; }
    public boolean ordinaryCivilian() { return this == SUMMER_MAN || this == SUMMER_FARMHAND; }
    public boolean activeCombatant() { return combat != Combat.PASSIVE; }
    public static SummerIslesNpcRole byId(String id) { return id == null ? SUMMER_MAN : BY_ID.getOrDefault(id.toLowerCase(Locale.ROOT), SUMMER_MAN); }
    public static List<SummerIslesNpcRole> spawnerOrder() { return SPAWNER_ORDER; }
    private static Map<String, SummerIslesNpcRole> index() {
        Map<String, SummerIslesNpcRole> roles = new LinkedHashMap<>();
        for (SummerIslesNpcRole role : values()) roles.put(role.id, role);
        return Map.copyOf(roles);
    }
}
