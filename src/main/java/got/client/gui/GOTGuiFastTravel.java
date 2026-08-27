package got.client.gui;

import got.common.fasttravel.GOTFastTravelData;
import got.common.world.map.GOTWaypoint;
import got.network.C2SDeleteCustomWaypointPacket;
import got.network.C2SFastTravelPacket;
import got.network.C2SRenameCustomWaypointPacket;
import got.network.GOTNetwork;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

/** Confirmation screen shared by fixed and player-created waypoints. */
public final class GOTGuiFastTravel extends Screen {
    private final Screen parent;
    private final GOTWaypoint waypoint;
    private final GOTFastTravelData.Custom custom;
    private EditBox renameBox;

    public GOTGuiFastTravel(Screen parent, GOTWaypoint waypoint) {
        super(Component.translatable("got.fastTravel.title")); this.parent=parent; this.waypoint=waypoint; this.custom=null;
    }
    public GOTGuiFastTravel(Screen parent, GOTFastTravelData.Custom custom) {
        super(Component.translatable("got.fastTravel.title")); this.parent=parent; this.waypoint=null; this.custom=custom;
    }

    @Override protected void init() {
        int cx=width/2, cy=height/2;
        addRenderableWidget(Button.builder(Component.translatable("got.fastTravel.travel"), b->confirmTravel()).bounds(cx-102,cy+28,100,20).build());
        addRenderableWidget(Button.builder(Component.translatable("gui.cancel"), b->onClose()).bounds(cx+2,cy+28,100,20).build());
        if(custom!=null && minecraft != null && minecraft.player != null && custom.ownedBy(minecraft.player.getUUID())){
            renameBox=new EditBox(font,cx-102,cy+53,204,20,Component.translatable("got.fastTravel.custom.rename")); renameBox.setMaxLength(32); renameBox.setValue(custom.name()); addRenderableWidget(renameBox);
            addRenderableWidget(Button.builder(Component.translatable("got.fastTravel.custom.rename"),b->{GOTNetwork.CHANNEL.sendToServer(new C2SRenameCustomWaypointPacket(custom.id(),renameBox.getValue()));onClose();}).bounds(cx-102,cy+77,100,20).build());
            addRenderableWidget(Button.builder(Component.translatable("got.fastTravel.custom.delete"),b->{GOTNetwork.CHANNEL.sendToServer(new C2SDeleteCustomWaypointPacket(custom.id()));onClose();}).bounds(cx+2,cy+77,100,20).build());
        }
    }
    private void confirmTravel(){ GOTNetwork.CHANNEL.sendToServer(waypoint!=null?new C2SFastTravelPacket(waypoint.getID()):new C2SFastTravelPacket(custom.owner(),custom.id())); if(minecraft!=null)minecraft.setScreen(null); }
    @Override public void render(GuiGraphics g,int mx,int my,float pt){renderBackground(g);int l=width/2-120,t=height/2-62,r=width/2+120,b=height/2+(custom==null?58:112);g.fill(l-3,t-3,r+3,b+3,0xFF2C0D08);g.fill(l,t,r,b,0xFFD0AD79);g.drawCenteredString(font,title,width/2,t+12,0xFF4A170E);Component name=waypoint!=null?waypoint.getDisplayName():Component.literal(custom.name());int x=waypoint!=null?waypoint.getCoordX():custom.x(),z=waypoint!=null?waypoint.getCoordZ():custom.z();g.drawCenteredString(font,name,width/2,t+34,0xFF3A120A);g.drawCenteredString(font,Component.literal("X: "+x+", Z: "+z),width/2,t+49,0xFF5A2A18);g.drawCenteredString(font,Component.translatable("got.fastTravel.confirm"),width/2,t+67,0xFF4A170E);super.render(g,mx,my,pt);}
    @Override public void onClose(){if(minecraft!=null)minecraft.setScreen(parent);} @Override public boolean isPauseScreen(){return false;}
}
