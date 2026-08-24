package got.common.block;

import got.GOTDrinkVessel;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class GOTBlockGlassBottle extends GOTBlockMug {
    public GOTBlockGlassBottle() {
        super(BlockBehaviour.Properties.of().strength(0.0F).sound(SoundType.GLASS).noOcclusion(), GOTDrinkVessel.BOTTLE, 3.0F, 10.0F);
    }
}
