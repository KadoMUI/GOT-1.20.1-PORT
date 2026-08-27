package got.claim;

import got.GOTAbstractBannerEntity;
import got.GOTBlocks;
import got.GOTStandingBannerEntity;
import got.faction.GOTFaction;
import got.faction.GOTFactionPlayerData;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import javax.annotation.Nullable;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/** Central claim lookup and authorization service used by every event hook. */
public final class GOTBannerProtection {
    public static final int BRONZE_RANGE = 8;
    public static final int SILVER_RANGE = 16;
    public static final int GOLD_RANGE = 32;
    public static final int VALYRIAN_RANGE = 64;
    private static final int WARNING_COOLDOWN_TICKS = 40;
    private static final Map<UUID, Long> LAST_WARNING = new ConcurrentHashMap<>();

    private GOTBannerProtection() {}

    public static int rangeForSupport(BlockState state) {
        if (state.is(GOTBlocks.BRONZE_BLOCK.get())) return BRONZE_RANGE;
        if (state.is(GOTBlocks.SILVER_BLOCK.get())) return SILVER_RANGE;
        if (state.is(Blocks.GOLD_BLOCK)) return GOLD_RANGE;
        if (state.is(GOTBlocks.VALYRIAN_STEEL_BLOCK.get())) return VALYRIAN_RANGE;
        return 0;
    }

    public static AABB bounds(BlockPos origin, int range) {
        return new AABB(origin.getX(), origin.getY(), origin.getZ(),
                origin.getX() + 1.0D, origin.getY() + 1.0D, origin.getZ() + 1.0D)
                .inflate(range);
    }

    public static List<GOTStandingBannerEntity> activeClaims(ServerLevel level, BlockPos position,
                                                               double extraRange) {
        double search = GOTBannerClaim.MAX_RANGE + Math.max(0.0D, extraRange);
        AABB area = new AABB(position).inflate(search);
        return level.getEntitiesOfClass(GOTStandingBannerEntity.class, area,
                        GOTAbstractBannerEntity::isClaimActive)
                .stream()
                .filter(banner -> banner.getClaimBounds().contains(position.getX() + 0.5D,
                        position.getY() + 0.5D, position.getZ() + 0.5D))
                .sorted(Comparator.comparingDouble(banner -> banner.distanceToSqr(
                        position.getX() + 0.5D, position.getY() + 0.5D, position.getZ() + 0.5D)))
                .toList();
    }

    @Nullable
    public static Denial denial(ServerLevel level, BlockPos position, ServerPlayer player,
                                 GOTBannerPermission permission) {
        for (GOTStandingBannerEntity banner : activeClaims(level, position, 0.0D)) {
            if (!isAllowed(banner, player, permission)) return denialFor(banner);
        }
        return null;
    }

    public static boolean isDenied(ServerLevel level, BlockPos position, ServerPlayer player,
                                   GOTBannerPermission permission, boolean warn) {
        Denial denial = denial(level, position, player, permission);
        if (denial == null) return false;
        if (warn) warn(player, denial);
        return true;
    }

    public static boolean isClaimed(ServerLevel level, BlockPos position) {
        return !activeClaims(level, position, 0.0D).isEmpty();
    }

    public static boolean isFactionBlocked(ServerLevel level, BlockPos position, GOTFaction faction) {
        for (GOTStandingBannerEntity banner : activeClaims(level, position, 0.0D)) {
            GOTFaction protecting = banner.getClaimFaction();
            if (faction == GOTFaction.HOSTILE || protecting.isBadRelation(faction)) return true;
        }
        return false;
    }

