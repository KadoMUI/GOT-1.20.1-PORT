package got.client.npc;

import got.GOTMod;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;

public final class GOTJogosNhaiNpcLayers {
    public static final ModelLayerLocation BASE = layer("jogos_nhai_npc", "base");
    public static final ModelLayerLocation OUTFIT = layer("jogos_nhai_npc", "outfit");
    public static final ModelLayerLocation ARMOR_INNER = layer("jogos_nhai_npc", "armor_inner");
    public static final ModelLayerLocation ARMOR_OUTER = layer("jogos_nhai_npc", "armor_outer");

    private GOTJogosNhaiNpcLayers() {}

    private static ModelLayerLocation layer(String path, String layer) {
        return new ModelLayerLocation(new ResourceLocation(GOTMod.MOD_ID, path), layer);
    }
}
