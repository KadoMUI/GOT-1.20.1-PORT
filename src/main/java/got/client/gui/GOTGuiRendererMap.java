package got.client.gui;

import got.GOTMod;
import got.world.GOTDimensions;
import got.world.PlanetosMapSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.resources.ResourceLocation;

/**
 * 1.20.1 translation of the original 1.7.10 GOTGuiRendererMap.
 *
 * The legacy class was deliberately small: it owned the interpolated map
 * centre and zoom values, established the map viewport, then called back into
 * GOTGuiMap to draw the atlas, overlays, Bezier routes and waypoint layers.
 * This class preserves that division instead of folding the renderer back into
 * the screen.
 */
public final class GOTGuiRendererMap {
    private static final ResourceLocation VIGNETTE_TEXTURE =
            ResourceLocation.withDefaultNamespace("textures/misc/vignette.png");

    private boolean sepia;
    private double prevMapX;
    private double mapX;
    private double prevMapY;
    private double mapY;
    private float zoomExp;
    private float zoomStable;

    /** Draw one full-screen vignette pass, matching the legacy helper. */
    private static void renderVignette(GuiGraphics graphics, Screen screen, int alpha) {
        renderVignette(graphics, screen, alpha, 0, 0, screen.width, screen.height);
    }

    /** Draw one clipped vignette pass. */
    private static void renderVignette(GuiGraphics graphics, Screen screen, int alpha,
                                       int xMin, int yMin, int xMax, int yMax) {
        int colour = (Math.max(0, Math.min(255, alpha)) << 24) | 0x00FFFFFF;
        graphics.setColor(1.0F, 1.0F, 1.0F, alpha / 255.0F);
        graphics.blit(VIGNETTE_TEXTURE, xMin, yMin, xMax - xMin, yMax - yMin,
                xMin, yMin, xMax - xMin, yMax - yMin, screen.width, screen.height);
        graphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
    }

    public static void renderVignettes(GuiGraphics graphics, Screen screen, int alpha, int passes) {
        for (int i = 0; i < passes; i++) {
            renderVignette(graphics, screen, alpha);
        }
    }

    public static void renderVignettes(GuiGraphics graphics, Screen screen, int alpha, int passes,
                                       int xMin, int yMin, int xMax, int yMax) {
        for (int i = 0; i < passes; i++) {
            renderVignette(graphics, screen, alpha, xMin, yMin, xMax, yMax);
        }
    }

    public void renderMap(GuiGraphics graphics, GOTGuiMap map, float partialTick) {
        renderMap(graphics, map, partialTick, 0, 0, map.width, map.height);
    }

    /**
     * Direct modern equivalent of the original six-argument renderMap method.
     */
    public void renderMap(GuiGraphics graphics, GOTGuiMap map, float partialTick,
                          int xMin, int yMin, int xMax, int yMax) {
        int oceanColour = sepia ? 0xFF8B7654 : 0xFF0B587D;
        graphics.fill(xMin, yMin, xMax, yMax, oceanColour);

        float zoom = (float) Math.pow(2.0D, zoomExp);
        double renderMapX = prevMapX + (mapX - prevMapX) * partialTick;
        double renderMapY = prevMapY + (mapY - prevMapY) * partialTick;

        map.setFakeMapProperties((float) renderMapX, (float) renderMapY, zoom, zoomExp, zoomStable);
        int[] previousStaticProperties = map.setFakeStaticProperties(
                xMax - xMin, yMax - yMin, xMin, xMax, yMin, yMax);

        map.setEnableZoomOutWPFading(false);
        map.renderMapAndOverlay(graphics, sepia, 1.0F, true);
        map.renderBeziers(graphics, false);
        map.renderWaypoints(graphics, false);
        map.renderPactMembers(graphics);
        map.renderPlayer(graphics);

        map.restoreFakeStaticProperties(previousStaticProperties);
    }

    public void updateTick() {
        prevMapX = mapX;
        prevMapY = mapY;
    }

    public void setSepia(boolean sepia) {
        this.sepia = sepia;
    }

    public void setPrevMapX(double prevMapX) {
        this.prevMapX = prevMapX;
    }

    public double getMapX() {
        return mapX;
    }

    public void setMapX(double mapX) {
        this.mapX = mapX;
    }

    public void setPrevMapY(double prevMapY) {
        this.prevMapY = prevMapY;
    }

    public double getMapY() {
        return mapY;
    }

    public void setMapY(double mapY) {
        this.mapY = mapY;
    }

    public float getZoomExp() {
        return zoomExp;
    }

    public void setZoomExp(float zoomExp) {
        this.zoomExp = zoomExp;
    }

    public void setZoomStable(float zoomStable) {
        this.zoomStable = zoomStable;
    }
}
