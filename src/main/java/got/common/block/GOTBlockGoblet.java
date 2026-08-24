package got.common.block;

import got.GOTDrinkVessel;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;

/** Modern rebuild of GOTBlockGoblet and its original nested material classes. */
public abstract class GOTBlockGoblet extends GOTBlockMug {
    protected GOTBlockGoblet(GOTDrinkVessel vessel, SoundType sound) {
        super(BlockBehaviour.Properties.of().strength(0.0F).sound(sound).noOcclusion(), vessel, 2.5F, 9.0F);
    }

    public static final class Gold extends GOTBlockGoblet {
        public Gold() { super(GOTDrinkVessel.GOLD_GOBLET, SoundType.METAL); }
    }
    public static final class Silver extends GOTBlockGoblet {
        public Silver() { super(GOTDrinkVessel.SILVER_GOBLET, SoundType.METAL); }
    }
    public static final class Bronze extends GOTBlockGoblet {
        public Bronze() { super(GOTDrinkVessel.BRONZE_GOBLET, SoundType.METAL); }
    }
    public static final class Copper extends GOTBlockGoblet {
        public Copper() { super(GOTDrinkVessel.COPPER_GOBLET, SoundType.METAL); }
    }
    public static final class Valyrian extends GOTBlockGoblet {
        public Valyrian() { super(GOTDrinkVessel.VALYRIAN_GOBLET, SoundType.METAL); }
    }
    public static final class Wood extends GOTBlockGoblet {
        public Wood() { super(GOTDrinkVessel.WOODEN_GOBLET, SoundType.WOOD); }
    }
}
