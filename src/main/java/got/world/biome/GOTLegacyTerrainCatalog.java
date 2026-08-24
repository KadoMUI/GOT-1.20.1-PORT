package got.world.biome;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Set;

/**
 * Terrain values recovered from the original 1.7.10 GOTBiome catalogue.
 *
 * <p>The old mod stored these as the two arguments passed to
 * {@code setMinMaxHeight}. Keeping the values in one catalogue prevents modern
 * biome presets (which describe climate, not landform) from accidentally
 * turning places such as the Dothraki Sea or the Riverlands into oceans.</p>
 */
public final class GOTLegacyTerrainCatalog {
    public enum SurfaceKind {
        GRASS, SAND, RED_SAND, STONE, SNOW, MUD, VOLCANIC, BEACH, GRAVEL
    }

    public record TerrainSpec(float baseHeight, float variation, SurfaceKind surfaceKind, boolean aquatic) {
        public BlockState surface() {
            return switch (surfaceKind) {
                case SAND, BEACH -> Blocks.SAND.defaultBlockState();
                case RED_SAND -> Blocks.RED_SAND.defaultBlockState();
                case STONE -> Blocks.STONE.defaultBlockState();
                case SNOW -> Blocks.SNOW_BLOCK.defaultBlockState();
                case MUD -> Blocks.MUD.defaultBlockState();
                case VOLCANIC -> Blocks.BLACKSTONE.defaultBlockState();
                case GRAVEL -> Blocks.GRAVEL.defaultBlockState();
                default -> Blocks.GRASS_BLOCK.defaultBlockState();
            };
        }

        public BlockState filler() {
            return switch (surfaceKind) {
                case SAND, BEACH -> Blocks.SANDSTONE.defaultBlockState();
                case RED_SAND -> Blocks.TERRACOTTA.defaultBlockState();
                case STONE -> Blocks.STONE.defaultBlockState();
                case SNOW -> Blocks.DIRT.defaultBlockState();
                case MUD -> Blocks.MUD.defaultBlockState();
                case VOLCANIC -> Blocks.BASALT.defaultBlockState();
                case GRAVEL -> Blocks.STONE.defaultBlockState();
                default -> Blocks.DIRT.defaultBlockState();
            };
        }
    }

    private static final Set<String> AQUATIC = Set.of(
            "ocean", "ocean1", "ocean2", "ocean3", "bleeding_sea",
            "valyria_sea", "valyria_sea1", "valyria_sea2", "king_spears",
            "lake", "river"
    );

    private static final Set<String> MOUNTAINS = Set.of(
            "arryn_mountains", "bone_mountains", "dorne_mountains", "essos_mountains",
            "frostfangs", "ibben_mountains", "mossovy_mountains", "north_mountains",
            "shadow_mountains", "skirling_pass", "sothoryos_mountains",
            "ulthos_mountains", "valyria_volcano"
    );

    private static final Set<String> HILLS = Set.of(
            "braavos_hills", "cannibal_sands_hills", "dothraki_sea_hills",
            "ibben_colony_hills", "iron_islands_hills", "jogos_nhai_desert_hills",
            "jogos_nhai_hills", "lhazar_hills", "lorath_hills", "massy_hills",
            "north_hills", "norvos_hills", "pentos_hills", "qohor_hills",
            "reach_hills", "sothoryos_desert_hills", "westerlands_hills"
    );

    private static final Set<String> MARSHES = Set.of(
            "essos_marshes", "mossovy_marshes", "neck", "shrykes_land",
            "sothoryos_mangrove", "ulthos_marshes", "ulthos_marshes_forest",
            "volantis_marshes", "yi_ti_marshes"
    );

    private static final Set<String> BEACHES = Set.of(
            "beach", "beach_gravel", "beach_white", "bleeding_beach"
    );

