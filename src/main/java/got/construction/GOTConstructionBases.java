package got.construction;

import got.GOTBlocks;
import got.GOTItems;
import got.GOTMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

/** Registers the full-block materials required by the construction catalogue. */
public final class GOTConstructionBases {
    public static final String CATALOGUE_RESOURCE = "/data/got/construction/base_blocks.csv";

    private static final List<ConstructionBase> BASES = new ArrayList<>();
    private static boolean bootstrapped;

    public static synchronized void bootstrap() {
        if (bootstrapped) {
            return;
        }
        for (CatalogueRow row : readCatalogue()) {
            register(row);
        }
        bootstrapped = true;
        GOTMod.LOGGER.info("Loaded {} construction base blocks", BASES.size());
    }

    public static List<ConstructionBase> bases() {
        return Collections.unmodifiableList(BASES);
    }

    private static void register(CatalogueRow row) {
        ResourceLocation id = new ResourceLocation(GOTMod.MOD_ID, row.id());
        boolean collision = GOTBlocks.BLOCKS.getEntries().stream()
                .anyMatch(entry -> entry.getId().equals(id));
        if (collision || BASES.stream().anyMatch(base -> base.id().equals(row.id()))) {
            throw new IllegalStateException("Duplicate construction base " + id);
        }

        Block copy = ForgeRegistries.BLOCKS.getValue(row.copyBlock());
        if (copy == null || copy == Blocks.AIR) {
            throw new IllegalStateException("Construction base " + id
                    + " references missing template " + row.copyBlock());
        }
        BlockBehaviour.Properties properties = BlockBehaviour.Properties.copy(copy);
        RegistryObject<Block> block = GOTBlocks.BLOCKS.register(row.id(), () -> switch (row.kind()) {
            case CUBE, COLUMN -> new Block(properties);
            case FALLING -> new FallingBlock(properties);
            case PILLAR -> new ConnectedPillarBlock(properties);
        });
        GOTItems.ITEMS.register(row.id(), () -> new BlockItem(block.get(), new Item.Properties()));
        GOTBlocks.BUILDING_BLOCKS.add(block);
        BASES.add(new ConstructionBase(
                row.id(), row.displayName(), row.material(), row.kind(),
                row.copyBlock(), block, row.textures()));
    }

    private static List<CatalogueRow> readCatalogue() {
        InputStream stream = GOTConstructionBases.class.getResourceAsStream(CATALOGUE_RESOURCE);
        if (stream == null) {
            throw new IllegalStateException("Missing construction base catalogue " + CATALOGUE_RESOURCE);
        }
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(stream, StandardCharsets.UTF_8))) {
            List<String> lines = reader.lines()
                    .map(String::trim)
                    .filter(line -> !line.isEmpty() && !line.startsWith("#"))
                    .collect(Collectors.toList());
            if (lines.isEmpty()) {
                throw new IllegalStateException("Construction base catalogue has no header");
            }
            List<String> header = parseCsvLine(lines.get(0));
            List<CatalogueRow> rows = new ArrayList<>();
            for (int index = 1; index < lines.size(); index++) {
                List<String> values = parseCsvLine(lines.get(index));
                if (values.size() != header.size()) {
                    throw new IllegalStateException("Construction base line " + (index + 1)
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
            throw new IllegalStateException("Unable to read construction base catalogue", exception);
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
            throw new IllegalStateException("Unclosed quote in construction base row: " + line);
        }
        values.add(current.toString().trim());
        return values;
    }

    private record CatalogueRow(
            String id,
            String displayName,
            ConstructionMaterial material,
            ConstructionBase.Kind kind,
            ResourceLocation copyBlock,
            Map<String, ResourceLocation> textures) {
        private static CatalogueRow from(Map<String, String> fields, int line) {
            List<String> required = List.of(
                    "id", "display_name", "material", "kind", "copy_block",
                    "side_texture", "top_texture", "bottom_texture", "face_texture",
                    "middle_texture", "top_segment_texture", "bottom_segment_texture");
            if (!fields.keySet().containsAll(required)) {
                throw new IllegalStateException("Construction base catalogue is missing columns");
            }
            String id = required(fields.get("id"), line, "id");
            String displayName = required(fields.get("display_name"), line, "display_name");
            ConstructionMaterial material = ConstructionMaterial.parse(
                    required(fields.get("material"), line, "material"));
            ConstructionBase.Kind kind = ConstructionBase.Kind.valueOf(
                    required(fields.get("kind"), line, "kind").toUpperCase(Locale.ROOT));
            ResourceLocation copyBlock = location(required(fields.get("copy_block"), line, "copy_block"));
            Map<String, ResourceLocation> textures = new LinkedHashMap<>();
            for (String key : List.of(
                    "side_texture", "top_texture", "bottom_texture", "face_texture",
                    "middle_texture", "top_segment_texture", "bottom_segment_texture")) {
                String value = fields.getOrDefault(key, "").trim();
                if (!value.isEmpty()) {
                    textures.put(key, location(value));
                }
            }
            if (!textures.containsKey("side_texture")) {
                throw new IllegalStateException("Construction base line " + line
                        + " requires side_texture");
            }
            return new CatalogueRow(id, displayName, material, kind, copyBlock, textures);
        }

        private static String required(String value, int line, String key) {
            if (value == null || value.trim().isEmpty()) {
                throw new IllegalStateException("Construction base line " + line
                        + " requires " + key);
            }
            return value.trim();
        }

        private static ResourceLocation location(String value) {
            return new ResourceLocation(value.trim().toLowerCase(Locale.ROOT));
        }
    }

    private GOTConstructionBases() {
    }
}
