package got.world.structure.north;

import net.minecraft.core.BlockPos;

/** Deterministic future hook for the later North NPC/faction population pass. */
public record NorthStructureMarker(String role, BlockPos position, int rotation) {}
