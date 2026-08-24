package got.world.structure.north;

import got.GOTMod;
import got.common.world.map.GOTBeziers;
import got.common.world.map.GOTWaypoint;
import got.npc.GOTNorthNpcPopulation;
import got.npc.GOTWhiteWalkerNpcPopulation;
import got.world.biome.GOTBiomeMetadata;
import got.world.biome.PlanetosBiomeManager;
import got.world.structure.major.MajorSchematicStructureGenerator;
import got.world.terrain.PlanetosTerrainSampler;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkAccess;

import java.util.ArrayList;
import java.util.List;

/**
 * North structure placement pass: authored waypoint sites, legacy settlement
 * grid, random watchforts and North Barrows burial mounds.
 */
public final class PlanetosNorthStructureGenerator {
    private static final int GRID = 12 * 16;
    private static final int MAX_RADIUS = 104;
    /**
     * The legacy mod made one quarter of ordinary North settlements forts.
     * Planetos is large enough that this produced a castle every few hundred
     * blocks. Fixed lore castles remain guaranteed below; this roll applies
     * only to otherwise-random settlements.
     */
    private static final long RANDOM_CASTLE_ONE_IN = 4096L;
    private static final int FIXED_SITE_CLEARANCE = 48;

    private static final List<FixedSite> FIXED_SITES = List.of(
            fort(GOTWaypoint.CATFISH_ROCK, 0, 0, 0),
            fort(GOTWaypoint.GOLDGRASS, 0, 51, 2),
            fort(GOTWaypoint.BLACK_POOL, 0, 0, 0),
            fort(GOTWaypoint.DEEPWOOD_MOTTE, 0, 0, 0),
            fort(GOTWaypoint.FLINTS_FINGER, 0, 0, 0),
            fort(GOTWaypoint.HIGHPOINT, 0, 0, 0),
            fort(GOTWaypoint.WIDOWS_WATCH, 0, 0, 0),
            fort(GOTWaypoint.HORNWOOD, 0, 0, 0),
            fort(GOTWaypoint.IRONRATH, 0, 0, 0),
            fort(GOTWaypoint.MOAT_KAILIN, 0, 0, 0),
            fort(GOTWaypoint.OLDCASTLE, 0, 0, 0),
            fort(GOTWaypoint.RAMSGATE, 0, 0, 0),
            fort(GOTWaypoint.RILLWATER_CROSSING, -51, 0, 3),
            site(GOTWaypoint.DREADFORT, NorthSettlementKind.FORT, 0, 0, 0,
                    "ramsay_bolton", "roose_bolton"),
            site(GOTWaypoint.KARHOLD, NorthSettlementKind.FORT, 45, 0, 1,
                    "rickard_karstark"),
            site(GOTWaypoint.LAST_HEARTH, NorthSettlementKind.FORT, 0, 0, 0,
                    "john_umber"),
            site(GOTWaypoint.MORMONTS_KEEP, NorthSettlementKind.FORT, 0, 0, 0,
                    "maege_mormont"),
            site(GOTWaypoint.RYSWELLS_CASTLE, NorthSettlementKind.FORT, -58, 6, 3,
                    "rodrik_ryswel"),
            site(GOTWaypoint.CASTLE_CERWYN, NorthSettlementKind.FORT, -51, 0, 3,
                    "cley_cerwyn"),
            site(GOTWaypoint.TORRHENS_SQUARE, NorthSettlementKind.FORT, -45, 0, 3,
                    "helman_tallhart"),
            site(GOTWaypoint.WINTERFELL, NorthSettlementKind.FORT, -58, -6, 3,
                    "robb_stark", "hodor", "arya_stark", "bran_stark", "rickon_stark", "maester_luwin", "osha")
    );

    private PlanetosNorthStructureGenerator() {}

    public static void generate(WorldGenLevel level, ChunkAccess chunk, long seed) {
        PlanetosTerrainSampler terrain = new PlanetosTerrainSampler(seed);
        int minX = chunk.getPos().getMinBlockX();
        int minZ = chunk.getPos().getMinBlockZ();
        int maxX = minX + 15;
        int maxZ = minZ + 15;

        for (FixedSite site : FIXED_SITES) {
            if (MajorSchematicStructureGenerator.replaces(site.waypoint())) continue;
            if (intersects(site.x(), site.z(), site.radius(), minX, minZ, maxX, maxZ)) {
                generateSettlement(level, terrain, site.x(), site.z(), site.rotation(),
                        seedFor(seed, site.x(), site.z()), minX, minZ, maxX, maxZ,
                        site.kind(), site.waypoint().getCodeName().toLowerCase(), site.legendaryRoles());
            }
        }

        // Disabled: legacy random settlements are not in the authoritative schematic ZIP.
        // Disabled: legacy random structure features are not in the authoritative schematic ZIP.
    }

