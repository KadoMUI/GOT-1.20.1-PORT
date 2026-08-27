package got.client.special;
import got.GOTMod;
import got.special.GOTStoneManEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
public final class GOTStoneManRenderer extends MobRenderer<GOTStoneManEntity,GOTSpecialHumanoidModel<GOTStoneManEntity>> {
 public GOTStoneManRenderer(EntityRendererProvider.Context c){super(c,new GOTSpecialHumanoidModel<>(c.bakeLayer(GOTSpecialHumanoidLayers.STONE_MAN)),0.5F);}
 public ResourceLocation getTextureLocation(GOTStoneManEntity e){int i=Math.floorMod(e.getUUID().hashCode(),8);String sex=((e.getUUID().hashCode()>>>3)&1)==0?"male":"female";return new ResourceLocation(GOTMod.MOD_ID,"textures/entity/essos/stone/"+sex+"/"+i+".png");}
}
