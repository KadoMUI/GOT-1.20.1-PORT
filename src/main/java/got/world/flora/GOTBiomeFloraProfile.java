package got.world.flora;

import net.minecraft.util.RandomSource;

import java.util.List;

/** Immutable decoration parameters for one Planetos biome family. */
public record GOTBiomeFloraProfile(
        float treesPerChunk,
        int grassPerChunk,
        int flowersPerChunk,
        int reedsPerChunk,
        int deadBushesPerChunk,
        int cactiPerChunk,
        int berryBushesPerChunk,
        int fallenLogsPerChunk,
        int bouldersPerChunk,
        int aquaticPlantsPerChunk,
        int snowAttemptsPerChunk,
        PlantTheme plantTheme,
        List<WeightedTree> trees
) {
    public enum PlantTheme { NORMAL, COLD, ARID, TROPICAL, MARSH, ASSHAI, YI_TI, VALYRIA }

    public record WeightedTree(GOTTreeSpecies species, int weight) {}

    public GOTTreeSpecies chooseTree(RandomSource random) {
        if (trees.isEmpty()) return GOTTreeSpecies.OAK;
        int total = trees.stream().mapToInt(WeightedTree::weight).sum();
        int selected = random.nextInt(Math.max(1, total));
        for (WeightedTree tree : trees) {
            selected -= tree.weight();
            if (selected < 0) return tree.species();
        }
        return trees.get(trees.size() - 1).species();
    }
}
