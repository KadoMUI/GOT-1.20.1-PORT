package got.world.flora;

import got.world.biome.GOTBiomeMetadata;
import got.world.biome.GOTBiomePreset;

import java.util.List;

import static got.world.flora.GOTBiomeFloraProfile.PlantTheme;
import static got.world.flora.GOTBiomeFloraProfile.WeightedTree;
import static got.world.flora.GOTTreeSpecies.*;

/**
 * Data-driven counterpart to the legacy biome constructors and
 * GOTBiomePreseter. It restores the original regional palettes while keeping
 * every one of the modern biome keys covered, including generated subtypes.
 */
public final class GOTBiomeFloraCatalog {
    private GOTBiomeFloraCatalog() {}

    public static GOTBiomeFloraProfile forBiome(GOTBiomeMetadata metadata) {
        if (metadata == null) return plains(false);
        String id = metadata.id();
        GOTBiomeFloraProfile profile = byPreset(metadata.preset(), id);

        if (id.equals("north_forest_irontree")) return override(profile, 10, PlantTheme.COLD,
                weighted(REDWOOD, 100));
        if (id.equals("isle_of_faces")) return new GOTBiomeFloraProfile(1, 12, 20, 1, 0, 0, 1, 0, 0, 0, 0,
                PlantTheme.NORMAL, weighted(WEIRWOOD, 100));
        if (id.equals("reach_fire_field")) return new GOTBiomeFloraProfile(1, 20, 20, 0, 0, 0, 0, 0, 0, 0, 0,
                PlantTheme.NORMAL, orchard());
        if (id.equals("qohor_forest")) return override(profile, 10, PlantTheme.COLD,
                weighted(CATALPA, 500, PINE, 400, VANILLA_SPRUCE, 300, FIR, 300, VANILLA_DARK_OAK, 100));
        if (id.equals("ifekevron_forest")) return override(profile, 10, PlantTheme.COLD,
                weighted(FOTINIA, 800, PINE, 250, FIR, 200));
        if (id.equals("sothoryos_mangrove")) return override(profile, 12, PlantTheme.MARSH,
                weighted(MANGROVE, 100));
        if (id.equals("volantis_orange_forest")) return override(profile, 10, PlantTheme.TROPICAL,
                weighted(ORANGE, 700, CEDAR, 150, CYPRESS, 100, OLIVE, 50));
        if (id.equals("reach_arbor")) return override(profile, 1.5F, PlantTheme.NORMAL, orchard());
        if (id.equals("shadow_land")) return new GOTBiomeFloraProfile(6, 5, 48, 0, 4, 0, 0, 2, 2, 0, 0,
                PlantTheme.ASSHAI, weighted(CHARRED, 1000, DEAD_OAK, 100));
        if (id.equals("shadow_town") || id.equals("shadow_mountains")) return override(profile,
                id.equals("shadow_mountains") ? 3 : 0, PlantTheme.ASSHAI, weighted(CHARRED, 100));
        if (id.equals("valyria")) return new GOTBiomeFloraProfile(6, 6, 2, 0, 3, 0, 0, 2, 2, 0, 0,
                PlantTheme.VALYRIA, weighted(OAK, 500, DEAD_OAK, 250, CHARRED, 100));
        if (id.equals("valyria_volcano")) return new GOTBiomeFloraProfile(0, 0, 0, 0, 4, 0, 0, 0, 4, 0, 0,
                PlantTheme.VALYRIA, List.of());
        if (id.equals("yeen")) return new GOTBiomeFloraProfile(2, 4, 1, 2, 2, 0, 0, 2, 3, 0, 0,
                PlantTheme.MARSH, weighted(ULTHOS_RED, 500, CHARRED, 400, DEAD_OAK, 100));

        if (id.startsWith("ulthos_red_forest")) return override(profile, id.endsWith("edge") ? 2 : 10,
                PlantTheme.TROPICAL, weighted(ULTHOS_RED, 1000, GREEN_OAK, 250, ULTHOS, 100));
        if (id.startsWith("ulthos_forest")) return override(profile, id.endsWith("edge") ? 2 : 10,
                PlantTheme.TROPICAL, weighted(ULTHOS, 1000, GREEN_OAK, 300, PINE, 100, FIR, 100));
        if (id.startsWith("ulthos_")) return replacePalette(profile,
                weighted(ULTHOS, 500, GREEN_OAK, 250, PINE, 200, FIR, 100));
        if (id.startsWith("yi_ti")) return replaceTheme(replacePalette(profile,
                weighted(MAPLE, 450, PLUM, 250, CATALPA, 200, CYPRESS, 100)), PlantTheme.YI_TI);
        if (id.startsWith("summer_islands") || id.equals("summer_colony") || id.equals("naath"))
            return replacePalette(profile, weighted(PALM, 400, BANANA, 300, MAHOGANY, 200, MANGO_PROXY, 100));
        if (id.startsWith("ibben")) return replacePalette(profile,
                weighted(IBBINIA, 500, VANILLA_SPRUCE, 400, FIR, 350, LARCH, 300, ASPEN, 100));
        if (id.startsWith("mossovy") || id.startsWith("north") || id.equals("wolfswood")
                || id.equals("haunted_forest") || id.equals("gift_new") || id.equals("gift_old")
                || id.equals("thenn_land") || id.equals("skagos"))
            return replacePalette(profile, northern());
        if (id.startsWith("dorne") || id.startsWith("ghiscar") || id.startsWith("qarth")
                || id.startsWith("lhazar") || id.equals("cannibal_sands") || id.equals("shrykes_land"))
            return replacePalette(profile, weighted(DATE_PALM, 400, PALM, 200, CYPRESS, 150, OLIVE, 100, DEAD_OAK, 50));
        if (id.startsWith("sothoryos") || id.equals("yeen")) return profile;
        if (id.contains("forest") && metadata.preset() == GOTBiomePreset.FOREST) {
            if (isSouthern(id)) return replacePalette(profile, southern());
            return replacePalette(profile, miderate());
        }
        return profile;
    }

