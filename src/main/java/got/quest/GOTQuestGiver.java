package got.quest;

import got.faction.GOTFaction;

/**
 * Small adapter implemented by quest-capable NPCs.  It deliberately contains
 * no GOT-region-specific entity type, so another project can reuse the quest
 * engine by exposing the same stable role and faction identifiers.
 */
public interface GOTQuestGiver {
    String getQuestRoleId();

    GOTFaction getQuestFaction();

    boolean canOfferQuests();
}
