package got;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeMod;

import java.util.UUID;

/**
 * Regional spear/polearm combat profile.
 *
 * Project Thrones design:
 *  - Stone-sword damage profile.
 *  - +3 blocks entity reach while held in the main hand.
 *  - A successful thrust can damage additional living targets that are
 *    standing directly behind the primary target along the attack line.
 */
public final class GOTRegionalSpearItem extends SwordItem {
    private static final UUID REACH_UUID = UUID.fromString("73c77ec8-9e07-4cf4-894a-2f40c5ebd52e");
    private static final double BONUS_REACH = 3.0D;
    private static final double TOTAL_THRUST_LENGTH = 6.0D;
    private static final double LINE_RADIUS = 0.72D;

    public GOTRegionalSpearItem(Tier tier, Properties properties) {
        // Stone sword: tier damage bonus + 3 attack modifier, normal sword speed.
        super(tier, 3, -2.4F, properties);
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot slot) {
        Multimap<Attribute, AttributeModifier> base = super.getDefaultAttributeModifiers(slot);
        if (slot != EquipmentSlot.MAINHAND) return base;

        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.putAll(base);
        builder.put(ForgeMod.ENTITY_REACH.get(), new AttributeModifier(
                REACH_UUID,
                "Project Thrones spear reach",
                BONUS_REACH,
                AttributeModifier.Operation.ADDITION));
        return builder.build();
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity primary, LivingEntity attacker) {
        boolean hit = super.hurtEnemy(stack, primary, attacker);
        if (attacker.level().isClientSide) return hit;

        Vec3 start = attacker.getEyePosition();
        Vec3 direction = attacker.getLookAngle().normalize();
        Vec3 end = start.add(direction.scale(TOTAL_THRUST_LENGTH));
        AABB search = new AABB(start, end).inflate(LINE_RADIUS);

        DamageSource source = attacker instanceof Player player
                ? attacker.damageSources().playerAttack(player)
                : attacker.damageSources().mobAttack(attacker);
        float damage = (float) attacker.getAttributeValue(Attributes.ATTACK_DAMAGE);

        for (LivingEntity candidate : attacker.level().getEntitiesOfClass(
                LivingEntity.class,
                search,
                e -> e.isAlive() && e != attacker && e != primary)) {
            Vec3 center = candidate.position().add(0.0D, candidate.getBbHeight() * 0.5D, 0.0D);
            Vec3 fromStart = center.subtract(start);
            double along = fromStart.dot(direction);
            if (along < 0.0D || along > TOTAL_THRUST_LENGTH) continue;

            Vec3 closest = start.add(direction.scale(along));
            double allowedRadius = LINE_RADIUS + candidate.getBbWidth() * 0.35D;
            if (center.distanceToSqr(closest) > allowedRadius * allowedRadius) continue;
            if (!attacker.hasLineOfSight(candidate)) continue;

            candidate.hurt(source, damage);
        }
        return hit;
    }
}
