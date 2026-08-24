package got.world.structure.nightwatch;

import got.GOTMod;
import got.common.world.map.GOTBeziers;
import got.common.world.map.GOTWaypoint;
import got.world.biome.GOTBiomeMetadata;
import got.world.biome.PlanetosBiomeManager;
import got.npc.GOTNightWatchNpcPopulation;
import got.world.structure.major.MajorSchematicStructureGenerator;
import got.world.structure.nightwatch.NightWatchStructureTemplates.CastleKind;
import got.world.structure.north.NorthStructureBuilder;
import got.world.structure.north.NorthStructureMarker;
import got.world.structure.north.NorthStructurePalette;
import got.world.terrain.PlanetosTerrainSampler;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;

/** Generates the authored Wall, fixed Wall castles, and Gift villages. */
public final class PlanetosNightWatchStructureGenerator {
    public static final int WALL_TOP = 150;
    private static final int UPDATE_FLAGS = Block.UPDATE_CLIENTS | Block.UPDATE_KNOWN_SHAPE;
    private static final int GRID = 12 * 16;
    private static final int MAX_RADIUS = 80;

    private static final List<FixedSite> FIXED_SITES = List.of(
            abandoned(GOTWaypoint.GREENGUARD),
            abandoned(GOTWaypoint.THE_TORCHES),
            abandoned(GOTWaypoint.THE_LONG_BARROW),
            abandoned(GOTWaypoint.RIMEGATE),
            abandoned(GOTWaypoint.SABLE_HALL),
            abandoned(GOTWaypoint.WOODSWATCH),
            abandoned(GOTWaypoint.NIGHTFORT),
            abandoned(GOTWaypoint.DEEP_LAKE),
            abandoned(GOTWaypoint.OAKENSHIELD),
            abandoned(GOTWaypoint.ICEMARK),
            abandoned(GOTWaypoint.HOARFROST_HILL),
            abandoned(GOTWaypoint.STONEDOOR),
            abandoned(GOTWaypoint.GREYGUARD),
            abandoned(GOTWaypoint.QUEENSGATE),
            abandoned(GOTWaypoint.SENTINEL_STAND),
            castle(GOTWaypoint.CASTLE_BLACK, CastleKind.CASTLE_BLACK),
            castle(GOTWaypoint.EASTWATCH, CastleKind.EASTWATCH),
            castle(GOTWaypoint.SHADOW_TOWER, CastleKind.SHADOW_TOWER)
    );

    private PlanetosNightWatchStructureGenerator() {}

    public static void generate(WorldGenLevel level, ChunkAccess chunk, long seed) {
        int minX = chunk.getPos().getMinBlockX();
        int minZ = chunk.getPos().getMinBlockZ();
        int maxX = minX + 15;
        int maxZ = minZ + 15;
        generateWall(level, seed, minX, minZ);

        PlanetosTerrainSampler terrain = new PlanetosTerrainSampler(seed);
        for (FixedSite site : FIXED_SITES) {
            if (MajorSchematicStructureGenerator.replaces(site.waypoint())) continue;
            if (!intersects(site.x(), site.z(), site.radius(), minX, minZ, maxX, maxZ)) continue;
            int y = terrain.surfaceHeight(site.x(), site.z()) + 1;
            boolean abandoned = site.castleKind() == CastleKind.ABANDONED;
            List<NorthStructureMarker> markers = new ArrayList<>();
            NorthStructureBuilder builder = new NorthStructureBuilder(level, site.x(), y, site.z(),
                    site.rotation(), seedFor(seed, site.x(), site.z()),
                    NorthStructurePalette.gift(abandoned), minX, minZ, maxX, maxZ,
                    markers);
            if (!site.village()) {
                got.world.structure.schematic.AuthoredSchematicTemplate.place(
                        builder, "WesterosCastleSmall.schem");
            }
            GOTNightWatchNpcPopulation.spawnMarkers(level, markers);
        }
        // Disabled: legacy random villages are not in the authoritative schematic ZIP.
    }

