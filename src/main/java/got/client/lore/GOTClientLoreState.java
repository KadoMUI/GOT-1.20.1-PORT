package got.client.lore;

import java.util.List;

public final class GOTClientLoreState {
    private static List<GOTLoreView> entries = List.of();
    private GOTClientLoreState() {}
    public static void set(List<GOTLoreView> value) { entries = List.copyOf(value); }
    public static List<GOTLoreView> entries() { return entries; }
}
