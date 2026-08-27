package got.client.cape;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Axis;
import got.cape.GOTCape;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.util.Mth;

public final class GOTCapeLayer extends RenderLayer<AbstractClientPlayer,PlayerModel<AbstractClientPlayer>> {
 private final ModelPart cape;
 public GOTCapeLayer(RenderLayerParent<AbstractClientPlayer,PlayerModel<AbstractClientPlayer>> parent){super(parent);
  MeshDefinition m=new MeshDefinition();m.getRoot().addOrReplaceChild("cape",CubeListBuilder.create().texOffs(0,0).addBox(-5,0,-1,10,16,1),PartPose.ZERO);
  cape=LayerDefinition.create(m,64,32).bakeRoot().getChild("cape");
 }
 @Override public void render(PoseStack ps,MultiBufferSource buf,int light,AbstractClientPlayer p,float limb,float limbAmt,float pt,float age,float yaw,float pitch){
  GOTCape c=GOTClientCapeState.player(p.getUUID());if(c==null&&p==net.minecraft.client.Minecraft.getInstance().player)c=GOTClientCapeState.selected();if(c==null||p.isInvisible())return;
  ps.pushPose();ps.translate(0,0,0.125D);
  double dx=Mth.lerp(pt,p.xCloakO,p.xCloak)-Mth.lerp(pt,p.xo,p.getX());
  double dy=Mth.lerp(pt,p.yCloakO,p.yCloak)-Mth.lerp(pt,p.yo,p.getY());
  double dz=Mth.lerp(pt,p.zCloakO,p.zCloak)-Mth.lerp(pt,p.zo,p.getZ());
  float body=Mth.rotLerp(pt,p.yBodyRotO,p.yBodyRot);double sn=Mth.sin(body*0.017453292F),cs=-Mth.cos(body*0.017453292F);
  float lift=(float)Mth.clamp(dy*10,-6,32);float sway=(float)Mth.clamp((dx*sn+dz*cs)*100,0,150);
  float side=(float)Mth.clamp((dx*cs-dz*sn)*100,-20,20);
  if(p.isCrouching())lift+=25;
  ps.mulPose(Axis.XP.rotationDegrees(6+sway/2+lift));
  ps.mulPose(Axis.ZP.rotationDegrees(side/2));
  ps.mulPose(Axis.YP.rotationDegrees(180-side/2));
  VertexConsumer v=buf.getBuffer(RenderType.entitySolid(c.texture()));
  cape.render(ps,v,light,OverlayTexture.NO_OVERLAY);
  ps.popPose();
 }
}
