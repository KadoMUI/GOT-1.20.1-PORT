package got.pact;

import got.network.GOTPactNetworkSync;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/** Keeps Pact snapshots fresh when players join or change dimensions. */
@Mod.EventBusSubscriber(modid = "got")
public final class GOTPactNetworkEvents {
    private GOTPactNetworkEvents() {}

    @SubscribeEvent public static void login(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) GOTPactNetworkSync.sync(player);
    }

    @SubscribeEvent public static void dimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) GOTPactNetworkSync.sync(player);
    }
}
