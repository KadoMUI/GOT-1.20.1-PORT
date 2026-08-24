package got.client.gui;

import got.common.world.map.GOTWaypoint;
import got.network.C2SFastTravelPacket;
import got.network.GOTNetwork;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public final class GOTGuiFastTravel extends Screen {
    private final Screen parent;
    private final GOTWaypoint waypoint;

    public GOTGuiFastTravel(Screen parent, GOTWaypoint waypoint) {
        super(Component.translatable("got.fastTravel.title"));
        this.parent = parent;
        this.waypoint = waypoint;
    }

    @Override
    protected void init() {
        int centerX = width / 2;
        int centerY = height / 2;

        addRenderableWidget(Button.builder(Component.translatable("got.fastTravel.travel"), button -> confirmTravel())
                .bounds(centerX - 102, centerY + 28, 100, 20)
                .build());
        addRenderableWidget(Button.builder(Component.translatable("gui.cancel"), button -> onClose())
                .bounds(centerX + 2, centerY + 28, 100, 20)
                .build());
    }

    private void confirmTravel() {
        GOTNetwork.CHANNEL.sendToServer(new C2SFastTravelPacket(waypoint.getID()));
        if (minecraft != null) minecraft.setScreen(null);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        renderBackground(graphics);
        int left = width / 2 - 120;
        int top = height / 2 - 62;
        int right = width / 2 + 120;
        int bottom = height / 2 + 58;

        graphics.fill(left - 3, top - 3, right + 3, bottom + 3, 0xFF2C0D08);
        graphics.fill(left, top, right, bottom, 0xFFD0AD79);
        graphics.drawCenteredString(font, title, width / 2, top + 12, 0xFF4A170E);
        graphics.drawCenteredString(font, waypoint.getDisplayName(), width / 2, top + 34, 0xFF3A120A);
        graphics.drawCenteredString(font,
                Component.literal("X: " + waypoint.getCoordX() + ", Z: " + waypoint.getCoordZ()),
                width / 2, top + 49, 0xFF5A2A18);
        graphics.drawCenteredString(font, Component.translatable("got.fastTravel.confirm"),
                width / 2, top + 67, 0xFF4A170E);
        super.render(graphics, mouseX, mouseY, partialTick);
    }

    @Override
    public void onClose() {
        if (minecraft != null) minecraft.setScreen(parent);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
