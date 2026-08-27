package got.wildlife;

import got.GOTItems;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.*;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class GOTBisonEntity extends Cow {
    public GOTBisonEntity(EntityType<? extends GOTBisonEntity> type, Level level) { super(type, level); xpReward = 4; }
    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 30.0D).add(Attributes.MOVEMENT_SPEED, 0.22D)
                .add(Attributes.ATTACK_DAMAGE, 4.0D).add(Attributes.FOLLOW_RANGE, 20.0D);
    }
    @Override protected void registerGoals() {
        goalSelector.addGoal(0, new FloatGoal(this));
        goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.4D, true));
        goalSelector.addGoal(2, new PanicGoal(this, 1.25D));
        goalSelector.addGoal(3, new BreedGoal(this, 1.0D));
        goalSelector.addGoal(4, new TemptGoal(this, 1.1D, net.minecraft.world.item.crafting.Ingredient.of(net.minecraft.world.item.Items.WHEAT), false));
        goalSelector.addGoal(5, new FollowParentGoal(this, 1.1D));
        goalSelector.addGoal(6, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 8.0F));
        goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        targetSelector.addGoal(1, new HurtByTargetGoal(this).setAlertOthers(GOTBisonEntity.class));
    }
    @Override public boolean doHurtTarget(Entity target) {
        boolean hit = super.doHurtTarget(target);
        if (hit) {
            double dx = target.getX() - getX(), dz = target.getZ() - getZ();
            double len = Math.max(0.001D, Math.sqrt(dx*dx + dz*dz));
            target.push(dx / len * 0.38D, 0.18D, dz / len * 0.38D);
        }
        return hit;
    }
    @Nullable @Override public Cow getBreedOffspring(ServerLevel level, AgeableMob mate) { return (Cow) getType().create(level); }
    protected net.minecraft.world.item.Item hornItem() { return GOTItems.HORN.get(); }
    @Override protected void dropCustomDeathLoot(DamageSource source, int looting, boolean recentlyHit) {
        super.dropCustomDeathLoot(source, looting, recentlyHit);
        int leather = 2 + random.nextInt(3) + random.nextInt(looting + 1);
        int meat = 2 + random.nextInt(3) + random.nextInt(looting + 1);
        for (int i = 0; i < leather; i++) spawnAtLocation(net.minecraft.world.item.Items.LEATHER);
        for (int i = 0; i < meat; i++) spawnAtLocation(isOnFire() ? net.minecraft.world.item.Items.COOKED_BEEF : net.minecraft.world.item.Items.BEEF);
        spawnAtLocation(hornItem());
    }
    @Override public boolean canFreeze() { return false; }
}
