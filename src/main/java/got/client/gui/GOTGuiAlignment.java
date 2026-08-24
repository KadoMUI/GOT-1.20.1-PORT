package got.client.gui;

import got.client.faction.GOTClientFactionState;
import got.faction.GOTFaction;
import got.faction.GOTFactionService;
import got.network.C2SRequestFactionDataPacket;
import got.network.GOTNetwork;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

import java.util.Comparator;
import java.util.List;

public final class GOTGuiAlignment extends GOTGuiMenuBaseReturn {
    private int scroll;

    public GOTGuiAlignment() {
        super(Component.translatable("got.gui.alignment"));
        sizeX=256; sizeY=230;
    }

    @Override
    protected void init() {
        super.init();
        GOTNetwork.CHANNEL.sendToServer(new C2SRequestFactionDataPacket());
    }

    @Override
    public boolean mouseScrolled(double mouseX,double mouseY,double delta) {
        int max=Math.max(0,GOTFaction.playableFactions().size()-12);
        if(delta<0) scroll=Math.min(max,scroll+1);
        else if(delta>0) scroll=Math.max(0,scroll-1);
        return true;
    }

    @Override
    public void render(GuiGraphics g,int mouseX,int mouseY,float partialTick) {
        renderBackground(g);
        g.drawCenteredString(font,title,width/2,guiTop+5,0xFFFFFF);

        List<GOTFaction> factions=GOTFaction.playableFactions().stream()
                .sorted(Comparator.comparingDouble((GOTFaction f) -> GOTClientFactionState.alignment(f)).reversed())
                .toList();

        int end=Math.min(factions.size(),scroll+12);
        int y=guiTop+27;
        for(int i=scroll;i<end;i++) {
            GOTFaction faction=factions.get(i);
            float alignment=GOTClientFactionState.alignment(faction);
            var rank=faction.rankFor(alignment);
            int color=faction.color();

            g.drawString(font,faction.displayName(),guiLeft+12,y,color,false);
            g.drawString(font,GOTFactionService.formatAlignment(alignment),
                    guiLeft+132,y,alignment<0?0xCC5555:0xDDDDDD,false);
            g.drawString(font,rank.displayName(faction),guiLeft+174,y,0xC8B47A,false);
            y+=15;
        }

        g.drawString(font,Component.translatable("got.gui.alignment.hint"),
                guiLeft+12,guiTop+211,0x999999,false);
        super.render(g,mouseX,mouseY,partialTick);
    }
}
