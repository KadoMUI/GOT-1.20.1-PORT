package got.world.terrain;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import got.world.biome.GOTBiomes;
import got.world.biome.PlanetosBiomeManager;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.biome.Climate;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Stream;

/** Atlas-backed biome source. Quart coordinates are converted back to blocks before lookup. */
public final class PlanetosBiomeSource extends BiomeSource {
    public static final Codec<PlanetosBiomeSource> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Biome.LIST_CODEC.fieldOf("biomes").forGetter(source -> source.biomes),
            Codec.LONG.optionalFieldOf("seed", 0L).forGetter(source -> source.seed)
    ).apply(instance, PlanetosBiomeSource::new));

    private final HolderSet<Biome> biomes;
    private final long seed;
    private final Map<ResourceKey<Biome>, Holder<Biome>> byKey = new LinkedHashMap<>();
    private final Holder<Biome> fallback;

    public PlanetosBiomeSource(HolderSet<Biome> biomes, long seed) {
        this.biomes = biomes;
        this.seed = seed;
        for (Holder<Biome> holder : biomes) {
            holder.unwrapKey().ifPresent(key -> byKey.put(key, holder));
        }
        this.fallback = byKey.getOrDefault(GOTBiomes.key("ocean"), biomes.get(0));
        PlanetosBiomeManager.initialize(seed);
    }

    @Override
    protected Codec<? extends BiomeSource> codec() {
        return CODEC;
    }

    @Override
    protected Stream<Holder<Biome>> collectPossibleBiomes() {
        return biomes.stream();
    }

    @Override
    public Holder<Biome> getNoiseBiome(int quartX, int quartY, int quartZ, Climate.Sampler sampler) {
        ResourceKey<Biome> key = PlanetosBiomeManager.getBiome(quartX << 2, quartZ << 2);
        return byKey.getOrDefault(key, fallback);
    }
}
