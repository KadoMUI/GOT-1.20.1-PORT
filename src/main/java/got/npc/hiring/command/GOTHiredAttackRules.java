package got.npc.hiring.command;

import got.npc.hiring.GOTHiredData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;

/**
 * Single bridge for the mod's full faction/Pact friendliness rules.
 *
 * The fallback rules below prevent the owner and the owner's own hired units
 * from being selected. When merging, delegate the final return to the existing
 * GOT NPC attack/faction relationship service so Command Sword exactly matches
 * ordinary faction combat.
 */
public final class GOTHiredAttackRules {
    private GOTHiredAttackRules() {}

    public static boolean canAttack(ServerPlayer owner, PathfinderMob attacker, LivingEntity target) {
        if (!target.isAlive() || target == attacker || target == owner) return false;
        if (target instanceof PathfinderMob other && GOTHiredData.isOwner(other, owner.getUUID())) return false;

        // MERGE HOOK:
        // return GOTFactionRules.canNpcAttack(attacker, target, true);
        return true;
    }
}
