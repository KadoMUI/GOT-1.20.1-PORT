package got.world.structure.north;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.SlabType;

import java.util.ArrayList;
import java.util.List;

/** Rotation-aware, chunk-clipped procedural structure writer. */
public final class NorthStructureBuilder {
    private static final int FLAGS = Block.UPDATE_CLIENTS | Block.UPDATE_KNOWN_SHAPE;

    private final LevelAccessor level;
    private final int originX;
    private final int originY;
    private final int originZ;
    private final int rotation;
    private final int clipMinX;
    private final int clipMinZ;
    private final int clipMaxX;
    private final int clipMaxZ;
    private final long seed;
    private final NorthStructurePalette palette;
    private final List<NorthStructureMarker> markers;

    public NorthStructureBuilder(LevelAccessor level, int originX, int originY, int originZ,
                                 int rotation, long seed, NorthStructurePalette palette,
                                 int clipMinX, int clipMinZ, int clipMaxX, int clipMaxZ,
                                 List<NorthStructureMarker> markers) {
        this.level = level;
        this.originX = originX;
        this.originY = originY;
        this.originZ = originZ;
        this.rotation = Math.floorMod(rotation, 4);
        this.seed = seed;
        this.palette = palette;
        this.clipMinX = clipMinX;
        this.clipMinZ = clipMinZ;
        this.clipMaxX = clipMaxX;
        this.clipMaxZ = clipMaxZ;
        this.markers = markers == null ? new ArrayList<>() : markers;
    }

    public LevelAccessor level() { return level; }
    public NorthStructurePalette palette() { return palette; }
    public long seed() { return seed; }
    public int originY() { return originY; }
    public List<NorthStructureMarker> markers() { return markers; }

    public NorthStructureBuilder child(int x, int y, int z, int childRotation, long childSeed) {
        BlockPos childOrigin = worldPos(x, y, z);
        return new NorthStructureBuilder(level, childOrigin.getX(), childOrigin.getY(), childOrigin.getZ(),
                rotation + childRotation, childSeed, palette,
                clipMinX, clipMinZ, clipMaxX, clipMaxZ, markers);
    }

    public BlockPos worldPos(int x, int y, int z) {
        return switch (rotation) {
            case 1 -> new BlockPos(originX - z, originY + y, originZ + x);
            case 2 -> new BlockPos(originX - x, originY + y, originZ - z);
            case 3 -> new BlockPos(originX + z, originY + y, originZ - x);
            default -> new BlockPos(originX + x, originY + y, originZ + z);
        };
    }

    public Direction rotate(Direction direction) {
        if (!direction.getAxis().isHorizontal()) return direction;
        Direction result = direction;
        for (int i = 0; i < rotation; i++) result = result.getClockWise();
        return result;
    }

    public void set(int x, int y, int z, BlockState state) {
        BlockPos pos = worldPos(x, y, z);
        if (!insideClip(pos) || pos.getY() < level.getMinBuildHeight()
                || pos.getY() >= level.getMaxBuildHeight()) return;
        level.setBlock(pos, rotateState(state), FLAGS);
    }

    public void fill(int minX, int minY, int minZ, int maxX, int maxY, int maxZ, BlockState state) {
        for (int y = minY; y <= maxY; y++) {
            for (int z = minZ; z <= maxZ; z++) {
                for (int x = minX; x <= maxX; x++) set(x, y, z, state);
            }
        }
    }

    public void clear(int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
        fill(minX, minY, minZ, maxX, maxY, maxZ, Blocks.AIR.defaultBlockState());
    }

    public void foundation(int minX, int minZ, int maxX, int maxZ, BlockState state) {
        fill(minX, -1, minZ, maxX, -1, maxZ, state);
        for (int z = minZ; z <= maxZ; z++) {
            for (int x = minX; x <= maxX; x++) {
                for (int y = -2; y >= -8; y--) {
                    BlockPos pos = worldPos(x, y, z);
                    if (!insideClip(pos)) continue;
                    BlockState existing = level.getBlockState(pos);
                    if (!existing.isAir() && existing.getFluidState().isEmpty()
                            && !existing.canBeReplaced()) break;
                    level.setBlock(pos, state, FLAGS);
                }
            }
        }
    }

    public void hollowBox(int minX, int minY, int minZ, int maxX, int maxY, int maxZ,
                          BlockState wall, BlockState floor) {
        clear(minX, minY, minZ, maxX, maxY, maxZ);
        fill(minX, minY, minZ, maxX, minY, maxZ, floor);
        fill(minX, minY + 1, minZ, minX, maxY, maxZ, wall);
        fill(maxX, minY + 1, minZ, maxX, maxY, maxZ, wall);
        fill(minX + 1, minY + 1, minZ, maxX - 1, maxY, minZ, wall);
        fill(minX + 1, minY + 1, maxZ, maxX - 1, maxY, maxZ, wall);
    }

    public void timberFrame(int minX, int minZ, int maxX, int maxZ, int wallHeight) {
        BlockState log = palette.log();
        for (int[] corner : new int[][]{{minX,minZ},{minX,maxZ},{maxX,minZ},{maxX,maxZ}}) {
            fill(corner[0], 0, corner[1], corner[0], wallHeight, corner[1], log);
        }
        fill(minX, wallHeight, minZ, maxX, wallHeight, minZ, log);
        fill(minX, wallHeight, maxZ, maxX, wallHeight, maxZ, log);
    }

