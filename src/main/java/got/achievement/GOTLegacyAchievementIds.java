package got.achievement;

import java.util.List;

/**
 * Exact static achievement identities recovered from the original achievement
 * registry. These remain stable audit IDs even when a 1.20.1 implementation
 * uses a vanilla advancement underneath.
 */
public final class GOTLegacyAchievementIds {
    private GOTLegacyAchievementIds() {}

    public static final String CONTENT = "CONTENT";
    public static final String ARMOR_ACHIEVEMENTS = "ARMOR_ACHIEVEMENTS";

    public static List<String> all() {
        return List.of(
            CONTENT,
            ARMOR_ACHIEVEMENTS
        );
    }
}
