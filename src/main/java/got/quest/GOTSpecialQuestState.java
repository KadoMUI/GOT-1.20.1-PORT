package got.quest;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;

/**
 * Persistent state for legendary/scripted quest sequences that cannot be
 * represented as one flat random miniquest definition.
 */
public final class GOTSpecialQuestState {
    private static final String ROOT = "GOTSpecialQuests";
    private GOTSpecialQuestState() {}

    public static int stage(ServerPlayer player, String id) {
        return player.getPersistentData().getCompound(ROOT).getInt(id + "_Stage");
    }

    public static void setStage(ServerPlayer player, String id, int stage) {
        CompoundTag root = player.getPersistentData().getCompound(ROOT);
        root.putInt(id + "_Stage", Math.max(0, stage));
        player.getPersistentData().put(ROOT, root);
    }

    public static boolean flag(ServerPlayer player, String id, String flag) {
        return player.getPersistentData().getCompound(ROOT).getBoolean(id + "_" + flag);
    }

    public static void setFlag(ServerPlayer player, String id, String flag, boolean value) {
        CompoundTag root = player.getPersistentData().getCompound(ROOT);
        root.putBoolean(id + "_" + flag, value);
        player.getPersistentData().put(ROOT, root);
    }
}
