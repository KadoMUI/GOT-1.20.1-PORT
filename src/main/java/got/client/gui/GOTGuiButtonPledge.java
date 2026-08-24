package got.client.gui;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.function.BooleanSupplier;

/** Exact five-state pledge icon from the original alignment.png atlas. */
public final class GOTGuiButtonPledge extends AbstractButton {
    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath("got", "textures/gui/alignment.png");
    private final BooleanSupplier broken;

    public GOTGuiButtonPledge(int x, int y, Component message, BooleanSupplier broken, OnPress onPress) {
        super(x, y, 32, 32, message);
        this.broken = broken;
        this.onPress = onPress;
    }

    private final OnPress onPress;

    @Override
    public void onPress() {
        onPress.onPress(this);
    }

    @Override
    protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        int state;
        if (broken.getAsBoolean()) state = isHoveredOrFocused() ? 4 : 3;
        else if (!active) state = 0;
        else state = isHoveredOrFocused() ? 2 : 1;
        graphics.blit(TEXTURE, getX(), getY(), state * 32, 180, 32, 32, 256, 256);
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput output) {
        defaultButtonNarrationText(output);
    }

    @FunctionalInterface
    public interface OnPress {
        void onPress(GOTGuiButtonPledge button);
    }
}
