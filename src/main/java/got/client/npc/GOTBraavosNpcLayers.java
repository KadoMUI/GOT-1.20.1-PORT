package got.client.npc;

import got.GOTMod;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;

public final class GOTBraavosNpcLayers {
    public static final ModelLayerLocation BASE = layer("braavos_npc", "base");
    public static final ModelLayerLocation OUTFIT = layer("braavos_npc", "outfit");
    public static final ModelLayerLocation ARMOR_INNER = layer("braavos_npc", "armor_inner");
    public static final ModelLayerLocation ARMOR_OUTER = layer("braavos_npc", "armor_outer");

    private GOTBraavosNpcLayers() {}

    private static ModelLayerLocation layer(String path, String layer) {
        return new ModelLayerLocation(new ResourceLocation(GOTMod.MOD_ID, path), layer);
    }
}
