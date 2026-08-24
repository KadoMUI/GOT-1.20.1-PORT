package got.calendar;

import got.calendar.event.GOTCalendarTransitionEvent;
import net.minecraftforge.common.MinecraftForge;

/** Shared transition dispatcher used by normal ticking and explicit time jumps such as sleeping. */
public final class GOTCalendarTransitions {
    private GOTCalendarTransitions() {}

    public static void post(GOTCalendarApi.Snapshot before, GOTCalendarApi.Snapshot after) {
        if (before.calendarDay() != after.calendarDay()) {
            MinecraftForge.EVENT_BUS.post(new GOTCalendarTransitionEvent.NewDay(before, after));
        }
        if (before.date().month() != after.date().month()) {
            MinecraftForge.EVENT_BUS.post(new GOTCalendarTransitionEvent.MonthChanged(before, after));
        }
        if (before.date().year() != after.date().year()) {
            MinecraftForge.EVENT_BUS.post(new GOTCalendarTransitionEvent.YearChanged(before, after));
        }
        if (before.season() != after.season()) {
            MinecraftForge.EVENT_BUS.post(new GOTCalendarTransitionEvent.SeasonChanged(before, after));
        }
        if (before.timeOfDay() != after.timeOfDay()) {
            if (after.timeOfDay() == GOTTimeOfDay.DAWN) {
                MinecraftForge.EVENT_BUS.post(new GOTCalendarTransitionEvent.Dawn(before, after));
            } else if (after.timeOfDay() == GOTTimeOfDay.DUSK) {
                MinecraftForge.EVENT_BUS.post(new GOTCalendarTransitionEvent.Dusk(before, after));
            }
        }
    }
}
