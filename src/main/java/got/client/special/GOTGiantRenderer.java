package got.client.special;
import got.GOTMod; import got.special.*; import com.mojang.blaze3d.vertex.PoseStack; import net.minecraft.client.renderer.entity.*; import net.minecraft.resources.ResourceLocation;
public final class GOTGiantRenderer<T extends GOTGiantBaseEntity> extends MobRenderer<T,GOTGiantModel<T>> {
 private final boolean wight; public GOTGiantRenderer(EntityRendererProvider.Context c,boolean wight){super(c,new GOTGiantModel<>(c.bakeLayer(GOTGiantLayers.GIANT)),1.0F);this.wight=wight;}
 public ResourceLocation getTextureLocation(T e){int i=Math.floorMod(e.getUUID().hashCode(),2);return new ResourceLocation(GOTMod.MOD_ID,wight?"textures/entity/westeros/ice/giant/"+i+".png":"textures/entity/westeros/giant/giant/"+i+".png");}
 protected void scale(T e,PoseStack p,float partial){p.scale(1.6F,1.6F,1.6F);}
}
