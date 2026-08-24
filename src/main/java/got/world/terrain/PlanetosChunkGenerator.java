package got.world.terrain;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import got.world.biome.GOTBiomeMetadata;
import got.world.biome.GOTLegacyTerrainCatalog;
import got.world.biome.PlanetosBiomeManager;
import got.world.flora.PlanetosBiomeDecorator;
import got.world.resource.PlanetosResourceGenerator;
import got.world.road.PlanetosRoadGenerator;
import got.npc.GOTWesterlandsNpcPopulation;
import got.npc.GOTRiverlandsNpcPopulation;
import got.npc.GOTArrynNpcPopulation;
import got.npc.GOTCrownlandsNpcPopulation;
import got.npc.GOTDragonstoneNpcPopulation;
import got.npc.GOTReachNpcPopulation;
import got.npc.GOTStormlandsNpcPopulation;
import got.npc.GOTDorneNpcPopulation;
import got.npc.GOTIronbornNpcPopulation;
import got.npc.GOTFreeCitiesNpcPopulation;
import got.world.structure.major.MajorSchematicStructureGenerator;
import got.world.structure.north.PlanetosNorthStructureGenerator;
import got.world.structure.nightwatch.PlanetosNightWatchStructureGenerator;
import got.world.structure.wildling.PlanetosWildlingStructureGenerator;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.NoiseColumn;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.blending.Blender;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

/**
 * Planetos terrain carrier. Unlike the previous minecraft:overworld noise
 * generator, this class actually consumes the atlas biome metadata and legacy
 * height catalogue while retaining modern -64..319 build height.
 */
public final class PlanetosChunkGenerator extends ChunkGenerator {
    public static final int MIN_Y = -64;
    public static final int GEN_DEPTH = 384;

