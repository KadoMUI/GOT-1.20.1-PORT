package got.common.world.map;

/** Exact source-level reconstruction of the original 1.7.10 coordinate helpers. */
public final class GOTCoordConverter {
    private GOTCoordConverter() {}

    public static double toEssosTown(double coordinate) {
        return toSettlement(coordinate, 46.0D);
    }

    public static double toEssosTownGate(double coordinate, boolean positiveSide) {
        return coordinate + 0.265625D * (positiveSide ? 1.0D : -1.0D);
    }

    private static double toSettlement(double coordinate, double offset) {
        if (coordinate > 0.0D) return coordinate - offset / 128.0D;
        if (coordinate < 0.0D) return coordinate + offset / 128.0D;
        return coordinate;
    }

    public static double toWesterosCastle(double coordinate) {
        return toSettlement(coordinate, 39.0D);
    }

    public static double toWesterosTown(double coordinate) {
        return toSettlement(coordinate, 84.0D);
    }

    public static double toYiTiTown(double coordinate) {
        return toSettlement(coordinate, 94.0D);
    }
}
