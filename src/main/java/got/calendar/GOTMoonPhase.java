package got.calendar;

/**
 * Eight-phase deterministic lunar cycle exposed for gameplay hooks.
 * This is intentionally data-driven by calendar day and does not alter legacy date math.
 */
public enum GOTMoonPhase {
    FULL_MOON("Full Moon"),
    WANING_GIBBOUS("Waning Gibbous"),
    LAST_QUARTER("Last Quarter"),
    WANING_CRESCENT("Waning Crescent"),
    NEW_MOON("New Moon"),
    WAXING_CRESCENT("Waxing Crescent"),
    FIRST_QUARTER("First Quarter"),
    WAXING_GIBBOUS("Waxing Gibbous");

    private static final int DAYS_PER_PHASE = 4;
    private final String displayName;

    GOTMoonPhase(String displayName) {
        this.displayName = displayName;
    }

    public String displayName() {
        return displayName;
    }

    public static GOTMoonPhase forDay(int calendarDay) {
        int phase = Math.floorMod(Math.floorDiv(calendarDay, DAYS_PER_PHASE), values().length);
        return values()[phase];
    }
}
