package got.client.gui.hiring;

import got.network.GOTNetwork;
import got.network.hiring.C2SHiredNpcActionPacket;
import got.npc.hiring.GOTHireSnapshot;
import got.npc.hiring.GOTHiredOrder;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

abstract class GOTGuiHiredNPC extends GOTGuiHireBase {
    protected EditBox squadron;

    protected GOTGuiHiredNPC(Component title, GOTHireSnapshot snapshot) {
        super(title,snapshot);
    }

    protected void send(C2SHiredNpcActionPacket.Action action) {
        GOTNetwork.CHANNEL.sendToServer(new C2SHiredNpcActionPacket(snapshot.entityId(),action,"",0));
    }

    protected void send(C2SHiredNpcActionPacket.Action action, String text, int number) {
        GOTNetwork.CHANNEL.sendToServer(new C2SHiredNpcActionPacket(snapshot.entityId(),action,text,number));
    }

    protected void drawStats(GuiGraphics g) {
        g.drawString(font, "Status: "+snapshot.order().name(), guiLeft+12, guiTop+42, 0x404040, false);
        g.drawString(font, "Level: "+snapshot.level(), guiLeft+12, guiTop+54, 0x404040, false);
        g.drawString(font, "XP: "+snapshot.xp(), guiLeft+12, guiTop+66, 0x404040, false);
        g.drawString(font, "Kills: "+snapshot.kills(), guiLeft+12, guiTop+78, 0x404040, false);
    }
}
