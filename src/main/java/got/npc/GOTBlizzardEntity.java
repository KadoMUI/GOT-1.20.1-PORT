package got.npc;

import got.GOTEntities;
import got.faction.GOTFactionHurtByTargetGoal;
import got.faction.GOTFactionTargetGoal;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.RangedAttackGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Snowball;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.phys.EntityHitResult;

import javax.annotation.Nullable;

/** Ulthos frost-biome White Walker entity from the legacy Blizzard class. */
public final class GOTBlizzardEntity extends PathfinderMob implements RangedAttackMob, GOTFactionNpc {
    @Nullable private LivingEntity huntedTarget;

    public GOTBlizzardEntity(EntityType<? extends GOTBlizzardEntity> type, Level level) {
        super(type, level);
        xpReward = 5;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 1.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.22D)
                .add(Attributes.ATTACK_DAMAGE, 2.0D)
                .add(Attributes.FOLLOW_RANGE, 32.0D);
    }

    @Override
    protected void registerGoals() {
        goalSelector.addGoal(0, new FloatGoal(this));
        goalSelector.addGoal(2, new RangedAttackGoal(this, 1.0D, 30, 18.0F));
        goalSelector.addGoal(5, new RandomStrollGoal(this, 0.9D));
        goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 12.0F));
        goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        targetSelector.addGoal(1, new GOTFactionHurtByTargetGoal(this));
        targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
        targetSelector.addGoal(3, new GOTFactionTargetGoal(this));
    }

    @Override
    public void setTarget(@Nullable LivingEntity target) {
        super.setTarget(target);
        if (target != null && target.isAlive()) huntedTarget = target;
    }

    @Override
    public void tick() {
        super.tick();
        if (!level().isClientSide && huntedTarget != null && !huntedTarget.isAlive()) {
            createWight(huntedTarget);
            huntedTarget = null;
        }
    }

    @Override
    public void performRangedAttack(LivingEntity target, float distanceFactor) {
        Snowball snowball = new BlizzardSnowball(level(), this);
        double dx = target.getX() - getX();
        double dy = target.getY(0.3333333333333333D) - snowball.getY();
        double dz = target.getZ() - getZ();
        double horizontal = Math.sqrt(dx * dx + dz * dz);
        snowball.shoot(dx, dy + horizontal * 0.2D, dz, 1.6F, 6.0F);
        playSound(SoundEvents.SNOWBALL_THROW, 1.0F, 0.8F + random.nextFloat() * 0.3F);
        level().addFreshEntity(snowball);
    }

    private void createWight(LivingEntity fallen) {
        if (!(level() instanceof ServerLevel serverLevel)) return;
        if (fallen instanceof GOTFactionNpc factionNpc
                && factionNpc.getFactionId().equals("white_walker")) return;
        GOTWhiteWalkerNpcEntity wight = GOTEntities.WHITE_WALKER_NPC.get().create(serverLevel);
        if (wight == null) return;
        BlockPos pos = fallen.blockPosition();
        wight.moveTo(fallen.getX(), fallen.getY(), fallen.getZ(), fallen.getYRot(), 0.0F);
        wight.prepareForSpawn(WhiteWalkerNpcRole.WIGHT, null, false, pos, 24, "");
        serverLevel.addFreshEntity(wight);
    }

    @Override
    protected void dropCustomDeathLoot(net.minecraft.world.damagesource.DamageSource source,
                                       int looting, boolean recentlyHit) {
        super.dropCustomDeathLoot(source, looting, recentlyHit);
        if (random.nextFloat() < 0.525F) {
            spawnAtLocation(GOTWhiteWalkerNpcLoadouts.stack("got:ice_shard", 1 + random.nextInt(2)));
        }
    }

    @Override public boolean causeFallDamage(float distance, float multiplier,
                                              net.minecraft.world.damagesource.DamageSource source) { return false; }
    @Override public boolean canFreeze() { return false; }
    @Override public String getFactionId() { return "white_walker"; }
    @Override public int getAlignmentBonus() { return 2; }
    @Override public boolean isCivilian() { return false; }
    @Override public boolean isActiveCombatant() { return true; }

    /** Uses the vanilla snowball entity type/renderer but restores legacy frost damage. */
    private static final class BlizzardSnowball extends Snowball {
        private BlizzardSnowball(Level level, LivingEntity owner) {
            super(level, owner);
        }

        @Override
        protected void onHitEntity(EntityHitResult hit) {
            super.onHitEntity(hit);
            Entity target = hit.getEntity();
            target.hurt(damageSources().thrown(this, getOwner()), 4.0F);
            if (target instanceof LivingEntity living) {
                living.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 100));
                if (!living.isAlive() && getOwner() instanceof GOTBlizzardEntity blizzard) {
                    blizzard.createWight(living);
                    blizzard.huntedTarget = null;
                }
            }
        }
    }
}
