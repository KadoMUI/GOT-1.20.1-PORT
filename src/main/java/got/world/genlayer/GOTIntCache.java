package got.world.genlayer;

/** Thread-local integer scratch arrays replacing the old client/server cache split. */
public final class GOTIntCache {
    private static final ThreadLocal<GOTIntCache> LOCAL = ThreadLocal.withInitial(GOTIntCache::new);
    public static GOTIntCache get() { return LOCAL.get(); }
    public int[] getIntArray(int size) { return new int[size]; }
    public void resetIntCache() { }
}
