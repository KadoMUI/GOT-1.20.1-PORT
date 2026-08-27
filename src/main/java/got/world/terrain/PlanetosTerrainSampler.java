package got.world.terrain;

import got.world.biome.GOTBiomeMetadata;
import got.world.biome.GOTLegacyTerrainCatalog;
import got.world.biome.PlanetosBiomeManager;

/**
 * Seeded, chunk-border-stable terrain sampler derived from the original
 * GOTChunkProvider's biome-height blend. The implementation is self-contained
 * so the world shape is stable across vanilla noise-router changes.
 */
public final class PlanetosTerrainSampler {
    public static final int SEA_LEVEL = 63;
    private final long seed;

    public PlanetosTerrainSampler(long seed) {
        this.seed = seed;
    }

    public int surfaceHeight(int blockX, int blockZ) {
        int naturalHeight = structureAnchorHeight(blockX, blockZ);
        return PlanetosLandmarkTerrain.applySeaCityMask(blockX, blockZ, naturalHeight);
    }

    /** Natural terrain height used to keep authored structures at their saved elevation. */
    public int structureAnchorHeight(int blockX, int blockZ) {
        BlendedTerrain blended = blendBiomes(blockX, blockZ);

        double continental = fractal(blockX, blockZ, 1.0D / 1800.0D, 4, 0.52D);
        double regional = fractal(blockX, blockZ, 1.0D / 420.0D, 4, 0.50D);
        double detail = fractal(blockX, blockZ, 1.0D / 95.0D, 3, 0.46D) * blended.detailScale();
        double ridge = 1.0D - Math.abs(fractal(blockX, blockZ, 1.0D / 310.0D, 3, 0.52D));
        ridge = ridge * ridge * blended.ridgeStrength();

        double landmarkBoost = PlanetosMountainAnchors.heightBoost(blockX, blockZ);
        double oceanWeight = smoothStep01(blended.oceanWeight());
        double biomeBase = blended.baseHeight() * 25.0D;
        double amplitude = 7.0D + blended.variation() * 22.0D;
        double result = SEA_LEVEL + biomeBase
                + continental * lerp(11.0D, 4.0D, oceanWeight)
                + regional * amplitude
                + detail * 5.0D
                + ridge * 38.0D;

        // Nearby ocean terrain still shapes the approach to a mapped coast,
        // but only the actual atlas biome decides whether this column is land
        // or water. Secondary variants are deliberately excluded from height
        // generation: their 128-block cells are useful for later decoration,
        // but any height boost leaves square plateaus visible on minimaps.
        double oceanFloor = SEA_LEVEL - 6.0D + regional * 5.0D;
        result = lerp(result, Math.min(result, oceanFloor), oceanWeight);

        if (blended.aquatic()) {
            // A shallow shelf starts every mapped shoreline at y=62 and
            // descends toward the noisy ocean floor only after the surrounding
            // biome blend is predominantly water. This removes vertical drops.
            double deepWater = smoothStep01((oceanWeight - 0.48D) / 0.48D);
            result = lerp(SEA_LEVEL - 1.0D, oceanFloor, deepWater);
        } else {
            // Land approaches every coast at a one-block beach before its full
            // regional height returns. Together with the shelf above this makes
            // the land/water boundary a continuous ramp on both sides.
            double inland = smoothStep01((0.52D - oceanWeight) / 0.48D);
            result = lerp(SEA_LEVEL + 1.0D, Math.max(result, SEA_LEVEL + 1.0D), inland);
        }

        if (landmarkBoost > 0.005D) {
            // Applied after the ocean-floor cap so the 55 King Spears and the
            // Targaryen islands can actually break the water surface.
            result += landmarkBoost * (25.0D + (1.0D - Math.abs(regional)) * 6.0D);
        }
        return (int)Math.round(Math.max(-48.0D, Math.min(300.0D, result)));
    }

    /** Sparse, deterministic caves below the surface. */
    public boolean isCave(int blockX, int blockY, int blockZ, int surfaceY) {
        if (blockY > surfaceY - 8 || blockY < -54) return false;
        double broad = fractal3D(blockX, blockY, blockZ, 1.0D / 58.0D, 3, 0.52D);
        double tunnels = Math.abs(fractal3D(blockX + 1703, blockY * 1.35D, blockZ - 919,
                1.0D / 34.0D, 2, 0.55D));
        double depthFade = Math.min(1.0D, (surfaceY - blockY - 7.0D) / 18.0D);
        return broad > 0.54D + (1.0D - depthFade) * 0.18D && tunnels < 0.32D;
    }

