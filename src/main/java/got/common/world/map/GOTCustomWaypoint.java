package got.common.world.map;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.Heightmap;

public final class GOTCustomWaypoint implements GOTAbstractWaypoint {
    private final int mapX;
    private final int mapY;
    private final int xCoord;
    private final int zCoord;
    private final int id;
    private String customName;
    private int yCoord;

    public GOTCustomWaypoint(String name, int mapX, int mapY, int xCoord, int yCoord, int zCoord, int id) {
        this.customName = validateCustomName(name);
        this.mapX = mapX;
        this.mapY = mapY;
        this.xCoord = xCoord;
        this.yCoord = yCoord;
        this.zCoord = zCoord;
        this.id = id;
    }

    public static String validateCustomName(String name) {
        String value = name == null ? "" : name.trim();
        if (value.isEmpty()) value = "Waypoint";
        return value.length() > 32 ? value.substring(0, 32) : value;
    }

    public void rename(String name) { this.customName = validateCustomName(name); }
    public String getCodeName() { return "custom_" + id; }
    public Component getDisplayName() { return Component.literal(customName); }
    public int getID() { return id; }
    public GOTWaypoint getInstance() { return null; }
    public WaypointLockState getLockState(Player player) { return WaypointLockState.UNLOCKED; }
    public Component getLoreText(Player player) { return Component.empty(); }
    public int getRotation() { return 0; }
    public double getShiftX() { return 0.0D; }
    public double getShiftY() { return 0.0D; }
    public double getImgX() { return mapX; }
    public int getCoordX() { return xCoord; }
    public double getImgY() { return mapY; }
    public int getCoordY(Level level, int x, int z) {
        return yCoord > 0 || level == null ? yCoord : level.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, x, z);
    }
    public int getCoordYSaved() { return yCoord; }
    public int getCoordZ() { return zCoord; }
    public boolean hasPlayerUnlocked(Player player) { return true; }
    public boolean isHidden() { return false; }
}
