package got.npc.hiring.inventory;

import net.minecraft.world.entity.LivingEntity;

/**
 * Call this after changes to warrior equipment slots.
 *
 * Keeps renderer/combat equipment synchronized immediately instead of waiting
 * for a periodic tick.
 */
public final class GOTHiredEquipmentSlotHook {
    private GOTHiredEquipmentSlotHook() {}

    public static void changed(LivingEntity npc, int slot) {
        if (slot >= 0 && slot <= 4) {
            GOTHiredReplacementService.restoreIfEmpty(npc, slot);
            GOTHiredEquipmentSync.sync(npc);
        }
    }
}
