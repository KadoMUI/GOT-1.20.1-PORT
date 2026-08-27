package got.quest;

import got.faction.GOTFaction;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;

import java.util.List;
import java.util.Map;
import java.util.Set;

/** Immutable, datapack-loaded quest definition. */
public record GOTQuestDefinition(
        ResourceLocation id,
        String titleKey,
        String descriptionKey,
        String offerKey,
        String progressKey,
        String completeKey,
        ResourceLocation icon,
        int color,
        int weight,
        boolean repeatable,
        int cooldownTicks,
        boolean sequential,
        boolean autoComplete,
        boolean legendary,
        float legacyRewardFactor,
        List<ResourceLocation> prerequisites,
        Giver giver,
        List<Objective> objectives,
        Reward reward
) {
    public GOTQuestDefinition {
        prerequisites = List.copyOf(prerequisites);
        objectives = List.copyOf(objectives);
    }

    public record Giver(
            Set<String> roles,
            Set<GOTFaction> factions,
            Set<ResourceLocation> entityTypes,
            float minimumAlignment,
            GOTFaction requiredMembership,
            int maximumActiveForFaction
    ) {
        public Giver {
            roles = Set.copyOf(roles);
            factions = Set.copyOf(factions);
            entityTypes = Set.copyOf(entityTypes);
        }
    }

    public record Objective(
            GOTQuestObjectiveType type,
            String target,
            String role,
            int count,
            int minimumCount,
            int maximumCount,
            ResourceLocation dimension,
            BlockPos position,
            double radius,
            boolean consume,
            String labelKey
    ) {}

    public record Reward(
            Map<GOTFaction, Float> alignment,
            List<ItemReward> items,
            int coins,
            int experience,
            boolean hireGiver,
            int hireAlignment
    ) {
        public Reward {
            alignment = Map.copyOf(alignment);
            items = List.copyOf(items);
        }
    }

    public record ItemReward(ResourceLocation item, int count) {}
}
