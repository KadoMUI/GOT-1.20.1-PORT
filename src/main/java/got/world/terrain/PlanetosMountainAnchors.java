package got.world.terrain;

import got.common.world.map.GOTAbstractWaypoint;
import got.common.world.map.GOTWaypoint;

import java.util.ArrayList;
import java.util.List;

/** Fixed landmark peaks recovered from the original GOTMountains catalogue. */
public final class PlanetosMountainAnchors {
    private interface TerrainAnchor {
        double boost(int blockX, int blockZ);
    }

    private record Anchor(int x, int z, double height, int range) implements TerrainAnchor {
        public double boost(int blockX, int blockZ) {
            double dx = blockX - x;
            double dz = blockZ - z;
            double distance = Math.sqrt(dx * dx + dz * dz);
            return distance < range ? (1.0D - distance / range) * height : 0.0D;
        }
    }

    private record FootprintAnchor(int minX, int minZ, int maxX, int maxZ,
                                   double height, int falloff) implements TerrainAnchor {
        public double boost(int blockX, int blockZ) {
            int dx = blockX < minX ? minX - blockX : Math.max(0, blockX - maxX);
            int dz = blockZ < minZ ? minZ - blockZ : Math.max(0, blockZ - maxZ);
            double distance = Math.sqrt((double)dx * dx + (double)dz * dz);
            return distance < falloff ? (1.0D - distance / falloff) * height : 0.0D;
        }
    }

    private static final List<TerrainAnchor> ANCHORS = build();

    private PlanetosMountainAnchors() {}

    public static double heightBoost(int blockX, int blockZ) {
        double result = 0.0D;
        for (TerrainAnchor anchor : ANCHORS) result += anchor.boost(blockX, blockZ);
        return result;
    }

    public static int count() {
        return ANCHORS.size();
    }

