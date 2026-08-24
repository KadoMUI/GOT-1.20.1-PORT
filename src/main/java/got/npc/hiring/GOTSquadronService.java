package got.npc.hiring;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.phys.AABB;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Modern squadron selection/command helper.
 *
 * The legacy mod represented a squadron by a String on each hired NPC.
 * We retain that behavior: no heavyweight separate database is required.
 */
public final class GOTSquadronService {
    private GOTSquadronService() {}

    public static List<PathfinderMob> nearbyOwned(ServerPlayer player, double range) {
        AABB box = player.getBoundingBox().inflate(range);
        List<PathfinderMob> result = new ArrayList<>();
        for (PathfinderMob mob : player.level().getEntitiesOfClass(PathfinderMob.class, box)) {
            if (GOTHiredData.isOwner(mob, player.getUUID())) {
                result.add(mob);
            }
        }
        return result;
    }

    public static List<PathfinderMob> nearbySquadron(ServerPlayer player, String squadron, double range) {
        String normalized = GOTHiredData.normalizeSquadron(squadron);
        List<PathfinderMob> result = new ArrayList<>();
        for (PathfinderMob mob : nearbyOwned(player, range)) {
            if (normalized.equalsIgnoreCase(GOTHiredData.squadron(mob))) {
                result.add(mob);
            }
        }
        return result;
    }

    public static int command(ServerPlayer player, String squadron, GOTHiredOrder order, double range) {
        List<PathfinderMob> units = nearbySquadron(player, squadron, range);
        units.forEach(unit -> GOTHiredData.setOrder(unit, order));
        return units.size();
    }

    public static int assignNearby(ServerPlayer player, String squadron, double range) {
        List<PathfinderMob> units = nearbyOwned(player, range);
        units.forEach(unit -> GOTHiredData.setSquadron(unit, squadron));
        return units.size();
    }
}
