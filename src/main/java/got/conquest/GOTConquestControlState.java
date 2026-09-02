package got.conquest;

/**
 * Political state of a waypoint independent from its immutable lore/native faction.
 * NATIVE means no conquest has displaced the lore controller yet; NEUTRAL means that
 * control has been broken; CONTESTED means multiple powers are actively competing;
 * CONTROLLED means a Pact currently holds the waypoint.
 */
public enum GOTConquestControlState {
    NATIVE,
    NEUTRAL,
    CONTESTED,
    CONTROLLED;

    public static GOTConquestControlState byName(String raw) {
        if (raw == null) return null;
        for (GOTConquestControlState state : values()) {
            if (state.name().equalsIgnoreCase(raw)) return state;
        }
        return null;
    }
}
