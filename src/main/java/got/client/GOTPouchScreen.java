package got.client;
import got.GOTPouchMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
public final class GOTPouchScreen extends AbstractContainerScreen<GOTPouchMenu>{
 private static final ResourceLocation TEX=ResourceLocation.withDefaultNamespace("textures/gui/container/generic_54.png");
 public GOTPouchScreen(GOTPouchMenu m,Inventory i,Component t){super(m,i,t);this.imageHeight=114+menu.rows()*18;this.inventoryLabelY=this.imageHeight-94;}
 protected void renderBg(GuiGraphics g,float pt,int mx,int my){int x=(width-imageWidth)/2,y=(height-imageHeight)/2;g.blit(TEX,x,y,0,0,imageWidth,menu.rows()*18+17);g.blit(TEX,x,y+menu.rows()*18+17,0,126,imageWidth,96);}
 @Override public void render(GuiGraphics g,int x,int y,float f){renderBackground(g);super.render(g,x,y,f);renderTooltip(g,x,y);}
}
