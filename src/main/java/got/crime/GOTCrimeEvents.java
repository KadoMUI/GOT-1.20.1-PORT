package got.crime;

import got.GOTMod;
import got.faction.GOTFaction;
import got.npc.GOTFactionNpc;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Mob;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid=GOTMod.MOD_ID,bus=Mod.EventBusSubscriber.Bus.FORGE)
public final class GOTCrimeEvents {
 private GOTCrimeEvents(){}
 @SubscribeEvent(priority=EventPriority.HIGH)
 public static void interact(PlayerInteractEvent.EntityInteract e){
   if(e.getHand()!= InteractionHand.MAIN_HAND||e.getLevel().isClientSide||!e.getEntity().isShiftKeyDown())return;
   if(e.getEntity() instanceof ServerPlayer p && e.getTarget() instanceof Mob m && e.getTarget() instanceof GOTFactionNpc n){
     if(GOTCrimeService.tryPickpocket(p,m,n)){e.setCancellationResult(InteractionResult.SUCCESS);e.setCanceled(true);}
   }
 }
 @SubscribeEvent
 public static void death(LivingDeathEvent e){
   if(e.getEntity().level().isClientSide)return;
   if(e.getEntity() instanceof GOTFactionNpc npc && e.getSource().getEntity() instanceof ServerPlayer p)
      GOTCrimeService.recordNpcKill(p,e.getEntity(),npc);
   if(e.getEntity() instanceof ServerPlayer target && e.getSource().getEntity() instanceof ServerPlayer hunter){
      for(GOTFaction f:GOTFaction.playableFactions()) if(GOTCrimeService.claimBounty(hunter,target,f)) break;
   }
 }
 @SubscribeEvent public static void serverTick(TickEvent.ServerTickEvent e){if(e.phase==TickEvent.Phase.END){var s=e.getServer();GOTCrimeData.get(s).tick(s.overworld().getGameTime());}}
}
