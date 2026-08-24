package got.client.gui;

import got.claim.GOTBannerClaimSnapshot;
import got.claim.GOTBannerPermission;
import got.network.C2SBannerClaimActionPacket;
import got.network.GOTNetwork;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/** Functional modern port of the original 200x250 banner_edit screen. */
public final class GOTGuiBannerClaim extends Screen {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(
            "got", "textures/gui/banner_edit.png");
    private static final int GUI_WIDTH = 200;
    private static final int GUI_HEIGHT = 250;
    private static final int VISIBLE_ROWS = 6;
    private GOTBannerClaimSnapshot snapshot;
    private int guiLeft;
    private int guiTop;
    private int scroll;
    private int selectedIndex = -1;
    private boolean showingDefaultPermissions;
    private Button modeButton;
    private Button selfButton;
    private Button defaultButton;
    private Button addButton;
    private Button removeButton;
    private Button alignmentButton;
    private EditBox alignmentField;
    private EditBox addField;
    private final List<Button> permissionButtons = new ArrayList<>();

    public GOTGuiBannerClaim(GOTBannerClaimSnapshot snapshot) {
        super(Component.translatable("got.gui.bannerEdit.title"));
        this.snapshot = snapshot;
    }

    public UUID entityUuid() {
        return snapshot.entityUuid();
    }

    public void accept(GOTBannerClaimSnapshot update) {
        snapshot = update;
        selectedIndex = Math.min(selectedIndex, update.entries().size() - 1);
        scroll = Math.min(scroll, Math.max(0, update.entries().size() - VISIBLE_ROWS));
        clearWidgets();
        init();
    }

    @Override
    protected void init() {
        guiLeft = (width - GUI_WIDTH) / 2;
        guiTop = (height - GUI_HEIGHT) / 2;
        permissionButtons.clear();

        modeButton = addRenderableWidget(Button.builder(modeText(), button -> send(
                        C2SBannerClaimActionPacket.simple(snapshot.entityId(),
                                C2SBannerClaimActionPacket.Action.SET_MODE,
                                snapshot.playerSpecific() ? 0 : 1)))
                .bounds(guiLeft + 20, guiTop + 20, 160, 20).build());

        alignmentField = addRenderableWidget(new EditBox(font, guiLeft + 35, guiTop + 91,
                90, 18, Component.translatable("got.gui.bannerEdit.protectionMode.faction.alignment")));
        alignmentField.setValue(formatAlignment(snapshot.alignmentRequired()));
        alignmentField.setMaxLength(10);
        alignmentField.setFilter(value -> value.isBlank() || value.matches("[0-9.,]*"));
        alignmentButton = addRenderableWidget(Button.builder(Component.translatable("got.gui.bannerEdit.set"),
                        button -> submitAlignment())
                .bounds(guiLeft + 130, guiTop + 90, 45, 20).build());

        addField = addRenderableWidget(new EditBox(font, guiLeft + 30, guiTop + 69,
                112, 18, Component.translatable("got.gui.bannerEdit.playerOrPact")));
        addField.setMaxLength(64);
        addButton = addRenderableWidget(Button.builder(Component.literal("+"), button -> addEntry())
                .bounds(guiLeft + 146, guiTop + 68, 30, 20).build());
        removeButton = addRenderableWidget(Button.builder(Component.literal("-"), button -> removeSelected())
                .bounds(guiLeft + 146, guiTop + 200, 30, 18).build());

        selfButton = addRenderableWidget(Button.builder(selfText(), button -> send(
                        C2SBannerClaimActionPacket.simple(snapshot.entityId(),
                                C2SBannerClaimActionPacket.Action.SET_SELF_PROTECTION,
                                snapshot.selfProtection() ? 0 : 1)))
                .bounds(guiLeft + 20, guiTop + 224, 76, 20).build());
        defaultButton = addRenderableWidget(Button.builder(
                        Component.translatable("got.gui.bannerEdit.perms.default"), button -> {
                            showingDefaultPermissions = !showingDefaultPermissions;
                            refreshVisibility();
                        }).bounds(guiLeft + 104, guiTop + 224, 76, 20).build());

        int permissionLeft = guiLeft + GUI_WIDTH + 6;
        for (GOTBannerPermission permission : GOTBannerPermission.values()) {
            int ordinal = permission.ordinal();
            Button button = addRenderableWidget(Button.builder(permissionText(permission),
                            ignored -> togglePermission(permission))
                    .bounds(permissionLeft + (ordinal % 2) * 96,
                            guiTop + 55 + (ordinal / 2) * 24, 92, 20).build());
            permissionButtons.add(button);
        }
        refreshVisibility();
    }

