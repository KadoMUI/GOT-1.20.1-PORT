package got.client.npc;

import got.GOTMod;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;

public final class GOTRiverlandsNpcLayers {
    public static final ModelLayerLocation BASE = layer("riverlands_npc", "base");
    public static final ModelLayerLocation OUTFIT = layer("riverlands_npc", "outfit");
    public static final ModelLayerLocation ARMOR_INNER = layer("riverlands_npc", "armor_inner");
    public static final ModelLayerLocation ARMOR_OUTER = layer("riverlands_npc", "armor_outer");

    private GOTRiverlandsNpcLayers() {}

    private static ModelLayerLocation layer(String path, String layer) {
        return new ModelLayerLocation(new ResourceLocation(GOTMod.MOD_ID, path), layer);
    }
}
