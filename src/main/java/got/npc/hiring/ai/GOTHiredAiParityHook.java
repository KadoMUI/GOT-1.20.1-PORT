package got.npc.hiring.ai;

import got.npc.hiring.command.GOTHiredCommandState;
import net.minecraft.world.entity.PathfinderMob;

/**
 * One integration point to place near the top of the existing GOTHiredAi.tick().
 */
public final class GOTHiredAiParityHook {
    private GOTHiredAiParityHook() {}

    /**
     * @return true when normal FOLLOW/HOLD movement should stop processing.
     */
    public static boolean beforeMovement(PathfinderMob mob) {
        if (GOTHiredCommandState.halted(mob)) {
            mob.getNavigation().stop();
            mob.setTarget(null);
            return true;
        }

        if (GOTHiredCombatPolicy.shouldDropCurrentTarget(mob)) {
            mob.setTarget(null);
            mob.getNavigation().stop();
        }

        return GOTHiredPatrolAi.tick(mob);
    }
}
