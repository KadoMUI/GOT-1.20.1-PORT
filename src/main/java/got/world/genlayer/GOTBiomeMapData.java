package got.world.genlayer;

import got.GOTMod;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import javax.imageio.ImageIO;

/** Loads the original atlas image and its exact legacy color-to-biome table. */
public final class GOTBiomeMapData {
    public static final int ORIGIN_X = 653;
    public static final int ORIGIN_Z = 870;
    public static final int SCALE = 128;
    private static final Map<Integer,Integer> COLOR_TO_ID = new HashMap<>();
    private static final Map<Integer,String> ID_TO_NAME = new HashMap<>();
    private static volatile BufferedImage image;
    private static volatile boolean loaded;
    private GOTBiomeMapData() {}
    public static synchronized void load() {
        if (loaded) return;
        try (InputStream csv = GOTBiomeMapData.class.getResourceAsStream("/data/got/worldgen/legacy_biome_map.csv")) {
            if (csv == null) throw new IllegalStateException("Missing legacy_biome_map.csv");
            try (BufferedReader r = new BufferedReader(new InputStreamReader(csv))) {
                String line; boolean first=true;
                while ((line=r.readLine())!=null) {
                    if (first){first=false;continue;} String[] p=line.split(","); if(p.length<4)continue;
                    int id=Integer.parseInt(p[0]); String name=p[2].trim().toLowerCase(Locale.ROOT); int rgb=Integer.parseInt(p[3].trim(),16);
                    COLOR_TO_ID.put(rgb,id); ID_TO_NAME.put(id,name);
                }
            }
        } catch (Exception e) { GOTMod.LOGGER.error("Failed loading GOT legacy biome color table",e); }
        try (InputStream in=GOTBiomeMapData.class.getResourceAsStream("/assets/got/textures/map/map.png")) {
            if(in==null) throw new IllegalStateException("Missing original map.png"); image=ImageIO.read(in);
        } catch(Exception e){ GOTMod.LOGGER.error("Failed loading GOT biome placement atlas",e); }
        loaded=true;
    }
    public static int legacyIdAtBlock(int blockX,int blockZ){load(); if(image==null)return 0;int px=Math.floorDiv(blockX,SCALE)+ORIGIN_X;int pz=Math.floorDiv(blockZ,SCALE)+ORIGIN_Z;return legacyIdAtPixel(px,pz);}
    public static int legacyIdAtPixel(int px,int pz){load();if(image==null||px<0||pz<0||px>=image.getWidth()||pz>=image.getHeight())return 0;int rgb=image.getRGB(px,pz)&0xFFFFFF;return COLOR_TO_ID.getOrDefault(rgb,0);}
    public static String registryId(int legacyId){load();return ID_TO_NAME.getOrDefault(legacyId,"ocean");}
    public static boolean isRiver(int legacyId){String n=registryId(legacyId);return n.equals("river")||n.endsWith("_river");}
    public static int fallbackLand(){load();for(var e:ID_TO_NAME.entrySet())if(e.getValue().equals("island"))return e.getKey();return 5;}
    public static int imageWidth(){load();return image==null?0:image.getWidth();}
    public static int imageHeight(){load();return image==null?0:image.getHeight();}
}
