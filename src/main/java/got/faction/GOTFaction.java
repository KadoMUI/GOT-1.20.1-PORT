package got.faction;

import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

/**
 * Modern faction catalogue backed by the original 24.08.29 relation table.
 * All legacy factions are represented now so later NPC regions only need to
 * expose a matching faction id.
 */
public enum GOTFaction {
    WHITE_WALKER(9296632, true, true),
    WILDLING(7641479, true, true),
    NIGHT_WATCH(2763306, true, false),
    NORTH(13946807, true, false),
    IRONBORN(4933690, true, true),
    WESTERLANDS(8129026, true, true),
    RIVERLANDS(1339241, true, false),
    HILL_TRIBES(5717039, true, true),
    ARRYN(2962007, true, false),
    DRAGONSTONE(6974058, true, true),
    CROWNLANDS(14396197, true, true),
    STORMLANDS(83508, true, false),
    REACH(6387270, true, false),
    DORNE(16087072, true, false),
    BRAAVOS(4852748, true, false),
    VOLANTIS(4995679, true, true),
    PENTOS(1262175, true, false),
    NORVOS(2968879, true, false),
    LORATH(15066597, true, false),
    QOHOR(340550, true, false),
    MYR(4144959, true, false),
    LYS(4010019, true, false),
    TYROSH(3026478, true, false),
    GHISCAR(11237169, true, true),
    QARTH(7344144, true, false),
    LHAZAR(10373918, true, false),
    DOTHRAKI(7820575, true, true),
    IBBEN(5126694, true, false),
    JOGOS_NHAI(9984278, true, true),
    MOSSOVY(4938818, true, false),
    YI_TI(12750899, true, false),
    ASSHAI(3945791, true, true),
    SUMMER_ISLANDS(9509399, true, false),
    SOTHORYOS(6187544, true, false),
    ULTHOS(3422764, true, true),
    HOSTILE(0, false, true),
    UNALIGNED(0, false, false);

    private static final List<GOTFactionRank> STANDARD_RANKS = List.of(
            new GOTFactionRank(1000.0F, "leader"),
            new GOTFactionRank(500.0F, "hero"),
            new GOTFactionRank(100.0F, "defender"),
            new GOTFactionRank(50.0F, "friend"),
            new GOTFactionRank(10.0F, "guest")
    );
    private static final GOTFactionRank NEUTRAL_RANK = new GOTFactionRank(0.0F, "neutral");
    private static final GOTFactionRank ENEMY_RANK = new GOTFactionRank(Float.NEGATIVE_INFINITY, "enemy");
    private static final Map<GOTFaction, EnumMap<GOTFaction, GOTFactionRelation>> RELATIONS = buildRelations();
    private static final Map<String, GOTFaction> BY_ID = buildIndex();
    private static final List<GOTFaction> PLAYABLE = List.of(values()).stream()
            .filter(GOTFaction::isPlayable)
            .toList();

    private final int color;
    private final boolean playable;
    private final boolean approvesCivilianEnemyKills;

    GOTFaction(int color, boolean playable, boolean approvesCivilianEnemyKills) {
        this.color = color;
        this.playable = playable;
        this.approvesCivilianEnemyKills = approvesCivilianEnemyKills;
    }

    public String id() {
        return name().toLowerCase(Locale.ROOT);
    }

    public String legacyName() {
        return name();
    }

    public int color() {
        return color;
    }

    public boolean isPlayable() {
        return playable;
    }

    public boolean approvesCivilianEnemyKills() {
        return approvesCivilianEnemyKills;
    }

    public float pledgeAlignment() {
        return this == WHITE_WALKER ? 1000.0F : 100.0F;
    }

    public Component displayName() {
        return Component.translatable("got.faction." + legacyName() + ".name");
    }

    public Component subtitle() {
        return Component.translatable("got.faction." + legacyName() + ".subtitle");
    }

    public GOTFactionRelation relationTo(GOTFaction other) {
        if (this == UNALIGNED || other == UNALIGNED) return GOTFactionRelation.NEUTRAL;
        if (this == HOSTILE || other == HOSTILE) return GOTFactionRelation.MORTAL_ENEMY;
        if (this == other) return GOTFactionRelation.ALLY;
        return RELATIONS.get(this).getOrDefault(other, GOTFactionRelation.NEUTRAL);
    }

