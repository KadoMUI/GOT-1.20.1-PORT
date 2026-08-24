package got.common.world.map;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public interface GOTAbstractWaypoint {
    enum WaypointLockState {
        UNLOCKED,
        STANDARD_LOCKED,
        FACTION_LOCKED,
        CONQUEST_LOCKED,
        HIDDEN
    }

    String getCodeName();
    Component getDisplayName();
    int getID();
    GOTWaypoint getInstance();
    WaypointLockState getLockState(Player player);
    Component getLoreText(Player player);
    int getRotation();
    double getShiftX();
    double getShiftY();
    double getImgX();
    int getCoordX();
    double getImgY();
    int getCoordY(Level level, int x, int z);
    int getCoordYSaved();
    int getCoordZ();
    boolean hasPlayerUnlocked(Player player);
    boolean isHidden();
}
