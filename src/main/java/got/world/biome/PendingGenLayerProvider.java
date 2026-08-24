package got.world.biome;
import net.minecraft.resources.ResourceKey;import net.minecraft.world.level.biome.Biome;
public interface PendingGenLayerProvider{ResourceKey<Biome> getBiome(int x,int z);GOTBiomeVariant getVariant(int x,int z);
 final class Holder{private static volatile PendingGenLayerProvider value=new DefaultPlanetosGenLayerProvider(0L);private Holder(){}}
 static PendingGenLayerProvider get(){return Holder.value;}static void set(PendingGenLayerProvider p){Holder.value=java.util.Objects.requireNonNull(p);}
}
