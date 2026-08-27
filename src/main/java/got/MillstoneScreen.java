package got;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
public final class MillstoneScreen extends AbstractContainerScreen<MillstoneMenu>{
 private static final ResourceLocation TEX=ResourceLocation.fromNamespaceAndPath(GOTMod.MOD_ID,"textures/gui/millstone.png");
 public MillstoneScreen(MillstoneMenu m,Inventory i,Component t){super(m,i,t);imageWidth=176;imageHeight=166;inventoryLabelY=72;}
 @Override protected void renderBg(GuiGraphics g,float pt,int mx,int my){RenderSystem.setShaderColor(1,1,1,1);g.blit(TEX,leftPos,topPos,0,0,imageWidth,imageHeight);int p=menu.progressPixels();if(p>0)g.blit(TEX,leftPos+76,topPos+35,176,0,p,16);}
 @Override public void render(GuiGraphics g,int mx,int my,float pt){renderBackground(g);super.render(g,mx,my,pt);renderTooltip(g,mx,my);}
}
