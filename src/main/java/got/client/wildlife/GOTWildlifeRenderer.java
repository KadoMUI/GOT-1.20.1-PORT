package got.client.wildlife;
import com.mojang.blaze3d.vertex.PoseStack; import got.GOTMod; import net.minecraft.client.model.geom.ModelLayerLocation; import net.minecraft.client.renderer.entity.*; import net.minecraft.resources.ResourceLocation; import net.minecraft.world.entity.Mob; import java.util.function.Function;
public final class GOTWildlifeRenderer<T extends Mob> extends MobRenderer<T,GOTWildlifeModel<T>> {
 private final Function<T,String> texture; private final float scale;
 public GOTWildlifeRenderer(EntityRendererProvider.Context c,ModelLayerLocation layer,GOTWildlifeModel.Kind kind,float shadow,Function<T,String> texture){this(c,layer,kind,shadow,1F,texture);}
 public GOTWildlifeRenderer(EntityRendererProvider.Context c,ModelLayerLocation layer,GOTWildlifeModel.Kind kind,float shadow,float scale,Function<T,String> texture){super(c,new GOTWildlifeModel<>(c.bakeLayer(layer),kind),shadow);this.texture=texture;this.scale=scale;}
 @Override protected void scale(T e,PoseStack p,float partial){p.scale(scale,scale,scale);super.scale(e,p,partial);} @Override public ResourceLocation getTextureLocation(T e){return new ResourceLocation(GOTMod.MOD_ID,texture.apply(e));} public static int skin(Mob e,int count){return Math.floorMod(e.getUUID().hashCode(),count);}
}
