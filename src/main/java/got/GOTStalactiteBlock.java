package got;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class GOTStalactiteBlock extends Block {
    public static final DirectionProperty TIP_DIRECTION = BlockStateProperties.VERTICAL_DIRECTION;
    private static final VoxelShape SHAPE = box(4, 0, 4, 12, 16, 12);

    public GOTStalactiteBlock(Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any().setValue(TIP_DIRECTION, Direction.DOWN));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction direction = context.getClickedFace() == Direction.DOWN ? Direction.DOWN : Direction.UP;
        BlockState preferred = defaultBlockState().setValue(TIP_DIRECTION, direction);
        if (preferred.canSurvive(context.getLevel(), context.getClickedPos())) {
            return preferred;
        }
        BlockState opposite = preferred.setValue(TIP_DIRECTION, direction.getOpposite());
        return opposite.canSurvive(context.getLevel(), context.getClickedPos()) ? opposite : null;
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        Direction supportDirection = state.getValue(TIP_DIRECTION).getOpposite();
        BlockPos support = pos.relative(supportDirection);
        return level.getBlockState(support).isFaceSturdy(level, support, supportDirection.getOpposite());
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighbor,
                                  net.minecraft.world.level.LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        return direction == state.getValue(TIP_DIRECTION).getOpposite() && !canSurvive(state, level, pos)
                ? net.minecraft.world.level.block.Blocks.AIR.defaultBlockState()
                : super.updateShape(state, direction, neighbor, level, pos, neighborPos);
    }

    @Override
    public void fallOn(Level level, BlockState state, BlockPos pos, Entity entity, float fallDistance) {
        if (state.getValue(TIP_DIRECTION) == Direction.UP) {
            entity.causeFallDamage(fallDistance + 1.0F, 2.0F, level.damageSources().stalagmite());
        } else {
            super.fallOn(level, state, pos, entity, fallDistance);
        }
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(TIP_DIRECTION);
    }
}
