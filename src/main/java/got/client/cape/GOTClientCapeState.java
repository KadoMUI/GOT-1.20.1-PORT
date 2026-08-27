package got.client.cape;
import got.cape.GOTCape;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
public final class GOTClientCapeState {
 private static final Set<GOTCape> UNLOCKED=EnumSet.noneOf(GOTCape.class);
 private static final ConcurrentHashMap<UUID,GOTCape> PLAYERS=new ConcurrentHashMap<>();
 private static GOTCape selected;
 private GOTClientCapeState(){}
 public static void setMenuData(GOTCape c,Set<GOTCape> u){selected=c;UNLOCKED.clear();UNLOCKED.addAll(u);}
 public static GOTCape selected(){return selected;}
 public static boolean unlocked(GOTCape c){return UNLOCKED.contains(c);}
 public static void setPlayer(UUID id,GOTCape c){if(c==null)PLAYERS.remove(id);else PLAYERS.put(id,c);}
 public static GOTCape player(UUID id){return PLAYERS.get(id);}
}
