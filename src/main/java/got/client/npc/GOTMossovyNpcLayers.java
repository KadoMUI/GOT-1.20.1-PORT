package got.client.npc;

import got.GOTMod;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;

public final class GOTMossovyNpcLayers {
    public static final ModelLayerLocation BASE = layer("mossovy_npc", "base");
    public static final ModelLayerLocation OUTFIT = layer("mossovy_npc", "outfit");
    public static final ModelLayerLocation ARMOR_INNER = layer("mossovy_npc", "armor_inner");
    public static final ModelLayerLocation ARMOR_OUTER = layer("mossovy_npc", "armor_outer");

    private GOTMossovyNpcLayers() {}

    private static ModelLayerLocation layer(String path, String layer) {
        return new ModelLayerLocation(new ResourceLocation(GOTMod.MOD_ID, path), layer);
    }
}

