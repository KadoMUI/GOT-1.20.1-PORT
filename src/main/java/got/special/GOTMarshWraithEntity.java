package got.special;

import got.GOTEntities;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.*;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.*;
import net.minecraft.world.entity.monster.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

/** Legacy Marsh Wraith: ranged spectral monster from Mossovy marshes. */
public final class GOTMarshWraithEntity extends Monster implements RangedAttackMob {
    private int fadeTicks=30;
    private int despawnTicks=-1;
    public GOTMarshWraithEntity(EntityType<? extends Monster> type, Level level){super(type,level);xpReward=8;}
    public static AttributeSupplier.Builder createAttributes(){return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH,20).add(Attributes.MOVEMENT_SPEED,.22).add(Attributes.FOLLOW_RANGE,24);}
    @Override protected void registerGoals(){
        goalSelector.addGoal(0,new RangedAttackGoal(this,1.6,40,12));
        goalSelector.addGoal(2,new WaterAvoidingRandomStrollGoal(this,1));
        goalSelector.addGoal(4,new LookAtPlayerGoal(this,Player.class,8));
        goalSelector.addGoal(5,new RandomLookAroundGoal(this));
        targetSelector.addGoal(1,new HurtByTargetGoal(this));
        targetSelector.addGoal(2,new NearestAttackableTargetGoal<>(this,Player.class,true));
    }
    @Override public void performRangedAttack(LivingEntity target,float distanceFactor){
        if(fadeTicks>0)return;
        GOTMarshWraithBallEntity ball=new GOTMarshWraithBallEntity(GOTEntities.MARSH_WRAITH_BALL.get(),level(),this,target);
        level().addFreshEntity(ball);
        playSound(SoundEvents.EVOKER_CAST_SPELL,1F,0.8F+random.nextFloat()*.4F);
    }
    @Override public void tick(){
        super.tick(); if(fadeTicks>0)fadeTicks--;
        if(!level().isClientSide){
            if(getTarget()!=null)despawnTicks=100;
            else if(despawnTicks>0)despawnTicks--;
            else if(despawnTicks==0)discard();
        }
    }
    public float spectralAlpha(){return fadeTicks>0?1F-(fadeTicks/30F):1F;}
    @Override public MobType getMobType(){return MobType.UNDEAD;}
}
