package got.quest;

import got.faction.GOTFaction;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntArrayTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

import java.util.Arrays;
import java.util.UUID;

/** Mutable progress for one accepted quest. */
public final class GOTQuestInstance {
    private final UUID instanceId;
    private final ResourceLocation definitionId;
    private final UUID giverId;
    private final String giverName;
    private final GOTFaction giverFaction;
    private final ResourceLocation giverDimension;
    private final BlockPos giverPosition;
    private final long acceptedGameTime;
    private int[] progress;
    private GOTQuestState state;
    private long endedGameTime;

    public GOTQuestInstance(UUID instanceId, ResourceLocation definitionId, UUID giverId,
                            String giverName, GOTFaction giverFaction,
                            ResourceLocation giverDimension, BlockPos giverPosition,
                            long acceptedGameTime, int[] progress, GOTQuestState state,
                            long endedGameTime) {
        this.instanceId = instanceId;
        this.definitionId = definitionId;
        this.giverId = giverId;
        this.giverName = giverName;
        this.giverFaction = giverFaction;
        this.giverDimension = giverDimension;
        this.giverPosition = giverPosition.immutable();
        this.acceptedGameTime = acceptedGameTime;
        this.progress = progress.clone();
        this.state = state;
        this.endedGameTime = endedGameTime;
    }

    public static GOTQuestInstance create(GOTQuestDefinition definition, UUID giverId,
                                          String giverName, GOTFaction giverFaction,
                                          ResourceKey<Level> dimension, BlockPos position,
                                          long gameTime) {
        return new GOTQuestInstance(UUID.randomUUID(), definition.id(), giverId, giverName,
                giverFaction, dimension.location(), position, gameTime,
                new int[definition.objectives().size()], GOTQuestState.ACTIVE, 0L);
    }

    public UUID instanceId() { return instanceId; }
    public ResourceLocation definitionId() { return definitionId; }
    public UUID giverId() { return giverId; }
    public String giverName() { return giverName; }
    public GOTFaction giverFaction() { return giverFaction; }
    public ResourceLocation giverDimension() { return giverDimension; }
    public BlockPos giverPosition() { return giverPosition; }
    public long acceptedGameTime() { return acceptedGameTime; }
    public GOTQuestState state() { return state; }
    public long endedGameTime() { return endedGameTime; }

    public int progress(int index) {
        return index >= 0 && index < progress.length ? progress[index] : 0;
    }

    public int[] progressCopy() { return progress.clone(); }

    public void ensureObjectiveCount(int count) {
        if (progress.length != count) progress = Arrays.copyOf(progress, Math.max(0, count));
    }

    public boolean addProgress(int index, int amount, int target) {
        if (!state.isActive() || index < 0 || index >= progress.length || amount <= 0) return false;
        int before = progress[index];
        progress[index] = Math.min(Math.max(1, target), before + amount);
        return progress[index] != before;
    }

    public void setState(GOTQuestState state, long gameTime) {
        this.state = state;
        if (!state.isActive()) this.endedGameTime = gameTime;
    }

    public CompoundTag save() {
        CompoundTag tag = new CompoundTag();
        tag.putUUID("Instance", instanceId);
        tag.putString("Definition", definitionId.toString());
        tag.putUUID("Giver", giverId);
        tag.putString("GiverName", giverName);
        tag.putString("GiverFaction", giverFaction.id());
        tag.putString("GiverDimension", giverDimension.toString());
        tag.putLong("GiverPosition", giverPosition.asLong());
        tag.putLong("Accepted", acceptedGameTime);
        tag.put("Progress", new IntArrayTag(progress));
        tag.putString("State", state.name());
        tag.putLong("Ended", endedGameTime);
        return tag;
    }

    public static GOTQuestInstance load(CompoundTag tag) {
        try {
            UUID instance = tag.getUUID("Instance");
            ResourceLocation definition = ResourceLocation.tryParse(tag.getString("Definition"));
            UUID giver = tag.getUUID("Giver");
            ResourceLocation dimension = ResourceLocation.tryParse(tag.getString("GiverDimension"));
            if (definition == null || dimension == null) return null;
            GOTFaction faction = GOTFaction.byId(tag.getString("GiverFaction")).orElse(GOTFaction.UNALIGNED);
            GOTQuestState state;
            try {
                state = GOTQuestState.valueOf(tag.getString("State"));
            } catch (IllegalArgumentException exception) {
                state = GOTQuestState.ACTIVE;
            }
            return new GOTQuestInstance(instance, definition, giver, tag.getString("GiverName"),
                    faction, dimension, BlockPos.of(tag.getLong("GiverPosition")),
                    tag.getLong("Accepted"), tag.getIntArray("Progress"), state,
                    tag.getLong("Ended"));
        } catch (RuntimeException exception) {
            return null;
        }
    }
}
