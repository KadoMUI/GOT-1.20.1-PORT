package got.npc;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/** Complete Dothraki settlement roster and its two Vaes Efe fixed characters. */
public enum DothrakiNpcRole {
    DOTHRAKI("dothraki", "Dothraki", Gender.RANDOM, Combat.MELEE, Trade.NONE, 1, false, null, "", "", 1.0F, 1.0F),
    DOTHRAKI_ARCHER("dothraki_archer", "Dothraki Archer", Gender.RANDOM, Combat.HYBRID, Trade.NONE, 1, false, null, "", "", 1.0F, 1.0F),
    DOTHRAKI_CHIEFTAIN("dothraki_chieftain", "Dothraki Chieftain", Gender.MALE, Combat.MELEE, Trade.UNITS, 5, false, null, "", "", 1.0F, 1.0F),
    DOTHRAKI_SHAMAN("dothraki_shaman", "Dothraki Shaman", Gender.FEMALE, Combat.PASSIVE, Trade.BUTCHER, 2, false, null, "", "", 1.0F, 1.0F),
    DAENERYS_TARGARYEN("daenerys_targaryen", "Daenerys Targaryen", Gender.FEMALE, Combat.PASSIVE, Trade.NONE, 500, true, "daenerys_targaryen", "", "", 1.0F, 1.0F),
    JORAH_MORMONT("jorah_mormont", "Jorah Mormont", Gender.MALE, Combat.MELEE, Trade.NONE, 300, true, "jorah_mormont", "", "", 1.0F, 1.0F);

    public enum Gender { RANDOM, MALE, FEMALE }
    public enum Combat { PASSIVE, MELEE, ARCHER, HYBRID }
    public enum Trade { NONE, BAKER, BARTENDER, BLACKSMITH, BREWER, BUTCHER, FARMER, FISHMONGER, FLORIST, GOLDSMITH, LUMBERMAN, MASON, MINER, UNITS, MAESTER, SLAVER }
    private static final Map<String, DothrakiNpcRole> BY_ID = index();
    private static final List<DothrakiNpcRole> SPAWNER_ORDER = List.of(values());
    private final String id, displayName, legendaryTexture, legendaryBaseSuffix, legendaryOverlaySuffix;
    private final Gender gender; private final Combat combat; private final Trade trade;
    private final int alignmentBonus; private final boolean legendary; private final float scale, collisionScale;
    DothrakiNpcRole(String id, String displayName, Gender gender, Combat combat, Trade trade, int alignmentBonus,
                    boolean legendary, String legendaryTexture, String legendaryBaseSuffix,
                    String legendaryOverlaySuffix, float scale, float collisionScale) {
        this.id=id; this.displayName=displayName; this.gender=gender; this.combat=combat; this.trade=trade;
        this.alignmentBonus=alignmentBonus; this.legendary=legendary; this.legendaryTexture=legendaryTexture;
        this.legendaryBaseSuffix=legendaryBaseSuffix; this.legendaryOverlaySuffix=legendaryOverlaySuffix;
        this.scale=scale; this.collisionScale=collisionScale;
    }
    public String id(){return id;} public String displayName(){return displayName;} public Gender gender(){return gender;}
    public Combat combat(){return combat;} public Trade trade(){return trade;} public int alignmentBonus(){return alignmentBonus;}
    public boolean legendary(){return legendary;} public String legendaryTexture(){return legendaryTexture;}
    public String legendaryBaseSuffix(){return legendaryBaseSuffix;} public String legendaryOverlaySuffix(){return legendaryOverlaySuffix;}
    public boolean hasLegendaryOverlay(){return !legendaryOverlaySuffix.isEmpty();} public float scale(){return scale;}
    public float collisionScale(){return collisionScale;} public boolean ordinaryCivilian(){return false;}
    public boolean activeCombatant(){return combat!=Combat.PASSIVE;}
    public static DothrakiNpcRole byId(String id){return id==null?DOTHRAKI:BY_ID.getOrDefault(id.toLowerCase(Locale.ROOT),DOTHRAKI);}
    public static DothrakiNpcRole findById(String id){return id==null?null:BY_ID.get(id.toLowerCase(Locale.ROOT));}
    public static List<DothrakiNpcRole> spawnerOrder(){return SPAWNER_ORDER;}
    private static Map<String,DothrakiNpcRole> index(){Map<String,DothrakiNpcRole> roles=new LinkedHashMap<>(); for(DothrakiNpcRole role:values())roles.put(role.id,role);return Map.copyOf(roles);}
}
