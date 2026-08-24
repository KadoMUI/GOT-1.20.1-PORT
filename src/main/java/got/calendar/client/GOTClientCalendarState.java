package got.calendar.client;

import got.calendar.GOTCalendarApi;

/** Client-side cache. The server remains authoritative. */
public final class GOTClientCalendarState {
    private static volatile long worldTime;
    private static volatile int calendarDay;
    private static volatile boolean synchronizedOnce;

    private GOTClientCalendarState() {}

    public static void accept(long syncedWorldTime, int syncedCalendarDay) {
        worldTime = syncedWorldTime;
        calendarDay = syncedCalendarDay;
        synchronizedOnce = true;
    }

    public static boolean isSynchronized() {
        return synchronizedOnce;
    }

    public static GOTCalendarApi.Snapshot snapshot() {
        return GOTCalendarApi.snapshot(worldTime, calendarDay);
    }

    public static void clear() {
        synchronizedOnce = false;
        worldTime = 0L;
        calendarDay = 0;
    }
}
