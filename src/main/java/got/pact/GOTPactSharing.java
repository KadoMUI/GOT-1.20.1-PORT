package got.pact;

import got.common.fasttravel.GOTFastTravelData;
import got.common.world.map.GOTWaypoint;
import net.minecraft.server.level.ServerPlayer;
import java.util.*;

/** Server-authoritative bridge between personal map/fast-travel state and Pact-shared knowledge. */
public final class GOTPactSharing {
    private GOTPactSharing() {}

    public static void importPlayer(ServerPlayer player) {
        GOTPact pact = GOTPactService.forPlayer(player.server, player.getUUID()).orElse(null);
        if (pact == null) return;
        boolean changed = false;
        for (GOTWaypoint.Region r : GOTFastTravelData.unlocked(player)) changed |= pact.shareFastTravelRegion(r);
        for (GOTFastTravelData.Custom c : GOTFastTravelData.custom(player)) {
            pact.publishWaypoint(new GOTPactWaypoint(player.getUUID(), c.id(), c.name(), c.x(), c.y(), c.z())); changed = true;
        }
        if (changed) GOTPactSavedData.get(player.server).setDirty();
    }

    public static void shareRegion(ServerPlayer player, GOTWaypoint.Region region) {
        GOTPact pact = GOTPactService.forPlayer(player.server, player.getUUID()).orElse(null);
        if (pact != null && pact.shareFastTravelRegion(region)) { GOTPactSavedData.get(player.server).setDirty(); syncOnline(pact, player); }
    }

    public static void publishWaypoint(ServerPlayer player, GOTFastTravelData.Custom c) {
        GOTPact pact = GOTPactService.forPlayer(player.server, player.getUUID()).orElse(null);
        if (pact == null) return;
        pact.publishWaypoint(new GOTPactWaypoint(player.getUUID(), c.id(), c.name(), c.x(), c.y(), c.z()));
        GOTPactSavedData.get(player.server).setDirty(); syncOnline(pact, player);
    }

    public static void renameWaypoint(ServerPlayer player, int id, String name) {
        GOTPact pact = GOTPactService.forPlayer(player.server, player.getUUID()).orElse(null);
        if (pact != null && pact.renameWaypoint(player.getUUID(), id, name)) { GOTPactSavedData.get(player.server).setDirty(); syncOnline(pact, player); }
    }

    public static void deleteWaypoint(ServerPlayer player, int id) {
        GOTPact pact = GOTPactService.forPlayer(player.server, player.getUUID()).orElse(null);
        if (pact != null && pact.removeWaypoint(player.getUUID(), id)) { GOTPactSavedData.get(player.server).setDirty(); syncOnline(pact, player); }
    }

    public static EnumSet<GOTWaypoint.Region> effectiveUnlocked(ServerPlayer player) {
        EnumSet<GOTWaypoint.Region> out = GOTFastTravelData.unlocked(player);
        GOTPactService.forPlayer(player.server, player.getUUID()).ifPresent(p -> out.addAll(p.sharedFastTravelRegions()));
        return out;
    }

    public static boolean isUnlocked(ServerPlayer player, GOTWaypoint waypoint) {
        if (waypoint == null || waypoint.isHidden()) return false;
        EnumSet<GOTWaypoint.Region> set = effectiveUnlocked(player);
        for (GOTWaypoint.Region region : waypoint.getRegions()) if (set.contains(region)) return true;
        return false;
    }

    public static List<GOTFastTravelData.Custom> effectiveWaypoints(ServerPlayer player) {
        ArrayList<GOTFastTravelData.Custom> out = new ArrayList<>();
        GOTPact pact = GOTPactService.forPlayer(player.server, player.getUUID()).orElse(null);
        if (pact == null) return GOTFastTravelData.custom(player);
        for (GOTPactWaypoint w : pact.sharedWaypoints()) out.add(w.asCustom());
        return out;
    }

    public static GOTFastTravelData.Custom waypoint(ServerPlayer player, UUID owner, int id) {
        if (owner.equals(player.getUUID())) return GOTFastTravelData.customById(player, id);
        GOTPact pact = GOTPactService.forPlayer(player.server, player.getUUID()).orElse(null);
        if (pact == null || !pact.contains(owner)) return null;
        GOTPactWaypoint w = pact.waypoint(owner, id); return w == null ? null : w.asCustom();
    }

    public static void syncOnline(GOTPact pact, ServerPlayer cause) {
        for (UUID id : pact.members()) { ServerPlayer p = cause.server.getPlayerList().getPlayer(id); if (p != null) got.common.fasttravel.GOTFastTravelManager.sync(p); }
    }
}
