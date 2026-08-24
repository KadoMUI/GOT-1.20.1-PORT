package got.client.gui.hiring;

import got.menu.hiring.GOTContainerHiredWarriorInventory;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

/**
 * Simple modern wrapper for the hired warrior container.
 *
 * If the exact original inventory texture exists in the 1.7.10 assets, swap the
 * texture constant to that file during the final fidelity pass.
 */
public final class GOTGuiHiredWarriorInventory extends AbstractContainerScreen<GOTContainerHiredWarriorInventory> {
    private static final ResourceLocation TEX =
        new ResourceLocation("minecraft", "textures/gui/container/generic_54.png");

    public GOTGuiHiredWarriorInventory(
        GOTContainerHiredWarriorInventory menu,
        Inventory inventory,
        Component title
    ) {
        super(menu, inventory, title);
        this.imageWidth = 176;
        this.imageHeight = 186;
    }

    @Override
    protected void renderBg(GuiGraphics g, float partialTick, int mouseX, int mouseY) {
        g.blit(TEX, leftPos, topPos, 0, 0, imageWidth, imageHeight);
    }

    @Override
    public void render(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
        renderBackground(g);
        super.render(g, mouseX, mouseY, partialTick);
        renderTooltip(g, mouseX, mouseY);
    }
}
