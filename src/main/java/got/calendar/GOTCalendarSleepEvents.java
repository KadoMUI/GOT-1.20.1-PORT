package got.calendar;

import got.world.GOTDimensions;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.event.level.SleepFinishedTimeEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/** Makes sleeping in Planetos advance the authoritative GOT clock instead of only vanilla dayTime. */
@Mod.EventBusSubscriber(modid = "got", bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class GOTCalendarSleepEvents {
    private GOTCalendarSleepEvents() {}

    @SubscribeEvent
    public static void onSleepFinished(SleepFinishedTimeEvent event) {
        if (!(event.getLevel() instanceof ServerLevel level)) return;
        if (!level.dimension().equals(GOTDimensions.PLANETOS)) return;

        MinecraftServer server = level.getServer();
        GOTWorldTimeData data = GOTWorldTimeData.get(server);
        GOTCalendarApi.Snapshot before = GOTCalendarApi.snapshot(data.getWorldTime(), data.getCalendarDay());

        // Legacy-style behavior: a successful night sleep wakes the world at the start of the
        // next Planetos morning. The data object preserves calendar rollover for the skipped night.
        data.advanceToMorning();

        GOTCalendarApi.Snapshot after = GOTCalendarApi.snapshot(data.getWorldTime(), data.getCalendarDay());
        GOTCalendarTransitions.post(before, after);
        GOTCalendarEvents.syncAll(server);

        // Keep the immediate vanilla wake-up target aligned with the half-speed visual clock.
        // The normal server tick continues enforcing this value afterward.
        event.setTimeAddition(Math.floorDiv(data.getWorldTime(), 2L));
        level.setDayTime(Math.floorDiv(data.getWorldTime(), 2L));
    }
}
