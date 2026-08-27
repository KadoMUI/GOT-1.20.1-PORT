package got;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
public final class AlloyForgeScreen extends AbstractContainerScreen<AlloyForgeMenu>{
 private static final ResourceLocation TEX=ResourceLocation.fromNamespaceAndPath(GOTMod.MOD_ID,"textures/gui/forge.png");
 public AlloyForgeScreen(AlloyForgeMenu m,Inventory i,Component t){super(m,i,t);imageWidth=176;imageHeight=233;inventoryLabelY=139;}
 @Override protected void renderBg(GuiGraphics g,float pt,int mx,int my){RenderSystem.setShaderColor(1,1,1,1);g.blit(TEX,leftPos,topPos,0,0,imageWidth,imageHeight);if(menu.isLit()){int b=menu.burnPixels();g.blit(TEX,leftPos+80,topPos+130-b,176,12-b,14,b+2);}int p=menu.progressPixels();for(int x:new int[]{26,62,98,134})g.blit(TEX,leftPos+x,topPos+75,176,14,p,16);}
 @Override public void render(GuiGraphics g,int mx,int my,float pt){renderBackground(g);super.render(g,mx,my,pt);renderTooltip(g,mx,my);}
}
