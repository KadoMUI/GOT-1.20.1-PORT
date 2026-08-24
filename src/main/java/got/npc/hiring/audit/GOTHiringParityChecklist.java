package got.npc.hiring.audit;

import java.util.List;

/**
 * Developer-visible parity checklist for the original hiring system.
 *
 * This is intentionally code-readable so future ports/updates can keep the
 * original 1.7.10 contract documented next to the implementation.
 */
public final class GOTHiringParityChecklist {
    private GOTHiringParityChecklist() {}

    public record Entry(String feature, Status status, String note) {}

    public enum Status {
        COMPLETE,
        COMPLETE_MODERN_EQUIVALENT,
        NEEDS_CONTENT_DATA,
        DEFERRED_BY_SCOPE
    }

    public static List<Entry> entries() {
        return List.of(
            new Entry("Hiring player UUID", Status.COMPLETE, "Persistent per NPC"),
            new Entry("Warrior/Farmer task", Status.COMPLETE, "Pass 1"),
            new Entry("Squadron string", Status.COMPLETE, "Legacy GOTSquadron semantics"),
            new Entry("Guard mode/radius", Status.COMPLETE, "HOLD + guard point/range"),
            new Entry("canMove Halt/Ready", Status.COMPLETE, "Separate command-state flag"),
            new Entry("Auto teleport", Status.COMPLETE, "Owner follow + GUI toggle"),
            new Entry("Command Sword", Status.COMPLETE, "64/6/12 legacy ranges"),
            new Entry("Command Horn", Status.COMPLETE, "Select/Halt/Ready/Summon"),
            new Entry("Hired inventory", Status.COMPLETE_MODERN_EQUIVALENT, "Warrior/Farmer containers"),
            new Entry("Replaced items", Status.COMPLETE, "Persistent restore store"),
            new Entry("Visible equipment sync", Status.COMPLETE, "Pass 6"),
            new Entry("Mob kills / XP / level", Status.COMPLETE, "Legacy curve retained"),
            new Entry("Patrol behavior", Status.COMPLETE_MODERN_EQUIVALENT, "Persistent route points"),
            new Entry("Alignment/pledge requirements", Status.NEEDS_CONTENT_DATA, "Engine complete; exact catalog values to transcribe"),
            new Entry("Per-unit coin prices", Status.NEEDS_CONTENT_DATA, "JSON catalog ready"),
            new Entry("Per-role farmer whitelist", Status.NEEDS_CONTENT_DATA, "Validation hook ready"),
            new Entry("Special mounts", Status.DEFERRED_BY_SCOPE, "Post-1.0"),
            new Entry("Siege units", Status.DEFERRED_BY_SCOPE, "Post-1.0")
        );
    }
}
