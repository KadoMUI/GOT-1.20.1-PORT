package got.world.structure.major;

import got.GOTMod;
import got.common.world.map.GOTWaypoint;
import got.npc.GOTNorthNpcPopulation;
import got.world.structure.north.NorthStructureMarker;
import got.world.terrain.PlanetosTerrainSampler;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * Chunk-safe placement for authored Sponge v2 schematics.  WorldEdit's saved
 * paste offsets are applied relative to the exact GOT waypoint, so the copy
 * origin selected by the builder remains the fast-travel/structure anchor.
 */
public final class MajorSchematicStructureGenerator {
    private static final String RESOURCE_ROOT = "/data/got/structures/major/";
    private static final int UPDATE_FLAGS = Block.UPDATE_CLIENTS | Block.UPDATE_KNOWN_SHAPE;

    private static final List<Site> SITES = List.of(
            site(GOTWaypoint.ASSHAI, "Asshai.schem"),
            site(GOTWaypoint.ASTAPOR, "Astapor.schem"),
            site(GOTWaypoint.BRAAVOS, "Braavos.schem"),
            site(GOTWaypoint.CASTERLY_ROCK, "CasterlyRock.schem"),
            adjustedSite(GOTWaypoint.CASTLE_BLACK, "CastleBlack.schem", 0, 60, 0, Rotation.NONE),
            site(GOTWaypoint.CRASTERS_KEEP, "CrastersKeep.schem"),
            site(GOTWaypoint.DRAGONSTONE, "Dragonstone.schem"),
            site(GOTWaypoint.DREADFORT, "Dreadfort.schem", "ramsay_bolton", "roose_bolton"),
            site(GOTWaypoint.THE_EYRIE, "Eyrie.schem"),
            site(GOTWaypoint.GULLTOWN, "Gulltown.schem"),
            site(GOTWaypoint.HARRENHAL, "Harrenhall.schem"),
            site(GOTWaypoint.HIGHGARDEN, "Highgarden.schem"),
            site(GOTWaypoint.KINGS_LANDING, "KingsLanding.schem"),
            site(GOTWaypoint.LYS, "Lys.schem"),
            site(GOTWaypoint.MEEREEN, "Meereen.schem"),
            site(GOTWaypoint.MOAT_KAILIN, "MoatCailin.schem"),
            site(GOTWaypoint.MYR, "Myr.schem"),
            site(GOTWaypoint.OLDTOWN, "OldTown.schem"),
            site(GOTWaypoint.PENTOS, "Pentos.schem"),
            site(GOTWaypoint.PYKE, "Pyke.schem"),
            adjustedSite(GOTWaypoint.QARTH, "Qarth.schem", 0, 0, 21, Rotation.NONE),
            site(GOTWaypoint.RIVERRUN, "Riverrun.schem"),
            site(GOTWaypoint.SEAGARD, "Seagard.schem"),
            site(GOTWaypoint.STORMS_END, "StormsEnd.schem"),
            site(GOTWaypoint.SUNSPEAR, "Sunspear.schem"),
            site(GOTWaypoint.TYROSH, "Tyrosh.schem"),
            site(GOTWaypoint.VAES_DOTHRAK, "VaesDothrak.schem"),
            adjustedSite(GOTWaypoint.VOLANTIS, "Volantis.schem", 0, 0, 0, Rotation.CLOCKWISE_90),
            site(GOTWaypoint.WHITE_HARBOUR, "WhiteHarbor.schem"),
            site(GOTWaypoint.WINTERFELL, "Winterfell.schem", "robb_stark", "hodor", "arya_stark", "bran_stark", "rickon_stark", "maester_luwin", "osha"),
            site(GOTWaypoint.YUNKAI, "Yunkai.schem"),
            site(GOTWaypoint.TWINS_LEFT, "Twin.schem"),
            site(GOTWaypoint.TWINS_RIGHT, "Twin.schem"),
            site(GOTWaypoint.DEEPWOOD_MOTTE, "DeepwoodMotte.schem"),
            site(GOTWaypoint.GREYWATER_WATCH, "GreywaterWatch.schem"),
            site(GOTWaypoint.REDFORT, "Redfort.schem"),
            site(GOTWaypoint.EVENFALL_HALL, "EvenfallHall.schem"),
            site(GOTWaypoint.NAATH, "Naath.schem"),
            site(GOTWaypoint.MORMONTS_KEEP, "MormontKeep.schem")
    );

