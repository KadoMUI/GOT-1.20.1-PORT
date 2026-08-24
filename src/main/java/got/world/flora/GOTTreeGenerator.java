package got.world.flora;

import got.GOTDecorativeFunctionalBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.VineBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.core.Direction.Axis;

/** Direct, deterministic tree geometry used by Planetos decoration and GOT saplings. */
public final class GOTTreeGenerator {
    private static final int UPDATE_FLAGS = Block.UPDATE_CLIENTS | Block.UPDATE_KNOWN_SHAPE;

    private GOTTreeGenerator() {}

    public static boolean generate(LevelAccessor level, BlockPos base, RandomSource random,
                                   GOTTreeSpecies species) {
        if (!validGround(level, base, species) || !trunkClear(level, base, species)) return false;
        int height = species.randomHeight(random);
        return switch (species.shape()) {
            case DECIDUOUS -> deciduous(level, base, random, species, height, false);
            case CONIFER -> conifer(level, base, random, species, height, false);
            case CYPRESS -> conifer(level, base, random, species, height, true);
            case SAVANNA -> savanna(level, base, random, species, height);
            case PALM -> palm(level, base, random, species, height);
            case WILLOW -> willow(level, base, random, species, height);
            case MANGROVE -> mangrove(level, base, random, species, height);
            case TROPICAL -> deciduous(level, base, random, species, height, true);
            case GIANT -> giant(level, base, random, species, height, false);
            case REDWOOD -> redwood(level, base, random, species, height);
            case BAOBAB -> giant(level, base, random, species, height, true);
            case DEAD -> dead(level, base, random, species, height);
        };
    }

    private static boolean validGround(LevelAccessor level, BlockPos base, GOTTreeSpecies species) {
        if (base.getY() <= level.getMinBuildHeight() || base.getY() + 32 >= level.getMaxBuildHeight()) return false;
        BlockState ground = level.getBlockState(base.below());
        boolean normal = ground.is(BlockTags.DIRT) || ground.is(BlockTags.SAND)
                || ground.is(Blocks.SNOW_BLOCK) || ground.is(Blocks.MUD)
                || ground.is(Blocks.CLAY) || ground.is(Blocks.GRAVEL);
        if (species.shape() == GOTTreeSpecies.Shape.MANGROVE) {
            return normal || !level.getFluidState(base.below()).isEmpty();
        }
        return normal && level.getFluidState(base).isEmpty();
    }

    private static boolean trunkClear(LevelAccessor level, BlockPos base, GOTTreeSpecies species) {
        int probe = Math.min(9, species.randomHeight(RandomSource.create(base.asLong())));
        for (int y = 0; y <= probe; y++) {
            BlockState state = level.getBlockState(base.above(y));
            if (!canReplace(state) && !state.is(species.log())) return false;
        }
        return true;
    }

    private static boolean deciduous(LevelAccessor level, BlockPos base, RandomSource random,
                                     GOTTreeSpecies species, int height, boolean tropical) {
        trunk(level, base, species, height, tropical ? 2 : 1);
        int radius = tropical ? 3 : 2;
        leafDisc(level, base.above(height - 2), species, radius, random, 1);
        leafDisc(level, base.above(height - 1), species, radius + 1, random, 1);
        leafDisc(level, base.above(height), species, radius, random, 1);
        leafDisc(level, base.above(height + 1), species, Math.max(1, radius - 1), random, 0);
        for (Direction direction : Direction.Plane.HORIZONTAL) {
            if (random.nextBoolean()) branch(level, base.above(height - 2), direction, species, tropical ? 3 : 2);
        }
        if (tropical) addHangingVines(level, base.above(height), radius + 1, random, "oldwood_vines");
        return true;
    }

    private static boolean conifer(LevelAccessor level, BlockPos base, RandomSource random,
                                   GOTTreeSpecies species, int height, boolean narrow) {
        trunk(level, base, species, height, 1);
        int crown = narrow ? Math.max(5, height - 3) : Math.max(6, height - 2);
        for (int y = 0; y < crown; y++) {
            int fromTop = crown - y;
            int radius = narrow ? (fromTop % 3 == 0 ? 2 : 1) : Math.min(3, 1 + fromTop / 4);
            if (y % 2 == 0 || narrow) leafDisc(level, base.above(height - crown + y), species, radius, random, 0);
        }
        leaf(level, base.above(height), species, 1);
        return true;
    }

