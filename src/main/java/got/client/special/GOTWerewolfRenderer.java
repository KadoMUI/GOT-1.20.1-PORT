package got.client.special;
import got.GOTMod;
import got.special.GOTWerewolfEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
public final class GOTWerewolfRenderer extends MobRenderer<GOTWerewolfEntity,GOTSpecialHumanoidModel<GOTWerewolfEntity>> {
 private static final ResourceLocation TEX=new ResourceLocation(GOTMod.MOD_ID,"textures/entity/animal/werewolf.png");
 public GOTWerewolfRenderer(EntityRendererProvider.Context c){super(c,new GOTSpecialHumanoidModel<>(c.bakeLayer(GOTSpecialHumanoidLayers.WEREWOLF)),0.7F);}
 public ResourceLocation getTextureLocation(GOTWerewolfEntity e){return TEX;}
}
