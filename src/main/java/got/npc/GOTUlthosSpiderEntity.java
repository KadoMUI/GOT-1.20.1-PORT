package got.npc;

import got.GOTItems;
import got.faction.GOTFaction;
import got.faction.GOTFactionPlayerData;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.monster.Spider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.UUID;

/** Three-status, three-size Ulthos spider with the legacy alignment-gated mount behavior. */
public final class GOTUlthosSpiderEntity extends Spider implements GOTFactionNpc {
    private static final EntityDataAccessor<Integer> DATA_SCALE =
            SynchedEntityData.defineId(GOTUlthosSpiderEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> DATA_VARIANT =
            SynchedEntityData.defineId(GOTUlthosSpiderEntity.class, EntityDataSerializers.INT);
    @Nullable private UUID ownerUuid;

    public GOTUlthosSpiderEntity(EntityType<? extends GOTUlthosSpiderEntity> type, Level level) {
        super(type, level);
        xpReward = 5;
    }

    public static net.minecraft.world.entity.ai.attributes.AttributeSupplier.Builder createAttributes() {
        return Spider.createAttributes();
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        entityData.define(DATA_SCALE, 1);
        entityData.define(DATA_VARIANT, Variant.NORMAL.id);
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty,
                                        MobSpawnType spawnType, @Nullable SpawnGroupData spawnData,
                                        @Nullable CompoundTag dataTag) {
        SpawnGroupData result = super.finalizeSpawn(level, difficulty, spawnType, spawnData, dataTag);
        randomizeLegacyVariant();
        return result;
    }

    public void randomizeLegacyVariant() {
        entityData.set(DATA_SCALE, random.nextInt(3));
        int roll = random.nextInt(4);
        entityData.set(DATA_VARIANT, roll < 2 ? Variant.NORMAL.id
                : roll == 2 ? Variant.SLOWNESS.id : Variant.POISON.id);
        applyVariantAttributes();
        refreshDimensions();
    }

    public int getScaleLevel() { return entityData.get(DATA_SCALE); }
    public float getVisualScale() { return 0.5F + getScaleLevel() * 0.5F; }
    public Variant getVariant() { return Variant.byId(entityData.get(DATA_VARIANT)); }
    public boolean isTamedSpider() { return ownerUuid != null; }
    public boolean isOwnedBy(Player player) { return ownerUuid != null && ownerUuid.equals(player.getUUID()); }

    @Override
    public EntityDimensions getDimensions(Pose pose) {
        return super.getDimensions(pose).scale(getVisualScale());
    }

    @Override
    public boolean doHurtTarget(Entity target) {
        boolean hurt = super.doHurtTarget(target);
        if (hurt && target instanceof LivingEntity living && getVariant() != Variant.NORMAL) {
            Difficulty difficulty = level().getDifficulty();
            int rank = difficulty == Difficulty.HARD ? 3 : difficulty == Difficulty.NORMAL ? 2 : 1;
            int duration = 20 * rank * (rank + 5) / 2;
            living.addEffect(new MobEffectInstance(getVariant() == Variant.POISON
                    ? MobEffects.POISON : MobEffects.MOVEMENT_SLOWDOWN, duration));
        }
        return hurt;
    }

    @Override
    public boolean canBeAffected(MobEffectInstance effect) {
        if (getVariant() == Variant.POISON && effect.getEffect() == MobEffects.POISON) return false;
        if (getVariant() == Variant.SLOWNESS && effect.getEffect() == MobEffects.MOVEMENT_SLOWDOWN) return false;
        return super.canBeAffected(effect);
    }

    @Override
    protected InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack held = player.getItemInHand(hand);
        float alignment = GOTFactionPlayerData.get(player).alignment(GOTFaction.ULTHOS);
        if (held.is(Items.BONE)) {
            if (alignment < 50.0F) {
                if (!level().isClientSide) player.displayClientMessage(
                        Component.translatable("entity.got.ulthos_spider.alignment", 50), true);
                return InteractionResult.sidedSuccess(level().isClientSide);
            }
            if (!level().isClientSide) {
                if (!player.getAbilities().instabuild) held.shrink(1);
                if (!isTamedSpider() && random.nextInt(3) == 0) {
                    ownerUuid = player.getUUID();
                    setPersistenceRequired();
                    level().broadcastEntityEvent(this, (byte)7);
                } else if (isOwnedBy(player)) {
                    heal(4.0F);
                    level().broadcastEntityEvent(this, (byte)7);
                } else {
                    level().broadcastEntityEvent(this, (byte)6);
                }
            }
            return InteractionResult.sidedSuccess(level().isClientSide);
        }
        if (isOwnedBy(player) && getVariant() == Variant.POISON && held.is(Items.SPIDER_EYE)) {
            if (!level().isClientSide) {
                if (!player.getAbilities().instabuild) held.shrink(1);
                ItemStack poison = new ItemStack(GOTItems.BOTTLE_POISON.get());
                if (!player.getInventory().add(poison)) player.drop(poison, false);
            }
            return InteractionResult.sidedSuccess(level().isClientSide);
        }
        if (isOwnedBy(player) && getScaleLevel() > 0 && held.isEmpty() && !isVehicle()) {
            if (!level().isClientSide) player.startRiding(this);
            return InteractionResult.sidedSuccess(level().isClientSide);
        }
        return super.mobInteract(player, hand);
    }

