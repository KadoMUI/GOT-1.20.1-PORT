package got.client.gui.hiring;

import got.npc.hiring.GOTHireSnapshot;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

abstract class GOTGuiHireBase extends Screen {
    protected static final ResourceLocation HIRED =
        new ResourceLocation("got", "textures/gui/npc/hired.png");

    protected GOTHireSnapshot snapshot;
    protected int guiLeft, guiTop;
    protected final int xSize=176, ySize=166;

    protected GOTGuiHireBase(Component title, GOTHireSnapshot snapshot) {
        super(title);
        this.snapshot=snapshot;
    }

    @Override
    protected void init() {
        guiLeft=(width-xSize)/2;
        guiTop=(height-ySize)/2;
    }

    protected Button button(String text, int x, int y, int w, Button.OnPress press) {
        Button b=Button.builder(Component.literal(text), press)
            .bounds(guiLeft+x, guiTop+y, w, 20).build();
        addRenderableWidget(b);
        return b;
    }

    protected void drawLegacyBase(GuiGraphics g) {
        g.blit(HIRED, guiLeft, guiTop, 0, 0, xSize, ySize);
        g.drawCenteredString(font, snapshot.npcName(), width/2, guiTop+8, 0x373737);
        g.drawCenteredString(font, snapshot.npcType(), width/2, guiTop+20, 0x373737);
    }

    @Override
    public boolean isPauseScreen() { return false; }
}
