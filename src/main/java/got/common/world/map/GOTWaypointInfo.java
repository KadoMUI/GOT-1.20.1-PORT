package got.common.world.map;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public final class GOTWaypointInfo implements GOTAbstractWaypoint {
    private final GOTWaypoint waypoint;
    private final double shiftX;
    private final double shiftY;
    private final int rotation;

    public GOTWaypointInfo(GOTWaypoint waypoint, double shiftX, double shiftY, int rotation) {
        this.waypoint = waypoint;
        this.shiftX = shiftX;
        this.shiftY = shiftY;
        this.rotation = rotation;
    }

    public String getCodeName() { return waypoint.getCodeName(); }
    public Component getDisplayName() { return waypoint.getDisplayName(); }
    public int getID() { return waypoint.getID(); }
    public GOTWaypoint getInstance() { return waypoint; }
    public WaypointLockState getLockState(Player player) { return waypoint.getLockState(player); }
    public Component getLoreText(Player player) { return waypoint.getLoreText(player); }
    public int getRotation() { return rotation; }
    public double getShiftX() { return shiftX; }
    public double getShiftY() { return shiftY; }
    public double getImgX() { return waypoint.getImgX() + shiftX; }
    public int getCoordX() { return waypoint.getCoordX() + GOTWaypoint.mapToWorldR(shiftX); }
    public double getImgY() { return waypoint.getImgY() + shiftY; }
    public int getCoordY(Level level, int x, int z) { return waypoint.getCoordY(level, x, z); }
    public int getCoordYSaved() { return waypoint.getCoordYSaved(); }
    public int getCoordZ() { return waypoint.getCoordZ() + GOTWaypoint.mapToWorldR(shiftY); }
    public boolean hasPlayerUnlocked(Player player) { return waypoint.hasPlayerUnlocked(player); }
    public boolean isHidden() { return waypoint.isHidden(); }
}
