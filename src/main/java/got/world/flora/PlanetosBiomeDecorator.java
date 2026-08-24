package got.world.flora;

import got.GOTBlocks;
import got.GOTDecorativeFunctionalBlocks;
import got.world.biome.GOTBiomeMetadata;
import got.world.biome.GOTLegacyTerrainCatalog;
import got.world.biome.PlanetosBiomeManager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SweetBerryBushBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.core.Direction.Axis;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.Heightmap;

import java.util.List;

/** Biome decoration pass for Planetos trees, ground plants, and natural clutter. */
public final class PlanetosBiomeDecorator {
    private static final int UPDATE_FLAGS = Block.UPDATE_CLIENTS | Block.UPDATE_KNOWN_SHAPE;

    private PlanetosBiomeDecorator() {}

    public static void decorate(WorldGenLevel level, ChunkAccess chunk, long generatorSeed) {
        int minX = chunk.getPos().getMinBlockX();
        int minZ = chunk.getPos().getMinBlockZ();
        long seed = mix(generatorSeed, chunk.getPos().x, chunk.getPos().z);
        RandomSource random = RandomSource.create(seed);
        GOTBiomeMetadata center = PlanetosBiomeManager.getMetadata(minX + 8, minZ + 8);
        GOTBiomeFloraProfile profile = GOTBiomeFloraCatalog.forBiome(center);

        if (center != null && GOTLegacyTerrainCatalog.isAquatic(center.id())) {
            aquaticPlants(level, random, minX, minZ, profile.aquaticPlantsPerChunk());
            return;
        }

        trees(level, random, minX, minZ, profile);
        fallenLogs(level, random, minX, minZ, profile);
        boulders(level, random, minX, minZ, profile.bouldersPerChunk());
        berryBushes(level, random, minX, minZ, profile.berryBushesPerChunk());
        groundPlants(level, random, minX, minZ, profile);
        snowCover(level, random, minX, minZ, profile.snowAttemptsPerChunk());
    }

    private static void trees(WorldGenLevel level, RandomSource random, int minX, int minZ,
                              GOTBiomeFloraProfile profile) {
        int attempts = fractionalAttempts(random, profile.treesPerChunk());
        for (int attempt = 0; attempt < attempts; attempt++) {
            int x = minX + 3 + random.nextInt(10);
            int z = minZ + 3 + random.nextInt(10);
            GOTBiomeMetadata local = PlanetosBiomeManager.getMetadata(x, z);
            if (local == null || GOTLegacyTerrainCatalog.isAquatic(local.id())) continue;
            GOTBiomeFloraProfile localProfile = GOTBiomeFloraCatalog.forBiome(local);
            BlockPos base = surface(level, x, z);
            GOTTreeSpecies species = localProfile.chooseTree(random);
            if (GOTTreeGenerator.generate(level, base, random, species) && random.nextBoolean()) {
                fallenLeaves(level, base, random, species);
            }
        }
    }

    private static void fallenLeaves(WorldGenLevel level, BlockPos treeBase, RandomSource random,
                                     GOTTreeSpecies species) {
        var registered = GOTDecorativeFunctionalBlocks.ALL.get(species.fallenLeavesId());
        if (registered == null) return;
        BlockState leaves = registered.get().defaultBlockState();
        int attempts = 3 + random.nextInt(6);
        for (int attempt = 0; attempt < attempts; attempt++) {
            int x = treeBase.getX() + random.nextInt(9) - 4;
            int z = treeBase.getZ() + random.nextInt(9) - 4;
            BlockPos pos = surface(level, x, z);
            if (level.getBlockState(pos).isAir() && leaves.canSurvive(level, pos)) {
                level.setBlock(pos, leaves, UPDATE_FLAGS);
            }
        }
    }

