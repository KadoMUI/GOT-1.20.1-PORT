package got.quest;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/** Compact network/client representation of a quest definition and its progress. */
public record GOTQuestView(
        @Nullable UUID instanceId,
        ResourceLocation definitionId,
        String titleKey,
        String descriptionKey,
        String offerKey,
        String progressKey,
        String completeKey,
        ResourceLocation icon,
        int color,
        String giverName,
        GOTQuestState state,
        boolean tracked,
        List<ObjectiveView> objectives
) {
    public GOTQuestView {
        objectives = List.copyOf(objectives);
    }

    public float completion() {
        int done = 0;
        int total = 0;
        for (ObjectiveView objective : objectives) {
            done += Math.min(objective.progress(), objective.target());
            total += objective.target();
        }
        return total <= 0 ? 0.0F : Math.min(1.0F, (float)done / total);
    }

    public ObjectiveView currentObjective() {
        for (ObjectiveView objective : objectives) {
            if (objective.progress() < objective.target()) return objective;
        }
        return objectives.isEmpty() ? new ObjectiveView("", 0, 1) : objectives.get(objectives.size() - 1);
    }

    public static void encode(GOTQuestView view, FriendlyByteBuf buffer) {
        buffer.writeBoolean(view.instanceId != null);
        if (view.instanceId != null) buffer.writeUUID(view.instanceId);
        buffer.writeResourceLocation(view.definitionId);
        buffer.writeUtf(view.titleKey);
        buffer.writeUtf(view.descriptionKey);
        buffer.writeUtf(view.offerKey);
        buffer.writeUtf(view.progressKey);
        buffer.writeUtf(view.completeKey);
        buffer.writeResourceLocation(view.icon);
        buffer.writeInt(view.color);
        buffer.writeUtf(view.giverName);
        buffer.writeEnum(view.state);
        buffer.writeBoolean(view.tracked);
        buffer.writeVarInt(view.objectives.size());
        for (ObjectiveView objective : view.objectives) {
            buffer.writeUtf(objective.labelKey());
            buffer.writeVarInt(objective.progress());
            buffer.writeVarInt(objective.target());
        }
    }

    public static GOTQuestView decode(FriendlyByteBuf buffer) {
        UUID instance = buffer.readBoolean() ? buffer.readUUID() : null;
        ResourceLocation definition = buffer.readResourceLocation();
        String title = buffer.readUtf();
        String description = buffer.readUtf();
        String offer = buffer.readUtf();
        String progress = buffer.readUtf();
        String complete = buffer.readUtf();
        ResourceLocation icon = buffer.readResourceLocation();
        int color = buffer.readInt();
        String giver = buffer.readUtf();
        GOTQuestState state = buffer.readEnum(GOTQuestState.class);
        boolean tracked = buffer.readBoolean();
        int size = buffer.readVarInt();
        List<ObjectiveView> objectives = new ArrayList<>(size);
        for (int index = 0; index < size; index++) {
            objectives.add(new ObjectiveView(buffer.readUtf(), buffer.readVarInt(), buffer.readVarInt()));
        }
        return new GOTQuestView(instance, definition, title, description, offer, progress,
                complete, icon, color, giver, state, tracked, objectives);
    }

    public record ObjectiveView(String labelKey, int progress, int target) {}
}