    @Override
    @Nullable
    public LivingEntity getControllingPassenger() {
        Entity passenger = getFirstPassenger();
        return passenger instanceof Player player && isOwnedBy(player) ? player : null;
    }

    @Override
    public void travel(Vec3 input) {
        LivingEntity rider = getControllingPassenger();
        if (isAlive() && rider instanceof Player player) {
            setYRot(player.getYRot());
            yRotO = getYRot();
            setXRot(player.getXRot() * 0.5F);
            setRot(getYRot(), getXRot());
            yBodyRot = getYRot();
            yHeadRot = getYRot();
            float strafe = player.xxa * 0.5F;
            float forward = player.zza;
            if (forward <= 0.0F) forward *= 0.25F;
            setSpeed((float)getAttributeValue(net.minecraft.world.entity.ai.attributes.Attributes.MOVEMENT_SPEED));
            super.travel(new Vec3(strafe, input.y, forward));
            return;
        }
        super.travel(input);
    }

    @Override public boolean causeFallDamage(float distance, float multiplier,
                                              net.minecraft.world.damagesource.DamageSource source) { return false; }
    @Override public boolean canFreeze() { return false; }
    @Override public String getFactionId() { return "ulthos"; }
    @Override public int getAlignmentBonus() { return 2; }
    @Override public boolean isCivilian() { return false; }
    @Override public boolean isActiveCombatant() { return true; }

    @Override
    public boolean isAlliedTo(Entity entity) {
        return entity instanceof Player player && isOwnedBy(player) || super.isAlliedTo(entity);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("UlthosScale", getScaleLevel());
        tag.putInt("UlthosVariant", getVariant().id);
        if (ownerUuid != null) tag.putUUID("UlthosOwner", ownerUuid);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        entityData.set(DATA_SCALE, Math.max(0, Math.min(2, tag.getInt("UlthosScale"))));
        entityData.set(DATA_VARIANT, Variant.byId(tag.getInt("UlthosVariant")).id);
        ownerUuid = tag.hasUUID("UlthosOwner") ? tag.getUUID("UlthosOwner") : null;
        applyVariantAttributes();
        refreshDimensions();
    }

    private void applyVariantAttributes() {
        int scale = getScaleLevel();
        double health = 12.0D + scale * 6.0D;
        if (getAttribute(net.minecraft.world.entity.ai.attributes.Attributes.MAX_HEALTH) != null) {
            getAttribute(net.minecraft.world.entity.ai.attributes.Attributes.MAX_HEALTH).setBaseValue(health);
            setHealth((float)health);
        }
        if (getAttribute(net.minecraft.world.entity.ai.attributes.Attributes.MOVEMENT_SPEED) != null) {
            getAttribute(net.minecraft.world.entity.ai.attributes.Attributes.MOVEMENT_SPEED).setBaseValue(0.35D - scale * 0.03D);
        }
        if (getAttribute(net.minecraft.world.entity.ai.attributes.Attributes.ATTACK_DAMAGE) != null) {
            getAttribute(net.minecraft.world.entity.ai.attributes.Attributes.ATTACK_DAMAGE).setBaseValue(2.0D + scale);
        }
    }

    public enum Variant {
        NORMAL(0), SLOWNESS(1), POISON(2);
        private final int id;
        Variant(int id) { this.id = id; }
        static Variant byId(int id) { return id == 1 ? SLOWNESS : id == 2 ? POISON : NORMAL; }
    }
}
