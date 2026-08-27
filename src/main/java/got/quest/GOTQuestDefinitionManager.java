package got.quest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import got.GOTMod;
import got.faction.GOTFaction;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/** Loads namespaced quest definitions from data/&lt;namespace&gt;/quests/*.json. */
@Mod.EventBusSubscriber(modid = GOTMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class GOTQuestDefinitionManager extends SimpleJsonResourceReloadListener {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static volatile Map<ResourceLocation, GOTQuestDefinition> definitions = Map.of();

    public GOTQuestDefinitionManager() {
        super(GSON, "quests");
    }

    @SubscribeEvent
    public static void addReloadListener(AddReloadListenerEvent event) {
        event.addListener(new GOTQuestDefinitionManager());
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> resources, ResourceManager manager,
                         ProfilerFiller profiler) {
        Map<ResourceLocation, GOTQuestDefinition> loaded = new LinkedHashMap<>();
        resources.entrySet().stream().sorted(Map.Entry.comparingByKey()).forEach(entry -> {
            try {
                GOTQuestDefinition definition = parse(entry.getKey(), GsonHelper.convertToJsonObject(
                        entry.getValue(), "quest"));
                loaded.put(entry.getKey(), definition);
            } catch (RuntimeException exception) {
                GOTMod.LOGGER.error("Unable to load quest definition {}", entry.getKey(), exception);
            }
        });
        definitions = Map.copyOf(loaded);
        GOTMod.LOGGER.info("Loaded {} quest definitions", definitions.size());
    }

    public static Optional<GOTQuestDefinition> get(ResourceLocation id) {
        return Optional.ofNullable(definitions.get(id));
    }

    public static List<GOTQuestDefinition> all() {
        return List.copyOf(definitions.values());
    }

    private static GOTQuestDefinition parse(ResourceLocation id, JsonObject json) {
        String prefix = "quest." + id.getNamespace() + "." + id.getPath().replace('/', '.');
        String title = GsonHelper.getAsString(json, "title", prefix + ".title");
        String description = GsonHelper.getAsString(json, "description", prefix + ".description");
        String offer = GsonHelper.getAsString(json, "offer", prefix + ".offer");
        String progress = GsonHelper.getAsString(json, "progress", prefix + ".progress");
        String complete = GsonHelper.getAsString(json, "complete", prefix + ".complete");
        ResourceLocation icon = resource(GsonHelper.getAsString(json, "icon", "minecraft:book"), "icon");
        int color = parseColor(json.get("color"), 0x7A5C3E);
        int weight = Math.max(1, GsonHelper.getAsInt(json, "weight", 1));
        boolean repeatable = GsonHelper.getAsBoolean(json, "repeatable", false);
        int cooldown = Math.max(0, GsonHelper.getAsInt(json, "cooldown_ticks", 0));
        boolean sequential = GsonHelper.getAsBoolean(json, "sequential", false);
        boolean autoComplete = GsonHelper.getAsBoolean(json, "auto_complete", false);
        boolean legendary = GsonHelper.getAsBoolean(json, "legendary", false);
        float legacyRewardFactor = GsonHelper.getAsFloat(json, "legacy_reward_factor", -1.0F);

        List<ResourceLocation> prerequisites = new ArrayList<>();
        for (JsonElement element : array(json, "prerequisites")) {
            prerequisites.add(resource(element.getAsString(), "prerequisite"));
        }

        GOTQuestDefinition.Giver giver = parseGiver(json.has("giver")
                ? GsonHelper.getAsJsonObject(json, "giver") : new JsonObject());
        JsonArray objectiveArray = GsonHelper.getAsJsonArray(json, "objectives");
        if (objectiveArray.size() == 0) throw new IllegalArgumentException("Quest has no objectives");
        List<GOTQuestDefinition.Objective> objectives = new ArrayList<>();
        for (int index = 0; index < objectiveArray.size(); index++) {
            objectives.add(parseObjective(prefix, index,
                    GsonHelper.convertToJsonObject(objectiveArray.get(index), "objective")));
        }

        GOTQuestDefinition.Reward reward = parseReward(json.has("rewards")
                ? GsonHelper.getAsJsonObject(json, "rewards") : new JsonObject());
        return new GOTQuestDefinition(id, title, description, offer, progress, complete,
                icon, color, weight, repeatable, cooldown, sequential, autoComplete, legendary, legacyRewardFactor,
                prerequisites, giver, objectives, reward);
    }

    private static GOTQuestDefinition.Giver parseGiver(JsonObject json) {
        Set<String> roles = new HashSet<>();
        for (JsonElement element : array(json, "roles")) roles.add(element.getAsString());

        Set<GOTFaction> factions = new HashSet<>();
        for (JsonElement element : array(json, "factions")) {
            GOTFaction faction = GOTFaction.byId(element.getAsString())
                    .orElseThrow(() -> new IllegalArgumentException("Unknown giver faction " + element));
            factions.add(faction);
        }

        Set<ResourceLocation> entityTypes = new HashSet<>();
        for (JsonElement element : array(json, "entity_types")) {
            entityTypes.add(resource(element.getAsString(), "giver entity type"));
        }
        GOTFaction membership = GOTFaction.byId(GsonHelper.getAsString(json,
                "required_membership", "unaligned")).orElse(GOTFaction.UNALIGNED);
        return new GOTQuestDefinition.Giver(roles, factions, entityTypes,
                GsonHelper.getAsFloat(json, "minimum_alignment", Float.NEGATIVE_INFINITY),
                membership, Math.max(1, GsonHelper.getAsInt(json, "maximum_active_for_faction", 5)));
    }

    private static GOTQuestDefinition.Objective parseObjective(String prefix, int index,
                                                               JsonObject json) {
        GOTQuestObjectiveType type = GOTQuestObjectiveType.byName(GsonHelper.getAsString(json, "type"));
        String target = GsonHelper.getAsString(json, "target", "");
        String role = GsonHelper.getAsString(json, "role", "");
        int count = Math.max(1, GsonHelper.getAsInt(json, "count", 1));
        int minimumCount = Math.max(1, GsonHelper.getAsInt(json, "count_min", count));
        int maximumCount = Math.max(minimumCount, GsonHelper.getAsInt(json, "count_max", count));
        ResourceLocation dimension = resource(GsonHelper.getAsString(json,
                "dimension", "minecraft:overworld"), "objective dimension");
        BlockPos position = BlockPos.ZERO;
        if (json.has("position")) {
            JsonArray coords = GsonHelper.getAsJsonArray(json, "position");
            if (coords.size() != 3) throw new IllegalArgumentException("Objective position needs three coordinates");
            position = new BlockPos(coords.get(0).getAsInt(), coords.get(1).getAsInt(), coords.get(2).getAsInt());
        }
        double radius = Math.max(0.0D, GsonHelper.getAsDouble(json, "radius", 4.0D));
        boolean consume = GsonHelper.getAsBoolean(json, "consume", type == GOTQuestObjectiveType.COLLECT);
        String label = GsonHelper.getAsString(json, "label", prefix + ".objective." + index);
        validateObjective(type, target, position, radius);
        return new GOTQuestDefinition.Objective(type, target, role, count, minimumCount, maximumCount, dimension,
                position, radius, consume, label);
    }

    private static void validateObjective(GOTQuestObjectiveType type, String target,
                                          BlockPos position, double radius) {
        if (type != GOTQuestObjectiveType.VISIT_LOCATION && target.isBlank()
                && type != GOTQuestObjectiveType.TALK_TO_NPC) {
            throw new IllegalArgumentException(type + " objective is missing its target");
        }
        if (type == GOTQuestObjectiveType.VISIT_LOCATION && radius <= 0.0D) {
            throw new IllegalArgumentException("Visit objective radius must be positive at " + position);
        }
        if ((type == GOTQuestObjectiveType.COLLECT || type == GOTQuestObjectiveType.KILL_ENTITY
                || type == GOTQuestObjectiveType.EVENT) && !target.isBlank()) {
            resource(target, "objective target");
        }
    }

    private static GOTQuestDefinition.Reward parseReward(JsonObject json) {
        Map<GOTFaction, Float> alignment = new EnumMap<>(GOTFaction.class);
        if (json.has("alignment")) {
            JsonObject values = GsonHelper.getAsJsonObject(json, "alignment");
            for (Map.Entry<String, JsonElement> entry : values.entrySet()) {
                GOTFaction faction = GOTFaction.byId(entry.getKey())
                        .orElseThrow(() -> new IllegalArgumentException("Unknown reward faction " + entry.getKey()));
                alignment.put(faction, entry.getValue().getAsFloat());
            }
        }
        List<GOTQuestDefinition.ItemReward> items = new ArrayList<>();
        for (JsonElement element : array(json, "items")) {
            JsonObject item = GsonHelper.convertToJsonObject(element, "reward item");
            items.add(new GOTQuestDefinition.ItemReward(resource(
                    GsonHelper.getAsString(item, "item"), "reward item"),
                    Math.max(1, GsonHelper.getAsInt(item, "count", 1))));
        }
        return new GOTQuestDefinition.Reward(alignment, items,
                Math.max(0, GsonHelper.getAsInt(json, "coins", 0)),
                Math.max(0, GsonHelper.getAsInt(json, "experience", 0)),
                GsonHelper.getAsBoolean(json, "hire_giver", false),
                Math.max(0, GsonHelper.getAsInt(json, "hire_alignment", 100)));
    }

    private static JsonArray array(JsonObject object, String name) {
        return object.has(name) ? GsonHelper.getAsJsonArray(object, name) : new JsonArray();
    }

    private static ResourceLocation resource(String value, String field) {
        ResourceLocation id = ResourceLocation.tryParse(value);
        if (id == null) throw new IllegalArgumentException("Invalid " + field + ": " + value);
        return id;
    }

    private static int parseColor(JsonElement element, int fallback) {
        if (element == null) return fallback;
        if (element.isJsonPrimitive() && element.getAsJsonPrimitive().isNumber()) {
            return element.getAsInt() & 0xFFFFFF;
        }
        String value = element.getAsString().trim();
        if (value.startsWith("#")) value = value.substring(1);
        return Integer.parseInt(value, 16) & 0xFFFFFF;
    }
}
