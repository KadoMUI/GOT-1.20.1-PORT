package got.invasion;
import got.GOTMod;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.levelgen.Heightmap;
import got.faction.GOTFactionPlayerData;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
@Mod.EventBusSubscriber(modid=GOTMod.MOD_ID,bus=Mod.EventBusSubscriber.Bus.FORGE)
public final class GOTInvasionEvents {
 private GOTInvasionEvents(){}
 @SubscribeEvent public static void levelTick(TickEvent.LevelTickEvent e){if(e.phase!=TickEvent.Phase.END||!(e.level instanceof ServerLevel level))return;GOTInvasionData.get(level).tick(level);if(level.getGameTime()%20==0)tickNatural(level);}
 private static void tickNatural(ServerLevel level){
  Set<Long> chunks=new HashSet<>();
  for(ServerPlayer p:level.players()){if(p.isSpectator())continue;ChunkPos pc=p.chunkPosition();for(int dx=-8;dx<=8;dx++)for(int dz=-8;dz<=8;dz++)chunks.add(ChunkPos.asLong(pc.x+dx,pc.z+dz));}
  var biomeRegistry=level.registryAccess().registryOrThrow(Registries.BIOME);
  for(long packed:chunks){ChunkPos cp=new ChunkPos(packed);int x=cp.getMinBlockX()+level.random.nextInt(16),z=cp.getMinBlockZ()+level.random.nextInt(16),y=level.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,x,z);BlockPos sample=new BlockPos(x,y,z);ResourceLocation biomeId=biomeRegistry.getKey(level.getBiome(sample).value());if(biomeId==null)continue;
   for(var entry:GOTBiomeInvasionTable.forBiome(biomeId)){if(level.random.nextDouble()>=entry.chance().perSecond())continue;ServerPlayer target=null;for(ServerPlayer p:level.players())if(!p.isSpectator()&&p.distanceToSqr(x+.5,y,z+.5)<=48D*48D&&GOTFactionPlayerData.get(p).alignment(entry.type().faction())<0){target=p;break;}if(target==null)continue;
    boolean nearby=false;for(var a:GOTInvasionData.get(level).active())if(a.type==entry.type()&&a.center.distSqr(sample)<=128D*128D){nearby=true;break;}if(nearby)continue;
    for(int tries=0;tries<16;tries++){int sx=x-32+level.random.nextInt(65),sz=z-32+level.random.nextInt(65),sy=level.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,sx,sz);if(sy<=60)continue;BlockPos pos=new BlockPos(sx,sy,sz);if(!level.getBlockState(pos.below()).isSolidRender(level,pos.below())||!level.getBlockState(pos).isAir()||!level.getBlockState(pos.above()).isAir())continue;GOTInvasionData.get(level).start(level,entry.type(),pos.above(3),-1);return;}
   }
  }
 }
 @SubscribeEvent public static void death(LivingDeathEvent e){if(!(e.getEntity().level() instanceof ServerLevel level))return; var tag=e.getEntity().getPersistentData();if(!tag.hasUUID("GOTInvasionId"))return; UUID killer=e.getSource().getEntity() instanceof ServerPlayer p?p.getUUID():null;GOTInvasionData.get(level).onMobDeath(level,tag.getUUID("GOTInvasionId"),killer);}
}
