package got;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FarmBlock;
import net.minecraftforge.registries.ForgeRegistries;

public class GOTSeedItem extends Item {
    private final ResourceLocation cropId;

    public GOTSeedItem(String cropId, Properties properties) {
        super(properties);
        this.cropId = ResourceLocation.fromNamespaceAndPath(GOTMod.MOD_ID, cropId);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        BlockPos soil = context.getClickedPos();
        BlockPos above = soil.above();
        Block crop = ForgeRegistries.BLOCKS.getValue(cropId);
        if (crop != null && context.getLevel().getBlockState(soil).getBlock() instanceof FarmBlock
                && context.getLevel().getBlockState(above).isAir()) {
            context.getLevel().setBlock(above, crop.defaultBlockState(), 3);
            if (context.getPlayer() == null || !context.getPlayer().getAbilities().instabuild) {
                context.getItemInHand().shrink(1);
            }
            return InteractionResult.sidedSuccess(context.getLevel().isClientSide);
        }
        return InteractionResult.PASS;
    }
}
