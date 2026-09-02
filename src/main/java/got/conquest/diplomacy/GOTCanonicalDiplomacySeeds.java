package got.conquest.diplomacy;

import got.conquest.pact.GOTCanonicalPacts;

import java.util.List;
import java.util.UUID;

/** Initial War of the Five Kings attitudes for the five canonical NPC Pacts. */
public final class GOTCanonicalDiplomacySeeds {
    public static final int DEFINITION_VERSION = 1;

    public record Seed(UUID pactA, UUID pactB,
                       int opinionAToB, int opinionBToA,
                       int trustAToB, int trustBToA,
                       int fearAToB, int fearBToA,
                       GOTCommunicationPolicy communicationAToB,
                       GOTCommunicationPolicy communicationBToA) {
        public GOTDiplomaticRelationState create() {
            GOTDiplomaticRelationState relation = new GOTDiplomaticRelationState(pactA, pactB);
            // Preserve the meaning of A/B even when UUID canonical ordering differs.
            relation.setOpinion(pactA, pactB, opinionAToB, 0L);
            relation.setOpinion(pactB, pactA, opinionBToA, 0L);
            relation.setTrust(pactA, pactB, trustAToB, 0L);
            relation.setTrust(pactB, pactA, trustBToA, 0L);
            relation.setFear(pactA, pactB, fearAToB, 0L);
            relation.setFear(pactB, pactA, fearBToA, 0L);
            relation.setCommunication(pactA, pactB, communicationAToB, 0L);
            relation.setCommunication(pactB, pactA, communicationBToA, 0L);
            return relation;
        }
    }

    private static final UUID NORTH = GOTCanonicalPacts.deterministicId(GOTCanonicalPacts.NORTH_AND_TRIDENT);
    private static final UUID RENLY = GOTCanonicalPacts.deterministicId(GOTCanonicalPacts.RENLY_TYRELL);
    private static final UUID DRAGONSTONE = GOTCanonicalPacts.deterministicId(GOTCanonicalPacts.STANNIS_VELARYON);
    private static final UUID THRONE = GOTCanonicalPacts.deterministicId(GOTCanonicalPacts.IRON_THRONE);
    private static final UUID ISLANDS = GOTCanonicalPacts.deterministicId(GOTCanonicalPacts.IRON_ISLANDS);

    private static final List<Seed> SEEDS = List.of(
            // North and Iron Throne openly despise one another.
            seed(NORTH, THRONE, -800, -850),

            // Renly is friendly toward Dragonstone; Stannis dislikes Renly but remains willing to negotiate.
            seed(RENLY, DRAGONSTONE, 300, -300),

            // Both Baratheon camps hate the Iron Throne, with Dragonstone's hatred the strongest.
            seed(RENLY, THRONE, -800, -850),
            seed(DRAGONSTONE, THRONE, -1000, -950),

            // Dragonstone sees the North as a problem for later, not a current enemy.
            seed(DRAGONSTONE, NORTH, 0, 0),

            // North and Renly have no seeded grievance against one another yet.
            seed(NORTH, RENLY, 0, 0),

            // Everyone hates the Iron Islands; Balon's camp hates the North most of all.
            seed(NORTH, ISLANDS, -900, -1000),
            seed(RENLY, ISLANDS, -650, -500),
            seed(DRAGONSTONE, ISLANDS, -600, -450),
            seed(THRONE, ISLANDS, -800, -700)
    );

    private GOTCanonicalDiplomacySeeds() {}

    public static List<Seed> seeds() { return SEEDS; }

    private static Seed seed(UUID a, UUID b, int aToB, int bToA) {
        return new Seed(a, b, aToB, bToA, 0, 0, 0, 0,
                GOTCommunicationPolicy.OPEN, GOTCommunicationPolicy.OPEN);
    }
}