    private static boolean savanna(LevelAccessor level, BlockPos base, RandomSource random,
                                   GOTTreeSpecies species, int height) {
        trunk(level, base, species, height, 1);
        Direction first = Direction.Plane.HORIZONTAL.getRandomDirection(random);
        Direction second = first.getClockWise();
        branch(level, base.above(height - 2), first, species, 3);
        branch(level, base.above(height - 3), second, species, 2);
        leafDisc(level, base.above(height), species, 3, random, 0);
        leafDisc(level, base.above(height + 1), species, 2, random, 0);
        leafDisc(level, base.above(height - 1).relative(first, 3), species, 2, random, 0);
        return true;
    }

    private static boolean palm(LevelAccessor level, BlockPos base, RandomSource random,
                                GOTTreeSpecies species, int height) {
        trunk(level, base, species, height, 1);
        BlockPos crown = base.above(height);
        leaf(level, crown, species, 1);
        for (Direction direction : Direction.Plane.HORIZONTAL) {
            for (int distance = 1; distance <= 4; distance++) {
                BlockPos pos = crown.relative(direction, distance).below(distance == 4 ? 1 : 0);
                leaf(level, pos, species, distance);
                if (distance == 2) leaf(level, pos.relative(direction.getClockWise()), species, 3);
            }
        }
        if (species == GOTTreeSpecies.BANANA) hangingFruit(level, crown, random, "banana");
        if (species == GOTTreeSpecies.DATE_PALM) hangingFruit(level, crown, random, "date");
        return true;
    }

    private static boolean willow(LevelAccessor level, BlockPos base, RandomSource random,
                                  GOTTreeSpecies species, int height) {
        trunk(level, base, species, height, 1);
        BlockPos crown = base.above(height - 1);
        leafDisc(level, crown.below(), species, 2, random, 1);
        leafDisc(level, crown, species, 3, random, 1);
        leafDisc(level, crown.above(), species, 2, random, 0);
        addHangingVines(level, crown, 3, random, "willow_vines");
        return true;
    }

    private static boolean mangrove(LevelAccessor level, BlockPos base, RandomSource random,
                                    GOTTreeSpecies species, int height) {
        for (Direction direction : Direction.Plane.HORIZONTAL) {
            BlockPos root = base.relative(direction);
            log(level, root, species, Axis.Y);
            log(level, root.below(), species, Axis.Y);
        }
        trunk(level, base, species, height, 1);
        leafDisc(level, base.above(height - 1), species, 3, random, 1);
        leafDisc(level, base.above(height), species, 3, random, 1);
        leafDisc(level, base.above(height + 1), species, 2, random, 0);
        return true;
    }

    private static boolean giant(LevelAccessor level, BlockPos base, RandomSource random,
                                 GOTTreeSpecies species, int height, boolean flatCrown) {
        trunk(level, base, species, height, 2);
        BlockPos crown = base.above(height - 1);
        if (flatCrown) {
            leafDisc(level, crown, species, 5, random, 1);
            leafDisc(level, crown.above(), species, 4, random, 0);
        } else {
            leafDisc(level, crown.below(2), species, 3, random, 1);
            leafDisc(level, crown.below(), species, 4, random, 1);
            leafDisc(level, crown, species, 4, random, 1);
            leafDisc(level, crown.above(), species, 3, random, 0);
            leafDisc(level, crown.above(2), species, 2, random, 0);
        }
        for (Direction direction : Direction.Plane.HORIZONTAL) {
            branch(level, base.above(height - 3), direction, species, flatCrown ? 4 : 3);
        }
        return true;
    }

    private static boolean redwood(LevelAccessor level, BlockPos base, RandomSource random,
                                   GOTTreeSpecies species, int height) {
        trunk(level, base, species, height, 2);
        int crownStart = height / 2;
        for (int y = crownStart; y <= height; y += 2) {
            int radius = Math.max(1, 4 - (y - crownStart) / Math.max(2, (height - crownStart) / 3));
            leafDisc(level, base.above(y), species, radius, random, 0);
        }
        leaf(level, base.above(height + 1), species, 1);
        return true;
    }

    private static boolean dead(LevelAccessor level, BlockPos base, RandomSource random,
                                GOTTreeSpecies species, int height) {
        trunk(level, base, species, height, 1);
        Direction direction = Direction.Plane.HORIZONTAL.getRandomDirection(random);
        branch(level, base.above(height - 2), direction, species, 2 + random.nextInt(2));
        branch(level, base.above(height - 4), direction.getOpposite(), species, 2);
        return true;
    }

