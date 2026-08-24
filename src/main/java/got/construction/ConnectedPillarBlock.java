package got.construction;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.core.Direction;

/**
 * Vertical connected-texture pillar used by the legacy GOT construction sets.
 * Any GOT pillar can visually join any other GOT pillar, matching 1.7.10.
 */
public final class ConnectedPillarBlock extends Block {
    public static final BooleanProperty UP = BlockStateProperties.UP;
    public static final BooleanProperty DOWN = BlockStateProperties.DOWN;

    public ConnectedPillarBlock(Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any().setValue(UP, false).setValue(DOWN, false));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockGetter level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        return defaultBlockState()
                .setValue(UP, isPillar(level.getBlockState(pos.above())))
                .setValue(DOWN, isPillar(level.getBlockState(pos.below())));
    }

    @Override
    public BlockState updateShape(
            BlockState state,
            Direction direction,
            BlockState neighbour,
            LevelAccessor level,
            BlockPos pos,
            BlockPos neighbourPos) {
        if (direction == Direction.UP) {
            return state.setValue(UP, isPillar(neighbour));
        }
        if (direction == Direction.DOWN) {
            return state.setValue(DOWN, isPillar(neighbour));
        }
        return super.updateShape(state, direction, neighbour, level, pos, neighbourPos);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(UP, DOWN);
    }

    private static boolean isPillar(BlockState state) {
        return state.getBlock() instanceof ConnectedPillarBlock;
    }
}
