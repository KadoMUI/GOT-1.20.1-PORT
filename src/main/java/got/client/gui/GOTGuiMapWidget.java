package got.client.gui;

import net.minecraft.network.chat.Component;

/**
 * Direct 1.20.1 source port of the original GOTGuiMapWidget value object.
 * Negative positions remain anchored to the far edge of the map viewport,
 * exactly as in 1.7.10.
 */
public final class GOTGuiMapWidget {
    private final String name;
    private final int xPos;
    private final int yPos;
    private final int texUBase;
    private final int texVBase;
    private final int width;
    private boolean visible = true;
    private int texVIndex;

    public GOTGuiMapWidget(int xPos, int yPos, int width, String name, int texUBase, int texVBase) {
        this.xPos = xPos;
        this.yPos = yPos;
        this.width = width;
        this.name = name;
        this.texUBase = texUBase;
        this.texVBase = texVBase;
    }

    public int getMapXPos(int mapWidth) {
        return xPos < 0 ? mapWidth + xPos : xPos;
    }

    public int getMapYPos(int mapHeight) {
        return yPos < 0 ? mapHeight + yPos : yPos;
    }

    public int getTexU() {
        return texUBase;
    }

    public int getTexV() {
        return texVBase + texVIndex * width;
    }

    public Component getTranslatedName() {
        return Component.translatable("got.gui.map.widget." + name);
    }

    public boolean isMouseOver(int mouseX, int mouseY, int mapWidth, int mapHeight) {
        return visible
                && mouseX >= getMapXPos(mapWidth)
                && mouseX < getMapXPos(mapWidth) + width
                && mouseY >= getMapYPos(mapHeight)
                && mouseY < getMapYPos(mapHeight) + width;
    }

    public void setTexVIndex(int texVIndex) {
        this.texVIndex = texVIndex;
    }

    public int getWidth() {
        return width;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }
}
