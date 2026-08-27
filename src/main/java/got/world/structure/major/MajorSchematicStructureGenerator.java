package got.world.structure.major;

import got.GOTMod;
import got.GOTAbstractBannerEntity;
import got.GOTStandingBannerEntity;
import got.GOTWallBannerEntity;
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
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.entity.Entity;
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
import java.util.Collections;
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

            int terrainApron = schematic.terrainApron();
            if (!intersects(site.structureMinX(schematic) - terrainApron, site.structureMinZ(schematic) - terrainApron,
                    site.structureMaxX(schematic) + terrainApron, site.structureMaxZ(schematic) + terrainApron,
                    chunkMinX, chunkMinZ, chunkMaxX, chunkMaxZ)) continue;

            int terrainAnchorY = schematic.recommendedAnchorY(terrain, site) + site.anchorYOffset();
            int minimumAnchorY = level.getMinBuildHeight() - schematic.offsetY;
            int maximumAnchorY = level.getMaxBuildHeight() - schematic.offsetY - schematic.height;
            int anchorY = Math.max(minimumAnchorY, Math.min(maximumAnchorY, terrainAnchorY));
            schematic.prepareTerrain(level, terrain, site, anchorY,
                    chunkMinX, chunkMinZ, chunkMaxX, chunkMaxZ);
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
    private record SchematicEntityData(double x, double y, double z, CompoundTag tag) {}

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
        private final List<SchematicEntityData> entities;
        private final int groundLayer;
        private final boolean[] groundMask;

        private SpongeSchematic(int width, int height, int length,
                                int offsetX, int offsetY, int offsetZ,
                                BlockState[] palette, byte[] blockData,
                                int[] rowOffsets, List<BlockEntityData> blockEntities,
                                List<SchematicEntityData> entities, int groundLayer, boolean[] groundMask) {
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
            this.entities = entities;
            this.groundLayer = groundLayer;
            this.groundMask = groundMask;
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
            List<SchematicEntityData> entities = readEntities(root);
            GroundProfile ground = analyzeGround(palette, blockData, rowOffsets, width, height, length);
            return new SpongeSchematic(width, height, length, offsetX, offsetY, offsetZ,
                    palette, blockData, rowOffsets, blockEntities, entities, ground.layer(), ground.mask());
        }

        int terrainApron() { return 6; }

        int recommendedAnchorY(PlanetosTerrainSampler terrain, Site site) {
            List<Integer> heights = new ArrayList<>();
            int count = 0;
            for (boolean value : groundMask) if (value) count++;
            int stride = Math.max(1, (int)Math.sqrt(Math.max(1, count) / 256.0D));
            for (int z = 0; z < length; z += stride) {
                for (int x = 0; x < width; x += stride) {
                    if (!groundMask[z * width + x]) continue;
                    heights.add(terrain.surfaceHeight(worldX(site, x, z), worldZ(site, x, z)));
                }
            }
            int ground = terrain.structureAnchorHeight(site.anchorX(), site.anchorZ());
            if (!heights.isEmpty()) {
                Collections.sort(heights);
                ground = heights.get(heights.size() / 2);
            }
            // The common schematic ground layer is a floor/road block. Place
            // it one block above the median natural terrain surface.
            return ground + 1 - offsetY - groundLayer;
        }

        void prepareTerrain(WorldGenLevel level, PlanetosTerrainSampler terrain, Site site, int anchorY,
                            int chunkMinX, int chunkMinZ, int chunkMaxX, int chunkMaxZ) {
            if (groundMask.length == 0) return;
            int apron = terrainApron();
            int targetGroundY = anchorY + offsetY + groundLayer - 1;
            BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
            for (int wz = chunkMinZ; wz <= chunkMaxZ; wz++) {
                for (int wx = chunkMinX; wx <= chunkMaxX; wx++) {
                    int[] local = localXZ(site, wx, wz);
                    int lx = local[0], lz = local[1];
                    int nearest = nearestGroundDistance(lx, lz, apron);
                    if (nearest > apron) continue;

                    float weight = nearest == 0 ? 1.0F : Math.max(0.0F, 1.0F - nearest / (float)(apron + 1));
                    weight = weight * weight * (3.0F - 2.0F * weight);
                    int naturalY = terrain.surfaceHeight(wx, wz);
                    pos.set(wx, naturalY, wz);
                    BlockState naturalTop = level.getBlockState(pos);
                    // Leave open water alone. Docks, bridges, harbours and
                    // river crossings should keep their water instead of
                    // gaining an accidental dirt causeway.
                    if (!naturalTop.getFluidState().isEmpty()) continue;

                    int desiredY = Math.round(naturalY + (targetGroundY - naturalY) * weight);
                    reshapeTerrainColumn(level, wx, wz, naturalY, desiredY, nearest == 0, naturalTop, pos);
                }
            }
        }

        private int nearestGroundDistance(int lx, int lz, int apron) {
            int best = apron + 1;
            for (int dz = -apron; dz <= apron; dz++) {
                int z = lz + dz;
                if (z < 0 || z >= length) continue;
                for (int dx = -apron; dx <= apron; dx++) {
                    int x = lx + dx;
                    if (x < 0 || x >= width || !groundMask[z * width + x]) continue;
                    int distance = Math.max(Math.abs(dx), Math.abs(dz));
                    if (distance < best) best = distance;
                }
            }
            return best;
        }

        private int[] localXZ(Site site, int worldX, int worldZ) {
            if (site.rotation() == Rotation.CLOCKWISE_90) {
                return new int[]{worldZ - site.anchorZ() - offsetX,
                        site.anchorX() - offsetZ - worldX};
            }
            return new int[]{worldX - site.anchorX() - offsetX,
                    worldZ - site.anchorZ() - offsetZ};
        }

        private static void reshapeTerrainColumn(WorldGenLevel level, int x, int z,
                                                 int fromY, int toY, boolean core,
                                                 BlockState originalTop, BlockPos.MutableBlockPos pos) {
            int minY = level.getMinBuildHeight();
            int maxY = level.getMaxBuildHeight() - 1;
            fromY = Math.max(minY, Math.min(maxY, fromY));
            toY = Math.max(minY, Math.min(maxY, toY));
            if (fromY > toY) {
                for (int y = Math.min(maxY, fromY + 6); y > toY; y--) {
                    pos.set(x, y, z);
                    BlockState state = level.getBlockState(pos);
                    if (state.isAir()) continue;
                    if (!isNaturalTerrain(state)) continue;
                    level.setBlock(pos, Blocks.AIR.defaultBlockState(), UPDATE_FLAGS);
                }
            } else if (fromY < toY) {
                BlockState fill = naturalFill(originalTop);
                for (int y = fromY + 1; y <= toY; y++) {
                    pos.set(x, y, z);
                    BlockState existing = level.getBlockState(pos);
                    if (!existing.isAir() && existing.getFluidState().isEmpty() && !isNaturalTerrain(existing)) continue;
                    BlockState state = (!core && y == toY) ? naturalTop(originalTop) : fill;
                    level.setBlock(pos, state, UPDATE_FLAGS);
                }
            }
        }

        private static boolean isNaturalTerrain(BlockState state) {
            Block block = state.getBlock();
            return state.is(BlockTags.LEAVES) || state.is(BlockTags.LOGS)
                    || block == Blocks.GRASS_BLOCK || block == Blocks.DIRT || block == Blocks.COARSE_DIRT
                    || block == Blocks.PODZOL || block == Blocks.ROOTED_DIRT || block == Blocks.MUD
                    || block == Blocks.STONE || block == Blocks.DEEPSLATE || block == Blocks.GRAVEL
                    || block == Blocks.SAND || block == Blocks.RED_SAND || block == Blocks.CLAY
                    || block == Blocks.SNOW || block == Blocks.SNOW_BLOCK || state.canBeReplaced();
        }

        private static BlockState naturalFill(BlockState top) {
            Block block = top.getBlock();
            if (block == Blocks.SAND || block == Blocks.RED_SAND || block == Blocks.GRAVEL
                    || block == Blocks.CLAY || block == Blocks.MUD) return top;
            return Blocks.DIRT.defaultBlockState();
        }

        private static BlockState naturalTop(BlockState top) {
            return !top.isAir() && top.getFluidState().isEmpty() && isNaturalTerrain(top)
                    ? top : Blocks.GRASS_BLOCK.defaultBlockState();
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

            placeBannerEntities(level, site, anchorY, chunkMinX, chunkMinZ, chunkMaxX, chunkMaxZ);
        }

        private void placeBannerEntities(WorldGenLevel level, Site site, int anchorY,
                                         int chunkMinX, int chunkMinZ, int chunkMaxX, int chunkMaxZ) {
            if (entities.isEmpty()) return;
            for (SchematicEntityData data : entities) {
                CompoundTag saved = data.tag().copy();
                CompoundTag entityTag = saved.contains("Data", Tag.TAG_COMPOUND)
                        ? saved.getCompound("Data").copy() : saved.copy();
                String id = entityTag.getString("id");
                if (id.isEmpty()) id = entityTag.getString("Id");
                if (id.isEmpty()) id = saved.getString("Id");
                if (!id.equals("got:standing_banner") && !id.equals("got:wall_banner")) continue;

                double localX = data.x() + offsetX;
                double localY = data.y() + offsetY;
                double localZ = data.z() + offsetZ;
                double worldX = site.rotation() == Rotation.CLOCKWISE_90
                        ? site.anchorX() - localZ : site.anchorX() + localX;
                double worldZ = site.rotation() == Rotation.CLOCKWISE_90
                        ? site.anchorZ() + localX : site.anchorZ() + localZ;
                double worldY = anchorY + localY;
                if (worldX < chunkMinX || worldX >= chunkMaxX + 1.0D
                        || worldZ < chunkMinZ || worldZ >= chunkMaxZ + 1.0D) continue;

                Entity created = id.equals("got:wall_banner")
                        ? got.GOTEntities.WALL_BANNER.get().create(level.getLevel())
                        : got.GOTEntities.STANDING_BANNER.get().create(level.getLevel());
                if (!(created instanceof GOTAbstractBannerEntity banner)) continue;

                entityTag.remove("UUID");
                entityTag.remove("UUIDMost");
                entityTag.remove("UUIDLeast");
                entityTag.putString("id", id);
                entityTag.remove("Id");
                try { banner.load(entityTag); }
                catch (Exception ex) {
                    GOTMod.LOGGER.warn("Unable to restore banner NBT from schematic {}", site.file(), ex);
                }

                if (banner instanceof GOTStandingBannerEntity standing) {
                    standing.setPos(worldX, worldY, worldZ);
                    standing.setYRot(standing.getYRot() + rotationDegrees(site.rotation()));
                    standing.yRotO = standing.getYRot();
                } else if (banner instanceof GOTWallBannerEntity wall) {
                    net.minecraft.core.Direction outward = wall.getDirection();
                    if (site.rotation() == Rotation.CLOCKWISE_90) outward = outward.getClockWise();
                    int anchorYWorld = net.minecraft.util.Mth.floor(worldY) + 1;
                    int anchorXWorld = net.minecraft.util.Mth.floor(worldX - outward.getStepX() * 0.54D);
                    int anchorZWorld = net.minecraft.util.Mth.floor(worldZ - outward.getStepZ() * 0.54D);
                    wall.setAnchor(new BlockPos(anchorXWorld, anchorYWorld, anchorZWorld), outward);
                }
                level.addFreshEntity(banner);
            }
        }

        private static float rotationDegrees(Rotation rotation) {
            return switch (rotation) {
                case CLOCKWISE_90 -> 90.0F;
                case CLOCKWISE_180 -> 180.0F;
                case COUNTERCLOCKWISE_90 -> -90.0F;
                default -> 0.0F;
            };
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

        private record GroundProfile(int layer, boolean[] mask) {}

        private static GroundProfile analyzeGround(BlockState[] palette, byte[] data, int[] rowOffsets,
                                                   int width, int height, int length) {
            int columns = width * length;
            int[] lowest = new int[columns];
            Arrays.fill(lowest, Integer.MAX_VALUE);
            Map<Integer, Integer> frequency = new HashMap<>();

            for (int y = 0; y < height; y++) {
                for (int z = 0; z < length; z++) {
                    int[] cursor = {rowOffsets[y * length + z]};
                    for (int x = 0; x < width; x++) {
                        int id = readVarInt(data, cursor);
                        BlockState state = id >= 0 && id < palette.length ? palette[id] : Blocks.AIR.defaultBlockState();
                        if (state.isAir() || !state.getFluidState().isEmpty()) continue;
                        int column = z * width + x;
                        if (lowest[column] == Integer.MAX_VALUE) lowest[column] = y;
                    }
                }
            }
            for (int y : lowest) if (y != Integer.MAX_VALUE) frequency.merge(y, 1, Integer::sum);
            int ground = 0, best = -1;
            for (Map.Entry<Integer, Integer> e : frequency.entrySet()) {
                if (e.getValue() > best) { best = e.getValue(); ground = e.getKey(); }
            }

            boolean[] atGround = new boolean[columns];
            if (ground >= 0 && ground < height) {
                for (int z = 0; z < length; z++) {
                    int[] cursor = {rowOffsets[ground * length + z]};
                    for (int x = 0; x < width; x++) {
                        int id = readVarInt(data, cursor);
                        BlockState state = id >= 0 && id < palette.length ? palette[id] : Blocks.AIR.defaultBlockState();
                        if (!state.isAir() && state.getFluidState().isEmpty()) atGround[z * width + x] = true;
                    }
                }
            }
            boolean[] mask = new boolean[columns];
            for (int i = 0; i < columns; i++) {
                int y = lowest[i];
                mask[i] = atGround[i] || (y != Integer.MAX_VALUE && Math.abs(y - ground) <= 1);
            }
            return new GroundProfile(ground, mask);
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

        private static List<SchematicEntityData> readEntities(CompoundTag root) {
            List<SchematicEntityData> result = new ArrayList<>();
            ListTag list = root.getList("Entities", Tag.TAG_COMPOUND);
            for (int i = 0; i < list.size(); i++) {
                CompoundTag tag = list.getCompound(i);
                double[] pos = readEntityPos(tag);
                if (pos != null) result.add(new SchematicEntityData(pos[0], pos[1], pos[2], tag.copy()));
            }
            return List.copyOf(result);
        }

        @Nullable
        private static double[] readEntityPos(CompoundTag tag) {
            if (tag.contains("Pos", Tag.TAG_LIST)) {
                ListTag pos = tag.getList("Pos", Tag.TAG_DOUBLE);
                if (pos.size() >= 3) return new double[]{pos.getDouble(0), pos.getDouble(1), pos.getDouble(2)};
            }
            int[] ints = tag.getIntArray("Pos");
            if (ints.length == 3) return new double[]{ints[0], ints[1], ints[2]};
            if (tag.contains("Data", Tag.TAG_COMPOUND)) {
                CompoundTag data = tag.getCompound("Data");
                if (data.contains("Pos", Tag.TAG_LIST)) {
                    ListTag pos = data.getList("Pos", Tag.TAG_DOUBLE);
                    if (pos.size() >= 3) return new double[]{pos.getDouble(0), pos.getDouble(1), pos.getDouble(2)};
                }
            }
            return null;
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
