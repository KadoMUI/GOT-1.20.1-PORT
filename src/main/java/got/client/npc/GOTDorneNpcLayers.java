package got.client.npc;

import got.GOTMod;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;

public final class GOTDorneNpcLayers {
    public static final ModelLayerLocation BASE = layer("dorne_npc", "base");
    public static final ModelLayerLocation OUTFIT = layer("dorne_npc", "outfit");
    public static final ModelLayerLocation ARMOR_INNER = layer("dorne_npc", "armor_inner");
    public static final ModelLayerLocation ARMOR_OUTER = layer("dorne_npc", "armor_outer");

    private GOTDorneNpcLayers() {}

    private static ModelLayerLocation layer(String path, String layer) {
        return new ModelLayerLocation(new ResourceLocation(GOTMod.MOD_ID, path), layer);
    }
}
