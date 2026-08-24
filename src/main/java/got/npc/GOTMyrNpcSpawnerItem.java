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

/** NBT-backed spawn egg for the complete Myr catalogue. */
public final class GOTMyrNpcSpawnerItem extends Item {
    private static final String ROLE_TAG = "MyrNpcRole";

    public GOTMyrNpcSpawnerItem(Properties properties) {
        super(properties);
    }

    public static ItemStack createStack(MyrNpcRole role) {
        ItemStack stack = new ItemStack(GOTItems.MYR_NPC_SPAWNER.get());
        stack.getOrCreateTag().putString(ROLE_TAG, role.id());
        return stack;
    }

    public static MyrNpcRole getRole(ItemStack stack) {
        return stack.hasTag()
                ? MyrNpcRole.byId(stack.getTag().getString(ROLE_TAG))
                : MyrNpcRole.MYR_MAN;
    }

    public static int tint(ItemStack stack, int tintIndex) {
        MyrNpcRole role = getRole(stack);
        if (tintIndex == 0) return 0x3F3F3F;
        return role == MyrNpcRole.MYR_SLAVE ? 0x9A6A45 : 0xE8C547;
    }

    @Override
    public Component getName(ItemStack stack) {
        return Component.translatable("item.got.myr_npc_spawner.named",
                getRole(stack).displayName());
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level,
                                List<Component> tooltip, TooltipFlag flag) {
        MyrNpcRole role = getRole(stack);
        tooltip.add(Component.translatable("item.got.myr_npc_spawner.faction")
                .withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.translatable("item.got.myr_npc_spawner.alignment",
                role.alignmentBonus()).withStyle(ChatFormatting.DARK_RED));
        tooltip.add(Component.translatable("item.got.myr_npc_spawner.hint")
                .withStyle(ChatFormatting.DARK_GRAY));
        if (role.legendary()) {
            tooltip.add(Component.translatable("item.got.myr_npc_spawner.legendary")
                    .withStyle(ChatFormatting.GOLD));
        }
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos().relative(context.getClickedFace());
        if (level instanceof ServerLevel serverLevel) {
            GOTMyrNpcEntity npc = GOTEntities.MYR_NPC.get().create(serverLevel);
            if (npc == null) return InteractionResult.FAIL;
            MyrNpcRole role = getRole(context.getItemInHand());
            npc.moveTo(pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D,
                    context.getRotation() + 180.0F, 0.0F);
            npc.prepareForSpawn(role, null, false, pos, role.legendary() ? 16 : 24, "");
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
