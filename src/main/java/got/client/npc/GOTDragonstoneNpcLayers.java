package got.client.npc;

import got.GOTMod;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;

public final class GOTDragonstoneNpcLayers {
    public static final ModelLayerLocation BASE = layer("dragonstone_npc", "base");
    public static final ModelLayerLocation OUTFIT = layer("dragonstone_npc", "outfit");
    public static final ModelLayerLocation ARMOR_INNER = layer("dragonstone_npc", "armor_inner");
    public static final ModelLayerLocation ARMOR_OUTER = layer("dragonstone_npc", "armor_outer");

    private GOTDragonstoneNpcLayers() {}

    private static ModelLayerLocation layer(String path, String layer) {
        return new ModelLayerLocation(new ResourceLocation(GOTMod.MOD_ID, path), layer);
    }
}
