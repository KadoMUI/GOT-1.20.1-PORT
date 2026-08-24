package got.client.npc;

import got.GOTMod;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;

public final class GOTIronbornNpcLayers {
    public static final ModelLayerLocation BASE = layer("ironborn_npc", "base");
    public static final ModelLayerLocation OUTFIT = layer("ironborn_npc", "outfit");
    public static final ModelLayerLocation ARMOR_INNER = layer("ironborn_npc", "armor_inner");
    public static final ModelLayerLocation ARMOR_OUTER = layer("ironborn_npc", "armor_outer");

    private GOTIronbornNpcLayers() {}

    private static ModelLayerLocation layer(String path, String layer) {
        return new ModelLayerLocation(new ResourceLocation(GOTMod.MOD_ID, path), layer);
    }
}
