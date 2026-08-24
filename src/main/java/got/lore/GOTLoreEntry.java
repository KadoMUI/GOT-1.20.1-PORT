package got.lore;

import java.util.List;

public record GOTLoreEntry(
        String id,
        String category,
        String title,
        String author,
        List<String> types,
        boolean rewardable,
        String text
) {}
