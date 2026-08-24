package got.client;

import got.GOTMod;
import got.world.GOTDimensions;
import got.world.PlanetosMapSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.PlayerFaceRenderer;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public final class GOTMapScreen extends Screen {
    private static final ResourceLocation MAP = ResourceLocation.fromNamespaceAndPath(GOTMod.MOD_ID, "textures/map/map.png");
    private final Screen parent;
    public GOTMapScreen(Screen parent) { super(Component.translatable("screen.got.map")); this.parent = parent; }
    @Override public void onClose() { minecraft.setScreen(parent); }
    @Override public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        renderBackground(graphics);
        int margin = 12;
        double scale = Math.min((width - margin * 2.0) / PlanetosMapSystem.MAP_WIDTH, (height - margin * 2.0) / PlanetosMapSystem.MAP_HEIGHT);
        int drawW = Math.max(1, (int)(PlanetosMapSystem.MAP_WIDTH * scale));
        int drawH = Math.max(1, (int)(PlanetosMapSystem.MAP_HEIGHT * scale));
        int left = (width - drawW) / 2;
        int top = (height - drawH) / 2;
        graphics.blit(MAP, left, top, 0, 0, drawW, drawH, PlanetosMapSystem.MAP_WIDTH, PlanetosMapSystem.MAP_HEIGHT);
        graphics.fill(left - 1, top - 1, left + drawW + 1, top, 0xFFFFFFFF);
        graphics.fill(left - 1, top + drawH, left + drawW + 1, top + drawH + 1, 0xFFFFFFFF);
        if (minecraft.player != null && minecraft.level != null && minecraft.level.dimension().equals(GOTDimensions.PLANETOS)) {
            int px = left + (int)(PlanetosMapSystem.worldToMapX(minecraft.player.getX()) * scale);
            int py = top + (int)(PlanetosMapSystem.worldToMapY(minecraft.player.getZ()) * scale);
            int headSize = 8;
            int half = headSize / 2;
            graphics.fill(px - half - 1, py - half - 1, px + half + 1, py + half + 1, 0xFF2B1608);
            PlayerFaceRenderer.draw(graphics, minecraft.player.getSkinTextureLocation(),
                    px - half, py - half, headSize);
        }
        graphics.drawString(font, Component.translatable("screen.got.map.hint"), 8, 8, 0xFFFFFF);
        super.render(graphics, mouseX, mouseY, partialTick);
    }
}
