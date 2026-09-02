package got.conquest;

import got.GOTMod;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/** Creates/normalizes the world conquest database without changing capture gameplay. */
@Mod.EventBusSubscriber(modid = GOTMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class GOTConquestEvents {
    private GOTConquestEvents() {}

    @SubscribeEvent
    public static void onServerStarted(ServerStartedEvent event) {
        GOTConquestSavedData data = GOTConquestSavedData.get(event.getServer());
        long treasuryTotal = data.allTreasuries().stream().mapToLong(treasury -> treasury.balance()).sum();
        GOTMod.LOGGER.info("Conquest foundation ready: schema={}, waypointRecords={}, canonicalPacts={}, legendaryMemberships={}, diplomaticRelations={}, treasuries={}, treasuryValue={}, revision={}",
                data.schemaVersion(), data.allWaypoints().size(), data.allCanonicalPacts().size(),
                data.allLegendaryMemberships().size(), data.allDiplomaticRelations().size(),
                data.allTreasuries().size(), treasuryTotal, data.revision());
    }
}
