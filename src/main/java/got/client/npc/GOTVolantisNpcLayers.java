package got.client.npc;

import got.GOTMod;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;

public final class GOTVolantisNpcLayers {
    public static final ModelLayerLocation BASE = layer("volantis_npc", "base");
    public static final ModelLayerLocation OUTFIT = layer("volantis_npc", "outfit");
    public static final ModelLayerLocation ARMOR_INNER = layer("volantis_npc", "armor_inner");
    public static final ModelLayerLocation ARMOR_OUTER = layer("volantis_npc", "armor_outer");

    private GOTVolantisNpcLayers() {}

    private static ModelLayerLocation layer(String path, String layer) {
        return new ModelLayerLocation(new ResourceLocation(GOTMod.MOD_ID, path), layer);
    }
}
