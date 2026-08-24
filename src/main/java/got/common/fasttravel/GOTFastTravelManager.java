package got.common.fasttravel;

import got.common.world.map.GOTAbstractWaypoint;
import got.common.world.map.GOTWaypoint;
import got.world.GOTDimensions;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;

public final class GOTFastTravelManager {
    private static final String USE_COUNT_TAG = "GOTFastTravelUseCount";
    private static final String LAST_WAYPOINT_TAG = "GOTLastFastTravelWaypoint";
    private static final String LAST_TRAVEL_TIME_TAG = "GOTLastFastTravelTime";

    private GOTFastTravelManager() {}

    public static boolean fastTravel(ServerPlayer player, GOTWaypoint waypoint) {
        if (waypoint.isHidden() || waypoint.getLockState(player) != GOTAbstractWaypoint.WaypointLockState.UNLOCKED) {
            player.displayClientMessage(Component.translatable("got.fastTravel.locked"), false);
            return false;
        }

        ServerLevel planetos = player.getServer() == null ? null : player.getServer().getLevel(GOTDimensions.PLANETOS);
        if (planetos == null) {
            player.displayClientMessage(Component.translatable("got.fastTravel.dimensionUnavailable"), false);
            return false;
        }

        int x = waypoint.getCoordX();
        int z = waypoint.getCoordZ();
        planetos.getChunk(x >> 4, z >> 4);

        BlockPos destination = findSafeDestination(planetos, x, z);
        player.stopRiding();
        if (player.isSleeping()) player.stopSleepInBed(true, true);
        player.teleportTo(planetos,
                destination.getX() + 0.5D,
                destination.getY(),
                destination.getZ() + 0.5D,
                player.getYRot(),
                player.getXRot());
        player.setDeltaMovement(0.0D, 0.0D, 0.0D);
        player.fallDistance = 0.0F;

        var data = player.getPersistentData();
        data.putInt(USE_COUNT_TAG, data.getInt(USE_COUNT_TAG) + 1);
        data.putString(LAST_WAYPOINT_TAG, waypoint.getCodeName());
        data.putLong(LAST_TRAVEL_TIME_TAG, planetos.getGameTime());

        player.displayClientMessage(Component.translatable("got.fastTravel.arrived", waypoint.getDisplayName()), false);
        return true;
    }

    public static BlockPos findSafeDestination(ServerLevel level, int x, int z) {
        int top = level.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, x, z);
        int min = level.getMinBuildHeight() + 1;
        int max = level.getMaxBuildHeight() - 2;
        int start = Math.max(min, Math.min(max, top));

        for (int y = start; y >= min; y--) {
            BlockPos feet = new BlockPos(x, y, z);
            BlockPos head = feet.above();
            BlockPos floor = feet.below();
            BlockState floorState = level.getBlockState(floor);
            if (floorState.isSolidRender(level, floor)
                    && !floorState.is(BlockTags.FIRE)
                    && level.getBlockState(feet).getCollisionShape(level, feet).isEmpty()
                    && level.getBlockState(head).getCollisionShape(level, head).isEmpty()) {
                return feet;
            }
        }

        return new BlockPos(x, Math.max(min + 1, start + 1), z);
    }
}
