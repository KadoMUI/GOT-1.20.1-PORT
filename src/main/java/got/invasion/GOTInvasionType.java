package got.invasion;

import got.faction.GOTFaction;
import net.minecraft.util.RandomSource;
import java.util.*;

public enum GOTInvasionType {
    HILL_TRIBES(GOTFaction.HILL_TRIBES, Region.ARRYN, List.of(new SpawnEntry("HILLMAN_WARRIOR", 10), new SpawnEntry("HILLMAN_ARCHER", 5), new SpawnEntry("HILLMAN_BERSERKER", 2), new SpawnEntry("HILLMAN_BANNER_BEARER", 2))),
    WILDLING(GOTFaction.WILDLING, Region.WILDLING, List.of(new SpawnEntry("WILDLING_WARRIOR", 10), new SpawnEntry("WILDLING_ARCHER", 5), new SpawnEntry("WILDLING_BANNER_BEARER", 2))),
    THENN(GOTFaction.WILDLING, Region.WILDLING, List.of(new SpawnEntry("THENN_WARRIOR", 10), new SpawnEntry("THENN_ARCHER", 5), new SpawnEntry("THENN_BANNER_BEARER", 2))),
    GIANT(GOTFaction.WILDLING, Region.WILDLING, List.of(new SpawnEntry("WILDLING_WARRIOR", 10), new SpawnEntry("WILDLING_ARCHER", 5), new SpawnEntry("WILDLING_BANNER_BEARER", 2), new SpawnEntry("GIANT", 1))),
    DOTHRAKI(GOTFaction.DOTHRAKI, Region.DOTHRAKI, List.of(new SpawnEntry("DOTHRAKI", 10), new SpawnEntry("DOTHRAKI_ARCHER", 5))),
    JOGOS_NHAI(GOTFaction.JOGOS_NHAI, Region.JOGOS_NHAI, List.of(new SpawnEntry("JOGOS_NHAI_MAN", 10), new SpawnEntry("JOGOS_NHAI_ARCHER", 5))),
    YI_TI(GOTFaction.YI_TI, Region.YI_TI, List.of(new SpawnEntry("YI_TI_SOLDIER", 10), new SpawnEntry("YI_TI_SOLDIER_CROSSBOWER", 5), new SpawnEntry("YI_TI_SAMURAI", 3), new SpawnEntry("YI_TI_SAMURAI_FLAMETHROWER", 2), new SpawnEntry("YI_TI_BOMBARDIER", 1), new SpawnEntry("YI_TI_BANNER_BEARER", 2))),
    IBBEN(GOTFaction.IBBEN, Region.IBBEN, List.of(new SpawnEntry("IBBEN_SOLDIER", 10), new SpawnEntry("IBBEN_SOLDIER_ARCHER", 5), new SpawnEntry("IBBEN_BANNER_BEARER", 2))),
    VOLANTIS(GOTFaction.VOLANTIS, Region.VOLANTIS, List.of(new SpawnEntry("VOLANTIS_SOLDIER", 10), new SpawnEntry("VOLANTIS_SOLDIER_ARCHER", 5), new SpawnEntry("VOLANTIS_BANNER_BEARER", 2))),
    GHISCAR(GOTFaction.GHISCAR, Region.GHISCAR, List.of(new SpawnEntry("GHISCAR_SOLDIER", 10), new SpawnEntry("GHISCAR_SOLDIER_ARCHER", 5), new SpawnEntry("GHISCAR_BANNER_BEARER", 3))),
    BRAAVOS(GOTFaction.BRAAVOS, Region.BRAAVOS, List.of(new SpawnEntry("BRAAVOS_SOLDIER", 10), new SpawnEntry("BRAAVOS_SOLDIER_ARCHER", 5), new SpawnEntry("BRAAVOS_BANNER_BEARER", 2))),
    LORATH(GOTFaction.LORATH, Region.LORATH, List.of(new SpawnEntry("LORATH_SOLDIER", 10), new SpawnEntry("LORATH_SOLDIER_ARCHER", 5), new SpawnEntry("LORATH_BANNER_BEARER", 2))),
    NORVOS(GOTFaction.NORVOS, Region.NORVOS, List.of(new SpawnEntry("NORVOS_SOLDIER", 10), new SpawnEntry("NORVOS_SOLDIER_ARCHER", 5), new SpawnEntry("NORVOS_BANNER_BEARER", 2))),
    TYROSH(GOTFaction.TYROSH, Region.TYROSH, List.of(new SpawnEntry("TYROSH_SOLDIER", 10), new SpawnEntry("TYROSH_SOLDIER_ARCHER", 5), new SpawnEntry("TYROSH_BANNER_BEARER", 2))),
    MYR(GOTFaction.MYR, Region.MYR, List.of(new SpawnEntry("MYR_SOLDIER", 10), new SpawnEntry("MYR_SOLDIER_ARCHER", 5), new SpawnEntry("MYR_BANNER_BEARER", 2))),
    LYS(GOTFaction.LYS, Region.LYS, List.of(new SpawnEntry("LYS_SOLDIER", 10), new SpawnEntry("LYS_SOLDIER_ARCHER", 5), new SpawnEntry("LYS_BANNER_BEARER", 2))),
    PENTOS(GOTFaction.PENTOS, Region.PENTOS, List.of(new SpawnEntry("PENTOS_SOLDIER", 10), new SpawnEntry("PENTOS_SOLDIER_ARCHER", 5), new SpawnEntry("PENTOS_BANNER_BEARER", 2))),
    IRONBORN(GOTFaction.IRONBORN, Region.IRONBORN, List.of(new SpawnEntry("IRONBORN_SOLDIER", 10), new SpawnEntry("IRONBORN_SOLDIER_ARCHER", 5), new SpawnEntry("IRONBORN_BANNER_BEARER", 2))),
    WESTERLANDS(GOTFaction.WESTERLANDS, Region.WESTERLANDS, List.of(new SpawnEntry("WESTERLANDS_SOLDIER", 10), new SpawnEntry("WESTERLANDS_SOLDIER_ARCHER", 5), new SpawnEntry("WESTERLANDS_BANNER_BEARER", 2))),
    RIVERLANDS(GOTFaction.RIVERLANDS, Region.RIVERLANDS, List.of(new SpawnEntry("RIVERLANDS_SOLDIER", 10), new SpawnEntry("RIVERLANDS_SOLDIER_ARCHER", 5), new SpawnEntry("RIVERLANDS_BANNER_BEARER", 2))),
    ARRYN(GOTFaction.ARRYN, Region.ARRYN, List.of(new SpawnEntry("ARRYN_SOLDIER", 10), new SpawnEntry("ARRYN_SOLDIER_ARCHER", 5), new SpawnEntry("ARRYN_BANNER_BEARER", 2))),
    DRAGONSTONE(GOTFaction.DRAGONSTONE, Region.DRAGONSTONE, List.of(new SpawnEntry("DRAGONSTONE_SOLDIER", 10), new SpawnEntry("DRAGONSTONE_SOLDIER_ARCHER", 5), new SpawnEntry("DRAGONSTONE_BANNER_BEARER", 2))),
    STORMLANDS(GOTFaction.STORMLANDS, Region.STORMLANDS, List.of(new SpawnEntry("STORMLANDS_SOLDIER", 10), new SpawnEntry("STORMLANDS_SOLDIER_ARCHER", 5), new SpawnEntry("STORMLANDS_BANNER_BEARER", 2))),
    REACH(GOTFaction.REACH, Region.REACH, List.of(new SpawnEntry("REACH_SOLDIER", 10), new SpawnEntry("REACH_SOLDIER_ARCHER", 5), new SpawnEntry("REACH_BANNER_BEARER", 2))),
    DORNE(GOTFaction.DORNE, Region.DORNE, List.of(new SpawnEntry("DORNE_SOLDIER", 10), new SpawnEntry("DORNE_SOLDIER_ARCHER", 5), new SpawnEntry("DORNE_BANNER_BEARER", 2))),
    NORTH(GOTFaction.NORTH, Region.NORTH, List.of(new SpawnEntry("NORTH_SOLDIER", 10), new SpawnEntry("NORTH_SOLDIER_ARCHER", 5), new SpawnEntry("NORTH_BANNER_BEARER", 2))),
    SOTHORYOS(GOTFaction.SOTHORYOS, Region.SOTHORYOS, List.of(new SpawnEntry("SOTHORYOS_WARRIOR", 10), new SpawnEntry("SOTHORYOS_BLOWGUNNER", 5), new SpawnEntry("SOTHORYOS_BANNER_BEARER", 2)));

