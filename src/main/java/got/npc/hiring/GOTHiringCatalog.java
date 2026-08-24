package got.npc.hiring;

import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.GsonHelper;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Data-driven exact legacy hiring catalog.
 */
public final class GOTHiringCatalog {
    private static final Map<ResourceLocation, GOTHireDefinition> DEFINITIONS = new ConcurrentHashMap<>();

    private GOTHiringCatalog() {}

    public static Collection<GOTHireDefinition> all() {
        return Collections.unmodifiableCollection(DEFINITIONS.values());
    }

    public static void clear() {
        DEFINITIONS.clear();
    }

    public static void register(GOTHireDefinition definition) {
        DEFINITIONS.put(definition.id(), definition);
    }

    public static void reload(ResourceManager manager) {
        clear();

        manager.listResources("hiring", path -> path.getPath().endsWith(".json"))
            .forEach((resourceId, resource) -> {
                try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(resource.open(), StandardCharsets.UTF_8)
                )) {
                    JsonObject json = GsonHelper.parse(reader);

                    String rawPath = resourceId.getPath();
                    ResourceLocation id = new ResourceLocation(
                        resourceId.getNamespace(),
                        rawPath.substring("hiring/".length(), rawPath.length() - ".json".length())
                    );

                    Set<String> roles = new LinkedHashSet<>();
                    if (json.has("roles")) {
                        json.getAsJsonArray("roles").forEach(e -> roles.add(e.getAsString()));
                    }

                    register(new GOTHireDefinition(
                        id,
                        roles,
                        GOTHiredTask.valueOf(GsonHelper.getAsString(json, "task").toUpperCase(Locale.ROOT)),
                        GsonHelper.getAsFloat(json, "required_alignment", 0.0F),
                        GsonHelper.getAsBoolean(json, "pledge_exclusive", false),
                        GsonHelper.getAsInt(json, "initial_cost"),
                        GsonHelper.getAsString(json, "legacy_entity", ""),
                        GsonHelper.getAsString(json, "legacy_group", "")
                    ));
                } catch (Exception e) {
                    throw new RuntimeException("Failed loading GOT hiring definition " + resourceId, e);
                }
            });
    }
}
