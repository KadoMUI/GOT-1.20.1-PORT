package got.conquest.diplomacy;

/** Human-readable interpretation of the directional opinion score. */
public enum GOTDiplomaticAttitude {
    FRIENDLY,
    FAVORABLE,
    NEUTRAL,
    WARY,
    HOSTILE,
    HATRED;

    public static GOTDiplomaticAttitude fromOpinion(int opinion) {
        if (opinion >= 250) return FRIENDLY;
        if (opinion >= 100) return FAVORABLE;
        if (opinion > -100) return NEUTRAL;
        if (opinion > -250) return WARY;
        if (opinion > -750) return HOSTILE;
        return HATRED;
    }
}
