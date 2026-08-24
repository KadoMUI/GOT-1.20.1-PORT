package got.speech;

import got.GOTMod;
import got.npc.GOTFactionNpc;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.UUID;

/**
 * Legacy speech behavior integration.
 *
 * The 1.7.10 mod did NOT continuously generate random NPC-to-NPC chatter.
 * Ordinary autonomous speech was limited to a hostile bark when an NPC newly
 * acquired a player target. Direct dialogue happened through player interaction.
 */
@Mod.EventBusSubscriber(modid = GOTMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class GOTSpeechEvents {
    private static final double HOSTILE_CROWD_RADIUS = 16.0D;
    private static final int HOSTILE_CROWD_LIMIT = 5;

    private GOTSpeechEvents() {}

    @SubscribeEvent
    public static void addReloadListener(AddReloadListenerEvent event) {
        event.addListener(new GOTSpeechBankRegistry());
    }

    /**
     * Faithful interaction rules from GOTEntityNPC#interact:
     *
     * - main hand
     * - server side
     * - living NPC
     * - 40-tick speech cooldown must have elapsed
     * - NPC cannot already be fighting somebody
     * - quest/hiring handlers get first refusal because this listener runs at
     *   LOWEST and does not receive canceled interaction events
     */
    @SubscribeEvent(priority = EventPriority.LOWEST, receiveCanceled = false)
    public static void onEntityInteract(PlayerInteractEvent.EntityInteract event) {
        if (event.getHand() != InteractionHand.MAIN_HAND || event.getLevel().isClientSide) return;
        if (!(event.getEntity() instanceof ServerPlayer player)) return;
        if (!(event.getTarget() instanceof GOTFactionNpc)) return;

        Entity target = event.getTarget();
        if (!target.isAlive()) return;
        if (!GOTSpeechBehaviorData.canSpeak(target)) return;

        // Original GOTEntityNPC only fell through to ordinary speech while it
        // had no attack target.
        if (target instanceof Mob mob && mob.getTarget() != null) return;

        GOTSpeechService.speakDefault(target, player);
    }

    /**
     * Modern equivalent of GOTEntityNPC#setAttackTarget(..., speak=true).
     *
     * Legacy conditions recovered from 24.08.29:
     * - target must be a player
     * - target must be newly acquired
     * - NPC must have line of sight
     * - 1 in 3 chance to bark
     * - within a 16-block cube, at most five other living NPCs may already be
     *   attacking that same player; this suppresses battle speech spam
     */
    @SubscribeEvent
    public static void onLivingTick(LivingEvent.LivingTickEvent event) {
        if (!(event.getEntity() instanceof PathfinderMob mob)) return;
        if (mob.level().isClientSide) return;
        if (!(mob instanceof GOTFactionNpc)) return;

        GOTSpeechBehaviorData.tick(mob);

        if (!(mob.getTarget() instanceof ServerPlayer target) || !target.isAlive()) {
            GOTSpeechBehaviorData.setPreviousTarget(mob, null);
            return;
        }

        UUID targetId = target.getUUID();
        UUID previous = GOTSpeechBehaviorData.previousTarget(mob);
        if (targetId.equals(previous)) return;

        // Record immediately so a failed/random-suppressed bark is not retried
        // every tick. The original only considered speech when the target was
        // changed, not continuously while combat continued.
        GOTSpeechBehaviorData.setPreviousTarget(mob, targetId);

        if (!mob.getSensing().hasLineOfSight(target)) return;
        if (mob.getRandom().nextInt(3) != 0) return;

        AABB area = mob.getBoundingBox().inflate(HOSTILE_CROWD_RADIUS);
        int attackers = 0;

        for (PathfinderMob other : mob.level().getEntitiesOfClass(PathfinderMob.class, area)) {
            if (!(other instanceof GOTFactionNpc)) continue;
            if (!other.isAlive()) continue;
            if (other.getTarget() != target) continue;

            attackers++;
            if (attackers > HOSTILE_CROWD_LIMIT) return;
        }

        GOTSpeechService.speakDefault(mob, target);
    }
}
