package got.client.npc;

import got.GOTMod;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;

public final class GOTDothrakiNpcLayers {
    public static final ModelLayerLocation BASE = layer("dothraki_npc", "base");
    public static final ModelLayerLocation OUTFIT = layer("dothraki_npc", "outfit");
    public static final ModelLayerLocation ARMOR_INNER = layer("dothraki_npc", "armor_inner");
    public static final ModelLayerLocation ARMOR_OUTER = layer("dothraki_npc", "armor_outer");

    private GOTDothrakiNpcLayers() {}

    private static ModelLayerLocation layer(String path, String layer) {
        return new ModelLayerLocation(new ResourceLocation(GOTMod.MOD_ID, path), layer);
    }
}
