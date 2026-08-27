package got.mount;

import got.npc.GOTFactionNpc;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.horse.Horse;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

/** 1.20.1 replacement for the legacy GOTNPCMount contract. */
public class GOTMountEntity extends Horse {
    private boolean belongsToNpc;
    private UUID npcRiderUuid;
    private int riderlessTicks;
    private int mountAttackCooldown;

    public GOTMountEntity(EntityType<? extends Horse> type, Level level) { super(type, level); }

    public static AttributeSupplier.Builder createGOTMountAttributes() {
        return Horse.createBaseHorseAttributes().add(Attributes.ATTACK_DAMAGE, 0.0D);
    }

    public boolean belongsToNpc() { return belongsToNpc; }
    public void setBelongsToNpc(boolean value) { belongsToNpc = value; }
    public @Nullable UUID getNpcRiderUuid() { return npcRiderUuid; }

    /** Public bridge for the vanilla horse armor equipment slot (401). */
    public void equipHorseArmor(net.minecraft.world.item.ItemStack stack) {
        getSlot(401).set(stack == null ? net.minecraft.world.item.ItemStack.EMPTY : stack);
    }

    public void prepareAsNpcMount() {
        setBelongsToNpc(true);
        setTamed(true);
        equipSaddle(null);
        setPersistenceRequired();
    }

    /** Called by mounted-NPC creation code after both entities exist. */
    public boolean mountNpc(Mob rider) {
        prepareAsNpcMount();
        npcRiderUuid = rider.getUUID();
        setYRot(rider.getYRot());
        setXRot(rider.getXRot());
        boolean mounted = rider.startRiding(this, true);
        if (mounted) riderlessTicks = 0;
        return mounted;
    }

    protected double clampHealth(double value) { return Mth.clamp(value, 12.0D, 48.0D); }
    protected double clampJump(double value) { return Mth.clamp(value, 0.30D, 1.00D); }
    protected double clampSpeed(double value) { return Mth.clamp(value, 0.08D, 0.45D); }

    /** Species hook matching legacy onGOTHorseSpawn(). */
    protected void applyLegacySpeciesStats() {
        getAttribute(Attributes.MAX_HEALTH).setBaseValue(clampHealth(getAttributeValue(Attributes.MAX_HEALTH)));
        getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(clampSpeed(getAttributeValue(Attributes.MOVEMENT_SPEED)));
        getAttribute(Attributes.JUMP_STRENGTH).setBaseValue(clampJump(getAttributeValue(Attributes.JUMP_STRENGTH)));
    }

    /** Heavy GOT mounts override this; ordinary horse/zebra/camel do not attack independently. */
    protected boolean hasMountAttack() { return false; }
    protected double mountAttackReachSqr() { return 7.0D; }
    protected int mountAttackInterval() { return 20; }

    @Override
    public void aiStep() {
        super.aiStep();
        if (level().isClientSide) return;
        if (mountAttackCooldown > 0) --mountAttackCooldown;

        Mob rider = getNpcRider();
        if (rider != null) {
            riderlessTicks = 0;
            npcRiderUuid = rider.getUUID();
            steerForNpc(rider);
            attackForNpc(rider);
        } else if (belongsToNpc) {
            ++riderlessTicks;
            // NPC mounts are persistent, but do not keep stale rider identity forever after death/dismissal.
            if (riderlessTicks > 200) npcRiderUuid = null;
        }
    }

    private @Nullable Mob getNpcRider() {
        Entity passenger = getFirstPassenger();
        if (passenger instanceof Mob mob && (npcRiderUuid == null || npcRiderUuid.equals(mob.getUUID()))) return mob;
        return null;
    }

    /**
     * Vanilla horses already provide player saddle controls.  Legacy NPC mounts instead inherit their
     * rider's combat intent: face and advance on the rider target, or mirror the rider's facing while idle.
     */
    private void steerForNpc(Mob rider) {
        // Hired command state must get first refusal: HALT must stop even a
        // mount that still has a combat target from the previous tick.
        if (got.npc.hiring.GOTHiredMountController.tick(this, rider)) return;

        LivingEntity target = rider.getTarget();
        float desiredYaw = rider.getYRot();
        if (target != null && target.isAlive()) {
            double dx = target.getX() - getX();
            double dz = target.getZ() - getZ();
            desiredYaw = (float)(Mth.atan2(dz, dx) * (180F / Math.PI)) - 90F;
            double stop = rider.getMainHandItem().getItem() instanceof net.minecraft.world.item.BowItem ? 100.0D : 4.0D;
            if (distanceToSqr(target) > stop) {
                getNavigation().moveTo(target, mountedMoveSpeed());
            } else {
                getNavigation().stop();
            }
        } else if (rider instanceof got.npc.GOTDothrakiNpcEntity dothraki) {
            got.npc.GOTDothrakiNpcEntity leader = dothraki.getKhalasarLeader();
            if (leader != null && leader.isAlive() && distanceToSqr(leader) > 36.0D) {
                double dx = leader.getX() - getX();
                double dz = leader.getZ() - getZ();
                desiredYaw = (float)(Mth.atan2(dz, dx) * (180F / Math.PI)) - 90F;
                getNavigation().moveTo(leader, mountedMoveSpeed());
            } else {
                getNavigation().stop();
            }
        } else {
            getNavigation().stop();
        }
        setYRot(Mth.rotateIfNecessary(getYRot(), desiredYaw, 12.0F));
        yBodyRot = getYRot();
        yHeadRot = getYRot();
        rider.setYRot(getYRot());
        rider.yBodyRot = getYRot();
    }

    protected double mountedMoveSpeed() { return 1.15D; }

    private void attackForNpc(Mob rider) {
        if (!hasMountAttack() || mountAttackCooldown > 0) return;
        LivingEntity target = rider.getTarget();
        if (target == null || !target.isAlive() || distanceToSqr(target) > mountAttackReachSqr()) return;
        if (!canNpcMountHurt(rider, target)) return;
        if (doHurtTarget(target)) mountAttackCooldown = mountAttackInterval();
    }

    private boolean canNpcMountHurt(Mob rider, LivingEntity target) {
        if (target == rider || target == this) return false;
        if (rider instanceof GOTFactionNpc owner && target instanceof GOTFactionNpc other) {
            return owner.getFaction().isBadRelation(other.getFaction());
        }
        return !rider.isAlliedTo(target);
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType reason,
                                                   @Nullable SpawnGroupData data, @Nullable CompoundTag tag) {
        SpawnGroupData result = super.finalizeSpawn(level, difficulty, reason, data, tag);
        applyLegacySpeciesStats();
        setHealth(getMaxHealth());
        return result;
    }

    @Override public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putBoolean("GOTBelongsToNPC", belongsToNpc);
        if (npcRiderUuid != null) tag.putUUID("GOTNPCRider", npcRiderUuid);
    }
    @Override public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        belongsToNpc = tag.getBoolean("GOTBelongsToNPC");
        npcRiderUuid = tag.hasUUID("GOTNPCRider") ? tag.getUUID("GOTNPCRider") : null;
    }
}
