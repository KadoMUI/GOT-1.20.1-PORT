package got.world.flora;

import got.GOTTreeGrower;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.state.BlockState;

/** A GOT sapling that grows its registered species instead of a vanilla oak. */
public final class GOTSaplingBlock extends SaplingBlock {
    private final GOTTreeSpecies species;

    public GOTSaplingBlock(GOTTreeSpecies species, Properties properties) {
        super(new GOTTreeGrower(), properties);
        this.species = species;
    }

    @Override
    public void advanceTree(ServerLevel level, BlockPos pos, BlockState state, RandomSource random) {
        if (state.getValue(STAGE) == 0) {
            level.setBlock(pos, state.cycle(STAGE), Block.UPDATE_CLIENTS);
            return;
        }
        level.setBlock(pos, net.minecraft.world.level.block.Blocks.AIR.defaultBlockState(), Block.UPDATE_CLIENTS);
        if (!GOTTreeGenerator.generate(level, pos, random, species)) {
            level.setBlock(pos, state, Block.UPDATE_CLIENTS);
        }
    }

    public GOTTreeSpecies species() {
        return species;
    }
}
