package got.common.world.map;

import java.util.*;

/**
 * 1.20.1 port of the original spline database used by both the atlas and world generation.
 * Coordinates are stored in world blocks; the registration data remains in original map pixels.
 */
public final class GOTBeziers {
    public enum Type { ROAD, WALL, LINKER }

    public static final Collection<GOTBeziers> CONTENT = new ArrayList<>();
    private static BezierPointDatabase linkerPointDatabase = new BezierPointDatabase();
    private static BezierPointDatabase roadPointDatabase = new BezierPointDatabase();
    private static BezierPointDatabase wallPointDatabase = new BezierPointDatabase();
    private static boolean initialized;

    private final Type type;
    private final List<BezierPoint> endpoints;
    private BezierPoint[] bezierPoints;
    private double minX;
    private double maxX;
    private double minZ;
    private double maxZ;

    private GOTBeziers(Type type, BezierPoint... endpoints) {
        this.type = type;
        this.endpoints = List.of(endpoints);
    }

    public static synchronized void onInit() {
        CONTENT.clear();
        roadPointDatabase = new BezierPointDatabase();
        wallPointDatabase = new BezierPointDatabase();
        linkerPointDatabase = new BezierPointDatabase();
        GOTBezierData.registerAll();
        initialized = true;
    }

    public static void ensureInitialized() {
        if (!initialized) onInit();
    }

    static void registerBezier(Type type, Object... objects) {
        List<BezierPoint> points = new ArrayList<>();
        for (Object object : objects) {
            if (object instanceof GOTAbstractWaypoint waypoint) {
                points.add(new BezierPoint(waypoint.getCoordX(), waypoint.getCoordZ()));
            } else if (object instanceof double[] coords && coords.length == 2) {
                points.add(new BezierPoint(GOTWaypoint.mapToWorldX(coords[0]), GOTWaypoint.mapToWorldZ(coords[1])));
            } else {
                throw new IllegalArgumentException("Bezier argument must be a waypoint or two map coordinates");
            }
        }
        GOTBeziers[] splines = BezierCurves.getSplines(points.toArray(BezierPoint[]::new), type);
        if (type != Type.LINKER) CONTENT.addAll(Arrays.asList(splines));
    }

    static void registerLinker(GOTAbstractWaypoint waypoint) {
        registerBezier(Type.LINKER, waypoint.getInstance(), waypoint);
    }

    static void registerLinkerAutoInv(GOTAbstractWaypoint waypoint) { registerAuto(waypoint, true); }
    static void registerLinkerAuto(GOTAbstractWaypoint waypoint) { registerAuto(waypoint, false); }

    private static void registerAuto(GOTAbstractWaypoint waypoint, boolean inverse) {
        GOTWaypoint base = waypoint.getInstance();
        double sx = waypoint.getShiftX();
        double sy = waypoint.getShiftY();
        boolean moveX = inverse ? Math.abs(sx) <= Math.abs(sy) : Math.abs(sx) > Math.abs(sy);
        if (moveX) {
            double extra = Math.min(Math.abs(sx / 2.0D), 0.1D) * (base.getImgX() < waypoint.getImgX() ? -1.0D : 1.0D);
            GOTAbstractWaypoint middle = base.info(sx + extra, sy);
            registerBezier(Type.LINKER, base, middle);
            registerBezier(Type.LINKER, middle, waypoint);
        } else {
            double extra = Math.min(Math.abs(sy / 2.0D), 0.1D) * (base.getImgY() < waypoint.getImgY() ? -1.0D : 1.0D);
            GOTAbstractWaypoint middle = base.info(sx, sy + extra);
            registerBezier(Type.LINKER, base, middle);
            registerBezier(Type.LINKER, middle, waypoint);
        }
    }

    public static boolean isBezierAt(int x, int z, Type type) { return isBezierNear(x, z, 4, type) >= 0.0F; }

    public static float isBezierNear(int x, int z, int radius, Type type) {
        ensureInitialized();
        double radiusSq = (double) radius * radius;
        float nearest = -1.0F;
        for (BezierPoint point : database(type).getPointsForCoords(x, z)) {
            double dx = point.x - x;
            double dz = point.z - z;
            double distSq = dx * dx + dz * dz;
            if (distSq < radiusSq) {
                float normalized = (float) (distSq / radiusSq);
                if (nearest < 0.0F || normalized < nearest) nearest = normalized;
            }
        }
        return nearest;
    }

    public static double distanceToNearest(int x, int z, Type type, int searchRadius) {
        float normalized = isBezierNear(x, z, searchRadius, type);
        return normalized < 0.0F ? Double.POSITIVE_INFINITY : Math.sqrt(normalized) * searchRadius;
    }

    public static boolean isRoadAt(int x, int z) { return isBezierAt(x, z, Type.ROAD); }
    public static boolean isWallAt(int x, int z) { return isBezierAt(x, z, Type.WALL); }
    public static boolean isLinkerAt(int x, int z) { return isBezierAt(x, z, Type.LINKER); }
    public static float isRoadNear(int x, int z, int radius) { return isBezierNear(x, z, radius, Type.ROAD); }
    public static float isWallNear(int x, int z, int radius) { return isBezierNear(x, z, radius, Type.WALL); }
    public static float isLinkerNear(int x, int z, int radius) { return isBezierNear(x, z, radius, Type.LINKER); }

    private static BezierPointDatabase database(Type type) {
        return switch (type) {
            case ROAD -> roadPointDatabase;
            case WALL -> wallPointDatabase;
            case LINKER -> linkerPointDatabase;
        };
    }

    public Type getType() { return type; }
    public BezierPoint[] getBezierPoints() { return bezierPoints; }
    public Collection<BezierPoint> getEndpoints() { return endpoints; }

