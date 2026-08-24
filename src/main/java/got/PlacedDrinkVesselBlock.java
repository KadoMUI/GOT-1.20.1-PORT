package got;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

/**
 * Shared 1.20.1 implementation of the original GOTBlockMug family.
 * Each concrete legacy block supplies its own vessel identity and exact
 * 1.7.10 width/height bounds; rendering remains block-entity based.
 */
public class PlacedDrinkVesselBlock extends BaseEntityBlock {
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

    private final GOTDrinkVessel defaultVessel;
    private final VoxelShape shape;

    public PlacedDrinkVesselBlock(Properties properties) {
        this(properties, GOTDrinkVessel.MUG, 3.0F, 8.0F);
    }

    public PlacedDrinkVesselBlock(Properties properties, GOTDrinkVessel defaultVessel, float widthPixels, float heightPixels) {
        super(properties);
        this.defaultVessel = defaultVessel;
        // Original 1.7.10 bounds used 75% of the supplied half-width.
        double radius = (widthPixels / 16.0D) * 0.75D;
        double min = 0.5D - radius;
        double max = 0.5D + radius;
        this.shape = Shapes.box(min, 0.0D, min, max, heightPixels / 16.0D * 0.75D, max);
        registerDefaultState(stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    public GOTDrinkVessel defaultVessel() {
        return defaultVessel;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<net.minecraft.world.level.block.Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override public RenderShape getRenderShape(BlockState state) { return RenderShape.INVISIBLE; }
    @Override public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) { return shape; }
    @Override public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) { return Shapes.empty(); }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        return level.getBlockState(pos.below()).isFaceSturdy(level, pos.below(), Direction.UP);
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState,
                                  net.minecraft.world.level.LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        if (direction == Direction.DOWN && !state.canSurvive(level, pos)) return net.minecraft.world.level.block.Blocks.AIR.defaultBlockState();
        return super.updateShape(state, direction, neighborState, level, pos, neighborPos);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player,
                                 InteractionHand hand, BlockHitResult hit) {
        BlockEntity raw = level.getBlockEntity(pos);
        if (!(raw instanceof PlacedDrinkVesselBlockEntity vessel)) return InteractionResult.PASS;
        ItemStack stored = vessel.getDrink();

        if (level.isClientSide) return InteractionResult.SUCCESS;

        if (player.isShiftKeyDown()) {
            ItemStack pickedUp = stored.isEmpty() ? vessel.getVessel().emptyStack() : stored.copy();
            if (!player.getInventory().add(pickedUp)) player.drop(pickedUp, false);
            level.removeBlock(pos, false);
            return InteractionResult.CONSUME;
        }

        if (stored.isEmpty()) return InteractionResult.PASS;
        if (!player.getItemInHand(hand).isEmpty()) return InteractionResult.PASS;
        if (stored.getItem() instanceof GOTDrinkItem drinkItem) {
            drinkItem.applyDrink(player, stored);
            vessel.removeDrink();
            return InteractionResult.CONSUME;
        }
        return InteractionResult.PASS;
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
        BlockEntity raw = builder.getOptionalParameter(LootContextParams.BLOCK_ENTITY);
        if (raw instanceof PlacedDrinkVesselBlockEntity vessel) {
            ItemStack drop = vessel.getDrink().isEmpty() ? vessel.getVessel().emptyStack() : vessel.getDrink().copy();
            return Collections.singletonList(drop);
        }
        return Collections.singletonList(defaultVessel.emptyStack());
    }

    @Nullable @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        PlacedDrinkVesselBlockEntity entity = new PlacedDrinkVesselBlockEntity(pos, state);
        entity.setInitialVessel(defaultVessel);
        return entity;
    }
}
