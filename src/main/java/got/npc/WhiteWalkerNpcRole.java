package got.npc;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/** Hostile creatures of the White Walker faction. */
public enum WhiteWalkerNpcRole {
    WIGHT("wight", "Wight", Gender.RANDOM, Combat.MELEE, 1, false, null, "", "", 1.0F, 1.0F),
    WHITE_WALKER("white_walker", "White Walker", Gender.MALE, Combat.MELEE, 5, false, null, "", "", 1.1F, 1.0F),
    WIGHT_GIANT("wight_giant", "Wight Giant", Gender.MALE, Combat.MELEE, 3, false, null, "", "", 3.5F, 3.0F),
    NIGHT_KING("night_king", "Night King", Gender.MALE, Combat.MELEE, 500, true, "night_king", "", "", 1.1F, 1.0F);

    public enum Gender { RANDOM, MALE, FEMALE }
    public enum Combat { PASSIVE, MELEE }
    public enum Trade { NONE }
    private static final Map<String,WhiteWalkerNpcRole> BY_ID=index();
    private static final List<WhiteWalkerNpcRole> SPAWNER_ORDER=List.of(values());
    private final String id,displayName,legendaryTexture,baseSuffix,overlaySuffix;
    private final Gender gender; private final Combat combat; private final int alignmentBonus;
    private final boolean legendary; private final float scale,collisionScale;
    WhiteWalkerNpcRole(String id,String displayName,Gender gender,Combat combat,int alignmentBonus,
                       boolean legendary,String legendaryTexture,String baseSuffix,String overlaySuffix,
                       float scale,float collisionScale){this.id=id;this.displayName=displayName;this.gender=gender;
        this.combat=combat;this.alignmentBonus=alignmentBonus;this.legendary=legendary;this.legendaryTexture=legendaryTexture;
        this.baseSuffix=baseSuffix;this.overlaySuffix=overlaySuffix;this.scale=scale;this.collisionScale=collisionScale;}
    public String id(){return id;} public String displayName(){return displayName;} public Gender gender(){return gender;}
    public Combat combat(){return combat;} public Trade trade(){return Trade.NONE;} public int alignmentBonus(){return alignmentBonus;}
    public boolean legendary(){return legendary;} public String legendaryTexture(){return legendaryTexture;}
    public String legendaryBaseSuffix(){return baseSuffix;} public String legendaryOverlaySuffix(){return overlaySuffix;}
    public boolean hasLegendaryOverlay(){return false;} public float scale(){return scale;} public float collisionScale(){return collisionScale;}
    public boolean ordinaryCivilian(){return this==WIGHT;} public boolean activeCombatant(){return true;}
    public boolean giant(){return this==WIGHT_GIANT;}
    public static WhiteWalkerNpcRole byId(String id){return id==null?WIGHT:BY_ID.getOrDefault(id.toLowerCase(Locale.ROOT),WIGHT);}
    public static WhiteWalkerNpcRole findById(String id){return id==null?null:BY_ID.get(id.toLowerCase(Locale.ROOT));}
    public static List<WhiteWalkerNpcRole> spawnerOrder(){return SPAWNER_ORDER;}
    private static Map<String,WhiteWalkerNpcRole> index(){Map<String,WhiteWalkerNpcRole> map=new LinkedHashMap<>();for(WhiteWalkerNpcRole role:values())map.put(role.id,role);return Map.copyOf(map);}
}
