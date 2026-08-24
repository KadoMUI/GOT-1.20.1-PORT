package got.world.biome;

import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.biome.BiomeSpecialEffects;
import net.minecraft.world.level.biome.MobSpawnSettings;

/** Modern builder counterpart to the original GOTBiomePreseter. */
public final class GOTBiomePreseter {
    private GOTBiomePreseter() {}

    public static Biome build(GOTBiomeMetadata data) {
        int sky = calculateSkyColor(data.temperature());
        BiomeSpecialEffects.Builder effects = new BiomeSpecialEffects.Builder()
                .waterColor(0x3F76E4)
                .waterFogColor(0x050533)
                .fogColor(0xC0D8FF)
                .skyColor(sky);

        return new Biome.BiomeBuilder()
                .hasPrecipitation(data.precipitation())
                .temperature(data.temperature())
                .downfall(data.downfall())
                .specialEffects(effects.build())
                .mobSpawnSettings(MobSpawnSettings.EMPTY)
                .generationSettings(BiomeGenerationSettings.EMPTY)
                .build();
    }

    private static int calculateSkyColor(float temperature) {
        float value = Math.max(-1.0F, Math.min(1.0F, temperature / 3.0F));
        return java.awt.Color.HSBtoRGB(0.62222224F - value * 0.05F, 0.5F + value * 0.1F, 1.0F) & 0xFFFFFF;
    }
}
