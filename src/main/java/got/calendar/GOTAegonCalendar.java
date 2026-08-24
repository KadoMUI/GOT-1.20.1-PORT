package got.calendar;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Faithful modern reconstruction of the original 1.7.10 GOTDate.AegonCalendar.
 * Day index 0 = 10 July 298 AC.
 */
public final class GOTAegonCalendar {
    public static final Date START_DATE = new Date(298, Month.JULY, 10);

    private static final Map<Integer, Date> CACHE = new ConcurrentHashMap<>();

    private GOTAegonCalendar() {}

    public static Date getDate(int dayIndex) {
        return CACHE.computeIfAbsent(dayIndex, GOTAegonCalendar::calculateDate);
    }

    public static Date getDate(GOTWorldTimeData data) {
        return getDate(data.getCalendarDay());
    }

    public static Season getSeason(int dayIndex) {
        return getDate(dayIndex).month().season();
    }

    private static Date calculateDate(int dayIndex) {
        Date date = START_DATE;
        if (dayIndex < 0) {
            for (int i = 0; i < -dayIndex; i++) {
                date = date.decrement();
            }
        } else {
            for (int i = 0; i < dayIndex; i++) {
                date = date.increment();
            }
        }
        return date;
    }

    /**
     * Matches the legacy mod's leap-year rule exactly: divisible by 4 but not 100.
     * The original did not include Gregorian's divisible-by-400 exception.
     */
    public static boolean isLeapYear(int year) {
        return year % 4 == 0 && year % 100 != 0;
    }

    public enum Season {
        SPRING,
        SUMMER,
        AUTUMN,
        WINTER
    }

    public enum Day {
        SUNDAY("Sunday"),
        MONDAY("Monday"),
        TUESDAY("Tuesday"),
        WEDNESDAY("Wednesday"),
        THURSDAY("Thursday"),
        FRIDAY("Friday"),
        SATURDAY("Saturday");

        private final String displayName;

        Day(String displayName) {
            this.displayName = displayName;
        }

        public String displayName() {
            return displayName;
        }
    }

    public enum Month {
        JANUARY("January", 31, Season.WINTER),
        FEBRUARY("February", 28, Season.WINTER),
        MARCH("March", 31, Season.SPRING),
        APRIL("April", 30, Season.SPRING),
        MAY("May", 31, Season.SPRING),
        JUNE("June", 30, Season.SUMMER),
        JULY("July", 31, Season.SUMMER),
        AUGUST("August", 31, Season.SUMMER),
        SEPTEMBER("September", 30, Season.AUTUMN),
        OCTOBER("October", 31, Season.AUTUMN),
        NOVEMBER("November", 30, Season.AUTUMN),
        DECEMBER("December", 31, Season.WINTER);

        private final String displayName;
        private final int days;
        private final Season season;

        Month(String displayName, int days, Season season) {
            this.displayName = displayName;
            this.days = days;
            this.season = season;
        }

        public String displayName() {
            return displayName;
        }

        public int baseDays() {
            return days;
        }

        public int daysInYear(int year) {
            return this == FEBRUARY && GOTAegonCalendar.isLeapYear(year) ? 29 : days;
        }

        public Season season() {
            return season;
        }
    }

    public record Date(int year, Month month, int dayOfMonth) {
        public Date {
            if (month == null) throw new IllegalArgumentException("month");
            if (dayOfMonth < 1 || dayOfMonth > month.daysInYear(year)) {
                throw new IllegalArgumentException("Invalid day " + dayOfMonth + " for " + month + " " + year);
            }
        }

        public Day weekday() {
            int dayOfYear = dayOfMonth;
            for (Month value : Month.values()) {
                if (value == month) break;
                dayOfYear += value.daysInYear(year);
            }
            // This mirrors the legacy code: weekday is calculated from day-of-year only.
            return Day.values()[Math.floorMod(dayOfYear - 1, Day.values().length)];
        }

        public Season season() {
            return month.season();
        }

        public int dayOfYear() {
            int value = dayOfMonth;
            for (Month candidate : Month.values()) {
                if (candidate == month) break;
                value += candidate.daysInYear(year);
            }
            return value;
        }

        public String dayName() {
            return weekday().displayName() + " " + dayOfMonth + " " + month.displayName();
        }

        public String yearName(boolean longForm) {
            return year + (longForm ? " After Conquest" : " AC");
        }

        public String displayName(boolean longForm) {
            return dayName() + ", " + yearName(longForm);
        }

        private Date increment() {
            int nextDay = dayOfMonth + 1;
            int nextYear = year;
            Month nextMonth = month;
            if (nextDay > month.daysInYear(year)) {
                nextDay = 1;
                int ordinal = month.ordinal() + 1;
                if (ordinal >= Month.values().length) {
                    ordinal = 0;
                    nextYear++;
                }
                nextMonth = Month.values()[ordinal];
            }
            return new Date(nextYear, nextMonth, nextDay);
        }

        private Date decrement() {
            int previousDay = dayOfMonth - 1;
            int previousYear = year;
            Month previousMonth = month;
            if (previousDay < 1) {
                int ordinal = month.ordinal() - 1;
                if (ordinal < 0) {
                    ordinal = Month.values().length - 1;
                    previousYear--;
                }
                previousMonth = Month.values()[ordinal];
                previousDay = previousMonth.daysInYear(previousYear);
            }
            return new Date(previousYear, previousMonth, previousDay);
        }
    }
}
