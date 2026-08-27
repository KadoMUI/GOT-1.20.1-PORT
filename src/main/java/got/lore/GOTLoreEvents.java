package got.lore;

import got.GOTMod;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = GOTMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class GOTLoreEvents {
    private GOTLoreEvents() {}

    @SubscribeEvent public static void rightClick(PlayerInteractEvent.RightClickItem event) {
        if (event.getLevel().isClientSide || !(event.getEntity() instanceof ServerPlayer player)) return;
        if (GOTLoreBookService.isLoreBook(event.getItemStack()))
            GOTLoreDiscoveryService.discover(player, GOTLoreBookService.loreId(event.getItemStack()));
    }

    @SubscribeEvent public static void clone(PlayerEvent.Clone event) {
        GOTLorePlayerData.copyPersisted(event.getOriginal(), event.getEntity());
    }
}