    private static void trunk(LevelAccessor level, BlockPos base, GOTTreeSpecies species, int height, int width) {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                for (int z = 0; z < width; z++) log(level, base.offset(x, y, z), species, Axis.Y);
            }
        }
    }

    private static void branch(LevelAccessor level, BlockPos start, Direction direction,
                               GOTTreeSpecies species, int length) {
        Axis axis = direction.getAxis();
        for (int distance = 1; distance <= length; distance++) {
            log(level, start.relative(direction, distance).above(distance / 3), species, axis);
        }
    }

    private static void leafDisc(LevelAccessor level, BlockPos center, GOTTreeSpecies species,
                                 int radius, RandomSource random, int verticalThickness) {
        if (!species.hasLeaves()) return;
        int radiusSquared = radius * radius + 1;
        for (int y = -verticalThickness; y <= verticalThickness; y++) {
            for (int x = -radius; x <= radius; x++) {
                for (int z = -radius; z <= radius; z++) {
                    if (x * x + z * z > radiusSquared) continue;
                    if (Math.abs(x) == radius && Math.abs(z) == radius) continue;
                    if (random.nextInt(12) == 0 && x * x + z * z > radiusSquared - 2) continue;
                    leaf(level, center.offset(x, y, z), species, Math.max(1, Math.abs(x) + Math.abs(z)));
                }
            }
        }
    }

    private static void log(LevelAccessor level, BlockPos pos, GOTTreeSpecies species, Axis axis) {
        BlockState current = level.getBlockState(pos);
        if (!canReplace(current) && !current.is(species.log())) return;
        BlockState state = species.log().defaultBlockState();
        if (state.hasProperty(BlockStateProperties.AXIS)) state = state.setValue(RotatedPillarBlock.AXIS, axis);
        level.setBlock(pos, state, UPDATE_FLAGS);
    }

    private static void leaf(LevelAccessor level, BlockPos pos, GOTTreeSpecies species, int distance) {
        if (!species.hasLeaves()) return;
        BlockState current = level.getBlockState(pos);
        if (!canReplace(current) || current.is(BlockTags.LOGS)) return;
        BlockState state = species.leaves().defaultBlockState();
        if (state.hasProperty(LeavesBlock.DISTANCE)) state = state.setValue(LeavesBlock.DISTANCE, Math.min(6, Math.max(1, distance)));
        if (state.hasProperty(LeavesBlock.PERSISTENT)) state = state.setValue(LeavesBlock.PERSISTENT, false);
        level.setBlock(pos, state, UPDATE_FLAGS);
    }

    private static void addHangingVines(LevelAccessor level, BlockPos center, int radius,
                                        RandomSource random, String vineId) {
        var registered = GOTDecorativeFunctionalBlocks.ALL.get(vineId);
        if (registered == null) return;
        Block vine = registered.get();
        for (Direction direction : Direction.Plane.HORIZONTAL) {
            for (int offset = -radius; offset <= radius; offset++) {
                if (random.nextInt(3) != 0) continue;
                BlockPos top = direction.getAxis() == Axis.X
                        ? center.offset(direction.getStepX() * radius, 0, offset)
                        : center.offset(offset, 0, direction.getStepZ() * radius);
                int length = 1 + random.nextInt(3);
                for (int down = 1; down <= length; down++) {
                    BlockPos pos = top.below(down);
                    if (!level.getBlockState(pos).isAir()) break;
                    BlockState state = vine.defaultBlockState();
                    if (state.hasProperty(VineBlock.UP)) state = state.setValue(VineBlock.UP, true);
                    level.setBlock(pos, state, UPDATE_FLAGS);
                }
            }
        }
    }

    private static void hangingFruit(LevelAccessor level, BlockPos crown, RandomSource random, String id) {
        var registered = GOTDecorativeFunctionalBlocks.ALL.get(id);
        if (registered == null) return;
        BlockState fruit = registered.get().defaultBlockState();
        for (Direction direction : Direction.Plane.HORIZONTAL) {
            if (!random.nextBoolean()) continue;
            BlockPos pos = crown.relative(direction).below();
            if (level.getBlockState(pos).isAir() && fruit.canSurvive(level, pos)) {
                level.setBlock(pos, fruit, UPDATE_FLAGS);
            }
        }
    }

    private static boolean canReplace(BlockState state) {
        return state.isAir() || state.canBeReplaced() || state.is(BlockTags.LEAVES)
                || state.is(Blocks.SNOW) || state.is(Blocks.VINE);
    }
}
