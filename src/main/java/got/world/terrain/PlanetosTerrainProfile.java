package got.world.terrain;

import got.world.biome.GOTBiomeMetadata;
import got.world.biome.GOTLegacyTerrainCatalog;
import got.world.biome.GOTBiomeVariant;

/**
 * Immutable terrain parameters derived from the 0.5F biome metadata and the
 * 0.5G variant lookup. 0.5H2 can refine these values without changing the
 * chunk generator/biome-source contract introduced by H1.
 */
public record PlanetosTerrainProfile(
        double baseHeight,
        double variation,
        double detailScale,
        double ridgeStrength,
        boolean ocean
) {
    public static PlanetosTerrainProfile of(GOTBiomeMetadata biome, GOTBiomeVariant variant) {
        if (biome == null) {
            return new PlanetosTerrainProfile(0.10D, 0.20D, 1.0D, 0.0D, false);
        }

        double base = biome.baseHeight();
        double variation = Math.max(0.02D, biome.heightVariation());
        double detail = 1.0D;
        double ridges = 0.0D;
        boolean ocean = GOTLegacyTerrainCatalog.isAquatic(biome.id());

        if (base >= 1.5D || variation >= 1.5D) {
            detail = 1.25D;
            ridges = 0.90D;
        } else if (variation >= 0.8D) {
            detail = 1.10D;
            ridges = 0.28D;
        } else if (base <= 0.0D && variation <= 0.1D && !ocean) {
            detail = 0.45D;
        }

        if (variant != null) {
            String id = variant.registryName();
            if (id.equals("mountains")) {
                base += 0.65D;
                variation *= 1.8D;
                ridges = 0.85D;
            } else if (id.equals("hills")) {
                base += 0.22D;
                variation *= 1.35D;
                ridges = 0.25D;
            } else if (id.equals("marsh")) {
                base = Math.min(base, -0.08D);
                variation *= 0.30D;
                detail = 0.45D;
            } else if (id.equals("lake") || id.equals("river")) {
                base = Math.min(base, -0.45D);
                variation *= 0.20D;
                ocean = true;
            } else if (id.equals("rocky")) {
                variation *= 1.20D;
                ridges = 0.35D;
            }
        }

        return new PlanetosTerrainProfile(base, variation, detail, ridges, ocean);
    }
}
