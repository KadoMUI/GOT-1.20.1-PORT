package got.calendar.npc;

import got.calendar.GOTCalendarApi;
import got.calendar.GOTTimeOfDay;

/**
 * Central calendar-to-NPC schedule mapping.
 *
 * Pass 3 deliberately keeps this independent of individual NPC classes. Existing and future
 * NPC goals can query this service without duplicating time math. Soldiers/guards may opt into
 * the night-watch schedule while ordinary civilians use the civilian schedule.
 */
public final class GOTNpcScheduleService {
    private GOTNpcScheduleService() {}

    public static GOTNpcSchedule civilian(GOTCalendarApi.Snapshot snapshot) {
        return switch (snapshot.timeOfDay()) {
            case DAWN -> GOTNpcSchedule.WAKE;
            case MORNING, AFTERNOON -> GOTNpcSchedule.WORK;
            case DUSK -> GOTNpcSchedule.EVENING;
            case NIGHT -> GOTNpcSchedule.SLEEP;
        };
    }

    public static GOTNpcSchedule guard(GOTCalendarApi.Snapshot snapshot, boolean assignedNightWatch) {
        if (assignedNightWatch && snapshot.timeOfDay() == GOTTimeOfDay.NIGHT) {
            return GOTNpcSchedule.NIGHT_WATCH;
        }
        return switch (snapshot.timeOfDay()) {
            case DAWN -> GOTNpcSchedule.WAKE;
            case MORNING, AFTERNOON, DUSK -> GOTNpcSchedule.WORK;
            case NIGHT -> GOTNpcSchedule.SLEEP;
        };
    }

    public static boolean shouldSleep(GOTCalendarApi.Snapshot snapshot) {
        return civilian(snapshot) == GOTNpcSchedule.SLEEP;
    }

    public static boolean shouldWork(GOTCalendarApi.Snapshot snapshot) {
        GOTNpcSchedule schedule = civilian(snapshot);
        return schedule == GOTNpcSchedule.WORK || schedule == GOTNpcSchedule.WAKE;
    }
}
