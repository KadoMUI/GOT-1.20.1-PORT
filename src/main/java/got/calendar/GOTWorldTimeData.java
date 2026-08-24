package got.calendar;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.saveddata.SavedData;

/**
 * Server-authoritative GOT time state.
 *
 * Legacy parity:
 * - 48,000 GOT ticks per day.
 * - Calendar day 0 is 10 July 298 AC.
 * - GOT time persists independently of vanilla level dayTime.
 */
public final class GOTWorldTimeData extends SavedData {
    public static final String DATA_NAME = "got_world_time";
    public static final long DAY_LENGTH = 48_000L;

    private static final String TAG_TOTAL_TIME = "GOTTotalTime";
    private static final String TAG_WORLD_TIME = "GOTWorldTime";
    private static final String TAG_CALENDAR_DAY = "AegonDate";

    private long totalTime;
    private long worldTime;
    private int calendarDay;

    public GOTWorldTimeData() {
        this.totalTime = 0L;
        this.worldTime = 0L;
        this.calendarDay = 0;
    }

    public static GOTWorldTimeData get(MinecraftServer server) {
        return server.overworld().getDataStorage().computeIfAbsent(
                GOTWorldTimeData::load,
                GOTWorldTimeData::new,
                DATA_NAME
        );
    }

    public static GOTWorldTimeData load(CompoundTag tag) {
        GOTWorldTimeData data = new GOTWorldTimeData();
        data.totalTime = tag.getLong(TAG_TOTAL_TIME);
        data.worldTime = tag.getLong(TAG_WORLD_TIME);
        data.calendarDay = tag.contains(TAG_CALENDAR_DAY)
                ? tag.getInt(TAG_CALENDAR_DAY)
                : Math.toIntExact(Math.floorDiv(data.worldTime, DAY_LENGTH));
        return data;
    }

    @Override
    public CompoundTag save(CompoundTag tag) {
        tag.putLong(TAG_TOTAL_TIME, totalTime);
        tag.putLong(TAG_WORLD_TIME, worldTime);
        tag.putInt(TAG_CALENDAR_DAY, calendarDay);
        return tag;
    }

    public long getTotalTime() {
        return totalTime;
    }

    public long getWorldTime() {
        return worldTime;
    }

    public int getCalendarDay() {
        return calendarDay;
    }

    public GOTAegonCalendar.Date getDate() {
        return GOTAegonCalendar.getDate(calendarDay);
    }

    public void tick(boolean advanceDaylight) {
        totalTime++;
        if (advanceDaylight) {
            setWorldTimeInternal(worldTime + 1L, true);
        } else {
            setDirty();
        }
    }

    public void addWorldTime(long ticks) {
        setWorldTimeInternal(worldTime + ticks, true);
    }

    public void setWorldTime(long ticks) {
        setWorldTimeInternal(Math.max(0L, ticks), true);
    }

    public void setCalendarDay(int day) {
        calendarDay = day;
        // Preserve time-of-day while moving to the requested date.
        long timeOfDay = Math.floorMod(worldTime, DAY_LENGTH);
        worldTime = Math.max(0L, (long) day * DAY_LENGTH + timeOfDay);
        setDirty();
    }

    public void advanceToMorning() {
        long next = worldTime + DAY_LENGTH;
        setWorldTimeInternal(next - Math.floorMod(next, DAY_LENGTH), true);
    }

    public void setTimeOfDay(long gotTickOfDay) {
        long normalized = Math.floorMod(gotTickOfDay, DAY_LENGTH);
        long dayStart = worldTime - Math.floorMod(worldTime, DAY_LENGTH);
        setWorldTimeInternal(dayStart + normalized, true);
    }

    private void setWorldTimeInternal(long newWorldTime, boolean updateCalendar) {
        long oldDay = Math.floorDiv(worldTime, DAY_LENGTH);
        worldTime = Math.max(0L, newWorldTime);
        long newDay = Math.floorDiv(worldTime, DAY_LENGTH);

        if (updateCalendar && newDay != oldDay) {
            long delta = newDay - oldDay;
            long candidate = (long) calendarDay + delta;
            calendarDay = (int) Math.max(Integer.MIN_VALUE, Math.min(Integer.MAX_VALUE, candidate));
        }
        setDirty();
    }
}
