package got.invasion;

import got.achievement.GOTAchievementHooks;
import got.faction.GOTFactionPlayerData;
import got.network.GOTNetwork;
import got.network.S2CInvasionWatchPacket;
import net.minecraftforge.network.PacketDistributor;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.*;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.Difficulty;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.saveddata.SavedData;
import java.util.*;

/** Persistent 1.7.10-style invasion-spawner state. Pass 3 restores kill-goal semantics and lifecycle timing. */
public final class GOTInvasionData extends SavedData {
    private static final String NAME="got_invasions";
    private static final SoundEvent HORN=SoundEvent.createVariableRangeEvent(new ResourceLocation("got","item.horn"));
    private final Map<UUID, Active> active=new LinkedHashMap<>();
    public static GOTInvasionData get(ServerLevel level){ return level.getDataStorage().computeIfAbsent(GOTInvasionData::load,GOTInvasionData::new,NAME); }
    public Collection<Active> active(){ return Collections.unmodifiableCollection(active.values()); }

    public UUID start(ServerLevel level,GOTInvasionType type,BlockPos center,int requestedSize){return start(level,type,center,requestedSize,false,null);}
    public UUID start(ServerLevel level,GOTInvasionType type,BlockPos center,int requestedSize,boolean warhorn,UUID initiator){
        int size=requestedSize<0?30+level.random.nextInt(41):Math.max(1,Math.min(10000,requestedSize));
        UUID id=UUID.randomUUID(); Active a=new Active(id,type,center,size,size);a.warhorn=warhorn;active.put(id,a);setDirty();
        playHorn(level,a);announceStart(level,a,initiator);return id;
    }
    public boolean stop(ServerLevel level,UUID id,boolean victory){Active a=active.remove(id);if(a==null)return false;for(ServerPlayer p:level.players())if(p.distanceToSqr(a.center.getX()+.5,a.center.getY()+.5,a.center.getZ()+.5)<=10000)GOTNetwork.CHANNEL.send(PacketDistributor.PLAYER.with(()->p),S2CInvasionWatchPacket.clear(a.id));if(victory)reward(level,a);announceEnd(level,a);level.explode(null,a.center.getX()+.5,a.center.getY()+.75,a.center.getZ()+.5,0.0F,Level.ExplosionInteraction.NONE);setDirty();return true;}

    /** In 1.7.10 invasionRemaining is a PLAYER-KILL goal, not a spawn budget. */
    public void onMobDeath(ServerLevel level,UUID invasionId,UUID killer){
        Active a=active.get(invasionId);if(a==null)return;a.alive=Math.max(0,a.alive-1);
        if(killer!=null){a.remaining=Math.max(0,a.remaining-1);a.timeSincePlayerProgress=0;a.contributors.put(killer,2400);}
        setDirty();
    }

