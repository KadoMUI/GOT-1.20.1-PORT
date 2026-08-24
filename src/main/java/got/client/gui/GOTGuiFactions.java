package got.client.gui;

import got.client.faction.GOTClientFactionState;
import got.faction.GOTFaction;
import got.faction.GOTFactionPlayerData;
import got.faction.GOTFactionRank;
import got.faction.GOTFactionRelation;
import got.faction.GOTFactionService;
import got.network.C2SFactionMembershipPacket;
import got.network.C2SRequestFactionDataPacket;
import got.network.GOTNetwork;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextColor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;

import java.util.ArrayList;
import java.util.List;

/** Modern faction/alignment/membership screen using the original GUI atlases. */
public final class GOTGuiFactions extends GOTGuiMenuBaseReturn {
    private static final ResourceLocation FACTIONS_TEXTURE =
            ResourceLocation.fromNamespaceAndPath("got", "textures/gui/factions.png");
    private static final ResourceLocation FACTIONS_TEXTURE_FULL =
            ResourceLocation.fromNamespaceAndPath("got", "textures/gui/factions_full.png");
    private static final List<GOTFaction> FACTIONS = GOTFaction.playableFactions();
    private static int selectedIndex = Math.max(0, FACTIONS.indexOf(GOTFaction.NORTH));

    private Page page = Page.OVERVIEW;
    private Confirm confirm = Confirm.NONE;
    private Button previous;
    private Button next;
    private Button overview;
    private Button relations;
    private Button ranks;
    private Button confirmButton;
    private Button cancelButton;
    private GOTGuiButtonPledge membershipButton;

    public GOTGuiFactions() {
        super(Component.translatable("got.gui.factions"));
        sizeX = 256;
        sizeY = 230;
    }

    @Override
    protected void init() {
        super.init();
        int panelY = guiTop + 42;
        previous = addRenderableWidget(Button.builder(Component.literal("<"), button -> select(-1))
                .bounds(guiLeft + 8, panelY + 104, 20, 20).build());
        next = addRenderableWidget(Button.builder(Component.literal(">"), button -> select(1))
                .bounds(guiLeft + 228, panelY + 104, 20, 20).build());

        overview = addRenderableWidget(Button.builder(Component.translatable("got.faction.page.overview"),
                        button -> setPage(Page.OVERVIEW))
                .bounds(guiLeft + 7, guiTop + 176, 78, 20).build());
        relations = addRenderableWidget(Button.builder(Component.translatable("got.faction.page.relations"),
                        button -> setPage(Page.RELATIONS))
                .bounds(guiLeft + 89, guiTop + 176, 78, 20).build());
        ranks = addRenderableWidget(Button.builder(Component.translatable("got.faction.page.ranks"),
                        button -> setPage(Page.RANKS))
                .bounds(guiLeft + 171, guiTop + 176, 78, 20).build());

        membershipButton = addRenderableWidget(new GOTGuiButtonPledge(
                guiLeft + 14, panelY + 86, Component.translatable("got.faction.membership"),
                () -> GOTClientFactionState.membership() == selected(),
                button -> beginMembershipAction()));

        confirmButton = addRenderableWidget(Button.builder(Component.translatable("gui.yes"),
                        button -> confirmMembershipAction())
                .bounds(guiLeft + 62, panelY + 96, 62, 20).build());
        cancelButton = addRenderableWidget(Button.builder(Component.translatable("gui.no"),
                        button -> confirm = Confirm.NONE)
                .bounds(guiLeft + 132, panelY + 96, 62, 20).build());

        GOTNetwork.CHANNEL.sendToServer(new C2SRequestFactionDataPacket());
        updateButtons();
    }

    private GOTFaction selected() {
        return FACTIONS.get(Math.floorMod(selectedIndex, FACTIONS.size()));
    }

    private void select(int direction) {
        selectedIndex = Math.floorMod(selectedIndex + direction, FACTIONS.size());
        confirm = Confirm.NONE;
        updateButtons();
    }

    private void setPage(Page page) {
        this.page = page;
        this.confirm = Confirm.NONE;
        updateButtons();
    }

    private void beginMembershipAction() {
        if (GOTClientFactionState.membership() == selected()) confirm = Confirm.LEAVE;
        else confirm = Confirm.JOIN;
        updateButtons();
    }

