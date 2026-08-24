package got;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class GOTKebabStandBlock extends Block {
    public static final IntegerProperty MEAT = IntegerProperty.create("meat", 0, 3);
    public static final BooleanProperty COOKED = BooleanProperty.create("cooked");
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final TagKey<Item> KEBAB_MEATS = TagKey.create(Registries.ITEM,
            ResourceLocation.fromNamespaceAndPath(GOTMod.MOD_ID, "kebab_meats"));
    private static final VoxelShape SHAPE = box(1, 0, 1, 15, 14, 15);

    public GOTKebabStandBlock(Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any()
                .setValue(MEAT, 0).setValue(COOKED, false).setValue(FACING, Direction.NORTH));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player,
                                 InteractionHand hand, BlockHitResult hit) {
        ItemStack held = player.getItemInHand(hand);
        int meat = state.getValue(MEAT);

        if (meat < 3 && held.is(KEBAB_MEATS)) {
            if (!level.isClientSide) {
                level.setBlock(pos, state.setValue(MEAT, meat + 1).setValue(COOKED, false), 3);
                level.scheduleTick(pos, this, 600);
                if (!player.getAbilities().instabuild) {
                    held.shrink(1);
                }
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }

        if (meat > 0 && state.getValue(COOKED)) {
            if (!level.isClientSide) {
                ItemStack result = new ItemStack(GOTItems.KEBAB.get(), meat);
                if (!player.getInventory().add(result)) {
                    popResource(level, pos, result);
                }
                level.setBlock(pos, state.setValue(MEAT, 0).setValue(COOKED, false), 3);
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }
        return InteractionResult.PASS;
    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (state.getValue(MEAT) > 0 && !state.getValue(COOKED)) {
            level.setBlock(pos, state.setValue(COOKED, true), 3);
        }
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(MEAT, COOKED, FACING);
    }
}
