package got.wildlife;

import got.GOTItems;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.*;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class GOTBearEntity extends Animal {
    private int angryTicks;
    public GOTBearEntity(EntityType<? extends GOTBearEntity> type, Level level) { super(type, level); xpReward = 5; }

    protected double legacyHealth() { return 40.0D; }
    protected double legacyDamage() { return 4.0D; }
    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 40.0D).add(Attributes.MOVEMENT_SPEED, 0.22D)
                .add(Attributes.ATTACK_DAMAGE, 4.0D).add(Attributes.FOLLOW_RANGE, 24.0D);
    }

    @Override protected void registerGoals() {
        goalSelector.addGoal(0, new FloatGoal(this));
        goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.4D, true));
        goalSelector.addGoal(2, new PanicGoal(this, 1.4D));
        goalSelector.addGoal(3, new BreedGoal(this, 1.0D));
        goalSelector.addGoal(4, new TemptGoal(this, 1.2D, Ingredient.of(Items.COD, Items.SALMON), false));
        goalSelector.addGoal(5, new FollowParentGoal(this, 1.2D));
        goalSelector.addGoal(6, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 8.0F));
        goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        targetSelector.addGoal(1, new HurtByTargetGoal(this));
    }

    @Override public boolean hurt(DamageSource source, float amount) {
        boolean hurt = super.hurt(source, amount);
        Entity attacker = source.getEntity();
        if (hurt && attacker instanceof LivingEntity living) {
            if (isBaby()) {
                List<GOTBearEntity> adults = level().getEntitiesOfClass(GOTBearEntity.class, getBoundingBox().inflate(12.0D), b -> !b.isBaby());
                adults.forEach(b -> b.becomeAngryAt(living));
            } else becomeAngryAt(living);
        }
        return hurt;
    }

    protected void becomeAngryAt(LivingEntity target) { setTarget(target); angryTicks = 200; }

    @Override public void aiStep() {
        super.aiStep();
        if (!level().isClientSide && angryTicks > 0 && --angryTicks == 0 && getTarget() instanceof Player) setTarget(null);
    }

    @Override public boolean isFood(ItemStack stack) { return stack.is(Items.COD) || stack.is(Items.SALMON); }
    @Nullable @Override public GOTBearEntity getBreedOffspring(ServerLevel level, AgeableMob mate) { return (GOTBearEntity) getType().create(level); }

    @Override protected void dropCustomDeathLoot(DamageSource source, int looting, boolean recentlyHit) {
        super.dropCustomDeathLoot(source, looting, recentlyHit);
        int fur = 1 + random.nextInt(3) + random.nextInt(looting + 1);
        for (int i=0;i<fur;i++) spawnAtLocation(GOTItems.FUR.get());
    }

    @Override public void addAdditionalSaveData(CompoundTag tag) { super.addAdditionalSaveData(tag); tag.putInt("GOTAngryTicks", angryTicks); }
    @Override public void readAdditionalSaveData(CompoundTag tag) { super.readAdditionalSaveData(tag); angryTicks = tag.getInt("GOTAngryTicks"); }
}
