package got.npc;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Complete North-only NPC catalogue recovered from the GPL 1.7.10 entity
 * hierarchy. Night's Watch, Wildling and Thenn roles deliberately live outside
 * this regional milestone.
 */
public enum NorthNpcRole {
    NORTH_MAN("north_man", "Northern Civilian", Gender.RANDOM, Combat.PASSIVE, Trade.NONE, 1, false, null, 1, 1.0F),
    NORTH_LEVYMAN("north_levyman", "Northern Levyman", Gender.MALE, Combat.MELEE, Trade.NONE, 2, false, null, 1, 1.0F),
    NORTH_LEVYMAN_ARCHER("north_levyman_archer", "Northern Levyman Archer", Gender.MALE, Combat.ARCHER, Trade.NONE, 2, false, null, 1, 1.0F),
    NORTH_SOLDIER("north_soldier", "Northern Soldier", Gender.MALE, Combat.MELEE, Trade.NONE, 2, false, null, 1, 1.0F),
    NORTH_SOLDIER_ARCHER("north_soldier_archer", "Northern Soldier Archer", Gender.MALE, Combat.ARCHER, Trade.NONE, 2, false, null, 1, 1.0F),
    NORTH_GUARD("north_guard", "Northern Guard", Gender.MALE, Combat.MELEE, Trade.NONE, 2, false, null, 1, 1.0F),
    NORTH_BANNER_BEARER("north_banner_bearer", "Northern Banner Bearer", Gender.MALE, Combat.MELEE, Trade.NONE, 2, false, null, 1, 1.0F),
    NORTH_CAPTAIN("north_captain", "Northern Captain", Gender.MALE, Combat.MELEE, Trade.UNITS, 5, false, null, 1, 1.0F),
    NORTH_BLACKSMITH("north_blacksmith", "Northern Blacksmith", Gender.MALE, Combat.PASSIVE, Trade.BLACKSMITH, 2, false, null, 1, 1.0F),
    NORTH_GOLDSMITH("north_goldsmith", "Northern Goldsmith", Gender.RANDOM, Combat.PASSIVE, Trade.GOLDSMITH, 2, false, null, 1, 1.0F),
    NORTH_FARMER("north_farmer", "Northern Farmer", Gender.MALE, Combat.PASSIVE, Trade.FARMER, 2, false, null, 1, 1.0F),
    NORTH_FARMHAND("north_farmhand", "Northern Farmhand", Gender.RANDOM, Combat.PASSIVE, Trade.NONE, 1, false, null, 1, 1.0F),
    NORTH_BARTENDER("north_bartender", "Northern Bartender", Gender.MALE, Combat.PASSIVE, Trade.BARTENDER, 2, false, null, 1, 1.0F),
    NORTH_MINER("north_miner", "Northern Miner", Gender.RANDOM, Combat.PASSIVE, Trade.MINER, 2, false, null, 1, 1.0F),
    NORTH_LUMBERMAN("north_lumberman", "Northern Lumberman", Gender.RANDOM, Combat.PASSIVE, Trade.LUMBERMAN, 2, false, null, 1, 1.0F),
    NORTH_MASON("north_mason", "Northern Mason", Gender.RANDOM, Combat.PASSIVE, Trade.MASON, 2, false, null, 1, 1.0F),
    NORTH_BREWER("north_brewer", "Northern Brewer", Gender.RANDOM, Combat.PASSIVE, Trade.BREWER, 2, false, null, 1, 1.0F),
    NORTH_FLORIST("north_florist", "Northern Florist", Gender.RANDOM, Combat.PASSIVE, Trade.FLORIST, 2, false, null, 1, 1.0F),
    NORTH_BUTCHER("north_butcher", "Northern Butcher", Gender.RANDOM, Combat.PASSIVE, Trade.BUTCHER, 2, false, null, 1, 1.0F),
    NORTH_FISHMONGER("north_fishmonger", "Northern Fishmonger", Gender.RANDOM, Combat.PASSIVE, Trade.FISHMONGER, 2, false, null, 1, 1.0F),
    NORTH_BAKER("north_baker", "Northern Baker", Gender.RANDOM, Combat.PASSIVE, Trade.BAKER, 2, false, null, 1, 1.0F),

