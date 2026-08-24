package got.client.npc;

import got.GOTMod;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;

public final class GOTCrownlandsNpcLayers {
    public static final ModelLayerLocation BASE = layer("crownlands_npc", "base");
    public static final ModelLayerLocation OUTFIT = layer("crownlands_npc", "outfit");
    public static final ModelLayerLocation ARMOR_INNER = layer("crownlands_npc", "armor_inner");
    public static final ModelLayerLocation ARMOR_OUTER = layer("crownlands_npc", "armor_outer");

    private GOTCrownlandsNpcLayers() {}

    private static ModelLayerLocation layer(String path, String layer) {
        return new ModelLayerLocation(new ResourceLocation(GOTMod.MOD_ID, path), layer);
    }
}
