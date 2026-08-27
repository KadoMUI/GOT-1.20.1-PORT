package got.npc;

import got.GOTMod;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.pathfinder.BlockPathTypes;

/** Legacy hostile crocodile used by Sothoryos Mangrove and Howland Reed's legendary quest. */
public final class GOTCrocodileEntity extends Monster {
    private static final SoundEvent SAY = SoundEvent.createVariableRangeEvent(new ResourceLocation(GOTMod.MOD_ID, "crocodile.say"));
    private static final SoundEvent DEATH = SoundEvent.createVariableRangeEvent(new ResourceLocation(GOTMod.MOD_ID, "crocodile.death"));
    private static final SoundEvent SNAP = SoundEvent.createVariableRangeEvent(new ResourceLocation(GOTMod.MOD_ID, "crocodile.snap"));
    private int snapTicks;

    public GOTCrocodileEntity(EntityType<? extends GOTCrocodileEntity> type, Level level) {
        super(type, level);
        xpReward = 5;
        setPathfindingMalus(BlockPathTypes.WATER, 0.0F);
        setPathfindingMalus(BlockPathTypes.WATER_BORDER, 0.0F);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 20.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.22D)
                .add(Attributes.ATTACK_DAMAGE, 4.0D)
                .add(Attributes.FOLLOW_RANGE, 24.0D);
    }

    @Override
    protected void registerGoals() {
        goalSelector.addGoal(0, new FloatGoal(this));
        goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.4D, true));
        goalSelector.addGoal(5, new RandomStrollGoal(this, 1.0D));
        goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 8.0F));
        goalSelector.addGoal(9, new RandomLookAroundGoal(this));
        targetSelector.addGoal(1, new HurtByTargetGoal(this));
        targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    @Override
    public boolean doHurtTarget(net.minecraft.world.entity.Entity target) {
        boolean hit = super.doHurtTarget(target);
        if (hit) {
            snapTicks = 20;
            playSound(SNAP, getSoundVolume(), getVoicePitch());
        }
        return hit;
    }

    @Override
    public void tick() {
        super.tick();
        if (snapTicks > 0) snapTicks--;
        // Legacy crocodiles get a small upward push while pursuing a target in water.
        if (!level().isClientSide && isInWater() && getTarget() != null) {
            setDeltaMovement(getDeltaMovement().add(0.0D, 0.02D, 0.0D));
        }
    }

    public float getSnapProgress(float partialTick) {
        return Math.max(0.0F, Math.min(1.0F, (snapTicks - partialTick) / 20.0F));
    }

    @Override protected SoundEvent getAmbientSound() { return SAY; }
    @Override protected SoundEvent getDeathSound() { return DEATH; }
    @Override public boolean canFreeze() { return false; }
    @Override public boolean isPushedByFluid() { return false; }

    public static boolean canSpawn(EntityType<GOTCrocodileEntity> type, ServerLevelAccessor level,
                                   net.minecraft.world.entity.MobSpawnType reason,
                                   BlockPos pos, net.minecraft.util.RandomSource random) {
        return Monster.checkMonsterSpawnRules(type, level, reason, pos, random);
    }
}
