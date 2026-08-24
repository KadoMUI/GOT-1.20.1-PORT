package got.common.block;

import got.GOTDrinkVessel;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class GOTBlockClayMug extends GOTBlockMug {
    public GOTBlockClayMug() {
        super(BlockBehaviour.Properties.of().strength(0.0F).sound(SoundType.STONE).noOcclusion(), GOTDrinkVessel.CLAY_MUG, 3.0F, 8.0F);
    }
}
