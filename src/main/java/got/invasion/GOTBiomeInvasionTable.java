package got.invasion;

import net.minecraft.resources.ResourceLocation;
import java.util.*;

/** Exact invasion registrations recovered from the 24.08.29 1.7.10 biome constructors. */
public final class GOTBiomeInvasionTable {
    public enum Chance { RARE(.10D), UNCOMMON(.30D), COMMON(.90D); private final double hourly; Chance(double h){hourly=h;} public double perSecond(){return 1D-Math.pow(1D-hourly,1D/3600D);} }
    public record Entry(GOTInvasionType type, Chance chance) {}
    private static final Map<String,List<Entry>> TABLE=new HashMap<>();
    static {
        add("braavos", "VOLANTIS:UNCOMMON","PENTOS:UNCOMMON");
        add("disputed_lands", "MYR:UNCOMMON","VOLANTIS:UNCOMMON","TYROSH:UNCOMMON","LYS:UNCOMMON","BRAAVOS:UNCOMMON");
        add("ibben", "IRONBORN:UNCOMMON","LORATH:UNCOMMON"); add("ibben_colony","LORATH:UNCOMMON");
        add("lhazar","DOTHRAKI:COMMON"); add("lorath","IBBEN:UNCOMMON");
        add("lys","MYR:UNCOMMON","VOLANTIS:UNCOMMON","TYROSH:UNCOMMON");
        add("myr","LYS:UNCOMMON","VOLANTIS:UNCOMMON","TYROSH:UNCOMMON","DOTHRAKI:UNCOMMON");
        add("norvos","VOLANTIS:UNCOMMON"); add("pentos","BRAAVOS:UNCOMMON");
        add("tyrosh","MYR:UNCOMMON","VOLANTIS:UNCOMMON","LYS:UNCOMMON");
        add("volantis","BRAAVOS:UNCOMMON","TYROSH:UNCOMMON","LYS:UNCOMMON","MYR:UNCOMMON","NORVOS:UNCOMMON","DOTHRAKI:UNCOMMON");
        add("yi_ti","JOGOS_NHAI:UNCOMMON"); add("ghiscar_colony","SOTHORYOS:COMMON");
        add("qarth_colony","SOTHORYOS:COMMON"); add("summer_colony","SOTHORYOS:COMMON"); add("summer_islands","GHISCAR:COMMON");
        add("arryn","WESTERLANDS:UNCOMMON","HILL_TRIBES:COMMON");
        add("crownlands","DRAGONSTONE:UNCOMMON","IRONBORN:UNCOMMON","STORMLANDS:UNCOMMON","RIVERLANDS:UNCOMMON");
        add("dorne","WESTERLANDS:UNCOMMON"); add("dragonstone","WESTERLANDS:UNCOMMON","STORMLANDS:UNCOMMON","REACH:UNCOMMON");
        add("gift_old","THENN:RARE","WILDLING:UNCOMMON","GIANT:RARE");
        add("north","WESTERLANDS:UNCOMMON","IRONBORN:UNCOMMON"); add("north_wild","THENN:RARE","WILDLING:UNCOMMON","GIANT:RARE");
        add("reach","DRAGONSTONE:UNCOMMON","IRONBORN:UNCOMMON");
        add("riverlands","WESTERLANDS:UNCOMMON","IRONBORN:UNCOMMON","HILL_TRIBES:COMMON");
        add("stormlands","WESTERLANDS:UNCOMMON","DRAGONSTONE:UNCOMMON");
        add("westerlands","DRAGONSTONE:UNCOMMON","STORMLANDS:UNCOMMON","RIVERLANDS:UNCOMMON","NORTH:UNCOMMON");
    }
    private static void add(String biome,String... entries){List<Entry> list=new ArrayList<>();for(String s:entries){String[] p=s.split(":");list.add(new Entry(GOTInvasionType.valueOf(p[0]),Chance.valueOf(p[1])));}TABLE.put(biome,List.copyOf(list));}
    public static List<Entry> forBiome(ResourceLocation id){ if(!"got".equals(id.getNamespace()))return List.of(); String path=id.getPath(); List<Entry> exact=TABLE.get(path); if(exact!=null)return exact; // 1.7.10 subclass biomes inherited their parent constructor registrations.
        String best=null; for(String k:TABLE.keySet())if(path.startsWith(k+"_")&&(best==null||k.length()>best.length()))best=k; return best==null?List.of():TABLE.get(best); }
    public static Map<String,List<Entry>> all(){return Collections.unmodifiableMap(TABLE);} private GOTBiomeInvasionTable(){}
}
