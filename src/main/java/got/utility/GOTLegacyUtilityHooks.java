package got.utility;

import got.GOTItems;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

/**
 * Small compatibility hooks for legacy utility behaviors that still need
 * individual content integration.
 */
public final class GOTLegacyUtilityHooks {
    private GOTLegacyUtilityHooks() {}

    public static void awardBountyTrophy(ServerPlayer player, LivingEntity target) {
        if (player == null || target == null) return;
        ItemStack trophy = new ItemStack(GOTItems.BOUNTY_TROPHY.get());
        if (!player.getInventory().add(trophy)) {
            player.drop(trophy, false);
        }
    }

    public static boolean canPickpocket(ServerPlayer player, LivingEntity target) {
        if (player == null || target == null || !target.isAlive()) return false;
        return player.distanceToSqr(target) <= 9.0D && !target.isAlliedTo(player);
    }
}
