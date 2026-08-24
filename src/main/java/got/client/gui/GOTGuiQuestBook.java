package got.client.gui;

import got.client.quest.GOTClientQuestState;
import got.network.C2SQuestActionPacket;
import got.network.C2SRequestQuestDataPacket;
import got.network.GOTNetwork;
import got.quest.GOTQuestState;
import got.quest.GOTQuestView;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;

/** Functional Scarlet Book journal using the original 420x256 artwork. */
public final class GOTGuiQuestBook extends Screen {
    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath("got", "textures/gui/quest/questBook.png");
    private static final int PAGE_COLOR = 0x7A5C3E;
    private boolean completed;
    private int selectedIndex = -1;
    private int scroll;
    private int guiLeft;
    private int guiTop;
    private Button trackButton;
    private Button abandonButton;

    public GOTGuiQuestBook() {
        super(Component.translatable("got.gui.redBook.page.miniquests"));
    }

    @Override
    protected void init() {
        guiLeft = (width - 420) / 2;
        guiTop = (height - 256) / 2;
        addRenderableWidget(Button.builder(Component.translatable("got.gui.redBook.mq.viewActive"), button -> {
            completed = false;
            selectedIndex = -1;
            scroll = 0;
            updateButtons();
        }).bounds(guiLeft + 222, guiTop + 18, 78, 18).build());
        addRenderableWidget(Button.builder(Component.translatable("got.gui.redBook.mq.viewComplete"), button -> {
            completed = true;
            selectedIndex = -1;
            scroll = 0;
            updateButtons();
        }).bounds(guiLeft + 304, guiTop + 18, 78, 18).build());
        trackButton = addRenderableWidget(Button.builder(Component.translatable("got.quest.track"), button -> {
            GOTQuestView selected = selected();
            if (selected != null && selected.instanceId() != null) {
                GOTNetwork.CHANNEL.sendToServer(C2SQuestActionPacket.track(selected.instanceId()));
            }
        }).bounds(guiLeft + 22, guiTop + 228, 72, 18).build());
        abandonButton = addRenderableWidget(Button.builder(Component.translatable("got.quest.abandon"), button -> {
            GOTQuestView selected = selected();
            if (selected != null && selected.instanceId() != null) {
                GOTNetwork.CHANNEL.sendToServer(C2SQuestActionPacket.abandon(selected.instanceId()));
                selectedIndex = -1;
                updateButtons();
            }
        }).bounds(guiLeft + 100, guiTop + 228, 72, 18).build());
        updateButtons();
        GOTNetwork.CHANNEL.sendToServer(new C2SRequestQuestDataPacket());
    }

    private List<GOTQuestView> quests() {
        return completed ? GOTClientQuestState.archive() : GOTClientQuestState.active();
    }

    private GOTQuestView selected() {
        List<GOTQuestView> quests = quests();
        return selectedIndex >= 0 && selectedIndex < quests.size() ? quests.get(selectedIndex) : null;
    }

    private void updateButtons() {
        if (trackButton == null || abandonButton == null) return;
        boolean enabled = !completed && selected() != null;
        trackButton.active = enabled;
        abandonButton.active = enabled;
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        renderBackground(graphics);
        graphics.blit(TEXTURE, guiLeft, guiTop, 0, 0, 420, 256, 512, 512);
        graphics.drawCenteredString(font, title, guiLeft + 107, guiTop + 24, PAGE_COLOR);
        graphics.drawCenteredString(font, Component.translatable(completed
                        ? "got.gui.redBook.mq.viewComplete" : "got.gui.redBook.mq.viewActive"),
                guiLeft + 303, guiTop + 43, PAGE_COLOR);

        renderSelected(graphics);
        renderQuestList(graphics, mouseX, mouseY);
        super.render(graphics, mouseX, mouseY, partialTick);
    }

