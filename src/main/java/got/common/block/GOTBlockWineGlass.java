package got.common.block;

import got.GOTDrinkVessel;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class GOTBlockWineGlass extends GOTBlockMug {
    public GOTBlockWineGlass() {
        super(BlockBehaviour.Properties.of().strength(0.0F).sound(SoundType.GLASS).noOcclusion(), GOTDrinkVessel.WINE_GLASS, 2.5F, 10.0F);
    }
}
