package got.client.npc;

import got.GOTMod;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;

public final class GOTNorvosNpcLayers {
    public static final ModelLayerLocation BASE = layer("norvos_npc", "base");
    public static final ModelLayerLocation OUTFIT = layer("norvos_npc", "outfit");
    public static final ModelLayerLocation ARMOR_INNER = layer("norvos_npc", "armor_inner");
    public static final ModelLayerLocation ARMOR_OUTER = layer("norvos_npc", "armor_outer");

    private GOTNorvosNpcLayers() {}

    private static ModelLayerLocation layer(String path, String layer) {
        return new ModelLayerLocation(new ResourceLocation(GOTMod.MOD_ID, path), layer);
    }
}

