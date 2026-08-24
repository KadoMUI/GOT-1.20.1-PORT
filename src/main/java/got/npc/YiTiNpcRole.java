package got.npc;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/** Complete Yi-Tish civic, military, specialist, and fixed-character catalogue. */
public enum YiTiNpcRole {
    YI_TI_MAN("yi_ti_man", "Yi-Tish Man", Gender.RANDOM, Combat.PASSIVE, Trade.NONE, 1, false, null, "", "", 1.0F, 1.0F),
    YI_TI_LEVYMAN("yi_ti_levyman", "Yi-Tish Levyman", Gender.MALE, Combat.MELEE, Trade.NONE, 2, false, null, "", "", 1.0F, 1.0F),
    YI_TI_LEVYMAN_CROSSBOWER("yi_ti_levyman_crossbower", "Yi-Tish Levyman Crossbower", Gender.MALE, Combat.ARCHER, Trade.NONE, 2, false, null, "", "", 1.0F, 1.0F),
    YI_TI_SOLDIER("yi_ti_soldier", "Yi-Tish Soldier", Gender.MALE, Combat.MELEE, Trade.NONE, 2, false, null, "", "", 1.0F, 1.0F),
    YI_TI_SOLDIER_CROSSBOWER("yi_ti_soldier_crossbower", "Yi-Tish Soldier Crossbower", Gender.MALE, Combat.ARCHER, Trade.NONE, 2, false, null, "", "", 1.0F, 1.0F),
    YI_TI_BANNER_BEARER("yi_ti_banner_bearer", "Yi-Tish Banner Bearer", Gender.MALE, Combat.MELEE, Trade.NONE, 2, false, null, "", "", 1.0F, 1.0F),
    YI_TI_CAPTAIN("yi_ti_captain", "Yi-Tish Captain", Gender.MALE, Combat.MELEE, Trade.UNITS, 5, false, null, "", "", 1.0F, 1.0F),
    YI_TI_BLACKSMITH("yi_ti_blacksmith", "Yi-Tish Blacksmith", Gender.MALE, Combat.PASSIVE, Trade.BLACKSMITH, 2, false, null, "", "", 1.0F, 1.0F),
    YI_TI_GOLDSMITH("yi_ti_goldsmith", "Yi-Tish Goldsmith", Gender.RANDOM, Combat.PASSIVE, Trade.GOLDSMITH, 2, false, null, "", "", 1.0F, 1.0F),
    YI_TI_FARMER("yi_ti_farmer", "Yi-Tish Farmer", Gender.MALE, Combat.PASSIVE, Trade.FARMER, 2, false, null, "", "", 1.0F, 1.0F),
    YI_TI_FARMHAND("yi_ti_farmhand", "Yi-Tish Farmhand", Gender.RANDOM, Combat.PASSIVE, Trade.NONE, 1, false, null, "", "", 1.0F, 1.0F),
    YI_TI_BARTENDER("yi_ti_bartender", "Yi-Tish Bartender", Gender.MALE, Combat.PASSIVE, Trade.BARTENDER, 2, false, null, "", "", 1.0F, 1.0F),
    YI_TI_MINER("yi_ti_miner", "Yi-Tish Miner", Gender.RANDOM, Combat.PASSIVE, Trade.MINER, 2, false, null, "", "", 1.0F, 1.0F),
    YI_TI_LUMBERMAN("yi_ti_lumberman", "Yi-Tish Lumberman", Gender.RANDOM, Combat.PASSIVE, Trade.LUMBERMAN, 2, false, null, "", "", 1.0F, 1.0F),
    YI_TI_MASON("yi_ti_mason", "Yi-Tish Mason", Gender.RANDOM, Combat.PASSIVE, Trade.MASON, 2, false, null, "", "", 1.0F, 1.0F),
    YI_TI_BREWER("yi_ti_brewer", "Yi-Tish Brewer", Gender.RANDOM, Combat.PASSIVE, Trade.BREWER, 2, false, null, "", "", 1.0F, 1.0F),
    YI_TI_FLORIST("yi_ti_florist", "Yi-Tish Florist", Gender.RANDOM, Combat.PASSIVE, Trade.FLORIST, 2, false, null, "", "", 1.0F, 1.0F),
    YI_TI_BUTCHER("yi_ti_butcher", "Yi-Tish Butcher", Gender.RANDOM, Combat.PASSIVE, Trade.BUTCHER, 2, false, null, "", "", 1.0F, 1.0F),
    YI_TI_FISHMONGER("yi_ti_fishmonger", "Yi-Tish Fishmonger", Gender.RANDOM, Combat.PASSIVE, Trade.FISHMONGER, 2, false, null, "", "", 1.0F, 1.0F),
    YI_TI_BAKER("yi_ti_baker", "Yi-Tish Baker", Gender.RANDOM, Combat.PASSIVE, Trade.BAKER, 2, false, null, "", "", 1.0F, 1.0F),
    YI_TI_SAMURAI("yi_ti_samurai", "Yi-Tish Samurai", Gender.MALE, Combat.MELEE, Trade.NONE, 3, false, null, "", "", 1.0F, 1.0F),
    YI_TI_SAMURAI_FLAMETHROWER("yi_ti_samurai_flamethrower", "Yi-Tish Samurai Flamethrower", Gender.MALE, Combat.ARCHER, Trade.NONE, 3, false, null, "", "", 1.0F, 1.0F),
    YI_TI_BOMBARDIER("yi_ti_bombardier", "Yi-Tish Bombardier", Gender.MALE, Combat.ARCHER, Trade.NONE, 3, false, null, "", "", 1.0F, 1.0F),
    BU_GAI("bu_gai", "Bu Gai", Gender.MALE, Combat.MELEE, Trade.NONE, 500, true, "bu_gai", "_1", "_2", 1.0F, 1.0F);

