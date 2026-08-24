package got.calendar;

import got.calendar.network.GOTCalendarNetwork;
import got.world.GOTDimensions;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.GameRules;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/** Drives the authoritative 48,000-tick Planetos clock, transition events, and client synchronization. */
@Mod.EventBusSubscriber(modid = "got", bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class GOTCalendarEvents {
    private static final int PERIODIC_SYNC_TICKS = 100;
    private static int syncTicker;

    private GOTCalendarEvents() {}

    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;

        MinecraftServer server = event.getServer();
        GOTWorldTimeData data = GOTWorldTimeData.get(server);
        GOTCalendarApi.Snapshot before = GOTCalendarApi.snapshot(data.getWorldTime(), data.getCalendarDay());
        ServerLevel planetos = server.getLevel(GOTDimensions.PLANETOS);

        boolean advance = planetos != null
                ? planetos.getGameRules().getBoolean(GameRules.RULE_DAYLIGHT)
                : server.overworld().getGameRules().getBoolean(GameRules.RULE_DAYLIGHT);

        data.tick(advance);

        if (planetos != null) {
            // Vanilla dayTime is kept at half-speed so the celestial cycle visually matches the
            // legacy 48,000-tick Planetos day while GOTWorldTimeData remains authoritative.
            planetos.setDayTime(Math.floorDiv(data.getWorldTime(), 2L));
        }

        GOTCalendarApi.Snapshot after = GOTCalendarApi.snapshot(data.getWorldTime(), data.getCalendarDay());
        GOTCalendarTransitions.post(before, after);

        syncTicker++;
        if (syncTicker >= PERIODIC_SYNC_TICKS) {
            syncTicker = 0;
            syncAll(server, data);
        }
    }

    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;
        sync(player);
    }

    @SubscribeEvent
    public static void onPlayerChangedDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;
        sync(player);
    }

    public static void sync(ServerPlayer player) {
        GOTWorldTimeData data = GOTWorldTimeData.get(player.server);
        GOTCalendarNetwork.sync(player, data.getWorldTime(), data.getCalendarDay());
    }

    public static void syncAll(MinecraftServer server) {
        GOTWorldTimeData data = GOTWorldTimeData.get(server);
        syncAll(server, data);
    }

    private static void syncAll(MinecraftServer server, GOTWorldTimeData data) {
        for (ServerPlayer player : server.getPlayerList().getPlayers()) {
            GOTCalendarNetwork.sync(player, data.getWorldTime(), data.getCalendarDay());
        }
    }
}