    public double surfaceDetail(int blockX, int blockZ) {
        return fractal(blockX + 411, blockZ - 733, 1.0D / 28.0D, 2, 0.5D);
    }

    private BlendedTerrain blendBiomes(int blockX, int blockZ) {
        GOTBiomeMetadata center = PlanetosBiomeManager.getMetadata(blockX, blockZ);
        if (center == null) {
            return new BlendedTerrain(0.1D, 0.15D, 1.0D, 0.0D, 0.0D, false);
        }

        double baseTotal = 0.0D;
        double variationTotal = 0.0D;
        double detailTotal = 0.0D;
        double ridgeTotal = 0.0D;
        double oceanTotal = 0.0D;
        double oceanSampleCount = 0.0D;
        double weightTotal = 0.0D;
        PlanetosTerrainProfile centerProfile = PlanetosTerrainProfile.of(center, null);
        double centerBase = centerProfile.baseHeight();

        // Restore a full 13x13 transition kernel. At eight-block intervals it
        // gives every coastline a 48-block shoulder instead of a cliff.
        // Blend only atlas landforms here. Random secondary variants are
        // selected in 128-block cells, so even a blended edge retains a large
        // square interior if those variants are allowed to alter elevation.
        for (int dz = -6; dz <= 6; dz++) {
            for (int dx = -6; dx <= 6; dx++) {
                int sampleX = blockX + dx * 8;
                int sampleZ = blockZ + dz * 8;
                GOTBiomeMetadata nearby = PlanetosBiomeManager.getMetadata(sampleX, sampleZ);
                if (nearby == null) continue;
                PlanetosTerrainProfile nearbyProfile = PlanetosTerrainProfile.of(nearby, null);
                double weight = 10.0D / Math.sqrt(dx * dx + dz * dz + 0.2D);
                if (nearbyProfile.baseHeight() > centerBase) weight *= 0.5D;
                baseTotal += nearbyProfile.baseHeight() * weight;
                variationTotal += nearbyProfile.variation() * weight;
                detailTotal += nearbyProfile.detailScale() * weight;
                ridgeTotal += nearbyProfile.ridgeStrength() * weight;
                // A radial coastal kernel smooths the atlas water mask itself.
                // The old even 13x13 count preserved square atlas corners in
                // the final shoreline even though terrain height was blended.
                double coastWeight = Math.exp(-(dx * dx + dz * dz) / 11.0D);
                oceanTotal += (nearbyProfile.ocean() ? 1.0D : 0.0D) * coastWeight;
                oceanSampleCount += coastWeight;
                weightTotal += weight;
            }
        }

        if (weightTotal == 0.0D) {
            return new BlendedTerrain(centerProfile.baseHeight(), centerProfile.variation(),
                    centerProfile.detailScale(), centerProfile.ridgeStrength(),
                    centerProfile.ocean() ? 1.0D : 0.0D, centerProfile.ocean());
        }
        double oceanWeight = oceanSampleCount == 0.0D ? 0.0D : oceanTotal / oceanSampleCount;
        boolean shorelineWater = shorelineWater(blockX, blockZ, oceanWeight);
        return new BlendedTerrain(baseTotal / weightTotal,
                variationTotal / weightTotal,
                detailTotal / weightTotal,
                ridgeTotal / weightTotal,
                oceanWeight,
                shorelineWater);
    }

    /**
     * Smoothed authored water mask used by both terrain shaping and sea fill.
     * Only coastal columns are perturbed; deep land/ocean remain exactly as
     * authored. This removes atlas-cell right angles without relocating the
     * coastline by more than a small local shoulder.
     */
    public boolean isWaterColumn(int blockX, int blockZ) {
        return blendBiomes(blockX, blockZ).aquatic();
    }

    private boolean shorelineWater(int blockX, int blockZ, double oceanWeight) {
        if (oceanWeight <= 0.08D) return false;
        if (oceanWeight >= 0.92D) return true;

        // Low-frequency distortion breaks long straight/orthogonal atlas edges
        // while retaining river channels and the authored continental outline.
        double warp = fractal(blockX + 9137, blockZ - 5279, 1.0D / 92.0D, 2, 0.55D);
        double threshold = 0.50D + warp * 0.085D;
        return oceanWeight >= threshold;
    }

