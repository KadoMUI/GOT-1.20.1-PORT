package got.player;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;

public final class GOTPlayerOptions {
    private static final String ROOT = "GOTOptions";

    public enum Option {
        FRIENDLY_FIRE(true),
        HIRED_DEATH_MESSAGES(true),
        SHOW_ALIGNMENT(true),
        SHOW_MAP_LOCATION(true),
        CONQUEST_KILLS(true),
        FEMININE_RANKS(false),
        IMMERSIVE_SPEECH(true),
        IMMERSIVE_SPEECH_CHAT(false);

        private final boolean defaultValue;
        Option(boolean defaultValue) { this.defaultValue = defaultValue; }
        public boolean defaultValue() { return defaultValue; }
    }

    private GOTPlayerOptions() {}

    public static boolean get(Player player, Option option) {
        CompoundTag tag = player.getPersistentData().getCompound(ROOT);
        String key = option.name();
        return tag.contains(key) ? tag.getBoolean(key) : option.defaultValue();
    }

    public static void set(Player player, Option option, boolean value) {
        CompoundTag tag = player.getPersistentData().getCompound(ROOT);
        tag.putBoolean(option.name(), value);
        player.getPersistentData().put(ROOT, tag);
    }

    public static boolean toggle(Player player, Option option) {
        boolean value = !get(player, option);
        set(player, option, value);
        return value;
    }

    public static Snapshot snapshot(Player player) {
        return new Snapshot(
                get(player, Option.FRIENDLY_FIRE),
                get(player, Option.HIRED_DEATH_MESSAGES),
                get(player, Option.SHOW_ALIGNMENT),
                get(player, Option.SHOW_MAP_LOCATION),
                get(player, Option.CONQUEST_KILLS),
                get(player, Option.FEMININE_RANKS),
                get(player, Option.IMMERSIVE_SPEECH),
                get(player, Option.IMMERSIVE_SPEECH_CHAT)
        );
    }

    public record Snapshot(boolean friendlyFire, boolean hiredDeathMessages,
                           boolean showAlignment, boolean showMapLocation,
                           boolean conquestKills, boolean feminineRanks,
                           boolean immersiveSpeech, boolean immersiveSpeechChat) {}
}
