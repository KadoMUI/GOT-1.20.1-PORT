package got.client.gui.hiring;

import got.network.GOTNetwork;
import got.network.hiring.C2SHiredNpcActionPacket;
import got.npc.hiring.GOTHireSnapshot;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

public final class GOTGuiHire extends GOTGuiHireBase {
    public GOTGuiHire(GOTHireSnapshot snapshot) {
        super(Component.literal("Hire"), snapshot);
    }

    @Override
    protected void init() {
        super.init();
        var hire=button("Hire", 18, 132, 66, b ->
            GOTNetwork.CHANNEL.sendToServer(
                new C2SHiredNpcActionPacket(snapshot.entityId(),
                    C2SHiredNpcActionPacket.Action.HIRE, "", 0)
            ));
        hire.active=snapshot.hireAllowed();
        if (snapshot.mountedVariant()) {
            var mounted=button("Hire Mounted", 92, 132, 76, b ->
                GOTNetwork.CHANNEL.sendToServer(new C2SHiredNpcActionPacket(snapshot.entityId(),
                    C2SHiredNpcActionPacket.Action.HIRE, "mounted", 0)));
            mounted.active=snapshot.mountedHireAllowed();
        } else button("Cancel", 92, 132, 66, b -> onClose());
    }

    @Override
    public void render(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
        renderBackground(g);
        drawLegacyBase(g);

        g.drawString(font, "Faction: " + snapshot.factionId(), guiLeft+14, guiTop+48, 0x404040, false);

        if (!snapshot.requirementText().isBlank())
            g.drawString(font, snapshot.requirementText(), guiLeft+14, guiTop+64, 0x404040, false);

        int y=80;
        if (!snapshot.cost().isEmpty()) {
            g.drawString(font, "Cost:", guiLeft+14, guiTop+y, 0x404040, false);
            y+=12;
            for (var cost : snapshot.cost()) {
                g.drawString(font, cost.amount()+" x "+cost.itemId(), guiLeft+22, guiTop+y, 0x404040, false);
                y+=10;
            }
        }

        if (!snapshot.hireAllowed() && !snapshot.rejectionReason().isBlank()) {
            g.drawCenteredString(font, snapshot.rejectionReason(), width/2, guiTop+116, 0xAA2222);
        }

        super.render(g,mouseX,mouseY,partialTick);
    }
}
