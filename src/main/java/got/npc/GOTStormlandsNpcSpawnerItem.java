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

/** NBT-backed spawn egg for every ordinary and named Stormlands NPC. */
public final class GOTStormlandsNpcSpawnerItem extends Item {
    private static final String ROLE_TAG = "StormlandsNpcRole";

    public GOTStormlandsNpcSpawnerItem(Properties properties) {
        super(properties);
    }

    public static ItemStack createStack(StormlandsNpcRole role) {
        ItemStack stack = new ItemStack(GOTItems.STORMLANDS_NPC_SPAWNER.get());
        stack.getOrCreateTag().putString(ROLE_TAG, role.id());
        return stack;
    }

    public static StormlandsNpcRole getRole(ItemStack stack) {
        return stack.hasTag()
                ? StormlandsNpcRole.byId(stack.getTag().getString(ROLE_TAG))
                : StormlandsNpcRole.STORMLANDS_MAN;
    }

    public static int tint(ItemStack stack, int tintIndex) {
        StormlandsNpcRole role = getRole(stack);
        if (tintIndex == 0) return role.legendary() ? 0x8D7B65 : 0x304F32;
        return 0xE8C547;
    }

    @Override
    public Component getName(ItemStack stack) {
        return Component.translatable("item.got.stormlands_npc_spawner.named",
                getRole(stack).displayName());
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level,
                                List<Component> tooltip, TooltipFlag flag) {
        StormlandsNpcRole role = getRole(stack);
        tooltip.add(Component.translatable("item.got.stormlands_npc_spawner.faction")
                .withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.translatable("item.got.stormlands_npc_spawner.alignment",
                role.alignmentBonus()).withStyle(ChatFormatting.DARK_RED));
        tooltip.add(Component.translatable("item.got.stormlands_npc_spawner.hint")
                .withStyle(ChatFormatting.DARK_GRAY));
        if (role.legendary()) {
            tooltip.add(Component.translatable("item.got.stormlands_npc_spawner.legendary")
                    .withStyle(ChatFormatting.GOLD));
        }
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos().relative(context.getClickedFace());
        if (level instanceof ServerLevel serverLevel) {
            GOTStormlandsNpcEntity npc = GOTEntities.STORMLANDS_NPC.get().create(serverLevel);
            if (npc == null) return InteractionResult.FAIL;
            StormlandsNpcRole role = getRole(context.getItemInHand());
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
