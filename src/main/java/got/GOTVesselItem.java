package got;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;

import java.util.function.Consumer;

/** Empty legacy vessel item with placeable behavior and the custom 3D item renderer. */
public class GOTVesselItem extends Item {
    private final GOTDrinkVessel vessel;

    public GOTVesselItem(GOTDrinkVessel vessel, Properties properties) {
        super(properties);
        this.vessel = vessel;
    }

    public GOTDrinkVessel vessel() {
        return vessel;
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        if (!vessel.isPlaceable() || context.getClickedFace() != net.minecraft.core.Direction.UP) {
            return InteractionResult.PASS;
        }

        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos().above();
        if (!level.getBlockState(pos).canBeReplaced()) return InteractionResult.FAIL;

        BlockState state = GOTDrinkItem.placedBlockFor(vessel).defaultBlockState();
        Player player = context.getPlayer();
        if (player != null) {
            state = state.setValue(PlacedDrinkVesselBlock.FACING, player.getDirection().getOpposite());
        }
        if (!state.canSurvive(level, pos)) return InteractionResult.FAIL;

        if (!level.isClientSide) {
            level.setBlock(pos, state, 3);
            if (level.getBlockEntity(pos) instanceof PlacedDrinkVesselBlockEntity placed) {
                placed.setEmptyVessel(vessel);
            }
            ItemStack stack = context.getItemInHand();
            if (player == null || !player.getAbilities().instabuild) stack.shrink(1);
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(GOTVesselItemClientExtensions.INSTANCE);
    }
}