    private void confirmMembershipAction() {
        if (confirm == Confirm.JOIN) {
            GOTNetwork.CHANNEL.sendToServer(C2SFactionMembershipPacket.join(selected()));
        } else if (confirm == Confirm.LEAVE) {
            GOTNetwork.CHANNEL.sendToServer(C2SFactionMembershipPacket.leave());
        }
        confirm = Confirm.NONE;
        GOTNetwork.CHANNEL.sendToServer(new C2SRequestFactionDataPacket());
        updateButtons();
    }

    private void updateButtons() {
        if (previous == null) return;
        boolean normal = confirm == Confirm.NONE;
        previous.visible = next.visible = normal;
        overview.visible = relations.visible = ranks.visible = normal;
        membershipButton.visible = normal && page == Page.OVERVIEW;
        confirmButton.visible = cancelButton.visible = !normal;

        overview.active = page != Page.OVERVIEW;
        relations.active = page != Page.RELATIONS;
        ranks.active = page != Page.RANKS;

        GOTFaction membership = GOTClientFactionState.membership();
        boolean isMemberHere = membership == selected();
        boolean mayJoin = membership == GOTFaction.UNALIGNED
                && GOTClientFactionState.pledgeBreakCooldown() <= 0
                && GOTClientFactionState.alignment(selected()) >= selected().pledgeAlignment();
        membershipButton.active = isMemberHere || mayJoin;
        membershipButton.setMessage(Component.translatable(isMemberHere
                ? "got.faction.leave.button" : "got.faction.join.button"));
    }

    @Override
    public void tick() {
        super.tick();
        updateButtons();
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        renderBackground(graphics);
        int panelY = guiTop + 42;
        ResourceLocation pageTexture = confirm == Confirm.NONE ? FACTIONS_TEXTURE : FACTIONS_TEXTURE_FULL;
        graphics.blit(pageTexture, guiLeft, panelY, 0, 0, 256, 128, 256, 256);

        GOTFaction faction = selected();
        Component heading = faction.displayName().copy().withStyle(style -> style.withColor(TextColor.fromRgb(faction.color())));
        graphics.drawCenteredString(font, heading, width / 2, guiTop + 3, 0xFFFFFF);
        graphics.drawCenteredString(font, faction.subtitle(), width / 2, guiTop + 16, 0xD5C7A3);
        drawAlignmentBar(graphics, faction, guiLeft + 16, guiTop + 29, 224);

        if (confirm == Confirm.NONE) {
            switch (page) {
                case OVERVIEW -> renderOverview(graphics, faction, panelY, mouseX, mouseY);
                case RELATIONS -> renderRelations(graphics, faction, panelY);
                case RANKS -> renderRanks(graphics, faction, panelY);
            }
        } else {
            renderConfirmation(graphics, faction, panelY);
        }

        super.render(graphics, mouseX, mouseY, partialTick);
        if (membershipButton.visible && membershipButton.isHovered()) {
            graphics.renderTooltip(font, membershipButton.getMessage(), mouseX, mouseY);
        }
    }

    private void renderOverview(GuiGraphics graphics, GOTFaction faction, int panelY, int mouseX, int mouseY) {
        int x = guiLeft + 16;
        int y = panelY + 14;
        float alignment = GOTClientFactionState.alignment(faction);
        graphics.drawString(font, Component.translatable("got.gui.factions.alignment"), x, y, 0x7A5A32, false);
        graphics.drawString(font, GOTFactionService.formatAlignment(alignment), x + 68, y,
                alignment < 0.0F ? 0xB52B2B : 0x2F7D32, false);
        y += 13;
        graphics.drawString(font, faction.rankFor(alignment).displayName(faction), x, y, 0x7A5A32, false);
        y += 16;

        GOTFactionPlayerData.FactionStats stats = GOTClientFactionState.stats(faction);
        graphics.drawString(font, Component.translatable("got.gui.factions.data.enemiesKilled", stats.enemyKills()), x, y, 0x7A5A32, false);
        y += 11;
        graphics.drawString(font, Component.translatable("got.gui.factions.data.npcsKilled", stats.npcKills()), x, y, 0x7A5A32, false);

        GOTFaction membership = GOTClientFactionState.membership();
        Component status;
        if (membership == faction) status = Component.translatable("got.faction.membership.current").withStyle(ChatFormatting.DARK_GREEN);
        else if (membership != GOTFaction.UNALIGNED) status = Component.translatable("got.faction.membership.other", membership.displayName()).withStyle(ChatFormatting.DARK_RED);
        else if (GOTClientFactionState.pledgeBreakCooldown() > 0) status = Component.translatable("got.faction.membership.cooldown",
                GOTFactionService.formatTicks(GOTClientFactionState.pledgeBreakCooldown())).withStyle(ChatFormatting.DARK_RED);
        else if (alignment < faction.pledgeAlignment()) status = Component.translatable("got.faction.membership.requires",
                GOTFactionService.formatAlignment(faction.pledgeAlignment())).withStyle(ChatFormatting.DARK_GRAY);
        else status = Component.translatable("got.faction.membership.none").withStyle(ChatFormatting.DARK_GRAY);
        graphics.drawString(font, status, guiLeft + 52, panelY + 103, 0xFFFFFF, false);
    }

