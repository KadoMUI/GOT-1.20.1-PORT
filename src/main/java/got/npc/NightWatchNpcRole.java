package got.npc;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/** Night's Watch and Gift population recovered from the final 1.7.10 build. */
public enum NightWatchNpcRole {
    GIFT_MAN("gift_man", "Gift Civilian", Gender.RANDOM, Combat.PASSIVE, Trade.NONE, 1, false, null, "", "", 1.0F, 1.0F),
    GIFT_GUARD("gift_guard", "Night's Watch Guard", Gender.MALE, Combat.MELEE, Trade.NONE, 2, false, null, "", "", 1.0F, 1.0F),
    GIFT_ARCHER("gift_archer", "Night's Watch Archer", Gender.MALE, Combat.ARCHER, Trade.NONE, 2, false, null, "", "", 1.0F, 1.0F),
    GIFT_BANNER_BEARER("gift_banner_bearer", "Night's Watch Banner Bearer", Gender.MALE, Combat.MELEE, Trade.NONE, 2, false, null, "", "", 1.0F, 1.0F),
    GIFT_BLACKSMITH("gift_blacksmith", "Night's Watch Blacksmith", Gender.MALE, Combat.PASSIVE, Trade.BLACKSMITH, 2, false, null, "", "", 1.0F, 1.0F),

    JEOR_MORMONT("jeor_mormont", "Jeor Mormont", Gender.MALE, Combat.MELEE, Trade.UNITS, 300, true, "jeor_mormont", "_1", "_2", 1.0F, 1.0F),
    JON_SNOW("jon_snow", "Jon Snow", Gender.MALE, Combat.MELEE, Trade.NONE, 500, true, "jon_snow", "_1", "_2", 1.0F, 1.0F),
    AEMON_TARGARYEN("aemon_targaryen", "Maester Aemon", Gender.MALE, Combat.PASSIVE, Trade.MAESTER, 100, true, "aemon", "", "", 1.0F, 1.0F),
    ALLISER_THORNE("alliser_thorne", "Alliser Thorne", Gender.MALE, Combat.MELEE, Trade.NONE, 100, true, "alliser_thorne", "_1", "_2", 1.0F, 1.0F),
    EDD("edd", "Dolorous Edd", Gender.MALE, Combat.MELEE, Trade.NONE, 30, true, "edd", "_1", "_2", 1.0F, 1.0F),
    SAMWELL_TARLY("samwell_tarly", "Samwell Tarly", Gender.MALE, Combat.PASSIVE, Trade.NONE, 10, true, "samwell_tarly", "_1", "_2", 1.0F, 1.0F),
    COTTER_PYKE("cotter_pyke", "Cotter Pyke", Gender.MALE, Combat.MELEE, Trade.UNITS, 100, true, "cotter_pyke", "_1", "_2", 1.0F, 1.0F),
    HARMUNE("harmune", "Harmune", Gender.MALE, Combat.MELEE, Trade.BLACKSMITH, 100, true, "harmune", "", "", 1.0F, 1.0F),
    DENYS_MALLISTER("denys_mallister", "Denys Mallister", Gender.MALE, Combat.MELEE, Trade.UNITS, 100, true, "denys_mallister", "_1", "_2", 1.0F, 1.0F),
    MULLIN("mullin", "Mullin", Gender.MALE, Combat.MELEE, Trade.BLACKSMITH, 100, true, "mullin", "", "", 1.0F, 1.0F),
    BENJEN_STARK("benjen_stark", "Benjen Stark", Gender.MALE, Combat.MELEE, Trade.NONE, 100, true, "benjen_stark", "_1", "_2", 1.0F, 1.0F),
    YOREN("yoren", "Yoren", Gender.MALE, Combat.MELEE, Trade.NONE, 100, true, "yoren", "", "", 1.0F, 1.0F);

    public enum Gender { RANDOM, MALE, FEMALE }
    public enum Combat { PASSIVE, MELEE, ARCHER }
    public enum Trade { NONE, BLACKSMITH, UNITS, MAESTER }

    private static final Map<String, NightWatchNpcRole> BY_ID = index();
    private static final List<NightWatchNpcRole> SPAWNER_ORDER = List.of(values());
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

    NightWatchNpcRole(String id, String displayName, Gender gender, Combat combat, Trade trade,
                      int alignmentBonus, boolean legendary, String legendaryTexture,
                      String baseSuffix, String overlaySuffix, float scale, float collisionScale) {
        this.id=id; this.displayName=displayName; this.gender=gender; this.combat=combat; this.trade=trade;
        this.alignmentBonus=alignmentBonus; this.legendary=legendary; this.legendaryTexture=legendaryTexture;
        this.legendaryBaseSuffix=baseSuffix; this.legendaryOverlaySuffix=overlaySuffix;
        this.scale=scale; this.collisionScale=collisionScale;
    }
    public String id(){return id;} public String displayName(){return displayName;}
    public Gender gender(){return gender;} public Combat combat(){return combat;} public Trade trade(){return trade;}
    public int alignmentBonus(){return alignmentBonus;} public boolean legendary(){return legendary;}
    public String legendaryTexture(){return legendaryTexture;} public String legendaryBaseSuffix(){return legendaryBaseSuffix;}
    public String legendaryOverlaySuffix(){return legendaryOverlaySuffix;}
    public boolean hasLegendaryOverlay(){return legendaryOverlaySuffix != null && !legendaryOverlaySuffix.isEmpty();}
    public float scale(){return scale;} public float collisionScale(){return collisionScale;}
    public boolean ordinaryCivilian(){return this == GIFT_MAN;}
    public boolean activeCombatant(){return combat != Combat.PASSIVE;}
    public static NightWatchNpcRole byId(String id){return id==null?GIFT_MAN:BY_ID.getOrDefault(id.toLowerCase(Locale.ROOT),GIFT_MAN);}
    public static NightWatchNpcRole findById(String id){return id==null?null:BY_ID.get(id.toLowerCase(Locale.ROOT));}
    public static List<NightWatchNpcRole> spawnerOrder(){return SPAWNER_ORDER;}
    private static Map<String,NightWatchNpcRole> index(){Map<String,NightWatchNpcRole> map=new LinkedHashMap<>();for(NightWatchNpcRole role:values())map.put(role.id,role);return Map.copyOf(map);}
}
