package got.speech;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;

/**
 * Small public context object for trading/quest systems that need dialogue
 * without duplicating faction/role checks.
 */
public record GOTDialogueContext(
    Entity npc,
    ServerPlayer player,
    String roleId,
    GOTSpeechSelector.Disposition disposition,
    String defaultBank
) {
    public static GOTDialogueContext of(Entity npc, ServerPlayer player) {
        GOTSpeechSelector.Disposition disposition = npc instanceof got.npc.GOTFactionNpc factionNpc
            ? GOTSpeechSelector.disposition(factionNpc, npc, player)
            : GOTSpeechSelector.Disposition.FRIENDLY;
        return new GOTDialogueContext(
            npc,
            player,
            GOTSpeechRoleResolver.roleId(npc),
            disposition,
            GOTSpeechSelector.defaultBank(npc, player)
        );
    }
}
