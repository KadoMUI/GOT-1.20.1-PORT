package got;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;

/** Placeable wildfire jar. Flint, fire charges, redstone, or another explosion trigger it. */
public class GOTWildFireJarBlock extends Block {
    public static final BooleanProperty LIT = BlockStateProperties.LIT;

    public GOTWildFireJarBlock(Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any().setValue(LIT, false));
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player,
                                 InteractionHand hand, BlockHitResult hit) {
        var stack = player.getItemInHand(hand);
        if (!stack.is(Items.FLINT_AND_STEEL) && !stack.is(Items.FIRE_CHARGE)) {
            return InteractionResult.PASS;
        }
        if (!level.isClientSide) {
            prime(level, pos, state);
            if (!player.getAbilities().instabuild) {
                if (stack.is(Items.FLINT_AND_STEEL)) {
                    stack.hurtAndBreak(1, player, entity -> entity.broadcastBreakEvent(hand));
                } else {
                    stack.shrink(1);
                }
            }
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean moving) {
        if (!oldState.is(state.getBlock()) && level.hasNeighborSignal(pos)) {
            prime(level, pos, state);
        }
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block neighbor,
                                BlockPos neighborPos, boolean moving) {
        if (level.hasNeighborSignal(pos) && !state.getValue(LIT)) {
            prime(level, pos, state);
        }
    }

    private void prime(Level level, BlockPos pos, BlockState state) {
        if (!state.getValue(LIT)) {
            level.setBlock(pos, state.setValue(LIT, true), 3);
            level.scheduleTick(pos, this, 50);
            level.playSound(null, pos, SoundEvents.TNT_PRIMED, SoundSource.BLOCKS, 1.0F, 1.0F);
        }
    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        explode(level, pos);
    }

    @Override
    public void wasExploded(Level level, BlockPos pos, Explosion explosion) {
        if (!level.isClientSide) {
            explode(level, pos);
        }
    }

    public void explode(Level level, BlockPos pos) {
        level.removeBlock(pos, false);
        level.explode(null, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D,
                3.5F, true, Level.ExplosionInteraction.BLOCK);
        for (BlockPos target : new BlockPos[] {pos, pos.north(), pos.south(), pos.east(), pos.west()}) {
            BlockState targetState = level.getBlockState(target);
            BlockState fire = GOTDecorativeFunctionalBlocks.WILD_FIRE.get().defaultBlockState();
            if (targetState.canBeReplaced() && fire.canSurvive(level, target)) {
                level.setBlock(target, fire, 3);
            }
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(LIT);
    }
}
