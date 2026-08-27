package got.cape;
import got.GOTMod;
import got.network.*;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;

@Mod.EventBusSubscriber(modid=GOTMod.MOD_ID)
public final class GOTCapeEvents {
 private GOTCapeEvents(){}
 @SubscribeEvent public static void clone(PlayerEvent.Clone e){if(!e.isWasDeath())return;e.getOriginal().reviveCaps();GOTCapeData.copy(e.getOriginal(),e.getEntity());e.getOriginal().invalidateCaps();}
 @SubscribeEvent public static void login(PlayerEvent.PlayerLoggedInEvent e){
  if(!(e.getEntity() instanceof ServerPlayer sp))return;
  for(ServerPlayer other:sp.server.getPlayerList().getPlayers()){
   GOTCape oc=GOTCapeData.selected(other);
   GOTNetwork.CHANNEL.send(PacketDistributor.PLAYER.with(()->sp),new S2CPlayerCapePacket(other.getUUID(),oc));
  }
  GOTCape mine=GOTCapeData.selected(sp);
  GOTNetwork.CHANNEL.send(PacketDistributor.ALL.noArg(),new S2CPlayerCapePacket(sp.getUUID(),mine));
 }
}
