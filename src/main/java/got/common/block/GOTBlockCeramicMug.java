package got.common.block;

import got.GOTDrinkVessel;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class GOTBlockCeramicMug extends GOTBlockMug {
    public GOTBlockCeramicMug() {
        super(BlockBehaviour.Properties.of().strength(0.0F).sound(SoundType.STONE).noOcclusion(), GOTDrinkVessel.CERAMIC_MUG, 3.0F, 8.0F);
    }
}
