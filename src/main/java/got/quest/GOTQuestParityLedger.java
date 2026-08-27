package got.quest;

import java.util.List;

/**
 * Pass-1 original-vs-port ledger.  This deliberately distinguishes engine
 * parity from content parity so later passes can fill missing legacy quest
 * families without destabilising the already-working journal.
 */
public final class GOTQuestParityLedger {
    private GOTQuestParityLedger() {}

    public enum Status { COMPLETE, MODERN_EQUIVALENT, DEFERRED_CONTENT, DEFERRED_SYSTEM }

    public record Entry(String feature, Status status, String note) {}

    public static List<Entry> entries() {
        return List.of(
            new Entry("Persistent per-player quest journal", Status.MODERN_EQUIVALENT,
                    "Active/archive journal persists through death and dimensions"),
            new Entry("Quest UUID / giver UUID ownership", Status.COMPLETE,
                    "Each accepted quest has an instance UUID and original giver UUID"),
            new Entry("Quest offer / accept / progress / completion lifecycle", Status.COMPLETE,
                    "Server authoritative"),
            new Entry("Five active quests per faction", Status.COMPLETE,
                    "Legacy cap retained through giver definition"),
            new Entry("Collect quests", Status.COMPLETE, "COLLECT objective"),
            new Entry("Kill entity quests", Status.COMPLETE, "KILL_ENTITY objective"),
            new Entry("Kill faction quests", Status.COMPLETE, "KILL_FACTION objective"),
            new Entry("NPC interaction objectives", Status.MODERN_EQUIVALENT, "TALK_TO_NPC objective"),
            new Entry("Miniquest event objectives", Status.MODERN_EQUIVALENT, "EVENT objective"),
            new Entry("Quest giver death/failure handling", Status.MODERN_EQUIVALENT,
                    "Lifecycle has failure/archive support; exact content rules audited later"),
            new Entry("Coin/item/alignment rewards", Status.COMPLETE, "Datapack reward block"),
            new Entry("Quest tracking", Status.COMPLETE, "Tracked quest UUID synced to client"),
            new Entry("Quest Book / tracker presentation", Status.MODERN_EQUIVALENT,
                    "Existing 1.20.1 quest UI and legacy assets"),
            new Entry("Legendary quest content", Status.MODERN_EQUIVALENT,
                    "Named legendary quest definitions, rewards, offer bindings, and speech are implemented"),
            new Entry("Jaqen welcome/tutorial quest", Status.MODERN_EQUIVALENT,
                    "Jaqen tutorial sequence, rewards, interface milestones, dialogue, and despawn flow are implemented"),
            new Entry("Player bounty quests", Status.MODERN_EQUIVALENT,
                    "Faction bounty ledger, target selection and bounty-claim hooks restored"),
            new Entry("Pickpocket quests", Status.MODERN_EQUIVALENT,
                    "Sneak-interact pickpocket runtime restored; quest event hook is got:pickpocket")
        );
    }
}
