package got.client.npc;

import got.GOTMod;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;

public final class GOTNorthNpcLayers {
    public static final ModelLayerLocation BASE = layer("north_npc", "base");
    public static final ModelLayerLocation OUTFIT = layer("north_npc", "outfit");
    public static final ModelLayerLocation ARMOR_INNER = layer("north_npc", "armor_inner");
    public static final ModelLayerLocation ARMOR_OUTER = layer("north_npc", "armor_outer");

    private GOTNorthNpcLayers() {}

    private static ModelLayerLocation layer(String path, String layer) {
        return new ModelLayerLocation(new ResourceLocation(GOTMod.MOD_ID, path), layer);
    }
}
