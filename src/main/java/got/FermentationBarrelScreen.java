package got;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public final class FermentationBarrelScreen extends AbstractContainerScreen<FermentationBarrelMenu>{
 private static final ResourceLocation TEX=ResourceLocation.fromNamespaceAndPath("got","textures/gui/barrel/barrel.png"); private Button button;
 public FermentationBarrelScreen(FermentationBarrelMenu m,Inventory i,Component t){super(m,i,t);imageWidth=212;imageHeight=220;inventoryLabelY=126;}
 @Override protected void init(){super.init();button=addRenderableWidget(Button.builder(Component.translatable("gui.got.start_brewing"),b->{minecraft.gameMode.handleInventoryButtonClick(menu.containerId,0);}).bounds(leftPos+132,topPos+91,68,20).build());}
 @Override protected void containerTick(){super.containerTick();button.setMessage(Component.translatable(menu.mode()==1?"gui.got.stop_brewing":"gui.got.start_brewing"));}
 @Override protected void renderBg(GuiGraphics g,float pt,int mx,int my){RenderSystem.setShaderTexture(0,TEX);g.blit(TEX,leftPos,topPos,0,0,imageWidth,imageHeight);if(menu.mode()==1){int h=menu.progress();g.fill(leftPos+129,topPos+82-h,leftPos+189,topPos+82,0xAA7A3E18);}}
 @Override public void render(GuiGraphics g,int mx,int my,float pt){renderBackground(g);super.render(g,mx,my,pt);g.drawString(font,Component.translatable("gui.got.fermentation_strength",menu.strength()),leftPos+119,topPos+28,0x404040,false);g.drawString(font,Component.translatable("gui.got.servings",menu.servings()),leftPos+119,topPos+40,0x404040,false);renderTooltip(g,mx,my);}
}
