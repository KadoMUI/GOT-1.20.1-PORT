package got.npc.hiring;

import got.network.GOTNetwork;
import got.mount.GOTMountEntity;
import got.network.hiring.S2CHiredNpcGuiPacket;
import got.npc.hiring.ai.GOTHiredCombatPolicy;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;

/**
 * Global integration hooks so every regional GOT NPC can use the hiring system
 * without duplicating code in 30+ entity classes.
 */
@Mod.EventBusSubscriber(modid = "got", bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class GOTHiringEvents {
    private GOTHiringEvents() {}

    @SubscribeEvent
    public static void onReloadListeners(AddReloadListenerEvent event) {
        event.addListener(new GOTHiringReloadListener());
    }

    @SubscribeEvent
    public static void onEntityInteract(PlayerInteractEvent.EntityInteract event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;
        if (event.getHand() != net.minecraft.world.InteractionHand.MAIN_HAND) return;

        var target = event.getTarget();
        boolean hired = GOTHiredData.isHired(target);
        boolean hasHireDefinition = GOTHiringRuleService.definitionFor(target) != null;
        if (!hired && !hasHireDefinition) return;

        // Non-owners may inspect an unhired unit, but may not manage somebody
        // else's hired unit.
        if (hired && !GOTHiredData.isOwner(target, player.getUUID())) {
            return;
        }

        GOTNetwork.CHANNEL.send(
                PacketDistributor.PLAYER.with(() -> player),
                new S2CHiredNpcGuiPacket(GOTHireSnapshotFactory.build(player, target))
        );

        event.setCanceled(true);
        event.setCancellationResult(InteractionResult.SUCCESS);
    }

    @SubscribeEvent
    public static void onHiredDeath(LivingDeathEvent event) {
        if (!(event.getEntity() instanceof PathfinderMob mob)) return;
        if (mob.level().isClientSide) return;
        if (mob.getVehicle() instanceof GOTMountEntity mount && mount.belongsToNpc()) {
            // NPC-owned cavalry mounts are part of the unit. Do not leave a
            // permanently persistent orphan mount behind when its rider dies.
            mob.stopRiding();
            mount.discard();
        }
    }

    @SubscribeEvent
    public static void onLivingTick(LivingEvent.LivingTickEvent event) {
        if (!(event.getEntity() instanceof PathfinderMob mob)) return;
        if (mob.level().isClientSide || !GOTHiredData.isHired(mob)) return;

        boolean consumed = GOTHiringRuntimeHooks.beforeHiredMovement(mob);
        if (!consumed) {
            GOTHiredAi.tick(mob);
        }

        GOTHiringRuntimeHooks.periodicEquipmentSafetySync(mob);
    }
}