    NORTH_HILLMAN("north_hillman", "Northern Hillman", Gender.RANDOM, Combat.PASSIVE, Trade.NONE, 1, false, null, 1, 1.0F),
    NORTH_HILLMAN_WARRIOR("north_hillman_warrior", "Northern Hillman Warrior", Gender.MALE, Combat.MELEE, Trade.NONE, 2, false, null, 1, 1.0F),
    NORTH_HILLMAN_ARCHER("north_hillman_archer", "Northern Hillman Archer", Gender.MALE, Combat.ARCHER, Trade.NONE, 2, false, null, 1, 1.0F),
    NORTH_HILLMAN_AXE_THROWER("north_hillman_axe_thrower", "Northern Hillman Axe Thrower", Gender.MALE, Combat.AXE_THROWER, Trade.NONE, 2, false, null, 1, 1.0F),
    NORTH_HILLMAN_BANNER_BEARER("north_hillman_banner_bearer", "Northern Hillman Banner Bearer", Gender.MALE, Combat.MELEE, Trade.NONE, 2, false, null, 1, 1.0F),
    NORTH_HILLMAN_CHIEFTAIN("north_hillman_chieftain", "Northern Hillman Chieftain", Gender.MALE, Combat.MELEE, Trade.HILLMEN_UNITS, 5, false, null, 1, 1.0F),

    BARBREY_DUSTIN("barbrey_dustin", "Barbrey Dustin", Gender.FEMALE, Combat.MELEE, Trade.UNITS, 100, true, "barbrey_dustin", 1, 1.0F),
    RAMSAY_BOLTON("ramsay_bolton", "Ramsay Bolton", Gender.MALE, Combat.MELEE, Trade.NONE, 100, true, "ramsay_bolton", 1, 1.0F),
    ROOSE_BOLTON("roose_bolton", "Roose Bolton", Gender.MALE, Combat.MELEE, Trade.NONE, 100, true, "roose_bolton", 2, 1.0F),
    HOWLAND_REED("howland_reed", "Howland Reed", Gender.MALE, Combat.MELEE, Trade.NONE, 100, true, "howland_reed", 2, 1.0F),
    RICKARD_KARSTARK("rickard_karstark", "Rickard Karstark", Gender.MALE, Combat.MELEE, Trade.UNITS, 100, true, "rickard_karstark", 2, 1.0F),
    JOHN_UMBER("john_umber", "John Umber", Gender.MALE, Combat.MELEE, Trade.UNITS, 100, true, "john_umber", 2, 1.2F),
    MAEGE_MORMONT("maege_mormont", "Maege Mormont", Gender.FEMALE, Combat.MELEE, Trade.UNITS, 100, true, "maege_mormont", 2, 1.0F),
    RODRIK_RYSWELL("rodrik_ryswell", "Rodrik Ryswell", Gender.MALE, Combat.MELEE, Trade.UNITS, 100, true, "rodrik_ryswell", 1, 1.0F),
    CLEY_CERWYN("cley_cerwyn", "Cley Cerwyn", Gender.MALE, Combat.MELEE, Trade.UNITS, 100, true, "cley_cerwyn", 1, 1.0F),
    HELMAN_TALLHART("helman_tallhart", "Helman Tallhart", Gender.MALE, Combat.MELEE, Trade.UNITS, 100, true, "helman_tallhart", 1, 1.0F),
    WYMAN_MANDERLY("wyman_manderly", "Wyman Manderly", Gender.MALE, Combat.MELEE, Trade.UNITS, 100, true, "wyman_manderly", 2, 1.0F),
    ROBB_STARK("robb_stark", "Robb Stark", Gender.MALE, Combat.MELEE, Trade.NONE, 500, true, "robb_stark", 2, 1.0F),
    HODOR("hodor", "Hodor", Gender.MALE, Combat.PASSIVE, Trade.NONE, 100, true, "hodor", 1, 1.30F),
    ARYA_STARK("arya_stark", "Arya Stark", Gender.FEMALE, Combat.MELEE, Trade.NONE, 300, true, "arya_stark", 1, 0.75F),
    BRAN_STARK("bran_stark", "Bran Stark", Gender.MALE, Combat.ARCHER, Trade.NONE, 500, true, "bran_stark", 1, 0.75F),
    RICKON_STARK("rickon_stark", "Rickon Stark", Gender.MALE, Combat.PASSIVE, Trade.NONE, 500, true, "rickon_stark", 1, 0.65F),
    MAESTER_LUWIN("maester_luwin", "Maester Luwin", Gender.MALE, Combat.PASSIVE, Trade.MAESTER, 15, true, "luwin", 1, 1.0F),
    OSHA("osha", "Osha", Gender.FEMALE, Combat.PASSIVE, Trade.NONE, 5, true, "osha", 2, 1.0F),
    CATELYN_STARK("catelyn_stark", "Catelyn Stark", Gender.FEMALE, Combat.PASSIVE, Trade.NONE, 500, true, "catelyn_stark", 1, 1.0F),
    RODRIK_CASSEL("rodrik_cassel", "Rodrik Cassel", Gender.MALE, Combat.MELEE, Trade.UNITS, 100, true, "rodrik_cassel", 1, 1.0F);