    public static boolean spawn(ServerLevel level, BlockPos origin,
                                NightWatchStructureType type, int rotation) {
        int radius = type.radius() + 8;
        long seed = seedFor(level.getSeed(), origin.getX(), origin.getZ()) ^ type.legacyId();
        List<NorthStructureMarker> markers = new ArrayList<>();
        NorthStructureBuilder builder = new NorthStructureBuilder(level,
                origin.getX(), origin.getY(), origin.getZ(), rotation, seed,
                NorthStructurePalette.gift(false), origin.getX() - radius, origin.getZ() - radius,
                origin.getX() + radius, origin.getZ() + radius, markers);
        NightWatchStructureTemplates.generate(builder, type);
        GOTNightWatchNpcPopulation.spawnMarkers(level, markers);
        GOTMod.LOGGER.info("Spawned {} at {}, {}, {} with {} future population markers",
                type.serializedName(), origin.getX(), origin.getY(), origin.getZ(), markers.size());
        return true;
    }

    public static int fixedSiteCount() { return FIXED_SITES.size(); }
    public static long abandonedCastleCount() {
        return FIXED_SITES.stream().filter(site -> site.castleKind() == CastleKind.ABANDONED).count();
    }

    private static void generateWall(WorldGenLevel level, long seed, int minX, int minZ) {
        int westX = Math.min(GOTWaypoint.WESTWATCH.getCoordX(), GOTWaypoint.EASTWATCH.getCoordX()) - 64;
        int eastX = Math.max(GOTWaypoint.WESTWATCH.getCoordX(), GOTWaypoint.EASTWATCH.getCoordX()) + 64;
        int northZ = Math.min(GOTWaypoint.WESTWATCH.getCoordZ(), GOTWaypoint.EASTWATCH.getCoordZ()) - 96;
        int southZ = Math.max(GOTWaypoint.WESTWATCH.getCoordZ(), GOTWaypoint.EASTWATCH.getCoordZ()) + 96;
        if (minX > eastX || minX + 15 < westX || minZ > southZ || minZ + 15 < northZ) return;

        BlockState iceBrick = registered("brick_ice_bricks", Blocks.PACKED_ICE.defaultBlockState());
        for (int localZ = 0; localZ < 16; localZ++) {
            for (int localX = 0; localX < 16; localX++) {
                int x = minX + localX;
                int z = minZ + localZ;
                if (!GOTBeziers.isWallAt(x, z)) continue;
                for (int y = PlanetosTerrainSampler.SEA_LEVEL; y <= WALL_TOP; y++) {
                    long value = seedFor(seed ^ y * 73428767L, x, z);
                    int choice = (int)Math.floorMod(value, 7L);
                    BlockState state = choice < 4 ? iceBrick
                            : choice < 6 ? Blocks.PACKED_ICE.defaultBlockState()
                            : Blocks.SNOW_BLOCK.defaultBlockState();
                    level.setBlock(new BlockPos(x, y, z), state, UPDATE_FLAGS);
                }
            }
        }
    }

