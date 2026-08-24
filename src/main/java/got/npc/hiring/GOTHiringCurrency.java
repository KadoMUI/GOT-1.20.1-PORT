package got.npc.hiring;

import net.minecraft.resources.ResourceLocation;

/**
 * Currency descriptor for hiring. The actual item lookup is intentionally
 * registry-based so the catalog can target the port's existing coin items.
 */
public record GOTHiringCurrency(ResourceLocation itemId, int amount) {
    public GOTHiringCurrency {
        if (amount < 0) throw new IllegalArgumentException("amount must be >= 0");
    }
}
