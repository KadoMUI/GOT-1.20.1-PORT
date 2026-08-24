package got.quest;

/** Persistent lifecycle states used by active and archived quests. */
public enum GOTQuestState {
    ACTIVE,
    READY,
    COMPLETED,
    FAILED,
    ABANDONED;

    public boolean isActive() {
        return this == ACTIVE || this == READY;
    }
}
