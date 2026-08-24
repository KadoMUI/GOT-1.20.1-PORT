package got.world.biome;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

/** Generator-facing metadata retained separately from Minecraft's Biome object. */
public record GOTBiomeMetadata(
        String id,
        GOTBiomePreset preset,
        GOTClimateType climate,
        float temperature,
        float downfall,
        boolean precipitation,
        float baseHeight,
        float heightVariation,
        BlockState surface,
        BlockState filler,
        float treeDensity,
        float grassDensity,
        float flowerDensity,
        float oreMultiplier,
        boolean allowsLakes,
        boolean allowsSettlements,
        boolean allowsRoads
) {
    public static GOTBiomeMetadata defaults(String id, GOTBiomePreset preset) {
        float temperature;
        float downfall;
        boolean precipitation = true;
        float baseHeight = 0.1F;
        float variation = 0.2F;
        float trees = 0.1F;
        float grass = 0.5F;
        float flowers = 0.1F;
        BlockState surface = Blocks.GRASS_BLOCK.defaultBlockState();
        BlockState filler = Blocks.DIRT.defaultBlockState();
        GOTClimateType climate = GOTClimateType.NORMAL;

        switch (preset) {
            case DESERT -> { temperature = 1.8F; downfall = 0.0F; precipitation = false; surface = Blocks.SAND.defaultBlockState(); filler = Blocks.SANDSTONE.defaultBlockState(); climate = GOTClimateType.SUMMER; trees = 0.0F; grass = 0.02F; flowers = 0.0F; }
            case DESERT_COLD -> { temperature = 0.25F; downfall = 0.05F; surface = Blocks.SAND.defaultBlockState(); filler = Blocks.SANDSTONE.defaultBlockState(); climate = GOTClimateType.COLD; trees = 0.0F; grass = 0.02F; flowers = 0.0F; }
            case FROST, POLAR -> { temperature = -0.5F; downfall = 0.5F; climate = GOTClimateType.WINTER; surface = Blocks.SNOW_BLOCK.defaultBlockState(); filler = Blocks.DIRT.defaultBlockState(); trees = 0.02F; grass = 0.05F; flowers = 0.0F; }
            case TAIGA -> { temperature = 0.2F; downfall = 0.8F; climate = GOTClimateType.COLD; trees = 0.8F; grass = 0.4F; flowers = 0.05F; }
            case SAVANNAH, BUSHLAND -> { temperature = 1.2F; downfall = 0.15F; climate = GOTClimateType.SUMMER; trees = 0.15F; grass = 0.8F; flowers = 0.05F; }
            case JUNGLE -> { temperature = 1.0F; downfall = 1.0F; climate = GOTClimateType.SUMMER; trees = 1.5F; grass = 1.0F; flowers = 0.3F; }
            case MOUNTAINS -> { temperature = 0.35F; downfall = 0.5F; climate = GOTClimateType.NORMAL_AZ; baseHeight = 1.0F; variation = 0.8F; surface = Blocks.STONE.defaultBlockState(); filler = Blocks.STONE.defaultBlockState(); trees = 0.08F; grass = 0.15F; flowers = 0.02F; }
            case MARSHES -> { temperature = 0.8F; downfall = 0.9F; climate = GOTClimateType.NORMAL; baseHeight = -0.1F; variation = 0.05F; trees = 0.25F; grass = 1.0F; flowers = 0.25F; }
            case FOREST -> { temperature = 0.7F; downfall = 0.8F; climate = GOTClimateType.NORMAL; trees = 1.0F; grass = 0.6F; flowers = 0.2F; }
            case NORTHERN_PLAINS -> { temperature = 0.35F; downfall = 0.6F; climate = GOTClimateType.COLD; trees = 0.12F; grass = 0.7F; flowers = 0.08F; }
            case SOUTHERN_PLAINS -> { temperature = 1.0F; downfall = 0.45F; climate = GOTClimateType.SUMMER; trees = 0.15F; grass = 0.8F; flowers = 0.2F; }
            case AQUATIC -> { temperature = 0.5F; downfall = 0.5F; climate = GOTClimateType.NORMAL; baseHeight = -1.0F; variation = 0.1F; surface = Blocks.GRAVEL.defaultBlockState(); filler = Blocks.STONE.defaultBlockState(); trees = 0.0F; grass = 0.0F; flowers = 0.0F; }
            default -> { temperature = 0.7F; downfall = 0.6F; }
        }
        GOTLegacyTerrainCatalog.TerrainSpec terrain = GOTLegacyTerrainCatalog.forBiome(id);
        baseHeight = terrain.baseHeight();
        variation = terrain.variation();
        surface = terrain.surface();
        filler = terrain.filler();
        boolean aquatic = terrain.aquatic();
        return new GOTBiomeMetadata(id, preset, climate, temperature, downfall, precipitation,
                baseHeight, variation, surface, filler, trees, grass, flowers, 1.0F,
                true, !aquatic, !aquatic);
    }
}
