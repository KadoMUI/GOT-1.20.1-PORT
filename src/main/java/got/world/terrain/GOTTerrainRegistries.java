package got.world.terrain;

import com.mojang.serialization.Codec;
import got.GOTMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public final class GOTTerrainRegistries {
    private static final DeferredRegister<Codec<? extends BiomeSource>> BIOME_SOURCES =
            DeferredRegister.create(Registries.BIOME_SOURCE, GOTMod.MOD_ID);
    private static final DeferredRegister<Codec<? extends ChunkGenerator>> CHUNK_GENERATORS =
            DeferredRegister.create(Registries.CHUNK_GENERATOR, GOTMod.MOD_ID);

    public static final RegistryObject<Codec<? extends BiomeSource>> PLANETOS =
            BIOME_SOURCES.register("planetos", () -> PlanetosBiomeSource.CODEC);
    public static final RegistryObject<Codec<? extends ChunkGenerator>> PLANETOS_CHUNK_GENERATOR =
            CHUNK_GENERATORS.register("planetos", () -> PlanetosChunkGenerator.CODEC);

    private GOTTerrainRegistries() {}
    public static void register(IEventBus bus) {
        BIOME_SOURCES.register(bus);
        CHUNK_GENERATORS.register(bus);
    }
}
