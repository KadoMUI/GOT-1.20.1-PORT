package got.wildlife;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

/** Shared hostile implementation for the legacy desert/jungle/red scorpions. */
public class GOTScorpionEntity extends Monster {
    public enum Species { DESERT, JUNGLE, RED }
    private final Species species;
    public GOTScorpionEntity(EntityType<? extends GOTScorpionEntity> type, Level level, Species species) { super(type, level); this.species=species; xpReward=species==Species.RED?8:4; }
    public Species species(){return species;}
    public static AttributeSupplier.Builder createAttributes(){return Mob.createMobAttributes().add(Attributes.MAX_HEALTH,16).add(Attributes.MOVEMENT_SPEED,.30).add(Attributes.ATTACK_DAMAGE,3).add(Attributes.FOLLOW_RANGE,20);}
    @Override protected void registerGoals(){goalSelector.addGoal(1,new FloatGoal(this));goalSelector.addGoal(2,new MeleeAttackGoal(this,1.15,true));goalSelector.addGoal(5,new WaterAvoidingRandomStrollGoal(this,1));goalSelector.addGoal(6,new LookAtPlayerGoal(this,Player.class,8));goalSelector.addGoal(7,new RandomLookAroundGoal(this));targetSelector.addGoal(1,new NearestAttackableTargetGoal<>(this,Player.class,true));}
    @Override public boolean doHurtTarget(net.minecraft.world.entity.Entity target){boolean hit=super.doHurtTarget(target);if(hit && target instanceof net.minecraft.world.entity.LivingEntity living){int seconds=species==Species.RED?10:species==Species.JUNGLE?7:5;int amp=species==Species.RED?1:0;living.addEffect(new MobEffectInstance(MobEffects.POISON,seconds*20,amp));}return hit;}
}
