package got.client.npc;

import got.GOTMod;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;

public final class GOTQohorNpcLayers {
    public static final ModelLayerLocation BASE = layer("qohor_npc", "base");
    public static final ModelLayerLocation OUTFIT = layer("qohor_npc", "outfit");
    public static final ModelLayerLocation ARMOR_INNER = layer("qohor_npc", "armor_inner");
    public static final ModelLayerLocation ARMOR_OUTER = layer("qohor_npc", "armor_outer");

    private GOTQohorNpcLayers() {}

    private static ModelLayerLocation layer(String path, String layer) {
        return new ModelLayerLocation(new ResourceLocation(GOTMod.MOD_ID, path), layer);
    }
}