    private void renderRelations(GuiGraphics graphics, GOTFaction faction, int panelY) {
        int x = guiLeft + 16;
        int y = panelY + 13;
        y = drawRelationGroup(graphics, faction, GOTFactionRelation.ALLY, x, y);
        y = drawRelationGroup(graphics, faction, GOTFactionRelation.FRIEND, x, y);
        y = drawRelationGroup(graphics, faction, GOTFactionRelation.ENEMY, guiLeft + 132, panelY + 13);
        drawRelationGroup(graphics, faction, GOTFactionRelation.MORTAL_ENEMY, guiLeft + 132, y);
    }

    private int drawRelationGroup(GuiGraphics graphics, GOTFaction faction, GOTFactionRelation relation, int x, int y) {
        List<GOTFaction> related = faction.factionsOfRelation(relation);
        if (related.isEmpty()) return y;
        graphics.drawString(font, relation.displayName().copy().append(":"), x, y, 0x7A5A32, false);
        y += 10;
        int shown = 0;
        for (GOTFaction other : related) {
            if (shown++ >= 7) {
                graphics.drawString(font, Component.literal("…"), x + 4, y, 0x7A5A32, false);
                y += 9;
                break;
            }
            graphics.drawString(font, Component.literal("• ").append(other.displayName()), x + 4, y, other.color(), false);
            y += 9;
        }
        return y + 3;
    }

    private void renderRanks(GuiGraphics graphics, GOTFaction faction, int panelY) {
        int x = guiLeft + 18;
        int y = panelY + 14;
        graphics.drawString(font, Component.translatable("got.gui.factions.rankHeader"), x, y, 0x7A5A32, false);
        y += 13;
        List<GOTFactionRank> ranks = new ArrayList<>(faction.ranksDescending());
        java.util.Collections.reverse(ranks);
        for (GOTFactionRank rank : ranks) {
            Component line = Component.literal("• ").append(rank.displayName(faction)).append("  ")
                    .append(Component.literal(GOTFactionService.formatAlignment(rank.alignment())).withStyle(ChatFormatting.DARK_GREEN));
            graphics.drawString(font, line, x, y, 0x7A5A32, false);
            y += 14;
        }
    }

    private void renderConfirmation(GuiGraphics graphics, GOTFaction faction, int panelY) {
        Component text;
        if (confirm == Confirm.JOIN) {
            text = Component.translatable("got.faction.join.confirm", faction.displayName());
        } else {
            text = Component.translatable("got.faction.leave.confirm", faction.displayName());
        }
        int y = panelY + 17;
        for (FormattedCharSequence line : font.split(text, 218)) {
            graphics.drawString(font, line, guiLeft + 19, y, 0x7A5A32, false);
            y += 11;
        }
    }

    private void drawAlignmentBar(GuiGraphics graphics, GOTFaction faction, int x, int y, int width) {
        float value = GOTClientFactionState.alignment(faction);
        graphics.fill(x, y, x + width, y + 7, 0xFF1C1712);
        graphics.fill(x + 1, y + 1, x + width - 1, y + 6, 0xFFB9A785);
        int center = x + width / 5;
        graphics.fill(center, y, center + 1, y + 7, 0xFFFFFFFF);
        float normalized = value < 0.0F
                ? Math.max(-1.0F, value / 100.0F) * 0.2F
                : Math.min(1.0F, value / 1000.0F) * 0.8F;
        int end = center + Math.round(normalized * width);
        graphics.fill(Math.min(center, end), y + 1, Math.max(center, end), y + 6,
                value < 0.0F ? 0xFFB52B2B : (0xFF000000 | faction.color()));
        graphics.drawCenteredString(font, GOTFactionService.formatAlignment(value), x + width / 2, y - 9, 0xFFFFFF);
    }

    private enum Page { OVERVIEW, RELATIONS, RANKS }
    private enum Confirm { NONE, JOIN, LEAVE }
}
