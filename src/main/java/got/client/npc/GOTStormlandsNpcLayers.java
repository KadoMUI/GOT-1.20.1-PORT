package got.client.npc;

import got.GOTMod;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;

public final class GOTStormlandsNpcLayers {
    public static final ModelLayerLocation BASE = layer("stormlands_npc", "base");
    public static final ModelLayerLocation OUTFIT = layer("stormlands_npc", "outfit");
    public static final ModelLayerLocation ARMOR_INNER = layer("stormlands_npc", "armor_inner");
    public static final ModelLayerLocation ARMOR_OUTER = layer("stormlands_npc", "armor_outer");

    private GOTStormlandsNpcLayers() {}

    private static ModelLayerLocation layer(String path, String layer) {
        return new ModelLayerLocation(new ResourceLocation(GOTMod.MOD_ID, path), layer);
    }
}
