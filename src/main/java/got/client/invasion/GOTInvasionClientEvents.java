package got.client.invasion;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import got.GOTEquipment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.joml.Quaternionf;

/** 1.7.10 invasion boss-bar and floating rotating iron-hammer spawner presentation. */
@Mod.EventBusSubscriber(modid="got",value=Dist.CLIENT,bus=Mod.EventBusSubscriber.Bus.FORGE)
public final class GOTInvasionClientEvents {
 private GOTInvasionClientEvents(){}
 @SubscribeEvent public static void tick(TickEvent.ClientTickEvent e){if(e.phase==TickEvent.Phase.END&&!Minecraft.getInstance().isPaused())GOTClientInvasionState.tick();}
 @SubscribeEvent public static void hud(RenderGuiOverlayEvent.Post e){
  if(!GOTClientInvasionState.active())return; Minecraft mc=Minecraft.getInstance(); if(mc.player==null)return; GuiGraphics g=e.getGuiGraphics();
  int w=182,h=5,x=(g.guiWidth()-w)/2,y=12; if(mc.gui.getBossOverlay()!=null)y=32;
  g.fill(x,y,x+w,y+h,0xFF202020); g.fill(x+1,y+1,x+w-1,y+h-1,0xFF505050);
  int fill=(int)((w-2)*GOTClientInvasionState.health()); int color=0xFF000000|GOTClientInvasionState.type().faction().color(); if(fill>0)g.fill(x+1,y+1,x+1+fill,y+h-1,color);
  String title=net.minecraft.network.chat.Component.translatable("got.invasion."+GOTClientInvasionState.type().codeName()).getString(); g.drawCenteredString(mc.font,title,g.guiWidth()/2,y-10,0xFFFFFF);
 }
 @SubscribeEvent public static void world(RenderLevelStageEvent e){
  if(e.getStage()!=RenderLevelStageEvent.Stage.AFTER_ENTITIES||!GOTClientInvasionState.active())return; Minecraft mc=Minecraft.getInstance(); if(mc.level==null)return;
  var cam=e.getCamera().getPosition(); var c=GOTClientInvasionState.center(); PoseStack ps=e.getPoseStack(); ps.pushPose(); ps.translate(c.getX()+.5-cam.x,c.getY()+.75-cam.y,c.getZ()+.5-cam.z);
  float spin=(mc.level.getGameTime()+e.getPartialTick())*4.0F; ps.mulPose(new Quaternionf().rotationY((float)Math.toRadians(spin))); ps.scale(1.5F,1.5F,1.5F);
  MultiBufferSource.BufferSource buf=mc.renderBuffers().bufferSource(); mc.getItemRenderer().renderStatic(new ItemStack(GOTEquipment.IRON_HAMMER.get()),ItemDisplayContext.FIXED,15728880,OverlayTexture.NO_OVERLAY,ps,buf,mc.level,0); buf.endBatch(); ps.popPose();
 }
}
