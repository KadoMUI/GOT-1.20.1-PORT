package got.common.fasttravel;

import got.GOTMod;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = GOTMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class GOTFastTravelEvents {
    private GOTFastTravelEvents() {}

    @SubscribeEvent public static void login(PlayerEvent.PlayerLoggedInEvent e) {
        if (e.getEntity() instanceof ServerPlayer p) GOTFastTravelManager.sync(p);
    }
    @SubscribeEvent public static void respawn(PlayerEvent.PlayerRespawnEvent e) {
        if (e.getEntity() instanceof ServerPlayer p) GOTFastTravelManager.sync(p);
    }
    @SubscribeEvent public static void dimension(PlayerEvent.PlayerChangedDimensionEvent e) {
        if (e.getEntity() instanceof ServerPlayer p) GOTFastTravelManager.sync(p);
    }
    @SubscribeEvent public static void clone(PlayerEvent.Clone e) {
        GOTFastTravelData.copyPersisted(e.getOriginal(), e.getEntity());
    }
    @SubscribeEvent public static void tick(TickEvent.PlayerTickEvent e) {
        if (e.phase == TickEvent.Phase.END && e.player instanceof ServerPlayer p) GOTFastTravelManager.tick(p);
    }
    @SubscribeEvent public static void hurt(LivingHurtEvent e) {
        if (e.getEntity() instanceof ServerPlayer p) GOTFastTravelManager.cancelForDamage(p);
    }
}
