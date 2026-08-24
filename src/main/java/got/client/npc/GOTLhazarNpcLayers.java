package got.client.npc;

import got.GOTMod;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;

public final class GOTLhazarNpcLayers {
    public static final ModelLayerLocation BASE = layer("lhazar_npc", "base");
    public static final ModelLayerLocation OUTFIT = layer("lhazar_npc", "outfit");
    public static final ModelLayerLocation ARMOR_INNER = layer("lhazar_npc", "armor_inner");
    public static final ModelLayerLocation ARMOR_OUTER = layer("lhazar_npc", "armor_outer");

    private GOTLhazarNpcLayers() {}

    private static ModelLayerLocation layer(String path, String layer) {
        return new ModelLayerLocation(new ResourceLocation(GOTMod.MOD_ID, path), layer);
    }
}

