package got.client.npc;

import got.GOTMod;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;

public final class GOTGhiscarNpcLayers {
    public static final ModelLayerLocation BASE = layer("ghiscar_npc", "base");
    public static final ModelLayerLocation OUTFIT = layer("ghiscar_npc", "outfit");
    public static final ModelLayerLocation ARMOR_INNER = layer("ghiscar_npc", "armor_inner");
    public static final ModelLayerLocation ARMOR_OUTER = layer("ghiscar_npc", "armor_outer");

    private GOTGhiscarNpcLayers() {}

    private static ModelLayerLocation layer(String path, String layer) {
        return new ModelLayerLocation(new ResourceLocation(GOTMod.MOD_ID, path), layer);
    }
}
