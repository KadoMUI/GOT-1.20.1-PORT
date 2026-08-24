package got.client.gui.hiring;

import got.network.GOTNetwork;
import got.network.hiring.C2SSetCommandHornModePacket;
import got.npc.hiring.command.GOTCommandHornMode;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;

public final class GOTGuiCommandHornSelect extends Screen {
    private final InteractionHand hand;

    public GOTGuiCommandHornSelect(InteractionHand hand) {
        super(Component.literal("Command Horn"));
        this.hand = hand;
    }

    @Override
    protected void init() {
        int x = width / 2 - 70;
        int y = height / 2 - 42;

        addRenderableWidget(Button.builder(Component.literal("Halt"),
            b -> choose(GOTCommandHornMode.HALT)).bounds(x, y, 140, 20).build());

        addRenderableWidget(Button.builder(Component.literal("Ready"),
            b -> choose(GOTCommandHornMode.READY)).bounds(x, y + 24, 140, 20).build());

        addRenderableWidget(Button.builder(Component.literal("Summon"),
            b -> choose(GOTCommandHornMode.SUMMON)).bounds(x, y + 48, 140, 20).build());
    }

    private void choose(GOTCommandHornMode mode) {
        GOTNetwork.CHANNEL.sendToServer(new C2SSetCommandHornModePacket(hand, mode));
        onClose();
    }

    @Override
    public void render(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
        renderBackground(g);
        g.drawCenteredString(font, title, width / 2, height / 2 - 64, 0xFFFFFF);
        super.render(g, mouseX, mouseY, partialTick);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
