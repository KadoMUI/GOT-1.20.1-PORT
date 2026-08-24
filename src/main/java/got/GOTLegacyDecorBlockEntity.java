package got;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

/** Marker block entity used only to attach the original decorative renderers. */
public final class GOTLegacyDecorBlockEntity extends BlockEntity {
    public GOTLegacyDecorBlockEntity(BlockPos pos, BlockState state) {
        super(GOTBlockEntities.LEGACY_DECOR.get(), pos, state);
    }
}
