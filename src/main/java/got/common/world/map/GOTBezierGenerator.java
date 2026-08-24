package got.common.world.map;

/**
 * Terrain-facing Bezier query facade shared by the atlas, road decorator,
 * and future structure passes.
 */
public final class GOTBezierGenerator {
    private GOTBezierGenerator() {}
    public static boolean isRoadAt(int x, int z) { return GOTBeziers.isRoadAt(x, z); }
    public static boolean isWallAt(int x, int z) { return GOTBeziers.isWallAt(x, z); }
    public static boolean isLinkerAt(int x, int z) { return GOTBeziers.isLinkerAt(x, z); }
    public static boolean isRouteAt(int x, int z) { return isRoadAt(x, z) || isLinkerAt(x, z); }
    public static float roadProximity(int x, int z, int radius) { return GOTBeziers.isRoadNear(x, z, radius); }
    public static float wallProximity(int x, int z, int radius) { return GOTBeziers.isWallNear(x, z, radius); }
    public static float linkerProximity(int x, int z, int radius) { return GOTBeziers.isLinkerNear(x, z, radius); }
}
