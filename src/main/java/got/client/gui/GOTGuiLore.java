package got.client.gui;

import got.client.lore.GOTClientLoreState;
import got.client.lore.GOTLoreView;
import got.network.C2SRequestLoreDataPacket;
import got.network.GOTNetwork;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

import java.util.List;

public final class GOTGuiLore extends GOTGuiMenuBaseReturn {
    private int selected = -1;
    private int scroll;

    public GOTGuiLore() {
        super(Component.translatable("got.gui.lore"));
        sizeX=310; sizeY=230;
    }

    @Override protected void init() {
        super.init();
        GOTNetwork.CHANNEL.sendToServer(new C2SRequestLoreDataPacket());
    }

    @Override public boolean mouseScrolled(double mx,double my,double delta) {
        int max=Math.max(0,GOTClientLoreState.entries().size()-8);
        if(delta<0)scroll=Math.min(max,scroll+1); else if(delta>0)scroll=Math.max(0,scroll-1);
        return true;
    }

    @Override public boolean mouseClicked(double mx,double my,int button) {
        if(button==0 && mx>=guiLeft+8 && mx<guiLeft+118 && my>=guiTop+30 && my<guiTop+190){
            int row=(int)((my-(guiTop+30))/20);
            int idx=scroll+row;
            if(idx<GOTClientLoreState.entries().size())selected=idx;
            return true;
        }
        return super.mouseClicked(mx,my,button);
    }

    @Override public void render(GuiGraphics g,int mouseX,int mouseY,float partialTick) {
        renderBackground(g);
        super.render(g,mouseX,mouseY,partialTick);
        g.drawCenteredString(font,title,width/2,guiTop+10,0xFFFFFF);
        List<GOTLoreView> entries=GOTClientLoreState.entries();
        if(entries.isEmpty()){
            g.drawCenteredString(font,Component.translatable("got.lore.none"),width/2,guiTop+100,0xAAAAAA); return;
        }
        int end=Math.min(entries.size(),scroll+8);
        for(int i=scroll;i<end;i++){
            int y=guiTop+30+(i-scroll)*20;
            int color=i==selected?0xFFD27F:0xFFFFFF;
            String title=entries.get(i).title();
            if(font.width(title)>104)title=font.plainSubstrByWidth(title,101)+"...";
            g.drawString(font,title,guiLeft+10,y+5,color,false);
        }
        if(selected<0||selected>=entries.size())selected=0;
        GOTLoreView e=entries.get(selected);
        int x=guiLeft+128;
        g.drawString(font,e.title(),x,guiTop+31,0xFFD27F,false);
        if(e.author()!=null&&!e.author().isBlank())g.drawString(font,Component.translatable("got.lore.by",e.author()),x,guiTop+44,0xAAAAAA,false);
        int y=guiTop+60;
        for(var line:font.split(Component.literal(e.text()),170)){
            if(y>guiTop+211)break;
            g.drawString(font,line,x,y,0xDDDDDD,false); y+=10;
        }
        g.drawString(font,Component.translatable("got.lore.discovered.count",entries.size()),guiLeft+8,guiTop+211,0xAAAAAA,false);
    }
}