    private static void fallenLogs(WorldGenLevel level, RandomSource random, int minX, int minZ,
                                   GOTBiomeFloraProfile profile) {
        for (int attempt = 0; attempt < profile.fallenLogsPerChunk(); attempt++) {
            int x = minX + 2 + random.nextInt(12);
            int z = minZ + 2 + random.nextInt(12);
            BlockPos base = surface(level, x, z);
            if (!level.getBlockState(base).isAir()) continue;
            GOTTreeSpecies species = profile.chooseTree(random);
            Direction direction = Direction.Plane.HORIZONTAL.getRandomDirection(random);
            Axis axis = direction.getAxis();
            BlockState log = species.log().defaultBlockState();
            if (log.hasProperty(BlockStateProperties.AXIS)) log = log.setValue(RotatedPillarBlock.AXIS, axis);
            int length = 3 + random.nextInt(4);
            for (int distance = 0; distance < length; distance++) {
                BlockPos pos = base.relative(direction, distance);
                BlockState below = level.getBlockState(pos.below());
                if (!level.getBlockState(pos).isAir() || !below.isFaceSturdy(level, pos.below(), Direction.UP)) break;
                level.setBlock(pos, log, UPDATE_FLAGS);
            }
        }
    }

    private static void boulders(WorldGenLevel level, RandomSource random, int minX, int minZ, int attempts) {
        for (int attempt = 0; attempt < attempts; attempt++) {
            int x = minX + 2 + random.nextInt(12);
            int z = minZ + 2 + random.nextInt(12);
            BlockPos center = surface(level, x, z);
            if (!level.getBlockState(center).isAir()) continue;
            BlockState rock = switch (random.nextInt(4)) {
                case 0 -> Blocks.COBBLESTONE.defaultBlockState();
                case 1 -> Blocks.MOSSY_COBBLESTONE.defaultBlockState();
                case 2 -> Blocks.ANDESITE.defaultBlockState();
                default -> Blocks.STONE.defaultBlockState();
            };
            int radius = random.nextInt(4) == 0 ? 2 : 1;
            for (int dx = -radius; dx <= radius; dx++) {
                for (int dy = 0; dy <= radius; dy++) {
                    for (int dz = -radius; dz <= radius; dz++) {
                        if (dx * dx + dy * dy + dz * dz > radius * radius + 1) continue;
                        BlockPos pos = center.offset(dx, dy, dz);
                        if (level.getBlockState(pos).isAir() || level.getBlockState(pos).canBeReplaced()) {
                            level.setBlock(pos, rock, UPDATE_FLAGS);
                        }
                    }
                }
            }
        }
    }

    private static void berryBushes(WorldGenLevel level, RandomSource random, int minX, int minZ, int attempts) {
        List<Block> berries = List.of(GOTBlocks.BLACKBERRY_BUSH.get(), GOTBlocks.BLUEBERRY_BUSH.get(),
                GOTBlocks.CRANBERRY_BUSH.get(), GOTBlocks.ELDERBERRY_BUSH.get(),
                GOTBlocks.RASPBERRY_BUSH.get(), GOTBlocks.WILDBERRY_BUSH.get());
        for (int attempt = 0; attempt < attempts; attempt++) {
            BlockState state = berries.get(random.nextInt(berries.size())).defaultBlockState();
            if (state.hasProperty(SweetBerryBushBlock.AGE)) state = state.setValue(SweetBerryBushBlock.AGE, 2 + random.nextInt(2));
            placePlant(level, randomPosition(level, random, minX, minZ), state);
        }
    }

    private static void groundPlants(WorldGenLevel level, RandomSource random, int minX, int minZ,
                                     GOTBiomeFloraProfile profile) {
        for (int i = 0; i < profile.grassPerChunk(); i++) {
            placePlant(level, randomPosition(level, random, minX, minZ), grass(profile.plantTheme(), random));
        }
        for (int i = 0; i < profile.flowersPerChunk(); i++) {
            placePlant(level, randomPosition(level, random, minX, minZ), flower(profile.plantTheme(), random));
        }
        for (int i = 0; i < profile.deadBushesPerChunk(); i++) {
            placePlant(level, randomPosition(level, random, minX, minZ), Blocks.DEAD_BUSH.defaultBlockState());
        }
        for (int i = 0; i < profile.cactiPerChunk(); i++) {
            BlockPos pos = randomPosition(level, random, minX, minZ);
            if (!level.getBlockState(pos.below()).is(BlockTags.SAND) || !level.getBlockState(pos).isAir()) continue;
            int height = 1 + random.nextInt(3);
            for (int y = 0; y < height && Blocks.CACTUS.defaultBlockState().canSurvive(level, pos.above(y)); y++) {
                level.setBlock(pos.above(y), Blocks.CACTUS.defaultBlockState(), UPDATE_FLAGS);
            }
        }
        for (int i = 0; i < profile.reedsPerChunk(); i++) {
            BlockPos pos = randomPosition(level, random, minX, minZ);
            if (!nearWater(level, pos)) continue;
            BlockState reed = profile.plantTheme() == GOTBiomeFloraProfile.PlantTheme.ARID && random.nextBoolean()
                    ? GOTBlocks.DRIED_REEDS.get().defaultBlockState() : GOTBlocks.REEDS.get().defaultBlockState();
            placePlant(level, pos, reed);
        }
        if (profile.plantTheme() == GOTBiomeFloraProfile.PlantTheme.MARSH && random.nextInt(4) == 0) {
            placePlant(level, randomPosition(level, random, minX, minZ),
                    dynamicPlant("marsh_lights", Blocks.TORCH.defaultBlockState()));
        }
    }

