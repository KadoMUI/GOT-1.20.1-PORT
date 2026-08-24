package got.world;

import got.GOTMod;
import got.common.fasttravel.GOTFastTravelManager;
import got.common.world.map.GOTWaypoint;
import got.world.terrain.PlanetosChunkGenerator;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/** Places first-time players from the Planetos world preset at Winterfell. */
@Mod.EventBusSubscriber(modid = GOTMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class GOTPlanetosSpawnEvents {
    private static final String INITIAL_SPAWN_TAG = "GOTInitialPlanetosSpawn";

    private GOTPlanetosSpawnEvents() {}

    @SubscribeEvent
    public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)
                || player.getPersistentData().getBoolean(INITIAL_SPAWN_TAG)) {
            return;
        }

        ServerLevel entryLevel = player.serverLevel();
        if (!entryLevel.dimension().equals(Level.OVERWORLD)
                || !(entryLevel.getChunkSource().getGenerator() instanceof PlanetosChunkGenerator)) {
            return;
        }

        ServerLevel planetos = player.getServer().getLevel(GOTDimensions.PLANETOS);
        if (planetos == null) {
            GOTMod.LOGGER.error("Planetos world preset could not find the got:planetos dimension");
            return;
        }

        GOTWaypoint winterfell = GOTWaypoint.WINTERFELL;
        int x = winterfell.getCoordX();
        int z = winterfell.getCoordZ();
        planetos.getChunk(x >> 4, z >> 4);
        BlockPos destination = GOTFastTravelManager.findSafeDestination(planetos, x, z);

        player.stopRiding();
        if (player.isSleeping()) player.stopSleepInBed(true, true);
        player.teleportTo(planetos,
                destination.getX() + 0.5D,
                destination.getY(),
                destination.getZ() + 0.5D,
                player.getYRot(),
                player.getXRot());
        player.setDeltaMovement(0.0D, 0.0D, 0.0D);
        player.fallDistance = 0.0F;
        player.setRespawnPosition(GOTDimensions.PLANETOS, destination, player.getYRot(), true, false);
        player.getPersistentData().putBoolean(INITIAL_SPAWN_TAG, true);
    }
}
