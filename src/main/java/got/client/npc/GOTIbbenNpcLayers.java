package got.client.npc;

import got.GOTMod;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;

public final class GOTIbbenNpcLayers {
    public static final ModelLayerLocation BASE = layer("ibben_npc", "base");
    public static final ModelLayerLocation OUTFIT = layer("ibben_npc", "outfit");
    public static final ModelLayerLocation ARMOR_INNER = layer("ibben_npc", "armor_inner");
    public static final ModelLayerLocation ARMOR_OUTER = layer("ibben_npc", "armor_outer");

    private GOTIbbenNpcLayers() {}

    private static ModelLayerLocation layer(String path, String layer) {
        return new ModelLayerLocation(new ResourceLocation(GOTMod.MOD_ID, path), layer);
    }
}
