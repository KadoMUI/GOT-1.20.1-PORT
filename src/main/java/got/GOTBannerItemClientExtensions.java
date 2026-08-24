package got;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;

/** Lazily supplies the shared original-geometry renderer for banner stacks. */
public final class GOTBannerItemClientExtensions implements IClientItemExtensions {
    public static final GOTBannerItemClientExtensions INSTANCE = new GOTBannerItemClientExtensions();
    private BlockEntityWithoutLevelRenderer renderer;

    private GOTBannerItemClientExtensions() {}

    @Override
    public BlockEntityWithoutLevelRenderer getCustomRenderer() {
        if (renderer == null) {
            Minecraft minecraft = Minecraft.getInstance();
            renderer = new GOTBannerItemRenderer(
                    minecraft.getBlockEntityRenderDispatcher(), minecraft.getEntityModels());
        }
        return renderer;
    }
}
