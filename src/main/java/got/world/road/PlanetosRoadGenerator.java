package got.world.road;

import got.GOTMod;
import got.common.world.map.GOTBezierGenerator;
import got.world.biome.GOTBiomeMetadata;
import got.world.biome.PlanetosBiomeManager;
import got.world.terrain.PlanetosTerrainSampler;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.registries.ForgeRegistries;

/** Projects the original authored Bezier routes onto completed Planetos terrain. */
public final class PlanetosRoadGenerator {
    private static final int UPDATE_FLAGS = Block.UPDATE_CLIENTS | Block.UPDATE_KNOWN_SHAPE;
    private static final int CLEARANCE = 24;

    private PlanetosRoadGenerator() {}

    public static void generate(WorldGenLevel level, ChunkAccess chunk, long generatorSeed) {
        int minX = chunk.getPos().getMinBlockX();
        int minZ = chunk.getPos().getMinBlockZ();
        for (int localZ = 0; localZ < 16; localZ++) {
            for (int localX = 0; localX < 16; localX++) {
                int x = minX + localX;
                int z = minZ + localZ;
                if (!GOTBezierGenerator.isRouteAt(x, z)) continue;
                GOTBiomeMetadata metadata = PlanetosBiomeManager.getMetadata(x, z);
                if (metadata == null) continue;
                if (isWaterColumn(level, x, z)) {
                    bridge(level, x, z);
                } else {
                    path(level, generatorSeed, x, z, metadata);
                }
            }
        }
    }

    private static boolean isWaterColumn(WorldGenLevel level, int x, int z) {
        int surfaceY = level.getHeight(Heightmap.Types.WORLD_SURFACE_WG, x, z) - 1;
        BlockPos surface = new BlockPos(x, surfaceY, z);
        return !level.getFluidState(surface).isEmpty()
                || !level.getFluidState(surface.below()).isEmpty();
    }

    private static void path(WorldGenLevel level, long seed, int x, int z, GOTBiomeMetadata metadata) {
        int top = level.getHeight(Heightmap.Types.WORLD_SURFACE_WG, x, z) - 1;
        int groundY = findGround(level, x, top, z);
        if (groundY <= level.getMinBuildHeight()) return;

        RoadStyle style = RoadStyle.forBiome(metadata.id());
        long hash = hash(seed, x, z);
        BlockState topState = style.top(hash);
        BlockState filler = style.filler(hash >>> 8);
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos(x, groundY, z);
        level.setBlock(pos, topState, UPDATE_FLAGS);
        for (int depth = 1; depth <= 3; depth++) {
            pos.setY(groundY - depth);
            BlockState existing = level.getBlockState(pos);
            if (existing.isAir() || !existing.getFluidState().isEmpty()) break;
            level.setBlock(pos, filler, UPDATE_FLAGS);
        }
        clearAbove(level, x, groundY, z);

        if (metadata.id().equals("reach_fire_field") && Math.floorMod(hash, 13L) == 0L) {
            roadsideFlower(level, seed, x, z);
        }
    }

    private static void bridge(WorldGenLevel level, int x, int z) {
        int deckY = PlanetosTerrainSampler.SEA_LEVEL;
        BlockPos deck = new BlockPos(x, deckY, z);
        level.setBlock(deck, Blocks.OAK_PLANKS.defaultBlockState(), UPDATE_FLAGS);
        clearAbove(level, x, deckY, z);

        boolean edge = !GOTBezierGenerator.isRouteAt(x + 1, z)
                || !GOTBezierGenerator.isRouteAt(x - 1, z)
                || !GOTBezierGenerator.isRouteAt(x, z + 1)
                || !GOTBezierGenerator.isRouteAt(x, z - 1);
        if (edge) {
            level.setBlock(deck.above(), Blocks.OAK_FENCE.defaultBlockState(), UPDATE_FLAGS);
        }

        if (Math.floorMod(x + z, 8) == 0) {
            int floor = level.getHeight(Heightmap.Types.OCEAN_FLOOR_WG, x, z) - 1;
            BlockState support = Blocks.STRIPPED_OAK_LOG.defaultBlockState();
            if (support.hasProperty(BlockStateProperties.AXIS)) {
                support = support.setValue(RotatedPillarBlock.AXIS, net.minecraft.core.Direction.Axis.Y);
            }
            for (int y = deckY - 1; y > floor; y--) {
                level.setBlock(new BlockPos(x, y, z), support, UPDATE_FLAGS);
            }
        }
    }

