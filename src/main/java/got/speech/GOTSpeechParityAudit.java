package got.speech;

import java.util.List;

/**
 * Original-vs-port speech parity ledger for the 1.0 port.
 */
public final class GOTSpeechParityAudit {
    private GOTSpeechParityAudit() {}

    public enum Status {
        COMPLETE,
        COMPLETE_MODERN_EQUIVALENT,
        DEFERRED_TO_QUEST_SYSTEM,
        NOT_PRESENT_IN_ORIGINAL,
        DEFERRED_MISSING_NPC_STATE
    }

    public record Entry(String feature, Status status, String note) {}

    public static List<Entry> entries() {
        return List.of(
            new Entry("Speech banks", Status.COMPLETE,
                "Original English speech text corpus carried forward as data"),
            new Entry("Random bank-line selection", Status.COMPLETE,
                "Reloadable modern speech bank registry"),
            new Entry("# player-name replacement", Status.COMPLETE,
                "GOTSpeechFormatter"),
            new Entry("@ / $ argument replacement", Status.COMPLETE,
                "GOTSpeechFormatter"),
            new Entry("Default friendly/hostile routing", Status.COMPLETE,
                "GOTSpeechSelector"),
            new Entry("Neutral hireable-unit routing", Status.COMPLETE,
                "Legacy neutral unit/trader banks restored"),
            new Entry("Trader/farmer/smith/bartender bank routing",
                Status.COMPLETE_MODERN_EQUIVALENT,
                "Mapped through current regional role IDs"),
            new Entry("Legendary NPC overrides", Status.COMPLETE_MODERN_EQUIVALENT,
                "Mapped through current legendary role IDs"),
            new Entry("Special NPC speech overrides", Status.COMPLETE_MODERN_EQUIVALENT,
                "Criminal/giant/gladiator/etc. current-role mapping"),
            new Entry("Immersive overhead display", Status.COMPLETE,
                "200 tick lifetime, matching GOTSpeechClient"),
            new Entry("Optional chat logging", Status.COMPLETE,
                "Immersive/chat-log/force-chat semantics restored"),
            new Entry("Interaction talk cooldown", Status.COMPLETE,
                "40 ticks, matching legacy npcTalkTick threshold"),
            new Entry("No speech while actively fighting", Status.COMPLETE,
                "Ordinary right-click chatter blocked while NPC has attack target"),
            new Entry("Hostile acquisition bark", Status.COMPLETE_MODERN_EQUIVALENT,
                "New player target, LoS, 1/3 chance, 16-block crowd suppression"),
            new Entry("NPC-to-NPC random ambient chatter", Status.NOT_PRESENT_IN_ORIGINAL,
                "Not invented for the port"),
            new Entry("Jaqen state-sequence dialogue", Status.DEFERRED_TO_QUEST_SYSTEM,
                "Banks preserved; must use real quest/state progression"),
            new Entry("Miniquest start/progress/complete speech", Status.DEFERRED_TO_QUEST_SYSTEM,
                "Banks preserved; Quest Fidelity will bind states"),
            new Entry("Drunken NPC speech distortion", Status.DEFERRED_MISSING_NPC_STATE,
                "Original algorithm recovered, but current NPC family/drunk state is not yet ported")
        );
    }
}