    private void refreshVisibility() {
        boolean playerMode = snapshot.playerSpecific();
        boolean mutable = snapshot.editable() && !snapshot.structureProtection();
        alignmentField.visible = !playerMode;
        alignmentButton.visible = !playerMode;
        addField.visible = playerMode;
        addButton.visible = playerMode;
        removeButton.visible = playerMode;
        alignmentField.setEditable(mutable);
        addField.setEditable(mutable);
        removeButton.active = mutable && playerMode
                && selectedEntry() != null && !selectedEntry().viewer();
        selfButton.active = mutable;
        modeButton.active = mutable;
        alignmentButton.active = mutable;
        addButton.active = mutable;
        defaultButton.active = mutable;
        boolean showPermissionPanel = showingDefaultPermissions
                || playerMode && selectedEntry() != null;
        for (GOTBannerPermission permission : GOTBannerPermission.values()) {
            Button button = permissionButtons.get(permission.ordinal());
            button.visible = showPermissionPanel
                    && (!showingDefaultPermissions || permission != GOTBannerPermission.FULL);
            button.active = mutable && button.visible;
            button.setMessage(permissionText(permission));
        }
    }

    private Component modeText() {
        return Component.translatable(snapshot.playerSpecific()
                ? "got.gui.bannerEdit.protectionMode.playerSpecific"
                : "got.gui.bannerEdit.protectionMode.faction");
    }

    private Component selfText() {
        return Component.translatable("got.gui.bannerEdit.selfProtection."
                + (snapshot.selfProtection() ? "on" : "off"));
    }

    private Component permissionText(GOTBannerPermission permission) {
        boolean enabled = (currentPermissionBits() & permission.bit()) != 0;
        return Component.translatable(permission.translationKey())
                .copy().withStyle(enabled ? ChatFormatting.GREEN : ChatFormatting.DARK_GRAY);
    }

    private int currentPermissionBits() {
        if (showingDefaultPermissions) return snapshot.defaultPermissions();
        GOTBannerClaimSnapshot.EntryView entry = selectedEntry();
        return entry == null ? 0 : entry.permissions();
    }

    private GOTBannerClaimSnapshot.EntryView selectedEntry() {
        return selectedIndex >= 0 && selectedIndex < snapshot.entries().size()
                ? snapshot.entries().get(selectedIndex) : null;
    }

    private void togglePermission(GOTBannerPermission permission) {
        int bits = currentPermissionBits();
        if (showingDefaultPermissions) {
            bits ^= permission.bit();
            bits &= ~GOTBannerPermission.FULL.bit();
            send(C2SBannerClaimActionPacket.simple(snapshot.entityId(),
                    C2SBannerClaimActionPacket.Action.SET_DEFAULT_PERMISSIONS, bits));
            return;
        }
        GOTBannerClaimSnapshot.EntryView entry = selectedEntry();
        if (entry == null || entry.viewer()) return;
        if (permission == GOTBannerPermission.FULL) {
            bits = (bits & permission.bit()) != 0 ? 0 : permission.bit();
        } else {
            bits &= ~GOTBannerPermission.FULL.bit();
            bits ^= permission.bit();
        }
        send(C2SBannerClaimActionPacket.entry(snapshot.entityId(),
                C2SBannerClaimActionPacket.Action.SET_ENTRY_PERMISSIONS, entry.key(), bits));
    }

    private void submitAlignment() {
        try {
            float value = Float.parseFloat(alignmentField.getValue().replace(",", ""));
            send(C2SBannerClaimActionPacket.alignment(snapshot.entityId(), value));
        } catch (NumberFormatException ignored) {
            alignmentField.setValue(formatAlignment(snapshot.alignmentRequired()));
        }
    }

    private void addEntry() {
        String name = addField.getValue().trim();
        if (name.isBlank()) return;
        send(C2SBannerClaimActionPacket.entry(snapshot.entityId(),
                C2SBannerClaimActionPacket.Action.ADD_ENTRY, name, 0));
        addField.setValue("");
    }

    private void removeSelected() {
        GOTBannerClaimSnapshot.EntryView entry = selectedEntry();
        if (entry == null || entry.viewer()) return;
        send(C2SBannerClaimActionPacket.entry(snapshot.entityId(),
                C2SBannerClaimActionPacket.Action.REMOVE_ENTRY, entry.key(), 0));
        selectedIndex = -1;
    }

    private void send(C2SBannerClaimActionPacket packet) {
        GOTNetwork.CHANNEL.sendToServer(packet);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        renderBackground(graphics);
        graphics.blit(TEXTURE, guiLeft, guiTop, 0, 0, GUI_WIDTH, GUI_HEIGHT, 256, 256);
        graphics.drawCenteredString(font, title, guiLeft + GUI_WIDTH / 2, guiTop + 6, 0x404040);
        graphics.drawCenteredString(font, Component.translatable("got.gui.bannerEdit.claimSummary",
                        snapshot.range(), snapshot.ownerName()),
                guiLeft + GUI_WIDTH / 2, guiTop + 45, 0x404040);

        if (snapshot.playerSpecific()) renderWhitelist(graphics, mouseX, mouseY);
        else renderFactionMode(graphics);
        renderPermissionPanel(graphics);
        super.render(graphics, mouseX, mouseY, partialTick);
    }

