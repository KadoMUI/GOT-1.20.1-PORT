package got.client.gui.hiring;

import got.network.hiring.C2SHiredNpcActionPacket;
import got.npc.hiring.GOTHireSnapshot;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public final class GOTGuiHiredFarmer extends GOTGuiHiredNPC {
    private static final ResourceLocation TEX =
        new ResourceLocation("got","textures/gui/npc/hiredfarmer.png");

    public GOTGuiHiredFarmer(GOTHireSnapshot snapshot) {
        super(Component.literal("Hired Farmer"),snapshot);
    }

    @Override
    protected void init() {
        super.init();
        button("Work/Wander", 8, 98, 76, b -> send(C2SHiredNpcActionPacket.Action.WANDER));
        button("Hold", 92, 98, 76, b -> send(C2SHiredNpcActionPacket.Action.HOLD));
        button("Range -", 8, 122, 76, b ->
            send(C2SHiredNpcActionPacket.Action.SET_GUARD_RANGE,"",Math.max(1,snapshot.guardRange()-1)));
        button("Range +", 92, 122, 76, b ->
            send(C2SHiredNpcActionPacket.Action.SET_GUARD_RANGE,"",Math.min(64,snapshot.guardRange()+1)));
        button("Inventory", 50, 146, 76, b -> send(C2SHiredNpcActionPacket.Action.OPEN_INVENTORY));
    }

    @Override
    public void render(GuiGraphics g,int mouseX,int mouseY,float partialTick) {
        renderBackground(g);
        g.blit(TEX,guiLeft,guiTop,0,0,xSize,ySize);
        g.drawCenteredString(font,snapshot.npcName(),width/2,guiTop+7,0x373737);
        drawStats(g);
        g.drawCenteredString(font,"Work range: "+snapshot.guardRange(),width/2,guiTop+86,0x404040);
        super.render(g,mouseX,mouseY,partialTick);
    }
}
