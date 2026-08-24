package got.calendar;

import net.minecraft.server.MinecraftServer;

/**
 * Public read-only facade for Project Thrones world time.
 * Future systems should query this class rather than duplicate calendar math.
 */
public final class GOTCalendarApi {
    private GOTCalendarApi() {}

    public static Snapshot snapshot(MinecraftServer server) {
        GOTWorldTimeData data = GOTWorldTimeData.get(server);
        return snapshot(data.getWorldTime(), data.getCalendarDay());
    }

    public static Snapshot snapshot(long worldTime, int calendarDay) {
        GOTAegonCalendar.Date date = GOTAegonCalendar.getDate(calendarDay);
        long tickOfDay = Math.floorMod(worldTime, GOTWorldTimeData.DAY_LENGTH);
        return new Snapshot(
                worldTime,
                calendarDay,
                tickOfDay,
                date,
                date.dayOfYear(),
                date.season(),
                GOTTimeOfDay.fromTick(tickOfDay),
                GOTMoonPhase.forDay(calendarDay)
        );
    }

    public static String formatDate(GOTAegonCalendar.Date date) {
        return date.dayOfMonth() + " " + date.month().displayName() + ", " + date.year() + " AC";
    }

    public static String formatLongDate(GOTAegonCalendar.Date date) {
        return date.weekday().displayName() + ", " + date.dayOfMonth() + " "
                + date.month().displayName() + ", " + date.year() + " After Conquest";
    }

    public record Snapshot(
            long worldTime,
            int calendarDay,
            long tickOfDay,
            GOTAegonCalendar.Date date,
            int dayOfYear,
            GOTAegonCalendar.Season season,
            GOTTimeOfDay timeOfDay,
            GOTMoonPhase moonPhase
    ) {
        public String displayDate() {
            return GOTCalendarApi.formatDate(date);
        }

        public String displayLongDate() {
            return GOTCalendarApi.formatLongDate(date);
        }
    }
}