    public static final Codec<PlanetosChunkGenerator> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            BiomeSource.CODEC.fieldOf("biome_source").forGetter(generator -> generator.biomeSource),
            Codec.LONG.optionalFieldOf("seed", 0L).forGetter(generator -> generator.seed)
    ).apply(instance, PlanetosChunkGenerator::new));

    private final long seed;
    private final PlanetosTerrainSampler sampler;

    public PlanetosChunkGenerator(BiomeSource biomeSource, long seed) {
        super(biomeSource);
        this.seed = seed;
        this.sampler = new PlanetosTerrainSampler(seed);
        PlanetosBiomeManager.initialize(seed);
    }

    @Override
    protected Codec<? extends ChunkGenerator> codec() {
        return CODEC;
    }

    @Override
    public CompletableFuture<ChunkAccess> fillFromNoise(Executor executor, Blender blender,
                                                         RandomState randomState,
                                                         StructureManager structureManager,
                                                         ChunkAccess chunk) {
        return CompletableFuture.supplyAsync(() -> {
            fillChunk(chunk);
            return chunk;
        }, executor);
    }

    private void fillChunk(ChunkAccess chunk) {
        int minX = chunk.getPos().getMinBlockX();
        int minZ = chunk.getPos().getMinBlockZ();
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
        Heightmap oceanFloor = chunk.getOrCreateHeightmapUnprimed(Heightmap.Types.OCEAN_FLOOR_WG);
        Heightmap worldSurface = chunk.getOrCreateHeightmapUnprimed(Heightmap.Types.WORLD_SURFACE_WG);
        int[][] heights = new int[18][18];

        for (int localZ = -1; localZ <= 16; localZ++) {
            for (int localX = -1; localX <= 16; localX++) {
                heights[localX + 1][localZ + 1] = sampler.surfaceHeight(minX + localX, minZ + localZ);
            }
        }

        for (int localZ = 0; localZ < 16; localZ++) {
            for (int localX = 0; localX < 16; localX++) {
                int blockX = minX + localX;
                int blockZ = minZ + localZ;
                int surfaceY = heights[localX + 1][localZ + 1];
                int slope = maximumSlope(heights, localX + 1, localZ + 1);
                GOTBiomeMetadata metadata = PlanetosBiomeManager.getMetadata(blockX, blockZ);
                double detail = sampler.surfaceDetail(blockX, blockZ);
                BlockState top = PlanetosSurfaceResolver.top(metadata, blockX, blockZ, surfaceY,
                        PlanetosTerrainSampler.SEA_LEVEL, slope, detail);
                BlockState filler = PlanetosSurfaceResolver.filler(metadata, blockX, blockZ, detail);
                int fillerDepth = 3 + (int)Math.floorMod(hash(blockX, blockZ), 3L);

                for (int y = MIN_Y; y < MIN_Y + GEN_DEPTH; y++) {
                    BlockState state = stateAt(blockX, y, blockZ, surfaceY, fillerDepth, metadata, top, filler);
                    if (state.isAir()) continue;
                    pos.set(blockX, y, blockZ);
                    chunk.setBlockState(pos, state, false);
                    if (!state.getFluidState().isEmpty()) chunk.markPosForPostprocessing(pos);
                    oceanFloor.update(localX, y, localZ, state);
                    worldSurface.update(localX, y, localZ, state);
                }
            }
        }
    }

    private BlockState stateAt(int x, int y, int z, int surfaceY, int fillerDepth,
                               GOTBiomeMetadata metadata, BlockState top, BlockState filler) {
        if (y <= MIN_Y + bedrockDepth(x, z)) return Blocks.BEDROCK.defaultBlockState();
        if (y <= surfaceY) {
            if (sampler.isCave(x, y, z, surfaceY)) {
                return y <= -54 ? Blocks.LAVA.defaultBlockState() : Blocks.AIR.defaultBlockState();
            }
            if (y == surfaceY) return top;
            if (y >= surfaceY - fillerDepth) return filler;
            return y < 0 ? Blocks.DEEPSLATE.defaultBlockState() : Blocks.STONE.defaultBlockState();
        }
        // Sea-level flooding follows the authored Planetos biome map. The
        // previous unconditional fill turned any low noise depression in a
        // land biome into a false lake with a grass floor.
        if (y <= PlanetosTerrainSampler.SEA_LEVEL
                && (isAquatic(metadata) || PlanetosLandmarkTerrain.isSeaCityWater(x, z))) {
            if (y == PlanetosTerrainSampler.SEA_LEVEL && metadata != null && metadata.temperature() < 0.15F) {
                return Blocks.ICE.defaultBlockState();
            }
            return Blocks.WATER.defaultBlockState();
        }
        return Blocks.AIR.defaultBlockState();
    }

    private static boolean isAquatic(GOTBiomeMetadata metadata) {
        return metadata != null && GOTLegacyTerrainCatalog.isAquatic(metadata.id());
    }

    private static int maximumSlope(int[][] heights, int x, int z) {
        int center = heights[x][z];
        return Math.max(Math.max(Math.abs(center - heights[x - 1][z]), Math.abs(center - heights[x + 1][z])),
                Math.max(Math.abs(center - heights[x][z - 1]), Math.abs(center - heights[x][z + 1])));
    }

    private int bedrockDepth(int x, int z) {
        return (int)Math.floorMod(hash(x, z), 5L);
    }

    private long hash(long x, long z) {
        long h = seed ^ x * 341873128712L ^ z * 132897987541L;
        h ^= h >>> 33;
        h *= 0xff51afd7ed558ccdL;
        h ^= h >>> 33;
        return h;
    }

    @Override
    public void buildSurface(WorldGenRegion region, StructureManager structureManager,
                             RandomState randomState, ChunkAccess chunk) {
        // Surface blocks are integrated into fillFromNoise to preserve legacy transitions.
    }

    @Override
    public void applyCarvers(WorldGenRegion region, long seed, RandomState randomState,
                             BiomeManager biomeManager, StructureManager structureManager,
                             ChunkAccess chunk, GenerationStep.Carving carving) {
        // Sparse legacy-style caves are integrated into fillFromNoise.
    }

    @Override
    public void applyBiomeDecoration(WorldGenLevel level, ChunkAccess chunk,
                                     StructureManager structureManager) {
        super.applyBiomeDecoration(level, chunk, structureManager);
        PlanetosResourceGenerator.generate(level, chunk, seed);
        PlanetosBiomeDecorator.decorate(level, chunk, seed);
        PlanetosRoadGenerator.generate(level, chunk, seed);
        // Structures run last so authored buildings clear foliage and merge
        // their local paths into the already projected road network.
        PlanetosNorthStructureGenerator.generate(level, chunk, seed);
        PlanetosNightWatchStructureGenerator.generate(level, chunk, seed);
        PlanetosWildlingStructureGenerator.generate(level, chunk, seed);
        // Authored major locations run last so their non-air blocks and
        // block-entity data replace any regional placeholder at the waypoint.
        MajorSchematicStructureGenerator.generate(level, chunk, seed);
        // Fixed Westerlands characters use the same post-worldgen deferred
        // population boundary as Winterfell, including sites whose replacement
        // schematics have not been supplied yet.
        GOTWesterlandsNpcPopulation.queueFixedSites(level, chunk, seed);
        GOTRiverlandsNpcPopulation.queueFixedSites(level, chunk, seed);
        GOTArrynNpcPopulation.queueFixedSites(level, chunk, seed);
        GOTCrownlandsNpcPopulation.queueFixedSites(level, chunk, seed);
        GOTDragonstoneNpcPopulation.queueFixedSites(level, chunk, seed);
        GOTReachNpcPopulation.queueFixedSites(level, chunk, seed);
        GOTStormlandsNpcPopulation.queueFixedSites(level, chunk, seed);
        GOTDorneNpcPopulation.queueFixedSites(level, chunk, seed);
        GOTIronbornNpcPopulation.queueFixedSites(level, chunk, seed);
        GOTFreeCitiesNpcPopulation.queueFixedSites(level, chunk, seed);
    }

    @Override
    public void spawnOriginalMobs(WorldGenRegion region) {
        // Mob lists are currently empty in the port's biome JSON and will be restored separately.
    }

    @Override
    public int getGenDepth() {
        return GEN_DEPTH;
    }

    @Override
    public int getSeaLevel() {
        return PlanetosTerrainSampler.SEA_LEVEL;
    }

    @Override
    public int getMinY() {
        return MIN_Y;
    }

    @Override
    public int getBaseHeight(int x, int z, Heightmap.Types type,
                             LevelHeightAccessor level, RandomState randomState) {
        int terrain = sampler.surfaceHeight(x, z) + 1;
        if (type == Heightmap.Types.WORLD_SURFACE || type == Heightmap.Types.WORLD_SURFACE_WG) {
            return Math.max(terrain, PlanetosTerrainSampler.SEA_LEVEL + 1);
        }
        return terrain;
    }

    @Override
    public NoiseColumn getBaseColumn(int x, int z, LevelHeightAccessor level, RandomState randomState) {
        int surfaceY = sampler.surfaceHeight(x, z);
        GOTBiomeMetadata metadata = PlanetosBiomeManager.getMetadata(x, z);
        double detail = sampler.surfaceDetail(x, z);
        BlockState top = PlanetosSurfaceResolver.top(metadata, x, z, surfaceY,
                PlanetosTerrainSampler.SEA_LEVEL, 0, detail);
        BlockState filler = PlanetosSurfaceResolver.filler(metadata, x, z, detail);
        BlockState[] states = new BlockState[GEN_DEPTH];
        for (int i = 0; i < states.length; i++) {
            states[i] = stateAt(x, MIN_Y + i, z, surfaceY, 4, metadata, top, filler);
        }
        return new NoiseColumn(MIN_Y, states);
    }

    @Override
    public void addDebugScreenInfo(List<String> lines, RandomState randomState, BlockPos pos) {
        GOTBiomeMetadata metadata = PlanetosBiomeManager.getMetadata(pos.getX(), pos.getZ());
        lines.add("Planetos terrain: " + (metadata == null ? "unknown" : metadata.id())
                + " surface=" + sampler.surfaceHeight(pos.getX(), pos.getZ()));
    }
}
