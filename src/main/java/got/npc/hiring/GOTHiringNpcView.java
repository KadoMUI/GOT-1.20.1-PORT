package got.npc.hiring;

/**
 * Small adapter interface exposing only the information the hiring system needs.
 *
 * Existing NPC classes do not have to inherit another base class; their common
 * GOT NPC base can implement this interface once and delegate to the current
 * faction/role system.
 */
public interface GOTHiringNpcView {
    String gotHiringRoleId();
    String gotHiringFactionId();
    boolean gotIsActiveCombatant();
    boolean gotIsCivilian();
}
