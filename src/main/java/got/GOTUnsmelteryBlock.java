package got;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;

import java.util.List;
import javax.annotation.Nullable;

/** Recipe-independent equipment recycler, retaining the legacy durability loss and material recovery role. */
public class GOTUnsmelteryBlock extends Block implements EntityBlock {
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty LIT = BlockStateProperties.LIT;

    public GOTUnsmelteryBlock(Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(LIT, false));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player,
                                 InteractionHand hand, BlockHitResult hit) {
        ItemStack input = player.getItemInHand(hand);
        ItemStack material = findRepairMaterial(input);
        int units = resourceUnits(input);
        if (material.isEmpty() || units <= 0) {
            if (!level.isClientSide) {
                player.displayClientMessage(Component.translatable("message.got.unsmeltery_invalid"), true);
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }

        if (!level.isClientSide) {
            float remaining = input.isDamageableItem()
                    ? 1.0F - (float) input.getDamageValue() / input.getMaxDamage()
                    : 1.0F;
            int recovered = Math.max(1, (int) Math.floor(units * remaining * 0.75F));
            ItemStack output = material.copyWithCount(recovered);
            input.shrink(1);
            if (!player.getInventory().add(output)) {
                popResource(level, pos, output);
            }
            level.setBlock(pos, state.setValue(LIT, true), 3);
            level.scheduleTick(pos, this, 40);
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    private static ItemStack findRepairMaterial(ItemStack input) {
        Ingredient ingredient;
        if (input.getItem() instanceof TieredItem tiered) {
            ingredient = tiered.getTier().getRepairIngredient();
        } else if (input.getItem() instanceof ArmorItem armor) {
            ingredient = armor.getMaterial().getRepairIngredient();
        } else {
            return ItemStack.EMPTY;
        }

        List<Item> candidates = List.of(
                GOTItems.VALYRIAN_STEEL_INGOT.get(), GOTItems.ALLOY_STEEL_INGOT.get(),
                GOTItems.BRONZE_INGOT.get(), GOTItems.SILVER_INGOT.get(), GOTItems.TIN_INGOT.get(),
                GOTItems.OBSIDIAN_SHARD.get(), Items.NETHERITE_INGOT, Items.DIAMOND,
                Items.GOLD_INGOT, Items.IRON_INGOT, Items.COPPER_INGOT, Items.FLINT,
                Items.COBBLESTONE, Items.OAK_PLANKS, Items.STRING, Items.LEATHER);
        for (Item candidate : candidates) {
            ItemStack stack = new ItemStack(candidate);
            if (ingredient.test(stack)) {
                return stack;
            }
        }
        return ItemStack.EMPTY;
    }

    private static int resourceUnits(ItemStack stack) {
        if (stack.getItem() instanceof ArmorItem armor) {
            return switch (armor.getType()) {
                case HELMET -> 5;
                case CHESTPLATE -> 8;
                case LEGGINGS -> 7;
                case BOOTS -> 4;
            };
        }
        if (stack.getItem() instanceof PickaxeItem || stack.getItem() instanceof AxeItem) return 3;
        if (stack.getItem() instanceof SwordItem || stack.getItem() instanceof HoeItem) return 2;
        if (stack.getItem() instanceof ShovelItem) return 1;
        return 0;
    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (state.getValue(LIT)) {
            level.setBlock(pos, state.setValue(LIT, false), 3);
        }
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new GOTLegacyDecorBlockEntity(pos, state);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.INVISIBLE;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, LIT);
    }
}
