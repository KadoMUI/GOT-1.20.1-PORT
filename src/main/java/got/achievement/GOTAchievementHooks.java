package got.achievement;

import got.faction.GOTFaction;
import got.network.GOTNetwork;
import got.network.S2CAchievementDataPacket;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.PacketDistributor;

import java.util.Collections;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

public final class GOTAchievementHooks {
    private static final String ROOT = "GOTAchievements";
    private GOTAchievementHooks() {}

    public static String canonical(String id) {
        if (id == null) return "";
        String raw = id.trim();
        if (raw.toLowerCase(Locale.ROOT).startsWith("alignment_")) return raw.toLowerCase(Locale.ROOT);
        String upper = raw.toUpperCase(Locale.ROOT);
        for (GOTAchievementCatalog.Entry e : GOTAchievementCatalog.all()) {
            if (e.code().equalsIgnoreCase(upper) || e.legacyField().equalsIgnoreCase(id)) return e.code();
        }
        return upper;
    }

    public static boolean has(ServerPlayer player, String id) {
        return player.getPersistentData().getCompound(ROOT).getBoolean(canonical(id));
    }

    public static boolean award(ServerPlayer player, String id) {
        String code = canonical(id);
        if (code.isBlank() || has(player, code)) return false;
        boolean known = GOTAchievementCatalog.all().stream().anyMatch(e -> e.code().equals(code));
        if (!known && !code.startsWith("alignment_")) return false;
        CompoundTag tag = player.getPersistentData().getCompound(ROOT);
        tag.putBoolean(code, true);
        player.getPersistentData().put(ROOT, tag);
        sync(player);
        if (known) player.displayClientMessage(Component.translatable("got.achievement.unlocked",
                Component.translatable("got.achievement." + code + ".title")).withStyle(ChatFormatting.GOLD), true);
        return true;
    }

    public static Set<String> snapshot(ServerPlayer player) {
        CompoundTag tag = player.getPersistentData().getCompound(ROOT);
        Set<String> result = new HashSet<>();
        for (String key : tag.getAllKeys()) {
            String code=canonical(key);
            if (tag.getBoolean(key) && GOTAchievementCatalog.all().stream().anyMatch(e -> e.code().equals(code))) result.add(code);
        }
        return Collections.unmodifiableSet(result);
    }

    public static void copyPersisted(net.minecraft.world.entity.player.Player oldPlayer,
                                     net.minecraft.world.entity.player.Player newPlayer) {
        if (oldPlayer.getPersistentData().contains(ROOT))
            newPlayer.getPersistentData().put(ROOT, oldPlayer.getPersistentData().getCompound(ROOT).copy());
        if (oldPlayer.getPersistentData().contains("GOTAchievementStats"))
            newPlayer.getPersistentData().put("GOTAchievementStats", oldPlayer.getPersistentData().getCompound("GOTAchievementStats").copy());
    }

    public static void sync(ServerPlayer player) {
        GOTNetwork.CHANNEL.send(PacketDistributor.PLAYER.with(() -> player),
                new S2CAchievementDataPacket(snapshot(player)));
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