    public boolean isGoodRelation(GOTFaction other) {
        return relationTo(other).isGood();
    }

    public boolean isBadRelation(GOTFaction other) {
        return relationTo(other).isBad();
    }

    public List<GOTFaction> factionsOfRelation(GOTFactionRelation relation) {
        List<GOTFaction> result = new ArrayList<>();
        for (GOTFaction faction : PLAYABLE) {
            if (faction != this && relationTo(faction) == relation) result.add(faction);
        }
        return Collections.unmodifiableList(result);
    }

    public GOTFactionRank rankFor(float alignment) {
        if (this == WHITE_WALKER && alignment >= 1000.0F) {
            return new GOTFactionRank(1000.0F, "king");
        }
        for (GOTFactionRank rank : STANDARD_RANKS) {
            if (alignment >= rank.alignment()) return rank;
        }
        return alignment >= 0.0F ? NEUTRAL_RANK : ENEMY_RANK;
    }

    public List<GOTFactionRank> ranksDescending() {
        if (this == WHITE_WALKER) {
            return List.of(new GOTFactionRank(1000.0F, "king"));
        }
        return STANDARD_RANKS;
    }

    public static List<GOTFaction> playableFactions() {
        return PLAYABLE;
    }

    public static Optional<GOTFaction> byId(String id) {
        if (id == null) return Optional.empty();
        return Optional.ofNullable(BY_ID.get(id.toLowerCase(Locale.ROOT)));
    }

    public static GOTFaction fromBiomeId(String biomeId) {
        if (biomeId == null) return UNALIGNED;
        String id = biomeId.toLowerCase(Locale.ROOT);
        if (id.startsWith("north") || id.equals("skagos")) return NORTH;
        if (id.startsWith("westerlands")) return WESTERLANDS;
        if (id.startsWith("riverlands") || id.equals("river")) return RIVERLANDS;
        if (id.startsWith("arryn")) return ARRYN;
        if (id.startsWith("crownlands") || id.startsWith("kingswood")) return CROWNLANDS;
        if (id.startsWith("dragonstone")) return DRAGONSTONE;
        if (id.startsWith("reach")) return REACH;
        if (id.startsWith("stormlands")) return STORMLANDS;
        if (id.startsWith("dorne")) return DORNE;
        if (id.startsWith("iron_islands")) return IRONBORN;
        if (id.equals("gift_new") || id.equals("gift_old")) return NIGHT_WATCH;
        if (id.equals("frozen_shore") || id.equals("haunted_forest") || id.equals("thenn_land")) return WILDLING;
        if (id.startsWith("braavos")) return BRAAVOS;
        if (id.startsWith("pentos")) return PENTOS;
        if (id.startsWith("volantis")) return VOLANTIS;
        if (id.startsWith("lys")) return LYS;
        if (id.startsWith("myr")) return MYR;
        if (id.startsWith("tyrosh")) return TYROSH;
        if (id.startsWith("ghiscar")) return GHISCAR;
        if (id.startsWith("dothraki")) return DOTHRAKI;
        if (id.startsWith("yi_ti")) return YI_TI;
        if (id.startsWith("ibben")) return IBBEN;
        if (id.startsWith("jogos_nhai")) return JOGOS_NHAI;
        if (id.startsWith("shadow_")) return ASSHAI;
        if (id.startsWith("qarth")) return QARTH;
        if (id.startsWith("lorath")) return LORATH;
        if (id.startsWith("qohor")) return QOHOR;
        if (id.startsWith("lhazar")) return LHAZAR;
        if (id.startsWith("norvos")) return NORVOS;
        if (id.startsWith("mossovy")) return MOSSOVY;
        if (id.startsWith("summer_")) return SUMMER_ISLANDS;
        if (id.startsWith("sothoryos")) return SOTHORYOS;
        if (id.startsWith("ulthos")) return ULTHOS;
        return UNALIGNED;
    }

    private static Map<String, GOTFaction> buildIndex() {
        Map<String, GOTFaction> map = new java.util.HashMap<>();
        for (GOTFaction faction : values()) {
            map.put(faction.id(), faction);
            map.put(faction.legacyName().toLowerCase(Locale.ROOT), faction);
            map.put(faction.id().replace("_", ""), faction);
        }
        return Map.copyOf(map);
    }

