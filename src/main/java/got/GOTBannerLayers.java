package got;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;

/** Model-layer identities for the original freestanding and wall banners. */
public final class GOTBannerLayers {
    public static final ModelLayerLocation STANDING = layer("standing_banner");
    public static final ModelLayerLocation WALL = layer("wall_banner");

    private static ModelLayerLocation layer(String name) {
        return new ModelLayerLocation(
                ResourceLocation.fromNamespaceAndPath(GOTMod.MOD_ID, name), "main");
    }

    private GOTBannerLayers() {}
}
