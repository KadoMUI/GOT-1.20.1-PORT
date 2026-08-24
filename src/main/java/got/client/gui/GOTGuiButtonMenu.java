package got.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;

/**
 * 1.20.1 port of the original 1.7.10 GOTGuiButtonMenu.
 *
 * The original texture atlas is arranged as 32x32 icons with three columns:
 * normal, hovered, disabled. The button id selects the 32px row.
 */
public final class GOTGuiButtonMenu extends AbstractButton {
    public static final int SIZE = 32;

    private final int iconId;
    private final int menuKeyCode;
    private final ScreenFactory screenFactory;

    public GOTGuiButtonMenu(int iconId, int x, int y, ScreenFactory screenFactory, Component tooltip, int menuKeyCode) {
        super(x, y, SIZE, SIZE, tooltip);
        this.iconId = iconId;
        this.screenFactory = screenFactory;
        this.menuKeyCode = menuKeyCode;
    }

    @Override
    public void onPress() {
        if (!active || screenFactory == null) {
            return;
        }

        Minecraft minecraft = Minecraft.getInstance();
        minecraft.setScreen(screenFactory.create());
    }

    @Override
    protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        int textureX;
        if (!active) {
            textureX = SIZE * 2;
        } else if (isHoveredOrFocused()) {
            textureX = SIZE;
        } else {
            textureX = 0;
        }

        graphics.blit(
                GOTGuiMenu.MENU_ICONS_TEXTURE,
                getX(),
                getY(),
                textureX,
                iconId * SIZE,
                SIZE,
                SIZE,
                256,
                256
        );
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput output) {
        defaultButtonNarrationText(output);
    }

    public int getMenuKeyCode() {
        return menuKeyCode;
    }

    @FunctionalInterface
    public interface ScreenFactory {
        net.minecraft.client.gui.screens.Screen create();
    }
}