    public enum Gender { RANDOM, MALE, FEMALE }
    public enum Combat { PASSIVE, MELEE, ARCHER, HYBRID }
    public enum Trade { NONE, BAKER, BARTENDER, BLACKSMITH, BREWER, BUTCHER, FARMER, FISHMONGER, FLORIST, GOLDSMITH, LUMBERMAN, MASON, MINER, UNITS, MAESTER, SLAVER }
    private static final Map<String,YiTiNpcRole> BY_ID=index(); private static final List<YiTiNpcRole> SPAWNER_ORDER=List.of(values());
    private final String id,displayName,legendaryTexture,legendaryBaseSuffix,legendaryOverlaySuffix; private final Gender gender;
    private final Combat combat; private final Trade trade; private final int alignmentBonus; private final boolean legendary; private final float scale,collisionScale;
    YiTiNpcRole(String id,String displayName,Gender gender,Combat combat,Trade trade,int alignmentBonus,boolean legendary,String legendaryTexture,String legendaryBaseSuffix,String legendaryOverlaySuffix,float scale,float collisionScale){this.id=id;this.displayName=displayName;this.gender=gender;this.combat=combat;this.trade=trade;this.alignmentBonus=alignmentBonus;this.legendary=legendary;this.legendaryTexture=legendaryTexture;this.legendaryBaseSuffix=legendaryBaseSuffix;this.legendaryOverlaySuffix=legendaryOverlaySuffix;this.scale=scale;this.collisionScale=collisionScale;}
    public String id(){return id;} public String displayName(){return displayName;} public Gender gender(){return gender;} public Combat combat(){return combat;} public Trade trade(){return trade;} public int alignmentBonus(){return alignmentBonus;} public boolean legendary(){return legendary;} public String legendaryTexture(){return legendaryTexture;} public String legendaryBaseSuffix(){return legendaryBaseSuffix;} public String legendaryOverlaySuffix(){return legendaryOverlaySuffix;} public boolean hasLegendaryOverlay(){return !legendaryOverlaySuffix.isEmpty();} public float scale(){return scale;} public float collisionScale(){return collisionScale;} public boolean ordinaryCivilian(){return this==YI_TI_MAN||this==YI_TI_FARMHAND;} public boolean activeCombatant(){return combat!=Combat.PASSIVE;}
    public static YiTiNpcRole byId(String id){return id==null?YI_TI_MAN:BY_ID.getOrDefault(id.toLowerCase(Locale.ROOT),YI_TI_MAN);} public static YiTiNpcRole findById(String id){return id==null?null:BY_ID.get(id.toLowerCase(Locale.ROOT));} public static List<YiTiNpcRole> spawnerOrder(){return SPAWNER_ORDER;} private static Map<String,YiTiNpcRole> index(){Map<String,YiTiNpcRole> roles=new LinkedHashMap<>();for(YiTiNpcRole role:values())roles.put(role.id,role);return Map.copyOf(roles);}
}