    private static Map<GOTFaction, EnumMap<GOTFaction, GOTFactionRelation>> buildRelations() {
        Map<GOTFaction, EnumMap<GOTFaction, GOTFactionRelation>> map = new EnumMap<>(GOTFaction.class);
        for (GOTFaction faction : values()) map.put(faction, new EnumMap<>(GOTFaction.class));

        for (GOTFaction faction : values()) {
            if (faction.playable && faction != WHITE_WALKER) {
                put(map, faction, WHITE_WALKER, GOTFactionRelation.MORTAL_ENEMY);
            }
        }

        put(map, ARRYN, CROWNLANDS, GOTFactionRelation.ENEMY);
        put(map, ARRYN, HILL_TRIBES, GOTFactionRelation.ENEMY);
        put(map, ARRYN, NORTH, GOTFactionRelation.FRIEND);
        put(map, ARRYN, RIVERLANDS, GOTFactionRelation.FRIEND);
        put(map, ARRYN, WESTERLANDS, GOTFactionRelation.ENEMY);
        put(map, ASSHAI, JOGOS_NHAI, GOTFactionRelation.ENEMY);
        put(map, ASSHAI, MOSSOVY, GOTFactionRelation.ENEMY);
        put(map, ASSHAI, QARTH, GOTFactionRelation.FRIEND);
        put(map, ASSHAI, ULTHOS, GOTFactionRelation.FRIEND);
        put(map, ASSHAI, YI_TI, GOTFactionRelation.FRIEND);
        put(map, BRAAVOS, LORATH, GOTFactionRelation.FRIEND);
        put(map, BRAAVOS, LYS, GOTFactionRelation.ENEMY);
        put(map, BRAAVOS, MYR, GOTFactionRelation.ENEMY);
        put(map, BRAAVOS, NORVOS, GOTFactionRelation.FRIEND);
        put(map, BRAAVOS, PENTOS, GOTFactionRelation.ENEMY);
        put(map, BRAAVOS, QOHOR, GOTFactionRelation.FRIEND);
        put(map, BRAAVOS, TYROSH, GOTFactionRelation.ENEMY);
        put(map, BRAAVOS, VOLANTIS, GOTFactionRelation.ENEMY);
        put(map, CROWNLANDS, DORNE, GOTFactionRelation.ENEMY);
        put(map, CROWNLANDS, DRAGONSTONE, GOTFactionRelation.ENEMY);
        put(map, CROWNLANDS, HILL_TRIBES, GOTFactionRelation.FRIEND);
        put(map, CROWNLANDS, NORTH, GOTFactionRelation.ENEMY);
        put(map, CROWNLANDS, RIVERLANDS, GOTFactionRelation.ENEMY);
        put(map, CROWNLANDS, STORMLANDS, GOTFactionRelation.ENEMY);
        put(map, CROWNLANDS, WESTERLANDS, GOTFactionRelation.ALLY);
        put(map, DORNE, WESTERLANDS, GOTFactionRelation.ENEMY);
        put(map, DRAGONSTONE, REACH, GOTFactionRelation.ENEMY);
        put(map, DRAGONSTONE, STORMLANDS, GOTFactionRelation.ENEMY);
        put(map, DRAGONSTONE, WESTERLANDS, GOTFactionRelation.ENEMY);
        put(map, GHISCAR, QARTH, GOTFactionRelation.FRIEND);
        put(map, GHISCAR, SOTHORYOS, GOTFactionRelation.ENEMY);
        put(map, GHISCAR, SUMMER_ISLANDS, GOTFactionRelation.ENEMY);
        put(map, GHISCAR, VOLANTIS, GOTFactionRelation.FRIEND);
        put(map, IBBEN, DOTHRAKI, GOTFactionRelation.ENEMY);
        put(map, IBBEN, JOGOS_NHAI, GOTFactionRelation.ENEMY);
        put(map, IBBEN, LORATH, GOTFactionRelation.ENEMY);
        put(map, IBBEN, MOSSOVY, GOTFactionRelation.FRIEND);
        put(map, IBBEN, NORVOS, GOTFactionRelation.ENEMY);
        put(map, IRONBORN, NORTH, GOTFactionRelation.ENEMY);
        put(map, IRONBORN, RIVERLANDS, GOTFactionRelation.ENEMY);
        put(map, IRONBORN, WESTERLANDS, GOTFactionRelation.FRIEND);
        put(map, JOGOS_NHAI, DOTHRAKI, GOTFactionRelation.FRIEND);
        put(map, JOGOS_NHAI, MOSSOVY, GOTFactionRelation.ENEMY);
        put(map, JOGOS_NHAI, YI_TI, GOTFactionRelation.ENEMY);
        put(map, LHAZAR, DOTHRAKI, GOTFactionRelation.ENEMY);
        put(map, LHAZAR, GHISCAR, GOTFactionRelation.FRIEND);
        put(map, LORATH, NORVOS, GOTFactionRelation.FRIEND);
        put(map, LORATH, QOHOR, GOTFactionRelation.FRIEND);
        put(map, LYS, MYR, GOTFactionRelation.ENEMY);
        put(map, LYS, TYROSH, GOTFactionRelation.ENEMY);
        put(map, LYS, VOLANTIS, GOTFactionRelation.ENEMY);
        put(map, MOSSOVY, YI_TI, GOTFactionRelation.ENEMY);
        put(map, MYR, TYROSH, GOTFactionRelation.ENEMY);
        put(map, MYR, VOLANTIS, GOTFactionRelation.ENEMY);
        put(map, NIGHT_WATCH, DRAGONSTONE, GOTFactionRelation.FRIEND);
        put(map, NIGHT_WATCH, NORTH, GOTFactionRelation.ALLY);
        put(map, NIGHT_WATCH, WILDLING, GOTFactionRelation.ENEMY);
        put(map, NORTH, RIVERLANDS, GOTFactionRelation.FRIEND);
        put(map, NORTH, WESTERLANDS, GOTFactionRelation.ENEMY);
        put(map, NORTH, WILDLING, GOTFactionRelation.ENEMY);
        put(map, NORVOS, QOHOR, GOTFactionRelation.FRIEND);
        put(map, NORVOS, VOLANTIS, GOTFactionRelation.ENEMY);
        put(map, PENTOS, MYR, GOTFactionRelation.FRIEND);
        put(map, PENTOS, VOLANTIS, GOTFactionRelation.ENEMY);
        put(map, QARTH, SOTHORYOS, GOTFactionRelation.ENEMY);
        put(map, QARTH, YI_TI, GOTFactionRelation.FRIEND);
        put(map, QOHOR, DOTHRAKI, GOTFactionRelation.ENEMY);
        put(map, QOHOR, VOLANTIS, GOTFactionRelation.ENEMY);
        put(map, REACH, STORMLANDS, GOTFactionRelation.FRIEND);
        put(map, RIVERLANDS, HILL_TRIBES, GOTFactionRelation.ENEMY);
        put(map, RIVERLANDS, WESTERLANDS, GOTFactionRelation.ENEMY);
        put(map, SOTHORYOS, SUMMER_ISLANDS, GOTFactionRelation.ENEMY);
        put(map, SOTHORYOS, ULTHOS, GOTFactionRelation.ENEMY);
        put(map, STORMLANDS, WESTERLANDS, GOTFactionRelation.ENEMY);
        put(map, SUMMER_ISLANDS, DORNE, GOTFactionRelation.FRIEND);
        put(map, SUMMER_ISLANDS, LYS, GOTFactionRelation.FRIEND);
        put(map, SUMMER_ISLANDS, TYROSH, GOTFactionRelation.FRIEND);
        put(map, SUMMER_ISLANDS, VOLANTIS, GOTFactionRelation.ENEMY);
        put(map, TYROSH, VOLANTIS, GOTFactionRelation.ENEMY);
        put(map, ULTHOS, ASSHAI, GOTFactionRelation.FRIEND);
        put(map, VOLANTIS, DOTHRAKI, GOTFactionRelation.ENEMY);
        put(map, VOLANTIS, TYROSH, GOTFactionRelation.ENEMY);
        put(map, WESTERLANDS, HILL_TRIBES, GOTFactionRelation.FRIEND);
        put(map, WILDLING, DRAGONSTONE, GOTFactionRelation.ENEMY);
        put(map, WILDLING, IBBEN, GOTFactionRelation.FRIEND);
        return Map.copyOf(map);
    }

    private static void put(Map<GOTFaction, EnumMap<GOTFaction, GOTFactionRelation>> map,
                            GOTFaction left, GOTFaction right, GOTFactionRelation relation) {
        map.get(left).put(right, relation);
        map.get(right).put(left, relation);
    }
}