    private static final Map<String, SoftReference<SpongeSchematic>> CACHE = new HashMap<>();
    private static final Set<String> FAILED = new HashSet<>();

    private MajorSchematicStructureGenerator() {}

    public static void generate(WorldGenLevel level, ChunkAccess chunk, long seed) {
        int chunkMinX = chunk.getPos().getMinBlockX();
        int chunkMinZ = chunk.getPos().getMinBlockZ();
        int chunkMaxX = chunkMinX + 15;
        int chunkMaxZ = chunkMinZ + 15;
        PlanetosTerrainSampler terrain = new PlanetosTerrainSampler(seed);

        for (Site site : SITES) {
            SpongeSchematic schematic = load(site);
            if (schematic == null) continue;

            if (!intersects(site.structureMinX(schematic), site.structureMinZ(schematic),
                    site.structureMaxX(schematic), site.structureMaxZ(schematic),
                    chunkMinX, chunkMinZ, chunkMaxX, chunkMaxZ)) continue;

            int terrainAnchorY = terrain.structureAnchorHeight(site.anchorX(), site.anchorZ())
                    + 1 + site.anchorYOffset();
            int minimumAnchorY = level.getMinBuildHeight() - schematic.offsetY;
            int maximumAnchorY = level.getMaxBuildHeight() - schematic.offsetY - schematic.height;
            int anchorY = Math.max(minimumAnchorY, Math.min(maximumAnchorY, terrainAnchorY));
            schematic.placeChunk(level, site, anchorY,
                    chunkMinX, chunkMinZ, chunkMaxX, chunkMaxZ);
            spawnNorthernLegendaryCharacters(level, site, schematic, anchorY,
                    chunkMinX, chunkMinZ, chunkMaxX, chunkMaxZ);
        }
    }

    /** True when a legacy procedural fixed site is replaced by an authored schematic. */
    public static boolean replaces(GOTWaypoint waypoint) {
        for (Site site : SITES) if (site.waypoint() == waypoint) return true;
        return false;
    }

    public static int siteCount() {
        return SITES.size();
    }

    private static void spawnNorthernLegendaryCharacters(WorldGenLevel level, Site site,
                                                           SpongeSchematic schematic, int anchorY,
                                                           int chunkMinX, int chunkMinZ,
                                                           int chunkMaxX, int chunkMaxZ) {
        if (site.legendaryRoles().isEmpty()) return;
        int centerX = (site.structureMinX(schematic) + site.structureMaxX(schematic)) / 2;
        int centerZ = (site.structureMinZ(schematic) + site.structureMaxZ(schematic)) / 2;
        if (centerX < chunkMinX || centerX > chunkMaxX || centerZ < chunkMinZ || centerZ > chunkMaxZ) return;

        List<NorthStructureMarker> markers = new ArrayList<>();
        for (int i = 0; i < site.legendaryRoles().size(); i++) {
            int angle = Math.floorMod(i, 8);
            int x = centerX + switch (angle) { case 0, 1, 7 -> 4; case 3, 4, 5 -> -4; default -> 0; };
            int z = centerZ + switch (angle) { case 1, 2, 3 -> 4; case 5, 6, 7 -> -4; default -> 0; };
            markers.add(new NorthStructureMarker("legendary_npc:" + site.legendaryRoles().get(i),
                    new BlockPos(x, anchorY + 1, z), 0));
        }
        GOTNorthNpcPopulation.spawnMarkers(level, markers);
    }

