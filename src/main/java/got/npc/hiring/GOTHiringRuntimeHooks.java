package got.npc.hiring;

import got.npc.hiring.ai.GOTHiredAiParityHook;
import got.npc.hiring.inventory.GOTHiredEquipmentSync;
import net.minecraft.world.entity.PathfinderMob;

/**
 * Final Pass-6 runtime merge point.
 *
 * Call from the common hired-NPC tick path.
 */
public final class GOTHiringRuntimeHooks {
    private GOTHiringRuntimeHooks() {}

    /**
     * @return true if normal hired movement should stop for this tick.
     */
    public static boolean beforeHiredMovement(PathfinderMob mob) {
        return GOTHiredAiParityHook.beforeMovement(mob);
    }

    public static void periodicEquipmentSafetySync(PathfinderMob mob) {
        if (mob.tickCount % 20 == 0) {
            GOTHiredEquipmentSync.sync(mob);
        }
    }
}
