package got;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.level.Level;

/** Two-block hanging banner fixed to a horizontal face of its anchor block. */
public final class GOTWallBannerEntity extends GOTAbstractBannerEntity {
    private static final double BANNER_WIDTH = 1.0D;
    private static final double BANNER_HEIGHT = 2.0D;
    private static final double BANNER_THICKNESS = 1.0D / 16.0D;
    private static final double WALL_GAP = 0.001D;

    private static final EntityDataAccessor<Integer> DIRECTION =
            SynchedEntityData.defineId(GOTWallBannerEntity.class, EntityDataSerializers.INT);
    private BlockPos anchor = BlockPos.ZERO;

    public GOTWallBannerEntity(EntityType<? extends GOTWallBannerEntity> type, Level level) {
        super(type, level);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        entityData.define(DIRECTION, Direction.NORTH.get2DDataValue());
    }

    public void setAnchor(BlockPos anchor, Direction outward) {
        if (outward.getAxis().isVertical()) {
            throw new IllegalArgumentException("Wall banners require a horizontal direction");
        }
        this.anchor = anchor.immutable();
        entityData.set(DIRECTION, outward.get2DDataValue());
        setYRot(outward.toYRot());
        yRotO = getYRot();
        double wallOffset = 0.5D + BANNER_THICKNESS / 2.0D + WALL_GAP;
        double x = anchor.getX() + 0.5D + outward.getStepX() * wallOffset;
        double z = anchor.getZ() + 0.5D + outward.getStepZ() * wallOffset;
        setPos(x, anchor.getY() - 1.0D, z);
        refreshWallBoundingBox();
    }

    public Direction getDirection() {
        return Direction.from2DDataValue(entityData.get(DIRECTION));
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
        super.onSyncedDataUpdated(key);
        if (DIRECTION.equals(key)) {
            setYRot(getDirection().toYRot());
            yRotO = getYRot();
            refreshWallBoundingBox();
        }
    }

    public BlockPos getAnchor() {
        return anchor;
    }

    @Override
    public void tick() {
        super.tick();
        setDeltaMovement(0.0D, 0.0D, 0.0D);
        // EntityDimensions are square in the horizontal plane. Restore the
        // original direction-aware hanging box after loading or client spawn.
        refreshWallBoundingBox();
        if (!level().isClientSide && tickCount % 20 == 0 && !hasWallSupport()) {
            spawnAtLocation(createDropStack());
            discard();
        }
    }

    private void refreshWallBoundingBox() {
        Direction outward = getDirection();
        double halfX = outward.getAxis() == Direction.Axis.X
                ? BANNER_THICKNESS / 2.0D : BANNER_WIDTH / 2.0D;
        double halfZ = outward.getAxis() == Direction.Axis.Z
                ? BANNER_THICKNESS / 2.0D : BANNER_WIDTH / 2.0D;
        setBoundingBox(new AABB(
                getX() - halfX, getY(), getZ() - halfZ,
                getX() + halfX, getY() + BANNER_HEIGHT, getZ() + halfZ));
    }

    private boolean hasWallSupport() {
        Direction outward = getDirection();
        return level().getBlockState(anchor).isFaceSturdy(level(), anchor, outward);
    }

    @Override
    protected void readBannerData(CompoundTag tag) {
        BlockPos savedAnchor = BlockPos.of(tag.getLong("Anchor"));
        Direction savedDirection = Direction.from2DDataValue(tag.getInt("Direction"));
        setAnchor(savedAnchor, savedDirection);
    }

    @Override
    protected void addBannerData(CompoundTag tag) {
        tag.putLong("Anchor", anchor.asLong());
        tag.putInt("Direction", getDirection().get2DDataValue());
    }
}
