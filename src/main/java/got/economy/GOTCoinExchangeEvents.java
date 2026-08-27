package got.economy;

import got.GOTCoinExchangeMenu;
import got.GOTMod;
import got.npc.GOTFactionNpc;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Coin exchange access for GOT traders.
 * Shift-right-click a GOT trader while holding any denomination coin.
 * HIGHEST priority deliberately takes precedence over the shift-right-click pickpocket action.
 */
@Mod.EventBusSubscriber(modid = GOTMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class GOTCoinExchangeEvents {
    private GOTCoinExchangeEvents() {}

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onTraderInteract(PlayerInteractEvent.EntityInteract event) {
        if (event.getHand() != InteractionHand.MAIN_HAND || event.getLevel().isClientSide) return;
        if (!(event.getEntity() instanceof ServerPlayer player) || !player.isShiftKeyDown()) return;
        if (GOTCoinValueService.valueOf(player.getMainHandItem()) <= 0) return;
        if (!(event.getTarget() instanceof AbstractVillager trader) || !(event.getTarget() instanceof GOTFactionNpc)) return;
        if (trader.isBaby() || !trader.isAlive() || trader.getOffers().isEmpty()) return;

        GOTCoinExchangeMenu.open(player, trader.getId());
        event.setCancellationResult(InteractionResult.SUCCESS);
        event.setCanceled(true);
    }
}