    private static final Set<String> SAND = Set.of(
            "cannibal_sands", "cannibal_sands_hills", "dorne_desert", "jogos_nhai_desert",
            "jogos_nhai_desert_hills", "qarth", "qarth_colony", "sothoryos_desert",
            "sothoryos_desert_cold", "sothoryos_desert_hills", "ulthos_desert",
            "ulthos_desert_cold"
    );

    private static final Set<String> STONE = Set.of(
            "dragonstone", "lorath", "lorath_forest", "lorath_hills", "lorath_maze",
            "norvos", "norvos_forest", "norvos_hills", "stoney_shore"
    );

    private static final Set<String> SNOW = Set.of(
            "always_winter", "frostfangs", "frozen_shore", "sothoryos_frost",
            "ulthos_frost", "westeros_frost"
    );

    private static final Set<String> MUD = Set.of(
            "sothoryos_jungle", "sothoryos_jungle_edge", "sothoryos_mangrove"
    );

    private static final Set<String> VOLCANIC = Set.of(
            "shadow_land", "shadow_mountains", "shadow_town", "valyria_volcano", "yeen"
    );

    private GOTLegacyTerrainCatalog() {}

    public static TerrainSpec forBiome(String id) {
        float base = 0.1F;
        float variation = 0.15F;

        if (MOUNTAINS.contains(id)) {
            base = 2.0F;
            variation = 2.0F;
        } else if (HILLS.contains(id)) {
            variation = 1.0F;
        } else if (MARSHES.contains(id)) {
            base = 0.0F;
            variation = 0.1F;
        } else if (AQUATIC.contains(id)) {
            base = switch (id) {
                case "ocean1", "king_spears" -> -0.7F;
                case "ocean2", "valyria_sea2" -> -0.8F;
                case "ocean3", "valyria_sea1" -> -0.9F;
                case "lake", "river" -> -0.5F;
                default -> -1.0F;
            };
            variation = id.equals("river") ? 0.0F : id.equals("lake") ? 0.2F : 0.3F;
        } else if (BEACHES.contains(id)) {
            variation = 0.0F;
        } else if (id.equals("island")) {
            base = 0.0F;
            variation = 0.3F;
        } else if (id.equals("dorne_mesa")) {
            base = 1.5F;
            variation = 0.05F;
        } else if (id.equals("dragonstone")) {
            base = 0.3F;
            variation = 0.35F;
        } else if (id.equals("stepstones")) {
            base = 0.0F;
            variation = 0.5F;
        } else if (id.equals("shrykes_land")) {
            base = 0.0F;
            variation = 0.1F;
        } else if (id.equals("riverlands_forest")) {
            variation = 1.0F;
        }

        SurfaceKind surface = surfaceKind(id);
        return new TerrainSpec(base, variation, surface, AQUATIC.contains(id));
    }

    private static SurfaceKind surfaceKind(String id) {
        if (id.equals("beach_gravel")) return SurfaceKind.GRAVEL;
        if (id.equals("bleeding_beach") || id.equals("dorne_mesa") || id.equals("qarth_desert")) {
            return SurfaceKind.RED_SAND;
        }
        if (BEACHES.contains(id)) return SurfaceKind.BEACH;
        if (SNOW.contains(id)) return SurfaceKind.SNOW;
        if (MUD.contains(id)) return SurfaceKind.MUD;
        if (VOLCANIC.contains(id)) return SurfaceKind.VOLCANIC;
        if (STONE.contains(id) || MOUNTAINS.contains(id)) return SurfaceKind.STONE;
        if (SAND.contains(id)) return SurfaceKind.SAND;
        if (AQUATIC.contains(id)) return SurfaceKind.GRAVEL;
        return SurfaceKind.GRASS;
    }

    public static boolean isAquatic(String id) {
        return AQUATIC.contains(id);
    }

    public static boolean isBakedLandform(String id) {
        return AQUATIC.contains(id) || MOUNTAINS.contains(id) || HILLS.contains(id)
                || MARSHES.contains(id) || BEACHES.contains(id) || id.equals("dorne_mesa")
                || id.equals("stepstones") || id.equals("island");
    }
}