    /**
     * Cheap route-level rejection for the atlas renderer.  The point arrays are
     * intentionally detailed enough for world generation, so walking every
     * off-screen point every frame is prohibitively expensive at close zoom.
     */
    public boolean intersectsWorldBounds(double xMin, double zMin, double xMax, double zMax) {
        return maxX >= xMin && minX <= xMax && maxZ >= zMin && minZ <= zMax;
    }

    private void updateBounds() {
        minX = Double.POSITIVE_INFINITY;
        maxX = Double.NEGATIVE_INFINITY;
        minZ = Double.POSITIVE_INFINITY;
        maxZ = Double.NEGATIVE_INFINITY;

        if (bezierPoints != null) {
            for (BezierPoint point : bezierPoints) includeInBounds(point);
        }
        for (BezierPoint endpoint : endpoints) includeInBounds(endpoint);
    }

    private void includeInBounds(BezierPoint point) {
        minX = Math.min(minX, point.x);
        maxX = Math.max(maxX, point.x);
        minZ = Math.min(minZ, point.z);
        maxZ = Math.max(maxZ, point.z);
    }

    public record BezierPoint(double x, double z) {}

    public static final class BezierPointDatabase {
        private final Map<Long, List<BezierPoint>> pointMap = new HashMap<>();
        private static long key(int x, int z) { return ((long)x << 32) ^ (z & 0xffffffffL); }
        private void add(BezierPoint point) {
            int cellX = (int)Math.round(point.x / 1000.0D);
            int cellZ = (int)Math.round(point.z / 1000.0D);
            for (int dx = -1; dx <= 1; dx++) for (int dz = -1; dz <= 1; dz++)
                pointMap.computeIfAbsent(key(cellX + dx, cellZ + dz), ignored -> new ArrayList<>()).add(point);
        }
        private List<BezierPoint> getPointsForCoords(int x, int z) {
            return pointMap.getOrDefault(key(x / 1000, z / 1000), List.of());
        }
    }

    private static final class BezierCurves {
        private static GOTBeziers[] getSplines(BezierPoint[] knots, Type type) {
            if (knots.length < 2) return new GOTBeziers[0];
            if (knots.length == 2) {
                GOTBeziers line = new GOTBeziers(type, knots);
                int samples = Math.max(1, (int)Math.round(distance(knots[0], knots[1])));
                line.bezierPoints = new BezierPoint[samples];
                for (int i = 0; i < samples; i++) {
                    BezierPoint p = lerp(knots[0], knots[1], (double)i / samples);
                    line.bezierPoints[i] = p;
                    database(type).add(p);
                }
                line.updateBounds();
                return new GOTBeziers[]{line};
            }
            double[] xs = Arrays.stream(knots).mapToDouble(BezierPoint::x).toArray();
            double[] zs = Arrays.stream(knots).mapToDouble(BezierPoint::z).toArray();
            double[][] cx = getControlPoints(xs);
            double[][] cz = getControlPoints(zs);
            GOTBeziers[] result = new GOTBeziers[knots.length - 1];
            for (int i = 0; i < result.length; i++) {
                BezierPoint c1 = new BezierPoint(cx[0][i], cz[0][i]);
                BezierPoint c2 = new BezierPoint(cx[1][i], cz[1][i]);
                GOTBeziers curve = new GOTBeziers(type, knots[i], knots[i + 1]);
                int samples = Math.max(1, (int)Math.round(distance(knots[i], knots[i + 1])));
                curve.bezierPoints = new BezierPoint[samples];
                for (int j = 0; j < samples; j++) {
                    BezierPoint p = bezier(knots[i], c1, c2, knots[i + 1], (double)j / samples);
                    curve.bezierPoints[j] = p;
                    database(type).add(p);
                }
                curve.updateBounds();
                result[i] = curve;
            }
            return result;
        }

        private static double[][] getControlPoints(double[] knots) {
            int n = knots.length - 1;
            double[] first = new double[n], second = new double[n];
            double[] a = new double[n], b = new double[n], c = new double[n], r = new double[n];
            a[0] = 0; b[0] = 2; c[0] = 1; r[0] = knots[0] + 2 * knots[1];
            for (int i = 1; i < n - 1; i++) { a[i] = 1; b[i] = 4; c[i] = 1; r[i] = 4 * knots[i] + 2 * knots[i + 1]; }
            a[n - 1] = 2; b[n - 1] = 7; c[n - 1] = 0; r[n - 1] = 8 * knots[n - 1] + knots[n];
            for (int i = 1; i < n; i++) { double m = a[i] / b[i - 1]; b[i] -= m * c[i - 1]; r[i] -= m * r[i - 1]; }
            first[n - 1] = r[n - 1] / b[n - 1];
            for (int i = n - 2; i >= 0; --i) first[i] = (r[i] - c[i] * first[i + 1]) / b[i];
            for (int i = 0; i < n - 1; i++) second[i] = 2 * knots[i + 1] - first[i + 1];
            second[n - 1] = 0.5 * (knots[n] + first[n - 1]);
            return new double[][]{first, second};
        }

        private static BezierPoint bezier(BezierPoint p0, BezierPoint p1, BezierPoint p2, BezierPoint p3, double t) {
            BezierPoint a = lerp(p0,p1,t), b = lerp(p1,p2,t), c = lerp(p2,p3,t);
            return lerp(lerp(a,b,t), lerp(b,c,t), t);
        }
        private static BezierPoint lerp(BezierPoint a, BezierPoint b, double t) { return new BezierPoint(a.x + (b.x-a.x)*t, a.z + (b.z-a.z)*t); }
        private static double distance(BezierPoint a, BezierPoint b) { return Math.hypot(b.x-a.x, b.z-a.z); }
    }
}
