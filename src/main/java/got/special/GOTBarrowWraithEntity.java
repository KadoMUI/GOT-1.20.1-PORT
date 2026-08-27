package got.special;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.*;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.*;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

/** Legacy Barrow Wraith: melee undead spectre from the North Barrows. */
public final class GOTBarrowWraithEntity extends Monster {
    public GOTBarrowWraithEntity(EntityType<? extends Monster> type, Level level){ super(type,level); xpReward=8; }
    public static AttributeSupplier.Builder createAttributes(){ return Monster.createMonsterAttributes()
            .add(Attributes.MAX_HEALTH,20).add(Attributes.MOVEMENT_SPEED,.22).add(Attributes.ATTACK_DAMAGE,5).add(Attributes.FOLLOW_RANGE,24); }
    @Override protected void registerGoals(){
        goalSelector.addGoal(0,new FloatGoal(this));
        goalSelector.addGoal(2,new MeleeAttackGoal(this,1.2,true));
        goalSelector.addGoal(5,new WaterAvoidingRandomStrollGoal(this,1));
        goalSelector.addGoal(7,new LookAtPlayerGoal(this, Player.class,12));
        goalSelector.addGoal(8,new RandomLookAroundGoal(this));
        targetSelector.addGoal(1,new HurtByTargetGoal(this).setAlertOthers());
        targetSelector.addGoal(2,new NearestAttackableTargetGoal<>(this,Player.class,true));
    }
    @Override public boolean doHurtTarget(Entity target){
        boolean hit=super.doHurtTarget(target);
        if(hit && target instanceof LivingEntity living){
            int seconds=Math.max(1,(level().getDifficulty().getId()+5)/2);
            int ticks=seconds*20;
            living.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN,ticks,0),this);
            living.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN,ticks,0),this);
            living.addEffect(new MobEffectInstance(MobEffects.WEAKNESS,ticks,0),this);
        }
        return hit;
    }
    @Override public MobType getMobType(){ return MobType.UNDEAD; }
}
