package got;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

/** Uses vanilla bed placement/sleep/explosion behavior while rendering the legacy six-face textures as models. */
public class GOTBedBlock extends BedBlock {
    public GOTBedBlock(Properties properties) {
        super(DyeColor.BROWN, properties);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        // The color is fixed per registered block, so the vanilla bed block entity is unnecessary.
        return null;
    }
}
