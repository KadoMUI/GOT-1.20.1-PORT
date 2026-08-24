package got.client.npc;

import got.GOTMod;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;

public final class GOTArrynNpcLayers {
    public static final ModelLayerLocation BASE = layer("arryn_npc", "base");
    public static final ModelLayerLocation OUTFIT = layer("arryn_npc", "outfit");
    public static final ModelLayerLocation ARMOR_INNER = layer("arryn_npc", "armor_inner");
    public static final ModelLayerLocation ARMOR_OUTER = layer("arryn_npc", "armor_outer");

    private GOTArrynNpcLayers() {}

    private static ModelLayerLocation layer(String path, String layer) {
        return new ModelLayerLocation(new ResourceLocation(GOTMod.MOD_ID, path), layer);
    }
}
