package got;

import got.network.C2SRenameSmithingItemPacket;

import got.network.C2SEngraveOwnerPacket;

import got.network.C2SReforgeItemPacket;
import got.network.GOTNetwork;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public final class GOTSmithingScreen extends AbstractContainerScreen<GOTSmithingMenu> {
    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath("minecraft", "textures/gui/container/anvil.png");

    private Button reforgeButton;
    private Button engraveButton;
    private Button renameButton;
    private EditBox nameField;

    public GOTSmithingScreen(GOTSmithingMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
        imageWidth = 176;
        imageHeight = 166;
        inventoryLabelY = 72;
    }


    @Override
    protected void init() {
        super.init();
        reforgeButton = addRenderableWidget(Button.builder(
                Component.translatable("got.container.anvil.reforge"),
                button -> GOTNetwork.CHANNEL.sendToServer(new C2SReforgeItemPacket()))
                .bounds(leftPos + 104, topPos + 58, 64, 18)
                .build());
        reforgeButton.active = menu.canReforge();

        nameField = new EditBox(font, leftPos + 8, topPos + 7, 108, 18,
                Component.translatable("container.got.smithing.name"));
        nameField.setMaxLength(50);
        addRenderableWidget(nameField);

        renameButton = addRenderableWidget(Button.builder(
                Component.translatable("container.got.smithing.rename"),
                button -> GOTNetwork.CHANNEL.sendToServer(
                        new C2SRenameSmithingItemPacket(nameField.getValue())))
                .bounds(leftPos + 118, topPos + 7, 50, 18)
                .build());

        engraveButton = addRenderableWidget(Button.builder(
                Component.translatable("got.container.anvil.engraveOwner"),
                button -> GOTNetwork.CHANNEL.sendToServer(new C2SEngraveOwnerPacket()))
                .bounds(leftPos + 8, topPos + 58, 64, 18)
                .build());
        engraveButton.active = false;
    }

    @Override
    protected void containerTick() {
        super.containerTick();
        if (reforgeButton != null) {
            reforgeButton.active = menu.canReforge();
            reforgeButton.setMessage(menu.reforgeCost() > 0
                    ? Component.translatable("got.container.anvil.reforge_cost", menu.reforgeCost())
                    : Component.translatable("got.container.anvil.reforge"));
        }
        if (engraveButton != null && minecraft != null && minecraft.player != null) {
            engraveButton.active = menu.canEngraveOwner(minecraft.player);
            engraveButton.setMessage(menu.engraveOwnerCost() > 0
                    ? Component.translatable("container.got.smithing.engrave_cost", menu.engraveOwnerCost())
                    : Component.translatable("got.container.anvil.engraveOwner"));
        }
        if (renameButton != null) renameButton.active = menu.canRename();
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
        RenderSystem.setShaderColor(1,1,1,1);
        graphics.blit(TEXTURE, leftPos, topPos, 0, 0, imageWidth, imageHeight);

        // Third input slot is unique to GOT smithing.
        graphics.fill(leftPos + 117, topPos + 26, leftPos + 135, topPos + 44, 0xFF8B8B8B);
        graphics.fill(leftPos + 118, topPos + 27, leftPos + 134, topPos + 43, 0xFF373737);
    }

    @Override
    protected void renderLabels(GuiGraphics graphics, int mouseX, int mouseY) {
        graphics.drawString(font, Component.translatable("container.got.smithing"),
                titleLabelX, titleLabelY, 0x404040, false);
        graphics.drawString(font, Component.translatable("container.inventory"),
                inventoryLabelX, inventoryLabelY, 0x404040, false);

        if (menu.combiningScrolls()) {
            graphics.drawString(font,
                    Component.translatable("container.got.smithing.combine_scrolls"),
                    8, 59, 0x404040, false);
        } else if (menu.materialCost() > 0) {
            graphics.drawString(font,
                    Component.translatable("container.got.smithing.material_cost", menu.materialCost()),
                    8, 59, 0x404040, false);
        }
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        renderBackground(graphics);
        super.render(graphics, mouseX, mouseY, partialTick);
        renderTooltip(graphics, mouseX, mouseY);
    }
}
