package got.construction;

import got.GOTBlocks;
import got.GOTItems;
import got.GOTMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.WallBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * Expands the construction family catalogue into normal 1.20.1 block and item
 * registrations.
 *
 * <p>Adding a material family should only require one catalogue row.  The
 * companion migration generator creates its blockstates, models, loot tables,
 * recipes, tags and translations from the same row.</p>
 */
public final class GOTConstructionFamilies {
    public static final String CATALOGUE_RESOURCE = "/data/got/construction/families.csv";

    private static final List<ConstructionFamily> FAMILIES = new ArrayList<>();
    private static final Map<String, ConstructionFamily.Variant> VARIANTS = new LinkedHashMap<>();
    private static boolean bootstrapped;

    public static synchronized void bootstrap() {
        if (bootstrapped) {
            return;
        }

        List<CatalogueRow> rows = readCatalogue();
        for (CatalogueRow row : rows) {
            register(row);
        }
        bootstrapped = true;
        GOTMod.LOGGER.info("Loaded {} construction families containing {} generated variants",
                FAMILIES.size(), VARIANTS.size());
    }

    public static List<ConstructionFamily> families() {
        return Collections.unmodifiableList(FAMILIES);
    }

    public static Map<String, ConstructionFamily.Variant> variants() {
        return Collections.unmodifiableMap(VARIANTS);
    }

    private static void register(CatalogueRow row) {
        if (FAMILIES.stream().anyMatch(family -> family.familyId().equals(row.familyId()))) {
            throw new IllegalStateException("Duplicate construction family: " + row.familyId());
        }

        Supplier<? extends Block> base = resolveBase(row.baseBlock());
        EnumMap<ConstructionPart, ConstructionFamily.Variant> registered =
                new EnumMap<>(ConstructionPart.class);

        for (ConstructionPart part : row.parts()) {
            String id = row.variantId(part);
            validateNewId(id, row.familyId());
            RegistryObject<Block> block = registerVariant(id, part, base);
            Map<String, ResourceLocation> textures = row.textures(part);
            ConstructionFamily.Variant variant = new ConstructionFamily.Variant(
                    part,
                    id,
                    row.variantName(part),
                    block,
                    textures);
            registered.put(part, variant);
            VARIANTS.put(id, variant);
        }

        FAMILIES.add(new ConstructionFamily(
                row.familyId(),
                row.baseBlock(),
                row.displayName(),
                row.material(),
                row.baseTexture(),
                row.baseModel(),
                registered));
    }

    private static RegistryObject<Block> registerVariant(
            String id,
            ConstructionPart part,
            Supplier<? extends Block> base) {
        Supplier<Block> supplier = switch (part) {
            case STAIRS -> () -> new StairBlock(
                    () -> base.get().defaultBlockState(),
                    BlockBehaviour.Properties.copy(base.get()));
            case SLAB -> () -> new SlabBlock(BlockBehaviour.Properties.copy(base.get()));
            case WALL -> () -> new WallBlock(BlockBehaviour.Properties.copy(base.get()));
            case FENCE -> () -> new FenceBlock(BlockBehaviour.Properties.copy(base.get()));
            case FENCE_GATE -> () -> new FenceGateBlock(
                    BlockBehaviour.Properties.copy(base.get()), WoodType.OAK);
            case DOOR -> () -> new DoorBlock(
                    BlockBehaviour.Properties.copy(base.get()).noOcclusion(), BlockSetType.OAK);
            case TRAPDOOR -> () -> new TrapDoorBlock(
                    BlockBehaviour.Properties.copy(base.get()).noOcclusion(), BlockSetType.OAK);
            case BEAM -> () -> new RotatedPillarBlock(BlockBehaviour.Properties.copy(base.get()));
        };

        RegistryObject<Block> block = GOTBlocks.BLOCKS.register(id, supplier);
        GOTItems.ITEMS.register(id, () -> new BlockItem(block.get(), new Item.Properties()));
        GOTBlocks.BUILDING_BLOCKS.add(block);
        return block;
    }

    private static Supplier<? extends Block> resolveBase(ResourceLocation baseId) {
        if (GOTMod.MOD_ID.equals(baseId.getNamespace())) {
            return GOTBlocks.BLOCKS.getEntries().stream()
                    .filter(entry -> entry.getId().equals(baseId))
                    .findFirst()
                    .<Supplier<? extends Block>>map(entry -> entry::get)
                    .orElseThrow(() -> new IllegalStateException(
                            "Construction family references missing GOT base block " + baseId));
        }

        Block block = ForgeRegistries.BLOCKS.getValue(baseId);
        if (block == null || block == Blocks.AIR) {
            throw new IllegalStateException("Construction family references missing base block " + baseId);
        }
        return () -> block;
    }

