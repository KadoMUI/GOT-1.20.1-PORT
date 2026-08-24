package got.world.structure.wildling;

import got.GOTMod;
import got.common.world.map.GOTBeziers;
import got.common.world.map.GOTWaypoint;
import got.world.biome.GOTBiomeMetadata;
import got.world.biome.PlanetosBiomeManager;
import got.npc.GOTNightWatchNpcPopulation;
import got.npc.GOTWhiteWalkerNpcPopulation;
import got.npc.GOTWildlingNpcPopulation;
import got.world.structure.major.MajorSchematicStructureGenerator;
import got.world.structure.north.NorthStructureBuilder;
import got.world.structure.north.NorthStructureMarker;
import got.world.structure.north.NorthStructurePalette;
import got.world.terrain.PlanetosTerrainSampler;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkAccess;

import java.util.ArrayList;
import java.util.List;

/** Natural and authored structure pass for the Free Folk lands beyond the Wall. */
public final class PlanetosWildlingStructureGenerator {
    private static final int GRID = 12 * 16;
    private static final int MAX_RADIUS = 96;

    private static final List<FixedSite> FIXED_SITES = List.of(
            site(GOTWaypoint.CRASTERS_KEEP, WildlingSettlementKind.CRASTER, 32),
            site(GOTWaypoint.HARDHOME, WildlingSettlementKind.HARDHOME, 94)
    );

    private PlanetosWildlingStructureGenerator() {}

    public static void generate(WorldGenLevel level, ChunkAccess chunk, long seed) {
        int minX = chunk.getPos().getMinBlockX();
        int minZ = chunk.getPos().getMinBlockZ();
        int maxX = minX + 15;
        int maxZ = minZ + 15;
        PlanetosTerrainSampler terrain = new PlanetosTerrainSampler(seed);

        for (FixedSite site : FIXED_SITES) {
            if (MajorSchematicStructureGenerator.replaces(site.waypoint())) continue;
            if (!intersects(site.x(), site.z(), site.radius(), minX, minZ, maxX, maxZ)) continue;
            int y = terrain.surfaceHeight(site.x(), site.z()) + 1;
            List<NorthStructureMarker> markers = new ArrayList<>();
            NorthStructureBuilder builder = builder(level, site.x(), y, site.z(),
                    site.rotation(), seedFor(seed, site.x(), site.z()),
                    minX, minZ, maxX, maxZ, markers);
            if (site.kind() == null) WildlingStructureTemplates.whitetree(builder);
            else WildlingSettlementGenerator.generate(builder, site.kind());
            GOTWildlingNpcPopulation.spawnMarkers(level, markers);
            GOTNightWatchNpcPopulation.spawnMarkers(level, markers);
        }

        // Disabled: only structures present in the authoritative schematic ZIP may generate.
        int nightKingX = GOTWaypoint.NIGHT_KING.getCoordX();
        int nightKingZ = GOTWaypoint.NIGHT_KING.getCoordZ();
        if (nightKingX >= minX && nightKingX <= maxX && nightKingZ >= minZ && nightKingZ <= maxZ) {
            int y = terrain.surfaceHeight(nightKingX, nightKingZ) + 1;
            GOTWhiteWalkerNpcPopulation.spawnMarkers(level, List.of(new NorthStructureMarker(
                    "night_king", new BlockPos(nightKingX, y, nightKingZ), GOTWaypoint.NIGHT_KING.getRotation())));
        }
    }

    public static boolean spawn(ServerLevel level, BlockPos origin,
                                WildlingStructureType type, int rotation) {
        int radius = type.radius() + 8;
        long seed = seedFor(level.getSeed(), origin.getX(), origin.getZ()) ^ type.legacyId();
        List<NorthStructureMarker> markers = new ArrayList<>();
        NorthStructureBuilder builder = builder(level, origin.getX(), origin.getY(), origin.getZ(),
                rotation, seed, origin.getX() - radius, origin.getZ() - radius,
                origin.getX() + radius, origin.getZ() + radius, markers);
        if (type == WildlingStructureType.SETTLEMENT) {
            WildlingSettlementGenerator.generate(builder, WildlingSettlementKind.DEFAULT);
        } else if (type == WildlingStructureType.THENN_SETTLEMENT) {
            WildlingSettlementGenerator.generate(builder, WildlingSettlementKind.THENN);
        } else {
            WildlingStructureTemplates.generate(builder, type);
        }
        GOTWildlingNpcPopulation.spawnMarkers(level, markers);
        GOTNightWatchNpcPopulation.spawnMarkers(level, markers);
        GOTMod.LOGGER.info("Spawned {} at {}, {}, {} with {} future population markers",
                type.serializedName(), origin.getX(), origin.getY(), origin.getZ(), markers.size());
        return true;
    }

