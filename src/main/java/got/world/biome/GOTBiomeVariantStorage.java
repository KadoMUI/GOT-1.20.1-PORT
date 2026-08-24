package got.world.biome;
import java.util.concurrent.ConcurrentHashMap;
public final class GOTBiomeVariantStorage{private final ConcurrentHashMap<Long,GOTBiomeVariant> cache=new ConcurrentHashMap<>();public GOTBiomeVariant get(int x,int z){return cache.get(key(x,z));}public void put(int x,int z,GOTBiomeVariant v){cache.put(key(x,z),v);}public GOTBiomeVariant computeIfAbsent(int x,int z,java.util.function.Supplier<GOTBiomeVariant>s){return cache.computeIfAbsent(key(x,z),k->s.get());}public void clear(){cache.clear();}private static long key(int x,int z){return ((long)x<<32)^(z&0xffffffffL);}}