    private void renderSelected(GuiGraphics graphics) {
        GOTQuestView quest = selected();
        if (quest == null) {
            graphics.drawCenteredString(font, Component.translatable("got.quest.book.summary",
                            GOTClientQuestState.active().size(), GOTClientQuestState.archive().size()),
                    guiLeft + 107, guiTop + 112, PAGE_COLOR);
            return;
        }
        int x = guiLeft + 28;
        int y = guiTop + 43;
        Item icon = ForgeRegistries.ITEMS.getValue(quest.icon());
        if (icon != null) graphics.renderItem(new ItemStack(icon), x, y - 3);
        graphics.drawString(font, Component.translatable(quest.titleKey()), x + 20, y, PAGE_COLOR, false);
        y += 18;
        graphics.drawString(font, Component.literal(quest.giverName()).withStyle(ChatFormatting.ITALIC),
                x, y, PAGE_COLOR, false);
        y += 14;
        List<net.minecraft.util.FormattedCharSequence> description = font.split(
                Component.translatable(quest.descriptionKey()), 154);
        for (int index = 0; index < Math.min(6, description.size()); index++) {
            graphics.drawString(font, description.get(index), x, y, PAGE_COLOR, false);
            y += font.lineHeight;
        }
        y += 4;
        for (GOTQuestView.ObjectiveView objective : quest.objectives()) {
            Component line = Component.translatable(objective.labelKey(), objective.progress(), objective.target())
                    .copy().append("  " + objective.progress() + "/" + objective.target());
            graphics.drawString(font, line, x, y,
                    objective.progress() >= objective.target() ? 0x3A6B35 : PAGE_COLOR, false);
            y += font.lineHeight + 2;
            if (y > guiTop + 214) break;
        }
        if (quest.state() == GOTQuestState.FAILED) {
            graphics.drawString(font, Component.translatable("got.quest.failed"), x, guiTop + 210,
                    0xAA2222, false);
        }
    }

    private void renderQuestList(GuiGraphics graphics, int mouseX, int mouseY) {
        List<GOTQuestView> quests = quests();
        int start = Math.min(scroll, Math.max(0, quests.size() - 4));
        for (int visible = 0; visible < 4 && start + visible < quests.size(); visible++) {
            int index = start + visible;
            GOTQuestView quest = quests.get(index);
            int x = guiLeft + 222;
            int y = guiTop + 54 + visible * 45;
            boolean hovered = mouseX >= x && mouseX < x + 170 && mouseY >= y && mouseY < y + 41;
            if (index == selectedIndex) graphics.fill(x, y, x + 170, y + 41, 0x357A5C3E);
            else if (hovered) graphics.fill(x, y, x + 170, y + 41, 0x207A5C3E);
            Item icon = ForgeRegistries.ITEMS.getValue(quest.icon());
            if (icon != null) graphics.renderItem(new ItemStack(icon), x + 3, y + 3);
            graphics.drawString(font, Component.translatable(quest.titleKey()), x + 23, y + 4,
                    PAGE_COLOR, false);
            graphics.drawString(font, Component.literal(quest.giverName()), x + 23, y + 16,
                    0x665044, false);
            GOTQuestView.ObjectiveView objective = quest.currentObjective();
            if (objective != null) {
                graphics.drawString(font, objective.progress() + " / " + objective.target(),
                        x + 23, y + 28, PAGE_COLOR, false);
            } else {
                graphics.drawString(font, Component.translatable("got.quest.no_objective"),
                        x + 23, y + 28, PAGE_COLOR, false);
            }
            if (quest.tracked()) graphics.drawString(font, "*", x + 158, y + 4, 0x2F6D32, false);
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0) {
            int x = guiLeft + 222;
            int start = Math.min(scroll, Math.max(0, quests().size() - 4));
            for (int visible = 0; visible < 4 && start + visible < quests().size(); visible++) {
                int y = guiTop + 54 + visible * 45;
                if (mouseX >= x && mouseX < x + 170 && mouseY >= y && mouseY < y + 41) {
                    selectedIndex = start + visible;
                    updateButtons();
                    return true;
                }
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        int max = Math.max(0, quests().size() - 4);
        scroll = Math.max(0, Math.min(max, scroll + (delta < 0 ? 1 : -1)));
        return true;
    }

    @Override public boolean isPauseScreen() { return false; }
}
