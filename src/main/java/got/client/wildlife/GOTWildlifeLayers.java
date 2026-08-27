package got.client.wildlife;
import got.GOTMod; import net.minecraft.client.model.geom.ModelLayerLocation; import net.minecraft.resources.ResourceLocation;
public final class GOTWildlifeLayers {
 public static final ModelLayerLocation DEER=layer("deer"),BEAR=layer("bear"),BISON=layer("bison"),DIREWOLF=layer("direwolf"),ELEPHANT=layer("elephant"),MAMMOTH=layer("mammoth"),GIRAFFE=layer("giraffe"),LION=layer("lion"),ORYX=layer("oryx"),DIKDIK=layer("dikdik"),WALRUS=layer("walrus"),BEAVER=layer("beaver"),SHADOWCAT=layer("shadowcat");
 private static ModelLayerLocation layer(String n){return new ModelLayerLocation(new ResourceLocation(GOTMod.MOD_ID,n),"main");} private GOTWildlifeLayers(){}
}
