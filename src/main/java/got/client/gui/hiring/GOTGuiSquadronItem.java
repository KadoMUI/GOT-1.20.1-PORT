package got.client.gui.hiring;

import got.network.GOTNetwork;
import got.network.hiring.C2SSquadronActionPacket;
import got.npc.hiring.GOTHiredOrder;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;

public final class GOTGuiSquadronItem extends Screen {
    private static final ResourceLocation TEX =
        new ResourceLocation("got", "textures/gui/squadronitem.png");

    private final InteractionHand hand;
    private EditBox squadronName;

    public GOTGuiSquadronItem(InteractionHand hand) {
        super(Component.literal("Squadron"));
        this.hand = hand;
    }

    @Override
    protected void init() {
        int left = width / 2 - 88;
        int top = height / 2 - 83;

        squadronName = new EditBox(font, left + 20, top + 34, 136, 18, Component.literal("Squadron"));
        squadronName.setMaxLength(200);
        addRenderableWidget(squadronName);

        addRenderableWidget(Button.builder(Component.literal("Set Item Squadron"),
            b -> send(C2SSquadronActionPacket.Action.SET_ITEM_SQUADRON, 0))
            .bounds(left + 20, top + 58, 136, 20).build());

        addRenderableWidget(Button.builder(Component.literal("Assign Nearby NPCs"),
            b -> send(C2SSquadronActionPacket.Action.ASSIGN_NEARBY, 24))
            .bounds(left + 20, top + 82, 136, 20).build());

        addRenderableWidget(Button.builder(Component.literal("Clear Nearby NPCs"),
            b -> send(C2SSquadronActionPacket.Action.CLEAR_NEARBY, 24))
            .bounds(left + 20, top + 106, 136, 20).build());

        addRenderableWidget(Button.builder(Component.literal("Follow"),
            b -> sendOrder(GOTHiredOrder.FOLLOW, 48))
            .bounds(left + 20, top + 130, 42, 20).build());

        addRenderableWidget(Button.builder(Component.literal("Hold"),
            b -> sendOrder(GOTHiredOrder.HOLD, 48))
            .bounds(left + 67, top + 130, 42, 20).build());

        addRenderableWidget(Button.builder(Component.literal("Patrol"),
            b -> sendOrder(GOTHiredOrder.PATROL, 48))
            .bounds(left + 114, top + 130, 42, 20).build());
    }

    private void send(C2SSquadronActionPacket.Action action, int radius) {
        GOTNetwork.CHANNEL.sendToServer(new C2SSquadronActionPacket(
            hand, action, squadronName.getValue(), null, radius
        ));
    }

    private void sendOrder(GOTHiredOrder order, int radius) {
        GOTNetwork.CHANNEL.sendToServer(new C2SSquadronActionPacket(
            hand, C2SSquadronActionPacket.Action.COMMAND_GROUP, squadronName.getValue(), order, radius
        ));
    }

    @Override
    public void render(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
        renderBackground(g);
        int left = width / 2 - 88;
        int top = height / 2 - 83;

        // Use original texture when present; otherwise the UI is still usable.
        try {
            g.blit(TEX, left, top, 0, 0, 176, 166);
        } catch (Exception ignored) {}

        g.drawCenteredString(font, title, width / 2, top + 12, 0xFFFFFF);
        g.drawString(font, "Squadron name", left + 20, top + 22, 0x404040, false);

        super.render(g, mouseX, mouseY, partialTick);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