    private void renderFactionMode(GuiGraphics graphics) {
        graphics.drawCenteredString(font, Component.translatable(
                        "got.gui.bannerEdit.protectionMode.faction.desc.1"),
                guiLeft + GUI_WIDTH / 2, guiTop + 51, 0x404040);
        graphics.drawCenteredString(font, Component.translatable(
                        "got.gui.bannerEdit.protectionMode.faction.desc.2",
                        formatAlignment(snapshot.alignmentRequired()), snapshot.factionId()),
                guiLeft + GUI_WIDTH / 2, guiTop + 62, 0x404040);
        graphics.drawString(font, Component.translatable(
                        "got.gui.bannerEdit.protectionMode.faction.alignment"),
                guiLeft + 35, guiTop + 80, 0x404040, false);
        if (snapshot.structureProtection()) {
            graphics.drawCenteredString(font, Component.translatable("got.gui.bannerEdit.structure"),
                    guiLeft + GUI_WIDTH / 2, guiTop + 126, 0x8B1A1A);
        }
    }

    private void renderWhitelist(GuiGraphics graphics, int mouseX, int mouseY) {
        int start = Math.min(scroll, Math.max(0, snapshot.entries().size() - VISIBLE_ROWS));
        for (int visible = 0; visible < VISIBLE_ROWS && start + visible < snapshot.entries().size(); visible++) {
            int index = start + visible;
            GOTBannerClaimSnapshot.EntryView entry = snapshot.entries().get(index);
            int x = guiLeft + 30;
            int y = guiTop + 94 + visible * 18;
            boolean hovered = mouseX >= x && mouseX < x + 146 && mouseY >= y && mouseY < y + 16;
            if (index == selectedIndex) graphics.fill(x, y, x + 146, y + 16, 0x554E7C35);
            else if (hovered) graphics.fill(x, y, x + 146, y + 16, 0x334E7C35);
            graphics.drawString(font, (index + 1) + ". " + entry.name(), x + 2, y + 4,
                    entry.kind() == got.claim.GOTBannerWhitelistEntry.Kind.GROUP ? 0x6D3E91 : 0x305A30,
                    false);
        }
        graphics.drawCenteredString(font, Component.translatable("got.gui.bannerEdit.fellowshipHint", "f/"),
                guiLeft + GUI_WIDTH / 2, guiTop + 207, 0x555555);
    }

    private void renderPermissionPanel(GuiGraphics graphics) {
        boolean show = showingDefaultPermissions
                || snapshot.playerSpecific() && selectedEntry() != null;
        if (!show) return;
        int left = guiLeft + GUI_WIDTH + 2;
        int right = left + 202;
        graphics.fill(left, guiTop + 27, right, guiTop + 160, 0xDD20190F);
        Component panelTitle = showingDefaultPermissions
                ? Component.translatable("got.gui.bannerEdit.perms.default")
                : Component.translatable(selectedEntry().kind() == got.claim.GOTBannerWhitelistEntry.Kind.GROUP
                        ? "got.gui.bannerEdit.perms.fellowship" : "got.gui.bannerEdit.perms.player");
        graphics.drawCenteredString(font, panelTitle, left + 101, guiTop + 33, 0xFFFFFF);
        if (!showingDefaultPermissions && selectedEntry() != null) {
            graphics.drawCenteredString(font, selectedEntry().name(), left + 101, guiTop + 44, 0xBEB09B);
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0 && snapshot.playerSpecific()) {
            int start = Math.min(scroll, Math.max(0, snapshot.entries().size() - VISIBLE_ROWS));
            for (int visible = 0; visible < VISIBLE_ROWS && start + visible < snapshot.entries().size(); visible++) {
                int x = guiLeft + 30;
                int y = guiTop + 94 + visible * 18;
                if (mouseX >= x && mouseX < x + 146 && mouseY >= y && mouseY < y + 16) {
                    selectedIndex = start + visible;
                    showingDefaultPermissions = false;
                    refreshVisibility();
                    return true;
                }
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        if (snapshot.playerSpecific()) {
            int max = Math.max(0, snapshot.entries().size() - VISIBLE_ROWS);
            scroll = Math.max(0, Math.min(max, scroll + (delta < 0 ? 1 : -1)));
            return true;
        }
        return super.mouseScrolled(mouseX, mouseY, delta);
    }

    private static String formatAlignment(float value) {
        return value == Math.round(value) ? Integer.toString(Math.round(value)) : Float.toString(value);
    }

    @Override public boolean isPauseScreen() { return false; }
}
