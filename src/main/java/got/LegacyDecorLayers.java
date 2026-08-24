package got;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;

/** Model-layer identities for the original 1.7.10 decorative Java models. */
public final class LegacyDecorLayers {
    public static final ModelLayerLocation BEACON = layer("beacon");
    public static final ModelLayerLocation UNSMELTERY = layer("unsmeltery");
    public static final ModelLayerLocation BEAR_RUG = layer("bear_rug");
    public static final ModelLayerLocation GIRAFFE_RUG = layer("giraffe_rug");
    public static final ModelLayerLocation LION_RUG = layer("lion_rug");

    private static ModelLayerLocation layer(String name) {
        return new ModelLayerLocation(
                ResourceLocation.fromNamespaceAndPath(GOTMod.MOD_ID, "legacy_" + name), "main");
    }

    private LegacyDecorLayers() {}
}