    private static void generateRandomVillages(WorldGenLevel level, PlanetosTerrainSampler terrain,
                                               long seed, int minX, int minZ, int maxX, int maxZ) {
        int minGridX = Math.floorDiv(minX - MAX_RADIUS, GRID);
        int maxGridX = Math.floorDiv(maxX + MAX_RADIUS, GRID);
        int minGridZ = Math.floorDiv(minZ - MAX_RADIUS, GRID);
        int maxGridZ = Math.floorDiv(maxZ + MAX_RADIUS, GRID);
        for (int gridZ = minGridZ; gridZ <= maxGridZ; gridZ++) {
            for (int gridX = minGridX; gridX <= maxGridX; gridX++) {
                long cellSeed = seedFor(seed ^ 0x4749465453455454L, gridX, gridZ);
                int x = gridX * GRID + GRID / 2 + ((int)Math.floorMod(cellSeed, 3L) - 1) * 16;
                int z = gridZ * GRID + GRID / 2 + ((int)Math.floorMod(cellSeed >>> 9, 3L) - 1) * 16;
                if (!intersects(x, z, 54, minX, minZ, maxX, maxZ)) continue;
                GOTBiomeMetadata metadata = PlanetosBiomeManager.getMetadata(x, z);
                if (metadata == null || !isGift(metadata.id()) || !metadata.allowsSettlements()) continue;
                if (nearFixedSite(x, z, 180)
                        || GOTBeziers.isWallNear(x, z, 80) >= 0.0F
                        || GOTBeziers.isRoadNear(x, z, 72) >= 0.0F
                        || GOTBeziers.isLinkerNear(x, z, 72) >= 0.0F
                        || !validGiftSite(terrain, x, z)) continue;
                int y = terrain.surfaceHeight(x, z) + 1;
                List<NorthStructureMarker> markers = new ArrayList<>();
                NorthStructureBuilder builder = new NorthStructureBuilder(level, x, y, z,
                        (int)Math.floorMod(cellSeed >>> 17, 4L), cellSeed,
                        NorthStructurePalette.gift(false), minX, minZ, maxX, maxZ,
                        markers);
                NightWatchStructureTemplates.village(builder);
                GOTNightWatchNpcPopulation.spawnMarkers(level, markers);
            }
        }
    }

    private static boolean validGiftSite(PlanetosTerrainSampler terrain, int x, int z) {
        int min = terrain.surfaceHeight(x, z);
        int max = min;
        for (int[] offset : new int[][]{{24,0},{-24,0},{0,24},{0,-24}}) {
            GOTBiomeMetadata metadata = PlanetosBiomeManager.getMetadata(x + offset[0], z + offset[1]);
            if (metadata == null || !isGift(metadata.id())) return false;
            int height = terrain.surfaceHeight(x + offset[0], z + offset[1]);
            min = Math.min(min, height);
            max = Math.max(max, height);
        }
        return max - min <= 10;
    }

    private static boolean nearFixedSite(int x, int z, int radius) {
        long radiusSq = (long)radius * radius;
        for (FixedSite site : FIXED_SITES) {
            long dx = x - site.x();
            long dz = z - site.z();
            if (dx * dx + dz * dz <= radiusSq) return true;
        }
        return false;
    }

    private static boolean isGift(String id) {
        return id.equals("gift_new") || id.equals("gift_old");
    }

    private static boolean intersects(int x, int z, int radius,
                                      int minX, int minZ, int maxX, int maxZ) {
        return x + radius >= minX && x - radius <= maxX
                && z + radius >= minZ && z - radius <= maxZ;
    }

    private static FixedSite abandoned(GOTWaypoint waypoint) {
        return castle(waypoint, CastleKind.ABANDONED);
    }

    private static FixedSite castle(GOTWaypoint waypoint, CastleKind kind) {
        int radius = kind == CastleKind.EASTWATCH ? 80 : 64;
        return new FixedSite(waypoint, waypoint.getCoordX(), waypoint.getCoordZ(),
                waypoint.getRotation(), radius, kind, false);
    }

    private static FixedSite village(GOTWaypoint waypoint) {
        return new FixedSite(waypoint, waypoint.getCoordX(), waypoint.getCoordZ(),
                waypoint.getRotation(), 58, null, true);
    }

    private static BlockState registered(String id, BlockState fallback) {
        Block block = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(GOTMod.MOD_ID, id));
        return block == null || block == Blocks.AIR ? fallback : block.defaultBlockState();
    }

    private static long seedFor(long seed, long x, long z) {
        long value = seed ^ x * 341873128712L ^ z * 132897987541L;
        value ^= value >>> 33;
        value *= 0xff51afd7ed558ccdL;
        value ^= value >>> 33;
        value *= 0xc4ceb9fe1a85ec53L;
        return value ^ value >>> 33;
    }

    private record FixedSite(GOTWaypoint waypoint, int x, int z, int rotation,
                             int radius, CastleKind castleKind, boolean village) {}
}
