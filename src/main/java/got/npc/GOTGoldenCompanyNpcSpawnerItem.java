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

/** Spawn egg and purchased contract for unaligned Golden Company units. */
public final class GOTGoldenCompanyNpcSpawnerItem extends Item {
    private static final String ROLE_TAG = "GoldenCompanyNpcRole";
    private static final String CONTRACT_TAG = "HiredContract";

    public GOTGoldenCompanyNpcSpawnerItem(Properties properties) { super(properties); }

    public static ItemStack createStack(GoldenCompanyNpcRole role) {
        ItemStack stack = new ItemStack(GOTItems.GOLDEN_COMPANY_NPC_SPAWNER.get());
        stack.getOrCreateTag().putString(ROLE_TAG, role.id());
        return stack;
    }

    public static ItemStack createContract(GoldenCompanyNpcRole role) {
        ItemStack stack = createStack(role);
        stack.getOrCreateTag().putBoolean(CONTRACT_TAG, true);
        return stack;
    }

    public static GoldenCompanyNpcRole getRole(ItemStack stack) {
        return stack.hasTag() ? GoldenCompanyNpcRole.byId(stack.getTag().getString(ROLE_TAG))
                : GoldenCompanyNpcRole.GOLDEN_COMPANY_WARRIOR;
    }
    public static boolean isContract(ItemStack stack) {
        return stack.hasTag() && stack.getTag().getBoolean(CONTRACT_TAG);
    }
    public static int tint(ItemStack stack, int tintIndex) {
        return tintIndex == 0 ? 0xD4AF37 : 0x181818;
    }

    @Override public Component getName(ItemStack stack) {
        return Component.translatable(isContract(stack) ? "item.got.hired_contract.named"
                : "item.got.golden_company_npc_spawner.named", getRole(stack).displayName());
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level,
                                List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable("item.got.golden_company_npc_spawner.faction")
                .withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.translatable(isContract(stack)
                        ? "item.got.hired_contract.tooltip"
                        : "item.got.golden_company_npc_spawner.hint")
                .withStyle(isContract(stack) ? ChatFormatting.GOLD : ChatFormatting.DARK_GRAY));
        if (getRole(stack).legendary()) {
            tooltip.add(Component.translatable("item.got.golden_company_npc_spawner.legendary")
                    .withStyle(ChatFormatting.GOLD));
        }
    }

    @Override public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos().relative(context.getClickedFace());
        if (level instanceof ServerLevel serverLevel) {
            GOTGoldenCompanyNpcEntity npc = GOTEntities.GOLDEN_COMPANY_NPC.get().create(serverLevel);
            if (npc == null) return InteractionResult.FAIL;
            GoldenCompanyNpcRole role = getRole(context.getItemInHand());
            npc.moveTo(pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D,
                    context.getRotation() + 180.0F, 0.0F);
            npc.prepareForSpawn(role, pos, role.legendary() ? 16 : 24, "");
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