    public static boolean spawn(ServerLevel level, net.minecraft.core.BlockPos origin,
                                NorthStructureType type, int rotation) {
        int radius = type.radius() + 8;
        long seed = seedFor(level.getSeed(), origin.getX(), origin.getZ()) ^ type.legacyId();
        NorthStructureBuilder builder = new NorthStructureBuilder(level, origin.getX(), origin.getY(),
                origin.getZ(), rotation, seed, NorthStructurePalette.create(seed),
                origin.getX() - radius, origin.getZ() - radius,
                origin.getX() + radius, origin.getZ() + radius, new ArrayList<>());
        if (type.settlement()) {
            NorthSettlementGenerator.generate(builder, switch (type) {
                case HILLMAN_SETTLEMENT -> NorthSettlementKind.HILLMAN;
                case FORT_SETTLEMENT -> NorthSettlementKind.FORT;
                case TOWN -> NorthSettlementKind.TOWN;
                default -> NorthSettlementKind.VILLAGE;
            }, "player_spawned", List.of());
        } else {
            NorthStructureTemplates.generate(builder, type);
        }
        int population = GOTNorthNpcPopulation.spawnMarkers(level, builder.markers());
        GOTWhiteWalkerNpcPopulation.spawnMarkers(level, builder.markers());
        GOTMod.LOGGER.info("Spawned {} at {}, {}, {} with {} North NPCs",
                type.serializedName(), origin.getX(), origin.getY(), origin.getZ(), population);
        return true;
    }

    public static int fixedSiteCount() { return FIXED_SITES.size(); }

    private static void generateGridSettlements(WorldGenLevel level, PlanetosTerrainSampler terrain,
                                                long seed, int minX, int minZ, int maxX, int maxZ) {
        int minGridX = Math.floorDiv(minX - MAX_RADIUS, GRID);
        int maxGridX = Math.floorDiv(maxX + MAX_RADIUS, GRID);
        int minGridZ = Math.floorDiv(minZ - MAX_RADIUS, GRID);
        int maxGridZ = Math.floorDiv(maxZ + MAX_RADIUS, GRID);
        for (int gridZ = minGridZ; gridZ <= maxGridZ; gridZ++) {
            for (int gridX = minGridX; gridX <= maxGridX; gridX++) {
                long cellSeed = seedFor(seed ^ 0x534554544c454d54L, gridX, gridZ);
                int anchorX = gridX * GRID + GRID / 2 + ((int)Math.floorMod(cellSeed, 3L) - 1) * 16;
                int anchorZ = gridZ * GRID + GRID / 2 + ((int)Math.floorMod(cellSeed >>> 9, 3L) - 1) * 16;
                GOTBiomeMetadata metadata = PlanetosBiomeManager.getMetadata(anchorX, anchorZ);
                if (metadata == null || !isNorth(metadata.id()) || !metadata.allowsSettlements()) continue;
                NorthSettlementKind kind = (metadata.id().equals("north_wild") || metadata.id().equals("skagos"))
                        ? NorthSettlementKind.HILLMAN
                        : (isRareRandomCastle(cellSeed)
                        ? NorthSettlementKind.FORT : NorthSettlementKind.VILLAGE);
                int radius = kind == NorthSettlementKind.HILLMAN ? 80 : 76;
                if (!intersects(anchorX, anchorZ, radius, minX, minZ, maxX, maxZ)) continue;
                if (!validRandomSite(terrain, anchorX, anchorZ, radius, true)) continue;
                generateSettlement(level, terrain, anchorX, anchorZ,
                        (int)Math.floorMod(cellSeed >>> 23, 4L), cellSeed,
                        minX, minZ, maxX, maxZ, kind, "random_north", List.of());
            }
        }
    }

    private static void generateRandomFeatures(WorldGenLevel level, PlanetosTerrainSampler terrain,
                                               long seed, int minX, int minZ, int maxX, int maxZ) {
        int centerChunkX = Math.floorDiv(minX, 16);
        int centerChunkZ = Math.floorDiv(minZ, 16);
        for (int dz = -2; dz <= 2; dz++) {
            for (int dx = -2; dx <= 2; dx++) {
                int chunkX = centerChunkX + dx;
                int chunkZ = centerChunkZ + dz;
                long chunkSeed = seedFor(seed ^ 0x5741544348464f52L, chunkX, chunkZ);
                int anchorX = chunkX * 16 + 8;
                int anchorZ = chunkZ * 16 + 8;
                GOTBiomeMetadata metadata = PlanetosBiomeManager.getMetadata(anchorX, anchorZ);
                if (metadata == null) continue;

                if (metadata.id().equals("north_barrows") && Math.floorMod(chunkSeed, 20L) == 0L
                        && intersects(anchorX, anchorZ, 9, minX, minZ, maxX, maxZ)
                        && validRandomSite(terrain, anchorX, anchorZ, 9, false)) {
                    generateTemplate(level, terrain, anchorX, anchorZ,
                            (int)Math.floorMod(chunkSeed >>> 7, 4L), chunkSeed,
                            minX, minZ, maxX, maxZ, NorthStructureType.HOUSE_SMALL, true);
                } else if (isNorth(metadata.id()) && Math.floorMod(chunkSeed, 800L) == 0L
                        && intersects(anchorX, anchorZ, 18, minX, minZ, maxX, maxZ)
                        && validRandomSite(terrain, anchorX, anchorZ, 18, true)) {
                    generateTemplate(level, terrain, anchorX, anchorZ,
                            (int)Math.floorMod(chunkSeed >>> 7, 4L), chunkSeed,
                            minX, minZ, maxX, maxZ, NorthStructureType.WATCHFORT, false);
                }
            }
        }
    }