    // The old mod's mango shares a tropical orchard shape; the modern shell
    // has not yet split mango wood from mahogany, so the latter is the closest
    // faithful registered material for natural generation.
    private static final GOTTreeSpecies MANGO_PROXY = MAHOGANY;

    private static GOTBiomeFloraProfile byPreset(GOTBiomePreset preset, String id) {
        return switch (preset) {
            case DESERT, DESERT_COLD -> new GOTBiomeFloraProfile(0.08F, 5, 1, 0, 3, 2, 0, 0, 1, 0,
                    preset == GOTBiomePreset.DESERT_COLD ? 16 : 0, PlantTheme.ARID,
                    weighted(DEAD_OAK, 800, DATE_PALM, 150, OAK, 50));
            case FROST, POLAR -> new GOTBiomeFloraProfile(0.12F, 0, 0, 0, 1, 0, 0, 0, 1, 0, 96,
                    PlantTheme.COLD, weighted(id.startsWith("sothoryos") ? FOTINIA : PINE, 100));
            case TAIGA -> new GOTBiomeFloraProfile(10, 3, 1, 1, 0, 0, 2, 1, 2, 0, 24,
                    PlantTheme.COLD, northern());
            case SAVANNAH -> new GOTBiomeFloraProfile(1.5F, 96, 1, 0, 2, 0, 0, 1, 2, 0, 0,
                    PlantTheme.ARID, savannah());
            case BUSHLAND -> new GOTBiomeFloraProfile(2, 24, 2, 0, 2, 0, 1, 1, 2, 0, 0,
                    PlantTheme.ARID, savannah());
            case JUNGLE -> new GOTBiomeFloraProfile(id.endsWith("edge") ? 2 : 32, 20, 8, 3, 0, 0, 3, 1, 1, 0, 0,
                    PlantTheme.TROPICAL, tropical());
            case MOUNTAINS -> new GOTBiomeFloraProfile(3, 6, 3, 0, 1, 0, 1, 1, 4, 0, 24,
                    PlantTheme.COLD, northern());
            case MARSHES -> new GOTBiomeFloraProfile(id.contains("forest") ? 8 : 3, 12, 2, 5, 0, 0, 2, 2, 1, 0, 0,
                    PlantTheme.MARSH, weighted(WILLOW, 700, MANGROVE, 150, OAK, 150));
            case FOREST -> new GOTBiomeFloraProfile(10, 12, 5, 1, 0, 0, 3, 2, 1, 0, 0,
                    PlantTheme.NORMAL, miderate());
            case NORTHERN_PLAINS -> new GOTBiomeFloraProfile(0.35F, 12, 3, 1, 0, 0, 1, 0, 1, 0, 12,
                    PlantTheme.COLD, northern());
            case SOUTHERN_PLAINS -> new GOTBiomeFloraProfile(0.45F, 14, 5, 1, 1, 0, 1, 0, 1, 0, 0,
                    PlantTheme.NORMAL, southern());
            case AQUATIC -> new GOTBiomeFloraProfile(0, 0, 0, 2, 0, 0, 0, 0, 0, 14, 0,
                    PlantTheme.NORMAL, List.of());
            case MIDERATE_PLAINS -> plains(isSouthern(id));
        };
    }

