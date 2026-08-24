package got.npc;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/** Complete Jogos Nhai roster and Tugar Khan's fixed legendary entry. */
public enum JogosNhaiNpcRole {
    JOGOS_NHAI_MAN("jogos_nhai_man", "Jogos Nhai", Gender.RANDOM, Combat.MELEE, Trade.NONE, 1, false, null, 1.0F, 1.0F),
    JOGOS_NHAI_ARCHER("jogos_nhai_archer", "Jogos Nhai Archer", Gender.RANDOM, Combat.HYBRID, Trade.NONE, 1, false, null, 1.0F, 1.0F),
    JOGOS_NHAI_CHIEFTAIN("jogos_nhai_chieftain", "Jogos Nhai Chieftain", Gender.MALE, Combat.MELEE, Trade.UNITS, 5, false, null, 1.0F, 1.0F),
    JOGOS_NHAI_SHAMAN("jogos_nhai_shaman", "Jogos Nhai Shaman", Gender.FEMALE, Combat.PASSIVE, Trade.BUTCHER, 2, false, null, 1.0F, 1.0F),
    TUGAR_KHAN("tugar_khan", "Tugar Khan", Gender.MALE, Combat.MELEE, Trade.NONE, 500, true, "tugar_khan", 1.3F, 1.3F);

    public enum Gender { RANDOM, MALE, FEMALE }
    public enum Combat { PASSIVE, MELEE, ARCHER, HYBRID }
    public enum Trade { NONE, BUTCHER, UNITS }

    private static final Map<String, JogosNhaiNpcRole> BY_ID = index();
    private static final List<JogosNhaiNpcRole> SPAWNER_ORDER = List.of(values());
    private final String id;
    private final String displayName;
    private final Gender gender;
    private final Combat combat;
    private final Trade trade;
    private final int alignmentBonus;
    private final boolean legendary;
    private final String legendaryTexture;
    private final float scale;
    private final float collisionScale;

    JogosNhaiNpcRole(String id, String displayName, Gender gender, Combat combat, Trade trade,
                     int alignmentBonus, boolean legendary, String legendaryTexture,
                     float scale, float collisionScale) {
        this.id = id;
        this.displayName = displayName;
        this.gender = gender;
        this.combat = combat;
        this.trade = trade;
        this.alignmentBonus = alignmentBonus;
        this.legendary = legendary;
        this.legendaryTexture = legendaryTexture;
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
    public String legendaryBaseSuffix() { return ""; }
    public String legendaryOverlaySuffix() { return ""; }
    public boolean hasLegendaryOverlay() { return false; }
    public float scale() { return scale; }
    public float collisionScale() { return collisionScale; }
    public boolean ordinaryCivilian() { return false; }
    public boolean activeCombatant() { return combat != Combat.PASSIVE; }
    public static JogosNhaiNpcRole byId(String id) { return id == null ? JOGOS_NHAI_MAN : BY_ID.getOrDefault(id.toLowerCase(Locale.ROOT), JOGOS_NHAI_MAN); }
    public static JogosNhaiNpcRole findById(String id) { return id == null ? null : BY_ID.get(id.toLowerCase(Locale.ROOT)); }
    public static List<JogosNhaiNpcRole> spawnerOrder() { return SPAWNER_ORDER; }
    private static Map<String, JogosNhaiNpcRole> index() {
        Map<String, JogosNhaiNpcRole> roles = new LinkedHashMap<>();
        for (JogosNhaiNpcRole role : values()) roles.put(role.id, role);
        return Map.copyOf(roles);
    }
}
