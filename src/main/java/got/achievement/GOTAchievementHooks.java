package got.achievement;

import got.faction.GOTFaction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public final class GOTAchievementHooks {
    private static final String ROOT = "GOTAchievements";
    private GOTAchievementHooks() {}

    public static boolean has(ServerPlayer player, String id) {
        return player.getPersistentData().getCompound(ROOT).getBoolean(id);
    }

    public static boolean award(ServerPlayer player, String id) {
        if (id == null || id.isBlank() || has(player, id)) return false;
        CompoundTag tag = player.getPersistentData().getCompound(ROOT);
        tag.putBoolean(id, true);
        player.getPersistentData().put(ROOT, tag);
        return true;
    }

    public static Set<String> snapshot(ServerPlayer player) {
        CompoundTag tag = player.getPersistentData().getCompound(ROOT);
        Set<String> result = new HashSet<>();
        for (String key : tag.getAllKeys()) if (tag.getBoolean(key)) result.add(key);
        return Collections.unmodifiableSet(result);
    }

    /** Original GOTAchievementRank code: alignment_<FACTION>_<threshold>. */
    public static void awardRanksForAlignment(ServerPlayer player, GOTFaction faction, float alignment) {
        if (faction == null || !faction.isPlayable() || alignment < 0.0F) return;
        if (alignment >= 10.0F) award(player, rankId(faction, 10.0F));
        if (alignment >= 50.0F) award(player, rankId(faction, 50.0F));
        if (alignment >= 100.0F) award(player, rankId(faction, 100.0F));
        if (alignment >= 500.0F) award(player, rankId(faction, 500.0F));
        if (alignment >= 1000.0F) award(player, rankId(faction, 1000.0F));
    }

    public static String rankId(GOTFaction faction, float threshold) {
        return "alignment_" + faction.legacyName() + "_" + threshold;
    }
}
