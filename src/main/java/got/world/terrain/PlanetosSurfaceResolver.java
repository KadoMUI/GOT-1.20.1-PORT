package got.world.terrain;

import got.world.biome.GOTBiomeMetadata;
import got.world.biome.GOTLegacyTerrainCatalog;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

/** Legacy-aware top/filler selection. Features and decorators do not live here. */
public final class PlanetosSurfaceResolver {
    private PlanetosSurfaceResolver() {}

    public static BlockState top(GOTBiomeMetadata metadata, int y, int seaLevel) {
        return top(metadata, 0, 0, y, seaLevel, 0, 0.0D);
    }

    public static BlockState filler(GOTBiomeMetadata metadata) {
        return filler(metadata, 0, 0, 0.0D);
    }

    public static BlockState top(GOTBiomeMetadata metadata, int x, int z, int y,
                                 int seaLevel, int slope, double detail) {
        if (metadata == null) return Blocks.GRASS_BLOCK.defaultBlockState();
        String id = metadata.id();

        if (GOTLegacyTerrainCatalog.isAquatic(id) && y < seaLevel - 3) {
            return detail > 0.35D ? Blocks.SAND.defaultBlockState() : Blocks.GRAVEL.defaultBlockState();
        }
        if (slope >= 5 || y > 128) {
            if (metadata.temperature() < 0.3F && y > 118) return Blocks.SNOW_BLOCK.defaultBlockState();
            return id.startsWith("shadow_") || id.startsWith("valyria")
                    ? Blocks.BLACKSTONE.defaultBlockState()
                    : Blocks.STONE.defaultBlockState();
        }
        if (id.equals("dorne") || id.equals("dorne_forest")) {
            if (detail > 0.32D) return Blocks.RED_SAND.defaultBlockState();
            if (detail < -0.35D) return Blocks.COARSE_DIRT.defaultBlockState();
        }
        if (isMixedSandCountry(id)) {
            if (detail > 0.38D) return Blocks.RED_SAND.defaultBlockState();
            if (detail < -0.22D) return Blocks.SAND.defaultBlockState();
        }
        if (id.equals("ghiscar") || id.equals("ghiscar_forest")) {
            return detail > 0.05D ? Blocks.SAND.defaultBlockState() : Blocks.COARSE_DIRT.defaultBlockState();
        }
        if (id.equals("frozen_shore") && detail > 0.0D) return Blocks.PACKED_ICE.defaultBlockState();
        if (id.equals("yeen") && detail > 0.1D) return Blocks.OBSIDIAN.defaultBlockState();
        return metadata.surface();
    }

    public static BlockState filler(GOTBiomeMetadata metadata, int x, int z, double detail) {
        if (metadata == null) return Blocks.DIRT.defaultBlockState();
        String id = metadata.id();
        if (id.equals("dorne") || id.equals("dorne_forest") || isMixedSandCountry(id)) {
            return detail > 0.15D ? Blocks.SANDSTONE.defaultBlockState() : Blocks.DIRT.defaultBlockState();
        }
        if (id.equals("dorne_mesa")) return Blocks.TERRACOTTA.defaultBlockState();
        if (id.equals("yeen")) return Blocks.BASALT.defaultBlockState();
        return metadata.filler();
    }

    private static boolean isMixedSandCountry(String id) {
        return id.equals("disputed_lands") || id.equals("disputed_lands_forest")
                || id.equals("lys") || id.equals("myr") || id.equals("myr_forest")
                || id.equals("pentos") || id.equals("pentos_forest")
                || id.equals("stepstones") || id.equals("tyrosh");
    }
}
