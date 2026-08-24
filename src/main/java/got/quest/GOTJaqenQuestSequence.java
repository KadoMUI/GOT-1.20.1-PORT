package got.quest;

import got.speech.GOTSpeechService;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;

/**
 * Stateful bridge reserved for the original GOTMiniQuestWelcome / Jaqen
 * sequence.  The original speech banks are already present from Speech Pass 1.
 *
 * Stage advancement is explicit and server-side so dialogue, quest acceptance,
 * and later scripted events cannot desynchronise.
 */
public final class GOTJaqenQuestSequence {
    public static final String ID = "jaqen_welcome";

    private GOTJaqenQuestSequence() {}

    public enum Stage {
        NOT_STARTED,
        ARRIVAL,
        WELCOME,
        QUEST,
        COMPLETE,
        DEPARTED;

        public static Stage from(int value) {
            Stage[] v=values();
            return v[Math.max(0, Math.min(value, v.length-1))];
        }
    }

    public static Stage stage(ServerPlayer player) {
        return Stage.from(GOTSpecialQuestState.stage(player, ID));
    }

    public static void setStage(ServerPlayer player, Stage stage) {
        GOTSpecialQuestState.setStage(player, ID, stage.ordinal());
    }

    /**
     * Speech-bank routing is kept separate from objective content.  This closes
     * the Speech-pass deferral without inventing missing quest targets/rewards.
     */
    public static void speakForStage(Entity jaqen, ServerPlayer player) {
        String bank = switch (stage(player)) {
            case NOT_STARTED, ARRIVAL -> "jaqen_arrive";
            case WELCOME -> "jaqen_welcome";
            case QUEST -> "jaqen_quest";
            case COMPLETE -> "jaqen_complete";
            case DEPARTED -> "jaqen_depart";
        };
        GOTSpeechService.speak(jaqen, player, bank);
    }

    public static void advance(ServerPlayer player) {
        Stage now=stage(player);
        if (now != Stage.DEPARTED) setStage(player, Stage.from(now.ordinal()+1));
    }
}
