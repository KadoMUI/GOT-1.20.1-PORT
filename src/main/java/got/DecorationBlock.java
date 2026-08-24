package got;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public final class DecorationBlock extends Block {
    public enum Shape { PLATE, CUP, JAR, CAGE, CHANDELIER, CHAIN, ROCK, ROPE, HEARTH, KEBAB }
    private final VoxelShape shape;

    public DecorationBlock(Properties properties, Shape type) {
        super(properties);
        this.shape = switch (type) {
            case PLATE -> Block.box(2, 0, 2, 14, 2, 14);
            case CUP -> Block.box(5, 0, 5, 11, 9, 11);
            case JAR -> Block.box(4, 0, 4, 12, 13, 12);
            case CAGE -> Block.box(2, 0, 2, 14, 16, 14);
            case CHANDELIER -> Block.box(1, 3, 1, 15, 16, 15);
            case CHAIN -> Block.box(6, 0, 6, 10, 16, 10);
            case ROCK -> Block.box(2, 0, 2, 14, 7, 14);
            case ROPE -> Block.box(6, 0, 6, 10, 16, 10);
            case HEARTH -> Block.box(0, 0, 0, 16, 8, 16);
            case KEBAB -> Block.box(6, 0, 6, 10, 16, 10);
        };
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return shape;
    }

    @Override
    public boolean propagatesSkylightDown(BlockState state, BlockGetter level, BlockPos pos) {
        return true;
    }
}
