package got.client.npc;

import got.GOTMod;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;

public final class GOTGoldenCompanyNpcLayers {
    public static final ModelLayerLocation BASE = layer("golden_company_npc", "base");
    public static final ModelLayerLocation OUTFIT = layer("golden_company_npc", "outfit");
    public static final ModelLayerLocation ARMOR_INNER = layer("golden_company_npc", "armor_inner");
    public static final ModelLayerLocation ARMOR_OUTER = layer("golden_company_npc", "armor_outer");
    private GOTGoldenCompanyNpcLayers() {}
    private static ModelLayerLocation layer(String path, String layer) {
        return new ModelLayerLocation(new ResourceLocation(GOTMod.MOD_ID, path), layer);
    }
}
