package got.client.npc;

import got.GOTMod;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;

public final class GOTLorathNpcLayers {
    public static final ModelLayerLocation BASE = layer("lorath_npc", "base");
    public static final ModelLayerLocation OUTFIT = layer("lorath_npc", "outfit");
    public static final ModelLayerLocation ARMOR_INNER = layer("lorath_npc", "armor_inner");
    public static final ModelLayerLocation ARMOR_OUTER = layer("lorath_npc", "armor_outer");

    private GOTLorathNpcLayers() {}

    private static ModelLayerLocation layer(String path, String layer) {
        return new ModelLayerLocation(new ResourceLocation(GOTMod.MOD_ID, path), layer);
    }
}

