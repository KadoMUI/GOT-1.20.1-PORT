package got.world.biome;

/** Original 1.7.10 seasonal climate categories. */
public enum GOTClimateType {
    WINTER(false),
    COLD(false),
    COLD_AZ(true),
    SUMMER(false),
    SUMMER_AZ(true),
    NORMAL(false),
    NORMAL_AZ(true);

    private final boolean altitudeZone;

    GOTClimateType(boolean altitudeZone) { this.altitudeZone = altitudeZone; }
    public boolean isAltitudeZone() { return altitudeZone; }
}
