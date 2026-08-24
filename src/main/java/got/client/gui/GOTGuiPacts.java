package got.client.gui;

import got.client.pact.ClientGOTPactData;
import got.network.C2SPactActionPacket;
import got.network.C2SRequestPactDataPacket;
import got.network.GOTNetwork;
import got.network.S2CPactDataPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.UUID;

/** Pacts Pass 3: legacy visual shell + live server-backed Pact behaviour. */
public final class GOTGuiPacts extends GOTGuiMenuBaseReturn {
    public static final ResourceLocation ICONS_TEXTURES = ResourceLocation.fromNamespaceAndPath("got", "textures/gui/fellowships.png");
    private static final int WHITE = 0xFFFFFF, GREY = 0xA0A0A0, GREEN = 0x55FF55, RED = 0xFF5555, GOLD = 0xFFAA00;
    private static final int PANEL = 0x88000000, PANEL_EDGE = 0x88706040, ROW = 0x44000000, ROW_SELECTED = 0x665F452E;

    private Page page = Page.LIST;
    private EditBox textFieldName, textFieldPlayer, textFieldRename;
    private UUID selectedMember;
    private Button buttonRemoveMember, buttonAdminMember, buttonTransferOwner;
    private String resultText = "";

    public GOTGuiPacts() {
        super(Component.translatable("got.gui.pacts.title"));
        this.sizeX = 220; this.sizeY = 256;
    }

    @Override protected void init() {
        super.init();
        GOTNetwork.CHANNEL.sendToServer(new C2SRequestPactDataPacket());
        rebuildPageWidgets();
    }

    @Override public void tick() {
        super.tick();
        if (textFieldName != null) textFieldName.tick();
        if (textFieldPlayer != null) textFieldPlayer.tick();
        if (textFieldRename != null) textFieldRename.tick();
    }

