package got.client.npc;

import got.GOTMod;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;

public final class GOTAsshaiNpcLayers {
    public static final ModelLayerLocation BASE = layer("asshai_npc", "base");
    public static final ModelLayerLocation OUTFIT = layer("asshai_npc", "outfit");
    public static final ModelLayerLocation ARMOR_INNER = layer("asshai_npc", "armor_inner");
    public static final ModelLayerLocation ARMOR_OUTER = layer("asshai_npc", "armor_outer");

    private GOTAsshaiNpcLayers() {}

    private static ModelLayerLocation layer(String path, String layer) {
        return new ModelLayerLocation(new ResourceLocation(GOTMod.MOD_ID, path), layer);
    }
}
