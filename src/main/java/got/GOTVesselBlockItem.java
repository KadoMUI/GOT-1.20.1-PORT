package got;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;

import java.util.function.Consumer;

/** Block item for an empty legacy vessel, rendered through its original Java model. */
public class GOTVesselBlockItem extends BlockItem {
    private final GOTDrinkVessel vessel;

    public GOTVesselBlockItem(Block block, Properties properties, GOTDrinkVessel vessel) {
        super(block, properties);
        this.vessel = vessel;
    }

    public GOTDrinkVessel vessel() {
        return vessel;
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(GOTVesselItemClientExtensions.INSTANCE);
    }
}