    private static void validateNewId(String id, String familyId) {
        ResourceLocation location = new ResourceLocation(GOTMod.MOD_ID, id);
        boolean registeredBlock = GOTBlocks.BLOCKS.getEntries().stream()
                .anyMatch(entry -> entry.getId().equals(location));
        boolean registeredItem = GOTItems.ITEMS.getEntries().stream()
                .anyMatch(entry -> entry.getId().equals(location));
        if (registeredBlock || registeredItem || VARIANTS.containsKey(id)) {
            throw new IllegalStateException(
                    "Construction family " + familyId + " tries to reuse registry id " + location);
        }
    }

    private static List<CatalogueRow> readCatalogue() {
        InputStream stream = GOTConstructionFamilies.class.getResourceAsStream(CATALOGUE_RESOURCE);
        if (stream == null) {
            throw new IllegalStateException("Missing construction catalogue " + CATALOGUE_RESOURCE);
        }

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(stream, StandardCharsets.UTF_8))) {
            List<String> meaningful = reader.lines()
                    .map(String::trim)
                    .filter(line -> !line.isEmpty() && !line.startsWith("#"))
                    .collect(Collectors.toList());
            if (meaningful.isEmpty()) {
                throw new IllegalStateException("Construction catalogue has no header");
            }

            List<String> header = parseCsvLine(meaningful.get(0));
            List<CatalogueRow> rows = new ArrayList<>();
            for (int index = 1; index < meaningful.size(); index++) {
                List<String> values = parseCsvLine(meaningful.get(index));
                if (values.size() != header.size()) {
                    throw new IllegalStateException("Construction catalogue line " + (index + 1)
                            + " has " + values.size() + " fields; expected " + header.size());
                }
                Map<String, String> fields = new LinkedHashMap<>();
                for (int column = 0; column < header.size(); column++) {
                    fields.put(header.get(column), values.get(column));
                }
                rows.add(CatalogueRow.from(fields, index + 1));
            }
            return rows;
        } catch (IOException exception) {
            throw new IllegalStateException("Unable to read construction catalogue", exception);
        }
    }

    private static List<String> parseCsvLine(String line) {
        List<String> values = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean quoted = false;
        for (int index = 0; index < line.length(); index++) {
            char character = line.charAt(index);
            if (character == '"') {
                if (quoted && index + 1 < line.length() && line.charAt(index + 1) == '"') {
                    current.append('"');
                    index++;
                } else {
                    quoted = !quoted;
                }
            } else if (character == ',' && !quoted) {
                values.add(current.toString().trim());
                current.setLength(0);
            } else {
                current.append(character);
            }
        }
        if (quoted) {
            throw new IllegalStateException("Unclosed quote in construction catalogue row: " + line);
        }
        values.add(current.toString().trim());
        return values;
    }

    private record CatalogueRow(
            String familyId,
            ResourceLocation baseBlock,
            String displayName,
            ConstructionMaterial material,
            List<ConstructionPart> parts,
            ResourceLocation baseTexture,
            ResourceLocation baseModel,
            Map<ConstructionPart, String> variantIds,
            Map<ConstructionPart, String> variantNames,
            Map<String, ResourceLocation> specialTextures) {

        static CatalogueRow from(Map<String, String> fields, int line) {
            requireColumns(fields);
            String familyId = required(fields, "family_id", line);
            ResourceLocation baseBlock = location(required(fields, "base_block", line), line);
            String displayName = required(fields, "display_name", line);
            ConstructionMaterial material = ConstructionMaterial.parse(required(fields, "material", line));
            List<ConstructionPart> parts = Arrays.stream(required(fields, "parts", line).split(";"))
                    .map(ConstructionPart::parse)
                    .toList();
            if (parts.isEmpty()) {
                throw new IllegalStateException("Construction catalogue line " + line + " has no parts");
            }
            if (new HashSet<>(parts).size() != parts.size()) {
                throw new IllegalStateException("Construction catalogue line " + line
                        + " contains duplicate parts");
            }
            if (!material.isWoodLike()
                    && (parts.contains(ConstructionPart.DOOR)
                    || parts.contains(ConstructionPart.TRAPDOOR)
                    || parts.contains(ConstructionPart.FENCE_GATE))) {
                throw new IllegalStateException("Construction catalogue line " + line
                        + " assigns a wood-only part to " + material);
            }

            ResourceLocation baseTexture = optionalLocation(
                    fields.get("base_texture"),
                    new ResourceLocation(baseBlock.getNamespace(), "block/" + baseBlock.getPath()),
                    line);
            ResourceLocation baseModel = optionalLocation(
                    fields.get("base_model"),
                    new ResourceLocation(baseBlock.getNamespace(), "block/" + baseBlock.getPath()),
                    line);

            EnumMap<ConstructionPart, String> variantIds = new EnumMap<>(ConstructionPart.class);
            EnumMap<ConstructionPart, String> variantNames = new EnumMap<>(ConstructionPart.class);
            for (ConstructionPart part : ConstructionPart.values()) {
                String override = fields.getOrDefault(part.suffix() + "_id", "").trim();
                variantIds.put(part, override.isEmpty() ? familyId + "_" + part.suffix() : override);
                String nameOverride = fields.getOrDefault(part.suffix() + "_name", "").trim();
                variantNames.put(part, nameOverride.isEmpty()
                        ? displayName + " " + part.displaySuffix()
                        : nameOverride);
            }

            Map<String, ResourceLocation> textures = new LinkedHashMap<>();
            putOptionalTexture(textures, fields, "door_bottom_texture", line);
            putOptionalTexture(textures, fields, "door_top_texture", line);
            putOptionalTexture(textures, fields, "door_item_texture", line);
            putOptionalTexture(textures, fields, "trapdoor_texture", line);
            putOptionalTexture(textures, fields, "beam_side_texture", line);
            putOptionalTexture(textures, fields, "beam_end_texture", line);

            return new CatalogueRow(
                    familyId,
                    baseBlock,
                    displayName,
                    material,
                    parts,
                    baseTexture,
                    baseModel,
                    variantIds,
                    variantNames,
                    textures);
        }

        String variantId(ConstructionPart part) {
            return variantIds.get(part);
        }

        String variantName(ConstructionPart part) {
            return variantNames.get(part);
        }

        Map<String, ResourceLocation> textures(ConstructionPart part) {
            Map<String, ResourceLocation> resolved = new LinkedHashMap<>();
            resolved.put("base", baseTexture);
            switch (part) {
                case DOOR -> {
                    resolved.put("bottom", textureOr("door_bottom_texture",
                            new ResourceLocation(GOTMod.MOD_ID, "block/" + variantId(part) + "_bottom")));
                    resolved.put("top", textureOr("door_top_texture",
                            new ResourceLocation(GOTMod.MOD_ID, "block/" + variantId(part) + "_top")));
                    resolved.put("item", textureOr("door_item_texture",
                            new ResourceLocation(GOTMod.MOD_ID, "item/" + variantId(part))));
                }
                case TRAPDOOR -> resolved.put("trapdoor", textureOr("trapdoor_texture",
                        new ResourceLocation(GOTMod.MOD_ID, "block/" + variantId(part))));
                case BEAM -> {
                    resolved.put("side", textureOr("beam_side_texture", baseTexture));
                    resolved.put("end", textureOr("beam_end_texture", baseTexture));
                }
                default -> {
                }
            }
            return resolved;
        }

        private ResourceLocation textureOr(String key, ResourceLocation fallback) {
            return specialTextures.getOrDefault(key, fallback);
        }

        private static void requireColumns(Map<String, String> fields) {
            List<String> required = List.of(
                    "family_id", "base_block", "display_name", "material", "parts",
                    "base_texture", "base_model",
                    "stairs_id", "slab_id", "wall_id", "fence_id", "fence_gate_id",
                    "door_id", "trapdoor_id", "beam_id",
                    "stairs_name", "slab_name", "wall_name", "fence_name", "fence_gate_name",
                    "door_name", "trapdoor_name", "beam_name",
                    "door_bottom_texture", "door_top_texture", "door_item_texture",
                    "trapdoor_texture", "beam_side_texture", "beam_end_texture");
            if (!fields.keySet().containsAll(required)) {
                List<String> missing = required.stream()
                        .filter(column -> !fields.containsKey(column))
                        .toList();
                throw new IllegalStateException("Construction catalogue is missing columns " + missing);
            }
        }

        private static String required(Map<String, String> fields, String key, int line) {
            String value = fields.getOrDefault(key, "").trim();
            if (value.isEmpty()) {
                throw new IllegalStateException(
                        "Construction catalogue line " + line + " has an empty " + key);
            }
            return value;
        }

        private static ResourceLocation optionalLocation(
                String value,
                ResourceLocation fallback,
                int line) {
            return value == null || value.isBlank() ? fallback : location(value, line);
        }

        private static ResourceLocation location(String value, int line) {
            ResourceLocation parsed = ResourceLocation.tryParse(value.trim().toLowerCase(Locale.ROOT));
            if (parsed == null) {
                throw new IllegalStateException(
                        "Invalid resource location on construction catalogue line " + line + ": " + value);
            }
            return parsed;
        }

        private static void putOptionalTexture(
                Map<String, ResourceLocation> output,
                Map<String, String> fields,
                String key,
                int line) {
            String value = fields.getOrDefault(key, "").trim();
            if (!value.isEmpty()) {
                output.put(key, location(value, line));
            }
        }
    }

    private GOTConstructionFamilies() {
    }
}
