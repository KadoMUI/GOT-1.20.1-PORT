package got.client.gui;

import got.network.C2SQuestActionPacket;
import got.network.GOTNetwork;
import got.quest.GOTQuestView;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;
import java.util.UUID;

/** Modern rendering of the original 256x200 miniquest offer parchment. */
public final class GOTGuiQuestOffer extends Screen {
    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath("got", "textures/gui/quest/miniquest.png");
    private final GOTQuestView offer;
    private final UUID giverId;
    private int guiLeft;
    private int guiTop;
    private boolean responded;

    public GOTGuiQuestOffer(GOTQuestView offer, UUID giverId) {
        super(Component.translatable(offer.titleKey()));
        this.offer = offer;
        this.giverId = giverId;
    }

    @Override
    protected void init() {
        guiLeft = (width - 256) / 2;
        guiTop = (height - 200) / 2;
        addRenderableWidget(Button.builder(Component.translatable("got.gui.miniquestOffer.accept"), button -> {
            responded = true;
            GOTNetwork.CHANNEL.sendToServer(C2SQuestActionPacket.accept(offer.definitionId(), giverId));
            onClose();
        }).bounds(guiLeft + 28, guiTop + 170, 80, 20).build());
        addRenderableWidget(Button.builder(Component.translatable("got.gui.miniquestOffer.decline"), button -> {
            responded = true;
            GOTNetwork.CHANNEL.sendToServer(C2SQuestActionPacket.decline(offer.definitionId(), giverId));
            onClose();
        }).bounds(guiLeft + 148, guiTop + 170, 80, 20).build());
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        renderBackground(graphics);
        graphics.blit(TEXTURE, guiLeft, guiTop, 0, 0, 256, 200, 256, 256);
        graphics.drawCenteredString(font, offer.giverName(), guiLeft + 128, guiTop + 8, 0x7A5C3E);
        graphics.drawCenteredString(font, title, guiLeft + 128, guiTop + 23, 0x7A5C3E);

        Component description = Component.translatable(offer.offerKey(), offer.giverName());
        List<net.minecraft.util.FormattedCharSequence> lines = font.split(description, 160);
        for (int index = 0; index < Math.min(8, lines.size()); index++) {
            graphics.drawString(font, lines.get(index), guiLeft + 84,
                    guiTop + 40 + index * font.lineHeight, 0x7A5C3E, false);
        }

        GOTQuestView.ObjectiveView objective = offer.currentObjective();
        Component objectiveText = objective == null
                ? Component.translatable("got.quest.no_objective")
                : Component.translatable(objective.labelKey(), objective.progress(), objective.target());
        graphics.drawCenteredString(font, objectiveText, guiLeft + 128, guiTop + 150, 0x7A5C3E);
        Item icon = ForgeRegistries.ITEMS.getValue(offer.icon());
        if (icon != null) {
            ItemStack stack = new ItemStack(icon);
            int textWidth = font.width(objectiveText);
            graphics.renderItem(stack, guiLeft + 128 - textWidth / 2 - 22, guiTop + 146);
            graphics.renderItem(stack, guiLeft + 128 + textWidth / 2 + 6, guiTop + 146);
        }
        super.render(graphics, mouseX, mouseY, partialTick);
    }

    @Override
    public void removed() {
        if (!responded) {
            GOTNetwork.CHANNEL.sendToServer(C2SQuestActionPacket.decline(offer.definitionId(), giverId));
            responded = true;
        }
        super.removed();
    }

    @Override public boolean isPauseScreen() { return false; }
}
