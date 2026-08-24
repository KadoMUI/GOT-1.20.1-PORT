package got.npc.hiring;

import net.minecraft.resources.ResourceLocation;

import java.util.Set;

/**
 * Exact 1.7.10 unit-trade definition for an unmounted unit.
 *
 * Cost is stored as legacy coin VALUE, not a concrete denomination.
 * The actual price is calculated dynamically from alignment and pledge state.
 */
public record GOTHireDefinition(
    ResourceLocation id,
    Set<String> roleIds,
    GOTHiredTask task,
    float requiredAlignment,
    boolean pledgeExclusive,
    int initialCost,
    String legacyEntity,
    String legacyGroup
) {}
