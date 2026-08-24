package got.quest;

import java.util.Locale;

/** Built-in objective families understood by the reusable quest engine. */
public enum GOTQuestObjectiveType {
    COLLECT,
    KILL_ENTITY,
    KILL_FACTION,
    TALK_TO_NPC,
    VISIT_LOCATION,
    EVENT;

    public static GOTQuestObjectiveType byName(String name) {
        if (name == null) throw new IllegalArgumentException("Quest objective type is missing");
        return valueOf(name.trim().toUpperCase(Locale.ROOT));
    }
}
