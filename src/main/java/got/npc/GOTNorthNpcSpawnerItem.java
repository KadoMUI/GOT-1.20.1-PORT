package got.npc;

import got.GOTEntities;
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

/** One NBT-backed spawn egg exposes every North NPC variant without 45 duplicate registry items. */
public final class GOTNorthNpcSpawnerItem extends Item {
    private static final String ROLE_TAG = "NorthNpcRole";

    public GOTNorthNpcSpawnerItem(Properties properties) {
        super(properties);
    }

    public static ItemStack createStack(NorthNpcRole role) {
        ItemStack stack = new ItemStack(got.GOTItems.NORTH_NPC_SPAWNER.get());
        stack.getOrCreateTag().putString(ROLE_TAG, role.id());
        return stack;
    }

    public static NorthNpcRole getRole(ItemStack stack) {
        return stack.hasTag() ? NorthNpcRole.byId(stack.getTag().getString(ROLE_TAG)) : NorthNpcRole.NORTH_MAN;
    }

    public static int tint(ItemStack stack, int tintIndex) {
        NorthNpcRole role = getRole(stack);
        if (tintIndex == 0) return role.legendary() ? 0x929292 : 0xD4CFB7;
        return 0xD4CFB7;
    }

    @Override
    public Component getName(ItemStack stack) {
        return Component.translatable("item.got.north_npc_spawner.named", getRole(stack).displayName());
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level,
                                List<Component> tooltip, TooltipFlag flag) {
        NorthNpcRole role = getRole(stack);
        tooltip.add(Component.translatable("item.got.north_npc_spawner.faction").withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.translatable("item.got.north_npc_spawner.hint").withStyle(ChatFormatting.DARK_GRAY));
        if (role.legendary()) {
            tooltip.add(Component.translatable("item.got.north_npc_spawner.legendary").withStyle(ChatFormatting.GOLD));
        }
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos().relative(context.getClickedFace());
        if (level instanceof ServerLevel serverLevel) {
            GOTNorthNpcEntity npc = GOTEntities.NORTH_NPC.get().create(serverLevel);
            if (npc == null) return InteractionResult.FAIL;
            NorthNpcRole role = getRole(context.getItemInHand());
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
