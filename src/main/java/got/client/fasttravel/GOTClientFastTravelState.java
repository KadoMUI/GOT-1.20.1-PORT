package got.client.fasttravel;

import got.common.fasttravel.GOTFastTravelData;
import got.common.world.map.GOTWaypoint;
import java.util.EnumSet;
import java.util.List;

public final class GOTClientFastTravelState {
    private static EnumSet<GOTWaypoint.Region> unlocked = EnumSet.noneOf(GOTWaypoint.Region.class);
    private static List<GOTFastTravelData.Custom> custom = List.of();
    private GOTClientFastTravelState() {}
    public static void set(EnumSet<GOTWaypoint.Region> u, List<GOTFastTravelData.Custom> c) { unlocked = u.clone(); custom = List.copyOf(c); }
    public static boolean isUnlocked(GOTWaypoint w) { for (var r : w.getRegions()) if (unlocked.contains(r)) return true; return false; }
    public static List<GOTFastTravelData.Custom> custom() { return custom; }
}