    private double fractal(double x, double z, double frequency, int octaves, double persistence) {
        double total = 0.0D;
        double amplitude = 1.0D;
        double normalizer = 0.0D;
        for (int i = 0; i < octaves; i++) {
            total += valueNoise(x * frequency, z * frequency, i) * amplitude;
            normalizer += amplitude;
            amplitude *= persistence;
            frequency *= 2.0D;
        }
        return total / normalizer;
    }

    private double valueNoise(double x, double z, int octave) {
        int x0 = fastFloor(x);
        int z0 = fastFloor(z);
        double tx = smooth(x - x0);
        double tz = smooth(z - z0);
        double a = randomUnit(x0, z0, octave);
        double b = randomUnit(x0 + 1, z0, octave);
        double c = randomUnit(x0, z0 + 1, octave);
        double d = randomUnit(x0 + 1, z0 + 1, octave);
        return lerp(lerp(a, b, tx), lerp(c, d, tx), tz);
    }

    private double fractal3D(double x, double y, double z, double frequency, int octaves, double persistence) {
        double total = 0.0D;
        double amplitude = 1.0D;
        double normalizer = 0.0D;
        for (int i = 0; i < octaves; i++) {
            total += valueNoise3D(x * frequency, y * frequency, z * frequency, i + 17) * amplitude;
            normalizer += amplitude;
            amplitude *= persistence;
            frequency *= 2.0D;
        }
        return total / normalizer;
    }

    private double valueNoise3D(double x, double y, double z, int octave) {
        int x0 = fastFloor(x);
        int y0 = fastFloor(y);
        int z0 = fastFloor(z);
        double tx = smooth(x - x0);
        double ty = smooth(y - y0);
        double tz = smooth(z - z0);
        double x00 = lerp(randomUnit(x0, y0, z0, octave), randomUnit(x0 + 1, y0, z0, octave), tx);
        double x10 = lerp(randomUnit(x0, y0 + 1, z0, octave), randomUnit(x0 + 1, y0 + 1, z0, octave), tx);
        double x01 = lerp(randomUnit(x0, y0, z0 + 1, octave), randomUnit(x0 + 1, y0, z0 + 1, octave), tx);
        double x11 = lerp(randomUnit(x0, y0 + 1, z0 + 1, octave), randomUnit(x0 + 1, y0 + 1, z0 + 1, octave), tx);
        return lerp(lerp(x00, x10, ty), lerp(x01, x11, ty), tz);
    }

    private double randomUnit(long x, long z, int octave) {
        long h = seed ^ x * 341873128712L ^ z * 132897987541L ^ octave * 42317861L;
        h ^= h >>> 33;
        h *= 0xff51afd7ed558ccdL;
        h ^= h >>> 33;
        h *= 0xc4ceb9fe1a85ec53L;
        h ^= h >>> 33;
        return ((h >>> 11) * 0x1.0p-53) * 2.0D - 1.0D;
    }

    private double randomUnit(long x, long y, long z, int octave) {
        long h = seed ^ x * 341873128712L ^ y * 42317861L ^ z * 132897987541L ^ octave * 73428767L;
        h ^= h >>> 33;
        h *= 0xff51afd7ed558ccdL;
        h ^= h >>> 33;
        h *= 0xc4ceb9fe1a85ec53L;
        h ^= h >>> 33;
        return ((h >>> 11) * 0x1.0p-53) * 2.0D - 1.0D;
    }

    private static int fastFloor(double value) {
        int i = (int)value;
        return value < i ? i - 1 : i;
    }
    private static double smooth(double t) { return t * t * (3.0D - 2.0D * t); }
    private static double smoothStep01(double value) {
        double clamped = Math.max(0.0D, Math.min(1.0D, value));
        return smooth(clamped);
    }
    private static double lerp(double a, double b, double t) { return a + (b - a) * t; }

    private record BlendedTerrain(
            double baseHeight,
            double variation,
            double detailScale,
            double ridgeStrength,
            double oceanWeight,
            boolean aquatic
    ) {}
}
