package got.achievement;

/**
 * Modern value object for exact faction-rank data once recovered from the
 * original GOTFactionRank constructor/state.
 */
public record GOTFactionRankDefinition(
        String legacyId,
        String displayName,
        float alignmentThreshold
) {}
