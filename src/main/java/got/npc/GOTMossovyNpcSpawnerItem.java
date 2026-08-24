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

/** NBT-backed Mossovy spawn egg; Witcher trades return an owned contract form. */
public final class GOTMossovyNpcSpawnerItem extends Item {
    private static final String ROLE_TAG = "MossovyNpcRole";
    private static final String CONTRACT_TAG = "HiredContract";

    public GOTMossovyNpcSpawnerItem(Properties properties) { super(properties); }

    public static ItemStack createStack(MossovyNpcRole role) {
        ItemStack stack = new ItemStack(GOTItems.MOSSOVY_NPC_SPAWNER.get());
        stack.getOrCreateTag().putString(ROLE_TAG, role.id());
        return stack;
    }

    public static ItemStack createContract(MossovyNpcRole role) {
        ItemStack stack = createStack(role);
        stack.getOrCreateTag().putBoolean(CONTRACT_TAG, true);
        return stack;
    }

    public static MossovyNpcRole getRole(ItemStack stack) {
        return stack.hasTag() ? MossovyNpcRole.byId(stack.getTag().getString(ROLE_TAG))
                : MossovyNpcRole.MOSSOVY_MAN;
    }

    public static boolean isContract(ItemStack stack) {
        return stack.hasTag() && stack.getTag().getBoolean(CONTRACT_TAG);
    }

    public static int tint(ItemStack stack, int tintIndex) {
        return tintIndex == 0 ? 0x4B5C42 : 0xCBD5C0;
    }

    @Override public Component getName(ItemStack stack) {
        return Component.translatable(isContract(stack) ? "item.got.hired_contract.named"
                : "item.got.mossovy_npc_spawner.named", getRole(stack).displayName());
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level,
                                List<Component> tooltip, TooltipFlag flag) {
        MossovyNpcRole role = getRole(stack);
        tooltip.add(Component.translatable("item.got.mossovy_npc_spawner.faction")
                .withStyle(ChatFormatting.GRAY));
        if (isContract(stack)) {
            tooltip.add(Component.translatable("item.got.hired_contract.tooltip")
                    .withStyle(ChatFormatting.GOLD));
        } else {
            tooltip.add(Component.translatable("item.got.mossovy_npc_spawner.alignment",
                    role.alignmentBonus()).withStyle(ChatFormatting.DARK_RED));
            tooltip.add(Component.translatable("item.got.mossovy_npc_spawner.hint")
                    .withStyle(ChatFormatting.DARK_GRAY));
        }
    }

    @Override public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos().relative(context.getClickedFace());
        if (level instanceof ServerLevel serverLevel) {
            GOTMossovyNpcEntity npc = GOTEntities.MOSSOVY_NPC.get().create(serverLevel);
            if (npc == null) return InteractionResult.FAIL;
            MossovyNpcRole role = getRole(context.getItemInHand());
            npc.moveTo(pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D,
                    context.getRotation() + 180.0F, 0.0F);
            npc.prepareForSpawn(role, null, false, pos, 24, "");
            if (isContract(context.getItemInHand()) && context.getPlayer() != null) {
                npc.setHiredOwner(context.getPlayer());
            } else {
                npc.setPersistenceRequired();
            }
            if (!serverLevel.noCollision(npc)) return InteractionResult.FAIL;
            serverLevel.addFreshEntity(npc);
            if (context.getPlayer() == null || !context.getPlayer().getAbilities().instabuild) {
                context.getItemInHand().shrink(1);
            }
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }
}
