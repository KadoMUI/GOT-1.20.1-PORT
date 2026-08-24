package got.client.gui;

import got.GOTMod;
import got.world.GOTDimensions;
import got.world.PlanetosMapSystem;
import got.common.world.map.GOTWaypoint;
import got.common.world.map.GOTMapLabels;
import got.common.world.map.GOTMapRegionNames;
import got.common.world.map.GOTBeziers;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.PlayerFaceRenderer;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.lwjgl.glfw.GLFW;

/**
 * First direct 1.20.1 translation pass of the original 1.7.10 GOTGuiMap.
 *
 * This class deliberately preserves the original map state model:
 * integer power-of-two zoom, six-tick zoom interpolation, a 312x200
 * windowed viewport, 30px fullscreen margins, map-relative widgets,
 * click-drag movement, keyboard movement, and coordinate readout.
 * Systems which do not exist in the port yet (waypoints, conquest,
 * fellowships/pacts and labels) remain dormant rather than being replaced
 * with invented behavior.
 */
public final class GOTGuiMap extends Screen {
    public static final ResourceLocation MAP_ICONS_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(GOTMod.MOD_ID, "textures/map/mapScreen.png");
    private static final ResourceLocation MAP_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(GOTMod.MOD_ID, "textures/map/map.png");
    private static final ResourceLocation MAP_OVERLAY_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(GOTMod.MOD_ID, "textures/map/mapOverlay.png");

    private static final int WINDOWED_MAP_WIDTH = 312;
    private static final int WINDOWED_MAP_HEIGHT = 200;
    private static final int FULLSCREEN_MARGIN = 30;
    private static final int ZOOM_TICKS_MAX = 6;
    private static final int MIN_ZOOM_POWER = -4;
    private static final int MAX_ZOOM_POWER = 4;
    private static final float ROAD_DOT_SPACING_BLOCKS = 400.0F;
    private static final float WALL_SAMPLE_SPACING_BLOCKS = 128.0F;
    private static final int PLAYER_HEAD_SIZE = 8;

    // These were static in the original class so reopening the map preserves its state.
    private static boolean fullscreen = true;
    private static int zoomPower = -1;
    private static float savedPosX = PlanetosMapSystem.MAP_WIDTH / 2.0F;
    private static float savedPosY = PlanetosMapSystem.MAP_HEIGHT / 2.0F;
    private static boolean showLabels = true;

    private final Screen parent;
    private final GOTGuiRendererMap mapRenderer = new GOTGuiRendererMap();
    private final GOTGuiMapWidget widgetZoomIn = new GOTGuiMapWidget(6, 6, 10, "zoomIn", 30, 0);
    private final GOTGuiMapWidget widgetZoomOut = new GOTGuiMapWidget(6, 20, 10, "zoomOut", 40, 0);
    private final GOTGuiMapWidget widgetFullscreen = new GOTGuiMapWidget(6, 34, 10, "fullScreen", 50, 0);
    private final GOTGuiMapWidget widgetLabels = new GOTGuiMapWidget(6, 48, 10, "labels", 70, 0);
    private final GOTGuiMapWidget widgetCompass = new GOTGuiMapWidget(-38, -38, 32, "compass", 224, 224);

    // Temporary static-map properties used by GOTGuiRendererMap, matching the legacy callback contract.
    private int renderWidth;
    private int renderHeight;
    private int renderXMin;
    private int renderXMax;
    private int renderYMin;
    private int renderYMax;
    private float renderMapX;
    private float renderMapY;
    private float renderZoom;
    private float renderZoomExp;
    private float renderZoomStable;
    private boolean enableZoomOutWPFading = true;

    private int mapWidth;
    private int mapHeight;
    private int mapXMin;
    private int mapXMax;
    private int mapYMin;
    private int mapYMax;

    private float posX;
    private float posY;
    private float prevPosX;
    private float prevPosY;
    private float posXMove;
    private float posYMove;

