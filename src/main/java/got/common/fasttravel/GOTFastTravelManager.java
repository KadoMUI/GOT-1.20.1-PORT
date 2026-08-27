package got.common.fasttravel;

import got.common.world.map.GOTWaypoint;
import got.faction.GOTFaction;
import got.faction.GOTFactionPlayerData;
import got.network.GOTNetwork;
import got.network.S2CFastTravelDataPacket;
import got.npc.GOTHiredNpc;
import got.pact.GOTPactSharing;
import got.world.GOTDimensions;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.network.PacketDistributor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public final class GOTFastTravelManager {
    public static final int WARMUP_TICKS = 200;
    public static final int COOLDOWN_MIN_SECONDS = 60;
    public static final int COOLDOWN_MAX_SECONDS = 600;
    private static final double ENTOURAGE_RADIUS = 256.0D;

    private GOTFastTravelManager() {}

    public static void sync(ServerPlayer player) {
        GOTNetwork.CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), S2CFastTravelDataPacket.from(player));
    }

    public static boolean requestFixed(ServerPlayer player, GOTWaypoint waypoint) {
        if (!GOTPactSharing.isUnlocked(player, waypoint) || !alignmentCompatible(player, waypoint)) {
            player.displayClientMessage(Component.translatable("got.fastTravel.locked"), false);
            return false;
        }
        return begin(player, false, waypoint.getID(), waypoint.getCodeName(), waypoint.getDisplayName(),
                waypoint.getCoordX(), waypoint.getCoordZ(), null);
    }

    public static boolean requestCustom(ServerPlayer player, UUID owner, int id) {
        GOTFastTravelData.Custom custom = GOTPactSharing.waypoint(player, owner, id);
        if (custom == null) {
            player.displayClientMessage(Component.translatable("got.fastTravel.locked"), false);
            return false;
        }
        return begin(player, true, id, "custom_" + owner + "_" + id, Component.literal(custom.name()), custom.x(), custom.z(), owner);
    }

    /** Compatibility entry point retained for old callers. */
    public static boolean fastTravel(ServerPlayer player, GOTWaypoint waypoint) { return requestFixed(player, waypoint); }

    private static boolean begin(ServerPlayer player, boolean custom, int id, String key, Component name, int x, int z, UUID customOwner) {
        if (!player.level().dimension().equals(GOTDimensions.PLANETOS)) {
            player.displayClientMessage(Component.translatable("got.fastTravel.dimensionUnavailable"), false);
            return false;
        }
        if (player.isSleeping()) {
            player.displayClientMessage(Component.translatable("got.fastTravel.inBed"), false);
            return false;
        }
        if (isUnderAttack(player)) {
            player.displayClientMessage(Component.translatable("got.fastTravel.underAttack"), false);
            return false;
        }
        int required = requiredCooldownTicks(player, key, x, z);
        int elapsed = GOTFastTravelData.ticksSinceTravel(player);
        if (elapsed < required) {
            int seconds = Math.max(1, (required - elapsed + 19) / 20);
            player.displayClientMessage(Component.translatable("got.fastTravel.moreTime", name, formatTime(seconds)), false);
            return false;
        }

        CompoundTag pending = new CompoundTag();
        pending.putBoolean("Custom", custom);
        pending.putInt("Id", id);
        if (customOwner != null) pending.putUUID("CustomOwner", customOwner);
        pending.putInt("Ticks", WARMUP_TICKS);
        pending.putDouble("StartX", player.getX());
        pending.putDouble("StartY", player.getY());
        pending.putDouble("StartZ", player.getZ());
        GOTFastTravelData.setPending(player, pending);
        player.displayClientMessage(Component.translatable("got.fastTravel.travelTicksStart", 10), false);
        return true;
    }

    public static int requiredCooldownTicks(ServerPlayer player, String key, int x, int z) {
        if (player.isCreative() || player.isSpectator()) return 0;
        int useCount = GOTFastTravelData.useCount(player, key);
        double distance = player.distanceToSqr(x + 0.5D, 64.0D, z + 0.5D);
        distance = Math.sqrt(distance);
        double seconds = COOLDOWN_MIN_SECONDS;
        seconds += (COOLDOWN_MAX_SECONDS - COOLDOWN_MIN_SECONDS) * Math.pow(0.9D, useCount);
        seconds *= Math.max(1.0D, distance * 1.2E-5D);
        return Math.max(0, (int)Math.round(seconds)) * 20;
    }

    public static void tick(ServerPlayer player) {
        GOTFastTravelData.tickSinceTravel(player);
        if (player.tickCount % 10 == 0 && player.level().dimension().equals(GOTDimensions.PLANETOS)) {
            player.level().getBiome(player.blockPosition()).unwrapKey().ifPresent(key -> {
                GOTWaypoint.Region region = GOTFastTravelRegions.fromBiome(key.location());
                if (GOTFastTravelData.unlock(player, region)) {
                    GOTPactSharing.shareRegion(player, region);
                    player.displayClientMessage(Component.translatable("got.fastTravel.regionUnlocked", region.name()), false);
                    sync(player);
                }
            });
        }
        if (!GOTFastTravelData.hasPending(player)) return;
        CompoundTag pending = GOTFastTravelData.pending(player);
        if (player.isSleeping() || !player.level().dimension().equals(GOTDimensions.PLANETOS)) {
            cancel(player, "got.fastTravel.inBed");
            return;
        }
        double dx = player.getX() - pending.getDouble("StartX");
        double dy = player.getY() - pending.getDouble("StartY");
        double dz = player.getZ() - pending.getDouble("StartZ");
        if (dx * dx + dy * dy + dz * dz > 0.04D) {
            cancel(player, "got.fastTravel.motion");
            return;
        }
        if (isUnderAttack(player)) {
            cancel(player, "got.fastTravel.underAttack");
            return;
        }
        int ticks = pending.getInt("Ticks") - 1;
        pending.putInt("Ticks", ticks);
        GOTFastTravelData.setPending(player, pending);
        if (ticks > 0) {
            if (ticks % 20 == 0 && ticks / 20 <= 5) {
                player.displayClientMessage(Component.translatable("got.fastTravel.travelTicks", ticks / 20), false);
            }
            return;
        }
        executePending(player, pending);
        GOTFastTravelData.clearPending(player);
    }

    public static void cancelForDamage(ServerPlayer player) {
        if (GOTFastTravelData.hasPending(player)) cancel(player, "got.fastTravel.motion");
    }

    private static void cancel(ServerPlayer player, String translation) {
        GOTFastTravelData.clearPending(player);
        player.displayClientMessage(Component.translatable(translation), false);
    }

    private static void executePending(ServerPlayer player, CompoundTag pending) {
        ServerLevel level = player.serverLevel();
        boolean custom = pending.getBoolean("Custom");
        int id = pending.getInt("Id");
        String key;
        Component name;
        int x, z;
        if (custom) {
            UUID owner = pending.hasUUID("CustomOwner") ? pending.getUUID("CustomOwner") : player.getUUID();
            GOTFastTravelData.Custom c = GOTPactSharing.waypoint(player, owner, id);
            if (c == null) return;
            key = "custom_" + owner + "_" + id; name = Component.literal(c.name()); x = c.x(); z = c.z();
        } else {
            GOTWaypoint waypoint = GOTWaypoint.byId(id);
            if (waypoint == null || !GOTPactSharing.isUnlocked(player, waypoint)) return;
            key = waypoint.getCodeName(); name = waypoint.getDisplayName(); x = waypoint.getCoordX(); z = waypoint.getCoordZ();
        }
        level.getChunk(x >> 4, z >> 4);
        BlockPos destination;
        if (custom) {
            UUID owner = pending.hasUUID("CustomOwner") ? pending.getUUID("CustomOwner") : player.getUUID();
            GOTFastTravelData.Custom c = GOTPactSharing.waypoint(player, owner, id);
            BlockPos saved = c == null ? null : new BlockPos(c.x(), c.y(), c.z());
            destination = saved != null && isSafe(level, saved) ? saved : findSafeDestination(level, x, z);
        } else destination = findSafeDestination(level, x, z);
        List<Entity> entourage = collectEntourage(player);
        Entity ridden = player.getVehicle();
        player.stopRiding();
        if (ridden != null && ridden.level() == level) teleportEntity(ridden, destination.offset(1, 0, 0));
        player.teleportTo(level, destination.getX() + 0.5D, destination.getY(), destination.getZ() + 0.5D, player.getYRot(), player.getXRot());
        player.setDeltaMovement(0, 0, 0); player.fallDistance = 0;
        if (ridden != null && ridden.isAlive()) player.startRiding(ridden, true);

        int index = 0;
        for (Entity entity : entourage) {
            if (!entity.isAlive() || entity == ridden || entity == player) continue;
            int ox = 2 + (index % 4) * 2;
            int oz = -3 + (index / 4) * 2;
            teleportMountedEntity(entity, findSafeDestination(level, x + ox, z + oz));
            index++;
        }
        GOTFastTravelData.incrementUse(player, key);
        GOTFastTravelData.setTicksSinceTravel(player, 0);
        player.displayClientMessage(Component.translatable("got.fastTravel.arrived", name), false);
        sync(player);
    }

    private static List<Entity> collectEntourage(ServerPlayer player) {
        UUID owner = player.getUUID();
        AABB box = player.getBoundingBox().inflate(ENTOURAGE_RADIUS);
        ArrayList<Entity> out = new ArrayList<>();
        for (Mob mob : player.serverLevel().getEntitiesOfClass(Mob.class, box)) {
            if (mob instanceof GOTHiredNpc hired && owner.equals(hired.getHiredOwnerUUID()) && hired.isFollowingHiredOwner()) { out.add(mob); continue; }
            if (mob instanceof TamableAnimal tame && tame.isOwnedBy(player) && !tame.isInSittingPose()) { out.add(mob); continue; }
            if (mob.isLeashed() && mob.getLeashHolder() == player) out.add(mob);
        }
        return out;
    }

    private static void teleportMountedEntity(Entity entity, BlockPos pos) {
        Entity vehicle = entity.getVehicle();
        if (vehicle != null && vehicle.isAlive()) {
            entity.stopRiding();
            teleportEntity(vehicle, pos);
            teleportEntity(entity, pos.above());
            entity.startRiding(vehicle, true);
        } else teleportEntity(entity, pos);
    }

    private static void teleportEntity(Entity entity, BlockPos pos) {
        entity.teleportTo(pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D);
        entity.setDeltaMovement(0, 0, 0); entity.fallDistance = 0;
    }

    private static boolean isUnderAttack(ServerPlayer player) {
        if (player.isCreative() || player.isSpectator()) return false;
        AABB box = player.getBoundingBox().inflate(16.0D);
        for (LivingEntity entity : player.serverLevel().getEntitiesOfClass(LivingEntity.class, box)) {
            if (entity instanceof Mob mob && mob.getTarget() == player) return true;
        }
        return false;
    }

    private static boolean isSafe(ServerLevel level, BlockPos feet) {
        BlockPos head = feet.above(), floor = feet.below(); BlockState floorState = level.getBlockState(floor);
        return floorState.isSolidRender(level, floor) && !floorState.is(BlockTags.FIRE)
                && level.getBlockState(feet).getCollisionShape(level, feet).isEmpty()
                && level.getBlockState(head).getCollisionShape(level, head).isEmpty();
    }

    public static BlockPos findSafeDestination(ServerLevel level, int x, int z) {
        int top = level.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, x, z);
        int min = level.getMinBuildHeight() + 1, max = level.getMaxBuildHeight() - 2;
        int start = Math.max(min, Math.min(max, top));
        for (int y = start; y >= min; y--) {
            BlockPos feet = new BlockPos(x, y, z), head = feet.above(), floor = feet.below();
            BlockState floorState = level.getBlockState(floor);
            if (floorState.isSolidRender(level, floor) && !floorState.is(BlockTags.FIRE)
                    && level.getBlockState(feet).getCollisionShape(level, feet).isEmpty()
                    && level.getBlockState(head).getCollisionShape(level, head).isEmpty()) return feet;
        }
        return new BlockPos(x, Math.max(min + 1, start + 1), z);
    }

    private static boolean alignmentCompatible(ServerPlayer player, GOTWaypoint waypoint) {
        GOTFaction faction = GOTFaction.byId(waypoint.getFactionCode()).orElse(GOTFaction.UNALIGNED);
        return faction == GOTFaction.UNALIGNED || GOTFactionPlayerData.get(player).alignment(faction) >= 0.0F;
    }

    private static String formatTime(int seconds) {
        int m = seconds / 60, s = seconds % 60;
        return m > 0 ? m + "m " + s + "s" : s + "s";
    }
}