    private static GOTBiomeFloraProfile plains(boolean southern) {
        return new GOTBiomeFloraProfile(0.35F, 14, 5, 1, 0, 0, 1, 0, 1, 0, 0,
                PlantTheme.NORMAL, southern ? southern() : miderate());
    }

    private static boolean isSouthern(String id) {
        return id.startsWith("reach") || id.startsWith("stormlands") || id.startsWith("volantis")
                || id.equals("lys") || id.equals("tyrosh") || id.startsWith("myr")
                || id.startsWith("pentos") || id.equals("naath");
    }

    private static List<WeightedTree> miderate() {
        return weighted(OAK, 500, CHESTNUT, 250, VANILLA_BIRCH, 200, BEECH, 200,
                MAPLE, 150, ARAMANT, 100, PLUM, 5);
    }

    private static List<WeightedTree> northern() {
        return weighted(PINE, 500, VANILLA_SPRUCE, 400, FIR, 350, LARCH, 300, ASPEN, 150);
    }

    private static List<WeightedTree> southern() {
        return weighted(CEDAR, 500, CYPRESS, 250, HOLLY, 200, OLIVE, 100,
                POMEGRANATE, 20, ORANGE, 15, LIME, 10, LEMON, 10, ALMOND, 10);
    }

    private static List<WeightedTree> savannah() {
        return weighted(VANILLA_ACACIA, 500, DRAGON, 200, KANUKA, 100, BAOBAB, 20, DEAD_OAK, 2);
    }

    private static List<WeightedTree> tropical() {
        return weighted(VANILLA_JUNGLE, 1000, MAHOGANY, 500, BANANA, 100, PALM, 80, ORANGE, 20);
    }

    private static List<WeightedTree> orchard() {
        return weighted(PLUM, 300, ALMOND, 250, POMEGRANATE, 200, ORANGE, 150, LEMON, 100, LIME, 100, OLIVE, 100);
    }

    private static GOTBiomeFloraProfile override(GOTBiomeFloraProfile source, float trees,
                                                  PlantTheme theme, List<WeightedTree> palette) {
        return new GOTBiomeFloraProfile(trees, source.grassPerChunk(), source.flowersPerChunk(),
                source.reedsPerChunk(), source.deadBushesPerChunk(), source.cactiPerChunk(),
                source.berryBushesPerChunk(), source.fallenLogsPerChunk(), source.bouldersPerChunk(),
                source.aquaticPlantsPerChunk(), source.snowAttemptsPerChunk(), theme, palette);
    }

    private static GOTBiomeFloraProfile replacePalette(GOTBiomeFloraProfile source, List<WeightedTree> palette) {
        return override(source, source.treesPerChunk(), source.plantTheme(), palette);
    }

    private static GOTBiomeFloraProfile replaceTheme(GOTBiomeFloraProfile source, PlantTheme theme) {
        return override(source, source.treesPerChunk(), theme, source.trees());
    }

    private static List<WeightedTree> weighted(GOTTreeSpecies a, int aw) {
        return List.of(new WeightedTree(a, aw));
    }