    private static List<TerrainAnchor> build() {
        List<TerrainAnchor> result = new ArrayList<>();
        addMap(result, 2847, 1273, 2.0D, 50);
        addMap(result, 2588, 1275, 2.0D, 50);
        addMap(result, 2708, 1230, 2.0D, 50);
        addMap(result, 2638, 1252, 2.0D, 50);

        int[][] kingSpears = {
                {793,1646},{795,1650},{798,1646},{800,1648},{800,1652},
                {805,1646},{805,1650},{804,1654},{811,1648},{810,1651},
                {817,1649},{814,1652},{819,1652},{822,1649},{828,1648},
                {833,1647},{837,1644},{842,1644},{846,1652},{851,1643},
                {856,1641},{860,1642},{864,1642},{869,1639},{872,1635},
                {877,1633},{882,1634},{873,1651},{877,1649},{880,1644},
                {882,1647},{881,1650},{882,1638},{884,1640},{885,1643},
                {885,1648},{886,1635},{912,1639},{908,1639},{904,1638},
                {904,1641},{902,1644},{900,1641},{899,1646},{899,1636},
                {895,1636},{896,1639},{895,1644},{894,1648},{892,1642},
                {892,1633},{890,1636},{889,1650},{889,1640},{889,1645}
        };
        for (int[] point : kingSpears) addMap(result, point[0], point[1], 4.0D, 40);

        addWaypoint(result, GOTWaypoint.PINGBEI, 2.0D, 50);
        addWaypoint(result, GOTWaypoint.ANBEI, 2.0D, 50);
        addWaypoint(result, GOTWaypoint.EASTWATCH, 3.0D, 50);
        addWaypoint(result, GOTWaypoint.WESTWATCH, 3.0D, 50);
        addWaypoint(result, GOTWaypoint.CROWS_NEST, 3.0D, 250);
        addWaypoint(result, GOTWaypoint.GRIFFINS_ROOST, 3.0D, 250);
        addWaypoint(result, GOTWaypoint.BLOODY_GATE.info(0.0D, -0.5D), 3.0D, 250);
        // The authored Eyrie is a 120x165 north/south footprint offset from
        // its waypoint. Give it a matching plateau and rounded falloff instead
        // of the old circular peak centered 64 blocks north of the structure.
        addFootprint(result, GOTWaypoint.THE_EYRIE,
                -47, -144, 120, 165, 3.0D, 180);
        addWaypoint(result, GOTWaypoint.GOLDENHILL, 1.0D, 70);
        addWaypoint(result, GOTWaypoint.VICTARION_LANDING, 0.5D, 100);
        addWaypoint(result, GOTWaypoint.LAST_HEARTH, 1.0D, 70);
        addWaypoint(result, GOTWaypoint.HOLLOW_HILL, 1.0D, 70);
        addWaypoint(result, GOTWaypoint.BREAKSTONE_HILL, 1.0D, 70);
        addWaypoint(result, GOTWaypoint.IRONRATH, 1.0D, 70);
        addWaypoint(result, GOTWaypoint.HIGHPOINT, 1.0D, 70);
        addWaypoint(result, GOTWaypoint.DEEPWOOD_MOTTE, 1.0D, 70);
        addWaypoint(result, GOTWaypoint.BARROWTOWN, 2.0D, 150);
        addWaypoint(result, GOTWaypoint.HAMMERHORN, 1.0D, 70);
        addWaypoint(result, GOTWaypoint.HARRIDAN_HILL, 1.0D, 70);
        addWaypoint(result, GOTWaypoint.DRAGONSTONE, 1.0D, 70);
        addWaypoint(result, GOTWaypoint.GREYWATER_WATCH, 0.5D, 70);
        addWaypoint(result, GOTWaypoint.PENNYTREE.info(0.0D, 1.0D), 1.0D, 70);
        addWaypoint(result, GOTWaypoint.PENNYTREE.info(0.0D, -1.0D), 1.0D, 70);
        addWaypoint(result, GOTWaypoint.VAES_DOTHRAK.info(0.0D, -2.0D), 5.0D, 250);
        addWaypoint(result, GOTWaypoint.MOAT_KAILIN, 1.0D, 70);
        addWaypoint(result, GOTWaypoint.NAGGAS_HILL, 1.0D, 70);
        addWaypoint(result, GOTWaypoint.PYKE, 1.0D, 70);
        addWaypoint(result, GOTWaypoint.FIST, 5.0D, 250);
        addWaypoint(result, GOTWaypoint.HIGHGARDEN.info(-1.0D, 1.0D), 1.0D, 120);
        addWaypoint(result, GOTWaypoint.HORN_HILL, 1.5D, 120);
        addWaypoint(result, GOTWaypoint.UPLANDS, 1.5D, 120);
        // Storm's End must sit on the hill rather than beside/through it. Shape the
        // terrain beneath the authored 71x74 castle footprint and let it fall away
        // around the castle perimeter.
        addFootprint(result, GOTWaypoint.STORMS_END,
                -35, -7, 71, 74, 1.0D, 70);
        addWaypoint(result, GOTWaypoint.AEGON, 2.5D, 150);
        addWaypoint(result, GOTWaypoint.VISENYA, 1.5D, 120);
        addWaypoint(result, GOTWaypoint.RAENYS, 1.5D, 120);
        addMap(result, 388, 1946, 1.0D, 70);
        addMap(result, 389, 1944, 1.0D, 70);
        addMap(result, 391, 1944, 1.0D, 70);
        return List.copyOf(result);
    }

    private static void addMap(List<TerrainAnchor> target,
                               double mapX, double mapZ, double height, int range) {
        target.add(new Anchor(GOTWaypoint.mapToWorldX(mapX), GOTWaypoint.mapToWorldZ(mapZ), height, range));
    }

    private static void addWaypoint(List<TerrainAnchor> target, GOTAbstractWaypoint waypoint,
                                    double height, int range) {
        addMap(target, waypoint.getImgX(), waypoint.getImgY(), height, range);
    }

    private static void addFootprint(List<TerrainAnchor> target, GOTWaypoint waypoint,
                                     int offsetX, int offsetZ, int width, int length,
                                     double height, int falloff) {
        int minX = waypoint.getCoordX() + offsetX;
        int minZ = waypoint.getCoordZ() + offsetZ;
        target.add(new FootprintAnchor(minX, minZ,
                minX + width - 1, minZ + length - 1, height, falloff));
    }
}
