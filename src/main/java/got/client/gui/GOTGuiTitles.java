package got.client.gui;

import got.client.player.GOTClientTitleState;
import got.faction.GOTFaction;
import got.faction.GOTFactionRank;
import got.network.C2SRequestTitlesPacket;
import got.network.C2SSelectTitlePacket;
import got.network.GOTNetwork;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;

public final class GOTGuiTitles extends GOTGuiMenuBaseReturn {
    private static final ChatFormatting[] COLORS = {
            ChatFormatting.WHITE, ChatFormatting.GRAY, ChatFormatting.DARK_GRAY,
            ChatFormatting.RED, ChatFormatting.GOLD, ChatFormatting.YELLOW,
            ChatFormatting.GREEN, ChatFormatting.AQUA, ChatFormatting.BLUE,
            ChatFormatting.LIGHT_PURPLE
    };

    private int scroll;
    private int selectedIndex=-1;
    private int colorIndex;
    private Button selectButton;
    private Button removeButton;
    private Button colorButton;

    public GOTGuiTitles() {
        super(Component.translatable("got.gui.titles"));
        sizeX=256; sizeY=230;
    }

    @Override
    protected void init() {
        super.init();
        colorIndex=indexOfColor(GOTClientTitleState.selectedColor());

        selectButton=addRenderableWidget(Button.builder(Component.translatable("got.gui.titles.select"),
                b -> select()).bounds(guiLeft+28,guiTop+190,65,20).build());
        removeButton=addRenderableWidget(Button.builder(Component.translatable("got.gui.titles.remove"),
                b -> GOTNetwork.CHANNEL.sendToServer(
                        new C2SSelectTitlePacket("",ChatFormatting.WHITE)))
                .bounds(guiLeft+96,guiTop+190,65,20).build());
        colorButton=addRenderableWidget(Button.builder(Component.empty(),
                b -> {
                    colorIndex=(colorIndex+1)%COLORS.length;
                    updateButtons();
                }).bounds(guiLeft+164,guiTop+190,65,20).build());

        GOTNetwork.CHANNEL.sendToServer(new C2SRequestTitlesPacket());
        updateButtons();
    }

    private List<String> unlocked() {
        return new ArrayList<>(GOTClientTitleState.unlocked());
    }

    @Override
    public boolean mouseClicked(double mouseX,double mouseY,int button) {
        if(button==0) {
            int y=guiTop+43;
            List<String> ids=unlocked();
            int end=Math.min(ids.size(),scroll+10);
            for(int i=scroll;i<end;i++) {
                if(mouseX>=guiLeft+28 && mouseX<=guiLeft+228
                        && mouseY>=y && mouseY<y+14) {
                    selectedIndex=i;
                    updateButtons();
                    return true;
                }
                y+=14;
            }
        }
        return super.mouseClicked(mouseX,mouseY,button);
    }

    @Override
    public boolean mouseScrolled(double mouseX,double mouseY,double delta) {
        int max=Math.max(0,unlocked().size()-10);
        if(delta<0) scroll=Math.min(max,scroll+1);
        else if(delta>0) scroll=Math.max(0,scroll-1);
        return true;
    }

    private void select() {
        List<String> ids=unlocked();
        if(selectedIndex<0 || selectedIndex>=ids.size()) return;
        GOTNetwork.CHANNEL.sendToServer(
                new C2SSelectTitlePacket(ids.get(selectedIndex),COLORS[colorIndex]));
    }

    private void updateButtons() {
        if(selectButton==null) return;
        selectButton.active=selectedIndex>=0 && selectedIndex<unlocked().size();
        removeButton.active=!GOTClientTitleState.selectedId().isBlank();
        colorButton.setMessage(Component.translatable("got.gui.titles.color")
                .append(": ")
                .append(Component.literal(COLORS[colorIndex].getName())
                        .withStyle(COLORS[colorIndex])));
    }

    private static int indexOfColor(ChatFormatting color) {
        for(int i=0;i<COLORS.length;i++) if(COLORS[i]==color) return i;
        return 0;
    }

    private Component titleName(String id) {
        String[] p=id.split(":");
        if(p.length==3 && p[0].equals("rank")) {
            return GOTFaction.byId(p[1]).map(f -> {
                GOTFactionRank rank=f.ranksDescending().stream()
                        .filter(r -> r.key().equals(p[2])).findFirst()
                        .orElse(f.rankFor(0));
                return rank.displayName(f);
            }).orElse(Component.literal(id));
        }
        return Component.literal(id);
    }

    @Override
    public void tick() {
        super.tick();
        updateButtons();
    }

    @Override
    public void render(GuiGraphics g,int mouseX,int mouseY,float partialTick) {
        renderBackground(g);
        g.drawCenteredString(font,title,width/2,guiTop+5,0xFFFFFF);

        Component current=GOTClientTitleState.selectedId().isBlank()
                ? Component.translatable("got.gui.titles.currentTitle.none")
                : titleName(GOTClientTitleState.selectedId()).copy()
                        .withStyle(GOTClientTitleState.selectedColor());
        g.drawCenteredString(font,Component.translatable("got.gui.titles.currentTitle",current),
                width/2,guiTop+22,0xFFFFFF);

        List<String> ids=unlocked();
        int end=Math.min(ids.size(),scroll+10);
        int y=guiTop+43;
        for(int i=scroll;i<end;i++) {
            boolean selected=i==selectedIndex;
            if(selected) g.fill(guiLeft+26,y-2,guiLeft+230,y+11,0x663C2E20);
            Component name=titleName(ids.get(i));
            g.drawString(font,name,guiLeft+32,y,0xDDDDDD,false);
            y+=14;
        }

        if(ids.isEmpty()) {
            g.drawCenteredString(font,Component.translatable("got.gui.titles.noneUnlocked"),
                    width/2,guiTop+85,0xAAAAAA);
        }
        super.render(g,mouseX,mouseY,partialTick);
    }
}
