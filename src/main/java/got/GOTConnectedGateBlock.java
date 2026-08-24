package got;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;

/** Connected portcullis/gate behavior: a contiguous panel opens as one by hand or redstone. */
public class GOTConnectedGateBlock extends Block {
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty OPEN = BlockStateProperties.OPEN;
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
    private static final VoxelShape NORTH_SOUTH = box(0, 0, 6, 16, 16, 10);
    private static final VoxelShape EAST_WEST = box(6, 0, 0, 10, 16, 16);
    private final Supplier<SoundEvent> openSound;
    private final Supplier<SoundEvent> closeSound;

    public GOTConnectedGateBlock(Properties properties, Supplier<SoundEvent> openSound, Supplier<SoundEvent> closeSound) {
        super(properties);
        this.openSound = openSound;
        this.closeSound = closeSound;
        registerDefaultState(stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(OPEN, false)
                .setValue(POWERED, false));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        boolean powered = context.getLevel().hasNeighborSignal(context.getClickedPos());
        return defaultBlockState()
                .setValue(FACING, context.getHorizontalDirection().getOpposite())
                .setValue(OPEN, powered)
                .setValue(POWERED, powered);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player,
                                 InteractionHand hand, BlockHitResult hit) {
        if (!level.isClientSide) {
            setConnectedOpen(level, pos, !state.getValue(OPEN), state.getValue(POWERED));
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block neighbor,
                                BlockPos neighborPos, boolean moving) {
        if (level.isClientSide) {
            return;
        }
        boolean powered = level.hasNeighborSignal(pos);
        if (powered != state.getValue(POWERED)) {
            setConnectedOpen(level, pos, powered, powered);
        }
    }

    private void setConnectedOpen(Level level, BlockPos origin, boolean open, boolean powered) {
        Set<BlockPos> connected = collectConnected(level, origin);
        for (BlockPos pos : connected) {
            BlockState state = level.getBlockState(pos);
            level.setBlock(pos, state.setValue(OPEN, open).setValue(POWERED, powered), 10);
        }
        level.playSound(null, origin, open ? openSound.get() : closeSound.get(), SoundSource.BLOCKS,
                1.0F, 0.85F + level.random.nextFloat() * 0.3F);
    }

    private static Set<BlockPos> collectConnected(LevelAccessor level, BlockPos origin) {
        Set<BlockPos> result = new HashSet<>();
        ArrayDeque<BlockPos> queue = new ArrayDeque<>();
        BlockState originState = level.getBlockState(origin);
        Direction.Axis axis = originState.getValue(FACING).getAxis();
        boolean expectedOpen = originState.getValue(OPEN);
        queue.add(origin.immutable());

        while (!queue.isEmpty() && result.size() < 4096) {
            BlockPos pos = queue.removeFirst();
            if (!result.add(pos) || pos.distManhattan(origin) > 16) {
                continue;
            }
            for (Direction direction : Direction.values()) {
                if ((axis == Direction.Axis.Z && direction.getAxis() == Direction.Axis.Z)
                        || (axis == Direction.Axis.X && direction.getAxis() == Direction.Axis.X)) {
                    continue;
                }
                BlockPos next = pos.relative(direction);
                BlockState nextState = level.getBlockState(next);
                if (nextState.getBlock() instanceof GOTConnectedGateBlock
                        && nextState.getValue(FACING).getAxis() == axis
                        && nextState.getValue(OPEN) == expectedOpen
                        && !result.contains(next)) {
                    queue.addLast(next.immutable());
                }
            }
        }
        return result;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        if (state.getValue(OPEN)) {
            return state.getValue(FACING).getAxis() == Direction.Axis.X
                    ? box(6, 14, 0, 10, 16, 16)
                    : box(0, 14, 6, 16, 16, 10);
        }
        return state.getValue(FACING).getAxis() == Direction.Axis.X ? EAST_WEST : NORTH_SOUTH;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return state.getValue(OPEN) ? Shapes.empty() : getShape(state, level, pos, context);
    }

    @Override
    public boolean isPathfindable(BlockState state, BlockGetter level, BlockPos pos, PathComputationType type) {
        return state.getValue(OPEN);
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        return rotate(state, mirror.getRotation(state.getValue(FACING)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, OPEN, POWERED);
    }
}
