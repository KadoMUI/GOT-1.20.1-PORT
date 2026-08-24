package got;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;

/** Lazily supplies the shared custom renderer for drink and empty-vessel items. */
public final class GOTVesselItemClientExtensions implements IClientItemExtensions {
    public static final GOTVesselItemClientExtensions INSTANCE = new GOTVesselItemClientExtensions();
    private BlockEntityWithoutLevelRenderer renderer;

    private GOTVesselItemClientExtensions() {}

    @Override
    public BlockEntityWithoutLevelRenderer getCustomRenderer() {
        if (renderer == null) {
            Minecraft minecraft = Minecraft.getInstance();
            renderer = new GOTVesselItemRenderer(
                    minecraft.getBlockEntityRenderDispatcher(),
                    minecraft.getEntityModels());
        }
        return renderer;
    }
}
