package got.world.resource;

import got.GOTBlocks;
import got.GOTMod;
import got.world.biome.GOTBiomeMetadata;
import got.world.biome.GOTBiomePreset;
import got.world.biome.GOTLegacyTerrainCatalog;
import got.world.biome.PlanetosBiomeManager;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * Legacy Planetos underground decoration, scaled onto the modern -64..319
 * build height. Generation is deterministic per chunk and intentionally
 * independent from the flora random stream.
 */
public final class PlanetosResourceGenerator {
    private static final long RESOURCE_SALT = 0x5245534f55524345L;
    private static final int UPDATE_FLAGS = Block.UPDATE_CLIENTS | Block.UPDATE_KNOWN_SHAPE;

    private PlanetosResourceGenerator() {}

    public static void generate(WorldGenLevel level, ChunkAccess chunk, long generatorSeed) {
        int minX = chunk.getPos().getMinBlockX();
        int minZ = chunk.getPos().getMinBlockZ();
        GOTBiomeMetadata center = PlanetosBiomeManager.getMetadata(minX + 8, minZ + 8);
        if (center == null) return;

        RandomSource random = RandomSource.create(mix(generatorSeed, chunk.getPos().x, chunk.getPos().z));
        Factors factors = factors(center);

        // Original soil and country-rock catalogue.
        veins(level, random, minX, minZ, 40.0F, 32, -64, 128,
                ignored -> Blocks.DIRT.defaultBlockState(), Target.STONE);
        veins(level, random, minX, minZ, 20.0F, 32, -64, 128,
                ignored -> Blocks.GRAVEL.defaultBlockState(), Target.STONE);
        rock(level, random, minX, minZ, "basalt", Blocks.BASALT.defaultBlockState(), 30, 12, -64, 10);
        rock(level, random, minX, minZ, "", Blocks.ANDESITE.defaultBlockState(), 30, 12, -64, 64);
        rock(level, random, minX, minZ, "rhyolite", Blocks.DIORITE.defaultBlockState(), 30, 12, -64, 64);
        rock(level, random, minX, minZ, "", Blocks.DIORITE.defaultBlockState(), 30, 12, -64, 64);
        rock(level, random, minX, minZ, "", Blocks.GRANITE.defaultBlockState(), 30, 12, -64, 64);
        rock(level, random, minX, minZ, "marble", Blocks.CALCITE.defaultBlockState(), 30, 12, -64, 64);
        rock(level, random, minX, minZ, "labradorite", Blocks.TUFF.defaultBlockState(), 30, 12, -64, 64);

        // Common metallic and chemical resources.
        ore(level, random, minX, minZ, 15.0F * factors.ore(), 8, -64, 128,
                state -> state.is(Blocks.DEEPSLATE) ? Blocks.DEEPSLATE_COPPER_ORE.defaultBlockState()
                        : Blocks.COPPER_ORE.defaultBlockState());
        ore(level, random, minX, minZ, 15.0F * factors.ore(), 8, -64, 128,
                state -> state.is(Blocks.DEEPSLATE) ? GOTBlocks.DEEPSLATE_TIN_ORE.get().defaultBlockState()
                        : GOTBlocks.TIN_ORE.get().defaultBlockState());
        ore(level, random, minX, minZ, 40.0F * factors.ore(), 16, -64, 128,
                state -> state.is(Blocks.DEEPSLATE) ? Blocks.DEEPSLATE_COAL_ORE.defaultBlockState()
                        : Blocks.COAL_ORE.defaultBlockState());
        ore(level, random, minX, minZ, 20.0F * factors.ore(), 8, -64, 64,
                state -> state.is(Blocks.DEEPSLATE) ? Blocks.DEEPSLATE_IRON_ORE.defaultBlockState()
                        : Blocks.IRON_ORE.defaultBlockState());
        ore(level, random, minX, minZ, 2.0F * factors.ore(), 8, -64, 64,
                ignored -> GOTBlocks.SULFUR_ORE.get().defaultBlockState());
        ore(level, random, minX, minZ, 2.0F * factors.ore(), 8, -64, 64,
                ignored -> GOTBlocks.SALTPETER_ORE.get().defaultBlockState());
        ore(level, random, minX, minZ, 2.0F * factors.ore(), 12, -64, 64,
                ignored -> GOTBlocks.SALT_ORE.get().defaultBlockState());
        ore(level, random, minX, minZ, 1.0F * factors.ore(), 6, -64, 48,
                state -> state.is(Blocks.DEEPSLATE) ? Blocks.DEEPSLATE_LAPIS_ORE.defaultBlockState()
                        : Blocks.LAPIS_ORE.defaultBlockState());
        ore(level, random, minX, minZ, 8.0F * factors.ore(), 4, -64, 32,
                state -> state.is(Blocks.DEEPSLATE) ? GOTBlocks.DEEPSLATE_SILVER_ORE.get().defaultBlockState()
                        : GOTBlocks.SILVER_ORE.get().defaultBlockState());
        ore(level, random, minX, minZ, 2.0F * factors.ore(), 4, -64, 32,
                state -> state.is(Blocks.DEEPSLATE) ? Blocks.DEEPSLATE_GOLD_ORE.defaultBlockState()
                        : Blocks.GOLD_ORE.defaultBlockState());

        // The original gem factor is one half in ordinary country and much
        // richer in Sothoryos, Ulthos, and mountain presets.
        gem(level, random, minX, minZ, 4.0F * factors.gem(), GOTBlocks.TOPAZ_ORE.get().defaultBlockState());
        gem(level, random, minX, minZ, 4.0F * factors.gem(), GOTBlocks.AMETHYST_ORE.get().defaultBlockState());
        gem(level, random, minX, minZ, 4.0F * factors.gem(), GOTBlocks.SAPPHIRE_ORE.get().defaultBlockState());
        gem(level, random, minX, minZ, 4.0F * factors.gem(), GOTBlocks.RUBY_ORE.get().defaultBlockState());
        gem(level, random, minX, minZ, 4.0F * factors.gem(), GOTBlocks.AMBER_ORE.get().defaultBlockState());
        gem(level, random, minX, minZ, 2.0F * factors.gem(), GOTBlocks.DIAMOND_VEIN.get().defaultBlockState());
        gem(level, random, minX, minZ, 4.0F * factors.gem(), GOTBlocks.OPAL_ORE.get().defaultBlockState());
        gem(level, random, minX, minZ, 3.0F * factors.gem(), GOTBlocks.EMERALD_VEIN.get().defaultBlockState());

        regionalDeposits(level, random, minX, minZ, center, factors);
    }

