package got.special;

import got.GOTEntities;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.*;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.*;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

/** Shared 1.20.1 restoration of the legacy GOT giant combat chassis. */
public abstract class GOTGiantBaseEntity extends Monster implements RangedAttackMob {
    protected GOTGiantBaseEntity(EntityType<? extends Monster> type, Level level) { super(type, level); xpReward = 20; }
    public static AttributeSupplier.Builder giantAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 100.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.22D)
                .add(Attributes.ATTACK_DAMAGE, 7.0D)
                .add(Attributes.FOLLOW_RANGE, 24.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.65D);
    }
    @Override protected void registerGoals() {
        goalSelector.addGoal(0, new FloatGoal(this));
        goalSelector.addGoal(2, new RangedAttackGoal(this, 1.2D, 30, 60, 25.0F));
        goalSelector.addGoal(3, new MeleeAttackGoal(this, 1.4D, false));
        goalSelector.addGoal(6, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 12.0F));
        goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        targetSelector.addGoal(1, new HurtByTargetGoal(this));
        targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }
    @Override public void performRangedAttack(LivingEntity target, float distanceFactor) {
        if (level().isClientSide) return;
        GOTThrownRockEntity rock = new GOTThrownRockEntity(GOTEntities.THROWN_ROCK.get(), level(), this);
        double dx = target.getX() - getX();
        double dz = target.getZ() - getZ();
        double dy = target.getEyeY() - rock.getY();
        double horiz = Math.sqrt(dx * dx + dz * dz);
        rock.shoot(dx, dy + horiz * 0.18D, dz, 1.5F, 0.5F);
        level().addFreshEntity(rock);
        playSound(getAmbientSound(), 0.75F, getVoicePitch() * 0.75F);
    }
    @Override public boolean doHurtTarget(Entity target) {
        boolean hit = super.doHurtTarget(target);
        if (hit) {
            float force = (float)(getAttributeValue(Attributes.ATTACK_DAMAGE) * 0.25D);
            target.push(-Math.sin(getYRot() * Math.PI / 180.0D) * force * 0.5D, force * 0.10D, Math.cos(getYRot() * Math.PI / 180.0D) * force * 0.5D);
        }
        return hit;
    }
    @Override protected SoundEvent getAmbientSound() { return SoundEvents.RAVAGER_AMBIENT; }
    @Override protected SoundEvent getHurtSound(net.minecraft.world.damagesource.DamageSource s) { return SoundEvents.RAVAGER_HURT; }
    @Override protected SoundEvent getDeathSound() { return SoundEvents.RAVAGER_DEATH; }
    @Override protected float getSoundVolume() { return 1.5F; }
}
