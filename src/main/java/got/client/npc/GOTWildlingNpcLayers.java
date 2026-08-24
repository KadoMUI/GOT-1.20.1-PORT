package got.client.npc;

import got.GOTMod;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;

public final class GOTWildlingNpcLayers {
    public static final ModelLayerLocation BASE = layer("wildling_npc", "base");
    public static final ModelLayerLocation OUTFIT = layer("wildling_npc", "outfit");
    public static final ModelLayerLocation ARMOR_INNER = layer("wildling_npc", "armor_inner");
    public static final ModelLayerLocation ARMOR_OUTER = layer("wildling_npc", "armor_outer");

    private GOTWildlingNpcLayers() {}

    private static ModelLayerLocation layer(String path, String layer) {
        return new ModelLayerLocation(new ResourceLocation(GOTMod.MOD_ID, path), layer);
    }
}
