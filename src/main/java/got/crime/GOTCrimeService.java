package got.crime;

import got.GOTItems;
import got.achievement.GOTAchievementHooks;
import got.faction.GOTFaction;
import got.faction.GOTFactionControl;
import got.faction.GOTFactionService;
import got.npc.GOTFactionNpc;
import got.npc.hiring.GOTHiredData;
import got.quest.GOTQuestService;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public final class GOTCrimeService {
    private static final String PICKPOCKETED = "GOTPickpocketedNPCs";
    private GOTCrimeService() {}

    /** Legacy bounty rule: direct, non-creative player kill inside the slain faction's defined control zone. */
    public static void recordNpcKill(ServerPlayer player, LivingEntity victim, GOTFactionNpc npc) {
        GOTFaction faction=npc.getFaction();
        if (!faction.isPlayable() || faction==GOTFaction.UNALIGNED || player.isCreative()) return;
        if (!GOTFactionControl.isInInfluence(player,faction)) return;
        GOTCrimeData.get(player.server).recordKill(faction,player.getUUID());
    }

    public static List<UUID> bountyTargets(ServerPlayer requester, GOTFaction faction, int minKills) {
        return GOTCrimeData.get(requester.server).targets(faction,Math.max(1,minKills));
    }

    /** Marks a faction bounty claimed. Quest/content code can call this after validating its target. */
    public static boolean claimBounty(ServerPlayer hunter, ServerPlayer target, GOTFaction faction) {
        GOTCrimeData data=GOTCrimeData.get(hunter.server);
        if (!data.hasBounty(faction,target.getUUID(),1)) return false;
        data.recordBountyClaimed(faction,target.getUUID());
        ItemStack trophy=new ItemStack(GOTItems.BOUNTY_TROPHY.get());
        if (!hunter.getInventory().add(trophy)) hunter.drop(trophy,false);
        GOTQuestService.triggerEvent(hunter,new net.minecraft.resources.ResourceLocation("got","bounty_kill"),1);
        GOTAchievementHooks.award(hunter, "KILL_HUNTING_PLAYER");
        target.displayClientMessage(Component.translatable("got.chat.bountyKilled1",hunter.getDisplayName(),faction.displayName()).withStyle(ChatFormatting.RED),false);
        return true;
    }

    public static boolean tryPickpocket(ServerPlayer player, Mob target, GOTFactionNpc npc) {
        if (!player.isShiftKeyDown() || !target.isAlive() || player.distanceToSqr(target)>9.0D) return false;
        if (GOTHiredData.isHired(target)) return false;
        if (target.getTarget()!=null) { player.displayClientMessage(Component.translatable("got.chat.pickpocket.inCombat").withStyle(ChatFormatting.DARK_RED),false); return true; }
        if (wasPickpocketed(player,target.getUUID())) return false;
        if (isWatching(target,player)) { player.displayClientMessage(Component.translatable("got.chat.pickpocket.watched").withStyle(ChatFormatting.GOLD),false); return true; }

        RandomSource random=target.getRandom();
        boolean lootSuccess=random.nextInt(3)==0; // exact legacy first roll
        boolean noticed=random.nextInt(lootSuccess?3:4)==0; // exact legacy target-notice roll
        if (lootSuccess) {
            ItemStack loot=createLoot(random);
            markPickpocketed(player,target.getUUID());
            if (!player.getInventory().add(loot)) player.drop(loot,false);
            player.displayClientMessage(Component.translatable("got.chat.pickpocket.success",loot.getCount(),loot.getHoverName(),target.getDisplayName()).withStyle(ChatFormatting.DARK_GREEN),false);
            GOTAchievementHooks.award(player,"pickpocket");
            GOTQuestService.triggerEvent(player,new net.minecraft.resources.ResourceLocation("got","pickpocket"),1);
        } else {
            player.displayClientMessage(Component.translatable("got.chat.pickpocket.missed",target.getDisplayName()).withStyle(ChatFormatting.GRAY),false);
        }

        boolean witnessed=noticed;
        if (noticed) {
            player.displayClientMessage(Component.translatable("got.chat.pickpocket.noticed",target.getDisplayName()).withStyle(ChatFormatting.GOLD),false);
            target.setTarget(player);
        }
        double box=16.0D;
        for (Mob witness:target.level().getEntitiesOfClass(Mob.class,target.getBoundingBox().inflate(box),m -> m!=target && m instanceof GOTFactionNpc)) {
            GOTFactionNpc wf=(GOTFactionNpc)witness;
            if (wf.getFaction()!=npc.getFaction() || witness.getTarget()!=null) continue;
            double range=wf.isCivilian()?8.0D:16.0D; double dist=witness.distanceTo(target); if(dist>range || !isWatching(witness,player)) continue;
            float closeness=(float)(1.0D-Math.max(0.0D,dist-4.0D)/Math.max(1.0D,range-4.0D));
            float chance=(0.5F+closeness*0.5F)*(wf.isCivilian()?0.25F:1.0F);
            if(random.nextFloat()<chance){ witness.setTarget(player); witnessed=true; }
        }
        if (witnessed && npc.getFaction().isPlayable()) GOTFactionService.addAlignment(player,npc.getFaction(),-1.0F);
        return true;
    }

    private static ItemStack createLoot(RandomSource random) {
        int r=random.nextInt(100);
        if(r<60) return new ItemStack(GOTItems.COIN_1.get(),1+random.nextInt(8));
        if(r<85) return new ItemStack(GOTItems.COIN_4.get(),1+random.nextInt(4));
        if(r<97) return new ItemStack(GOTItems.COIN_16.get(),1+random.nextInt(2));
        return new ItemStack(GOTItems.COIN_64.get(),1);
    }

    private static boolean isWatching(Mob watcher, LivingEntity player) {
        if (!watcher.hasLineOfSight(player)) return false;
        Vec3 to=player.getEyePosition().subtract(watcher.getEyePosition()).normalize();
        return watcher.getViewVector(1.0F).normalize().dot(to)>0.45D;
    }

    private static boolean wasPickpocketed(ServerPlayer p,UUID id){return readSet(p).contains(id);}    
    private static void markPickpocketed(ServerPlayer p,UUID id){Set<UUID>s=readSet(p);s.add(id);var tag=p.getPersistentData().getCompound(PICKPOCKETED);tag.putBoolean(id.toString(),true);p.getPersistentData().put(PICKPOCKETED,tag);}    
    private static Set<UUID> readSet(ServerPlayer p){Set<UUID>s=new HashSet<>();var t=p.getPersistentData().getCompound(PICKPOCKETED);for(String k:t.getAllKeys())try{if(t.getBoolean(k))s.add(UUID.fromString(k));}catch(Exception ignored){}return s;}
}
