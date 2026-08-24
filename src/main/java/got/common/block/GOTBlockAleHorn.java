package got.common.block;

import got.GOTDrinkVessel;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class GOTBlockAleHorn extends GOTBlockMug {
    public GOTBlockAleHorn(GOTDrinkVessel vessel) {
        super(BlockBehaviour.Properties.of().strength(0.0F).sound(SoundType.BONE_BLOCK).noOcclusion(), vessel, 5.0F, 12.0F);
    }
}
