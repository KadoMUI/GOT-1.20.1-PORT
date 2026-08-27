package got.client.gui;
import got.cape.GOTCape;
import got.client.cape.GOTClientCapeState;
import got.network.*;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public final class GOTGuiCapes extends Screen {
 private final Screen parent; private int index;
 public GOTGuiCapes(){this(new GOTGuiMenu());}
 public GOTGuiCapes(Screen parent){super(Component.translatable("got.gui.capes"));this.parent=parent;}
 @Override protected void init(){
  GOTNetwork.CHANNEL.sendToServer(new C2SRequestCapeDataPacket());
  GOTCape sel=GOTClientCapeState.selected();if(sel!=null)index=sel.ordinal();
  int cx=width/2, y=height/2+45;
  addRenderableWidget(Button.builder(Component.literal("<"),b->{index=(index+GOTCape.values().length-1)%GOTCape.values().length;}).bounds(cx-100,y,30,20).build());
  addRenderableWidget(Button.builder(Component.translatable("got.gui.capes.select"),b->select()).bounds(cx-65,y,130,20).build());
  addRenderableWidget(Button.builder(Component.literal(">"),b->{index=(index+1)%GOTCape.values().length;}).bounds(cx+70,y,30,20).build());
  addRenderableWidget(Button.builder(Component.translatable("got.gui.capes.remove"),b->GOTNetwork.CHANNEL.sendToServer(new C2SSelectCapePacket(""))).bounds(cx-65,y+25,130,20).build());
  addRenderableWidget(Button.builder(Component.translatable("gui.back"),b->minecraft.setScreen(parent)).bounds(cx-65,y+50,130,20).build());
 }
 private void select(){GOTCape c=GOTCape.values()[index];if(GOTClientCapeState.unlocked(c))GOTNetwork.CHANNEL.sendToServer(new C2SSelectCapePacket(c.id()));}
 @Override public void render(GuiGraphics g,int mx,int my,float pt){
  renderBackground(g);GOTCape c=GOTCape.values()[index];int cx=width/2;
  g.drawCenteredString(font,title,cx,25,0xFFFFFF);
  g.drawCenteredString(font,Component.translatable("got.capes."+c.id()),cx,55,0xFFFFFF);
  boolean u=GOTClientCapeState.unlocked(c);
  Component req=c==GOTCape.TARGARYEN?Component.translatable("got.capes.targaryen.requirement"):Component.translatable("got.capes.alignment.requirement");
  g.drawCenteredString(font,u?Component.translatable("got.gui.capes.unlocked"):req,cx,75,u?0x55FF55:0xFF7777);
  if(GOTClientCapeState.selected()==c)g.drawCenteredString(font,Component.translatable("got.gui.capes.selected"),cx,95,0xFFFF55);
  super.render(g,mx,my,pt);
 }
 @Override public void onClose(){minecraft.setScreen(parent);}
 @Override public boolean isPauseScreen(){return false;}
}
