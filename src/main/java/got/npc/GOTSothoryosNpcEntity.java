package got.npc;

import got.faction.GOTFaction;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.item.trading.MerchantOffers;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;

import javax.annotation.Nullable;

/** Sothoryosi warrior, blowgunner, shaman, farming and smith roster. */
public final class GOTSothoryosNpcEntity extends GOTNorvosNpcEntity {
    private static final EntityDataAccessor<String> DATA_SOTHORYOS_ROLE =
            SynchedEntityData.defineId(GOTSothoryosNpcEntity.class, EntityDataSerializers.STRING);
    private MerchantOffers sothoryosOffers;

    public GOTSothoryosNpcEntity(EntityType<? extends GOTSothoryosNpcEntity> type, Level level) {
        super(type, level);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        entityData.define(DATA_SOTHORYOS_ROLE, SothoryosNpcRole.SOTHORYOS_MAN.id());
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty,
                                        MobSpawnType spawnType, @Nullable SpawnGroupData spawnData,
                                        @Nullable CompoundTag dataTag) {
        SpawnGroupData result = super.finalizeSpawn(level, difficulty, spawnType, spawnData, dataTag);
        if (spawnType == MobSpawnType.NATURAL || spawnType == MobSpawnType.CHUNK_GENERATION) {
            SothoryosNpcRole role = random.nextInt(15) < 10
                    ? SothoryosNpcRole.SOTHORYOS_WARRIOR
                    : SothoryosNpcRole.SOTHORYOS_BLOWGUNNER;
            prepareForSpawn(role, false, false, blockPosition(), 24, "");
        } else {
            prepareForSpawn(getSothoryosRole(), null, false, blockPosition(), 24, "");
        }
        return result;
    }

    public void prepareForSpawn(SothoryosNpcRole role, @Nullable Boolean female, boolean child,
                                BlockPos home, int homeRadius, String populationKey) {
        boolean resolvedFemale = female != null ? female : switch (role.gender()) {
            case FEMALE -> true;
            case MALE -> false;
            case RANDOM -> random.nextBoolean();
        };
        entityData.set(DATA_SOTHORYOS_ROLE, role.id());
        super.prepareForSpawn(role.parentRole(), resolvedFemale, child, home, homeRadius, populationKey);
        setCustomName(Component.literal(GOTNpcNames.randomSothoryos(random, resolvedFemale)));
        setCustomNameVisible(false);
        sothoryosOffers = null;
        if (!level().isClientSide) GOTSothoryosNpcLoadouts.configure(this);
    }

    public SothoryosNpcRole getSothoryosRole() {
        return SothoryosNpcRole.byId(entityData.get(DATA_SOTHORYOS_ROLE));
    }

    @Override public String getFactionId() { return "sothoryos"; }
    @Override public String getQuestRoleId() { return getSothoryosRole().id(); }
    @Override public GOTFaction getQuestFaction() { return GOTFaction.SOTHORYOS; }
    @Override public boolean canOfferQuests() {
        return isAlive() && !isBaby() && getSothoryosRole().trade() == SothoryosNpcRole.Trade.NONE;
    }
    @Override public int getAlignmentBonus() { return getSothoryosRole().alignmentBonus(); }
    @Override public boolean isCivilian() { return !getSothoryosRole().activeCombatant(); }
    @Override public boolean isActiveCombatant() { return getSothoryosRole().activeCombatant(); }

    @Override
    public void performRangedAttack(LivingEntity target, float distanceFactor) {
        Arrow dart = new Arrow(level(), this);
        double dx = target.getX() - getX();
        double dy = target.getY(0.3333333333333333D) - dart.getY();
        double dz = target.getZ() - getZ();
        double horizontal = Math.sqrt(dx * dx + dz * dz);
        dart.shoot(dx, dy + horizontal * 0.14D, dz, 1.8F, 5.0F);
        dart.setBaseDamage(1.5D + distanceFactor * 0.5D);
        playSound(SoundEvents.ARROW_SHOOT, 0.75F, 1.35F + random.nextFloat() * 0.2F);
        level().addFreshEntity(dart);
        swing(net.minecraft.world.InteractionHand.MAIN_HAND);
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (!level().isClientSide && sothoryosOffers != null
                && got.economy.GOTNpcTraderRuntime.tick(this, sothoryosOffers)) {
            sothoryosOffers = null;
        }
        got.economy.GOTTraderAdvertisement.tick(this);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putString("SothoryosRole", getSothoryosRole().id());
        if (sothoryosOffers != null) tag.put("SothoryosOffers", sothoryosOffers.createTag());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        entityData.set(DATA_SOTHORYOS_ROLE, SothoryosNpcRole.byId(tag.getString("SothoryosRole")).id());
        if (tag.contains("SothoryosOffers")) sothoryosOffers = new MerchantOffers(tag.getCompound("SothoryosOffers"));
    }

    @Override public MerchantOffers getOffers() {
        if (sothoryosOffers == null) sothoryosOffers = GOTSothoryosNpcLoadouts.createOffers(getSothoryosRole());
        return sothoryosOffers;
    }
    @Override public void overrideOffers(MerchantOffers offers) { sothoryosOffers = offers; }
}
