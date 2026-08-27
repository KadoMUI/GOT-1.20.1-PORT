package got;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

/**
 * Placeable Iron Bank / Coin Exchange block.
 *
 * Uses the same coin-exchange menu as eligible GOT traders, but without a
 * backing trader entity. A negative entity id tells the menu that the block
 * access should remain valid without an NPC proximity check.
 */
public class GOTIronBankBlock extends Block {
    public GOTIronBankBlock(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player,
                                 InteractionHand hand, BlockHitResult hit) {
        if (level.isClientSide) return InteractionResult.SUCCESS;
        if (!(player instanceof ServerPlayer serverPlayer)) return InteractionResult.PASS;

        GOTCoinExchangeMenu.open(serverPlayer, -1);
        return InteractionResult.CONSUME;
    }
}
