package got.npc.hiring.command;

import net.minecraft.world.entity.PathfinderMob;

/**
 * Put this before normal hired movement logic in GOTHiredAi.tick(...).
 */
public final class GOTHiredCommandAiHook {
    private GOTHiredCommandAiHook() {}

    /**
     * @return true when ordinary follow/guard movement should not run this tick.
     */
    public static boolean blocksMovement(PathfinderMob mob) {
        if (!GOTHiredCommandState.halted(mob)) return false;
        mob.getNavigation().stop();
        if (mob.getTarget() != null) mob.setTarget(null);
        return true;
    }
}
