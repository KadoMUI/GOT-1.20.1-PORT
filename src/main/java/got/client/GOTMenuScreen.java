package got.client;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public final class GOTMenuScreen extends Screen {
    public GOTMenuScreen() { super(Component.translatable("screen.got.menu")); }
    @Override protected void init() {
        int x = width / 2 - 100;
        int y = height / 2 - 50;
        addRenderableWidget(Button.builder(Component.translatable("screen.got.map"), b -> minecraft.setScreen(new GOTMapScreen(this))).bounds(x, y, 200, 20).build());
        Button factions = Button.builder(Component.translatable("screen.got.factions"), b -> {}).bounds(x, y + 25, 200, 20).build(); factions.active = false; addRenderableWidget(factions);
        Button quests = Button.builder(Component.translatable("screen.got.quests"), b -> {}).bounds(x, y + 50, 200, 20).build(); quests.active = false; addRenderableWidget(quests);
    }
    @Override public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        renderBackground(graphics);
        graphics.drawCenteredString(font, title, width / 2, height / 2 - 85, 0xFFFFFF);
        super.render(graphics, mouseX, mouseY, partialTick);
    }
}