    private static void generateTemplate(WorldGenLevel level, PlanetosTerrainSampler terrain,
                                         int x, int z, int rotation, long structureSeed,
                                         int minX, int minZ, int maxX, int maxZ,
                                         NorthStructureType type, boolean barrow) {
        int y = terrain.surfaceHeight(x, z) + 1;
        NorthStructureBuilder builder = new NorthStructureBuilder(level, x, y, z, rotation,
                structureSeed, NorthStructurePalette.create(structureSeed),
                minX, minZ, maxX, maxZ, new ArrayList<>());
        if (barrow) NorthStructureTemplates.barrow(builder);
        else NorthStructureTemplates.generate(builder, type);
        GOTNorthNpcPopulation.spawnMarkers(level, builder.markers());
        GOTWhiteWalkerNpcPopulation.spawnMarkers(level, builder.markers());
    }

    private static void generateSettlement(WorldGenLevel level, PlanetosTerrainSampler terrain,
                                           int x, int z, int rotation, long structureSeed,
                                           int minX, int minZ, int maxX, int maxZ,
                                           NorthSettlementKind kind, String siteName,
                                           List<String> legendaryRoles) {
        int y = terrain.surfaceHeight(x, z) + 1;
        NorthStructureBuilder builder = new NorthStructureBuilder(level, x, y, z, rotation,
                structureSeed, NorthStructurePalette.create(structureSeed),
                minX, minZ, maxX, maxZ, new ArrayList<>());
        NorthSettlementGenerator.generate(builder, kind, siteName, legendaryRoles);
        GOTNorthNpcPopulation.spawnMarkers(level, builder.markers());
    }

    private static boolean validRandomSite(PlanetosTerrainSampler terrain, int x, int z,
                                           int radius, boolean avoidRoads) {
        if (nearFixedSite(x, z, radius, FIXED_SITE_CLEARANCE)) return false;
        if (avoidRoads && (GOTBeziers.isRoadNear(x, z, radius + 24) > 0.0F
                || GOTBeziers.isWallNear(x, z, radius + 24) > 0.0F
                || GOTBeziers.isLinkerNear(x, z, radius + 24) > 0.0F)) return false;
        int center = terrain.surfaceHeight(x, z);
        int sample = Math.max(8, radius / 2);
        int min = center;
        int max = center;
        for (int[] offset : new int[][]{{sample,0},{-sample,0},{0,sample},{0,-sample}}) {
            int height = terrain.surfaceHeight(x + offset[0], z + offset[1]);
            min = Math.min(min, height);
            max = Math.max(max, height);
            GOTBiomeMetadata metadata = PlanetosBiomeManager.getMetadata(x + offset[0], z + offset[1]);
            if (metadata == null || !isNorth(metadata.id())) return false;
        }
        return max - min <= (radius >= 50 ? 12 : 8);
    }

    private static boolean nearFixedSite(int x, int z, int randomRadius, int clearance) {
        for (FixedSite site : FIXED_SITES) {
            long exclusionRadius = (long)randomRadius + site.radius() + clearance;
            long radiusSq = exclusionRadius * exclusionRadius;
            long dx = x - site.x();
            long dz = z - site.z();
            if (dx * dx + dz * dz <= radiusSq) return true;
        }
        return false;
    }

    private static boolean isRareRandomCastle(long cellSeed) {
        return Math.floorMod(cellSeed >>> 17, RANDOM_CASTLE_ONE_IN) == 0L;
    }

    private static boolean isNorth(String biome) {
        return biome.equals("skagos") || biome.equals("north") || biome.startsWith("north_");
    }

    private static boolean intersects(int x, int z, int radius,
                                      int minX, int minZ, int maxX, int maxZ) {
        return x + radius >= minX && x - radius <= maxX
                && z + radius >= minZ && z - radius <= maxZ;
    }

    private static FixedSite fort(GOTWaypoint waypoint, int dx, int dz, int rotation) {
        return site(waypoint, NorthSettlementKind.FORT, dx, dz, rotation);
    }

    private static FixedSite hillman(GOTWaypoint waypoint) {
        return site(waypoint, NorthSettlementKind.HILLMAN, 0, 0, 0);
    }

    private static FixedSite site(GOTWaypoint waypoint, NorthSettlementKind kind,
                                  int dx, int dz, int rotation, String... legendaryRoles) {
        int radius = switch (kind) {
            case TOWN -> 104;
            case SMALL_TOWN -> 82;
            case VILLAGE, FORT -> 76;
            case HILLMAN -> 80;
        };
        return new FixedSite(waypoint, waypoint.getCoordX() + dx, waypoint.getCoordZ() + dz,
                rotation, radius, kind, List.of(legendaryRoles));
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
                             int radius, NorthSettlementKind kind,
                             List<String> legendaryRoles) {}
}
