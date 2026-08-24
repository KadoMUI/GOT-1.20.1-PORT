package got.client.npc;

import got.GOTMod;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;

public final class GOTNightWatchNpcLayers {
    public static final ModelLayerLocation BASE = layer("night_watch_npc", "base");
    public static final ModelLayerLocation OUTFIT = layer("night_watch_npc", "outfit");
    public static final ModelLayerLocation ARMOR_INNER = layer("night_watch_npc", "armor_inner");
    public static final ModelLayerLocation ARMOR_OUTER = layer("night_watch_npc", "armor_outer");

    private GOTNightWatchNpcLayers() {}

    private static ModelLayerLocation layer(String path, String layer) {
        return new ModelLayerLocation(new ResourceLocation(GOTMod.MOD_ID, path), layer);
    }
}
