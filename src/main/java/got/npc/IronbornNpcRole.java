package got.npc;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Ironborn population catalogue recovered from the original 1.7.10 entity
 * hierarchy and its fixed-waypoint registrations.
 */
public enum IronbornNpcRole {
    IRONBORN_MAN("ironborn_man", "Ironborn Civilian", Gender.RANDOM, Combat.PASSIVE, Trade.NONE, 1, false, null, "", "", 1.0F, 1.0F),
    IRONBORN_LEVYMAN("ironborn_levyman", "Ironborn Levyman", Gender.MALE, Combat.MELEE, Trade.NONE, 2, false, null, "", "", 1.0F, 1.0F),
    IRONBORN_LEVYMAN_ARCHER("ironborn_levyman_archer", "Ironborn Levyman Archer", Gender.MALE, Combat.ARCHER, Trade.NONE, 2, false, null, "", "", 1.0F, 1.0F),
    IRONBORN_SOLDIER("ironborn_soldier", "Ironborn Soldier", Gender.MALE, Combat.MELEE, Trade.NONE, 2, false, null, "", "", 1.0F, 1.0F),
    IRONBORN_SOLDIER_ARCHER("ironborn_soldier_archer", "Ironborn Soldier Archer", Gender.MALE, Combat.ARCHER, Trade.NONE, 2, false, null, "", "", 1.0F, 1.0F),
    IRONBORN_PRIEST("ironborn_priest", "Drowned Priest", Gender.MALE, Combat.MELEE, Trade.PRIEST, 2, false, null, "", "", 1.0F, 1.0F),
    IRONBORN_BANNER_BEARER("ironborn_banner_bearer", "Ironborn Banner Bearer", Gender.MALE, Combat.MELEE, Trade.NONE, 2, false, null, "", "", 1.0F, 1.0F),
    IRONBORN_CAPTAIN("ironborn_captain", "Ironborn Captain", Gender.MALE, Combat.MELEE, Trade.UNITS, 5, false, null, "", "", 1.0F, 1.0F),
    IRONBORN_BLACKSMITH("ironborn_blacksmith", "Ironborn Blacksmith", Gender.MALE, Combat.PASSIVE, Trade.BLACKSMITH, 2, false, null, "", "", 1.0F, 1.0F),
    IRONBORN_GOLDSMITH("ironborn_goldsmith", "Ironborn Goldsmith", Gender.RANDOM, Combat.PASSIVE, Trade.GOLDSMITH, 2, false, null, "", "", 1.0F, 1.0F),
    IRONBORN_FARMER("ironborn_farmer", "Ironborn Farmer", Gender.MALE, Combat.PASSIVE, Trade.FARMER, 2, false, null, "", "", 1.0F, 1.0F),
    IRONBORN_FARMHAND("ironborn_farmhand", "Ironborn Farmhand", Gender.RANDOM, Combat.PASSIVE, Trade.NONE, 1, false, null, "", "", 1.0F, 1.0F),
    IRONBORN_BARTENDER("ironborn_bartender", "Ironborn Bartender", Gender.MALE, Combat.PASSIVE, Trade.BARTENDER, 2, false, null, "", "", 1.0F, 1.0F),
    IRONBORN_MINER("ironborn_miner", "Ironborn Miner", Gender.RANDOM, Combat.PASSIVE, Trade.MINER, 2, false, null, "", "", 1.0F, 1.0F),
    IRONBORN_LUMBERMAN("ironborn_lumberman", "Ironborn Lumberman", Gender.RANDOM, Combat.PASSIVE, Trade.LUMBERMAN, 2, false, null, "", "", 1.0F, 1.0F),
    IRONBORN_MASON("ironborn_mason", "Ironborn Mason", Gender.RANDOM, Combat.PASSIVE, Trade.MASON, 2, false, null, "", "", 1.0F, 1.0F),
    IRONBORN_BREWER("ironborn_brewer", "Ironborn Brewer", Gender.RANDOM, Combat.PASSIVE, Trade.BREWER, 2, false, null, "", "", 1.0F, 1.0F),
    IRONBORN_FLORIST("ironborn_florist", "Ironborn Florist", Gender.RANDOM, Combat.PASSIVE, Trade.FLORIST, 2, false, null, "", "", 1.0F, 1.0F),
    IRONBORN_BUTCHER("ironborn_butcher", "Ironborn Butcher", Gender.RANDOM, Combat.PASSIVE, Trade.BUTCHER, 2, false, null, "", "", 1.0F, 1.0F),
    IRONBORN_FISHMONGER("ironborn_fishmonger", "Ironborn Fishmonger", Gender.RANDOM, Combat.PASSIVE, Trade.FISHMONGER, 2, false, null, "", "", 1.0F, 1.0F),
    IRONBORN_BAKER("ironborn_baker", "Ironborn Baker", Gender.RANDOM, Combat.PASSIVE, Trade.BAKER, 2, false, null, "", "", 1.0F, 1.0F),

