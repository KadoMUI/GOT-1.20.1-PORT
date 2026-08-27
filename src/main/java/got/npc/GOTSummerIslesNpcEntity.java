package got.npc;

import got.faction.GOTFaction;
import got.world.biome.GOTBiomeMetadata;
import got.world.biome.PlanetosBiomeManager;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.trading.MerchantOffers;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;

import javax.annotation.Nullable;

/** Summer Isles roster backed by the shared human AI while retaining its own legacy identity. */
public final class GOTSummerIslesNpcEntity extends GOTNorvosNpcEntity {
    private static final EntityDataAccessor<String> DATA_SUMMER_ROLE =
            SynchedEntityData.defineId(GOTSummerIslesNpcEntity.class, EntityDataSerializers.STRING);
    private MerchantOffers summerOffers;

    public GOTSummerIslesNpcEntity(EntityType<? extends GOTSummerIslesNpcEntity> type, Level level) {
        super(type, level);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        entityData.define(DATA_SUMMER_ROLE, SummerIslesNpcRole.SUMMER_MAN.id());
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty,
                                        MobSpawnType spawnType, @Nullable SpawnGroupData spawnData,
                                        @Nullable CompoundTag dataTag) {
        SpawnGroupData result = super.finalizeSpawn(level, difficulty, spawnType, spawnData, dataTag);
        if (spawnType == MobSpawnType.NATURAL || spawnType == MobSpawnType.CHUNK_GENERATION) {
            GOTBiomeMetadata metadata = PlanetosBiomeManager.getMetadata(blockPosition().getX(), blockPosition().getZ());
            if (metadata != null && (metadata.id().equals("summer_islands")
                    || metadata.id().equals("summer_colony"))) {
                SummerIslesNpcRole role = random.nextInt(15) < 10
                        ? SummerIslesNpcRole.SUMMER_SOLDIER
                        : SummerIslesNpcRole.SUMMER_SOLDIER_ARCHER;
                prepareForSpawn(role, false, false, blockPosition(), 24, "");
            }
        } else {
            prepareForSpawn(getSummerRole(), null, false, blockPosition(), 24, "");
        }
        return result;
    }

    public void prepareForSpawn(SummerIslesNpcRole role, @Nullable Boolean female, boolean child,
                                BlockPos home, int homeRadius, String populationKey) {
        boolean resolvedFemale = female != null ? female : switch (role.gender()) {
            case FEMALE -> true;
            case MALE -> false;
            case RANDOM -> random.nextBoolean();
        };
        entityData.set(DATA_SUMMER_ROLE, role.id());
        super.prepareForSpawn(role.parentRole(), resolvedFemale, child, home, homeRadius, populationKey);
        setCustomName(Component.literal(GOTNpcNames.randomSothoryos(random, resolvedFemale)));
        setCustomNameVisible(false);
        summerOffers = null;
        if (!level().isClientSide) GOTSummerIslesNpcLoadouts.configure(this);
    }

    public SummerIslesNpcRole getSummerRole() {
        return SummerIslesNpcRole.byId(entityData.get(DATA_SUMMER_ROLE));
    }

    @Override public String getFactionId() { return "summer_islands"; }
    @Override public String getQuestRoleId() { return getSummerRole().id(); }
    @Override public GOTFaction getQuestFaction() { return GOTFaction.SUMMER_ISLANDS; }
    @Override public boolean canOfferQuests() {
        return isAlive() && !isBaby() && getSummerRole().trade() == SummerIslesNpcRole.Trade.NONE;
    }
    @Override public int getAlignmentBonus() { return getSummerRole().alignmentBonus(); }
    @Override public boolean isCivilian() { return !getSummerRole().activeCombatant(); }
    @Override public boolean isActiveCombatant() { return getSummerRole().activeCombatant(); }

    @Override
    public void aiStep() {
        super.aiStep();
        if (!level().isClientSide && summerOffers != null
                && got.economy.GOTNpcTraderRuntime.tick(this, summerOffers)) {
            summerOffers = null;
        }
        got.economy.GOTTraderAdvertisement.tick(this);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putString("SummerRole", getSummerRole().id());
        if (summerOffers != null) tag.put("SummerOffers", summerOffers.createTag());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        entityData.set(DATA_SUMMER_ROLE, SummerIslesNpcRole.byId(tag.getString("SummerRole")).id());
        if (tag.contains("SummerOffers")) summerOffers = new MerchantOffers(tag.getCompound("SummerOffers"));
    }

    @Override public MerchantOffers getOffers() {
        if (summerOffers == null) summerOffers = GOTSummerIslesNpcLoadouts.createOffers(getSummerRole());
        return summerOffers;
    }
    @Override public void overrideOffers(MerchantOffers offers) { summerOffers = offers; }
}
