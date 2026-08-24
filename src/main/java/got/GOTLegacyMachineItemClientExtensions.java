package got;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;

/** Lazily supplies the original Beacon/Unsmeltery inventory renderer. */
public final class GOTLegacyMachineItemClientExtensions implements IClientItemExtensions {
    public static final GOTLegacyMachineItemClientExtensions INSTANCE = new GOTLegacyMachineItemClientExtensions();
    private BlockEntityWithoutLevelRenderer renderer;

    private GOTLegacyMachineItemClientExtensions() {}

    @Override
    public BlockEntityWithoutLevelRenderer getCustomRenderer() {
        if (renderer == null) {
            Minecraft minecraft = Minecraft.getInstance();
            renderer = new GOTLegacyMachineItemRenderer(
                    minecraft.getBlockEntityRenderDispatcher(), minecraft.getEntityModels());
        }
        return renderer;
    }
}