    BAELOR_BLACKTYDE("baelor_blacktyde", "Baelor Blacktyde", Gender.MALE, Combat.MELEE, Trade.UNITS, 100, true, "baelor_blacktyde", "", "", 1.0F, 1.0F),
    DUNSTAN_DRUMM("dunstan_drumm", "Dunstan Drumm", Gender.MALE, Combat.MELEE, Trade.UNITS, 100, true, "dunstan_drumm", "", "", 1.0F, 1.0F),
    ANDRIK_THE_UNSMILING("andrik_the_unsmiling", "Andrik the Unsmiling", Gender.MALE, Combat.MELEE, Trade.NONE, 50, true, "andrik_the_unsmilling", "", "", 1.0F, 1.0F),
    HARRAS_HARLAW("harras_harlaw", "Harras Harlaw", Gender.MALE, Combat.MELEE, Trade.UNITS, 100, true, "harras_harlaw", "", "", 1.0F, 1.0F),
    GOROLD_GOODBROTHER("gorold_goodbrother", "Gorold Goodbrother", Gender.MALE, Combat.MELEE, Trade.UNITS, 100, true, "gorold_goodbrother", "", "", 1.0F, 1.0F),
    GYLBERT_FARWYND("gylbert_farwynd", "Gylbert Farwynd", Gender.MALE, Combat.MELEE, Trade.UNITS, 100, true, "gylbert_farwynd", "", "", 1.0F, 1.0F),
    DAGMER("dagmer", "Dagmer", Gender.MALE, Combat.MELEE, Trade.NONE, 10, true, "dagmer", "", "", 1.0F, 1.0F),
    AERON_GREYJOY("aeron_greyjoy", "Aeron Greyjoy", Gender.MALE, Combat.MELEE, Trade.PRIEST, 100, true, "aeron_greyjoy", "", "", 1.0F, 1.0F),
    BALON_GREYJOY("balon_greyjoy", "Balon Greyjoy", Gender.MALE, Combat.MELEE, Trade.NONE, 500, true, "balon_greyjoy", "_1", "_2", 1.0F, 1.0F),
    YARA_GREYJOY("yara_greyjoy", "Yara Greyjoy", Gender.FEMALE, Combat.MELEE, Trade.UNITS, 300, true, "yara_greyjoy", "_1", "_2", 1.0F, 1.0F),
    THEON_GREYJOY("theon_greyjoy", "Theon Greyjoy", Gender.MALE, Combat.MELEE, Trade.NONE, 300, true, "theon_greyjoy", "_1", "_2", 1.0F, 1.0F),
    ERIK_IRONMAKER("erik_ironmaker", "Erik Ironmaker", Gender.MALE, Combat.MELEE, Trade.UNITS, 100, true, "erik_ironmaker", "", "", 1.0F, 1.0F),
    RODRIK_HARLAW("rodrik_harlaw", "Rodrik Harlaw", Gender.MALE, Combat.MELEE, Trade.UNITS, 100, true, "rodrik_harlaw", "", "", 1.0F, 1.0F),
    MARON_VOLMARK("maron_volmark", "Maron Volmark", Gender.MALE, Combat.MELEE, Trade.UNITS, 100, true, "maron_volmark", "", "", 1.0F, 1.0F),
    EURON_GREYJOY("euron_greyjoy", "Euron Greyjoy", Gender.MALE, Combat.MELEE, Trade.NONE, 0, true, "euron_greyjoy", "_1", "_2", 1.0F, 1.0F),
    VICTARION_GREYJOY("victarion_greyjoy", "Victarion Greyjoy", Gender.MALE, Combat.MELEE, Trade.NONE, 200, true, "victarion_greyjoy", "_1", "_2", 1.0F, 1.0F);

    public enum Gender { RANDOM, MALE, FEMALE }
    public enum Combat { PASSIVE, MELEE, ARCHER, HYBRID }
    public enum Trade {
        NONE, BAKER, BARTENDER, BLACKSMITH, BREWER, BUTCHER, FARMER,
        FISHMONGER, FLORIST, GOLDSMITH, LUMBERMAN, MASON, MINER,
        UNITS, MAESTER, PRIEST
    }

    private static final Map<String, IronbornNpcRole> BY_ID = index();
    private static final List<IronbornNpcRole> SPAWNER_ORDER = List.of(values());

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

    IronbornNpcRole(String id, String displayName, Gender gender, Combat combat, Trade trade,
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
    public boolean ordinaryCivilian() { return this == IRONBORN_MAN; }
    public boolean activeCombatant() { return combat != Combat.PASSIVE; }

    public static IronbornNpcRole byId(String id) {
        if (id == null) return IRONBORN_MAN;
        return BY_ID.getOrDefault(id.toLowerCase(Locale.ROOT), IRONBORN_MAN);
    }

    public static IronbornNpcRole findById(String id) {
        return id == null ? null : BY_ID.get(id.toLowerCase(Locale.ROOT));
    }

    public static List<IronbornNpcRole> spawnerOrder() { return SPAWNER_ORDER; }

    private static Map<String, IronbornNpcRole> index() {
        Map<String, IronbornNpcRole> roles = new LinkedHashMap<>();
        for (IronbornNpcRole role : values()) roles.put(role.id, role);
        return Map.copyOf(roles);
    }
}
