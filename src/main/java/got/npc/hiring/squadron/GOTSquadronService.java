package got.npc.hiring.squadron;

import got.npc.hiring.GOTHiredData;
import got.npc.hiring.GOTHiredOrder;
import got.npc.hiring.command.GOTCommandToolSquadron;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;

import java.util.ArrayList;
import java.util.List;

/**
 * Shared squadron operations.
 *
 * This deliberately retains the legacy representation: squadron identity is
 * just a String on each hired NPC and on command items.
 */
public final class GOTSquadronService {
    private GOTSquadronService() {}

    public static List<PathfinderMob> nearbyOwned(ServerPlayer player, double radius) {
        AABB box = player.getBoundingBox().inflate(radius);
        return new ArrayList<>(player.level().getEntitiesOfClass(
            PathfinderMob.class,
            box,
            mob -> GOTHiredData.isOwner(mob, player.getUUID())
        ));
    }

    public static int assignNearby(ServerPlayer player, String squadron, double radius) {
        int count = 0;
        for (PathfinderMob mob : nearbyOwned(player, radius)) {
            GOTHiredData.setSquadron(mob, squadron);
            count++;
        }
        return count;
    }

    public static int clearNearby(ServerPlayer player, double radius) {
        int count = 0;
        for (PathfinderMob mob : nearbyOwned(player, radius)) {
            GOTHiredData.setSquadron(mob, "");
            count++;
        }
        return count;
    }

    public static int commandNearby(ServerPlayer player, String squadron, GOTHiredOrder order, double radius) {
        int count = 0;
        for (PathfinderMob mob : nearbyOwned(player, radius)) {
            String mobSquad = GOTHiredData.squadron(mob);
            boolean match = squadron == null || squadron.isBlank()
                ? mobSquad == null || mobSquad.isBlank()
                : mobSquad != null && mobSquad.equalsIgnoreCase(squadron);
            if (!match) continue;
            GOTHiredData.setOrder(mob, order);
            count++;
        }
        return count;
    }

    public static void setItemSquadron(ItemStack stack, String squadron) {
        GOTCommandToolSquadron.set(stack, squadron);
    }

    public static String getItemSquadron(ItemStack stack) {
        String s = GOTCommandToolSquadron.get(stack);
        return s == null ? "" : s;
    }
}
