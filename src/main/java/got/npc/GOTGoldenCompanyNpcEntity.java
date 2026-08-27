package got.npc;

import got.faction.GOTFactionHurtByTargetGoal;
import got.faction.GOTFactionTargetGoal;
import got.world.biome.GOTBiomeMetadata;
import got.world.biome.PlanetosBiomeManager;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.OpenDoorGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.trading.Merchant;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;

import javax.annotation.Nullable;
import java.util.UUID;

/** Unaligned Golden Company troops with persistent player ownership. */
public class GOTGoldenCompanyNpcEntity extends PathfinderMob implements
        Merchant, GOTFactionNpc, GOTHiredNpc {
    private static final EntityDataAccessor<String> DATA_ROLE =
            SynchedEntityData.defineId(GOTGoldenCompanyNpcEntity.class, EntityDataSerializers.STRING);
    private static final EntityDataAccessor<Integer> DATA_SKIN =
            SynchedEntityData.defineId(GOTGoldenCompanyNpcEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<String> DATA_POPULATION_KEY =
            SynchedEntityData.defineId(GOTGoldenCompanyNpcEntity.class, EntityDataSerializers.STRING);

    private ItemStack combatWeapon = ItemStack.EMPTY;
    private ItemStack idleItem = ItemStack.EMPTY;
    private MerchantOffers offers;
    private Player tradingPlayer;
    private int villagerXp;
    @Nullable private UUID hiredOwnerUUID;
    private boolean followingHiredOwner = true;

    public GOTGoldenCompanyNpcEntity(EntityType<? extends GOTGoldenCompanyNpcEntity> type,
                                     Level level) {
        super(type, level);
        setCanPickUpLoot(false);
        xpReward = 5;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 20.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.23D)
                .add(Attributes.ATTACK_DAMAGE, 4.0D)
                .add(Attributes.ARMOR, 0.0D)
                .add(Attributes.FOLLOW_RANGE, 24.0D);
    }

    @Override protected void defineSynchedData() {
        super.defineSynchedData();
        entityData.define(DATA_ROLE, GoldenCompanyNpcRole.GOLDEN_COMPANY_WARRIOR.id());
        entityData.define(DATA_SKIN, 0);
        entityData.define(DATA_POPULATION_KEY, "");
    }

    @Override protected void registerGoals() {
        goalSelector.addGoal(0, new FloatGoal(this));
        goalSelector.addGoal(1, new OpenDoorGoal(this, true));
        goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.35D, false));
        goalSelector.addGoal(5, new RandomStrollGoal(this, 0.9D));
        goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 8.0F));
        goalSelector.addGoal(8, new LookAtPlayerGoal(this, GOTGoldenCompanyNpcEntity.class, 5.0F));
        goalSelector.addGoal(9, new RandomLookAroundGoal(this));
        targetSelector.addGoal(1, new GOTFactionHurtByTargetGoal(this));
        targetSelector.addGoal(2, new GOTFactionTargetGoal(this));
        targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Monster.class, true) {
            @Override public boolean canUse() { return getRole().activeCombatant() && super.canUse(); }
        });
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty,
                                        MobSpawnType spawnType, @Nullable SpawnGroupData spawnData,
                                        @Nullable CompoundTag dataTag) {
        SpawnGroupData result = super.finalizeSpawn(level, difficulty, spawnType, spawnData, dataTag);
        if (spawnType == MobSpawnType.NATURAL || spawnType == MobSpawnType.CHUNK_GENERATION) {
            GOTBiomeMetadata metadata = PlanetosBiomeManager.getMetadata(blockPosition().getX(), blockPosition().getZ());
            if (metadata != null && metadata.id().equals("disputed_lands")) {
                GoldenCompanyNpcRole role = random.nextInt(15) < 10
                        ? GoldenCompanyNpcRole.GOLDEN_COMPANY_WARRIOR
                        : GoldenCompanyNpcRole.GOLDEN_COMPANY_SPEARMAN;
                prepareForSpawn(role, blockPosition(), 24, "");
            }
        } else if (getCustomName() == null) {
            prepareForSpawn(getRole(), blockPosition(), 24, "");
        }
        return result;
    }

    public void prepareForSpawn(GoldenCompanyNpcRole role, BlockPos home, int homeRadius,
                                String populationKey) {
        entityData.set(DATA_ROLE, role.id());
        entityData.set(DATA_SKIN, random.nextInt(10000));
        String key = populationKey == null ? "" : populationKey;
        entityData.set(DATA_POPULATION_KEY, key);
        setCustomName(Component.literal(role.legendary() ? role.displayName()
                : GOTNpcNames.randomEssos(random, false)));
        setCustomNameVisible(role.legendary());
        if (role.legendary() || !key.isEmpty()) setPersistenceRequired();
        restrictTo(home, Math.max(4, homeRadius));
        applyRoleAttributes();
        if (!level().isClientSide) GOTGoldenCompanyNpcLoadouts.configure(this);
    }

    public GoldenCompanyNpcRole getRole() {
        return GoldenCompanyNpcRole.byId(entityData.get(DATA_ROLE));
    }
    public int getSkinIndex() { return entityData.get(DATA_SKIN); }
    public String getPopulationKey() { return entityData.get(DATA_POPULATION_KEY); }
    public boolean isAimingBow() { return false; }
    @Override public String getFactionId() { return "unaligned"; }
    @Override public int getAlignmentBonus() { return 0; }
    @Override public boolean isCivilian() { return false; }
    @Override public boolean isActiveCombatant() { return getRole().activeCombatant(); }

    @Override public void setTarget(@Nullable LivingEntity target) {
        if (target != null && isHiredAlly(target)) return;
        super.setTarget(target);
        updateHeldItem();
    }

    @Override protected InteractionResult mobInteract(Player player, InteractionHand hand) {
        if (isHired()) {
            if (isHiredOwner(player)) {
                if (!level().isClientSide) {
                    setFollowingHiredOwner(!isFollowingHiredOwner());
                    if (!isFollowingHiredOwner()) getNavigation().stop();
                    player.displayClientMessage(Component.translatable(isFollowingHiredOwner()
                            ? "got.hired.following" : "got.hired.guarding", getDisplayName()), true);
                }
            } else if (!level().isClientSide) {
                player.displayClientMessage(Component.translatable("got.hired.not_owner"), true);
            }
            return InteractionResult.sidedSuccess(level().isClientSide);
        }
        if (getRole().trade() == GoldenCompanyNpcRole.Trade.UNITS && isAlive()) {
            if (!level().isClientSide) {
                setTradingPlayer(player);
                openTradingScreen(player, getDisplayName(), getRole().ordinal());
            }
            return InteractionResult.sidedSuccess(level().isClientSide);
        }
        return super.mobInteract(player, hand);
    }

    @Override public void tick() {
        super.tick();
        if (!level().isClientSide) tickHiredBehavior(this);
    }

    @Override public boolean isAlliedTo(Entity entity) {
        return isHiredAlly(entity) || super.isAlliedTo(entity);
    }

    @Override public boolean hurt(DamageSource source, float amount) {
        Entity attacker = source.getEntity();
        return (attacker == null || !isHiredAlly(attacker)) && super.hurt(source, amount);
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (!level().isClientSide && offers != null
                && got.economy.GOTNpcTraderRuntime.tick(this, offers)) {
            offers = null;
        }
        got.economy.GOTTraderAdvertisement.tick(this);
    }

    void clearLoadout() {
        combatWeapon = ItemStack.EMPTY;
        idleItem = ItemStack.EMPTY;
        for (EquipmentSlot slot : EquipmentSlot.values()) setItemSlot(slot, ItemStack.EMPTY);
    }
    void setWeapons(ItemStack combat, ItemStack idle) { combatWeapon = combat; idleItem = idle; }
    void updateHeldItem() {
        ItemStack held = getTarget() == null ? idleItem : combatWeapon;
        setItemSlot(EquipmentSlot.MAINHAND, held.isEmpty() ? ItemStack.EMPTY : held.copy());
        for (EquipmentSlot slot : EquipmentSlot.values()) setDropChance(slot, 0.0F);
    }

    private void applyRoleAttributes() {
        double health = getRole().legendary() ? 30.0D : 20.0D;
        if (getAttribute(Attributes.MAX_HEALTH) != null) {
            getAttribute(Attributes.MAX_HEALTH).setBaseValue(health);
            setHealth((float) health);
        }
        if (getAttribute(Attributes.ARMOR) != null) {
            getAttribute(Attributes.ARMOR).setBaseValue(getRole().legendary() ? 10.0D : 0.0D);
        }
        if (getAttribute(Attributes.ATTACK_DAMAGE) != null) {
            getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(4.0D);
        }
    }

    @Override public @Nullable UUID getHiredOwnerUUID() { return hiredOwnerUUID; }
    @Override public void setHiredOwner(Player owner) {
        hiredOwnerUUID = owner.getUUID();
        followingHiredOwner = true;
        setPersistenceRequired();
        clearRestriction();
    }
    @Override public boolean isFollowingHiredOwner() { return followingHiredOwner; }
    @Override public void setFollowingHiredOwner(boolean following) { followingHiredOwner = following; }

    @Override public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putString("GoldenCompanyRole", getRole().id());
        tag.putInt("GoldenCompanySkin", getSkinIndex());
        tag.putString("PopulationKey", getPopulationKey());
        tag.put("CombatWeapon", combatWeapon.save(new CompoundTag()));
        tag.put("IdleItem", idleItem.save(new CompoundTag()));
        if (offers != null) tag.put("Offers", offers.createTag());
        tag.putInt("TradeXp", villagerXp);
        if (hiredOwnerUUID != null) tag.putUUID("HiredOwner", hiredOwnerUUID);
        tag.putBoolean("HiredFollowing", followingHiredOwner);
    }

    @Override public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        entityData.set(DATA_ROLE, GoldenCompanyNpcRole.byId(tag.getString("GoldenCompanyRole")).id());
        entityData.set(DATA_SKIN, tag.getInt("GoldenCompanySkin"));
        entityData.set(DATA_POPULATION_KEY, tag.getString("PopulationKey"));
        if (tag.contains("CombatWeapon")) combatWeapon = ItemStack.of(tag.getCompound("CombatWeapon"));
        if (tag.contains("IdleItem")) idleItem = ItemStack.of(tag.getCompound("IdleItem"));
        if (tag.contains("Offers")) offers = new MerchantOffers(tag.getCompound("Offers"));
        villagerXp = tag.getInt("TradeXp");
        hiredOwnerUUID = tag.hasUUID("HiredOwner") ? tag.getUUID("HiredOwner") : null;
        followingHiredOwner = !tag.contains("HiredFollowing") || tag.getBoolean("HiredFollowing");
        applyRoleAttributes();
        updateHeldItem();
    }

    @Override public void setTradingPlayer(@Nullable Player player) { tradingPlayer = player; }
    @Override @Nullable public Player getTradingPlayer() { return tradingPlayer; }
    @Override public MerchantOffers getOffers() {
        if (offers == null) offers = GOTGoldenCompanyNpcLoadouts.createOffers(getRole());
        return offers;
    }
    @Override public void overrideOffers(MerchantOffers offers) { this.offers = offers; }
    @Override public void notifyTrade(MerchantOffer offer) {
        villagerXp += offer.getXp();
        got.economy.GOTNpcTraderRuntime.onTrade(this, getOffers(), offer);
        if (tradingPlayer instanceof net.minecraft.server.level.ServerPlayer serverPlayer) {
            got.economy.GOTTradeProgress.record(serverPlayer, getFaction());
        }
        got.economy.GOTTraderAdvertisement.markTrade(this);
    }
    @Override public void notifyTradeUpdated(ItemStack stack) { }
    @Override public int getVillagerXp() { return villagerXp; }
    @Override public void overrideXp(int xp) { villagerXp = xp; }
    @Override public boolean showProgressBar() { return false; }
    @Override public SoundEvent getNotifyTradeSound() { return SoundEvents.VILLAGER_YES; }
    @Override public boolean isClientSide() { return level().isClientSide; }
}