    @Nullable
    private static SpongeSchematic load(Site site) {
        synchronized (CACHE) {
            if (FAILED.contains(site.file())) return null;
            SoftReference<SpongeSchematic> reference = CACHE.get(site.file());
            SpongeSchematic cached = reference == null ? null : reference.get();
            if (cached != null) return cached;

            try (InputStream input = MajorSchematicStructureGenerator.class
                    .getResourceAsStream(RESOURCE_ROOT + site.file())) {
                if (input == null) throw new IOException("missing resource " + site.file());
                SpongeSchematic loaded = SpongeSchematic.read(input);
                CACHE.put(site.file(), new SoftReference<>(loaded));
                GOTMod.LOGGER.info("Loaded authored structure {} for {} ({} x {} x {})",
                        site.file(), site.waypoint().getCodeName(),
                        loaded.width, loaded.height, loaded.length);
                return loaded;
            } catch (Exception exception) {
                FAILED.add(site.file());
                GOTMod.LOGGER.error("Unable to load authored structure {} for {}",
                        site.file(), site.waypoint().getCodeName(), exception);
                return null;
            }
        }
    }

    private static boolean intersects(int minX1, int minZ1, int maxX1, int maxZ1,
                                      int minX2, int minZ2, int maxX2, int maxZ2) {
        return maxX1 >= minX2 && minX1 <= maxX2 && maxZ1 >= minZ2 && minZ1 <= maxZ2;
    }


    private static Site site(GOTWaypoint waypoint, String file, String... legendaryRoles) {
        return new Site(waypoint, file, 0, 0, 0, Rotation.NONE, List.of(legendaryRoles));
    }

    private static Site adjustedSite(GOTWaypoint waypoint, String file,
                                     int anchorShiftX, int anchorShiftZ, int anchorYOffset,
                                     Rotation rotation) {
        return new Site(waypoint, file, anchorShiftX, anchorShiftZ, anchorYOffset,
                rotation, List.of());
    }

    private record Site(GOTWaypoint waypoint, String file,
                        int anchorShiftX, int anchorShiftZ, int anchorYOffset,
                        Rotation rotation, List<String> legendaryRoles) {
        int anchorX() { return waypoint.getCoordX() + anchorShiftX; }
        int anchorZ() { return waypoint.getCoordZ() + anchorShiftZ; }

        int structureMinX(SpongeSchematic schematic) {
            return anchorX() + (rotation == Rotation.CLOCKWISE_90
                    ? -schematic.offsetZ - schematic.length + 1 : schematic.offsetX);
        }

        int structureMinZ(SpongeSchematic schematic) {
            return anchorZ() + (rotation == Rotation.CLOCKWISE_90
                    ? schematic.offsetX : schematic.offsetZ);
        }

        int structureMaxX(SpongeSchematic schematic) {
            return structureMinX(schematic)
                    + (rotation == Rotation.CLOCKWISE_90 ? schematic.length : schematic.width) - 1;
        }

        int structureMaxZ(SpongeSchematic schematic) {
            return structureMinZ(schematic)
                    + (rotation == Rotation.CLOCKWISE_90 ? schematic.width : schematic.length) - 1;
        }
    }
    private record BlockEntityData(int x, int y, int z, CompoundTag tag) {}

    private static final class SpongeSchematic {
        private final int width;
        private final int height;
        private final int length;
        private final int offsetX;
        private final int offsetY;
        private final int offsetZ;
        private final BlockState[] palette;
        private final byte[] blockData;
        private final int[] rowOffsets;
        private final List<BlockEntityData> blockEntities;

        private SpongeSchematic(int width, int height, int length,
                                int offsetX, int offsetY, int offsetZ,
                                BlockState[] palette, byte[] blockData,
                                int[] rowOffsets, List<BlockEntityData> blockEntities) {
            this.width = width;
            this.height = height;
            this.length = length;
            this.offsetX = offsetX;
            this.offsetY = offsetY;
            this.offsetZ = offsetZ;
            this.palette = palette;
            this.blockData = blockData;
            this.rowOffsets = rowOffsets;
            this.blockEntities = blockEntities;
        }

