package got.pact;

import got.npc.hiring.GOTHiredData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.UUID;

/** Legacy fellowship friendly-fire behavior, now under Pacts. */
@Mod.EventBusSubscriber(modid = "got", bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class GOTPactEvents {
    private GOTPactEvents() {}

    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        Entity attacker = event.getSource().getEntity();
        Entity victim = event.getEntity();
        if (attacker == null || attacker == victim) return;

        UUID attackerPlayer = controllingPlayer(attacker);
        UUID victimPlayer = controllingPlayer(victim);
        if (attackerPlayer == null || victimPlayer == null || attackerPlayer.equals(victimPlayer)) return;

        net.minecraft.server.MinecraftServer server = event.getEntity().getServer();
        if (server == null) return;

        GOTPactService.forPlayer(server, attackerPlayer).ifPresent(pact -> {
            if (!pact.contains(victimPlayer)) return;

            boolean directPlayers = attacker instanceof ServerPlayer && victim instanceof ServerPlayer;
            if (directPlayers && pact.preventPvp()) {
                event.setCanceled(true);
                return;
            }

            boolean hiredInvolved = GOTHiredData.isHired(attacker) || GOTHiredData.isHired(victim);
            if (hiredInvolved && pact.preventHiredFriendlyFire()) {
                event.setCanceled(true);
            }
        });
    }

    private static UUID controllingPlayer(Entity entity) {
        if (entity instanceof ServerPlayer player) return player.getUUID();
        if (entity instanceof PathfinderMob mob) return GOTHiredData.owner(mob);
        return null;
    }
}