    public enum Region { ARRYN, WILDLING, DOTHRAKI, JOGOS_NHAI, YI_TI, IBBEN, VOLANTIS, GHISCAR, BRAAVOS, LORATH, NORVOS, TYROSH, MYR, LYS, PENTOS, IRONBORN, WESTERLANDS, RIVERLANDS, DRAGONSTONE, STORMLANDS, REACH, DORNE, NORTH, SOTHORYOS }
    public record SpawnEntry(String role, int weight) {}
    private final GOTFaction faction; private final Region region; private final List<SpawnEntry> mobs;
    GOTInvasionType(GOTFaction faction, Region region, List<SpawnEntry> mobs) { this.faction=faction; this.region=region; this.mobs=List.copyOf(mobs); }
    public GOTFaction faction() { return faction; } public Region region() { return region; } public List<SpawnEntry> mobs() { return mobs; }
    public String codeName() { return name().toLowerCase(Locale.ROOT); }
    public static Optional<GOTInvasionType> byName(String s) { if(s==null)return Optional.empty(); String n=s.trim().toUpperCase(Locale.ROOT).replace('-','_'); try { return Optional.of(valueOf(n)); } catch(Exception e) { return Optional.empty(); } }
    public SpawnEntry randomEntry(RandomSource random) { int total=mobs.stream().mapToInt(SpawnEntry::weight).sum(); int r=random.nextInt(Math.max(1,total)); for(SpawnEntry e:mobs){ r-=e.weight(); if(r<0)return e; } return mobs.get(0); }
}