        static SpongeSchematic read(InputStream input) throws IOException {
            CompoundTag root = NbtIo.readCompressed(input);
            int version = root.getInt("Version");
            if (version != 2) throw new IOException("unsupported Sponge schematic version " + version);

            int width = root.getShort("Width") & 0xffff;
            int height = root.getShort("Height") & 0xffff;
            int length = root.getShort("Length") & 0xffff;
            CompoundTag metadata = root.getCompound("Metadata");
            int offsetX = metadata.getInt("WEOffsetX");
            int offsetY = metadata.getInt("WEOffsetY");
            int offsetZ = metadata.getInt("WEOffsetZ");

            CompoundTag paletteTag = root.getCompound("Palette");
            int paletteSize = Math.max(1, root.getInt("PaletteMax"));
            BlockState[] palette = new BlockState[paletteSize];
            Arrays.fill(palette, Blocks.AIR.defaultBlockState());
            for (String serializedState : paletteTag.getAllKeys()) {
                int id = paletteTag.getInt(serializedState);
                if (id >= 0 && id < palette.length) palette[id] = parseBlockState(serializedState);
            }

            byte[] blockData = root.getByteArray("BlockData");
            int[] rowOffsets = indexRows(blockData, width, height, length);
            List<BlockEntityData> blockEntities = readBlockEntities(root);
            return new SpongeSchematic(width, height, length, offsetX, offsetY, offsetZ,
                    palette, blockData, rowOffsets, blockEntities);
        }

        void placeChunk(WorldGenLevel level, Site site, int anchorY,
                        int chunkMinX, int chunkMinZ, int chunkMaxX, int chunkMaxZ) {
            int worldMinY = anchorY + offsetY;
            int startX;
            int endX;
            int startZ;
            int endZ;
            if (site.rotation() == Rotation.CLOCKWISE_90) {
                startX = Math.max(0, chunkMinZ - site.anchorZ() - offsetX);
                endX = Math.min(width - 1, chunkMaxZ - site.anchorZ() - offsetX);
                startZ = Math.max(0, site.anchorX() - offsetZ - chunkMaxX);
                endZ = Math.min(length - 1, site.anchorX() - offsetZ - chunkMinX);
            } else {
                int worldMinX = site.anchorX() + offsetX;
                int worldMinZ = site.anchorZ() + offsetZ;
                startX = Math.max(0, chunkMinX - worldMinX);
                endX = Math.min(width - 1, chunkMaxX - worldMinX);
                startZ = Math.max(0, chunkMinZ - worldMinZ);
                endZ = Math.min(length - 1, chunkMaxZ - worldMinZ);
            }
            BlockPos.MutableBlockPos cursorPos = new BlockPos.MutableBlockPos();

            for (int y = 0; y < height; y++) {
                int worldY = worldMinY + y;
                if (worldY < level.getMinBuildHeight() || worldY >= level.getMaxBuildHeight()) continue;
                for (int z = startZ; z <= endZ; z++) {
                    int[] byteCursor = {rowOffsets[y * length + z]};
                    for (int x = 0; x <= endX; x++) {
                        int stateId = readVarInt(blockData, byteCursor);
                        if (x < startX) continue;
                        BlockState state = stateId >= 0 && stateId < palette.length
                                ? palette[stateId] : Blocks.AIR.defaultBlockState();
                        // Temporary global paste policy: schematic air never
                        // replaces world blocks. This applies to Winterfell,
                        // Sunspear, and every other authored major structure.
                        if (state.isAir()) continue;
                        int worldX = worldX(site, x, z);
                        int worldZ = worldZ(site, x, z);
                        state = state.rotate(site.rotation());
                        cursorPos.set(worldX, worldY, worldZ);
                        if (!level.getBlockState(cursorPos).equals(state)) {
                            level.setBlock(cursorPos, state, UPDATE_FLAGS);
                        }
                    }
                }
            }

            for (BlockEntityData data : blockEntities) {
                int x = worldX(site, data.x(), data.z());
                int z = worldZ(site, data.x(), data.z());
                int y = worldMinY + data.y();
                if (x < chunkMinX || x > chunkMaxX || z < chunkMinZ || z > chunkMaxZ
                        || y < level.getMinBuildHeight() || y >= level.getMaxBuildHeight()) continue;
                BlockPos position = new BlockPos(x, y, z);
                BlockEntity blockEntity = level.getBlockEntity(position);
                if (blockEntity == null) continue;
                CompoundTag tag = data.tag().copy();
                String id = tag.getString("Id");
                tag.remove("Id");
                tag.remove("Pos");
                if (!id.isEmpty()) tag.putString("id", id);
                tag.putInt("x", x);
                tag.putInt("y", y);
                tag.putInt("z", z);
                blockEntity.load(tag);
                blockEntity.setChanged();
            }
        }

