package got.client.gui;

import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;

/**
 * Direct modern counterpart of GOTGuiMenuBaseReturn.
 * Adds the original left-side return control and routes it to GOTGuiMenu.
 */
public abstract class GOTGuiMenuBaseReturn extends GOTGuiMenuBase {
    protected Button buttonMenuReturn;

    protected GOTGuiMenuBaseReturn(Component title) {
        super(title);
    }

    protected GOTGuiMenuBaseReturn() {
        super();
    }

    @Override
    protected void init() {
        super.init();
        final int verticalOffset = 20;
        final int preferredX = 35;
        final int gap = 10;
        final int buttonWidth = 70;
        final int buttonHeight = 20;
        int x = Math.min(preferredX, guiLeft - gap - buttonWidth);
        int y = guiTop + (sizeY + verticalOffset) / 4;

        buttonMenuReturn = Button.builder(
                Component.translatable("got.gui.menuButton"),
                button -> {
                    if (minecraft != null) minecraft.setScreen(new GOTGuiMenu());
                })
                .bounds(x, y, buttonWidth, buttonHeight)
                .build();
        addRenderableWidget(buttonMenuReturn);
    }
}
