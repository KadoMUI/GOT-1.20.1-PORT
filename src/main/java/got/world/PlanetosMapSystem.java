package got.world;

/** Shared coordinate bridge for the original 5291 x 5067 Planetos map. */
public final class PlanetosMapSystem {
    public static final int MAP_WIDTH = 5291;
    public static final int MAP_HEIGHT = 5067;

    /** Legacy atlas origin and scale, shared by biomes, waypoints, roads and players. */
    public static final double MAP_ORIGIN_X = 653.0D;
    public static final double MAP_ORIGIN_Z = 870.0D;
    public static final double BLOCKS_PER_MAP_PIXEL = 128.0D;

    public static final double WORLD_MIN_X = mapToWorldX(0.0D);
    public static final double WORLD_MIN_Z = mapToWorldZ(0.0D);
    public static final double WORLD_MAX_X = mapToWorldX(MAP_WIDTH);
    public static final double WORLD_MAX_Z = mapToWorldZ(MAP_HEIGHT);

    private PlanetosMapSystem() {}

    public static double worldToMapX(double worldX) {
        return worldX / BLOCKS_PER_MAP_PIXEL + MAP_ORIGIN_X;
    }

    public static double worldToMapY(double worldZ) {
        return worldZ / BLOCKS_PER_MAP_PIXEL + MAP_ORIGIN_Z;
    }

    public static double mapToWorldX(double mapX) {
        return (mapX - MAP_ORIGIN_X) * BLOCKS_PER_MAP_PIXEL;
    }

    public static double mapToWorldZ(double mapY) {
        return (mapY - MAP_ORIGIN_Z) * BLOCKS_PER_MAP_PIXEL;
    }
}
