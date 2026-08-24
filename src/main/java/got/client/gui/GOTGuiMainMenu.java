package got.client.gui;

import got.GOTMod;
import got.world.PlanetosMapSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

/**
 * 1.20.1 adaptation of the original GOTGuiMainMenu map-backed title screen.
 * The original waypoint tour is represented by the same continuous map camera
 * contract; named waypoint data will replace the temporary route coordinates
 * when GOTWaypoint is ported.
 */
public class GOTGuiMainMenu extends TitleScreen {
    private static final ResourceLocation MENU_OVERLAY =
            ResourceLocation.fromNamespaceAndPath(GOTMod.MOD_ID, "textures/gui/menu_overlay.png");

    private static final double[][] WAYPOINT_ROUTE = {
            {0.18D, 0.10D}, {0.20D, 0.23D}, {0.25D, 0.38D}, {0.19D, 0.52D},
            {0.30D, 0.57D}, {0.33D, 0.68D}, {0.25D, 0.77D}, {0.31D, 0.88D},
            {0.47D, 0.55D}, {0.57D, 0.72D}, {0.72D, 0.54D}, {0.88D, 0.67D},
            {0.95D, 0.48D}, {0.88D, 0.37D}, {0.73D, 0.91D}, {0.62D, 0.86D},
            {0.57D, 0.79D}, {0.52D, 0.62D}, {0.30D, 0.57D}
    };

    private static final GOTGuiRendererMap MAP_RENDERER = new GOTGuiRendererMap();
    private static boolean firstMenu = true;
    private static float mapSpeed;
    private static float mapVelX;
    private static float mapVelY;
    private static int currentWaypointIndex;

    private final GOTGuiMap mapGui;
    private final boolean fadeIn;
    private long firstRenderTime;

    public GOTGuiMainMenu() {
        super();
        fadeIn = firstMenu;
        firstMenu = false;
        mapGui = new GOTGuiMap(this);
        MAP_RENDERER.setSepia(false);
        double[] start = waypointPixels(currentWaypointIndex);
        MAP_RENDERER.setMapX(start[0]);
        MAP_RENDERER.setMapY(start[1]);
        MAP_RENDERER.setPrevMapX(start[0]);
        MAP_RENDERER.setPrevMapY(start[1]);
    }

    private static double[] waypointPixels(int index) {
        double[] normalized = WAYPOINT_ROUTE[Math.floorMod(index, WAYPOINT_ROUTE.length)];
        return new double[] {
                normalized[0] * PlanetosMapSystem.MAP_WIDTH,
                normalized[1] * PlanetosMapSystem.MAP_HEIGHT
        };
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        if (firstRenderTime == 0L && fadeIn) firstRenderTime = System.currentTimeMillis();
        float elapsed = fadeIn ? (System.currentTimeMillis() - firstRenderTime) / 1000.0F : 1.0F;
        float overlayAlpha = fadeIn ? Mth.clamp(elapsed - 1.0F, 0.0F, 1.0F) : 1.0F;

        MAP_RENDERER.setZoomExp(-0.3F + (fadeIn ? -1.5F * Mth.clamp(1.0F - elapsed * 0.5F, 0.0F, 1.0F) : 0.0F));
        MAP_RENDERER.setZoomStable((float) Math.pow(2.0D, -0.1D));
        MAP_RENDERER.renderMap(graphics, mapGui, partialTick, 0, 0, width, height);

        graphics.setColor(1.0F, 1.0F, 1.0F, overlayAlpha);
        graphics.blit(MENU_OVERLAY, 0, 0, width, height, 0, 0, 256, 256, 256, 256);
        graphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);

        super.render(graphics, mouseX, mouseY, partialTick);
    }

    @Override
    public void tick() {
        super.tick();
        MAP_RENDERER.updateTick();

        double[] target = waypointPixels(currentWaypointIndex);
        double dx = target[0] - MAP_RENDERER.getMapX();
        double dy = target[1] - MAP_RENDERER.getMapY();
        double distance = Math.sqrt(dx * dx + dy * dy);
        if (distance <= 12.0D) {
            currentWaypointIndex = (currentWaypointIndex + 1) % WAYPOINT_ROUTE.length;
            mapSpeed = 0.0F;
        } else {
            mapSpeed = Math.min(mapSpeed + 0.01F, 0.8F);
            double desiredX = dx / distance * mapSpeed;
            double desiredY = dy / distance * mapSpeed;
            float smoothing = 0.02F;
            mapVelX += (float) ((desiredX - mapVelX) * smoothing);
            mapVelY += (float) ((desiredY - mapVelY) * smoothing);
        }
        MAP_RENDERER.setMapX(MAP_RENDERER.getMapX() + mapVelX);
        MAP_RENDERER.setMapY(MAP_RENDERER.getMapY() + mapVelY);
    }
}
