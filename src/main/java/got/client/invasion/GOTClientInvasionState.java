package got.client.invasion;

import got.invasion.GOTInvasionType;
import got.network.S2CInvasionWatchPacket;
import net.minecraft.core.BlockPos;
import java.util.UUID;

/** Client watched-invasion state. Legacy watcher expired after 600 ticks without relevance. */
public final class GOTClientInvasionState {
 private static UUID id; private static GOTInvasionType type; private static BlockPos center=BlockPos.ZERO; private static int size,remaining,stale;
 private GOTClientInvasionState(){}
 public static void accept(S2CInvasionWatchPacket p){
  if(p.clear()){if(id!=null&&id.equals(p.id()))clear();return;}
  if(id==null||p.override()||id.equals(p.id())){try{type=GOTInvasionType.valueOf(p.type());}catch(Exception e){return;}id=p.id();center=p.center();size=p.size();remaining=p.remaining();stale=0;}
 }
 public static void tick(){if(id!=null&&++stale>=600)clear();}
 public static void clear(){id=null;type=null;center=BlockPos.ZERO;size=remaining=stale=0;}
 public static boolean active(){return id!=null&&type!=null&&size>0;}
 public static GOTInvasionType type(){return type;} public static BlockPos center(){return center;}
 public static float health(){return size<=0?0:Math.max(0,Math.min(1,remaining/(float)size));}
}
