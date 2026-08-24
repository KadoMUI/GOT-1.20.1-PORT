package got.common.world.map;

import net.minecraft.network.chat.Component;

/**
 * Hierarchical labels for the Planetos atlas.
 *
 * <p>The atlas starts with continents and oceans.  Once the player crosses
 * from zoom power -1 to zoom power 0, those broad labels give way to the
 * kingdoms and geographic regions inside the landmass.  Small islands and
 * city-state regions wait until the next zoom step so the world view never
 * becomes a wall of text.</p>
 */
public enum GOTMapLabels {
    // Planetary view: continents and major islands.
    WESTEROS("westeros", 650, 1350, 30.0F, 67, -6.0F, -0.5F),
    ESSOS("essos", 2200, 1650, 50.0F, -10, -6.0F, -0.5F),
    SOTHORYOS("sothoryos", 2400, 3600, 40.0F, 60, -6.0F, -0.5F),
    ULTHOS("ulthos", 4144, 3540, 50.0F, 10, -6.0F, -0.5F),
    IBBEN("ibben", 2700, 1000, 15.0F, 0, -6.0F, -0.5F),
    LANG("lang", 3550, 2500, 15.0F, 67, -6.0F, -0.5F),

    // Planetary/continental view: named oceans and seas.
    NARROW_SEA("narrow", 1050, 1600, 9.0F, 70, -6.0F, 0.5F),
    JADE_SEA("jade", 3050, 3000, 10.0F, 0, -6.0F, 0.5F),
    SUMMER_SEA("summer", 2150, 2530, 15.0F, -10, -6.0F, 0.5F),
    SUNSET_SEA("sunset", 100, 1500, 15.0F, -70, -6.0F, 0.5F),
    SHIVERING_SEA("shivering", 2250, 500, 40.0F, 0, -6.0F, 0.5F),

    // Westeros: visible after zooming into the continent.
    BEYOND_THE_WALL("beyond_the_wall", 610, 450, 15.0F, 0, -0.5F, 4.0F),
    THE_GIFT("the_gift", 760, 665, 11.0F, 0, 0.5F, 4.0F),
    THE_NORTH("the_north", 660, 945, 18.0F, 0, -0.5F, 4.0F),
    SKAGOS("skagos_region", 950, 620, 9.0F, 0, 0.5F, 4.0F),
    THE_NECK("the_neck", 610, 1190, 11.0F, 0, 0.0F, 4.0F),
    IRON_ISLANDS("iron_islands", 330, 1330, 11.0F, 0, 0.0F, 4.0F),
    RIVERLANDS("riverlands", 650, 1445, 15.0F, 0, -0.5F, 4.0F),
    VALE_OF_ARRYN("vale_of_arryn", 885, 1330, 13.0F, 0, -0.5F, 4.0F),
    WESTERLANDS("westerlands_region", 450, 1565, 14.0F, 0, -0.5F, 4.0F),
    CROWNLANDS("crownlands", 805, 1575, 12.0F, 0, 0.0F, 4.0F),
    DRAGONSTONE("dragonstone", 920, 1550, 9.0F, 0, 1.0F, 4.0F),
    STORMLANDS("stormlands", 800, 1800, 14.0F, 0, -0.5F, 4.0F),
    THE_REACH("the_reach", 510, 1800, 17.0F, 0, -0.5F, 4.0F),
    DORNE("dorne", 700, 2005, 17.0F, 0, -0.5F, 4.0F),
    STEPSTONES("stepstones", 1060, 1955, 10.0F, 0, 0.5F, 4.0F),

