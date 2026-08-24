package got.npc;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Crownlands population catalogue recovered from the original 1.7.10 entity
 * hierarchy and its fixed-waypoint registrations.
 */
public enum CrownlandsNpcRole {
    CROWNLANDS_MAN("crownlands_man", "Crownlands Civilian", Gender.RANDOM, Combat.PASSIVE, Trade.NONE, 1, false, null, "", "", 1.0F, 1.0F),
    CROWNLANDS_LEVYMAN("crownlands_levyman", "Crownlands Levyman", Gender.MALE, Combat.MELEE, Trade.NONE, 2, false, null, "", "", 1.0F, 1.0F),
    CROWNLANDS_LEVYMAN_ARCHER("crownlands_levyman_archer", "Crownlands Levyman Archer", Gender.MALE, Combat.ARCHER, Trade.NONE, 2, false, null, "", "", 1.0F, 1.0F),
    CROWNLANDS_GUARD("crownlands_guard", "King's Landing Guard", Gender.MALE, Combat.MELEE, Trade.NONE, 2, false, null, "", "", 1.0F, 1.0F),
    CROWNLANDS_CAPTAIN("crownlands_captain", "Crownlands Captain", Gender.MALE, Combat.MELEE, Trade.UNITS, 5, false, null, "", "", 1.0F, 1.0F),
    CROWNLANDS_ALCHEMIST("crownlands_alchemist", "Crownlands Alchemist", Gender.RANDOM, Combat.PASSIVE, Trade.ALCHEMIST, 2, false, null, "", "", 1.0F, 1.0F),
    CROWNLANDS_BLACKSMITH("crownlands_blacksmith", "Crownlands Blacksmith", Gender.MALE, Combat.PASSIVE, Trade.BLACKSMITH, 2, false, null, "", "", 1.0F, 1.0F),
    CROWNLANDS_GOLDSMITH("crownlands_goldsmith", "Crownlands Goldsmith", Gender.RANDOM, Combat.PASSIVE, Trade.GOLDSMITH, 2, false, null, "", "", 1.0F, 1.0F),
    CROWNLANDS_FARMER("crownlands_farmer", "Crownlands Farmer", Gender.MALE, Combat.PASSIVE, Trade.FARMER, 2, false, null, "", "", 1.0F, 1.0F),
    CROWNLANDS_FARMHAND("crownlands_farmhand", "Crownlands Farmhand", Gender.RANDOM, Combat.PASSIVE, Trade.NONE, 1, false, null, "", "", 1.0F, 1.0F),
    CROWNLANDS_BARTENDER("crownlands_bartender", "Crownlands Bartender", Gender.MALE, Combat.PASSIVE, Trade.BARTENDER, 2, false, null, "", "", 1.0F, 1.0F),
    CROWNLANDS_MINER("crownlands_miner", "Crownlands Miner", Gender.RANDOM, Combat.PASSIVE, Trade.MINER, 2, false, null, "", "", 1.0F, 1.0F),
    CROWNLANDS_LUMBERMAN("crownlands_lumberman", "Crownlands Lumberman", Gender.RANDOM, Combat.PASSIVE, Trade.LUMBERMAN, 2, false, null, "", "", 1.0F, 1.0F),
    CROWNLANDS_MASON("crownlands_mason", "Crownlands Mason", Gender.RANDOM, Combat.PASSIVE, Trade.MASON, 2, false, null, "", "", 1.0F, 1.0F),
    CROWNLANDS_BREWER("crownlands_brewer", "Crownlands Brewer", Gender.RANDOM, Combat.PASSIVE, Trade.BREWER, 2, false, null, "", "", 1.0F, 1.0F),
    CROWNLANDS_FLORIST("crownlands_florist", "Crownlands Florist", Gender.RANDOM, Combat.PASSIVE, Trade.FLORIST, 2, false, null, "", "", 1.0F, 1.0F),
    CROWNLANDS_BUTCHER("crownlands_butcher", "Crownlands Butcher", Gender.RANDOM, Combat.PASSIVE, Trade.BUTCHER, 2, false, null, "", "", 1.0F, 1.0F),
    CROWNLANDS_FISHMONGER("crownlands_fishmonger", "Crownlands Fishmonger", Gender.RANDOM, Combat.PASSIVE, Trade.FISHMONGER, 2, false, null, "", "", 1.0F, 1.0F),
    CROWNLANDS_BAKER("crownlands_baker", "Crownlands Baker", Gender.RANDOM, Combat.PASSIVE, Trade.BAKER, 2, false, null, "", "", 1.0F, 1.0F),
    KINGSGUARD("kingsguard", "Kingsguard", Gender.MALE, Combat.MELEE, Trade.NONE, 10, false, null, "", "", 1.0F, 1.0F),

