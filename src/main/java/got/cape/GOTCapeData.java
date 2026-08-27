package got.cape;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
public final class GOTCapeData {
 private static final String KEY="got_selected_cape";
 private GOTCapeData(){}
 public static GOTCape selected(Player p){return GOTCape.byId(p.getPersistentData().getCompound(Player.PERSISTED_NBT_TAG).getString(KEY));}
 public static void setSelected(Player p,GOTCape c){CompoundTag a=p.getPersistentData(),q=a.getCompound(Player.PERSISTED_NBT_TAG);if(c==null)q.remove(KEY);else q.putString(KEY,c.id());a.put(Player.PERSISTED_NBT_TAG,q);}
 public static void copy(Player a,Player b){GOTCape c=selected(a);if(c!=null)setSelected(b,c);}
}
