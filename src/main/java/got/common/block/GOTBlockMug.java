package got.common.block;

import got.GOTDrinkVessel;
import got.PlacedDrinkVesselBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;

/** Modern rebuild of the original 1.7.10 GOTBlockMug base class. */
public class GOTBlockMug extends PlacedDrinkVesselBlock {
    public GOTBlockMug(BlockBehaviour.Properties properties) {
        this(properties, GOTDrinkVessel.MUG, 3.0F, 8.0F);
    }

    protected GOTBlockMug(BlockBehaviour.Properties properties, GOTDrinkVessel vessel, float width, float height) {
        super(properties, vessel, width, height);
    }

    public static BlockBehaviour.Properties woodenProperties() {
        return BlockBehaviour.Properties.of().strength(0.0F).sound(SoundType.WOOD).noOcclusion();
    }
}
