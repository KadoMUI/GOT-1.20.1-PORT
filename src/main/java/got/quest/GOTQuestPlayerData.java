package got.quest;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/** Persistent per-player quest journal stored with the same death-safe pattern as factions. */
public final class GOTQuestPlayerData {
    private static final String PERSISTED_KEY = "got_quest_data";
    private static final String ACTIVE = "Active";
    private static final String ARCHIVE = "Archive";
    private static final int MAX_ARCHIVED_QUESTS = 100;

    private final Player player;
    private final CompoundTag root;

    private GOTQuestPlayerData(Player player) {
        this.player = player;
        CompoundTag persisted = player.getPersistentData().getCompound(Player.PERSISTED_NBT_TAG);
        this.root = persisted.getCompound(PERSISTED_KEY).copy();
    }

    public static GOTQuestPlayerData get(Player player) {
        return new GOTQuestPlayerData(player);
    }

    public List<GOTQuestInstance> active() {
        return readList(ACTIVE);
    }

    public List<GOTQuestInstance> archive() {
        return readList(ARCHIVE);
    }

    public Optional<GOTQuestInstance> findActive(UUID instanceId) {
        return active().stream().filter(quest -> quest.instanceId().equals(instanceId)).findFirst();
    }

    public Optional<GOTQuestInstance> findActiveByGiver(UUID giverId) {
        return active().stream().filter(quest -> quest.giverId().equals(giverId)).findFirst();
    }

    public void add(GOTQuestInstance instance) {
        List<GOTQuestInstance> active = active();
        active.add(instance);
        writeList(ACTIVE, active);
        save();
    }

    public void update(GOTQuestInstance instance) {
        List<GOTQuestInstance> active = active();
        for (int index = 0; index < active.size(); index++) {
            if (active.get(index).instanceId().equals(instance.instanceId())) {
                active.set(index, instance);
                writeList(ACTIVE, active);
                save();
                return;
            }
        }
    }

    public void archive(GOTQuestInstance instance) {
        List<GOTQuestInstance> active = active();
        active.removeIf(quest -> quest.instanceId().equals(instance.instanceId()));
        writeList(ACTIVE, active);

        List<GOTQuestInstance> archive = archive();
        archive.add(0, instance);
        if (archive.size() > MAX_ARCHIVED_QUESTS) {
            archive = new ArrayList<>(archive.subList(0, MAX_ARCHIVED_QUESTS));
        }
        writeList(ARCHIVE, archive);
        if (instance.instanceId().equals(tracked())) root.remove("Tracked");
        save();
    }

    public int completedCount(ResourceLocation definitionId) {
        int count = 0;
        for (GOTQuestInstance quest : archive()) {
            if (quest.definitionId().equals(definitionId) && quest.state() == GOTQuestState.COMPLETED) count++;
        }
        return count;
    }

    public long lastCompletedTime(ResourceLocation definitionId) {
        long latest = Long.MIN_VALUE;
        for (GOTQuestInstance quest : archive()) {
            if (quest.definitionId().equals(definitionId) && quest.state() == GOTQuestState.COMPLETED) {
                latest = Math.max(latest, quest.endedGameTime());
            }
        }
        return latest;
    }

    @Nullable
    public UUID tracked() {
        if (!root.hasUUID("Tracked")) return null;
        try {
            return root.getUUID("Tracked");
        } catch (RuntimeException exception) {
            return null;
        }
    }

    public void setTracked(@Nullable UUID instanceId) {
        if (instanceId == null) root.remove("Tracked");
        else root.putUUID("Tracked", instanceId);
        save();
    }

    public static void copyPersisted(Player original, Player clone) {
        CompoundTag originalPersisted = original.getPersistentData().getCompound(Player.PERSISTED_NBT_TAG);
        CompoundTag clonePersisted = clone.getPersistentData().getCompound(Player.PERSISTED_NBT_TAG);
        if (originalPersisted.contains(PERSISTED_KEY)) {
            clonePersisted.put(PERSISTED_KEY, originalPersisted.getCompound(PERSISTED_KEY).copy());
            clone.getPersistentData().put(Player.PERSISTED_NBT_TAG, clonePersisted);
        }
    }

    private List<GOTQuestInstance> readList(String key) {
        List<GOTQuestInstance> result = new ArrayList<>();
        ListTag list = root.getList(key, Tag.TAG_COMPOUND);
        for (int index = 0; index < list.size(); index++) {
            GOTQuestInstance instance = GOTQuestInstance.load(list.getCompound(index));
            if (instance != null) result.add(instance);
        }
        return result;
    }

    private void writeList(String key, List<GOTQuestInstance> instances) {
        ListTag list = new ListTag();
        for (GOTQuestInstance instance : instances) list.add(instance.save());
        root.put(key, list);
    }

    private void save() {
        CompoundTag persistentData = player.getPersistentData();
        CompoundTag persisted = persistentData.getCompound(Player.PERSISTED_NBT_TAG);
        persisted.put(PERSISTED_KEY, root.copy());
        persistentData.put(Player.PERSISTED_NBT_TAG, persisted);
    }
}
