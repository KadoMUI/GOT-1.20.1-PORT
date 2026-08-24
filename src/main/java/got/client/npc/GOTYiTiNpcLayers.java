package got.client.npc;

import got.GOTMod;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;

public final class GOTYiTiNpcLayers {
    public static final ModelLayerLocation BASE = layer("yi_ti_npc", "base");
    public static final ModelLayerLocation OUTFIT = layer("yi_ti_npc", "outfit");
    public static final ModelLayerLocation ARMOR_INNER = layer("yi_ti_npc", "armor_inner");
    public static final ModelLayerLocation ARMOR_OUTER = layer("yi_ti_npc", "armor_outer");

    private GOTYiTiNpcLayers() {}

    private static ModelLayerLocation layer(String path, String layer) {
        return new ModelLayerLocation(new ResourceLocation(GOTMod.MOD_ID, path), layer);
    }
}
