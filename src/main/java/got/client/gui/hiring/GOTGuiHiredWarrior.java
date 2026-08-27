package got.client.gui.hiring;

import got.network.hiring.C2SHiredNpcActionPacket;
import got.npc.hiring.GOTHireSnapshot;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public final class GOTGuiHiredWarrior extends GOTGuiHiredNPC {
    private static final ResourceLocation TEX =
        new ResourceLocation("got","textures/gui/npc/hiredwarrior.png");

    public GOTGuiHiredWarrior(GOTHireSnapshot snapshot) {
        super(Component.literal("Hired Warrior"),snapshot);
    }

    @Override
    protected void init() {
        super.init();

        button("Follow", 8, 94, 50, b -> send(C2SHiredNpcActionPacket.Action.FOLLOW));
        button("Hold", 63, 94, 50, b -> send(C2SHiredNpcActionPacket.Action.HOLD));
        button("Patrol", 118, 94, 50, b -> send(C2SHiredNpcActionPacket.Action.PATROL));

        button("Teleport: "+(snapshot.autoTeleport()?"On":"Off"), 8, 118, 78,
            b -> send(C2SHiredNpcActionPacket.Action.TOGGLE_TELEPORT));

        button("Inventory", 90, 118, 78, b -> send(C2SHiredNpcActionPacket.Action.OPEN_INVENTORY));

        squadron=new EditBox(font, guiLeft+8, guiTop+144, 105, 18, Component.literal("Squadron"));
        squadron.setMaxLength(32);
        squadron.setValue(snapshot.squadron());
        addRenderableWidget(squadron);

        button("Set", 118, 143, 50, b ->
            send(C2SHiredNpcActionPacket.Action.SET_SQUADRON, squadron.getValue(), 0));
    }

    @Override
    public void render(GuiGraphics g,int mouseX,int mouseY,float partialTick) {
        renderBackground(g);
        g.blit(TEX,guiLeft,guiTop,0,0,xSize,ySize);
        g.drawCenteredString(font,snapshot.npcName(),width/2,guiTop+7,0x373737);
        g.drawCenteredString(font,snapshot.npcType(),width/2,guiTop+19,0x373737);
        drawStats(g);
        g.drawString(font,"Squadron",guiLeft+8,guiTop+134,0x404040,false);
        super.render(g,mouseX,mouseY,partialTick);
    }
}
