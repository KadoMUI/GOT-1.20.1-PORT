package got.world.terrain;

import got.common.world.map.GOTWaypoint;

import java.util.List;

/**
 * Terrain masks required by authored structures that were built over open
 * water.  Their schematics retain every authored block, including their own
 * foundations, while the atlas terrain is lowered into a feathered sea shelf.
 */
public final class PlanetosLandmarkTerrain {
    private static final int OCEAN_FLOOR = PlanetosTerrainSampler.SEA_LEVEL - 8;
    private static final int FEATHER = 32;

    private static final List<SeaCity> SEA_CITIES = List.of(
            seaCity(GOTWaypoint.BRAAVOS, 146, 136, -23, -49),
            seaCity(GOTWaypoint.LYS, 155, 156, -4, -82),
            seaCity(GOTWaypoint.MYR, 101, 102, -5, -57),
            seaCity(GOTWaypoint.TYROSH, 92, 82, -40, -20)
    );

    private PlanetosLandmarkTerrain() {}

    public static int applySeaCityMask(int blockX, int blockZ, int naturalHeight) {
        double weight = seaCityWeight(blockX, blockZ);
        if (weight <= 0.0D) return naturalHeight;
        return (int)Math.round(lerp(naturalHeight, OCEAN_FLOOR, weight));
    }

    public static boolean isSeaCityWater(int blockX, int blockZ) {
        return seaCityWeight(blockX, blockZ) > 0.0D;
    }

    static double seaCityWeight(int blockX, int blockZ) {
        double result = 0.0D;
        for (SeaCity city : SEA_CITIES) {
            result = Math.max(result, city.weight(blockX, blockZ));
        }
        return result;
    }

    private static SeaCity seaCity(GOTWaypoint waypoint, int width, int length,
                                   int offsetX, int offsetZ) {
        int minX = waypoint.getCoordX() + offsetX;
        int minZ = waypoint.getCoordZ() + offsetZ;
        return new SeaCity(minX, minZ, minX + width - 1, minZ + length - 1);
    }

    private static double lerp(double start, double end, double amount) {
        return start + (end - start) * amount;
    }

    private record SeaCity(int minX, int minZ, int maxX, int maxZ) {
        double weight(int x, int z) {
            int dx = x < minX ? minX - x : Math.max(0, x - maxX);
            int dz = z < minZ ? minZ - z : Math.max(0, z - maxZ);
            if (dx == 0 && dz == 0) return 1.0D;
            double distance = Math.sqrt((double)dx * dx + (double)dz * dz);
            if (distance >= FEATHER) return 0.0D;
            double linear = 1.0D - distance / FEATHER;
            return linear * linear * (3.0D - 2.0D * linear);
        }
    }
}