    private int prevZoomPower;
    private int zoomTicks;
    private float zoomExp;
    private float zoomScale;
    private float zoomScaleStable;

    private boolean dragging;
    private double previousMouseX;
    private double previousMouseY;
    private int mouseXCoord;
    private int mouseZCoord;
    private int currentMouseX;
    private int currentMouseY;

    public GOTGuiMap(Screen parent) {
        super(Component.translatable("got.gui.map.title"));
        this.parent = parent;
        this.posX = savedPosX;
        this.posY = savedPosY;
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevZoomPower = zoomPower;
    }

    @Override
    protected void init() {
        setupMapDimensions();

        // The original map opens on the player when a valid location is available.
        if (minecraft != null && minecraft.player != null && minecraft.level != null
                && minecraft.level.dimension().equals(GOTDimensions.PLANETOS)) {
            posX = (float) PlanetosMapSystem.worldToMapX(minecraft.player.getX());
            posY = (float) PlanetosMapSystem.worldToMapY(minecraft.player.getZ());
            prevPosX = posX;
            prevPosY = posY;
        }

        clampPosition();
    }

    private void setupMapDimensions() {
        if (fullscreen) {
            mapXMin = FULLSCREEN_MARGIN;
            mapXMax = width - FULLSCREEN_MARGIN;
            mapYMin = FULLSCREEN_MARGIN;
            mapYMax = height - FULLSCREEN_MARGIN;
        } else {
            mapXMin = width / 2 - WINDOWED_MAP_WIDTH / 2;
            mapXMax = width / 2 + WINDOWED_MAP_WIDTH / 2;
            int guiTop = (height - 256) / 2;
            mapYMin = guiTop;
            mapYMax = guiTop + WINDOWED_MAP_HEIGHT;
        }
        mapWidth = Math.max(1, mapXMax - mapXMin);
        mapHeight = Math.max(1, mapYMax - mapYMin);
    }

    private void setupZoomVariables(float partialTick) {
        zoomExp = zoomPower;
        if (zoomTicks > 0) {
            float progress = (ZOOM_TICKS_MAX - (zoomTicks - partialTick)) / ZOOM_TICKS_MAX;
            zoomExp = prevZoomPower + (zoomPower - prevZoomPower) * progress;
        }
        zoomScale = (float) Math.pow(2.0D, zoomExp);
        zoomScaleStable = (float) Math.pow(2.0D,
                zoomTicks == 0 ? zoomPower : Math.min(zoomPower, prevZoomPower));
    }

    @Override
    public void tick() {
        if (zoomTicks > 0) {
            zoomTicks--;
        }
        handleMapKeyboardMovement();
        prevPosX = posX;
        prevPosY = posY;
        posX += posXMove;
        posY += posYMove;
        posXMove *= 0.72F;
        posYMove *= 0.72F;
        if (Math.abs(posXMove) < 0.001F) posXMove = 0;
        if (Math.abs(posYMove) < 0.001F) posYMove = 0;
        clampPosition();
        savedPosX = posX;
        savedPosY = posY;
    }