    public void gabledRoof(int halfWidth, int minZ, int maxZ, int wallHeight) {
        for (int step = 0; step <= halfWidth; step++) {
            int y = wallHeight + step;
            int left = -halfWidth - 1 + step;
            int right = halfWidth + 1 - step;
            BlockState leftStair = facing(palette.roofStairs(), Direction.WEST);
            BlockState rightStair = facing(palette.roofStairs(), Direction.EAST);
            fill(left, y, minZ - 1, left, y, maxZ + 1, leftStair);
            if (right != left) fill(right, y, minZ - 1, right, y, maxZ + 1, rightStair);
        }
        fill(0, wallHeight + halfWidth + 1, minZ - 1, 0, wallHeight + halfWidth + 1,
                maxZ + 1, topSlab(palette.roofSlab()));
    }

    public void door(int x, int y, int z, Direction facing) {
        BlockState lower = facing(palette.door(), facing);
        if (lower.hasProperty(BlockStateProperties.DOUBLE_BLOCK_HALF)) {
            lower = lower.setValue(BlockStateProperties.DOUBLE_BLOCK_HALF, DoubleBlockHalf.LOWER);
            set(x, y, z, lower);
            set(x, y + 1, z, lower.setValue(BlockStateProperties.DOUBLE_BLOCK_HALF, DoubleBlockHalf.UPPER));
        } else {
            set(x, y, z, Blocks.SPRUCE_DOOR.defaultBlockState()
                    .setValue(DoorBlock.FACING, facing)
                    .setValue(DoorBlock.HALF, DoubleBlockHalf.LOWER));
            set(x, y + 1, z, Blocks.SPRUCE_DOOR.defaultBlockState()
                    .setValue(DoorBlock.FACING, facing)
                    .setValue(DoorBlock.HALF, DoubleBlockHalf.UPPER));
        }
    }

    public void chest(int x, int y, int z, Direction facing, String lootTable) {
        BlockState chest = Blocks.CHEST.defaultBlockState().setValue(ChestBlock.FACING, rotate(facing));
        BlockPos pos = worldPos(x, y, z);
        if (!insideClip(pos)) return;
        level.setBlock(pos, chest, FLAGS);
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof RandomizableContainerBlockEntity container) {
            container.setLootTable(new ResourceLocation("got", "chests/" + lootTable),
                    seed ^ pos.asLong());
        }
    }

    public void marker(String role, int x, int y, int z) {
        BlockPos pos = worldPos(x, y, z);
        // Large settlements are rebuilt once per intersecting chunk. A marker
        // belongs only to the chunk that contains its world position; without
        // this clip every household is duplicated for every settlement chunk.
        if (!insideClip(pos)) return;
        markers.add(new NorthStructureMarker(role, pos, rotation));
    }

    public void path(int minX, int minZ, int maxX, int maxZ) {
        fill(minX, -1, minZ, maxX, -1, maxZ, Blocks.COARSE_DIRT.defaultBlockState());
        clear(minX, 0, minZ, maxX, 2, maxZ);
    }

    public static BlockState facing(BlockState state, Direction direction) {
        if (state.hasProperty(BlockStateProperties.HORIZONTAL_FACING)) {
            return state.setValue(BlockStateProperties.HORIZONTAL_FACING, direction);
        }
        return state;
    }

    public static BlockState topSlab(BlockState state) {
        return state.hasProperty(BlockStateProperties.SLAB_TYPE)
                ? state.setValue(BlockStateProperties.SLAB_TYPE, SlabType.TOP) : state;
    }

    private BlockState rotateState(BlockState state) {
        if (state.hasProperty(BlockStateProperties.HORIZONTAL_FACING)) {
            return state.setValue(BlockStateProperties.HORIZONTAL_FACING,
                    rotate(state.getValue(BlockStateProperties.HORIZONTAL_FACING)));
        }
        if (state.hasProperty(BlockStateProperties.AXIS) && rotation % 2 == 1) {
            Direction.Axis axis = state.getValue(BlockStateProperties.AXIS);
            if (axis == Direction.Axis.X) return state.setValue(BlockStateProperties.AXIS, Direction.Axis.Z);
            if (axis == Direction.Axis.Z) return state.setValue(BlockStateProperties.AXIS, Direction.Axis.X);
        }
        return state;
    }

    /**
     * True when a position belongs to the chunk slice currently being written.
     *
     * Large legacy structures are replayed once for every intersecting chunk.
     * WorldGenRegion does not permit reads outside its active neighborhood, so
     * compatibility helpers must apply the same clip to reads as set() applies
     * to writes.
     */
    public boolean insideClip(BlockPos pos) {
        return pos.getX() >= clipMinX && pos.getX() <= clipMaxX
                && pos.getZ() >= clipMinZ && pos.getZ() <= clipMaxZ
                && pos.getY() >= level.getMinBuildHeight()
                && pos.getY() < level.getMaxBuildHeight();
    }
}
