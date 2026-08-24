package got.npc;

import got.faction.GOTFactionHurtByTargetGoal;
import got.faction.GOTFactionTargetGoal;
import got.quest.GOTQuestGiver;
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
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.OpenDoorGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.RangedAttackGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.entity.projectile.LargeFireball;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.entity.projectile.SmallFireball;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.Merchant;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;

import javax.annotation.Nullable;

/** Shared modern entity implementation for the complete Ibben roster. */
public class GOTIbbenNpcEntity extends PathfinderMob implements net.minecraft.world.entity.monster.RangedAttackMob, Merchant, GOTFactionNpc, GOTQuestGiver {
    private static final EntityDataAccessor<String> DATA_ROLE =
            SynchedEntityData.defineId(GOTIbbenNpcEntity.class, EntityDataSerializers.STRING);
    private static final EntityDataAccessor<Boolean> DATA_FEMALE =
            SynchedEntityData.defineId(GOTIbbenNpcEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> DATA_CHILD =
            SynchedEntityData.defineId(GOTIbbenNpcEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> DATA_SKIN =
            SynchedEntityData.defineId(GOTIbbenNpcEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<String> DATA_POPULATION_KEY =
            SynchedEntityData.defineId(GOTIbbenNpcEntity.class, EntityDataSerializers.STRING);

    private ItemStack combatWeapon = ItemStack.EMPTY;
    private ItemStack rangedWeapon = ItemStack.EMPTY;
    private ItemStack idleItem = ItemStack.EMPTY;
    private MerchantOffers offers;
    private Player tradingPlayer;
    private int villagerXp;

    public GOTIbbenNpcEntity(EntityType<? extends GOTIbbenNpcEntity> type, Level level) {
        super(type, level);
        setCanPickUpLoot(false);
        xpReward = 5;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 20.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.22D)
                .add(Attributes.ATTACK_DAMAGE, 3.0D)
                .add(Attributes.ARMOR, 0.0D)
                .add(Attributes.FOLLOW_RANGE, 24.0D);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        entityData.define(DATA_ROLE, IbbenNpcRole.IBBEN_MAN.id());
        entityData.define(DATA_FEMALE, false);
        entityData.define(DATA_CHILD, false);
        entityData.define(DATA_SKIN, 0);
        entityData.define(DATA_POPULATION_KEY, "");
    }

    @Override
    protected void registerGoals() {
        goalSelector.addGoal(0, new FloatGoal(this));
        goalSelector.addGoal(1, new OpenDoorGoal(this, true));
        goalSelector.addGoal(2, new RangedAttackGoal(this, 1.25D, 35, 20.0F) {
            @Override public boolean canUse() {
                return usesRangedCombat() && super.canUse();
            }
            @Override public boolean canContinueToUse() {
                return usesRangedCombat() && super.canContinueToUse();
            }
        });
        goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.35D, false) {
            @Override public boolean canUse() {
                return !usesRangedCombat() && super.canUse();
            }
            @Override public boolean canContinueToUse() {
                return !usesRangedCombat() && super.canContinueToUse();
            }
        });
        goalSelector.addGoal(3, new AvoidEntityGoal<>(this, Monster.class, 8.0F, 1.2D, 1.5D) {
            @Override public boolean canUse() {
                return getRole().combat() == IbbenNpcRole.Combat.PASSIVE
                        && getTarget() == null && super.canUse();
            }
        });
        goalSelector.addGoal(5, new RandomStrollGoal(this, 0.9D));
        goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 8.0F));
        goalSelector.addGoal(8, new LookAtPlayerGoal(this, GOTIbbenNpcEntity.class, 5.0F));
        goalSelector.addGoal(9, new RandomLookAroundGoal(this));

        targetSelector.addGoal(1, new GOTFactionHurtByTargetGoal(this));
        targetSelector.addGoal(2, new GOTFactionTargetGoal(this));
        targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Monster.class, true) {
            @Override public boolean canUse() {
                return getRole().activeCombatant() && super.canUse();
            }
        });
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty,
                                        MobSpawnType spawnType, @Nullable SpawnGroupData spawnData,
                                        @Nullable CompoundTag dataTag) {
        SpawnGroupData result = super.finalizeSpawn(level, difficulty, spawnType, spawnData, dataTag);
        if (spawnType == MobSpawnType.NATURAL || spawnType == MobSpawnType.CHUNK_GENERATION) {
            GOTBiomeMetadata metadata = PlanetosBiomeManager.getMetadata(blockPosition().getX(), blockPosition().getZ());
            if (metadata != null && (metadata.id().equals("ibben")
                    || metadata.id().equals("ibben_colony"))) {
                int roll = random.nextInt(15);
                IbbenNpcRole role = roll < 10 ? IbbenNpcRole.IBBEN_LEVYMAN
                        : IbbenNpcRole.IBBEN_LEVYMAN_ARCHER;
                prepareForSpawn(role, false, false,
                        blockPosition(), 24, "");
            }
        } else if (getCustomName() == null) {
            prepareForSpawn(getRole(), null, false, blockPosition(), 24, "");
        }
        return result;
    }

    public void prepareForSpawn(IbbenNpcRole role, @Nullable Boolean female, boolean child,
                                BlockPos home, int homeRadius, String populationKey) {
        entityData.set(DATA_ROLE, role.id());
        boolean resolvedFemale = female != null ? female : switch (role.gender()) {
            case FEMALE -> true;
            case MALE -> false;
            case RANDOM -> random.nextBoolean();
        };
        entityData.set(DATA_FEMALE, resolvedFemale);
        entityData.set(DATA_CHILD, child && role.ordinaryCivilian());
        entityData.set(DATA_SKIN, random.nextInt(10000));
        String resolvedPopulationKey = populationKey == null ? "" : populationKey;
        entityData.set(DATA_POPULATION_KEY, resolvedPopulationKey);
        setCustomName(Component.literal(role.legendary()
                ? role.displayName()
                : GOTNpcNames.randomIbben(random, resolvedFemale)));
        setCustomNameVisible(role.legendary());
        if (role.legendary() || !resolvedPopulationKey.isEmpty()) setPersistenceRequired();
        restrictTo(home, Math.max(4, homeRadius));
        refreshDimensions();
        applyRoleAttributes();
        if (!level().isClientSide) GOTIbbenNpcLoadouts.configure(this);
    }

    public IbbenNpcRole getRole() { return IbbenNpcRole.byId(entityData.get(DATA_ROLE)); }
    @Override public String getFactionId() { return "ibben"; }
    @Override public String getQuestRoleId() { return getRole().id(); }
    @Override public got.faction.GOTFaction getQuestFaction() { return getFaction(); }
    @Override public boolean canOfferQuests() {
        return isAlive() && !isBaby() && getRole().trade() == IbbenNpcRole.Trade.NONE;
    }
    @Override public int getAlignmentBonus() { return getRole().alignmentBonus(); }
    @Override public boolean isCivilian() { return !getRole().legendary() && !getRole().activeCombatant(); }
    @Override public boolean isActiveCombatant() { return getRole().activeCombatant(); }
    public boolean isFemale() { return entityData.get(DATA_FEMALE); }
    @Override public boolean isBaby() { return entityData.get(DATA_CHILD); }
    public int getSkinIndex() { return entityData.get(DATA_SKIN); }
    public String getPopulationKey() { return entityData.get(DATA_POPULATION_KEY); }

    @Override
    public EntityDimensions getDimensions(Pose pose) {
        float scale = (isBaby() ? 0.5F : 1.0F) * getRole().collisionScale();
        return super.getDimensions(pose).scale(scale);
    }

    public boolean isAimingBow() {
        return getTarget() != null && usesRangedCombat();
    }

    private boolean usesRangedCombat() {
        if (getRole().combat() == IbbenNpcRole.Combat.ARCHER) return true;
        LivingEntity target = getTarget();
        return getRole().combat() == IbbenNpcRole.Combat.HYBRID
                && target != null && distanceToSqr(target) > 36.0D;
    }

    @Override
    public void setTarget(@Nullable LivingEntity target) {
        super.setTarget(target);
        updateHeldItem();
    }

    @Override
    public void performRangedAttack(LivingEntity target, float distanceFactor) {
        ItemStack bow = rangedWeapon.isEmpty() ? new ItemStack(Items.BOW) : rangedWeapon;
        Arrow arrow = new Arrow(level(), this);
        double dx = target.getX() - getX();
        double dy = target.getY(0.3333333333333333D) - arrow.getY();
        double dz = target.getZ() - getZ();
        double horizontal = Math.sqrt(dx * dx + dz * dz);
        arrow.shoot(dx, dy + horizontal * 0.2D, dz, 1.6F, 8.0F);
        arrow.setBaseDamage(2.0D + distanceFactor);
        playSound(SoundEvents.SKELETON_SHOOT, 1.0F,
                1.0F / (random.nextFloat() * 0.4F + 0.8F));
        level().addFreshEntity(arrow);
        swing(ProjectileUtil.getWeaponHoldingHand(this, item -> item instanceof BowItem));
    }

    @Override
    protected InteractionResult mobInteract(Player player, InteractionHand hand) {
        if (getRole().trade() != IbbenNpcRole.Trade.NONE && !isBaby() && isAlive()) {
            if (!level().isClientSide) {
                setTradingPlayer(player);
                openTradingScreen(player, getDisplayName(), getRole().ordinal());
            }
            return InteractionResult.sidedSuccess(level().isClientSide);
        }
        return super.mobInteract(player, hand);
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

    @Override
    protected void dropCustomDeathLoot(DamageSource source, int looting, boolean recentlyHit) {
        super.dropCustomDeathLoot(source, looting, recentlyHit);
        if (random.nextInt(6) == 0) spawnAtLocation(Items.BONE);
    }

    @Override
    public boolean canFreeze() { return false; }

    void clearLoadout() {
        combatWeapon = ItemStack.EMPTY;
        rangedWeapon = ItemStack.EMPTY;
        idleItem = ItemStack.EMPTY;
        for (EquipmentSlot slot : EquipmentSlot.values()) setItemSlot(slot, ItemStack.EMPTY);
    }

    void setCombatWeapon(ItemStack stack) { combatWeapon = stack; }
    void setRangedWeapon(ItemStack stack) { rangedWeapon = stack; }
    void setWeapons(ItemStack combat, ItemStack idle) {
        combatWeapon = combat;
        idleItem = idle;
    }

    void updateHeldItem() {
        ItemStack held;
        if (getTarget() != null && usesRangedCombat()) {
            held = rangedWeapon;
        } else if (getTarget() != null) {
            held = combatWeapon;
        } else {
            held = idleItem;
        }
        setItemSlot(EquipmentSlot.MAINHAND, held.isEmpty() ? ItemStack.EMPTY : held.copy());
        for (EquipmentSlot slot : EquipmentSlot.values()) setDropChance(slot, 0.0F);
    }

    private void applyRoleAttributes() {
        double health = 20.0D;
        if (getAttribute(Attributes.MAX_HEALTH) != null) {
            getAttribute(Attributes.MAX_HEALTH).setBaseValue(health);
            setHealth((float)health);
        }
        if (getAttribute(Attributes.ARMOR) != null) {
            getAttribute(Attributes.ARMOR).setBaseValue(getRole().legendary() ? 15.0D : 0.0D);
        }
        if (getAttribute(Attributes.ATTACK_DAMAGE) != null) {
            getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(3.0D);
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putString("IbbenRole", getRole().id());
        tag.putBoolean("Female", isFemale());
        tag.putBoolean("IbbenChild", isBaby());
        tag.putInt("IbbenSkin", getSkinIndex());
        tag.putString("PopulationKey", getPopulationKey());
        tag.put("CombatWeapon", combatWeapon.save(new CompoundTag()));
        tag.put("RangedWeapon", rangedWeapon.save(new CompoundTag()));
        tag.put("IdleItem", idleItem.save(new CompoundTag()));
        if (offers != null) tag.put("Offers", offers.createTag());
        tag.putInt("TradeXp", villagerXp);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        entityData.set(DATA_ROLE, IbbenNpcRole.byId(tag.getString("IbbenRole")).id());
        entityData.set(DATA_FEMALE, tag.getBoolean("Female"));
        entityData.set(DATA_CHILD, tag.getBoolean("IbbenChild"));
        entityData.set(DATA_SKIN, tag.getInt("IbbenSkin"));
        entityData.set(DATA_POPULATION_KEY, tag.getString("PopulationKey"));
        if (tag.contains("CombatWeapon")) combatWeapon = ItemStack.of(tag.getCompound("CombatWeapon"));
        if (tag.contains("RangedWeapon")) rangedWeapon = ItemStack.of(tag.getCompound("RangedWeapon"));
        if (tag.contains("IdleItem")) idleItem = ItemStack.of(tag.getCompound("IdleItem"));
        if (tag.contains("Offers")) offers = new MerchantOffers(tag.getCompound("Offers"));
        villagerXp = tag.getInt("TradeXp");
        refreshDimensions();
        applyRoleAttributes();
        updateHeldItem();
    }

    @Override public void setTradingPlayer(@Nullable Player player) { tradingPlayer = player; }
    @Override @Nullable public Player getTradingPlayer() { return tradingPlayer; }
    @Override public MerchantOffers getOffers() {
        if (offers == null) offers = GOTIbbenNpcLoadouts.createOffers(getRole());
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
