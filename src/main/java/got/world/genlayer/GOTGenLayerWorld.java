package got.world.genlayer;

/** Authoritative biome source backed by the original 5291x5067 color atlas. */
public final class GOTGenLayerWorld extends GOTGenLayer {
    public static final int ORIGIN_X=GOTBiomeMapData.ORIGIN_X, ORIGIN_Z=GOTBiomeMapData.ORIGIN_Z, SCALE=GOTBiomeMapData.SCALE;
    public GOTGenLayerWorld(){super(0L);GOTBiomeMapData.load();}
    public static GOTGenLayer[] createWorld(long seed){
        GOTGenLayer biome=new GOTGenLayerWorld(); biome.initWorldGenSeed(seed);
        GOTGenLayer coarse=new GOTGenLayerBiomeVariants(200L); coarse=GOTGenLayerZoom.magnify(200L,coarse,8);coarse.initWorldGenSeed(seed);
        GOTGenLayer fine=new GOTGenLayerBiomeVariants(300L); fine=GOTGenLayerZoom.magnify(300L,fine,6);fine.initWorldGenSeed(seed);
        GOTGenLayer lakes=new GOTGenLayerBiomeVariantsLake(100L,null,0).setLakeFlags(1);lakes.initWorldGenSeed(seed);
        GOTGenLayer rivers=new GOTGenLayerRiverInit(100L);rivers=GOTGenLayerZoom.magnify(1000L,rivers,10);rivers=new GOTGenLayerRiver(1L,rivers);rivers=new GOTGenLayerSmooth(1000L,rivers);rivers.initWorldGenSeed(seed);
        return new GOTGenLayer[]{biome,coarse,fine,lakes,rivers};
    }
    public int[] getInts(int x,int z,int w,int h){int[] o=new int[w*h];for(int dz=0;dz<h;dz++)for(int dx=0;dx<w;dx++)o[dx+dz*w]=GOTBiomeMapData.legacyIdAtBlock(x+dx,z+dz);return o;}
    public static int getBiomeOrOcean(int blockX,int blockZ){return GOTBiomeMapData.legacyIdAtBlock(blockX,blockZ);}
    public static boolean loadedBiomeImage(){GOTBiomeMapData.load();return GOTBiomeMapData.imageWidth()>0;}
    public static int getImageWidth(){return GOTBiomeMapData.imageWidth();} public static int getImageHeight(){return GOTBiomeMapData.imageHeight();}
}
