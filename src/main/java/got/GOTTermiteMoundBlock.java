package got;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

public class GOTTermiteMoundBlock extends Block {
    private final boolean infested;

    public GOTTermiteMoundBlock(boolean infested, Properties properties) {
        super(properties);
        this.infested = infested;
    }

    @Override
    public void playerDestroy(Level level, Player player, BlockPos pos, BlockState state,
                              @Nullable BlockEntity blockEntity, ItemStack tool) {
        super.playerDestroy(level, player, pos, state, blockEntity, tool);
        if (!level.isClientSide && infested) {
            popResource(level, pos, new ItemStack(GOTEquipment.TERMITE.get(), 2 + level.random.nextInt(4)));
        }
    }
}
