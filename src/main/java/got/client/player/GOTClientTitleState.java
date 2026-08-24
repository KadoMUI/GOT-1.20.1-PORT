package got.client.player;

import net.minecraft.ChatFormatting;
import java.util.List;

public final class GOTClientTitleState {
    private static String selectedId = "";
    private static ChatFormatting selectedColor = ChatFormatting.WHITE;
    private static List<String> unlocked = List.of();

    private GOTClientTitleState() {}

    public static void set(String id, ChatFormatting color, List<String> ids) {
        selectedId = id == null ? "" : id;
        selectedColor = color == null ? ChatFormatting.WHITE : color;
        unlocked = List.copyOf(ids);
    }

    public static String selectedId() { return selectedId; }
    public static ChatFormatting selectedColor() { return selectedColor; }
    public static List<String> unlocked() { return unlocked; }
}
