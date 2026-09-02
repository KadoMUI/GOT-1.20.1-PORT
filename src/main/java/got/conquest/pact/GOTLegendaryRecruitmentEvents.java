package got.conquest.pact;

import got.GOTMod;
import got.conquest.GOTConquestSavedData;
import got.pact.GOTPactSavedData;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Interaction bridge for political Legendary recruitment.
 *
 * Sneak + main-hand interaction with an empty hand is intentionally distinct
 * from normal quest, trade and hired-unit interaction.
 */
@Mod.EventBusSubscriber(modid = GOTMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class GOTLegendaryRecruitmentEvents {
    private GOTLegendaryRecruitmentEvents() {}

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onEntityInteract(PlayerInteractEvent.EntityInteract event) {
        if (event.getHand() != InteractionHand.MAIN_HAND || event.getLevel().isClientSide) return;
        if (!(event.getEntity() instanceof ServerPlayer player)) return;
        if (!player.isShiftKeyDown() || !player.getMainHandItem().isEmpty()) return;
        if (!GOTLegendaryNpcService.isLegendary(event.getTarget())) return;

        GOTLegendaryRecruitmentService.RecruitResult result =
                GOTLegendaryRecruitmentService.recruit(player, event.getTarget());
        if (!result.success()) player.sendSystemMessage(Component.literal(result.error()));

        // An explicit recruitment gesture gets first refusal over quests/trades.
        event.setCancellationResult(InteractionResult.SUCCESS);
        event.setCanceled(true);
    }

    @SubscribeEvent
    public static void onEntityJoin(EntityJoinLevelEvent event) {
        if (!(event.getLevel() instanceof ServerLevel level) || !GOTLegendaryNpcService.isLegendary(event.getEntity())) return;
        String roleId = GOTLegendaryNpcService.roleId(event.getEntity());
        if (roleId.isBlank()) return;
        GOTConquestSavedData.get(level.getServer()).legendaryMembership(roleId)
                .ifPresent(membership -> GOTLegendaryRecruitmentService.syncEntityTag(event.getEntity(), membership));
    }

    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase != TickEvent.Phase.END || event.getServer().getTickCount() % 200 != 0) return;
        GOTPactSavedData pacts = GOTPactSavedData.get(event.getServer());
        GOTConquestSavedData conquest = GOTConquestSavedData.get(event.getServer());
        int cleared = conquest.clearMissingPlayerPactAssignments(
                pactId -> pacts.pact(pactId).isPresent(),
                event.getServer().overworld().getGameTime());
        if (cleared > 0) {
            GOTMod.LOGGER.info("Released {} Legendary NPC Pact assignments because their player Pact no longer exists", cleared);
        }
    }
}
