package got.lore;

import got.faction.GOTFaction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/** Recreates the legacy GOTLore written-book delivery path. */
public final class GOTLoreBookService {
    public static final String LORE_ID = "GOTLoreId";
    private GOTLoreBookService() {}

    public static ItemStack createBook(GOTLoreEntry entry) {
        ItemStack stack = new ItemStack(Items.WRITTEN_BOOK);
        CompoundTag tag = stack.getOrCreateTag();
        tag.putString(LORE_ID, entry.id());
        tag.putString("title", entry.title());
        tag.putString("author", entry.author() == null || entry.author().isBlank() ? "Unknown" : entry.author());
        tag.putBoolean("resolved", true);
        ListTag pages = new ListTag();
        for (String page : paginate(entry.text(), 220)) {
            pages.add(StringTag.valueOf(Component.Serializer.toJson(Component.literal(page))));
        }
        tag.put("pages", pages);
        return stack;
    }

    public static boolean isLoreBook(ItemStack stack) {
        return stack.is(Items.WRITTEN_BOOK) && stack.hasTag() && stack.getTag().contains(LORE_ID);
    }

    public static String loreId(ItemStack stack) { return isLoreBook(stack) ? stack.getTag().getString(LORE_ID) : ""; }

    public static ItemStack randomForFaction(ServerPlayer player, GOTFaction faction, RandomSource random) {
        String language = languageRoot(player);
        List<GOTLoreEntry> preferred = new ArrayList<>();
        List<GOTLoreEntry> fallback = new ArrayList<>();
        String factionId = faction == null ? "" : faction.id().toLowerCase(Locale.ROOT);
        String broad = broadType(factionId);
        for (GOTLoreEntry e : GOTLoreRegistry.all()) {
            boolean lang = e.id().startsWith(language + "/");
            boolean match = e.types().stream().map(s -> s.toLowerCase(Locale.ROOT))
                    .anyMatch(t -> t.equals(factionId) || t.equals(broad));
            if (!match) continue;
            if (lang) preferred.add(e);
            else if (e.id().startsWith("en/")) fallback.add(e);
        }
        List<GOTLoreEntry> pool = preferred.isEmpty() ? fallback : preferred;
        if (pool.isEmpty()) return ItemStack.EMPTY;
        return createBook(pool.get(random.nextInt(pool.size())));
    }

    private static String languageRoot(ServerPlayer player) {
        String l = player.getLanguage().toLowerCase(Locale.ROOT);
        int split = l.indexOf('_');
        return split > 0 ? l.substring(0, split) : l;
    }

    private static String broadType(String faction) {
        return switch (faction) {
            case "north","arryn","riverlands","westerlands","crownlands","dragonstone","reach","stormlands","dorne","ironborn","night_watch","wildling" -> "westeros";
            case "sothoryos" -> "sothoryos";
            case "yi_ti" -> "yi_ti";
            case "asshai" -> "asshai";
            default -> "essos";
        };
    }

    private static List<String> paginate(String text, int targetChars) {
        List<String> pages = new ArrayList<>();
        if (text == null || text.isBlank()) { pages.add(""); return pages; }
        String[] paragraphs = text.split("\\R\\s*\\R");
        StringBuilder page = new StringBuilder();
        for (String paragraph : paragraphs) {
            String p = paragraph.trim();
            while (p.length() > targetChars) {
                int cut = p.lastIndexOf(' ', targetChars);
                if (cut < targetChars / 2) cut = targetChars;
                if (page.length() > 0) { pages.add(page.toString()); page.setLength(0); }
                pages.add(p.substring(0, cut).trim());
                p = p.substring(cut).trim();
            }
            if (page.length() + p.length() + 2 > targetChars && page.length() > 0) { pages.add(page.toString()); page.setLength(0); }
            if (!p.isBlank()) { if (page.length() > 0) page.append("\n\n"); page.append(p); }
        }
        if (page.length() > 0) pages.add(page.toString());
        return pages;
    }
}
