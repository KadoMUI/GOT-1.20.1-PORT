package got.client.npc;

import got.GOTMod;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;

public final class GOTPentosNpcLayers {
    public static final ModelLayerLocation BASE = layer("pentos_npc", "base");
    public static final ModelLayerLocation OUTFIT = layer("pentos_npc", "outfit");
    public static final ModelLayerLocation ARMOR_INNER = layer("pentos_npc", "armor_inner");
    public static final ModelLayerLocation ARMOR_OUTER = layer("pentos_npc", "armor_outer");

    private GOTPentosNpcLayers() {}

    private static ModelLayerLocation layer(String path, String layer) {
        return new ModelLayerLocation(new ResourceLocation(GOTMod.MOD_ID, path), layer);
    }
}
