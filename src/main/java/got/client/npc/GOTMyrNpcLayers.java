package got.client.npc;

import got.GOTMod;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;

public final class GOTMyrNpcLayers {
    public static final ModelLayerLocation BASE = layer("myr_npc", "base");
    public static final ModelLayerLocation OUTFIT = layer("myr_npc", "outfit");
    public static final ModelLayerLocation ARMOR_INNER = layer("myr_npc", "armor_inner");
    public static final ModelLayerLocation ARMOR_OUTER = layer("myr_npc", "armor_outer");

    private GOTMyrNpcLayers() {}

    private static ModelLayerLocation layer(String path, String layer) {
        return new ModelLayerLocation(new ResourceLocation(GOTMod.MOD_ID, path), layer);
    }
}
