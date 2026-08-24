package got.client.gui.hiring;

import got.menu.hiring.GOTContainerHiredFarmerInventory;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

/**
 * Simple modern wrapper for the hired farmer container.
 */
public final class GOTGuiHiredFarmerInventory extends AbstractContainerScreen<GOTContainerHiredFarmerInventory> {
    private static final ResourceLocation TEX =
        new ResourceLocation("minecraft", "textures/gui/container/generic_54.png");

    public GOTGuiHiredFarmerInventory(
        GOTContainerHiredFarmerInventory menu,
        Inventory inventory,
        Component title
    ) {
        super(menu, inventory, title);
        this.imageWidth = 176;
        this.imageHeight = 166;
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
