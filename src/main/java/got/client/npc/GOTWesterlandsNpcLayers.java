package got.client.npc;

import got.GOTMod;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;

public final class GOTWesterlandsNpcLayers {
    public static final ModelLayerLocation BASE = layer("westerlands_npc", "base");
    public static final ModelLayerLocation OUTFIT = layer("westerlands_npc", "outfit");
    public static final ModelLayerLocation ARMOR_INNER = layer("westerlands_npc", "armor_inner");
    public static final ModelLayerLocation ARMOR_OUTER = layer("westerlands_npc", "armor_outer");

    private GOTWesterlandsNpcLayers() {}

    private static ModelLayerLocation layer(String path, String layer) {
        return new ModelLayerLocation(new ResourceLocation(GOTMod.MOD_ID, path), layer);
    }
}
