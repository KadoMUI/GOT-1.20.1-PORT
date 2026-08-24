package got.calendar;

/** Coarse gameplay phases based on the 48,000-tick Planetos day. */
public enum GOTTimeOfDay {
    DAWN("Dawn"),
    MORNING("Morning"),
    AFTERNOON("Afternoon"),
    DUSK("Dusk"),
    NIGHT("Night");

    private final String displayName;

    GOTTimeOfDay(String displayName) {
        this.displayName = displayName;
    }

    public String displayName() {
        return displayName;
    }

    public static GOTTimeOfDay fromTick(long tickOfDay) {
        long t = Math.floorMod(tickOfDay, GOTWorldTimeData.DAY_LENGTH);
        // Because Planetos celestial time mirrors GOT time / 2, these boundaries
        // correspond to familiar vanilla sky positions while preserving a 48k day.
        if (t < 2_000L) return DAWN;
        if (t < 12_000L) return MORNING;
        if (t < 24_000L) return AFTERNOON;
        if (t < 26_000L) return DUSK;
        return NIGHT;
    }
}
