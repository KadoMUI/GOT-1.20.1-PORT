package got;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;

/** Redstone or hand-triggered Sothoryos dart trap with a short poison cooldown. */
public class GOTSarbacaneTrapBlock extends Block {
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty TRIGGERED = BlockStateProperties.TRIGGERED;
    private final float damage;

    public GOTSarbacaneTrapBlock(float damage, Properties properties) {
        super(properties);
        this.damage = damage;
        registerDefaultState(stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(TRIGGERED, false));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player,
                                 InteractionHand hand, BlockHitResult hit) {
        if (!level.isClientSide) {
            fire(level, pos, state);
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block neighbor,
                                BlockPos neighborPos, boolean moving) {
        if (!level.isClientSide && level.hasNeighborSignal(pos)) {
            fire(level, pos, state);
        }
    }

    private void fire(Level level, BlockPos pos, BlockState state) {
        if (state.getValue(TRIGGERED)) {
            return;
        }
        Direction facing = state.getValue(FACING);
        AABB line = new AABB(pos.relative(facing)).expandTowards(
                facing.getStepX() * 7.0D, 0.0D, facing.getStepZ() * 7.0D).inflate(0.25D);
        for (LivingEntity target : level.getEntitiesOfClass(LivingEntity.class, line)) {
            target.hurt(level.damageSources().magic(), damage);
            target.addEffect(new MobEffectInstance(MobEffects.POISON, 80, 0));
            break;
        }
        level.setBlock(pos, state.setValue(TRIGGERED, true), 3);
        level.scheduleTick(pos, this, 30);
        level.playSound(null, pos, SoundEvents.ARROW_SHOOT, SoundSource.BLOCKS, 0.8F, 1.2F);
    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (state.getValue(TRIGGERED)) {
            level.setBlock(pos, state.setValue(TRIGGERED, false), 3);
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, TRIGGERED);
    }
}