    public static int fixedSiteCount() { return FIXED_SITES.size(); }

    private static void generateRandomSettlements(WorldGenLevel level, PlanetosTerrainSampler terrain,
                                                  long seed, int minX, int minZ, int maxX, int maxZ) {
        int minGridX = Math.floorDiv(minX - MAX_RADIUS, GRID);
        int maxGridX = Math.floorDiv(maxX + MAX_RADIUS, GRID);
        int minGridZ = Math.floorDiv(minZ - MAX_RADIUS, GRID);
        int maxGridZ = Math.floorDiv(maxZ + MAX_RADIUS, GRID);
        for (int gridZ = minGridZ; gridZ <= maxGridZ; gridZ++) {
            for (int gridX = minGridX; gridX <= maxGridX; gridX++) {
                long cellSeed = seedFor(seed ^ 0x57494c444c494e47L, gridX, gridZ);
                int x = gridX * GRID + GRID / 2 + ((int)Math.floorMod(cellSeed, 3L) - 1) * 16;
                int z = gridZ * GRID + GRID / 2 + ((int)Math.floorMod(cellSeed >>> 9, 3L) - 1) * 16;
                GOTBiomeMetadata metadata = PlanetosBiomeManager.getMetadata(x, z);
                if (metadata == null || !isSettlementBiome(metadata.id()) || !metadata.allowsSettlements()) continue;
                WildlingSettlementKind kind = metadata.id().equals("thenn_land")
                        ? WildlingSettlementKind.THENN : WildlingSettlementKind.DEFAULT;
                if (!intersects(x, z, 82, minX, minZ, maxX, maxZ)
                        || !validSite(terrain, x, z, metadata.id())) continue;
                int y = terrain.surfaceHeight(x, z) + 1;
                List<NorthStructureMarker> markers = new ArrayList<>();
                NorthStructureBuilder builder = builder(level, x, y, z,
                        (int)Math.floorMod(cellSeed >>> 17, 4L), cellSeed,
                        minX, minZ, maxX, maxZ, markers);
                WildlingSettlementGenerator.generate(builder, kind);
                GOTWildlingNpcPopulation.spawnMarkers(level, markers);
            }
        }
    }

    private static boolean validSite(PlanetosTerrainSampler terrain, int x, int z, String biome) {
        if (nearFixedSite(x, z, 180)
                || GOTBeziers.isRoadNear(x, z, 96) > 0.0F
                || GOTBeziers.isWallNear(x, z, 96) > 0.0F
                || GOTBeziers.isLinkerNear(x, z, 96) > 0.0F) return false;
        int center = terrain.surfaceHeight(x, z);
        int min = center;
        int max = center;
        for (int[] offset : new int[][]{{36,0},{-36,0},{0,36},{0,-36}}) {
            GOTBiomeMetadata metadata = PlanetosBiomeManager.getMetadata(x + offset[0], z + offset[1]);
            if (metadata == null || (biome.equals("thenn_land")
                    ? !metadata.id().equals("thenn_land") : !isDefaultBiome(metadata.id()))) return false;
            int height = terrain.surfaceHeight(x + offset[0], z + offset[1]);
            min = Math.min(min, height);
            max = Math.max(max, height);
        }
        return max - min <= 12;
    }

    private static NorthStructureBuilder builder(WorldGenLevel level, int x, int y, int z,
                                                 int rotation, long seed,
                                                 int minX, int minZ, int maxX, int maxZ,
                                                 List<NorthStructureMarker> markers) {
        return new NorthStructureBuilder(level, x, y, z, rotation, seed,
                NorthStructurePalette.gift(false), minX, minZ, maxX, maxZ, markers);
    }

    private static boolean isSettlementBiome(String id) {
        return isDefaultBiome(id) || id.equals("thenn_land");
    }

    private static boolean isDefaultBiome(String id) {
        return id.equals("frozen_shore") || id.equals("haunted_forest");
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

    private static boolean intersects(int x, int z, int radius,
                                      int minX, int minZ, int maxX, int maxZ) {
        return x + radius >= minX && x - radius <= maxX
                && z + radius >= minZ && z - radius <= maxZ;
    }

    private static FixedSite site(GOTWaypoint waypoint, WildlingSettlementKind kind, int radius) {
        return new FixedSite(waypoint, waypoint.getCoordX(), waypoint.getCoordZ(),
                waypoint.getRotation(), radius, kind);
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
                             int radius, WildlingSettlementKind kind) {}
}
