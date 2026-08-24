package got.npc.hiring.command;

import got.npc.hiring.GOTHiredData;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Faithful modern reconstruction of GOTItemCommandSword.command(...).
 *
 * Legacy constants recovered from 24.08.29:
 * - ray distance: 64
 * - candidate target radius around hit point: 6
 * - commanded hired NPC radius around player: 12
 */
public final class GOTCommandSwordService {
    public static final double RAY_DISTANCE = 64.0D;
    public static final double TARGET_RADIUS = 6.0D;
    public static final double UNIT_RADIUS = 12.0D;

    private GOTCommandSwordService() {}

    public static int command(ServerPlayer player, ItemStack sword) {
        ServerLevel level = player.serverLevel();

        HitResult hit = raycast(player);
        Vec3 point = hit.getType() == HitResult.Type.MISS ? null : hit.getLocation();

        List<LivingEntity> candidates = new ArrayList<>();
        if (point != null) {
            AABB targetBox = new AABB(point, point).inflate(TARGET_RADIUS);
            for (LivingEntity living : level.getEntitiesOfClass(LivingEntity.class, targetBox)) {
                if (living.isAlive() && living != player) candidates.add(living);
            }
        }

        AABB unitBox = player.getBoundingBox().inflate(UNIT_RADIUS);
        List<PathfinderMob> units = level.getEntitiesOfClass(PathfinderMob.class, unitBox,
            mob -> GOTCommandEligibility.obeysSwordOrHorn(player, mob, sword));

        int commanded = 0;

        for (PathfinderMob unit : units) {
            LivingEntity nearest = candidates.stream()
                .filter(target -> GOTHiredAttackRules.canAttack(player, unit, target))
                .min(Comparator.comparingDouble(unit::distanceToSqr))
                .orElse(null);

            if (nearest == null) {
                GOTHiredCommandState.commandSwordCancel(unit);
            } else {
                GOTHiredCommandState.commandSwordAttack(unit, nearest);
                commanded++;
            }
        }

        // The legacy mod spawned GOTEntitySwordCommandMarker at the hit point.
        // Use a lightweight marker burst until/if the exact entity renderer is ported.
        if (commanded > 0 && point != null) {
            level.sendParticles(ParticleTypes.CRIT, point.x, point.y + 0.2D, point.z,
                12, 0.25D, 0.25D, 0.25D, 0.05D);
        }

        return commanded;
    }

    private static HitResult raycast(ServerPlayer player) {
        // Entity targeting has priority, matching the legacy sword.
        Vec3 eye = player.getEyePosition();
        Vec3 look = player.getViewVector(1.0F);
        Vec3 end = eye.add(look.scale(RAY_DISTANCE));

        AABB search = player.getBoundingBox().expandTowards(look.scale(RAY_DISTANCE)).inflate(1.0D);
        EntityHitResult entityHit = net.minecraft.world.entity.projectile.ProjectileUtil.getEntityHitResult(
            player.level(), player, eye, end, search,
            e -> e instanceof LivingEntity && e.isPickable(),
            (float) (RAY_DISTANCE * RAY_DISTANCE)
        );
        if (entityHit != null) return entityHit;

        return player.level().clip(new net.minecraft.world.level.ClipContext(
            eye, end,
            net.minecraft.world.level.ClipContext.Block.COLLIDER,
            net.minecraft.world.level.ClipContext.Fluid.NONE,
            player
        ));
    }
}
