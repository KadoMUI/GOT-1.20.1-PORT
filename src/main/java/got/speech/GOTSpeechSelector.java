package got.speech;

import got.faction.GOTFaction;
import got.faction.GOTFactionPlayerData;
import got.npc.GOTFactionNpc;
import got.npc.hiring.GOTHireDefinition;
import got.npc.hiring.GOTHiredData;
import got.npc.hiring.GOTHiredTask;
import got.npc.hiring.GOTHiringPlayerRules;
import got.npc.hiring.GOTHiringRuleService;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;

/**
 * Modern reconstruction of GOTSpeech#getDefaultSpeech with Pass-2 named NPC,
 * special-role and neutral-hireable routing.
 */
public final class GOTSpeechSelector {
    public enum Disposition { FRIENDLY, NEUTRAL, HOSTILE }

    private GOTSpeechSelector() {}

    public static String defaultBank(Entity entity, ServerPlayer player) {
        if (!(entity instanceof GOTFactionNpc npc)) return "standard/default_friendly";

        Disposition disposition = disposition(npc, entity, player);
        boolean friendly = disposition != Disposition.HOSTILE;

        // Named/special dialogue always gets first refusal.
        String dedicated = GOTSpeechSpecificSelector.dedicatedBank(entity, friendly);
        if (dedicated != null) return dedicated;

        String role = GOTSpeechRoleResolver.roleId(entity);
        boolean child = isChildRole(role);
        boolean farmer = role.contains("farmer") || role.contains("farmhand") || role.contains("slave");
        boolean bartender = role.contains("bartender") || role.contains("innkeeper");
        boolean smith = role.contains("smith");
        boolean trader = role.contains("trader") || role.contains("merchant") || bartender || smith || farmer;
        boolean hireable = GOTHiringRuleService.definitionFor(entity) != null;

        if (disposition == Disposition.NEUTRAL && hireable) {
            return trader ? "standard/default_neutral_unit_trader" : "standard/default_neutral_unit";
        }

        if (disposition == Disposition.FRIENDLY) {
            if (GOTHiredData.isOwner(entity, player.getUUID())) {
                if (GOTHiredData.task(entity) == GOTHiredTask.WARRIOR) return "standard/default_friendly_unit_warrior";
                if (GOTHiredData.task(entity) == GOTHiredTask.FARMER) return "standard/default_friendly_unit_farmhand";
            }
            if (child) return femaleChildRole(role)
                ? "standard/default_friendly_kid_woman"
                : "standard/default_friendly_kid_man";
            if (farmer) return "standard/default_friendly_trader_farmer";
            if (bartender) return "standard/default_friendly_trader_bartender";
            if (smith) return "standard/default_friendly_trader_smith";
            if (trader) return "standard/default_friendly_trader_default";
            if (hireable) return "standard/default_friendly_trader_unit";
            return "standard/default_friendly";
        }

        if (child) return "standard/default_hostile_kid";
        if (trader) return "standard/default_hostile_trader_default";
        if (hireable) return "standard/default_hostile_trader_unit";
        if (npc.isActiveCombatant()) return "standard/default_hostile_warrior";
        return "standard/default_hostile";
    }

    public static Disposition disposition(GOTFactionNpc npc, Entity entity, ServerPlayer player) {
        if (GOTHiredData.isOwner(entity, player.getUUID())) return Disposition.FRIENDLY;

        GOTFaction npcFaction = npc.getFaction();
        GOTFactionPlayerData data = GOTFactionPlayerData.get(player);
        float alignment = data.alignment(npcFaction);

        if (alignment < 0.0F) return Disposition.HOSTILE;

        GOTFaction membership = data.membership();
        if (membership != GOTFaction.UNALIGNED && membership.relationTo(npcFaction).isBad()) {
            return Disposition.HOSTILE;
        }

        // The legacy neutral unit banks represent a player who is not an enemy
        // but has not yet reached this unit's hiring reputation threshold.
        GOTHireDefinition hire = GOTHiringRuleService.definitionFor(entity);
        if (hire != null) {
            if (hire.pledgeExclusive() && !GOTHiringPlayerRules.pledgedTo(player, npc.getFactionId())) {
                return Disposition.NEUTRAL;
            }
            if (alignment < hire.requiredAlignment()) return Disposition.NEUTRAL;
        }

        return Disposition.FRIENDLY;
    }

    public static boolean isFriendly(GOTFactionNpc npc, ServerPlayer player) {
        if (!(npc instanceof Entity entity)) return true;
        return disposition(npc, entity, player) == Disposition.FRIENDLY;
    }

    private static boolean isChildRole(String role) {
        return role.contains("child") || role.contains("kid") || role.contains("boy") || role.contains("girl")
            || role.equals("bran_stark") || role.equals("rickon_stark") || role.equals("shireen_baratheon")
            || role.equals("robin_arryn") || role.equals("tommen_baratheon") || role.equals("myrcella_baratheon");
    }

    private static boolean femaleChildRole(String role) {
        return role.contains("girl") || role.equals("shireen_baratheon") || role.equals("myrcella_baratheon");
    }
}
