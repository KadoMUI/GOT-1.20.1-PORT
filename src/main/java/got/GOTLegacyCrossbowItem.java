package got;

import got.achievement.GOTAchievementHooks;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

/** Vanilla 1.20.1 crossbow mechanics with the legacy GOT use achievement hook. */
public final class GOTLegacyCrossbowItem extends CrossbowItem {
    public GOTLegacyCrossbowItem(Properties props) { super(props); }
    @Override public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        boolean wasCharged = isCharged(stack);
        InteractionResultHolder<ItemStack> out = super.use(level, player, hand);
        if (wasCharged && !level.isClientSide && player instanceof ServerPlayer sp) GOTAchievementHooks.award(sp, "USE_CROSSBOW");
        return out;
    }
}