    public void tick(ServerLevel level){
        if(level.getDifficulty()==Difficulty.PEACEFUL){for(Active a:new ArrayList<>(active.values()))stop(level,a.id,false);return;}
        for(Active a:new ArrayList<>(active.values())){
            if(a.remaining<=0){stop(level,a.id,true);continue;}
            a.timeSincePlayerProgress++;
            // Original natural invasions slowly regain one required kill after five idle minutes; warhorn invasions do not.
            if(!a.warhorn&&a.timeSincePlayerProgress>=6000&&a.timeSincePlayerProgress%1200==0)a.remaining=Math.min(a.size,a.remaining+1);
            if(!a.contributors.isEmpty()){Iterator<Map.Entry<UUID,Integer>> it=a.contributors.entrySet().iterator();while(it.hasNext()){var e=it.next();int ttl=e.getValue()-1;if(ttl<=0)it.remove();else e.setValue(ttl);}}
            net.minecraft.world.entity.player.Player nearestPlayer=level.getNearestPlayer(a.center.getX()+.5,a.center.getY()+.5,a.center.getZ()+.5,80,false);if(!(nearestPlayer instanceof ServerPlayer nearest))continue;
            if(a.alive>=16||level.random.nextInt(160)!=0)continue;
            int count=Math.min(a.remaining,1+level.random.nextInt(6));boolean any=false;
            for(int i=0;i<count;i++)if(spawnOne(level,a)){a.alive++;any=true;}
            if(!any){a.failed++;if(a.failed>=16){stop(level,a.id,false);continue;}}else{a.failed=0;playHorn(level,a);}
            syncWatchers(level,a);setDirty();
        }
    }
    private void syncWatchers(ServerLevel level,Active a){for(ServerPlayer p:level.players()){if(p.distanceToSqr(a.center.getX()+.5,a.center.getY()+.5,a.center.getZ()+.5)>10000)continue;if(GOTFactionPlayerData.get(p).alignment(a.type.faction())>=0)continue;GOTNetwork.CHANNEL.send(PacketDistributor.PLAYER.with(()->p),new S2CInvasionWatchPacket(a.id,a.type.name(),a.center,a.size,a.remaining,false,false));}}
    private boolean spawnOne(ServerLevel level,Active a){for(int tries=0;tries<40;tries++){int x=a.center.getX()-6+level.random.nextInt(13),z=a.center.getZ()-6+level.random.nextInt(13);int baseY=a.center.getY()-8+level.random.nextInt(13);int surface=level.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,x,z);int y=Math.max(level.getMinBuildHeight()+1,Math.min(surface,baseY));BlockPos pos=new BlockPos(x,y,z);if(!level.getBlockState(pos.below()).isSolidRender(level,pos.below()))continue;if(!level.getBlockState(pos).isAir()||!level.getBlockState(pos.above()).isAir())continue;var entry=a.type.randomEntry(level.random);Mob mob=GOTInvasionNpcFactory.create(level,a.type,entry.role(),pos,a.id);if(mob==null)continue;if(level.addFreshEntity(mob))return true;}return false;}
    private void playHorn(ServerLevel level,Active a){level.playSound(null,a.center,HORN,SoundSource.HOSTILE,4.0F,0.65F+level.random.nextFloat()*0.1F);}
    private void reward(ServerLevel level,Active a){List<ServerPlayer> pledged=new ArrayList<>();for(UUID id:a.contributors.keySet()){ServerPlayer p=level.getServer().getPlayerList().getPlayer(id);if(p==null||p.level()!=level||p.distanceToSqr(a.center.getX()+.5,a.center.getY()+.5,a.center.getZ()+.5)>=10000)continue;GOTFactionPlayerData data=GOTFactionPlayerData.get(p);if(data.alignment(a.type.faction())<=0)GOTAchievementHooks.award(p,"DEFEAT_INVASION");if(data.membership()!=got.faction.GOTFaction.UNALIGNED&&data.membership().isBadRelation(a.type.faction()))pledged.add(p);}if(!pledged.isEmpty()){float each=50.0F/pledged.size();for(ServerPlayer p:pledged){GOTFactionPlayerData data=GOTFactionPlayerData.get(p);data.addAlignment(data.membership(),each);}}}
    private void announceStart(ServerLevel level,Active a,UUID initiator){for(ServerPlayer p:level.players()){if(p.distanceToSqr(a.center.getX()+.5,a.center.getY()+.5,a.center.getZ()+.5)>6400)continue;if(!p.getUUID().equals(initiator)&&GOTFactionPlayerData.get(p).alignment(a.type.faction())>=0)continue;p.sendSystemMessage(Component.translatable("got.invasion.start",Component.translatable("got.invasion."+a.type.codeName()),a.size));GOTNetwork.CHANNEL.send(PacketDistributor.PLAYER.with(()->p),new S2CInvasionWatchPacket(a.id,a.type.name(),a.center,a.size,a.remaining,false,true));}}
    private void announceEnd(ServerLevel level,Active a){for(ServerPlayer p:level.players())if(p.distanceToSqr(a.center.getX()+.5,a.center.getY()+.5,a.center.getZ()+.5)<=6400)p.sendSystemMessage(Component.translatable("got.invasion.end",Component.translatable("got.invasion."+a.type.codeName()),a.size));}
    @Override public CompoundTag save(CompoundTag root){ListTag list=new ListTag();for(Active a:active.values())list.add(a.save());root.put("Active",list);return root;}
    public static GOTInvasionData load(CompoundTag root){GOTInvasionData data=new GOTInvasionData();ListTag list=root.getList("Active",Tag.TAG_COMPOUND);for(int i=0;i<list.size();i++){Active a=Active.load(list.getCompound(i));data.active.put(a.id,a);}return data;}
    public static final class Active{
        public final UUID id;public final GOTInvasionType type;public final BlockPos center;public final int size;public int remaining,alive,failed,timeSincePlayerProgress;public boolean warhorn;public final Map<UUID,Integer> contributors=new HashMap<>();
        Active(UUID id,GOTInvasionType type,BlockPos center,int size,int remaining){this.id=id;this.type=type;this.center=center;this.size=size;this.remaining=remaining;}
        public float health(){return size<=0?0:remaining/(float)size;}
        CompoundTag save(){CompoundTag t=new CompoundTag();t.putUUID("Id",id);t.putString("Type",type.name());t.putLong("Center",center.asLong());t.putInt("Size",size);t.putInt("Remaining",remaining);t.putInt("Alive",alive);t.putInt("Failed",failed);t.putInt("TimeSincePlayerProgress",timeSincePlayerProgress);t.putBoolean("Warhorn",warhorn);ListTag c=new ListTag();for(var e:contributors.entrySet()){CompoundTag ct=new CompoundTag();ct.putUUID("Player",e.getKey());ct.putInt("Ticks",e.getValue());c.add(ct);}t.put("Contributors",c);return t;}
        static Active load(CompoundTag t){Active a=new Active(t.getUUID("Id"),GOTInvasionType.valueOf(t.getString("Type")),BlockPos.of(t.getLong("Center")),t.getInt("Size"),t.getInt("Remaining"));a.alive=t.getInt("Alive");a.failed=t.getInt("Failed");a.timeSincePlayerProgress=t.getInt("TimeSincePlayerProgress");a.warhorn=t.getBoolean("Warhorn");ListTag c=t.getList("Contributors",Tag.TAG_COMPOUND);for(int i=0;i<c.size();i++){CompoundTag ct=c.getCompound(i);if(ct.hasUUID("Player"))a.contributors.put(ct.getUUID("Player"),ct.getInt("Ticks"));}return a;}
    }
}
