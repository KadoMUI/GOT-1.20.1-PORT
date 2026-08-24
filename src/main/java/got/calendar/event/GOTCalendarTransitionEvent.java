package got.calendar.event;

import got.calendar.GOTCalendarApi;
import net.minecraftforge.eventbus.api.Event;

/** Base event for calendar transitions. Posted on MinecraftForge.EVENT_BUS. */
public abstract class GOTCalendarTransitionEvent extends Event {
    private final GOTCalendarApi.Snapshot previous;
    private final GOTCalendarApi.Snapshot current;

    protected GOTCalendarTransitionEvent(GOTCalendarApi.Snapshot previous, GOTCalendarApi.Snapshot current) {
        this.previous = previous;
        this.current = current;
    }

    public GOTCalendarApi.Snapshot previous() {
        return previous;
    }

    public GOTCalendarApi.Snapshot current() {
        return current;
    }

    public static final class NewDay extends GOTCalendarTransitionEvent {
        public NewDay(GOTCalendarApi.Snapshot previous, GOTCalendarApi.Snapshot current) { super(previous, current); }
    }

    public static final class MonthChanged extends GOTCalendarTransitionEvent {
        public MonthChanged(GOTCalendarApi.Snapshot previous, GOTCalendarApi.Snapshot current) { super(previous, current); }
    }

    public static final class YearChanged extends GOTCalendarTransitionEvent {
        public YearChanged(GOTCalendarApi.Snapshot previous, GOTCalendarApi.Snapshot current) { super(previous, current); }
    }

    public static final class SeasonChanged extends GOTCalendarTransitionEvent {
        public SeasonChanged(GOTCalendarApi.Snapshot previous, GOTCalendarApi.Snapshot current) { super(previous, current); }
    }

    public static final class Dawn extends GOTCalendarTransitionEvent {
        public Dawn(GOTCalendarApi.Snapshot previous, GOTCalendarApi.Snapshot current) { super(previous, current); }
    }

    public static final class Dusk extends GOTCalendarTransitionEvent {
        public Dusk(GOTCalendarApi.Snapshot previous, GOTCalendarApi.Snapshot current) { super(previous, current); }
    }
}