    private static List<WeightedTree> weighted(GOTTreeSpecies a, int aw, GOTTreeSpecies b, int bw) {
        return List.of(new WeightedTree(a, aw), new WeightedTree(b, bw));
    }

    private static List<WeightedTree> weighted(GOTTreeSpecies a, int aw, GOTTreeSpecies b, int bw,
                                               GOTTreeSpecies c, int cw) {
        return List.of(new WeightedTree(a, aw), new WeightedTree(b, bw), new WeightedTree(c, cw));
    }

    private static List<WeightedTree> weighted(GOTTreeSpecies a, int aw, GOTTreeSpecies b, int bw,
                                               GOTTreeSpecies c, int cw, GOTTreeSpecies d, int dw) {
        return List.of(new WeightedTree(a, aw), new WeightedTree(b, bw), new WeightedTree(c, cw),
                new WeightedTree(d, dw));
    }

    private static List<WeightedTree> weighted(GOTTreeSpecies a, int aw, GOTTreeSpecies b, int bw,
                                               GOTTreeSpecies c, int cw, GOTTreeSpecies d, int dw,
                                               GOTTreeSpecies e, int ew) {
        return List.of(new WeightedTree(a, aw), new WeightedTree(b, bw), new WeightedTree(c, cw),
                new WeightedTree(d, dw), new WeightedTree(e, ew));
    }

    private static List<WeightedTree> weighted(GOTTreeSpecies a, int aw, GOTTreeSpecies b, int bw,
                                               GOTTreeSpecies c, int cw, GOTTreeSpecies d, int dw,
                                               GOTTreeSpecies e, int ew, GOTTreeSpecies f, int fw) {
        return List.of(new WeightedTree(a, aw), new WeightedTree(b, bw), new WeightedTree(c, cw),
                new WeightedTree(d, dw), new WeightedTree(e, ew), new WeightedTree(f, fw));
    }

    private static List<WeightedTree> weighted(GOTTreeSpecies a, int aw, GOTTreeSpecies b, int bw,
                                               GOTTreeSpecies c, int cw, GOTTreeSpecies d, int dw,
                                               GOTTreeSpecies e, int ew, GOTTreeSpecies f, int fw,
                                               GOTTreeSpecies g, int gw) {
        return List.of(new WeightedTree(a, aw), new WeightedTree(b, bw), new WeightedTree(c, cw),
                new WeightedTree(d, dw), new WeightedTree(e, ew), new WeightedTree(f, fw),
                new WeightedTree(g, gw));
    }

    private static List<WeightedTree> weighted(GOTTreeSpecies a, int aw, GOTTreeSpecies b, int bw,
                                               GOTTreeSpecies c, int cw, GOTTreeSpecies d, int dw,
                                               GOTTreeSpecies e, int ew, GOTTreeSpecies f, int fw,
                                               GOTTreeSpecies g, int gw, GOTTreeSpecies h, int hw,
                                               GOTTreeSpecies i, int iw) {
        return List.of(new WeightedTree(a, aw), new WeightedTree(b, bw), new WeightedTree(c, cw),
                new WeightedTree(d, dw), new WeightedTree(e, ew), new WeightedTree(f, fw),
                new WeightedTree(g, gw), new WeightedTree(h, hw), new WeightedTree(i, iw));
    }

    private static List<WeightedTree> weighted(GOTTreeSpecies a, int aw, GOTTreeSpecies b, int bw,
                                               GOTTreeSpecies c, int cw, GOTTreeSpecies d, int dw,
                                               GOTTreeSpecies e, int ew, GOTTreeSpecies f, int fw,
                                               GOTTreeSpecies g, int gw, GOTTreeSpecies h, int hw,
                                               GOTTreeSpecies i, int iw, GOTTreeSpecies j, int jw) {
        return List.of(new WeightedTree(a, aw), new WeightedTree(b, bw), new WeightedTree(c, cw),
                new WeightedTree(d, dw), new WeightedTree(e, ew), new WeightedTree(f, fw),
                new WeightedTree(g, gw), new WeightedTree(h, hw), new WeightedTree(i, iw),
                new WeightedTree(j, jw));
    }
}