    private void rebuildPageWidgets() {
        clearWidgets(); super.init();
        textFieldName = textFieldPlayer = textFieldRename = null;
        buttonRemoveMember = buttonAdminMember = buttonTransferOwner = null;
        int cx = guiLeft + sizeX / 2, bottom = guiTop + sizeY - 46;
        var snap = ClientGOTPactData.snapshot();
        var pact = snap.pact();

        switch (page) {
            case LIST -> {
                if (!snap.inPact()) addRenderableWidget(Button.builder(Component.translatable("got.gui.pacts.create"), b -> setPage(Page.CREATE)).bounds(guiLeft + 10, bottom, 96, 20).build());
                if (snap.inPact()) addRenderableWidget(Button.builder(Component.literal("Open Pact"), b -> setPage(Page.PACT)).bounds(guiLeft + 10, bottom, 96, 20).build());
                addRenderableWidget(Button.builder(Component.translatable("got.gui.pacts.invites"), b -> setPage(Page.INVITATIONS)).bounds(guiLeft + 114, bottom, 96, 20).build());
            }
            case CREATE -> {
                textFieldName = field(cx, guiTop + 91, 32, "got.gui.pacts.createName");
                addRenderableWidget(Button.builder(Component.translatable("got.gui.pacts.createThis"), b -> sendText(C2SPactActionPacket.Action.CREATE, textFieldName, Page.PACT)).bounds(cx - 75, guiTop + 121, 150, 20).build()); addBack(cx, bottom, Page.LIST);
            }
            case PACT -> {
                boolean manage = pact != null && pact.viewerCanManage();
                addRenderableWidget(Button.builder(Component.translatable("got.gui.pacts.invite"), b -> setPage(Page.INVITE)).bounds(guiLeft + 10, bottom - 69, 96, 20).build()).active = manage;
                addRenderableWidget(Button.builder(Component.translatable("got.gui.pacts.rename"), b -> setPage(Page.RENAME)).bounds(guiLeft + 114, bottom - 69, 96, 20).build()).active = manage;
                Button pvp = addRenderableWidget(Button.builder(toggleLabel("PvP", pact != null && pact.preventPvp()), b -> send(new C2SPactActionPacket(C2SPactActionPacket.Action.TOGGLE_PVP))).bounds(guiLeft + 10, bottom - 46, 96, 20).build()); pvp.active = manage;
                Button hired = addRenderableWidget(Button.builder(toggleLabel("Hired FF", pact != null && pact.preventHiredFriendlyFire()), b -> send(new C2SPactActionPacket(C2SPactActionPacket.Action.TOGGLE_HIRED_FF))).bounds(guiLeft + 114, bottom - 46, 96, 20).build()); hired.active = manage;
                Button map = addRenderableWidget(Button.builder(toggleLabel("Map", pact != null && pact.showMapLocations()), b -> send(new C2SPactActionPacket(C2SPactActionPacket.Action.TOGGLE_MAP))).bounds(guiLeft + 10, bottom - 23, 96, 20).build()); map.active = manage;
                boolean meSharing = selfMember().map(S2CPactDataPacket.MemberView::sharingMap).orElse(false);
                addRenderableWidget(Button.builder(toggleLabel("My Marker", meSharing), b -> send(C2SPactActionPacket.flag(C2SPactActionPacket.Action.SET_MY_MAP_SHARING, !meSharing))).bounds(guiLeft + 114, bottom - 23, 96, 20).build());

                // Original Fellowship behavior: Set Icon copies the item held in the main hand.
                Button setIcon = addRenderableWidget(Button.builder(Component.literal("Set Icon (Held Item)"), b -> send(new C2SPactActionPacket(C2SPactActionPacket.Action.SET_ICON))).bounds(guiLeft + 10, guiTop + 44, 200, 20).build());
                setIcon.active = manage;

                addSelectedMemberButtons(pact);
                addBack(cx, bottom, Page.LIST);
            }
            case INVITE -> {
                textFieldPlayer = field(cx, guiTop + 91, 16, "got.gui.pacts.inviteName");
                addRenderableWidget(Button.builder(Component.translatable("got.gui.pacts.inviteThis"), b -> sendText(C2SPactActionPacket.Action.INVITE, textFieldPlayer, Page.PACT)).bounds(cx - 75, guiTop + 121, 150, 20).build()); addBack(cx, bottom, Page.PACT);
            }
            case RENAME -> {
                textFieldRename = field(cx, guiTop + 91, 32, "got.gui.pacts.renameName");
                addRenderableWidget(Button.builder(Component.translatable("got.gui.pacts.renameThis"), b -> sendText(C2SPactActionPacket.Action.RENAME, textFieldRename, Page.PACT)).bounds(cx - 75, guiTop + 121, 150, 20).build()); addBack(cx, bottom, Page.PACT);
            }
            case INVITATIONS -> {
                if (snap.invite() != null) {
                    addRenderableWidget(Button.builder(Component.literal("Accept"), b -> { send(new C2SPactActionPacket(C2SPactActionPacket.Action.ACCEPT)); setPage(Page.ACCEPT_INVITE_RESULT); }).bounds(cx - 76, guiTop + 92, 72, 20).build());
                    addRenderableWidget(Button.builder(Component.literal("Decline"), b -> { send(new C2SPactActionPacket(C2SPactActionPacket.Action.DECLINE)); setPage(Page.LIST); }).bounds(cx + 4, guiTop + 92, 72, 20).build());
                }
                addBack(cx, bottom, Page.LIST);
            }
            case REMOVE, OP, DEOP, TRANSFER, LEAVE, DISBAND -> addConfirmButtons(cx, bottom);
            case ACCEPT_INVITE_RESULT -> addBack(cx, bottom, Page.LIST);
        }
    }

    private EditBox field(int cx, int y, int max, String key) {
        EditBox f = new EditBox(font, cx - 75, y, 150, 20, Component.translatable(key)); f.setMaxLength(max); addRenderableWidget(f); return f;
    }
    private Component toggleLabel(String what, boolean on) { return Component.literal(what + ": " + (on ? "ON" : "OFF")); }
    private void addBack(int cx, int y, Page dest) { addRenderableWidget(Button.builder(Component.translatable("gui.back"), b -> setPage(dest)).bounds(cx - 50, y, 100, 20).build()); }

    private void addSelectedMemberButtons(S2CPactDataPacket.PactView pact) {
        if (pact == null || selectedMember == null) return;
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || selectedMember.equals(mc.player.getUUID())) return;
        S2CPactDataPacket.MemberView selected = pact.members().stream()
                .filter(m -> m.id().equals(selectedMember)).findFirst().orElse(null);
        if (selected == null || selected.owner()) return;

