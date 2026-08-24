package got.client.npc;

import got.GOTMod;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;

public final class GOTWhiteWalkerNpcLayers {
    public static final ModelLayerLocation BASE = layer("white_walker_npc", "base");
    public static final ModelLayerLocation OUTFIT = layer("white_walker_npc", "outfit");
    public static final ModelLayerLocation ARMOR_INNER = layer("white_walker_npc", "armor_inner");
    public static final ModelLayerLocation ARMOR_OUTER = layer("white_walker_npc", "armor_outer");

    private GOTWhiteWalkerNpcLayers() {}

    private static ModelLayerLocation layer(String path, String layer) {
        return new ModelLayerLocation(new ResourceLocation(GOTMod.MOD_ID, path), layer);
    }
}