        private int worldX(Site site, int x, int z) {
            return site.rotation() == Rotation.CLOCKWISE_90
                    ? site.anchorX() - offsetZ - z
                    : site.anchorX() + offsetX + x;
        }

        private int worldZ(Site site, int x, int z) {
            return site.rotation() == Rotation.CLOCKWISE_90
                    ? site.anchorZ() + offsetX + x
                    : site.anchorZ() + offsetZ + z;
        }

        String dimensionsAndOffset() {
            return width + "x" + height + "x" + length + " @ " + offsetX + "," + offsetY + "," + offsetZ;
        }

        private static int[] indexRows(byte[] data, int width, int height, int length) throws IOException {
            int rows = Math.multiplyExact(height, length);
            int[] offsets = new int[rows + 1];
            int[] cursor = {0};
            for (int row = 0; row < rows; row++) {
                offsets[row] = cursor[0];
                for (int x = 0; x < width; x++) readVarIntChecked(data, cursor);
            }
            offsets[rows] = cursor[0];
            if (cursor[0] != data.length) {
                throw new IOException("BlockData contains " + (data.length - cursor[0]) + " trailing bytes");
            }
            return offsets;
        }

        private static List<BlockEntityData> readBlockEntities(CompoundTag root) {
            List<BlockEntityData> result = new ArrayList<>();
            ListTag list = root.getList("BlockEntities", Tag.TAG_COMPOUND);
            for (int i = 0; i < list.size(); i++) {
                CompoundTag tag = list.getCompound(i);
                int[] position = tag.getIntArray("Pos");
                if (position.length == 3) {
                    result.add(new BlockEntityData(position[0], position[1], position[2], tag.copy()));
                }
            }
            return List.copyOf(result);
        }

        private static BlockState parseBlockState(String serialized) {
            int propertiesStart = serialized.indexOf('[');
            String blockName = propertiesStart < 0 ? serialized : serialized.substring(0, propertiesStart);
            ResourceLocation id = ResourceLocation.tryParse(blockName);
            Block block = id == null ? null : ForgeRegistries.BLOCKS.getValue(id);
            if (block == null) {
                GOTMod.LOGGER.warn("Authored schematic references missing block {}", blockName);
                return Blocks.AIR.defaultBlockState();
            }

            BlockState state = block.defaultBlockState();
            if (propertiesStart < 0 || !serialized.endsWith("]")) return state;
            String properties = serialized.substring(propertiesStart + 1, serialized.length() - 1);
            if (properties.isEmpty()) return state;
            for (String entry : properties.split(",")) {
                int separator = entry.indexOf('=');
                if (separator <= 0) continue;
                String name = entry.substring(0, separator);
                String value = entry.substring(separator + 1);
                Property<?> property = block.getStateDefinition().getProperty(name);
                if (property != null) state = setProperty(state, property, value);
            }
            return state;
        }

        private static <T extends Comparable<T>> BlockState setProperty(BlockState state,
                                                                         Property<T> property,
                                                                         String serializedValue) {
            Optional<T> value = property.getValue(serializedValue);
            return value.map(parsed -> state.setValue(property, parsed)).orElse(state);
        }

        private static int readVarInt(byte[] data, int[] cursor) {
            int value = 0;
            int shift = 0;
            while (cursor[0] < data.length) {
                int next = data[cursor[0]++] & 0xff;
                value |= (next & 0x7f) << shift;
                if ((next & 0x80) == 0) return value;
                shift += 7;
                if (shift > 28) return 0;
            }
            return 0;
        }

        private static int readVarIntChecked(byte[] data, int[] cursor) throws IOException {
            int start = cursor[0];
            int value = readVarInt(data, cursor);
            if (cursor[0] == start || cursor[0] > data.length) throw new IOException("truncated BlockData varint");
            return value;
        }
    }
}
