package got.speech;

import net.minecraft.world.entity.player.Player;

/** Exact legacy placeholder semantics: # player/name, @ arg1, $ arg2. */
public final class GOTSpeechFormatter {
    private GOTSpeechFormatter() {}

    public static String format(String speech, Player player, CharSequence arg1, CharSequence arg2) {
        String result = speech == null ? "" : speech;
        if (player != null) result = result.replace("#", player.getName().getString());
        if (arg1 != null) result = result.replace("@", arg1);
        if (arg2 != null) result = result.replace("$", arg2);
        return result;
    }
}
