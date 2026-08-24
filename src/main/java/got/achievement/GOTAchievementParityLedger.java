package got.achievement;

import java.util.List;

public final class GOTAchievementParityLedger {
    private GOTAchievementParityLedger() {}

    public enum Status {
        AUDITED,
        PRESENT_IN_PORT,
        NEEDS_PORT,
        CONTENT_PASS,
        INTEGRATION_PASS
    }

    public record Entry(String feature, Status status, String note) {}

    public static List<Entry> entries() {
        return List.of(
            new Entry("Original achievement class/resource inventory", Status.AUDITED,
                    "See docs/ACHIEVEMENTS_PASS1_ORIGINAL_ENTRY_INVENTORY.txt"),
            new Entry("Original achievement/rank API", Status.AUDITED,
                    "See docs/ACHIEVEMENTS_PASS1_ORIGINAL_API_AUDIT.txt"),
            new Entry("Modern advancement/rank references", Status.AUDITED,
                    "See docs/ACHIEVEMENTS_PASS1_MODERN_SOURCE_HITS.json"),
            new Entry("Exact original achievement catalog", Status.CONTENT_PASS,
                    "Pass 2: recover IDs, icons, triggers, relationships and descriptions"),
            new Entry("Faction rank thresholds / benefits", Status.CONTENT_PASS,
                    "Pass 2: recover exact original rank data"),
            new Entry("Achievement persistence / sync", Status.NEEDS_PORT,
                    "Use modern advancements where equivalent; custom state where required"),
            new Entry("Ranks GUI / Titles integration", Status.INTEGRATION_PASS,
                    "Pass 3: presentation and final fidelity audit")
        );
    }
}