    private void handleMapKeyboardMovement() {
        if (minecraft == null || minecraft.getWindow() == null) return;
        long window = minecraft.getWindow().getWindow();
        float amount = 8.0F / Math.max(zoomScaleStable, 0.0001F);
        if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_LEFT) == GLFW.GLFW_PRESS
                || GLFW.glfwGetKey(window, GLFW.GLFW_KEY_A) == GLFW.GLFW_PRESS) posXMove -= amount;
        if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_RIGHT) == GLFW.GLFW_PRESS
                || GLFW.glfwGetKey(window, GLFW.GLFW_KEY_D) == GLFW.GLFW_PRESS) posXMove += amount;
        if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_UP) == GLFW.GLFW_PRESS
                || GLFW.glfwGetKey(window, GLFW.GLFW_KEY_W) == GLFW.GLFW_PRESS) posYMove -= amount;
        if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_DOWN) == GLFW.GLFW_PRESS
                || GLFW.glfwGetKey(window, GLFW.GLFW_KEY_S) == GLFW.GLFW_PRESS) posYMove += amount;
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        renderBackground(graphics);
        this.currentMouseX = mouseX;
        this.currentMouseY = mouseY;
        setupZoomVariables(partialTick);

        float renderPosX = prevPosX + (posX - prevPosX) * partialTick;
        float renderPosY = prevPosY + (posY - prevPosY) * partialTick;

        drawMapFrame(graphics);
        mapRenderer.setPrevMapX(prevPosX);
        mapRenderer.setMapX(posX);
        mapRenderer.setPrevMapY(prevPosY);
        mapRenderer.setMapY(posY);
        mapRenderer.setZoomExp(zoomExp);
        mapRenderer.setZoomStable(zoomScaleStable);
        mapRenderer.renderMap(graphics, this, partialTick, mapXMin, mapYMin, mapXMax, mapYMax);

        renderMapWidgets(graphics, mouseX, mouseY);
        renderCoordinateReadout(graphics, mouseX, mouseY);
        renderReturnTab(graphics, mouseX, mouseY);
        super.render(graphics, mouseX, mouseY, partialTick);
    }

    void setFakeMapProperties(float mapX, float mapY, float zoom, float zoomExp, float zoomStable) {
        this.renderMapX = mapX;
        this.renderMapY = mapY;
        this.renderZoom = zoom;
        this.renderZoomExp = zoomExp;
        this.renderZoomStable = zoomStable;
    }

    int[] setFakeStaticProperties(int width, int height, int xMin, int xMax, int yMin, int yMax) {
        int[] previous = {renderWidth, renderHeight, renderXMin, renderXMax, renderYMin, renderYMax};
        renderWidth = width;
        renderHeight = height;
        renderXMin = xMin;
        renderXMax = xMax;
        renderYMin = yMin;
        renderYMax = yMax;
        return previous;
    }

    void restoreFakeStaticProperties(int[] previous) {
        if (previous == null || previous.length < 6) return;
        renderWidth = previous[0];
        renderHeight = previous[1];
        renderXMin = previous[2];
        renderXMax = previous[3];
        renderYMin = previous[4];
        renderYMax = previous[5];
    }

    void setEnableZoomOutWPFading(boolean enabled) {
        this.enableZoomOutWPFading = enabled;
    }

    void renderMapAndOverlay(GuiGraphics graphics, boolean sepia, float alpha, boolean renderOverlay) {
        graphics.enableScissor(renderXMin, renderYMin, renderXMax, renderYMax);

        float drawX = renderXMin + renderWidth / 2.0F - renderMapX * renderZoom;
        float drawY = renderYMin + renderHeight / 2.0F - renderMapY * renderZoom;
        int drawWidth = Math.max(1, Math.round(PlanetosMapSystem.MAP_WIDTH * renderZoom));
        int drawHeight = Math.max(1, Math.round(PlanetosMapSystem.MAP_HEIGHT * renderZoom));

        graphics.setColor(1.0F, sepia ? 0.90F : 1.0F, sepia ? 0.70F : 1.0F, alpha);
        graphics.blit(MAP_TEXTURE, Math.round(drawX), Math.round(drawY), drawWidth, drawHeight,
                0, 0, PlanetosMapSystem.MAP_WIDTH, PlanetosMapSystem.MAP_HEIGHT,
                PlanetosMapSystem.MAP_WIDTH, PlanetosMapSystem.MAP_HEIGHT);
        graphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);

        // The supplied overlay is kept as a distinct renderer layer, as in the original class.
        if (renderOverlay && renderZoom >= 1.0F) {
            // Region/road/conquest overlay data is not registered yet; no placeholder is invented here.
        }

        graphics.disableScissor();
    }

    void renderBeziers(GuiGraphics graphics, boolean interactive) {
        GOTBeziers.ensureInitialized();
        graphics.enableScissor(renderXMin, renderYMin, renderXMax, renderYMax);
        float drawX = renderXMin + renderWidth / 2.0F - renderMapX * renderZoom;
        float drawY = renderYMin + renderHeight / 2.0F - renderMapY * renderZoom;

        double mapPadding = 2.0D / Math.max(renderZoom, 0.0625F);
        double visibleMapMinX = renderMapX - renderWidth / (2.0D * renderZoom) - mapPadding;
        double visibleMapMaxX = renderMapX + renderWidth / (2.0D * renderZoom) + mapPadding;
        double visibleMapMinY = renderMapY - renderHeight / (2.0D * renderZoom) - mapPadding;
        double visibleMapMaxY = renderMapY + renderHeight / (2.0D * renderZoom) + mapPadding;
        double visibleWorldMinX = PlanetosMapSystem.mapToWorldX(visibleMapMinX);
        double visibleWorldMaxX = PlanetosMapSystem.mapToWorldX(visibleMapMaxX);
        double visibleWorldMinZ = PlanetosMapSystem.mapToWorldZ(visibleMapMinY);
        double visibleWorldMaxZ = PlanetosMapSystem.mapToWorldZ(visibleMapMaxY);

        for (GOTBeziers route : GOTBeziers.CONTENT) {
            GOTBeziers.BezierPoint[] points = route.getBezierPoints();
            if (points == null || points.length < 2) continue;
            if (!route.intersectsWorldBounds(visibleWorldMinX, visibleWorldMinZ,
                    visibleWorldMaxX, visibleWorldMaxZ)) continue;

            boolean wall = route.getType() == GOTBeziers.Type.WALL;
            int colour = wall ? 0xFF324B52 : 0xFF4A2D18;
            float spacing = wall ? WALL_SAMPLE_SPACING_BLOCKS : ROAD_DOT_SPACING_BLOCKS;
            int step = Math.max(1, Math.round(spacing / Math.max(renderZoomStable, 0.0625F)));

            for (int i = 0; i < points.length; i += step) {
                GOTBeziers.BezierPoint point = points[i];
                float mapPointX = (float) PlanetosMapSystem.worldToMapX(point.x());
                float mapPointY = (float) PlanetosMapSystem.worldToMapY(point.z());
                int x = Math.round(drawX + mapPointX * renderZoom);
                int y = Math.round(drawY + mapPointY * renderZoom);
                if (x >= renderXMin && x < renderXMax && y >= renderYMin && y < renderYMax) {
                    // Roads are the original one-pixel dotted atlas paths.  Walls
                    // use denser one-pixel samples so they still read as solid.
                    graphics.fill(x, y, x + 1, y + 1, colour);
                }
            }
        }
        graphics.disableScissor();
    }

    void renderWaypoints(GuiGraphics graphics, boolean interactive) {
        graphics.enableScissor(renderXMin, renderYMin, renderXMax, renderYMax);
        float drawX = renderXMin + renderWidth / 2.0F - renderMapX * renderZoom;
        float drawY = renderYMin + renderHeight / 2.0F - renderMapY * renderZoom;

        GOTWaypoint hovered = null;
        double nearest = Double.MAX_VALUE;
        for (GOTWaypoint waypoint : GOTWaypoint.values()) {
            if (waypoint.isHidden()) continue;
            float mapPointX = (float) PlanetosMapSystem.worldToMapX(waypoint.getCoordX());
            float mapPointY = (float) PlanetosMapSystem.worldToMapY(waypoint.getCoordZ());
            int x = Math.round(drawX + mapPointX * renderZoom);
            int y = Math.round(drawY + mapPointY * renderZoom);
            if (x < renderXMin - 4 || x > renderXMax + 4 || y < renderYMin - 4 || y > renderYMax + 4) continue;

            int size = renderZoom >= 2.0F ? 5 : 3;
            int half = size / 2;
            int colour = waypoint.hasPlayerUnlocked(minecraft == null ? null : minecraft.player)
                    ? 0xFFFFB52E : 0xFF7A6A58;
            graphics.fill(x - half, y - half, x - half + size, y - half + size, 0xFF2B1608);
            if (size > 2) graphics.fill(x - half + 1, y - half + 1, x - half + size - 1, y - half + size - 1, colour);

            double dx = currentMouseX - x;
            double dy = currentMouseY - y;
            double distance = dx * dx + dy * dy;
            double hitRadius = Math.max(4.0D, size + 1.0D);
            if (distance <= hitRadius * hitRadius && distance < nearest) {
                nearest = distance;
                hovered = waypoint;
            }
        }

        if (showLabels) {
            for (GOTMapLabels label : GOTMapLabels.allMapLabels()) {
                if (renderZoomExp < label.getMinZoom() || renderZoomExp > label.getMaxZoom()) continue;
                int x = Math.round(drawX + label.getPosX() * renderZoom);
                int y = Math.round(drawY + label.getPosY() * renderZoom);
                // Do not build pose/text draw calls for labels whose anchor is
                // nowhere near the current map viewport.
                if (x < renderXMin - 128 || x > renderXMax + 128
                        || y < renderYMin - 32 || y > renderYMax + 32) continue;
                float scale = Math.max(0.45F, Math.min(1.5F, label.getScale() * renderZoom / 30.0F));
                graphics.pose().pushPose();
                graphics.pose().translate(x, y, 0);
                graphics.pose().scale(scale, scale, 1.0F);
                graphics.drawCenteredString(font, label.getDisplayName(), 0, -4, 0xFFD8C49A);
                graphics.pose().popPose();
            }
        }
        graphics.disableScissor();

        if (hovered != null && currentMouseX >= renderXMin && currentMouseX <= renderXMax
                && currentMouseY >= renderYMin && currentMouseY <= renderYMax) {
            graphics.renderTooltip(font,
                    java.util.List.of(
                            hovered.getDisplayName(),
                            net.minecraft.network.chat.Component.literal(
                                    "X: " + hovered.getCoordX() + ", Z: " + hovered.getCoordZ())),
                    java.util.Optional.empty(), currentMouseX, currentMouseY);
        }
    }

    private void drawMapFrame(GuiGraphics graphics) {
        // The original map is clipped inside a dark/red ornamental border.
        graphics.fill(mapXMin - 4, mapYMin - 4, mapXMax + 4, mapYMax + 4, 0xFF190000);
        graphics.fill(mapXMin - 2, mapYMin - 2, mapXMax + 2, mapYMax + 2, 0xFF9B160F);
        graphics.fill(mapXMin, mapYMin, mapXMax, mapYMax, 0xFF000000);

        // Preserve the supplied original overlay asset without stretching it over the atlas.
        graphics.blit(MAP_OVERLAY_TEXTURE, mapXMin - 8, mapYMin - 8,
                0, 0, 16, 16, 256, 256);
    }


