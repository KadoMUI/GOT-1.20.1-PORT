package got.conquest.economy;

import got.conquest.pact.GOTCanonicalPactDefinition;
import got.conquest.pact.GOTCanonicalPacts;
import net.minecraft.resources.ResourceLocation;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * Provisional War of the Five Kings economy baselines.
 *
 * These are intentionally modest and independent from territory. Later passes
 * can layer settlement taxes, trade, raiding, tribute and wartime costs over
 * the same treasury without migrating the save format again.
 */
public final class GOTCanonicalEconomySeeds {
    public static final int DEFINITION_VERSION = 1;

    private static final List<Seed> SEEDS = List.of(
            seed(GOTCanonicalPacts.NORTH_AND_TRIDENT, 6_144L, 128L),
            seed(GOTCanonicalPacts.RENLY_TYRELL,      8_192L, 192L),
            seed(GOTCanonicalPacts.STANNIS_VELARYON, 2_048L,  64L),
            seed(GOTCanonicalPacts.IRON_THRONE,      12_288L, 256L),
            seed(GOTCanonicalPacts.IRON_ISLANDS,     2_048L,  48L)
    );

    private static final Map<UUID, Seed> BY_ID = indexById();

    private GOTCanonicalEconomySeeds() {}

    public static List<Seed> seeds() { return SEEDS; }
    public static Optional<Seed> byPactId(UUID pactId) { return Optional.ofNullable(BY_ID.get(pactId)); }

    private static Seed seed(ResourceLocation key, long startingTreasury, long incomePerMinecraftDay) {
        GOTCanonicalPactDefinition pact = GOTCanonicalPacts.byKey(key)
                .orElseThrow(() -> new IllegalStateException("Missing canonical Pact economy target " + key));
        return new Seed(key, pact.id(), pact.name(), startingTreasury, incomePerMinecraftDay);
    }

    private static Map<UUID, Seed> indexById() {
        Map<UUID, Seed> map = new LinkedHashMap<>();
        for (Seed seed : SEEDS) {
            if (map.put(seed.pactId(), seed) != null) {
                throw new IllegalStateException("Duplicate canonical economy Pact id " + seed.pactId());
            }
        }
        return Map.copyOf(map);
    }

    public record Seed(ResourceLocation pactKey, UUID pactId, String pactName,
                       long startingTreasury, long incomePerMinecraftDay) {}
}
