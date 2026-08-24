package got.client.gui;

import got.client.GOTKeyMappings;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

/**
 * Modern base for the original GOT menu-family screens.
 * Preserves the original 200x256 default panel and centering contract.
 */
public abstract class GOTGuiMenuBase extends Screen {
    protected int sizeX = 200;
    protected int sizeY = 256;
    protected int guiLeft;
    protected int guiTop;

    protected GOTGuiMenuBase(Component title) {
        super(title);
    }

    protected GOTGuiMenuBase() {
        this(Component.empty());
    }

    @Override
    protected void init() {
        super.init();
        guiLeft = (width - sizeX) / 2;
        guiTop = (height - sizeY) / 2;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (GOTKeyMappings.OPEN_MENU.matches(keyCode, scanCode)) {
            if (minecraft != null) minecraft.setScreen(new GOTGuiMenu());
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    protected int getSizeX() {
        return sizeX;
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