    SANSA_STARK("sansa_stark", "Sansa Stark", Gender.FEMALE, Combat.PASSIVE, Trade.NONE, 0, true, "sansa_stark", "", "", 1.0F, 1.0F),
    SHAE("shae", "Shae", Gender.FEMALE, Combat.PASSIVE, Trade.NONE, 1, true, "shae", "", "", 1.0F, 1.0F),
    YOREN("yoren", "Yoren", Gender.MALE, Combat.MELEE, Trade.NONE, 100, true, "yoren", "", "", 1.0F, 1.0F),
    SANDOR_CLEGANE("sandor_clegane", "Sandor Clegane", Gender.MALE, Combat.MELEE, Trade.NONE, 200, true, "sandor_clegane", "_1", "_2", 1.0F, 1.0F),
    JOFFREY_BARATHEON("joffrey_baratheon", "Joffrey Baratheon", Gender.MALE, Combat.HYBRID, Trade.NONE, 500, true, "joffrey_baratheon", "_1", "_2", 0.9F, 0.9F),
    CERSEI_LANNISTER("cersei_lannister", "Cersei Lannister", Gender.FEMALE, Combat.MELEE, Trade.NONE, 100, true, "cersei_lannister", "", "", 1.0F, 1.0F),
    JAIME_LANNISTER("jaime_lannister", "Jaime Lannister", Gender.MALE, Combat.MELEE, Trade.NONE, 300, true, "jaime_lannister", "_1", "_2", 1.0F, 1.0F),
    PYCELLE("pycelle", "Pycelle", Gender.MALE, Combat.MELEE, Trade.MAESTER, 200, true, "pycelle", "", "", 1.0F, 1.0F),
    JANOS_SLYNT("janos_slynt", "Janos Slynt", Gender.MALE, Combat.MELEE, Trade.UNITS, 200, true, "janos_slynt", "", "", 1.0F, 1.0F),
    VARYS("varys", "Varys", Gender.MALE, Combat.MELEE, Trade.NONE, 200, true, "varys", "", "", 1.0F, 1.0F),
    ILYN_PAYNE("ilyn_payne", "Ilyn Payne", Gender.MALE, Combat.MELEE, Trade.NONE, 100, true, "ilyn_payne", "", "", 1.0F, 1.0F),
    HIGH_SEPTON("high_septon", "High Septon", Gender.MALE, Combat.MELEE, Trade.MAESTER, 100, true, "high_septon", "_1", "_2", 1.0F, 1.0F),
    TOMMEN_BARATHEON("tommen_baratheon", "Tommen Baratheon", Gender.MALE, Combat.PASSIVE, Trade.NONE, 500, true, "tommen_baratheon", "", "", 0.75F, 0.75F),
    MYRCELLA_BARATHEON("myrcella_baratheon", "Myrcella Baratheon", Gender.FEMALE, Combat.PASSIVE, Trade.NONE, 500, true, "myrcella_baratheon", "", "", 0.9F, 0.9F),
    MERYN_TRANT("meryn_trant", "Meryn Trant", Gender.MALE, Combat.MELEE, Trade.NONE, 100, true, "meryn_trant", "", "", 1.0F, 1.0F),
    BARRISTAN_SELMY("barristan_selmy", "Barristan Selmy", Gender.MALE, Combat.MELEE, Trade.UNITS, 200, true, "barristan_selmy", "", "", 1.0F, 1.0F),
    PETYR_BAELISH("petyr_baelish", "Petyr Baelish", Gender.MALE, Combat.MELEE, Trade.BARTENDER, 300, true, "petyr_baelish", "", "", 1.0F, 1.0F),
    TYRION_LANNISTER("tyrion_lannister", "Tyrion Lannister", Gender.MALE, Combat.MELEE, Trade.NONE, 300, true, "tyrion_lannister", "", "", 0.72F, 0.72F),
    LANCEL_LANNISTER("lancel_lannister", "Lancel Lannister", Gender.MALE, Combat.MELEE, Trade.NONE, 100, true, "lancel_lannister", "_1", "_2", 1.0F, 1.0F),
    BRONN("bronn", "Bronn", Gender.MALE, Combat.MELEE, Trade.UNITS, 0, true, "bronn", "", "", 1.0F, 1.0F),
    PODRICK_PAYNE("podrick_payne", "Podrick Payne", Gender.MALE, Combat.MELEE, Trade.NONE, 100, true, "podrick_payne", "", "", 1.0F, 1.0F),
    TOBHO_MOTT("tobho_mott", "Tobho Mott", Gender.MALE, Combat.MELEE, Trade.BLACKSMITH, 10, true, "tobho_mott", "", "", 1.0F, 1.0F),
    GENDRY_BARATHEON("gendry_baratheon", "Gendry Baratheon", Gender.MALE, Combat.MELEE, Trade.BLACKSMITH, 0, true, "gendry_baratheon", "", "", 1.0F, 1.0F);

    public enum Gender { RANDOM, MALE, FEMALE }
    public enum Combat { PASSIVE, MELEE, ARCHER, HYBRID }
    public enum Trade {
        NONE, ALCHEMIST, BAKER, BARTENDER, BLACKSMITH, BREWER, BUTCHER, FARMER,
        FISHMONGER, FLORIST, GOLDSMITH, LUMBERMAN, MASON, MINER,
        UNITS, MAESTER
    }

    private static final Map<String, CrownlandsNpcRole> BY_ID = index();
    private static final List<CrownlandsNpcRole> SPAWNER_ORDER = List.of(values());

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

    CrownlandsNpcRole(String id, String displayName, Gender gender, Combat combat, Trade trade,
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
    public boolean ordinaryCivilian() { return this == CROWNLANDS_MAN; }
    public boolean activeCombatant() { return combat != Combat.PASSIVE; }

    public static CrownlandsNpcRole byId(String id) {
        if (id == null) return CROWNLANDS_MAN;
        return BY_ID.getOrDefault(id.toLowerCase(Locale.ROOT), CROWNLANDS_MAN);
    }

    public static CrownlandsNpcRole findById(String id) {
        return id == null ? null : BY_ID.get(id.toLowerCase(Locale.ROOT));
    }

    public static List<CrownlandsNpcRole> spawnerOrder() { return SPAWNER_ORDER; }

    private static Map<String, CrownlandsNpcRole> index() {
        Map<String, CrownlandsNpcRole> roles = new LinkedHashMap<>();
        for (CrownlandsNpcRole role : values()) roles.put(role.id, role);
        return Map.copyOf(roles);
    }
}
