package got.lore;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Modern persistence container for lore IDs once Pass 2/3 content bindings
 * identify which original resources are genuinely player-discoverable.
 *
 * It intentionally makes no assumptions about HOW an entry is discovered.
 */
public final class GOTLorePlayerData {
    private static final String ROOT = "GOTLore";
    private static final String DISCOVERED = "Discovered";

    private GOTLorePlayerData() {}

    public static boolean has(ServerPlayer player, String id) {
        CompoundTag root = player.getPersistentData().getCompound(ROOT);
        return root.getBoolean(DISCOVERED + "_" + id);
    }

    public static boolean discover(ServerPlayer player, String id) {
        if (id == null || id.isBlank() || has(player, id)) return false;
        CompoundTag root = player.getPersistentData().getCompound(ROOT);
        root.putBoolean(DISCOVERED + "_" + id, true);
        player.getPersistentData().put(ROOT, root);
        return true;
    }


    public static boolean rewardPending(ServerPlayer player, String id) {
        return player.getPersistentData().getCompound(ROOT).getBoolean("RewardPending_" + id);
    }

    public static void setRewardPending(ServerPlayer player, String id, boolean value) {
        CompoundTag root = player.getPersistentData().getCompound(ROOT);
        root.putBoolean("RewardPending_" + id, value);
        player.getPersistentData().put(ROOT, root);
    }

    public static void forget(ServerPlayer player, String id) {
        CompoundTag root = player.getPersistentData().getCompound(ROOT);
        root.remove(DISCOVERED + "_" + id);
        player.getPersistentData().put(ROOT, root);
    }
}
