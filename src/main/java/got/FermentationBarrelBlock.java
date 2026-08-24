package got;

import net.minecraft.core.*;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;

public final class FermentationBarrelBlock extends BaseEntityBlock {
    public FermentationBarrelBlock(Properties properties) { super(properties); }
    @Override public RenderShape getRenderShape(BlockState state) { return RenderShape.MODEL; }
    @Override public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        BlockEntity be = level.getBlockEntity(pos);
        if (be instanceof FermentationBarrelBlockEntity barrel) {
            ItemStack held = player.getItemInHand(hand);
            if (!level.isClientSide && barrel.tryFillVessel(player, hand, held)) return InteractionResult.CONSUME;
            if (!level.isClientSide && player instanceof ServerPlayer sp) NetworkHooks.openScreen(sp, barrel, pos);
            return InteractionResult.sidedSuccess(level.isClientSide);
        }
        return InteractionResult.PASS;
    }
    @Override public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean moving) {
        if (!state.is(newState.getBlock()) && level.getBlockEntity(pos) instanceof FermentationBarrelBlockEntity barrel) Containers.dropContents(level,pos,barrel);
        super.onRemove(state,level,pos,newState,moving);
    }
    @Nullable @Override public BlockEntity newBlockEntity(BlockPos pos, BlockState state) { return new FermentationBarrelBlockEntity(pos,state); }
    @Nullable @Override public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return level.isClientSide ? null : createTickerHelper(type, GOTBlockEntities.FERMENTATION_BARREL.get(), FermentationBarrelBlockEntity::serverTick);
    }
}
