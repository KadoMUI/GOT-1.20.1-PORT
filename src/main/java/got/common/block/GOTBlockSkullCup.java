package got.common.block;

import got.GOTDrinkVessel;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class GOTBlockSkullCup extends GOTBlockMug {
    public GOTBlockSkullCup() {
        super(BlockBehaviour.Properties.of().strength(0.0F).sound(SoundType.BONE_BLOCK).noOcclusion(), GOTDrinkVessel.SKULL_CUP, 4.0F, 10.0F);
    }
}