    public static boolean canCreateClaim(ServerLevel level, BlockPos origin, int range,
                                         ServerPlayer player, boolean warn) {
        AABB proposed = bounds(origin, range);
        AABB search = proposed.inflate(GOTBannerClaim.MAX_RANGE);
        List<GOTStandingBannerEntity> banners = level.getEntitiesOfClass(
                GOTStandingBannerEntity.class, search, GOTAbstractBannerEntity::isClaimActive);
        for (GOTStandingBannerEntity banner : banners) {
            if (banner.getClaimBounds().intersects(proposed)
                    && !isAllowed(banner, player, GOTBannerPermission.FULL)) {
                if (warn) player.displayClientMessage(
                        Component.translatable("got.chat.alreadyProtected"), false);
                return false;
            }
        }
        return true;
    }

    public static boolean isAllowed(GOTAbstractBannerEntity banner, ServerPlayer player,
                                    GOTBannerPermission permission) {
        if (!banner.isClaimActive()) return true;
        if (player.getAbilities().instabuild) return true;
        UUID owner = banner.getOwnerUUID();
        if (owner != null && owner.equals(player.getUUID())) return true;
        if (owner != null && player.getServer() != null && got.pact.GOTPactService.samePact(player.getServer(), owner, player.getUUID())) return true;

        GOTBannerClaim claim = banner.getClaim();
        if (claim.structureProtection()) return player.hasPermissions(2);
        if (claim.defaultAllows(permission)) return true;
        if (!claim.playerSpecific()) {
            GOTFaction faction = banner.getClaimFaction();
            return faction != GOTFaction.UNALIGNED
                    && GOTFactionPlayerData.get(player).alignment(faction) >= claim.alignmentRequired();
        }

        for (GOTBannerWhitelistEntry entry : claim.entries()) {
            boolean match = entry.kind() == GOTBannerWhitelistEntry.Kind.PLAYER
                    ? player.getUUID().equals(entry.id())
                    : player.getServer() != null
                    && GOTClaimGroupResolver.isMember(player.getServer(), entry, player.getUUID());
            if (match && entry.allows(permission)) return true;
        }
        return false;
    }

    public static boolean mayDamageBanner(GOTAbstractBannerEntity banner, ServerPlayer player) {
        ServerLevel level = (ServerLevel) banner.level();
        if (banner.isClaimActive() && GOTBannerClaimConfig.SELF_PROTECTION.get()
                && banner.getClaim().selfProtection() && !banner.canPlayerEditClaim(player)) {
            warn(player, denialFor(banner));
            return false;
        }
        for (GOTStandingBannerEntity other : activeClaims(level, banner.blockPosition(), 0.0D)) {
            if (other.getUUID().equals(banner.getUUID())) continue;
            if (!isAllowed(other, player, GOTBannerPermission.FULL)) {
                warn(player, denialFor(other));
                return false;
            }
        }
        return true;
    }

    public static void warn(ServerPlayer player, Denial denial) {
        long now = player.level().getGameTime();
        Long previous = LAST_WARNING.put(player.getUUID(), now);
        if (previous != null && now - previous < WARNING_COOLDOWN_TICKS) return;
        player.displayClientMessage(Component.translatable("got.chat.protectedLand", denial.protector()), true);
    }

    private static Denial denialFor(GOTAbstractBannerEntity banner) {
        String protector;
        if (banner.getClaim().structureProtection()) {
            protector = Component.translatable("got.chat.protectedStructure").getString();
        } else if (banner.getClaim().playerSpecific()) {
            protector = banner.getOwnerName();
        } else {
            protector = banner.getClaimFaction().displayName().getString();
        }
        return new Denial(banner.getUUID(), protector, banner.getClaim().structureProtection());
    }

    @Nullable
    public static ServerPlayer responsiblePlayer(@Nullable Entity entity) {
        if (entity instanceof ServerPlayer player) return player;
        Entity owner = null;
        if (entity instanceof net.minecraft.world.entity.projectile.Projectile projectile) {
            owner = projectile.getOwner();
        } else if (entity instanceof net.minecraft.world.entity.item.PrimedTnt tnt) {
            owner = tnt.getOwner();
        }
        return owner instanceof ServerPlayer player ? player : null;
    }

    public record Denial(UUID bannerId, String protector, boolean structure) {}
}
