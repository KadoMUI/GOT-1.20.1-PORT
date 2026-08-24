package got.world.structure.north;

import got.GOTMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ForgeRegistries;

/** Modern block-state translation of the legacy North kingdom palette. */
public record NorthStructurePalette(
        BlockState stone,
        BlockState crackedStone,
        BlockState mossyStone,
        BlockState stoneStairs,
        BlockState stoneSlab,
        BlockState stoneWall,
        BlockState log,
        BlockState planks,
        BlockState woodStairs,
        BlockState woodSlab,
        BlockState fence,
        BlockState fenceGate,
        BlockState door,
        BlockState daub,
        BlockState roof,
        BlockState roofStairs,
        BlockState roofSlab
) {
    private static final String[] NORTH_WOODS = {"beech", "cedar", "aramant", "oak", "birch"};

    public static NorthStructurePalette create(long seed) {
        String wood = NORTH_WOODS[(int)Math.floorMod(mix(seed), NORTH_WOODS.length)];
        boolean vanilla = wood.equals("oak") || wood.equals("birch");
        String prefix = vanilla ? "minecraft:" : "got:";
        String logId = prefix + wood + "_log";
        String plankId = prefix + wood + "_planks";
        String stairsId = vanilla ? prefix + wood + "_stairs" : "got:stairs_" + wood;
        String slabId = vanilla ? prefix + wood + "_slab" : "got:wood_slab_" + wood;
        String fenceId = prefix + wood + "_fence";
        String gateId = prefix + wood + "_fence_gate";
        String doorId = vanilla ? prefix + wood + "_door" : "got:door_" + wood;
        return new NorthStructurePalette(
                block("got:andesite_bricks", Blocks.STONE_BRICKS),
                block("got:cracked_andesite_bricks", Blocks.CRACKED_STONE_BRICKS),
                block("got:mossy_andesite_bricks", Blocks.MOSSY_STONE_BRICKS),
                block("got:stairs_andesite_brick", Blocks.STONE_BRICK_STAIRS),
                block("got:andesite_brick_slab", Blocks.STONE_BRICK_SLAB),
                block("got:andesite_brick_wall", Blocks.STONE_BRICK_WALL),
                block(logId, Blocks.SPRUCE_LOG),
                block(plankId, Blocks.SPRUCE_PLANKS),
                block(stairsId, Blocks.SPRUCE_STAIRS),
                block(slabId, Blocks.SPRUCE_SLAB),
                block(fenceId, Blocks.SPRUCE_FENCE),
                block(gateId, Blocks.SPRUCE_FENCE_GATE),
                block(doorId, Blocks.SPRUCE_DOOR),
                block("got:daub", Blocks.WHITE_TERRACOTTA),
                block("got:thatch_thatch", Blocks.HAY_BLOCK),
                block("got:stairs_thatch", Blocks.SPRUCE_STAIRS),
                block("got:thatch_slab", Blocks.SPRUCE_SLAB));
    }

    /** Exact spruce/cobble palette used by the legacy Gift and Night's Watch structures. */
    public static NorthStructurePalette gift(boolean abandoned) {
        return new NorthStructurePalette(
                block("got:cobblestone_bricks", Blocks.COBBLESTONE),
                Blocks.CRACKED_STONE_BRICKS.defaultBlockState(),
                Blocks.MOSSY_STONE_BRICKS.defaultBlockState(),
                Blocks.STONE_BRICK_STAIRS.defaultBlockState(),
                Blocks.STONE_BRICK_SLAB.defaultBlockState(),
                Blocks.STONE_BRICK_WALL.defaultBlockState(),
                abandoned ? block("got:wood_beam_rotten", Blocks.STRIPPED_SPRUCE_LOG)
                        : block("got:wood_beam_spruce", Blocks.STRIPPED_SPRUCE_LOG),
                abandoned ? block("got:rotten_planks", Blocks.SPRUCE_PLANKS)
                        : Blocks.SPRUCE_PLANKS.defaultBlockState(),
                abandoned ? block("got:stairs_rotten", Blocks.SPRUCE_STAIRS)
                        : Blocks.SPRUCE_STAIRS.defaultBlockState(),
                abandoned ? block("got:wood_slab_rotten", Blocks.SPRUCE_SLAB)
                        : Blocks.SPRUCE_SLAB.defaultBlockState(),
                abandoned ? block("got:fence_rotten", Blocks.SPRUCE_FENCE)
                        : Blocks.SPRUCE_FENCE.defaultBlockState(),
                abandoned ? block("got:fence_gate_rotten", Blocks.SPRUCE_FENCE_GATE)
                        : Blocks.SPRUCE_FENCE_GATE.defaultBlockState(),
                abandoned ? block("got:door_rotten", Blocks.SPRUCE_DOOR)
                        : Blocks.SPRUCE_DOOR.defaultBlockState(),
                block("got:daub", Blocks.WHITE_TERRACOTTA),
                abandoned ? Blocks.AIR.defaultBlockState()
                        : block("got:thatch_thatch", Blocks.HAY_BLOCK),
                abandoned ? block("got:stairs_rotten", Blocks.SPRUCE_STAIRS)
                        : block("got:stairs_thatch", Blocks.SPRUCE_STAIRS),
                abandoned ? block("got:wood_slab_rotten", Blocks.SPRUCE_SLAB)
                        : block("got:thatch_slab", Blocks.SPRUCE_SLAB));
    }

    public BlockState variedStone(long salt) {
        int value = (int)Math.floorMod(mix(salt), 12L);
        return value == 0 ? mossyStone : value <= 2 ? crackedStone : stone;
    }

    private static BlockState block(String id, Block fallback) {
        ResourceLocation location = new ResourceLocation(id);
        Block block = ForgeRegistries.BLOCKS.getValue(location);
        if (block == null || block == Blocks.AIR) {
            GOTMod.LOGGER.warn("Missing North structure palette block {}, using {}", id,
                    ForgeRegistries.BLOCKS.getKey(fallback));
            return fallback.defaultBlockState();
        }
        return block.defaultBlockState();
    }

    private static long mix(long value) {
        value ^= value >>> 33;
        value *= 0xff51afd7ed558ccdL;
        value ^= value >>> 33;
        value *= 0xc4ceb9fe1a85ec53L;
        return value ^ value >>> 33;
    }
}
