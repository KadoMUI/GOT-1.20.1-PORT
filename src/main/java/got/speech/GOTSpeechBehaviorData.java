package got.speech;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;

import javax.annotation.Nullable;
import java.util.UUID;

/**
 * Per-NPC speech behavior state.
 *
 * The original GOTEntityNPC kept an npcTalkTick which had to reach 40 before
 * another player interaction could produce dialogue. Sending speech reset it
 * to zero.
 *
 * Modern storage uses persistent entity NBT so the shared regional NPC classes
 * do not need duplicate fields.
 */
public final class GOTSpeechBehaviorData {
    private static final String ROOT = "GOTSpeechBehavior";
    private static final String TALK_COOLDOWN = "TalkCooldown";
    private static final String PREVIOUS_TARGET = "PreviousTarget";

    public static final int INTERACTION_COOLDOWN_TICKS = 40;

    private GOTSpeechBehaviorData() {}

    private static CompoundTag data(Entity entity) {
        CompoundTag persistent = entity.getPersistentData();
        if (!persistent.contains(ROOT)) {
            persistent.put(ROOT, new CompoundTag());
        }
        return persistent.getCompound(ROOT);
    }

    private static void save(Entity entity, CompoundTag tag) {
        entity.getPersistentData().put(ROOT, tag);
    }

    public static boolean canSpeak(Entity entity) {
        return data(entity).getInt(TALK_COOLDOWN) <= 0;
    }

    public static int cooldown(Entity entity) {
        return Math.max(0, data(entity).getInt(TALK_COOLDOWN));
    }

    /**
     * Equivalent to legacy markNPCSpoken(): another ordinary interaction cannot
     * trigger speech for 40 ticks.
     */
    public static void markSpoken(Entity entity) {
        CompoundTag tag = data(entity);
        tag.putInt(TALK_COOLDOWN, INTERACTION_COOLDOWN_TICKS);
        save(entity, tag);
    }

    public static void tick(Entity entity) {
        CompoundTag tag = data(entity);
        int cooldown = tag.getInt(TALK_COOLDOWN);
        if (cooldown > 0) {
            tag.putInt(TALK_COOLDOWN, cooldown - 1);
            save(entity, tag);
        }
    }

    @Nullable
    public static UUID previousTarget(Entity entity) {
        CompoundTag tag = data(entity);
        return tag.hasUUID(PREVIOUS_TARGET) ? tag.getUUID(PREVIOUS_TARGET) : null;
    }

    public static void setPreviousTarget(Entity entity, @Nullable UUID target) {
        CompoundTag tag = data(entity);
        if (target == null) tag.remove(PREVIOUS_TARGET);
        else tag.putUUID(PREVIOUS_TARGET, target);
        save(entity, tag);
    }
}
