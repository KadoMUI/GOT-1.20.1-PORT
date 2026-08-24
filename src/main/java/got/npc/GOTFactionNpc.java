package got.npc;

import got.faction.GOTFaction;

/**
 * Stable bridge for the forthcoming Alignment system. Regional NPC entities
 * expose their faction and the exact legacy alignment value without making
 * the population milestone depend on the later reputation implementation.
 */
public interface GOTFactionNpc {
    String getFactionId();

    int getAlignmentBonus();

    default GOTFaction getFaction() {
        return GOTFaction.byId(getFactionId()).orElse(GOTFaction.UNALIGNED);
    }

    /** Civilians do not award enemy reputation to factions that reject war crimes. */
    boolean isCivilian();

    /** Whether this NPC should naturally seek hostile faction targets. */
    boolean isActiveCombatant();
}
