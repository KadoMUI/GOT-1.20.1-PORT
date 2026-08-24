package got;

import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.List;

/** Snow-layer-style treasure pile; each placed item adds one visible layer. */
public class GOTTreasurePileBlock extends Block {
    public static final IntegerProperty LAYERS = IntegerProperty.create("layers", 1, 8);
    private static final VoxelShape[] SHAPES = new VoxelShape[] {
            box(0, 0, 0, 16, 2, 16), box(0, 0, 0, 16, 4, 16),
            box(0, 0, 0, 16, 6, 16), box(0, 0, 0, 16, 8, 16),
            box(0, 0, 0, 16, 10, 16), box(0, 0, 0, 16, 12, 16),
            box(0, 0, 0, 16, 14, 16), box(0, 0, 0, 16, 16, 16)
    };

    public GOTTreasurePileBlock(Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any().setValue(LAYERS, 1));
    }

    @Override
    public boolean canBeReplaced(BlockState state, BlockPlaceContext context) {
        return context.getItemInHand().is(asItem()) && state.getValue(LAYERS) < 8
                && (!context.isSecondaryUseActive() || context.replacingClickedOnBlock());
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState current = context.getLevel().getBlockState(context.getClickedPos());
        return current.is(this)
                ? current.setValue(LAYERS, Mth.clamp(current.getValue(LAYERS) + 1, 1, 8))
                : defaultBlockState();
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPES[state.getValue(LAYERS) - 1];
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPES[Math.max(0, state.getValue(LAYERS) - 2)];
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
        return List.of(new ItemStack(asItem(), state.getValue(LAYERS)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(LAYERS);
    }
}
