package got.npc.hiring;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;

/**
 * One atomic server-side hire transaction.
 */
public final class GOTHiringTransactionService {
    private GOTHiringTransactionService() {}

    public static boolean hire(ServerPlayer player, Entity entity) {
        GOTHiringRuleService.Result result = GOTHiringRuleService.evaluate(player, entity);
        if (!result.allowed()) {
            player.displayClientMessage(result.message(), false);
            return false;
        }

        if (!GOTHiringCoinService.take(player, result.price())) {
            player.displayClientMessage(Component.literal("You cannot afford this unit."), false);
            return false;
        }

        GOTHiredData.hire(entity, player.getUUID(), result.definition().task());

        player.displayClientMessage(
            Component.literal("Hired " + entity.getDisplayName().getString()
                + " for " + result.price() + " coin value."),
            false
        );
        return true;
    }
}