    // Essos and the eastern lands.
    IBBEN_REGION("ibben_region", 2720, 1020, 13.0F, 0, -0.5F, 4.0F),
    BRAAVOS("braavos", 1220, 1365, 11.0F, 0, 0.0F, 4.0F),
    LORATH("lorath", 1525, 1330, 10.0F, 0, 0.5F, 4.0F),
    NORVOS("norvos", 1420, 1490, 11.0F, 0, 0.0F, 4.0F),
    QOHOR("qohor", 1600, 1645, 11.0F, 0, 0.0F, 4.0F),
    PENTOS("pentos", 1210, 1575, 11.0F, 0, 0.0F, 4.0F),
    MYR("myr", 1355, 1790, 10.0F, 0, 0.5F, 4.0F),
    TYROSH("tyrosh", 1105, 1880, 9.0F, 0, 1.0F, 4.0F),
    LYS("lys", 1205, 2050, 9.0F, 0, 1.0F, 4.0F),
    DISPUTED_LANDS("disputed_lands", 1300, 1960, 12.0F, 0, 0.0F, 4.0F),
    VOLANTIS("volantis", 1560, 1980, 12.0F, 0, 0.0F, 4.0F),
    VALYRIA("valyria_region", 1785, 2350, 14.0F, 0, -0.5F, 4.0F),
    IFEKEVRON_FOREST("ifekevron_forest", 2565, 1350, 12.0F, 0, 0.0F, 4.0F),
    DOTHRAKI_SEA("dothraki_sea", 2290, 1640, 19.0F, 0, -0.5F, 4.0F),
    LHAZAR("lhazar", 2515, 1935, 12.0F, 0, 0.0F, 4.0F),
    GHISCAR("ghiscar", 2300, 2165, 15.0F, 0, -0.5F, 4.0F),
    QARTH("qarth", 2780, 2265, 13.0F, 0, -0.5F, 4.0F),
    BONE_MOUNTAINS("bone_mountains", 2970, 1750, 14.0F, 0, -0.5F, 4.0F),
    JOGOS_NHAI("jogos_nhai", 3415, 1645, 17.0F, 0, -0.5F, 4.0F),
    CANNIBAL_SANDS("cannibal_sands", 4130, 1780, 14.0F, 0, 0.0F, 4.0F),
    LAND_OF_THE_SHRYKES("land_of_the_shrykes", 3955, 1840, 12.0F, 0, 0.0F, 4.0F),
    YI_TI("yi_ti", 3440, 2170, 19.0F, 0, -0.5F, 4.0F),
    MOSSOVY("mossovy", 4505, 1470, 18.0F, 0, -0.5F, 4.0F),
    SHADOW_LANDS("shadow_lands", 4185, 2310, 18.0F, 0, -0.5F, 4.0F),
    ASSHAI("asshai", 3760, 2805, 11.0F, 0, 0.5F, 4.0F),
    LANG_REGION("lang_region", 3550, 2500, 13.0F, 0, 0.0F, 4.0F),
    SUMMER_ISLANDS("summer_islands", 1220, 2780, 13.0F, 0, 0.0F, 4.0F),
    NAATH("naath", 1715, 2785, 9.0F, 0, 1.0F, 4.0F),

    // Sothoryos and its colonial/coastal regions.
    GHISCARI_COLONY("ghiscari_colony", 2040, 2740, 11.0F, 0, 0.5F, 4.0F),
    QARTHI_COLONY("qarthi_colony", 2790, 3440, 11.0F, 0, 0.5F, 4.0F),
    SUMMER_COLONY("summer_colony", 1710, 3465, 11.0F, 0, 0.5F, 4.0F),
    GREEN_HELL("green_hell", 2290, 2915, 16.0F, 0, -0.5F, 4.0F),
    SOTHORYOS_SAVANNAH("sothoryos_savannah", 2460, 3740, 15.0F, 0, 0.0F, 4.0F),
    SOTHORYOS_DESERT("sothoryos_desert", 2800, 4250, 14.0F, 0, 0.0F, 4.0F),
    SOTHORYOS_POLAR_REGION("sothoryos_polar_region", 3200, 4610, 14.0F, 0, 0.0F, 4.0F),

    // Ulthos terrain regions become useful only at continental zoom.
    ULTHOS_FOREST("ulthos_forest", 3900, 3350, 15.0F, 0, 0.0F, 4.0F),
    ULTHOS_BUSHLAND("ulthos_bushland", 4450, 3400, 15.0F, 0, 0.0F, 4.0F),
    ULTHOS_MARSHES("ulthos_marshes", 4575, 3770, 11.0F, 0, 0.5F, 4.0F),
    ULTHOS_DESERT("ulthos_desert", 4560, 4120, 13.0F, 0, 0.0F, 4.0F),
    ULTHOS_POLAR_REGION("ulthos_polar_region", 4445, 4360, 14.0F, 0, 0.0F, 4.0F);

    private final String labelName;
    private final int posX;
    private final int posY;
    private final float scale;
    private final int angle;
    private float minZoom;
    private float maxZoom;

    GOTMapLabels(String labelName, int posX, int posY, float scale, int angle,
                 float minZoom, float maxZoom) {
        this.labelName = labelName;
        this.posX = posX;
        this.posY = posY;
        this.scale = scale;
        this.angle = angle;
        this.minZoom = minZoom;
        this.maxZoom = maxZoom;
    }

    public static GOTMapLabels[] allMapLabels() { return values(); }
    public int getAngle() { return angle; }
    public Component getDisplayName() { return Component.translatable("got.map.label." + labelName); }
    public float getMaxZoom() { return maxZoom; }
    public void setMaxZoom(float value) { maxZoom = value; }
    public float getMinZoom() { return minZoom; }
    public void setMinZoom(float value) { minZoom = value; }
    public int getPosX() { return posX; }
    public int getPosY() { return posY; }
    public float getScale() { return scale; }
}
