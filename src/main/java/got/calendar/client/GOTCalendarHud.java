package got.calendar.client;

import got.calendar.GOTCalendarApi;
import got.client.gui.GOTGuiMap;
import got.client.gui.GOTGuiMenu;
import got.client.gui.GOTGuiMenuBase;
import got.world.GOTDimensions;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Pass 3 calendar presentation.
 * Replaces the always-on Pass 2 debug HUD with a compact date panel on GOT screens.
 */
@Mod.EventBusSubscriber(modid = "got", value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class GOTCalendarHud {
    private GOTCalendarHud() {}

    @SubscribeEvent
    public static void onRenderGui(RenderGuiOverlayEvent.Post event) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.level == null || minecraft.player == null) return;
        if (!minecraft.level.dimension().equals(GOTDimensions.PLANETOS)) return;
        if (!GOTClientCalendarState.isSynchronized()) return;

        Screen screen = minecraft.screen;
        if (!(screen instanceof GOTGuiMap) && !(screen instanceof GOTGuiMenu) && !(screen instanceof GOTGuiMenuBase)) {
            return;
        }

        GOTCalendarApi.Snapshot snapshot = GOTClientCalendarState.snapshot();
        GuiGraphics graphics = event.getGuiGraphics();

        String line1 = snapshot.displayDate();
        String line2 = snapshot.season().name() + " | "
                + snapshot.timeOfDay().displayName() + " | "
                + snapshot.moonPhase().displayName();

        int margin = 8;
        int panelWidth = Math.max(minecraft.font.width(line1), minecraft.font.width(line2)) + 10;
        int panelHeight = 25;
        int x = graphics.guiWidth() - panelWidth - margin;
        int y = margin;

        graphics.fill(x, y, x + panelWidth, y + panelHeight, 0x90000000);
        graphics.drawString(minecraft.font, line1, x + 5, y + 4, 0xFFFFFF, false);
        graphics.drawString(minecraft.font, line2, x + 5, y + 14, 0xD0D0D0, false);
    }
}
