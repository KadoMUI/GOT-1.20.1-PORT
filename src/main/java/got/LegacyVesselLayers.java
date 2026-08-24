package got;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;

public final class LegacyVesselLayers {
    private LegacyVesselLayers() {}
    public static final ModelLayerLocation MUG = layer("mug");
    public static final ModelLayerLocation GOBLET = layer("goblet");
    public static final ModelLayerLocation ALE_HORN = layer("ale_horn");
    public static final ModelLayerLocation WINE_GLASS = layer("wine_glass");
    public static final ModelLayerLocation SKULL_CUP = layer("skull_cup");

    private static ModelLayerLocation layer(String name) {
        return new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(GOTMod.MOD_ID, name), "main");
    }
}
