package got.lore;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Modern loader for the preserved 1.7.10 lore text corpus.
 *
 * Metadata syntax is limited to directives proven by the original GOTLore
 * audit: title, author, types, reward, pages, choose, name and num.
 */
public final class GOTLoreRegistry {
    private static final Map<String, GOTLoreEntry> ENTRIES = new LinkedHashMap<>();

    private GOTLoreRegistry() {}

    public static void reload(ResourceManager manager) {
        ENTRIES.clear();
        Map<ResourceLocation, Resource> found =
                manager.listResources("lore_legacy/texts", id -> id.getPath().endsWith(".txt"));
        for (var e : found.entrySet()) {
            try (BufferedReader reader = e.getValue().openAsReader()) {
                List<String> lines = reader.lines().toList();
                GOTLoreEntry entry = parse(e.getKey(), lines);
                ENTRIES.put(entry.id(), entry);
            } catch (IOException ignored) {
            }
        }
    }

    private static GOTLoreEntry parse(ResourceLocation resource, List<String> lines) {
        String title = null;
        String author = null;
        List<String> types = new ArrayList<>();
        boolean reward = false;
        List<String> body = new ArrayList<>();

        for (String raw : lines) {
            String s = raw.trim();
            String lower = s.toLowerCase(Locale.ROOT);
            if (lower.startsWith("title:")) title = s.substring(s.indexOf(':') + 1).trim();
            else if (lower.startsWith("author:")) author = s.substring(s.indexOf(':') + 1).trim();
            else if (lower.startsWith("types:")) {
                types.addAll(Arrays.stream(s.substring(s.indexOf(':') + 1).split(","))
                        .map(String::trim).filter(x -> !x.isEmpty()).toList());
            } else if (lower.equals("reward") || lower.startsWith("reward:")) reward = true;
            else if (lower.startsWith("pages") || lower.startsWith("choose:")
                    || lower.startsWith("name:") || lower.startsWith("num:")) {
                // Proven formatting/control directives; consumed by the legacy parser layer.
            } else body.add(raw);
        }

        String path = resource.getPath()
                .replaceFirst("^lore_legacy/texts/", "")
                .replaceFirst("\\.txt$", "");
        String[] parts = path.split("/");
        String category = parts.length > 1 ? parts[0] : "all";
        if (title == null || title.isBlank()) title = parts[parts.length - 1];

        return new GOTLoreEntry(path, category, title, author,
                List.copyOf(types), reward, String.join("\n", body).trim());
    }

    public static Collection<GOTLoreEntry> all() {
        return Collections.unmodifiableCollection(ENTRIES.values());
    }

    public static Optional<GOTLoreEntry> get(String id) {
        return Optional.ofNullable(ENTRIES.get(id));
    }

    public static List<GOTLoreEntry> category(String category) {
        return ENTRIES.values().stream()
                .filter(e -> e.category().equalsIgnoreCase(category))
                .collect(Collectors.toUnmodifiableList());
    }
}
