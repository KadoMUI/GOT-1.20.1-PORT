package got.world.biome;

import got.world.genlayer.GOTBiomeMapData;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;

/** Atlas biome lookup plus large-scale secondary variants. */
public final class DefaultPlanetosGenLayerProvider implements PendingGenLayerProvider {
    private final long seed;

    public DefaultPlanetosGenLayerProvider(long seed) {
        this.seed = seed;
        GOTBiomeMapData.load();
    }

    @Override
    public ResourceKey<Biome> getBiome(int x, int z) {
        return GOTBiomes.key(GOTBiomeMapData.registryId(GOTBiomeMapData.legacyIdAtBlock(x, z)));
    }

    @Override
    public GOTBiomeVariant getVariant(int x, int z) {
        return selectVariant(x, z);
    }

    private GOTBiomeVariant selectVariant(int x, int z) {
        String id = GOTBiomeMapData.registryId(GOTBiomeMapData.legacyIdAtBlock(x, z));

        // These are literal atlas biomes, not substring hints. In particular,
        // Riverlands is land and the Dothraki Sea is grassland.
        if (id.equals("river")) return GOTBiomeVariantList.RIVER;
        if (id.equals("lake")) return GOTBiomeVariantList.LAKE;
        if (GOTLegacyTerrainCatalog.isBakedLandform(id)) return GOTBiomeVariantList.STANDARD;

        if (id.endsWith("_forest") || id.contains("forest_")) {
            long h = hash(x >> 6, z >> 6);
            return Math.floorMod(h, 5) == 0
                    ? GOTBiomeVariantList.DENSE_FOREST
                    : GOTBiomeVariantList.FOREST;
        }

        long h = hash(x >> 7, z >> 7);
        int roll = (int)Math.floorMod(h, 100);
        if (roll < 7) return GOTBiomeVariantList.HILLS;
        if (roll < 10) return GOTBiomeVariantList.ROCKY;
        if (roll == 10) return GOTBiomeVariantList.ORCHARD;
        return GOTBiomeVariantList.STANDARD;
    }

    private long hash(long x, long z) {
        long h = seed ^ x * 341873128712L ^ z * 132897987541L;
        h ^= h >>> 33;
        h *= 0xff51afd7ed558ccdL;
        h ^= h >>> 33;
        return h;
    }
}
