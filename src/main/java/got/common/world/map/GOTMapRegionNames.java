package got.common.world.map;

import got.world.biome.PlanetosBiomeManager;
import net.minecraft.network.chat.Component;

import java.util.Locale;
import java.util.Map;

/** Resolves the exact legacy GOT biome/region name beneath a map coordinate. */
public final class GOTMapRegionNames {
    private static final Map<String, String> TRANSLATION_ALIASES = Map.ofEntries(
            Map.entry("ocean1", "ocean"),
            Map.entry("ocean2", "ocean"),
            Map.entry("ocean3", "ocean"),
            Map.entry("valyria_sea1", "valyria_sea"),
            Map.entry("valyria_sea2", "valyria_sea"),
            Map.entry("kingswood_north", "kingswood"),
            Map.entry("kingswood_south", "kingswood")
    );

    private GOTMapRegionNames() {}

    public static Component atWorldPosition(int worldX, int worldZ) {
        String registryId = PlanetosBiomeManager.getBiome(worldX, worldZ).location().getPath();
        return forRegistryId(registryId);
    }

    public static Component forRegistryId(String registryId) {
        String normalized = registryId.toLowerCase(Locale.ROOT);
        String translationId = TRANSLATION_ALIASES.getOrDefault(normalized, normalized);
        return Component.translatable("got.biome." + snakeToLowerCamel(translationId) + ".name");
    }

    private static String snakeToLowerCamel(String value) {
        StringBuilder result = new StringBuilder(value.length());
        boolean capitalizeNext = false;
        for (int i = 0; i < value.length(); i++) {
            char character = value.charAt(i);
            if (character == '_') {
                capitalizeNext = true;
            } else if (capitalizeNext) {
                result.append(Character.toUpperCase(character));
                capitalizeNext = false;
            } else {
                result.append(character);
            }
        }
        return result.toString();
    }
}