    private static void regionalDeposits(WorldGenLevel level, RandomSource random, int minX, int minZ,
                                         GOTBiomeMetadata metadata, Factors factors) {
        String id = metadata.id();
        if (id.startsWith("sothoryos_") || id.startsWith("ulthos_")) {
            BlockState obsidianGravel = registered("obsidian_gravel", Blocks.GRAVEL.defaultBlockState());
            veins(level, random, minX, minZ, 20.0F, 32, -64, 64,
                    ignored -> obsidianGravel, Target.STONE);
        }

        if (metadata.preset() == GOTBiomePreset.MOUNTAINS || id.equals("dorne_mesa")) {
            ore(level, random, minX, minZ, 8.0F * factors.ore(), 4, -64, 48,
                    ignored -> GOTBlocks.GLOWSTONE_ORE.get().defaultBlockState());
            ore(level, random, minX, minZ, 5.0F * factors.ore(), 5, -64, 32,
                    ignored -> GOTBlocks.COBALT_ORE.get().defaultBlockState());
        }

        // Tarth's exceptional sapphire beds are distinct from the default gem pass.
        if (id.equals("stormlands_tarth") || id.equals("stormlands_tarth_forest")) {
            veins(level, random, minX, minZ, 10.0F, 2, -64, 50,
                    ignored -> GOTBlocks.SAPPHIRE_ORE.get().defaultBlockState(), Target.STONE);
        }

        if (GOTLegacyTerrainCatalog.isAquatic(id)) {
            veins(level, random, minX, minZ, 4.0F, 8, -64, 48,
                    ignored -> GOTBlocks.SALT_ORE.get().defaultBlockState(), Target.STONE);
            veins(level, random, minX, minZ, 0.5F, 8, 48, 80,
                    ignored -> GOTBlocks.SALT_ORE.get().defaultBlockState(), Target.SAND);
        }

        if (id.equals("valyria_volcano")) {
            veins(level, random, minX, minZ, 2.0F, 3, -64, 16,
                    ignored -> GOTBlocks.VALYRIAN_ORE.get().defaultBlockState(), Target.STONE);
        }
    }

    private static void rock(WorldGenLevel level, RandomSource random, int minX, int minZ,
                             String id, BlockState fallback, float attempts, int size, int minY, int maxY) {
        BlockState rock = id.isEmpty() ? fallback : registered(id, fallback);
        veins(level, random, minX, minZ, attempts, size, minY, maxY, ignored -> rock, Target.STONE);
    }

