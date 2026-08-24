package got;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

/** Freestanding three-block-tall banner with the original floor support rule. */
public final class GOTStandingBannerEntity extends GOTAbstractBannerEntity {
    public GOTStandingBannerEntity(EntityType<? extends GOTStandingBannerEntity> type, Level level) {
        super(type, level);
    }

    @Override
    public void tick() {
        super.tick();
        setDeltaMovement(0.0D, 0.0D, 0.0D);
        if (!level().isClientSide && tickCount % 20 == 0 && !hasFloorSupport()) {
            spawnAtLocation(createDropStack());
            discard();
        }
    }

    private boolean hasFloorSupport() {
        BlockPos support = BlockPos.containing(getX(), getY() - 0.01D, getZ());
        return level().getBlockState(support).isFaceSturdy(level(), support, Direction.UP);
    }

    @Override
    protected void readBannerData(CompoundTag tag) {
        setYRot(tag.getFloat("BannerRotation"));
        yRotO = getYRot();
    }

    @Override
    protected void addBannerData(CompoundTag tag) {
        tag.putFloat("BannerRotation", getYRot());
    }
}
