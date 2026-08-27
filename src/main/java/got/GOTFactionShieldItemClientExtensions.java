package got;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;

public final class GOTFactionShieldItemClientExtensions implements IClientItemExtensions {
    public static final GOTFactionShieldItemClientExtensions INSTANCE = new GOTFactionShieldItemClientExtensions();
    private BlockEntityWithoutLevelRenderer renderer;

    private GOTFactionShieldItemClientExtensions() {}

    @Override
    public BlockEntityWithoutLevelRenderer getCustomRenderer() {
        if (renderer == null) {
            Minecraft mc = Minecraft.getInstance();
            renderer = new GOTFactionShieldItemRenderer(mc.getBlockEntityRenderDispatcher(), mc.getEntityModels());
        }
        return renderer;
    }
}
