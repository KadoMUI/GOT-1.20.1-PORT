package got.npc;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/** Complete Mossovy catalogue from the final 1.7.10 build. */
public enum MossovyNpcRole {
    MOSSOVY_MAN("mossovy_man", "Mossovy Man", Gender.RANDOM, Combat.PASSIVE, Trade.NONE, 1),
    MOSSOVY_WITCHER("mossovy_witcher", "Mossovy Witcher", Gender.MALE, Combat.HYBRID, Trade.MERCENARY, 5),
    MOSSOVY_BLACKSMITH("mossovy_blacksmith", "Mossovy Blacksmith", Gender.MALE, Combat.PASSIVE, Trade.BLACKSMITH, 2),
    MOSSOVY_GOLDSMITH("mossovy_goldsmith", "Mossovy Goldsmith", Gender.RANDOM, Combat.PASSIVE, Trade.GOLDSMITH, 2),
    MOSSOVY_BARTENDER("mossovy_bartender", "Mossovy Bartender", Gender.RANDOM, Combat.PASSIVE, Trade.BARTENDER, 2);

    public enum Gender { RANDOM, MALE, FEMALE }
    public enum Combat { PASSIVE, MELEE, ARCHER, HYBRID }
    public enum Trade { NONE, BARTENDER, BLACKSMITH, GOLDSMITH, MERCENARY }

    private static final Map<String, MossovyNpcRole> BY_ID = index();
    private static final List<MossovyNpcRole> SPAWNER_ORDER = List.of(values());
    private final String id;
    private final String displayName;
    private final Gender gender;
    private final Combat combat;
    private final Trade trade;
    private final int alignmentBonus;

    MossovyNpcRole(String id, String displayName, Gender gender, Combat combat,
                   Trade trade, int alignmentBonus) {
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
    public boolean ordinaryCivilian() { return this == MOSSOVY_MAN; }
    public boolean activeCombatant() { return combat != Combat.PASSIVE; }

    public static MossovyNpcRole byId(String id) {
        return id == null ? MOSSOVY_MAN
                : BY_ID.getOrDefault(id.toLowerCase(Locale.ROOT), MOSSOVY_MAN);
    }

    public static MossovyNpcRole findById(String id) {
        return id == null ? null : BY_ID.get(id.toLowerCase(Locale.ROOT));
    }

    public static List<MossovyNpcRole> spawnerOrder() { return SPAWNER_ORDER; }

    private static Map<String, MossovyNpcRole> index() {
        Map<String, MossovyNpcRole> roles = new LinkedHashMap<>();
        for (MossovyNpcRole role : values()) roles.put(role.id, role);
        return Map.copyOf(roles);
    }
}
