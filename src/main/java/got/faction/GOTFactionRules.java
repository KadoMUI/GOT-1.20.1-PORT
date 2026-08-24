package got.faction;

import got.npc.GOTFactionNpc;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;

public final class GOTFactionRules {
    private GOTFactionRules() {}

    public static GOTFaction factionOf(LivingEntity entity) {
        if (entity instanceof GOTFactionNpc npc) return npc.getFaction();
        if (entity instanceof Player player) return GOTFactionPlayerData.get(player).membership();
        return GOTFaction.UNALIGNED;
    }

    /** Natural acquisition: enemies and mortal enemies, plus personally hostile players. */
    public static boolean shouldNpcTarget(LivingEntity attacker, LivingEntity target) {
        if (!(attacker instanceof GOTFactionNpc attackerNpc) || !target.isAlive() || attacker == target) return false;
        GOTFaction attackerFaction = attackerNpc.getFaction();
        if (target instanceof Player player) {
            GOTFactionPlayerData data = GOTFactionPlayerData.get(player);
            if (data.alignment(attackerFaction) < 0.0F) return true;
            GOTFaction membership = data.membership();
            return membership != GOTFaction.UNALIGNED && attackerFaction.isBadRelation(membership);
        }
        if (target instanceof GOTFactionNpc targetNpc) {
            return attackerFaction.isBadRelation(targetNpc.getFaction());
        }
        return false;
    }

    /** Retaliation permits neutral targets but never allies or friends. */
    public static boolean canNpcRetaliate(LivingEntity attacker, LivingEntity target) {
        if (!(attacker instanceof GOTFactionNpc attackerNpc) || target == null || !target.isAlive()) return false;
        if (target instanceof Player) return true;
        GOTFaction targetFaction = factionOf(target);
        return targetFaction == GOTFaction.UNALIGNED || !attackerNpc.getFaction().isGoodRelation(targetFaction);
    }

    public static boolean canNpcDamage(LivingEntity attacker, LivingEntity target) {
        if (!(attacker instanceof GOTFactionNpc attackerNpc)) return true;
        GOTFaction targetFaction = factionOf(target);
        if (targetFaction == GOTFaction.UNALIGNED) return true;
        if (attacker instanceof Mob mob && mob.getTarget() == target) return true;
        return !attackerNpc.getFaction().isGoodRelation(targetFaction);
    }

    public static boolean isHostileToPlayer(GOTFaction npcFaction, ServerPlayer player) {
        GOTFactionPlayerData data = GOTFactionPlayerData.get(player);
        if (data.alignment(npcFaction) < 0.0F) return true;
        GOTFaction membership = data.membership();
        return membership != GOTFaction.UNALIGNED && npcFaction.isBadRelation(membership);
    }
}
