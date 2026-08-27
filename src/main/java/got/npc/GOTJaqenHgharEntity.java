package got.npc;

import got.GOTEquipment;
import got.quest.GOTJaqenQuestSequence;
import got.speech.GOTSpeechService;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;

import javax.annotation.Nullable;

/** Legendary Jaqen H'Ghar NPC and owner of the restored welcome/tutorial miniquest. */
public final class GOTJaqenHgharEntity extends GOTBraavosNpcEntity {
    public GOTJaqenHgharEntity(EntityType<? extends GOTJaqenHgharEntity> type, Level level) {
        super(type, level);
        setPersistenceRequired();
    }

    @Override public String getFactionId() { return "lorath"; }
    @Override public String getQuestRoleId() { return "jaqen_hghar"; }
    @Override public int getAlignmentBonus() { return 300; }
    @Override public boolean canOfferQuests() { return false; }
    @Override public boolean isCivilian() { return false; }
    @Override public boolean isActiveCombatant() { return true; }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty,
                                        MobSpawnType reason, @Nullable SpawnGroupData data,
                                        @Nullable net.minecraft.nbt.CompoundTag tag) {
        SpawnGroupData result = super.finalizeSpawn(level, difficulty, reason, data, tag);
        prepareForSpawn(BraavosNpcRole.JAQEN_HGHAR, false, false, blockPosition(), 20, "jaqen_hghar");
        setItemInHand(net.minecraft.world.InteractionHand.MAIN_HAND, GOTEquipment.ALLOY_STEEL_SWORD.get().getDefaultInstance());
        setCustomName(Component.literal("Jaqen H'Ghar"));
        setCustomNameVisible(true);
        return result;
    }

    public void announceArrival(@Nullable ServerPlayer focus) {
        if (!(level() instanceof ServerLevel server)) return;
        for (ServerPlayer player : server.players()) {
            if ((focus != null && player == focus) || distanceToSqr(player) < 4096.0D) {
                GOTSpeechService.speak(this, player, "legendary/jaqen_arrive");
            }
        }
        fx();
    }

    public void depart() {
        if (!(level() instanceof ServerLevel server)) return;
        for (ServerPlayer player : server.players()) if (distanceToSqr(player) < 4096.0D) {
            GOTSpeechService.speak(this, player, "legendary/jaqen_depart");
        }
        fx();
        discard();
    }

    private void fx() {
        level().playSound(null, blockPosition(), SoundEvents.CHORUS_FRUIT_TELEPORT, SoundSource.NEUTRAL, 1.4F, 0.9F + random.nextFloat() * 0.2F);
        if (level() instanceof ServerLevel server) {
            server.sendParticles(ParticleTypes.POOF, getX(), getY() + 1.0D, getZ(), 16, 0.35D, 0.7D, 0.35D, 0.04D);
        }
    }
}
