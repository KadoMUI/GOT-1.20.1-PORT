package got;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;

import java.util.function.Consumer;

/** Inventory item that retains the original machine's Java model. */
public final class GOTLegacyMachineBlockItem extends BlockItem {
    public enum Kind { BEACON, UNSMELTERY }

    private final Kind kind;

    public GOTLegacyMachineBlockItem(Block block, Properties properties, Kind kind) {
        super(block, properties);
        this.kind = kind;
    }

    public Kind kind() {
        return kind;
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(GOTLegacyMachineItemClientExtensions.INSTANCE);
    }
}
