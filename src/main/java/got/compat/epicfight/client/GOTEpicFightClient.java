package got.compat.epicfight.client;

import got.GOTMod;
import got.compat.epicfight.GOTEpicFightCompat;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import yesman.epicfight.api.client.forgeevent.PatchedRenderersEvent;

/** Client-only half of the optional Epic Fight integration. */
public final class GOTEpicFightClient {
    private static boolean bootstrapped;

    private GOTEpicFightClient() {}

    public static void bootstrap(IEventBus modBus) {
        if (bootstrapped) return;
        bootstrapped = true;
        modBus.addListener(GOTEpicFightClient::registerPatchedRenderers);
    }

    private static void registerPatchedRenderers(PatchedRenderersEvent.Add event) {
        try {
            for (EntityType<?> entityType : GOTEpicFightCompat.targetEntityTypes()) {
                event.addPatchedEntityRenderer(
                        entityType,
                        type -> new GOTHumanoidPatchedRenderer(event.getContext(), type)
                );
            }
        } catch (RuntimeException | LinkageError exception) {
            // Client rendering compatibility is optional; never break base GOT startup.
            GOTMod.LOGGER.error("Failed to register GOT Epic Fight patched renderers; continuing with vanilla GOT renderers",
                    exception);
        }
    }
}