void renderPactMembers(GuiGraphics graphics) {
    if (minecraft == null || minecraft.player == null || minecraft.level == null
            || !minecraft.level.dimension().equals(GOTDimensions.PLANETOS)) return;

    String dimension = minecraft.level.dimension().location().toString();
    float drawX = renderXMin + renderWidth / 2.0F - renderMapX * renderZoom;
    float drawY = renderYMin + renderHeight / 2.0F - renderMapY * renderZoom;

    got.network.S2CPactDataPacket.MemberView hovered = null;
    double nearest = Double.MAX_VALUE;

    graphics.enableScissor(renderXMin, renderYMin, renderXMax, renderYMax);
    for (got.network.S2CPactDataPacket.MemberView member : got.client.pact.ClientGOTPactData.mapMembers()) {
        if (member.id().equals(minecraft.player.getUUID())) continue;
        if (!dimension.equals(member.dimension())) continue;

        float mapX = (float) PlanetosMapSystem.worldToMapX(member.x());
        float mapY = (float) PlanetosMapSystem.worldToMapY(member.z());
        int x = Math.round(drawX + mapX * renderZoom);
        int y = Math.round(drawY + mapY * renderZoom);
        if (x < renderXMin - 4 || x > renderXMax + 4 || y < renderYMin - 4 || y > renderYMax + 4) continue;

        graphics.fill(x - 3, y - 3, x + 4, y + 4, 0xFF2B1608);
        graphics.fill(x - 2, y - 2, x + 3, y + 3, 0xFF5DB6E8);

        double dx = currentMouseX - x;
        double dy = currentMouseY - y;
        double distance = dx * dx + dy * dy;
        if (distance <= 36.0D && distance < nearest) {
            nearest = distance;
            hovered = member;
        }
    }
    graphics.disableScissor();

    if (hovered != null) {
        graphics.renderTooltip(font,
                java.util.List.of(
                        net.minecraft.network.chat.Component.literal(hovered.name()),
                        net.minecraft.network.chat.Component.literal(
                                "X: " + Math.round(hovered.x()) + ", Z: " + Math.round(hovered.z()))
                ),
                java.util.Optional.empty(), currentMouseX, currentMouseY);
    }
}

    void renderPlayer(GuiGraphics graphics) {
        if (minecraft == null || minecraft.player == null || minecraft.level == null
                || !minecraft.level.dimension().equals(GOTDimensions.PLANETOS)) return;

        float drawX = renderXMin + renderWidth / 2.0F - renderMapX * renderZoom;
        float drawY = renderYMin + renderHeight / 2.0F - renderMapY * renderZoom;
        float mapX = (float) PlanetosMapSystem.worldToMapX(minecraft.player.getX());
        float mapY = (float) PlanetosMapSystem.worldToMapY(minecraft.player.getZ());
        int x = Math.round(drawX + mapX * renderZoom);
        int y = Math.round(drawY + mapY * renderZoom);

        int half = PLAYER_HEAD_SIZE / 2;
        if (x + half < renderXMin || x - half >= renderXMax
                || y + half < renderYMin || y - half >= renderYMax) return;

        graphics.enableScissor(renderXMin, renderYMin, renderXMax, renderYMax);
        graphics.fill(x - half - 1, y - half - 1, x + half + 1, y + half + 1, 0xFF2B1608);
        PlayerFaceRenderer.draw(graphics, minecraft.player.getSkinTextureLocation(),
                x - half, y - half, PLAYER_HEAD_SIZE);
        graphics.disableScissor();
    }

    private void renderMapWidgets(GuiGraphics graphics, int mouseX, int mouseY) {
        widgetZoomIn.setTexVIndex(widgetZoomIn.isMouseOver(mouseX - mapXMin, mouseY - mapYMin, mapWidth, mapHeight) ? 2 : 0);
        widgetZoomOut.setTexVIndex(widgetZoomOut.isMouseOver(mouseX - mapXMin, mouseY - mapYMin, mapWidth, mapHeight) ? 2 : 0);
        widgetFullscreen.setTexVIndex(widgetFullscreen.isMouseOver(mouseX - mapXMin, mouseY - mapYMin, mapWidth, mapHeight) ? 2 : 0);
        widgetLabels.setTexVIndex(showLabels ? 0 : 1);

        drawMapWidget(graphics, widgetZoomIn, mouseX, mouseY);
        drawMapWidget(graphics, widgetZoomOut, mouseX, mouseY);
        drawMapWidget(graphics, widgetFullscreen, mouseX, mouseY);
        drawMapWidget(graphics, widgetLabels, mouseX, mouseY);
        drawMapWidget(graphics, widgetCompass, mouseX, mouseY);
    }

    private void drawMapWidget(GuiGraphics graphics, GOTGuiMapWidget widget, int mouseX, int mouseY) {
        if (!widget.isVisible()) return;
        int x = mapXMin + widget.getMapXPos(mapWidth);
        int y = mapYMin + widget.getMapYPos(mapHeight);
        int size = widget.getWidth();
        graphics.blit(MAP_ICONS_TEXTURE, x, y, widget.getTexU(), widget.getTexV(), size, size, 256, 256);
        if (widget.isMouseOver(mouseX - mapXMin, mouseY - mapYMin, mapWidth, mapHeight)) {
            graphics.renderTooltip(font, widget.getTranslatedName(), mouseX, mouseY);
        }
    }

    private void renderCoordinateReadout(GuiGraphics graphics, int mouseX, int mouseY) {
        boolean mouseWithinMap = isInside(mouseX, mouseY, mapXMin, mapYMin, mapWidth, mapHeight);
        float mapPixelX = mouseWithinMap
                ? posX + (mouseX - (mapXMin + mapWidth / 2.0F)) / zoomScale
                : posX;
        float mapPixelY = mouseWithinMap
                ? posY + (mouseY - (mapYMin + mapHeight / 2.0F)) / zoomScale
                : posY;

        mouseXCoord = (int) Math.round(PlanetosMapSystem.mapToWorldX(mapPixelX));
        mouseZCoord = (int) Math.round(PlanetosMapSystem.mapToWorldZ(mapPixelY));

        Component coordinates = Component.translatable("got.gui.map.coords", mouseXCoord, mouseZCoord);
        graphics.drawCenteredString(font, coordinates, width / 2, mapYMax + 9, 0xFFFFFF);
        Component regionName = GOTMapRegionNames.atWorldPosition(mouseXCoord, mouseZCoord);
        graphics.drawCenteredString(font, regionName, width / 2, mapYMax + 21, 0xFFFFFF);
    }

    private void renderReturnTab(GuiGraphics graphics, int mouseX, int mouseY) {
        int tabX = 0;
        int tabY = Math.max(0, height / 2 - 18);
        boolean hovered = isInside(mouseX, mouseY, tabX, tabY, 58, 36);
        graphics.fill(tabX, tabY, 58, tabY + 36, hovered ? 0xFFE2C08F : 0xFFCDA978);
        graphics.fill(56, tabY, 58, tabY + 36, 0xFF701B16);
        graphics.drawString(font, Component.literal("Menu"), 10, tabY + 14, 0xFF4A170E, false);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
            int relativeMouseX = (int) mouseX - mapXMin;
            int relativeMouseY = (int) mouseY - mapYMin;
            if (widgetZoomIn.isMouseOver(relativeMouseX, relativeMouseY, mapWidth, mapHeight)) {
                zoomIn();
                return true;
            }
            if (widgetZoomOut.isMouseOver(relativeMouseX, relativeMouseY, mapWidth, mapHeight)) {
                zoomOut();
                return true;
            }
            if (widgetFullscreen.isMouseOver(relativeMouseX, relativeMouseY, mapWidth, mapHeight)) {
                fullscreen = !fullscreen;
                setupMapDimensions();
                clampPosition();
                return true;
            }
            if (widgetLabels.isMouseOver(relativeMouseX, relativeMouseY, mapWidth, mapHeight)) {
                showLabels = !showLabels;
                return true;
            }
            int tabY = Math.max(0, height / 2 - 18);
            if (isInside(mouseX, mouseY, 0, tabY, 58, 36)) {
                onClose();
                return true;
            }
            if (isInside(mouseX, mouseY, mapXMin, mapYMin, mapWidth, mapHeight)) {
                GOTWaypoint clickedWaypoint = getWaypointAt(mouseX, mouseY);
                if (clickedWaypoint != null && clickedWaypoint.hasPlayerUnlocked(minecraft == null ? null : minecraft.player)) {
                    if (minecraft != null) minecraft.setScreen(new GOTGuiFastTravel(this, clickedWaypoint));
                    return true;
                }
                dragging = true;
                previousMouseX = mouseX;
                previousMouseY = mouseY;
                return true;
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    private GOTWaypoint getWaypointAt(double mouseX, double mouseY) {
        float drawX = mapXMin + mapWidth / 2.0F - posX * zoomScale;
        float drawY = mapYMin + mapHeight / 2.0F - posY * zoomScale;
        GOTWaypoint nearest = null;
        double nearestDistance = Double.MAX_VALUE;
        for (GOTWaypoint waypoint : GOTWaypoint.values()) {
            if (waypoint.isHidden()) continue;
            float mapPointX = (float) PlanetosMapSystem.worldToMapX(waypoint.getCoordX());
            float mapPointY = (float) PlanetosMapSystem.worldToMapY(waypoint.getCoordZ());
            int x = Math.round(drawX + mapPointX * zoomScale);
            int y = Math.round(drawY + mapPointY * zoomScale);
            int markerSize = zoomScale >= 2.0F ? 5 : 3;
            double radius = Math.max(5.0D, markerSize + 2.0D);
            double dx = mouseX - x;
            double dy = mouseY - y;
            double distance = dx * dx + dy * dy;
            if (distance <= radius * radius && distance < nearestDistance) {
                nearestDistance = distance;
                nearest = waypoint;
            }
        }
        return nearest;
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (button == GLFW.GLFW_MOUSE_BUTTON_LEFT && dragging) {
            dragging = false;
            return true;
        }
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        if (dragging && button == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
            posX -= (float) ((mouseX - previousMouseX) / zoomScale);
            posY -= (float) ((mouseY - previousMouseY) / zoomScale);
            previousMouseX = mouseX;
            previousMouseY = mouseY;
            prevPosX = posX;
            prevPosY = posY;
            clampPosition();
            return true;
        }
        return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        if (isInside(mouseX, mouseY, mapXMin, mapYMin, mapWidth, mapHeight)) {
            if (delta > 0) zoomIn();
            if (delta < 0) zoomOut();
            return delta != 0;
        }
        return super.mouseScrolled(mouseX, mouseY, delta);
    }

    private void zoom(int amount) {
        int next = Math.max(MIN_ZOOM_POWER, Math.min(MAX_ZOOM_POWER, zoomPower + amount));
        if (next == zoomPower) return;
        prevZoomPower = zoomPower;
        zoomPower = next;
        zoomTicks = ZOOM_TICKS_MAX;
        clampPosition();
    }

    private void zoomIn() {
        zoom(1);
    }

    private void zoomOut() {
        zoom(-1);
    }

    private void clampPosition() {
        setupZoomVariables(1.0F);
        float halfVisibleWidth = mapWidth / (2.0F * Math.max(zoomScale, 0.0001F));
        float halfVisibleHeight = mapHeight / (2.0F * Math.max(zoomScale, 0.0001F));

        float minX = Math.min(halfVisibleWidth, PlanetosMapSystem.MAP_WIDTH / 2.0F);
        float maxX = Math.max(PlanetosMapSystem.MAP_WIDTH - halfVisibleWidth,
                PlanetosMapSystem.MAP_WIDTH / 2.0F);
        float minY = Math.min(halfVisibleHeight, PlanetosMapSystem.MAP_HEIGHT / 2.0F);
        float maxY = Math.max(PlanetosMapSystem.MAP_HEIGHT - halfVisibleHeight,
                PlanetosMapSystem.MAP_HEIGHT / 2.0F);

        posX = Math.max(minX, Math.min(maxX, posX));
        posY = Math.max(minY, Math.min(maxY, posY));
    }

    private static boolean isInside(double x, double y, int left, int top, int width, int height) {
        return x >= left && x < left + width && y >= top && y < top + height;
    }

    @Override
    public void onClose() {
        savedPosX = posX;
        savedPosY = posY;
        if (minecraft != null) minecraft.setScreen(parent != null ? parent : new GOTGuiMenu());
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
