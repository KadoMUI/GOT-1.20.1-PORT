package got.client.npc;

import got.GOTMod;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;

public final class GOTLysNpcLayers {
    public static final ModelLayerLocation BASE = layer("lys_npc", "base");
    public static final ModelLayerLocation OUTFIT = layer("lys_npc", "outfit");
    public static final ModelLayerLocation ARMOR_INNER = layer("lys_npc", "armor_inner");
    public static final ModelLayerLocation ARMOR_OUTER = layer("lys_npc", "armor_outer");

    private GOTLysNpcLayers() {}

    private static ModelLayerLocation layer(String path, String layer) {
        return new ModelLayerLocation(new ResourceLocation(GOTMod.MOD_ID, path), layer);
    }
}
