package got.world.genlayer;

/** Deterministic modern counterpart of Minecraft 1.7.10 GenLayer. */
public abstract class GOTGenLayer {
    protected GOTGenLayer gotParent;
    private final long baseSeed;
    protected long worldGenSeed;
    protected long chunkSeed;

    protected GOTGenLayer(long seed) {
        long s = seed;
        s = mix(s, seed); s = mix(s, seed); s = mix(s, seed);
        this.baseSeed = s;
    }
    private static long mix(long value, long salt) { return value * value * 6364136223846793005L + value * 1442695040888963407L + salt; }
    public void initWorldGenSeed(long seed) {
        worldGenSeed = seed;
        if (gotParent != null) gotParent.initWorldGenSeed(seed);
        worldGenSeed = mix(worldGenSeed, baseSeed); worldGenSeed = mix(worldGenSeed, baseSeed); worldGenSeed = mix(worldGenSeed, baseSeed);
    }
    protected void initChunkSeed(long x, long z) {
        chunkSeed = worldGenSeed;
        chunkSeed = mix(chunkSeed, x); chunkSeed = mix(chunkSeed, z); chunkSeed = mix(chunkSeed, x); chunkSeed = mix(chunkSeed, z);
    }
    protected int nextInt(int bound) {
        int result = (int)((chunkSeed >> 24) % bound); if (result < 0) result += bound;
        chunkSeed = mix(chunkSeed, worldGenSeed); return result;
    }
    protected int selectRandom(int... values) { return values[nextInt(values.length)]; }
    protected int selectModeOrRandom(int a,int b,int c,int d) {
        if (b==c && c==d) return b; if (a==b && a==c) return a; if (a==b && a==d) return a; if (a==c && a==d) return a;
        if (a==b && c!=d) return a; if (a==c && b!=d) return a; if (a==d && b!=c) return a;
        if (b==c && a!=d) return b; if (b==d && a!=c) return b; if (c==d && a!=b) return c;
        return selectRandom(a,b,c,d);
    }
    public abstract int[] getInts(int x, int z, int width, int height);
}
