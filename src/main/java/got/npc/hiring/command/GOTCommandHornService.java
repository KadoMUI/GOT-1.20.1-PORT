package got.npc.hiring.command;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * Faithful horn semantics:
 *
 * HALT   -> canMove false, clear attack target, then horn switches to READY
 * READY  -> canMove true, then horn switches to HALT
 * SUMMON -> teleport compatible hired warriors to the hiring player
 *
 * The original iterated the world's loaded entity list, with no distance cap.
 * This implementation likewise commands all currently loaded compatible units
 * in the player's current dimension.
 */
public final class GOTCommandHornService {
    private GOTCommandHornService() {}

    public static int execute(ServerPlayer player, ItemStack horn, GOTCommandHornMode mode) {
        ServerLevel level = player.serverLevel();
        List<PathfinderMob> units = new ArrayList<>();

        for (var entity : level.getAllEntities()) {
            if (entity instanceof PathfinderMob mob
                && GOTCommandEligibility.obeysSwordOrHorn(player, mob, horn)) {
                units.add(mob);
            }
        }

        int affected = 0;
        for (PathfinderMob mob : units) {
            switch (mode) {
                case HALT -> {
                    GOTHiredCommandState.halt(mob);
                    affected++;
                }
                case READY -> {
                    GOTHiredCommandState.ready(mob);
                    affected++;
                }
                case SUMMON -> {
                    mob.teleportTo(player.getX(), player.getY(), player.getZ());
                    mob.getNavigation().stop();
                    affected++;
                }
                default -> {}
            }
        }

        return affected;
    }
}
