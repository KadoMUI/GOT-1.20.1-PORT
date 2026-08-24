package got.client.lore;

import java.util.List;

public record GOTLoreView(String id, String category, String title,
                          String author, List<String> types, String text,
                          boolean rewardable, boolean discovered) {}