    private static void ore(WorldGenLevel level, RandomSource random, int minX, int minZ,
                            float attempts, int size, int minY, int maxY, StateProvider provider) {
        veins(level, random, minX, minZ, attempts, size, minY, maxY, provider, Target.STONE);
    }

    private static void gem(WorldGenLevel level, RandomSource random, int minX, int minZ,
                            float attempts, BlockState gem) {
        veins(level, random, minX, minZ, attempts, 4, -64, 32, ignored -> gem, Target.STONE);
    }

    private static void veins(WorldGenLevel level, RandomSource random, int minX, int minZ,
                              float expectedAttempts, int size, int minY, int maxY,
                              StateProvider provider, Target target) {
        int attempts = (int)Math.floor(expectedAttempts);
        if (random.nextFloat() < expectedAttempts - attempts) attempts++;
        int lower = Math.max(level.getMinBuildHeight(), minY);
        int upper = Math.min(level.getMaxBuildHeight() - 1, maxY);
        if (upper < lower) return;

        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
        for (int attempt = 0; attempt < attempts; attempt++) {
            int x = minX + random.nextInt(16);
            int y = lower + random.nextInt(upper - lower + 1);
            int z = minZ + random.nextInt(16);
            for (int step = 0; step < size; step++) {
                replace(level, pos, x, y, z, provider, target);
                if (random.nextFloat() < 0.35F) {
                    int px = x + random.nextInt(3) - 1;
                    int py = y + random.nextInt(3) - 1;
                    int pz = z + random.nextInt(3) - 1;
                    if (px >= minX && px < minX + 16 && pz >= minZ && pz < minZ + 16
                            && py >= lower && py <= upper) {
                        replace(level, pos, px, py, pz, provider, target);
                    }
                }
                x = Math.max(minX, Math.min(minX + 15, x + random.nextInt(3) - 1));
                y = Math.max(lower, Math.min(upper, y + random.nextInt(3) - 1));
                z = Math.max(minZ, Math.min(minZ + 15, z + random.nextInt(3) - 1));
            }
        }
    }

    private static void replace(WorldGenLevel level, BlockPos.MutableBlockPos pos,
                                int x, int y, int z, StateProvider provider, Target target) {
        pos.set(x, y, z);
        BlockState existing = level.getBlockState(pos);
        if (target.matches(existing)) {
            level.setBlock(pos, provider.stateFor(existing), UPDATE_FLAGS);
        }
    }

    private static Factors factors(GOTBiomeMetadata metadata) {
        float ore = 1.0F;
        float gem = 0.5F;
        if (metadata.id().startsWith("sothoryos_")) {
            ore = 2.0F;
            gem = 2.0F;
        } else if (metadata.id().startsWith("ulthos_")) {
            ore = 3.0F;
            gem = 3.0F;
        }
        if (metadata.preset() == GOTBiomePreset.MOUNTAINS) {
            ore *= 2.0F;
            gem *= 2.0F;
        }
        return new Factors(ore, gem);
    }

    private static BlockState registered(String id, BlockState fallback) {
        Block block = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(GOTMod.MOD_ID, id));
        return block == null || block == Blocks.AIR ? fallback : block.defaultBlockState();
    }

    private static long mix(long seed, int chunkX, int chunkZ) {
        long mixed = seed ^ RESOURCE_SALT ^ (long)chunkX * 341873128712L ^ (long)chunkZ * 132897987541L;
        mixed ^= mixed >>> 33;
        mixed *= 0xff51afd7ed558ccdL;
        mixed ^= mixed >>> 33;
        mixed *= 0xc4ceb9fe1a85ec53L;
        return mixed ^ mixed >>> 33;
    }

    private enum Target {
        STONE {
            @Override boolean matches(BlockState state) {
                return state.is(Blocks.STONE) || state.is(Blocks.DEEPSLATE);
            }
        },
        SAND {
            @Override boolean matches(BlockState state) {
                return state.is(Blocks.SAND) || state.is(Blocks.RED_SAND)
                        || state.is(registeredBlock("white_sand"));
            }
        };

        abstract boolean matches(BlockState state);

        private static Block registeredBlock(String id) {
            Block block = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(GOTMod.MOD_ID, id));
            return block == null ? Blocks.AIR : block;
        }
    }

    @FunctionalInterface
    private interface StateProvider {
        BlockState stateFor(BlockState existing);
    }

    private record Factors(float ore, float gem) {}
}
