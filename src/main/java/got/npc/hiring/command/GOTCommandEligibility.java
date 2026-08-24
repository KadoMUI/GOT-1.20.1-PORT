package got.npc.hiring.command;

import got.npc.hiring.GOTHiredData;
import got.npc.hiring.GOTHiredOrder;
import got.npc.hiring.GOTHiredTask;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.item.ItemStack;

/**
 * Mirrors the old GOTHireableInfo obey rules:
 * command sword and horn commands are for hired WARRIOR units that are not
 * currently in guard mode.
 *
 * In the modern hired model, HOLD is the guard-mode state.
 */
public final class GOTCommandEligibility {
    private GOTCommandEligibility() {}

    public static boolean obeysSwordOrHorn(ServerPlayer owner, PathfinderMob mob, ItemStack tool) {
        return GOTHiredData.isOwner(mob, owner.getUUID())
            && GOTHiredData.task(mob) == GOTHiredTask.WARRIOR
            && GOTHiredData.order(mob) != GOTHiredOrder.HOLD
            && GOTCommandToolSquadron.compatible(GOTHiredData.squadron(mob), tool);
    }
}
