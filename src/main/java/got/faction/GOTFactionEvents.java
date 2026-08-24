package got.faction;

import got.GOTMod;
import got.npc.GOTFactionNpc;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = GOTMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class GOTFactionEvents {
    private GOTFactionEvents() {}

    @SubscribeEvent
    public static void onLivingAttack(LivingAttackEvent event) {
        if (event.getEntity().level().isClientSide) return;
        if (!(event.getSource().getEntity() instanceof LivingEntity attacker)) return;
        if (!GOTFactionRules.canNpcDamage(attacker, event.getEntity())) {
            event.setCanceled(true);
            if (attacker instanceof Mob mob && mob.getTarget() == event.getEntity()) mob.setTarget(null);
        }
    }

    @SubscribeEvent
    public static void onLivingDeath(LivingDeathEvent event) {
        if (event.getEntity().level().isClientSide) return;
        if (!(event.getEntity() instanceof GOTFactionNpc npc)) return;
        if (event.getSource().getEntity() instanceof ServerPlayer player) {
            GOTFactionService.onNpcKilled(player, event.getEntity(), npc);
        }
    }

    @SubscribeEvent
    public static void onClone(PlayerEvent.Clone event) {
        GOTFactionPlayerData.copyPersisted(event.getOriginal(), event.getEntity());
    }

    @SubscribeEvent
    public static void onLogin(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) GOTFactionService.sync(player);
    }

    @SubscribeEvent
    public static void onRespawn(PlayerEvent.PlayerRespawnEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) GOTFactionService.sync(player);
    }

    @SubscribeEvent
    public static void onDimensionChange(PlayerEvent.PlayerChangedDimensionEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) GOTFactionService.sync(player);
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END || !(event.player instanceof ServerPlayer player)) return;
        GOTFactionPlayerData data = GOTFactionPlayerData.get(player);
        int before = data.pledgeBreakCooldown();
        boolean changed = data.tickCooldowns();
        if (!changed) return;
        if (before > 0 && data.pledgeBreakCooldown() == 0) {
            player.displayClientMessage(Component.translatable("got.faction.join.cooldownExpired")
                    .withStyle(ChatFormatting.GOLD), false);
            GOTFactionService.sync(player);
        } else if (player.tickCount % 100 == 0) {
            GOTFactionService.sync(player);
        }
    }
}
