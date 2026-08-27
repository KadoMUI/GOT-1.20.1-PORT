package got.client.gui;

import got.network.C2SJaqenTutorialPacket;
import got.network.GOTNetwork;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public final class GOTGuiJaqenTutorialOffer extends Screen {
    private final int entityId;
    public GOTGuiJaqenTutorialOffer(int entityId){super(Component.translatable("got.jaqen.tutorial.title"));this.entityId=entityId;}
    @Override protected void init(){
        int x=width/2-100,y=height/2+38;
        addRenderableWidget(Button.builder(Component.translatable("got.gui.miniquestOffer.accept"), b->{GOTNetwork.CHANNEL.sendToServer(C2SJaqenTutorialPacket.accept(entityId)); onClose();}).bounds(x,y,96,20).build());
        addRenderableWidget(Button.builder(Component.translatable("got.gui.miniquestOffer.decline"), b->{GOTNetwork.CHANNEL.sendToServer(C2SJaqenTutorialPacket.decline(entityId)); onClose();}).bounds(x+104,y,96,20).build());
    }
    @Override public void render(GuiGraphics g,int mx,int my,float pt){renderBackground(g); g.drawCenteredString(font,title,width/2,height/2-42,0xFFE7C58A); for(int i=0;i<font.split(Component.translatable("got.jaqen.tutorial.offer"),280).size();i++){var line=font.split(Component.translatable("got.jaqen.tutorial.offer"),280).get(i);g.drawCenteredString(font,line,width/2,height/2-16+i*font.lineHeight,0xFFFFFF);} super.render(g,mx,my,pt);}
}