    public enum Gender { RANDOM, MALE, FEMALE }
    public enum Combat { PASSIVE, MELEE, ARCHER, AXE_THROWER }
    public enum Trade {
        NONE, BAKER, BARTENDER, BLACKSMITH, BREWER, BUTCHER, FARMER,
        FISHMONGER, FLORIST, GOLDSMITH, LUMBERMAN, MASON, MINER,
        UNITS, HILLMEN_UNITS, MAESTER
    }

    private static final Map<String, NorthNpcRole> BY_ID = index();
    private static final List<NorthNpcRole> SPAWNER_ORDER = List.of(values());

    private final String id;
    private final String displayName;
    private final Gender gender;
    private final Combat combat;
    private final Trade trade;
    private final int alignmentBonus;
    private final boolean legendary;
    private final String legendaryTexture;
    private final int legendaryTextureLayers;
    private final float scale;

    NorthNpcRole(String id, String displayName, Gender gender, Combat combat, Trade trade,
                 int alignmentBonus, boolean legendary, String legendaryTexture,
                 int legendaryTextureLayers, float scale) {
        this.id = id;
        this.displayName = displayName;
        this.gender = gender;
        this.combat = combat;
        this.trade = trade;
        this.alignmentBonus = alignmentBonus;
        this.legendary = legendary;
        this.legendaryTexture = legendaryTexture;
        this.legendaryTextureLayers = legendaryTextureLayers;
        this.scale = scale;
    }

    public String id() { return id; }
    public String displayName() { return displayName; }
    public Gender gender() { return gender; }
    public Combat combat() { return combat; }
    public Trade trade() { return trade; }
    public int alignmentBonus() { return alignmentBonus; }
    public boolean legendary() { return legendary; }
    public String legendaryTexture() { return legendaryTexture; }
    public boolean legendaryLayered() { return legendary && legendaryTextureLayers > 1; }
    public float scale() { return scale; }
    public boolean hillman() { return name().startsWith("NORTH_HILLMAN"); }
    public boolean ordinaryCivilian() { return this == NORTH_MAN || this == NORTH_HILLMAN; }
    public boolean activeCombatant() { return combat != Combat.PASSIVE; }

    public static NorthNpcRole byId(String id) {
        if (id == null) return NORTH_MAN;
        return BY_ID.getOrDefault(id.toLowerCase(Locale.ROOT), NORTH_MAN);
    }

    public static NorthNpcRole findById(String id) {
        return id == null ? null : BY_ID.get(id.toLowerCase(Locale.ROOT));
    }

    public static List<NorthNpcRole> spawnerOrder() { return SPAWNER_ORDER; }

    private static Map<String, NorthNpcRole> index() {
        Map<String, NorthNpcRole> roles = new LinkedHashMap<>();
        for (NorthNpcRole role : values()) roles.put(role.id, role);
        return Map.copyOf(roles);
    }
}
