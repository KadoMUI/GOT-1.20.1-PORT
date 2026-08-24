package got;

import got.economy.GOTCoinValueService;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

/** Iron Bank coin consolidation using the shared legacy-value economy service. */
public class GOTIronBankBlock extends Block {
    public GOTIronBankBlock(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player,
                                 InteractionHand hand, BlockHitResult hit) {
        if (level.isClientSide) return InteractionResult.SUCCESS;
        if (!(player instanceof ServerPlayer serverPlayer)) return InteractionResult.PASS;

        int total = GOTCoinValueService.inventoryValue(serverPlayer);
        if (total > 0) {
            GOTCoinValueService.take(serverPlayer, total);
            GOTCoinValueService.give(serverPlayer, total);
        }

        player.displayClientMessage(
            Component.translatable("message.got.iron_bank_balance", total),
            true
        );
        return InteractionResult.CONSUME;
    }
}
