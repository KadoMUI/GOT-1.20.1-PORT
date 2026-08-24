package got;

import got.npc.hiring.command.GOTCommandSwordService;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.level.Level;

/**
 * Legacy Command Sword:
 * iron-tier base, effectively 1 point of weapon damage in the old mod.
 *
 * The main purpose is command designation, not combat.
 */
public final class GOTCommandSwordItem extends SwordItem {
    public GOTCommandSwordItem(Properties properties) {
        super(Tiers.IRON, 0, -2.4F, properties.stacksTo(1));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (!level.isClientSide && player instanceof ServerPlayer sp) {
            int count = GOTCommandSwordService.command(sp, stack);
            sp.displayClientMessage(
                Component.literal(count > 0
                    ? "Ordered " + count + " hired warrior" + (count == 1 ? "" : "s") + " to attack."
                    : "Command sword order cancelled."),
                true
            );
        }

        player.swing(hand, true);
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
    }

    @Override
    public boolean canBeDepleted() {
        return false;
    }
}