    private static BlockState grass(GOTBiomeFloraProfile.PlantTheme theme, RandomSource random) {
        return switch (theme) {
            case ARID -> random.nextInt(4) == 0 ? GOTBlocks.PLANTAIN.get().defaultBlockState()
                    : GOTBlocks.ARID_GRASS.get().defaultBlockState();
            case ASSHAI, VALYRIA -> switch (random.nextInt(5)) {
                case 0 -> dynamicPlant("asshai_moss", GOTBlocks.ASSHAI_GRASS.get().defaultBlockState());
                case 1 -> dynamicPlant("asshai_thorn", GOTBlocks.ASSHAI_GRASS.get().defaultBlockState());
                default -> GOTBlocks.ASSHAI_GRASS.get().defaultBlockState();
            };
            case YI_TI -> GOTBlocks.QUENDITE_GRASS.get().defaultBlockState();
            case MARSH -> switch (random.nextInt(6)) {
                case 0 -> dynamicPlant("blackroot", Blocks.FERN.defaultBlockState());
                case 1, 2 -> Blocks.FERN.defaultBlockState();
                default -> dynamicPlant("tall_grass_nettles", Blocks.GRASS.defaultBlockState());
            };
            case TROPICAL -> random.nextInt(3) == 0 ? Blocks.FERN.defaultBlockState()
                    : dynamicPlant("tall_grass_jungle_sprout", Blocks.GRASS.defaultBlockState());
            case COLD -> random.nextBoolean() ? Blocks.FERN.defaultBlockState()
                    : dynamicPlant("tall_grass_short", Blocks.GRASS.defaultBlockState());
            case NORMAL -> switch (random.nextInt(8)) {
                case 0 -> GOTBlocks.PLANTAIN.get().defaultBlockState();
                case 1 -> dynamicPlant("clover", Blocks.GRASS.defaultBlockState());
                case 2 -> dynamicPlant("tall_grass_flowery", Blocks.GRASS.defaultBlockState());
                default -> Blocks.GRASS.defaultBlockState();
            };
        };
    }

    private static BlockState flower(GOTBiomeFloraProfile.PlantTheme theme, RandomSource random) {
        return switch (theme) {
            case ARID -> switch (random.nextInt(6)) {
                case 0 -> dynamicPlant("red_sand_gem", Blocks.POPPY.defaultBlockState());
                case 1 -> dynamicPlant("yellow_sand_gem", Blocks.DANDELION.defaultBlockState());
                case 2, 3 -> dynamicPlant("southern_daisy", Blocks.DANDELION.defaultBlockState());
                default -> dynamicPlant("southern_eastbells", Blocks.POPPY.defaultBlockState());
            };
            case ASSHAI -> GOTBlocks.ASSHAI_FLOWER.get().defaultBlockState();
            case VALYRIA -> random.nextBoolean() ? GOTBlocks.ASSHAI_FLOWER.get().defaultBlockState()
                    : Blocks.WITHER_ROSE.defaultBlockState();
            case YI_TI -> switch (random.nextInt(5)) {
                case 0 -> GOTBlocks.CHRYSANTHEMUM_ORANGE.get().defaultBlockState();
                case 1 -> dynamicPlant("chrysanthemum_blue", Blocks.CORNFLOWER.defaultBlockState());
                case 2 -> dynamicPlant("chrysanthemum_pink", Blocks.PINK_TULIP.defaultBlockState());
                case 3 -> dynamicPlant("chrysanthemum_yellow", Blocks.DANDELION.defaultBlockState());
                default -> dynamicPlant("chrysanthemum_white", Blocks.OXEYE_DAISY.defaultBlockState());
            };
            case MARSH -> GOTBlocks.DEAD_MARSH_PLANT.get().defaultBlockState();
            // Hibiscus is a two-block TallFlowerBlock and is generated in a
            // later structure-aware pass; this ground pass uses single-block
            // tropical flowers so it never leaves orphaned upper halves.
            case TROPICAL -> random.nextBoolean() ? GOTBlocks.CHRYSANTHEMUM_ORANGE.get().defaultBlockState()
                    : dynamicPlant("southern_daisy", Blocks.POPPY.defaultBlockState());
            case COLD -> dynamicPlant("bluebell", Blocks.CORNFLOWER.defaultBlockState());
            case NORMAL -> switch (random.nextInt(6)) {
                case 0 -> dynamicPlant("bluebell", Blocks.CORNFLOWER.defaultBlockState());
                case 1 -> dynamicPlant("marigold", Blocks.DANDELION.defaultBlockState());
                case 2 -> Blocks.POPPY.defaultBlockState();
                case 3 -> Blocks.AZURE_BLUET.defaultBlockState();
                case 4 -> Blocks.OXEYE_DAISY.defaultBlockState();
                default -> Blocks.CORNFLOWER.defaultBlockState();
            };
        };
    }

