package got;

import got.claim.GOTBannerClaim;
import got.claim.GOTBannerClaimConfig;
import got.claim.GOTBannerProtection;
import got.faction.GOTFaction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.UUID;

/** Shared legacy-ID, ownership, persistence, breaking, and item-drop behavior. */
public abstract class GOTAbstractBannerEntity extends Entity {
    private static final EntityDataAccessor<Integer> BANNER_TYPE =
            SynchedEntityData.defineId(GOTAbstractBannerEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> CLAIM_RANGE =
            SynchedEntityData.defineId(GOTAbstractBannerEntity.class, EntityDataSerializers.INT);
    @Nullable private UUID owner;
    private String ownerName = "?";
    private final GOTBannerClaim claim = new GOTBannerClaim();

    protected GOTAbstractBannerEntity(EntityType<?> type, Level level) {
        super(type, level);
        noPhysics = true;
        setNoGravity(true);
    }

    @Override
    protected void defineSynchedData() {
        entityData.define(BANNER_TYPE, GOTBannerType.DEFAULT.legacyId());
        entityData.define(CLAIM_RANGE, 0);
    }

    public GOTBannerType getBannerType() {
        return GOTBannerType.byLegacyId(entityData.get(BANNER_TYPE));
    }

    public void setBannerType(GOTBannerType type) {
        entityData.set(BANNER_TYPE, type.legacyId());
    }

    @Nullable
    public UUID getOwnerUUID() {
        return owner;
    }

    public void setOwner(@Nullable UUID owner) {
        this.owner = owner;
    }

    public void setOwner(@Nullable Player player) {
        owner = player == null ? null : player.getUUID();
        ownerName = player == null ? "?" : player.getGameProfile().getName();
    }

    public String getOwnerName() {
        return ownerName == null || ownerName.isBlank() ? "?" : ownerName;
    }

    public GOTBannerClaim getClaim() {
        return claim;
    }

    public GOTFaction getClaimFaction() {
        return getBannerType().faction();
    }

    public int getClaimRange() {
        if (level().isClientSide) return entityData.get(CLAIM_RANGE);
        if (!(this instanceof GOTStandingBannerEntity)) return 0;
        if (!claim.structureProtection() && !GOTBannerClaimConfig.ENABLED.get()) return 0;
        if (claim.customRange() > 0) return claim.customRange();
        return GOTBannerProtection.rangeForSupport(level().getBlockState(blockPosition().below()));
    }

    public boolean isClaimActive() {
        return getClaimRange() > 0;
    }

    public net.minecraft.world.phys.AABB getClaimBounds() {
        return GOTBannerProtection.bounds(blockPosition(), getClaimRange());
    }

    public boolean canPlayerEditClaim(Player player) {
        if (owner != null && owner.equals(player.getUUID())) return true;
        if (owner != null && player instanceof net.minecraft.server.level.ServerPlayer sp && sp.getServer() != null
                && got.pact.GOTPactService.samePact(sp.getServer(), owner, player.getUUID())) return true;
        return !claim.structureProtection()
                && player.getAbilities().instabuild && player.hasPermissions(2);
    }

    public void claimChanged() {
        if (!level().isClientSide) {
            int range = getClaimRange();
            if (range > 0) claim.markConfigured();
            entityData.set(CLAIM_RANGE, range);
        }
    }

    @Nullable
    public CompoundTag getProtectionData() {
        return !claim.configured() && !isClaimActive()
                ? null : claim.save(owner, getOwnerName());
    }

    public void setProtectionData(@Nullable CompoundTag data) {
        claim.load(data);
        if (data != null) {
            if (data.hasUUID("Owner")) owner = data.getUUID("Owner");
            if (data.contains("OwnerName", Tag.TAG_STRING)) ownerName = data.getString("OwnerName");
        }
        claimChanged();
    }

    public ItemStack createDropStack() {
        ItemStack stack = GOTBannerItem.createStack(getBannerType());
        // Legacy structure claims were administrator/worldgen fixtures and did
        // not transfer their protected state to a dropped player item.
        GOTBannerItem.setProtectionData(stack,
                claim.structureProtection() ? null : getProtectionData());
        return stack;
    }

    @Override
    public boolean isPickable() {
        return true;
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (isInvulnerableTo(source)) return false;
        if (level().isClientSide || isRemoved()) return true;
        Entity attacker = source.getEntity();
        if (isClaimActive() && !(attacker instanceof Player)) return false;
        if (attacker instanceof net.minecraft.server.level.ServerPlayer player
                && !GOTBannerProtection.mayDamageBanner(this, player)) return false;
        boolean creative = attacker instanceof Player player && player.getAbilities().instabuild;
        level().playSound(null, blockPosition(), SoundEvents.WOOD_BREAK,
                SoundSource.BLOCKS, 1.0F, 0.9F + random.nextFloat() * 0.2F);
        if (!creative) spawnAtLocation(createDropStack());
        discard();
        return true;
    }

    @Override
    public InteractionResult interact(Player player, InteractionHand hand) {
        if (hand != InteractionHand.MAIN_HAND) return InteractionResult.PASS;
        if (level().isClientSide) return isClaimActive() ? InteractionResult.SUCCESS : InteractionResult.PASS;
        if (!isClaimActive()) return InteractionResult.PASS;
        if (!(player instanceof net.minecraft.server.level.ServerPlayer serverPlayer)) return InteractionResult.PASS;
        if (!canPlayerEditClaim(player)) {
            GOTBannerProtection.warn(serverPlayer,
                    new GOTBannerProtection.Denial(getUUID(), getOwnerName(), claim.structureProtection()));
            return InteractionResult.CONSUME;
        }
        got.claim.GOTBannerClaimService.open(serverPlayer, this);
        return InteractionResult.CONSUME;
    }

    @Override
    public void tick() {
        super.tick();
        if (!level().isClientSide && tickCount % 20 == 0) claimChanged();
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        setBannerType(GOTBannerType.byLegacyId(tag.getInt("BannerType")));
        owner = tag.hasUUID("Owner") ? tag.getUUID("Owner") : null;
        ownerName = tag.contains("OwnerName", Tag.TAG_STRING) ? tag.getString("OwnerName") : "?";
        setProtectionData(tag.contains(GOTBannerItem.PROTECTION_TAG, Tag.TAG_COMPOUND)
                ? tag.getCompound(GOTBannerItem.PROTECTION_TAG).copy() : null);
        readBannerData(tag);
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        tag.putInt("BannerType", getBannerType().legacyId());
        if (owner != null) tag.putUUID("Owner", owner);
        if (!getOwnerName().equals("?")) tag.putString("OwnerName", getOwnerName());
        CompoundTag protectionData = getProtectionData();
        if (protectionData != null) tag.put(GOTBannerItem.PROTECTION_TAG, protectionData);
        addBannerData(tag);
    }

    protected void readBannerData(CompoundTag tag) {}

    protected void addBannerData(CompoundTag tag) {}

    public ItemStack getPickResult() {
        return createDropStack();
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
