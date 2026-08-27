package got.common.fasttravel;

import got.common.world.map.GOTWaypoint;
import net.minecraft.resources.ResourceLocation;

/** Maps modern Planetos biome IDs back to the legacy fast-travel region unlocked by entering them. */
public final class GOTFastTravelRegions {
    private GOTFastTravelRegions() {}

    public static GOTWaypoint.Region fromBiome(ResourceLocation biome) {
        if (biome == null || !"got".equals(biome.getNamespace())) return null;
        String p = biome.getPath();
        if (p.startsWith("north_") || p.equals("north") || p.equals("wolfswood") || p.equals("whispering_wood") || p.equals("stoney_shore")) return GOTWaypoint.Region.NORTH;
        if (p.startsWith("arryn")) return GOTWaypoint.Region.ARRYN;
        if (p.startsWith("riverlands") || p.equals("isle_of_faces")) return GOTWaypoint.Region.RIVERLANDS;
        if (p.startsWith("westerlands")) return GOTWaypoint.Region.WESTERLANDS;
        if (p.startsWith("reach")) return GOTWaypoint.Region.REACH;
        if (p.startsWith("stormlands")) return GOTWaypoint.Region.STORMLANDS;
        if (p.startsWith("dorne")) return GOTWaypoint.Region.DORNE;
        if (p.startsWith("iron_islands")) return GOTWaypoint.Region.IRONBORN;
        if (p.startsWith("crownlands") || p.startsWith("kingswood") || p.equals("massy") || p.equals("massy_hills")) return GOTWaypoint.Region.CROWNLANDS;
        if (p.startsWith("dragonstone")) return GOTWaypoint.Region.KINGS_LANDING;
        if (p.equals("haunted_forest") || p.startsWith("gift_") || p.equals("thenn_land") || p.equals("frozen_shore") || p.equals("frostfangs")) return GOTWaypoint.Region.BEYOND_WALL;
        if (p.startsWith("braavos")) return GOTWaypoint.Region.BRAAVOS;
        if (p.startsWith("pentos")) return GOTWaypoint.Region.PENTOS;
        if (p.equals("myr")) return GOTWaypoint.Region.SOUTHERN_FREE_CITIES;
        if (p.equals("tyrosh")) return GOTWaypoint.Region.SOUTHERN_FREE_CITIES;
        if (p.equals("lys")) return GOTWaypoint.Region.SOUTHERN_FREE_CITIES;
        if (p.startsWith("lorath")) return GOTWaypoint.Region.LORATH;
        if (p.equals("norvos")) return GOTWaypoint.Region.NORVOS;
        if (p.equals("qohor")) return GOTWaypoint.Region.QOHOR;
        if (p.startsWith("volantis")) return GOTWaypoint.Region.VOLANTIS;
        if (p.startsWith("dothraki")) return GOTWaypoint.Region.DOTHRAKI;
        if (p.startsWith("lhazar")) return GOTWaypoint.Region.LHAZAR;
        if (p.startsWith("ghiscar")) return GOTWaypoint.Region.GHISCAR;
        if (p.startsWith("qarth")) return GOTWaypoint.Region.QARTH;
        if (p.startsWith("yi_ti")) return GOTWaypoint.Region.YI_TI;
        if (p.startsWith("jogos_nhai")) return GOTWaypoint.Region.JOGOS_NHAI;
        if (p.startsWith("ibben")) return GOTWaypoint.Region.IBBEN;
        if (p.startsWith("mossovy")) return GOTWaypoint.Region.MOSSOVY;
        if (p.startsWith("summer")) return GOTWaypoint.Region.SUMMER;
        if (p.startsWith("sothoryos") || p.equals("yeen") || p.startsWith("cannibal_sands")) return GOTWaypoint.Region.SOTHORYOS;
        if (p.startsWith("asshai") || p.equals("shadow_land") || p.startsWith("shadow_mountains")) return GOTWaypoint.Region.ASSHAI;
        if (p.startsWith("ulthos")) return GOTWaypoint.Region.ULTHOS;
        if (p.startsWith("valyria")) return GOTWaypoint.Region.VALYRIA;
        if (p.startsWith("ocean") || p.equals("island") || p.startsWith("beach")) return GOTWaypoint.Region.OCEAN;
        return null;
    }
}
