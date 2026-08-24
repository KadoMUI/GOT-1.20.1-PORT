package got.client.npc;

import got.GOTMod;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;

public final class GOTReachNpcLayers {
    public static final ModelLayerLocation BASE = layer("reach_npc", "base");
    public static final ModelLayerLocation OUTFIT = layer("reach_npc", "outfit");
    public static final ModelLayerLocation ARMOR_INNER = layer("reach_npc", "armor_inner");
    public static final ModelLayerLocation ARMOR_OUTER = layer("reach_npc", "armor_outer");

    private GOTReachNpcLayers() {}

    private static ModelLayerLocation layer(String path, String layer) {
        return new ModelLayerLocation(new ResourceLocation(GOTMod.MOD_ID, path), layer);
    }
}
