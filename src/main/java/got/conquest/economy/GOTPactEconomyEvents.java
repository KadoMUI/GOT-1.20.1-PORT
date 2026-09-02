package got.conquest.economy;

import got.GOTMod;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/** Lightweight background tick for canonical Pact income and stale treasury cleanup. */
@Mod.EventBusSubscriber(modid = GOTMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class GOTPactEconomyEvents {
    private GOTPactEconomyEvents() {}

    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase != TickEvent.Phase.END || event.getServer().getTickCount() % 200 != 0) return;
        GOTPactEconomyService.processCanonicalIncome(event.getServer());
        int removed = GOTPactEconomyService.cleanupMissingPlayerPacts(event.getServer());
        if (removed > 0) {
            GOTMod.LOGGER.info("Removed {} Conquest treasury records whose player Pact no longer exists", removed);
        }
    }
}