    private static int findGround(WorldGenLevel level, int x, int startY, int z) {
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos(x, startY, z);
        for (int y = startY; y > level.getMinBuildHeight(); y--) {
            pos.setY(y);
            BlockState state = level.getBlockState(pos);
            if (state.isAir() || state.canBeReplaced() || state.is(BlockTags.LEAVES)
                    || state.is(BlockTags.LOGS) || state.is(Blocks.SNOW)
                    || !state.getFluidState().isEmpty()) continue;
            return y;
        }
        return level.getMinBuildHeight();
    }

    private static void clearAbove(WorldGenLevel level, int x, int groundY, int z) {
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos(x, groundY + 1, z);
        int ceiling = Math.min(level.getMaxBuildHeight() - 1, groundY + CLEARANCE);
        for (int y = groundY + 1; y <= ceiling; y++) {
            pos.setY(y);
            BlockState state = level.getBlockState(pos);
            if (state.isAir()) continue;
            if (state.canBeReplaced() || state.is(BlockTags.LEAVES) || state.is(BlockTags.LOGS)
                    || state.is(Blocks.SNOW) || state.is(Blocks.VINE)) {
                level.setBlock(pos, Blocks.AIR.defaultBlockState(), UPDATE_FLAGS);
            }
        }
    }

    private static void roadsideFlower(WorldGenLevel level, long seed, int x, int z) {
        int[][] offsets = {{5, 0}, {-5, 0}, {0, 5}, {0, -5}};
        int[] offset = offsets[(int)Math.floorMod(hash(seed ^ 0x464c4f574552L, x, z), offsets.length)];
        int flowerX = x + offset[0];
        int flowerZ = z + offset[1];
        if (GOTBezierGenerator.isRouteAt(flowerX, flowerZ)) return;
        int y = level.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, flowerX, flowerZ);
        BlockPos pos = new BlockPos(flowerX, y, flowerZ);
        BlockState flower = switch ((int)Math.floorMod(hash(seed, flowerX, flowerZ), 3L)) {
            case 0 -> Blocks.POPPY.defaultBlockState();
            case 1 -> Blocks.CORNFLOWER.defaultBlockState();
            default -> Blocks.DANDELION.defaultBlockState();
        };
        if (level.getBlockState(pos).isAir() && flower.canSurvive(level, pos)) {
            level.setBlock(pos, flower, UPDATE_FLAGS);
        }
    }

    private static BlockState registered(String id, BlockState fallback) {
        Block block = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(GOTMod.MOD_ID, id));
        return block == null || block == Blocks.AIR ? fallback : block.defaultBlockState();
    }

    private static long hash(long seed, long x, long z) {
        long value = seed ^ x * 341873128712L ^ z * 132897987541L;
        value ^= value >>> 33;
        value *= 0xff51afd7ed558ccdL;
        value ^= value >>> 33;
        return value;
    }

    private enum RoadStyle {
        DIRTY, PAVING, SANDY, COBBLE, ASSHAI, SNOWY, SOTHORYOS;

        static RoadStyle forBiome(String id) {
            if (id.equals("always_winter") || id.equals("frozen_shore") || id.equals("frostfangs")
                    || id.equals("westeros_frost") || id.equals("sothoryos_frost")
                    || id.equals("ulthos_frost")) return SNOWY;
            if (id.equals("arryn_town") || id.equals("crownlands_town") || id.equals("north_town")
                    || id.equals("reach_town") || id.equals("reach_arbor") || id.equals("reach_fire_field")
                    || id.equals("yi_ti") || id.equals("yi_ti_marshes")
                    || id.equals("yi_ti_tropical_forest")) return PAVING;
            if (id.equals("valyria") || id.equals("long_summer") || id.equals("long_summer_forest")) {
                return COBBLE;
            }
            if (id.startsWith("shadow_")) return ASSHAI;
            if (id.startsWith("sothoryos_jungle") || id.equals("sothoryos_mangrove")) return SOTHORYOS;
            if (isSandy(id)) return SANDY;
            return DIRTY;
        }

        private static boolean isSandy(String id) {
            return id.startsWith("dorne") || id.startsWith("cannibal_sands")
                    || id.startsWith("disputed_lands") || id.startsWith("ghiscar")
                    || id.startsWith("jogos_nhai_desert") || id.equals("lys") || id.equals("myr")
                    || id.equals("myr_forest") || id.equals("pentos") || id.equals("pentos_forest")
                    || id.startsWith("qarth") || id.equals("stepstones") || id.equals("tyrosh")
                    || id.startsWith("sothoryos_desert") || id.startsWith("ulthos_desert");
        }

        BlockState top(long hash) {
            int choice = (int)Math.floorMod(hash, 8L);
            return switch (this) {
                case DIRTY -> switch (choice) {
                    case 0, 1, 2, 3 -> Blocks.DIRT_PATH.defaultBlockState();
                    case 4, 5 -> Blocks.COARSE_DIRT.defaultBlockState();
                    default -> Blocks.GRAVEL.defaultBlockState();
                };
                case PAVING -> choice < 6 ? registered("bruschatka", Blocks.COBBLESTONE.defaultBlockState())
                        : Blocks.STONE_BRICKS.defaultBlockState();
                case SANDY -> switch (choice) {
                    case 0, 1 -> Blocks.COARSE_DIRT.defaultBlockState();
                    case 2, 3 -> Blocks.SAND.defaultBlockState();
                    case 4, 5 -> Blocks.SANDSTONE.defaultBlockState();
                    case 6 -> registered("sandstone_bricks", Blocks.CUT_SANDSTONE.defaultBlockState());
                    default -> Blocks.DIRT_PATH.defaultBlockState();
                };
                case COBBLE -> switch (choice) {
                    case 0, 1, 2, 3 -> Blocks.COBBLESTONE.defaultBlockState();
                    case 4, 5 -> Blocks.STONE_BRICKS.defaultBlockState();
                    default -> Blocks.MOSSY_COBBLESTONE.defaultBlockState();
                };
                case ASSHAI -> choice < 4 ? registered("asshai_dirt", Blocks.COARSE_DIRT.defaultBlockState())
                        : registered("basalt_gravel", Blocks.GRAVEL.defaultBlockState());
                case SNOWY -> Blocks.SNOW_BLOCK.defaultBlockState();
                case SOTHORYOS -> switch (choice % 3) {
                    case 0 -> registered("sothoryos_bricks", Blocks.STONE_BRICKS.defaultBlockState());
                    case 1 -> registered("cracked_sothoryos_bricks", Blocks.CRACKED_STONE_BRICKS.defaultBlockState());
                    default -> registered("mossy_sothoryos_bricks", Blocks.MOSSY_STONE_BRICKS.defaultBlockState());
                };
            };
        }

        BlockState filler(long hash) {
            return switch (this) {
                case DIRTY -> Math.floorMod(hash, 3L) == 0L
                        ? Blocks.GRAVEL.defaultBlockState() : Blocks.COARSE_DIRT.defaultBlockState();
                case PAVING -> registered("bruschatka", Blocks.COBBLESTONE.defaultBlockState());
                case SANDY -> Blocks.SANDSTONE.defaultBlockState();
                case COBBLE -> Blocks.COBBLESTONE.defaultBlockState();
                case ASSHAI -> registered("asshai_dirt", Blocks.COARSE_DIRT.defaultBlockState());
                case SNOWY -> Blocks.SNOW_BLOCK.defaultBlockState();
                case SOTHORYOS -> registered("sothoryos_bricks", Blocks.STONE_BRICKS.defaultBlockState());
            };
        }
    }
}
