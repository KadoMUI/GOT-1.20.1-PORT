package got.player;

import got.faction.GOTFaction;
import got.faction.GOTFactionPlayerData;
import got.faction.GOTFactionRank;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;

import java.util.ArrayList;
import java.util.List;

public final class GOTPlayerTitleData {
    private static final String ROOT = "GOTTitle";
    private static final String ID = "Id";
    private static final String COLOR = "Color";

    private GOTPlayerTitleData() {}

    public static String selectedId(ServerPlayer player) {
        return player.getPersistentData().getCompound(ROOT).getString(ID);
    }

    public static ChatFormatting selectedColor(ServerPlayer player) {
        String value = player.getPersistentData().getCompound(ROOT).getString(COLOR);
        ChatFormatting color = ChatFormatting.getByName(value);
        return color != null && color.isColor() ? color : ChatFormatting.WHITE;
    }

    public static void clear(ServerPlayer player) {
        player.getPersistentData().remove(ROOT);
    }

    public static boolean select(ServerPlayer player, String id, ChatFormatting color) {
        if (id == null || id.isBlank()) {
            clear(player);
            return true;
        }
        if (!unlockedIds(player).contains(id)) return false;
        if (color == null || !color.isColor()) color = ChatFormatting.WHITE;

        CompoundTag tag = player.getPersistentData().getCompound(ROOT);
        tag.putString(ID, id);
        tag.putString(COLOR, color.getName());
        player.getPersistentData().put(ROOT, tag);
        return true;
    }

    public static List<String> unlockedIds(ServerPlayer player) {
        List<String> ids = new ArrayList<>();
        GOTFactionPlayerData data = GOTFactionPlayerData.get(player);
        for (GOTFaction faction : GOTFaction.playableFactions()) {
            float alignment = data.alignment(faction);
            for (GOTFactionRank rank : faction.ranksDescending()) {
                if (alignment >= rank.alignment()) {
                    ids.add(id(faction, rank));
                }
            }
        }
        return List.copyOf(ids);
    }

    public static String id(GOTFaction faction, GOTFactionRank rank) {
        return "rank:" + faction.id() + ":" + rank.key();
    }
}
