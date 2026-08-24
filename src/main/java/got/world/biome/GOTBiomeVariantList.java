package got.world.biome;
import java.util.*;
public final class GOTBiomeVariantList{
 private static final Map<Integer,GOTBiomeVariant> BY_ID=new LinkedHashMap<>();private static final Map<String,GOTBiomeVariant> BY_NAME=new LinkedHashMap<>();
 public static final GOTBiomeVariant STANDARD=register(0,"standard","Standard",GOTBiomeVariant.Category.STANDARD,0,1);
 public static final GOTBiomeVariant HILLS=register(1,"hills","Hills",GOTBiomeVariant.Category.HILLS,.35f,.8f);
 public static final GOTBiomeVariant MOUNTAINS=register(2,"mountains","Mountains",GOTBiomeVariant.Category.MOUNTAINS,1f,.25f);
 public static final GOTBiomeVariant FOREST=register(3,"forest","Forest",GOTBiomeVariant.Category.FOREST,.05f,1.5f);
 public static final GOTBiomeVariant DENSE_FOREST=register(4,"dense_forest","Dense Forest",GOTBiomeVariant.Category.DENSE_FOREST,.05f,2f);
 public static final GOTBiomeVariant SPARSE_FOREST=register(5,"sparse_forest","Sparse Forest",GOTBiomeVariant.Category.SPARSE_FOREST,0,.65f);
 public static final GOTBiomeVariant ORCHARD=register(6,"orchard","Orchard",GOTBiomeVariant.Category.ORCHARD,0,1.4f);
 public static final GOTBiomeVariant LAKE=register(7,"lake","Lake",GOTBiomeVariant.Category.LAKE,-.5f,0);
 public static final GOTBiomeVariant RIVER=register(8,"river","River",GOTBiomeVariant.Category.RIVER,-.4f,0);
 public static final GOTBiomeVariant MARSH=register(9,"marsh","Marsh",GOTBiomeVariant.Category.MARSH,-.15f,.5f);
 public static final GOTBiomeVariant ROCKY=register(10,"rocky","Rocky",GOTBiomeVariant.Category.ROCKY,.2f,.15f);
 private GOTBiomeVariantList(){}
 public static GOTBiomeVariant register(int id,String n,String d,GOTBiomeVariant.Category c,float h,float t){var v=new GOTBiomeVariant(id,n,d,c,h,t);BY_ID.put(id,v);BY_NAME.put(n,v);return v;}
 public static GOTBiomeVariant byId(int id){return BY_ID.getOrDefault(id,STANDARD);}public static GOTBiomeVariant byName(String n){return BY_NAME.getOrDefault(n,STANDARD);}public static Collection<GOTBiomeVariant> values(){return Collections.unmodifiableCollection(BY_ID.values());}
}
