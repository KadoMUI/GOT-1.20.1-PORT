package got.calendar.season;

import got.calendar.GOTCalendarApi;
import got.calendar.GOTAegonCalendar;

/**
 * Small dependency-free bridge for future Homeostatic/environment integration.
 * It exposes seasonal climate biases but does not alter temperature or player needs yet.
 */
public final class GOTClimateCalendarHooks {
    private GOTClimateCalendarHooks() {}

    public static float seasonalTemperatureBias(GOTCalendarApi.Snapshot snapshot) {
        return seasonalTemperatureBias(snapshot.season());
    }

    public static float seasonalTemperatureBias(GOTAegonCalendar.Season season) {
        return switch (season) {
            case SPRING -> 0.05F;
            case SUMMER -> 0.15F;
            case AUTUMN -> -0.05F;
            case WINTER -> -0.20F;
        };
    }

    public static boolean isWinter(GOTCalendarApi.Snapshot snapshot) {
        return snapshot.season() == GOTAegonCalendar.Season.WINTER;
    }
}
