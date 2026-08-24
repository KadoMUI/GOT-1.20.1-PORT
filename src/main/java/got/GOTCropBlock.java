package got;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.registries.RegistryObject;

public class GOTCropBlock extends CropBlock {
    private final RegistryObject<Item> seed;
    public GOTCropBlock(BlockBehaviour.Properties properties, RegistryObject<Item> seed) {
        super(properties);
        this.seed = seed;
    }
    @Override
    protected net.minecraft.world.level.ItemLike getBaseSeedId() {
        return seed.get();
    }
}
