package got.client.gui;

import got.achievement.GOTAchievementCatalog;
import got.client.achievement.GOTClientAchievementState;
import got.network.C2SRequestAchievementDataPacket;
import got.network.GOTNetwork;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public final class GOTGuiAchievements extends GOTGuiMenuBaseReturn {
    private static final ResourceLocation PAGE =
            ResourceLocation.fromNamespaceAndPath("got", "textures/gui/achievements/page.png");
    private static final ResourceLocation ICONS =
            ResourceLocation.fromNamespaceAndPath("got", "textures/gui/achievements/icons.png");

    private GOTAchievementCatalog.Category category=GOTAchievementCatalog.Category.GENERAL;
    private int scroll;
    private Button previous;
    private Button next;

    public GOTGuiAchievements() {
        super(Component.translatable("got.gui.achievements"));
        sizeX=256;
        sizeY=230;
    }

    @Override
    protected void init() {
        super.init();
        previous=addRenderableWidget(Button.builder(Component.literal("<"), b -> changeCategory(-1))
                .bounds(guiLeft+8, guiTop+25, 20, 20).build());
        next=addRenderableWidget(Button.builder(Component.literal(">"), b -> changeCategory(1))
                .bounds(guiLeft+228, guiTop+25, 20, 20).build());
        GOTNetwork.CHANNEL.sendToServer(new C2SRequestAchievementDataPacket());
    }

    private void changeCategory(int amount) {
        var values=GOTAchievementCatalog.Category.values();
        int i=(category.ordinal()+amount+values.length)%values.length;
        category=values[i];
        scroll=0;
    }

    @Override
    public boolean mouseScrolled(double mx,double my,double delta) {
        List<GOTAchievementCatalog.Entry> list=GOTAchievementCatalog.category(category);
        int max=Math.max(0,list.size()-4);
        if (delta < 0) scroll=Math.min(max,scroll+1);
        else if (delta > 0) scroll=Math.max(0,scroll-1);
        return true;
    }

    @Override
    public void render(GuiGraphics g,int mouseX,int mouseY,float partialTick) {
        g.blit(PAGE,guiLeft,guiTop,0,0,sizeX,sizeY,256,256);
        super.render(g,mouseX,mouseY,partialTick);
        g.drawCenteredString(font, Component.translatable("got.achievement.category."+category.name()),
                width/2, guiTop+31, 0x5A402B);

        List<GOTAchievementCatalog.Entry> list=GOTAchievementCatalog.category(category);
        int end=Math.min(list.size(),scroll+4);
        for (int i=scroll;i<end;i++) {
            var entry=list.get(i);
            int row=i-scroll;
            int y=guiTop+50+row*42;
            boolean taken=GOTClientAchievementState.has(entry.code());

            g.blit(ICONS,guiLeft+9,y,0,taken?0:50,190,40,256,256);
            int titleColor=taken?0x7A5C3E:0x56504F;
            g.drawString(font,Component.translatable(entry.titleKey()),guiLeft+16,y+6,titleColor,false);
            g.drawString(font,Component.translatable(entry.descKey()),guiLeft+16,y+20,titleColor,false);
        }

        g.drawString(font,
                Component.translatable("got.gui.achievements.count",
                        GOTClientAchievementState.count(), GOTAchievementCatalog.all().size()),
                guiLeft+8,guiTop+216,0x5A402B,false);
    }

}
