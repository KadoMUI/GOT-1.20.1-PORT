package got.npc.hiring;

/**
 * Persistent high-level command state for a hired NPC.
 *
 * FOLLOW       - stay with the owner
 * HOLD         - remain at the current guard point
 * PATROL       - reserved for the next AI pass
 * WANDER       - remain hired but behave locally
 */
public enum GOTHiredOrder {
    FOLLOW,
    HOLD,
    PATROL,
    WANDER
}
