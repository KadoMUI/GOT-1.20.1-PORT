package got.npc.hiring;

import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;

/** Reloads the exact legacy hire table on server data-pack reloads. */
public final class GOTHiringReloadListener extends SimplePreparableReloadListener<Void> {
    @Override
    protected Void prepare(ResourceManager manager, ProfilerFiller profiler) {
        return null;
    }

    @Override
    protected void apply(Void ignored, ResourceManager manager, ProfilerFiller profiler) {
        GOTHiringCatalog.reload(manager);
    }
}
