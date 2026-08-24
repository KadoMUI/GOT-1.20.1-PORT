package got.lore;

import java.util.List;

/**
 * Lore Pass 1 forensic parity ledger.
 *
 * This class intentionally does not fabricate lore content.  It records the
 * implementation categories that subsequent passes must bind to evidence
 * recovered from the original 24.08.29 JAR.
 */
public final class GOTLoreParityLedger {
    private GOTLoreParityLedger() {}

    public enum Status {
        AUDITED,
        PRESENT_IN_PORT,
        NEEDS_PORT,
        CONTENT_PASS
    }

    public record Entry(String feature, Status status, String note) {}

    public static List<Entry> entries() {
        return List.of(
            new Entry("Original lore class/resource inventory", Status.AUDITED,
                    "See docs/LORE_PASS1_ORIGINAL_ENTRY_INVENTORY.txt"),
            new Entry("Original lore API/disassembly", Status.AUDITED,
                    "See docs/LORE_PASS1_ORIGINAL_API_AUDIT.txt"),
            new Entry("Current-port lore references", Status.AUDITED,
                    "See docs/LORE_PASS1_MODERN_SOURCE_HITS.json"),
            new Entry("Exact lore catalog/text", Status.CONTENT_PASS,
                    "Recover in Pass 2; do not invent missing entries"),
            new Entry("Unlock/persistence integration", Status.NEEDS_PORT,
                    "Implement only from recovered original behavior"),
            new Entry("Lore GUI/menu fidelity", Status.NEEDS_PORT,
                    "Presentation/final integration belongs to Pass 3")
        );
    }
}
