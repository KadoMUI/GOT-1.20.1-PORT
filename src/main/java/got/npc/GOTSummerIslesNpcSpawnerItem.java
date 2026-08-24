package got.npc;

import got.GOTEntities;
import got.GOTItems;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

/** NBT-backed spawn egg for all twenty Summer Isles roles. */
public final class GOTSummerIslesNpcSpawnerItem extends Item {
    private static final String ROLE_TAG = "SummerNpcRole";

    public GOTSummerIslesNpcSpawnerItem(Properties properties) { super(properties); }

    public static ItemStack createStack(SummerIslesNpcRole role) {
        ItemStack stack = new ItemStack(GOTItems.SUMMER_ISLES_NPC_SPAWNER.get());
        stack.getOrCreateTag().putString(ROLE_TAG, role.id());
        return stack;
    }

    public static SummerIslesNpcRole getRole(ItemStack stack) {
        return stack.hasTag() ? SummerIslesNpcRole.byId(stack.getTag().getString(ROLE_TAG))
                : SummerIslesNpcRole.SUMMER_MAN;
    }

    public static int tint(ItemStack stack, int tintIndex) {
        return tintIndex == 0 ? 0x911657 : 0xF0D778;
    }

    @Override public Component getName(ItemStack stack) {
        return Component.translatable("item.got.summer_isles_npc_spawner.named", getRole(stack).displayName());
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable("item.got.summer_isles_npc_spawner.faction").withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.translatable("item.got.summer_isles_npc_spawner.alignment",
                getRole(stack).alignmentBonus()).withStyle(ChatFormatting.DARK_RED));
        tooltip.add(Component.translatable("item.got.summer_isles_npc_spawner.hint").withStyle(ChatFormatting.DARK_GRAY));
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos().relative(context.getClickedFace());
        if (level instanceof ServerLevel serverLevel) {
            GOTSummerIslesNpcEntity npc = GOTEntities.SUMMER_ISLES_NPC.get().create(serverLevel);
            if (npc == null) return InteractionResult.FAIL;
            npc.moveTo(pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D,
                    context.getRotation() + 180.0F, 0.0F);
            npc.prepareForSpawn(getRole(context.getItemInHand()), null, false, pos, 24, "");
            npc.setPersistenceRequired();
            if (!serverLevel.noCollision(npc)) return InteractionResult.FAIL;
            serverLevel.addFreshEntity(npc);
            if (context.getPlayer() == null || !context.getPlayer().getAbilities().instabuild) {
                context.getItemInHand().shrink(1);
            }
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }
}
