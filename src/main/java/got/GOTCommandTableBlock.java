package got;

import got.network.GOTNetwork;
import got.network.S2COpenGOTMapPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.network.PacketDistributor;

/**
 * Lightweight 1.0 Command Table implementation.
 *
 * This pass restores the physical table model/shape and gives the block a
 * useful interaction without committing to the post-1.0 conquest overhaul.
 * Right-click opens the same GOT map screen used by GOT Menu -> Map.
 */
public final class GOTCommandTableBlock extends Block {
    private static final VoxelShape TOP = Block.box(0, 10, 0, 16, 16, 16);
    private static final VoxelShape LEG_NW = Block.box(1, 0, 1, 4, 10, 4);
    private static final VoxelShape LEG_NE = Block.box(12, 0, 1, 15, 10, 4);
    private static final VoxelShape LEG_SW = Block.box(1, 0, 12, 4, 10, 15);
    private static final VoxelShape LEG_SE = Block.box(12, 0, 12, 15, 10, 15);
    private static final VoxelShape SHAPE = Shapes.or(TOP, LEG_NW, LEG_NE, LEG_SW, LEG_SE);

    public GOTCommandTableBlock(Properties properties) {
        super(properties);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public boolean useShapeForLightOcclusion(BlockState state) {
        return true;
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos,
                                 Player player, InteractionHand hand, BlockHitResult hit) {
        if (!level.isClientSide && player instanceof ServerPlayer serverPlayer) {
            GOTNetwork.CHANNEL.send(PacketDistributor.PLAYER.with(() -> serverPlayer), new S2COpenGOTMapPacket());
            player.awardStat(Stats.INTERACT_WITH_CRAFTING_TABLE); // interaction statistic only; no menu/container
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }
}