        int y = guiTop + 164;
        if (pact.viewerCanManage() && (!selected.admin() || pact.viewerOwner())) {
            buttonRemoveMember = addRenderableWidget(Button.builder(Component.literal("Remove"), b -> setPage(Page.REMOVE))
                    .bounds(guiLeft + 10, y, 62, 18).build());
        }
        if (pact.viewerOwner()) {
            buttonAdminMember = addRenderableWidget(Button.builder(Component.literal(selected.admin() ? "De-admin" : "Admin"),
                    b -> setPage(selected.admin() ? Page.DEOP : Page.OP)).bounds(guiLeft + 79, y, 62, 18).build());
            buttonTransferOwner = addRenderableWidget(Button.builder(Component.literal("Transfer"), b -> setPage(Page.TRANSFER))
                    .bounds(guiLeft + 148, y, 62, 18).build());
        }
    }

    private void addConfirmButtons(int cx, int bottom) {
        addRenderableWidget(Button.builder(Component.literal("Confirm"), b -> confirmCurrent()).bounds(cx - 76, bottom - 23, 72, 20).build());
        addRenderableWidget(Button.builder(Component.literal("Cancel"), b -> setPage(Page.PACT)).bounds(cx + 4, bottom - 23, 72, 20).build());
    }

    private void confirmCurrent() {
        C2SPactActionPacket.Action action = switch (page) {
            case REMOVE -> C2SPactActionPacket.Action.KICK; case OP -> C2SPactActionPacket.Action.OP;
            case DEOP -> C2SPactActionPacket.Action.DEOP; case TRANSFER -> C2SPactActionPacket.Action.TRANSFER;
            case LEAVE -> C2SPactActionPacket.Action.LEAVE; case DISBAND -> C2SPactActionPacket.Action.DISBAND;
            default -> null;
        };
        if (action != null) send(selectedMember != null ? C2SPactActionPacket.target(action, selectedMember) : new C2SPactActionPacket(action));
        selectedMember = null; setPage(Page.LIST);
    }

    private void sendText(C2SPactActionPacket.Action action, EditBox field, Page next) {
        if (field == null || field.getValue().trim().isEmpty()) return;
        send(C2SPactActionPacket.text(action, field.getValue().trim())); setPage(next);
    }
    private void send(C2SPactActionPacket packet) { GOTNetwork.CHANNEL.sendToServer(packet); }
    private void setPage(Page next) { page = next; resultText = ""; rebuildPageWidgets(); GOTNetwork.CHANNEL.sendToServer(new C2SRequestPactDataPacket()); }

    private java.util.Optional<S2CPactDataPacket.MemberView> selfMember() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return java.util.Optional.empty();
        return ClientGOTPactData.member(mc.player.getUUID());
    }

    @Override public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (page == Page.PACT) {
            var p = ClientGOTPactData.snapshot().pact();
            if (p != null) {
                int y = guiTop + 88;
                for (S2CPactDataPacket.MemberView member : p.members()) {
                    if (mouseX >= guiLeft + 10 && mouseX <= guiLeft + sizeX - 10 && mouseY >= y - 3 && mouseY <= y + 13) {
                        selectedMember = member.id(); rebuildPageWidgets(); return true;
                    }
                    y += 16; if (y > guiTop + 154) break;
                }
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override public void render(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
        renderBackground(g); super.render(g, mouseX, mouseY, partialTick);
        int cx = guiLeft + sizeX / 2;
        g.drawCenteredString(font, Component.translatable("got.gui.pacts.title"), cx, guiTop - 30, WHITE);
        g.fill(guiLeft, guiTop, guiLeft + sizeX, guiTop + sizeY - 52, PANEL);
        g.fill(guiLeft, guiTop, guiLeft + sizeX, guiTop + 1, PANEL_EDGE);
        switch (page) {
            case LIST -> renderList(g, cx); case CREATE -> renderPrompt(g, cx, "got.gui.pacts.createName");
            case PACT -> renderPact(g, cx); case INVITE -> renderPrompt(g, cx, "got.gui.pacts.inviteName");
            case RENAME -> renderPrompt(g, cx, "got.gui.pacts.renameName"); case INVITATIONS -> renderInvitations(g, cx);
            case REMOVE -> renderConfirm(g, cx, "got.gui.pacts.removeCheck"); case OP -> renderConfirm(g, cx, "got.gui.pacts.opCheck1");
            case DEOP -> renderConfirm(g, cx, "got.gui.pacts.deopCheck"); case TRANSFER -> renderConfirm(g, cx, "got.gui.pacts.transferCheck1");
            case LEAVE -> renderConfirm(g, cx, "got.gui.pacts.leaveCheck1"); case DISBAND -> renderConfirm(g, cx, "got.gui.pacts.disbandCheck1");
            case ACCEPT_INVITE_RESULT -> g.drawCenteredString(font, Component.literal("Pact invitation processed."), cx, guiTop + 70, WHITE);
        }
    }

    private void renderList(GuiGraphics g, int cx) {
        var s = ClientGOTPactData.snapshot();
        g.drawCenteredString(font, Component.translatable("got.gui.pacts.leading"), cx, guiTop + 8, WHITE);
        String lead = s.pact() != null && s.pact().viewerOwner() ? s.pact().name() : "None";
        row(g, Component.literal(lead), guiTop + 28, false);
        g.drawCenteredString(font, Component.translatable("got.gui.pacts.member"), cx, guiTop + 92, WHITE);
        String member = s.pact() != null ? s.pact().name() + " (" + s.pact().members().size() + ")" : "None";
        row(g, Component.literal(member), guiTop + 112, false);
    }

    private void renderPact(GuiGraphics g, int cx) {
        var p = ClientGOTPactData.snapshot().pact();
        if (p == null) { g.drawCenteredString(font, Component.literal("You are not in a Pact."), cx, guiTop + 20, GREY); return; }
        g.drawCenteredString(font, Component.literal(p.name() + " — " + p.members().size() + " members"), cx, guiTop + 8, WHITE);
        int iy = guiTop + 28;
        drawStateIcon(g, cx - 27, iy, p.preventPvp(), 0); drawStateIcon(g, cx - 8, iy, p.preventHiredFriendlyFire(), 16); drawStateIcon(g, cx + 11, iy, p.showMapLocations(), 32);
        renderPactIcon(g, p, guiLeft + 16, guiTop + 20);
        g.drawCenteredString(font, Component.translatable("got.gui.pacts.members"), cx, guiTop + 70, WHITE);
        int y = guiTop + 88;
        for (S2CPactDataPacket.MemberView m : p.members()) {
            boolean selected = m.id().equals(selectedMember);
            g.fill(guiLeft + 10, y - 3, guiLeft + sizeX - 10, y + 13, selected ? ROW_SELECTED : ROW);
            int color = m.online() ? WHITE : GREY;
            String prefix = m.owner() ? "★ " : m.admin() ? "◆ " : "";
            String suffix = m.title().isBlank() ? "" : " — " + m.title();
            g.drawString(font, prefix + m.name() + suffix, guiLeft + 16, y, color, false);
            if (m.sharingMap()) g.drawString(font, "M", guiLeft + sizeX - 24, y, GREEN, false);
            y += 16; if (y > guiTop + 158) break;
        }
        if (selectedMember != null) renderSelectedMemberActions(g, p, cx);
    }

    private void renderSelectedMemberActions(GuiGraphics g, S2CPactDataPacket.PactView p, int cx) {
        var selected = p.members().stream().filter(m -> m.id().equals(selectedMember)).findFirst().orElse(null);
        Minecraft mc = Minecraft.getInstance();
        if (selected == null || mc.player == null || selected.id().equals(mc.player.getUUID())) return;
        g.drawCenteredString(font, Component.literal("Selected: " + selected.name()), cx, guiTop + 154, GOLD);
    }

    private void renderPactIcon(GuiGraphics g, S2CPactDataPacket.PactView pact, int x, int y) {
        if (pact.icon() == null || pact.icon().isBlank()) return;
        ResourceLocation id = ResourceLocation.tryParse(pact.icon());
        if (id == null || !BuiltInRegistries.ITEM.containsKey(id)) return;
        ItemStack stack = new ItemStack(BuiltInRegistries.ITEM.get(id));
        if (!stack.isEmpty()) g.renderItem(stack, x, y);
    }

    private void drawStateIcon(GuiGraphics g, int x, int y, boolean on, int u) {
        g.blit(ICONS_TEXTURES, x, y, u, 0, 16, 16, 256, 256);
        if (!on) g.drawString(font, "×", x + 5, y + 4, RED, false);
    }

    private void renderInvitations(GuiGraphics g, int cx) {
        var invite = ClientGOTPactData.snapshot().invite();
        g.drawCenteredString(font, Component.translatable("got.gui.pacts.invites"), cx, guiTop + 8, WHITE);
        if (invite == null) row(g, Component.translatable("got.gui.pacts.invitesNone"), guiTop + 38, false);
        else {
            row(g, Component.literal(invite.pactName()), guiTop + 38, false);
            g.drawCenteredString(font, Component.literal("Invited by " + invite.inviterName()), cx, guiTop + 60, GREY);
        }
    }
    private void renderPrompt(GuiGraphics g, int cx, String key) { g.drawCenteredString(font, Component.translatable(key), cx, guiTop + 70, WHITE); }
    private void renderConfirm(GuiGraphics g, int cx, String key) { g.drawCenteredString(font, Component.translatable(key), cx, guiTop + 74, RED); }
    private void row(GuiGraphics g, Component text, int y, boolean selected) { g.fill(guiLeft + 10, y - 4, guiLeft + sizeX - 10, y + 14, selected ? ROW_SELECTED : ROW); g.drawCenteredString(font, text, guiLeft + sizeX / 2, y, GREY); }

    public enum Page { LIST, CREATE, PACT, INVITE, DISBAND, LEAVE, REMOVE, OP, DEOP, TRANSFER, RENAME, INVITATIONS, ACCEPT_INVITE_RESULT }
}