    private static BlockState dynamicPlant(String id, BlockState fallback) {
        var registered = GOTDecorativeFunctionalBlocks.ALL.get(id);
        return registered == null ? fallback : registered.get().defaultBlockState();
    }

    private static void aquaticPlants(WorldGenLevel level, RandomSource random, int minX, int minZ, int attempts) {
        for (int attempt = 0; attempt < attempts; attempt++) {
            int x = minX + 1 + random.nextInt(14);
            int z = minZ + 1 + random.nextInt(14);
            int y = level.getHeight(Heightmap.Types.OCEAN_FLOOR_WG, x, z);
            BlockPos pos = new BlockPos(x, y, z);
            if (level.getFluidState(pos).isEmpty()) continue;
            BlockState plant = random.nextInt(5) == 0
                    ? Blocks.KELP.defaultBlockState() : Blocks.SEAGRASS.defaultBlockState();
            if (plant.canSurvive(level, pos)) level.setBlock(pos, plant, UPDATE_FLAGS);
        }
    }

    private static void snowCover(WorldGenLevel level, RandomSource random, int minX, int minZ, int attempts) {
        for (int attempt = 0; attempt < attempts; attempt++) {
            BlockPos pos = randomPosition(level, random, minX, minZ);
            BlockState snow = Blocks.SNOW.defaultBlockState();
            if (level.getBlockState(pos).isAir() && snow.canSurvive(level, pos)) {
                level.setBlock(pos, snow, UPDATE_FLAGS);
            }
        }
    }

    private static void placePlant(WorldGenLevel level, BlockPos pos, BlockState state) {
        if (!level.getBlockState(pos).isAir()) return;
        if (!state.canSurvive(level, pos)) return;
        level.setBlock(pos, state, UPDATE_FLAGS);
    }

    private static BlockPos randomPosition(WorldGenLevel level, RandomSource random, int minX, int minZ) {
        int x = minX + 1 + random.nextInt(14);
        int z = minZ + 1 + random.nextInt(14);
        return surface(level, x, z);
    }

    private static BlockPos surface(WorldGenLevel level, int x, int z) {
        return new BlockPos(x, level.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, x, z), z);
    }

    private static boolean nearWater(WorldGenLevel level, BlockPos pos) {
        for (Direction direction : Direction.Plane.HORIZONTAL) {
            if (!level.getFluidState(pos.below().relative(direction)).isEmpty()) return true;
        }
        return false;
    }

    private static int fractionalAttempts(RandomSource random, float value) {
        int whole = (int)Math.floor(value);
        return whole + (random.nextFloat() < value - whole ? 1 : 0);
    }

    private static long mix(long seed, int chunkX, int chunkZ) {
        long value = seed ^ (long)chunkX * 341873128712L ^ (long)chunkZ * 132897987541L;
        value ^= value >>> 33;
        value *= 0xff51afd7ed558ccdL;
        value ^= value >>> 33;
        return value;
    }
}
