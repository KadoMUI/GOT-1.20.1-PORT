package got.client;

import got.GOTCoinExchangeMenu;
import got.GOTMod;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public final class GOTCoinExchangeScreen extends AbstractContainerScreen<GOTCoinExchangeMenu> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(GOTMod.MOD_ID, "textures/gui/coin_exchange.png");
    private Button lower, higher;

    public GOTCoinExchangeScreen(GOTCoinExchangeMenu menu, Inventory inv, Component title) {
        super(menu, inv, title);
        imageHeight = 188;
        inventoryLabelY = 94;
    }

    @Override protected void init() {
        super.init();
        int cx = leftPos + imageWidth / 2;
        lower = addRenderableWidget(Button.builder(Component.literal("<"), b -> press(0)).bounds(cx - 44, topPos + 40, 32, 20).build());
        higher = addRenderableWidget(Button.builder(Component.literal(">"), b -> press(1)).bounds(cx + 12, topPos + 40, 32, 20).build());
        refreshButtons();
    }

    private void press(int id) {
        if (minecraft != null && minecraft.gameMode != null) minecraft.gameMode.handleInventoryButtonClick(menu.containerId, id);
    }

    private void refreshButtons() {
        if (lower != null) lower.active = menu.canBreakDown();
        if (higher != null) higher.active = menu.canConsolidate();
    }

    @Override public void containerTick() { super.containerTick(); refreshButtons(); }

    @Override protected void renderBg(GuiGraphics g, float partial, int mouseX, int mouseY) {
        g.blit(TEXTURE, leftPos, topPos, 0, 0, imageWidth, imageHeight);
    }

    @Override protected void renderLabels(GuiGraphics g, int mouseX, int mouseY) {
        g.drawCenteredString(font, title, imageWidth / 2, 11, 0x404040);
        g.drawString(font, playerInventoryTitle, 8, inventoryLabelY, 0x404040, false);
    }

    @Override public void render(GuiGraphics g, int mouseX, int mouseY, float partial) {
        renderBackground(g);
        super.render(g, mouseX, mouseY, partial);
        renderTooltip(g, mouseX, mouseY);
    }
}
