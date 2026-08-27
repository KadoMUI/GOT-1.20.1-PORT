package got.cape;
import got.faction.GOTFaction;
import got.faction.GOTFactionPlayerData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import java.util.List;
import java.util.Locale;

public enum GOTCape {
 NORTH(GOTFaction.NORTH), NORTHGUARD(GOTFaction.NORTH), NIGHT(GOTFaction.NIGHT_WATCH),
 RIVERLANDS(GOTFaction.RIVERLANDS), ARRYN(GOTFaction.ARRYN), ARRYNGUARD(GOTFaction.ARRYN),
 IRONBORN(GOTFaction.IRONBORN), WESTERLANDS(GOTFaction.WESTERLANDS),
 DRAGONSTONE(GOTFaction.DRAGONSTONE), CROWNLANDS(GOTFaction.CROWNLANDS),
 KINGSGUARD_1(GOTFaction.CROWNLANDS), KINGSGUARD_2(GOTFaction.CROWNLANDS),
 STORMLANDS(GOTFaction.STORMLANDS), REACH(GOTFaction.REACH), DORNE(GOTFaction.DORNE),
 VOLANTIS(GOTFaction.VOLANTIS), PENTOS(GOTFaction.PENTOS), NORVOS(GOTFaction.NORVOS),
 BRAAVOS(GOTFaction.BRAAVOS), TYROSH(GOTFaction.TYROSH), LORATH(GOTFaction.LORATH),
 QOHOR(GOTFaction.QOHOR), LYS(GOTFaction.LYS), MYR(GOTFaction.MYR), QARTH(GOTFaction.QARTH),
 GHISCAR(GOTFaction.GHISCAR), UNSULLIED(GOTFaction.GHISCAR),
 YI_TI(GOTFaction.YI_TI), YI_TI_BOMBARDIER(GOTFaction.YI_TI), YI_TI_SAMURAI(GOTFaction.YI_TI),
 ASSHAI(GOTFaction.ASSHAI), TARGARYEN(null);

 public static final float LEGACY_ALIGNMENT=100F, TARGARYEN_ALIGNMENT=500F;
 public static final List<GOTFaction> DAENERYS_FACTIONS =
   List.of(GOTFaction.DOTHRAKI,GOTFaction.GHISCAR,GOTFaction.DRAGONSTONE);
 private final GOTFaction faction;
 GOTCape(GOTFaction faction){this.faction=faction;}
 public String id(){return name().toLowerCase(Locale.ROOT);}
 public ResourceLocation texture(){return ResourceLocation.fromNamespaceAndPath("got","textures/cape/"+id()+".png");}
 public boolean unlocked(ServerPlayer p){
  GOTFactionPlayerData d=GOTFactionPlayerData.get(p);
  if(this==TARGARYEN){for(GOTFaction f:DAENERYS_FACTIONS)if(d.alignment(f)<500F)return false;return true;}
  return faction!=null&&d.alignment(faction)>=100F;
 }
 public static GOTCape byId(String id){if(id==null||id.isBlank())return null;try{return valueOf(id.toUpperCase(Locale.ROOT));}catch(Exception e){return null;}}
}
