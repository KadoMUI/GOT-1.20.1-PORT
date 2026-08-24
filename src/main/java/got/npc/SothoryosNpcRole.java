package got.npc;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/** Complete Sothoryosi humanoid catalogue from the final 1.7.10 build. */
public enum SothoryosNpcRole {
    SOTHORYOS_MAN("sothoryos_man", "Sothoryos Man", Gender.RANDOM, Combat.PASSIVE, Trade.NONE, 1, NorvosNpcRole.NORVOS_MAN),
    SOTHORYOS_WARRIOR("sothoryos_warrior", "Sothoryos Warrior", Gender.MALE, Combat.MELEE, Trade.NONE, 2, NorvosNpcRole.NORVOS_SOLDIER),
    SOTHORYOS_BANNER_BEARER("sothoryos_banner_bearer", "Sothoryos Banner Bearer", Gender.MALE, Combat.MELEE, Trade.NONE, 2, NorvosNpcRole.NORVOS_BANNER_BEARER),
    SOTHORYOS_CHIEFTAIN("sothoryos_chieftain", "Sothoryos Chieftain", Gender.MALE, Combat.MELEE, Trade.UNITS, 5, NorvosNpcRole.NORVOS_CAPTAIN),
    SOTHORYOS_BLOWGUNNER("sothoryos_blowgunner", "Sothoryos Blowgunner", Gender.MALE, Combat.RANGED, Trade.NONE, 2, NorvosNpcRole.NORVOS_SOLDIER_ARCHER),
    SOTHORYOS_SHAMAN("sothoryos_shaman", "Sothoryos Shaman", Gender.MALE, Combat.PASSIVE, Trade.ALCHEMIST, 2, NorvosNpcRole.NORVOS_BLACKSMITH),
    SOTHORYOS_FARMER("sothoryos_farmer", "Sothoryos Farmer", Gender.MALE, Combat.PASSIVE, Trade.FARMER, 2, NorvosNpcRole.NORVOS_FARMER),
    SOTHORYOS_FARMHAND("sothoryos_farmhand", "Sothoryos Farmhand", Gender.RANDOM, Combat.PASSIVE, Trade.NONE, 1, NorvosNpcRole.NORVOS_FARMHAND),
    SOTHORYOS_SMITH("sothoryos_smith", "Sothoryos Smith", Gender.MALE, Combat.PASSIVE, Trade.BLACKSMITH, 2, NorvosNpcRole.NORVOS_BLACKSMITH);

    public enum Gender { RANDOM, MALE, FEMALE }
    public enum Combat { PASSIVE, MELEE, RANGED }
    public enum Trade { NONE, ALCHEMIST, BLACKSMITH, FARMER, UNITS }

    private static final Map<String, SothoryosNpcRole> BY_ID = index();
    private static final List<SothoryosNpcRole> SPAWNER_ORDER = List.of(values());
    private final String id;
    private final String displayName;
    private final Gender gender;
    private final Combat combat;
    private final Trade trade;
    private final int alignmentBonus;
    private final NorvosNpcRole parentRole;

    SothoryosNpcRole(String id, String displayName, Gender gender, Combat combat, Trade trade,
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
    public boolean ordinaryCivilian() { return this == SOTHORYOS_MAN || this == SOTHORYOS_FARMHAND; }
    public boolean activeCombatant() { return combat != Combat.PASSIVE; }
    public static SothoryosNpcRole byId(String id) { return id == null ? SOTHORYOS_MAN : BY_ID.getOrDefault(id.toLowerCase(Locale.ROOT), SOTHORYOS_MAN); }
    public static List<SothoryosNpcRole> spawnerOrder() { return SPAWNER_ORDER; }
    private static Map<String, SothoryosNpcRole> index() {
        Map<String, SothoryosNpcRole> roles = new LinkedHashMap<>();
        for (SothoryosNpcRole role : values()) roles.put(role.id, role);
        return Map.copyOf(roles);
    }
}
