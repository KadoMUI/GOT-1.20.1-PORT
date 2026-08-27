package got.client.gui;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

/**
 * Direct 1.20.1 translation of the original 1.7.10 GOTGuiMenu.
 *
 * Preserved from the original class:
 * - original menu_icons.png atlas and icon row ids
 * - 32x32 buttons
 * - 10px spacing (42px pitch)
 * - automatic two-row split and centering calculations
 * - title at screen center - 80px
 * - hover tooltip behavior
 * - per-entry keyboard shortcuts
 * - remembered last menu screen support
 *
 * Screens not yet ported are intentionally disabled. Map and factions are active.
 */
public final class GOTGuiMenu extends Screen {
    public static final ResourceLocation MENU_ICONS_TEXTURE =
            ResourceLocation.fromNamespaceAndPath("got", "textures/gui/menu_icons.png");

    private static final int BUTTON_SIZE = 32;
    private static final int BUTTON_GAP = 10;
    private static final int BUTTON_PITCH = BUTTON_SIZE + BUTTON_GAP;

    private static Class<? extends Screen> lastMenuScreen;

    private final List<GOTGuiButtonMenu> menuButtons = new ArrayList<>();

    public GOTGuiMenu() {
        super(Component.translatable("got.gui.menu"));
    }

    /** Equivalent to the original openMenu(EntityPlayer), minus unported quest forcing. */
    public static Screen openMenu() {
        if (lastMenuScreen != null) {
            try {
                return lastMenuScreen.getConstructor().newInstance();
            } catch (ReflectiveOperationException exception) {
                exception.printStackTrace();
            }
        }
        return new GOTGuiMenu();
    }

    public static void resetLastMenuScreen() {
        lastMenuScreen = null;
    }

    @Override
    protected void init() {
        super.init();
        resetLastMenuScreen();
        menuButtons.clear();

        // Original order, icon ids, labels, and keyboard shortcuts.
        addMenuButton(2, Component.translatable("got.gui.achievements"), GLFW.GLFW_KEY_A, GOTGuiAchievements::new, true);
        addMenuButton(3, Component.translatable("got.gui.map"), GLFW.GLFW_KEY_M,
                () -> new GOTGuiMap(this), true);
        addMenuButton(4, Component.translatable("got.gui.factions"), GLFW.GLFW_KEY_F,
                GOTGuiFactions::new, true);
        addMenuButton(8, Component.translatable("got.gui.lore"), GLFW.GLFW_KEY_B, GOTGuiLore::new, true);
        addMenuButton(0, Component.translatable("got.gui.capes"), GLFW.GLFW_KEY_C, GOTGuiCapes::new, true);
        addMenuButton(6, Component.translatable("got.gui.pacts"), GLFW.GLFW_KEY_P, GOTGuiPacts::new, true);
        addMenuButton(7, Component.translatable("got.gui.titles"), GLFW.GLFW_KEY_T, GOTGuiTitles::new, true);
        addMenuButton(5, Component.translatable("got.gui.alignment"), GLFW.GLFW_KEY_S, GOTGuiAlignment::new, true);
        addMenuButton(1, Component.translatable("got.gui.settings"), GLFW.GLFW_KEY_O, GOTGuiSettings::new, true);

        // Exact layout math from GOTGuiMenu.func_73866_w_().
        int centerX = width / 2;
        int centerY = height / 2;
        int buttonCount = menuButtons.size();
        int topRowCount = (buttonCount - 1) / 2 + 1;
        int bottomRowCount = buttonCount - topRowCount;

        int topRowStartX = centerX - (topRowCount * BUTTON_SIZE + (topRowCount - 1) * BUTTON_GAP) / 2;
        int bottomRowStartX = centerX - (bottomRowCount * BUTTON_SIZE + (bottomRowCount - 1) * BUTTON_GAP) / 2;

        for (int index = 0; index < buttonCount; index++) {
            GOTGuiButtonMenu button = menuButtons.get(index);
            if (index < topRowCount) {
                button.setX(topRowStartX + index * BUTTON_PITCH);
                button.setY(centerY - 5 - BUTTON_SIZE);
            } else {
                button.setX(bottomRowStartX + (index - topRowCount) * BUTTON_PITCH);
                button.setY(centerY + 5);
            }
        }
    }

    private void addMenuButton(
            int iconId,
            Component tooltip,
            int keyCode,
            GOTGuiButtonMenu.ScreenFactory screenFactory,
            boolean enabled
    ) {
        GOTGuiButtonMenu button = new GOTGuiButtonMenu(iconId, 0, 0, screenFactory, tooltip, keyCode);
        button.active = enabled;
        menuButtons.add(button);
        addRenderableWidget(button);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        renderBackground(graphics);

        graphics.drawString(
                font,
                title,
                width / 2 - font.width(title) / 2,
                height / 2 - 80,
                0xFFFFFF,
                false
        );

        super.render(graphics, mouseX, mouseY, partialTick);

        // The old GuiButton rendered no text; GOTGuiMenu supplied the hovered label as a tooltip.
        for (GOTGuiButtonMenu button : menuButtons) {
            if (button.isHovered() && button.getMessage() != null) {
                graphics.renderTooltip(font, button.getMessage(), mouseX, mouseY);
                break;
            }
        }
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        for (GOTGuiButtonMenu button : menuButtons) {
            if (button.visible && button.active && button.getMenuKeyCode() >= 0 && keyCode == button.getMenuKeyCode()) {
                button.onPress();
                return true;
            }
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    /** Called by later menu screens when their own direct ports are added. */
    public static void rememberMenuScreen(Screen screen) {
        if (screen != null && screen.getClass() != GOTGuiMenu.class) {
            lastMenuScreen = screen.getClass();
        }
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
