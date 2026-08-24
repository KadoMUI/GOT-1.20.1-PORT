package got.network;

import got.client.quest.GOTClientQuestState;
import got.quest.GOTQuestView;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;

public record S2CQuestDataPacket(List<GOTQuestView> active, List<GOTQuestView> archive,
                                 @Nullable UUID tracked) {
    public S2CQuestDataPacket {
        active = List.copyOf(active);
        archive = List.copyOf(archive);
    }

    public static void encode(S2CQuestDataPacket message, FriendlyByteBuf buffer) {
        writeViews(buffer, message.active);
        writeViews(buffer, message.archive);
        buffer.writeBoolean(message.tracked != null);
        if (message.tracked != null) buffer.writeUUID(message.tracked);
    }

    public static S2CQuestDataPacket decode(FriendlyByteBuf buffer) {
        List<GOTQuestView> active = readViews(buffer);
        List<GOTQuestView> archive = readViews(buffer);
        UUID tracked = buffer.readBoolean() ? buffer.readUUID() : null;
        return new S2CQuestDataPacket(active, archive, tracked);
    }

    public static void handle(S2CQuestDataPacket message,
                              Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT,
                () -> () -> GOTClientQuestState.accept(message)));
        context.setPacketHandled(true);
    }

    private static void writeViews(FriendlyByteBuf buffer, List<GOTQuestView> views) {
        buffer.writeVarInt(views.size());
        for (GOTQuestView view : views) GOTQuestView.encode(view, buffer);
    }

    private static List<GOTQuestView> readViews(FriendlyByteBuf buffer) {
        int size = buffer.readVarInt();
        List<GOTQuestView> views = new ArrayList<>(size);
        for (int index = 0; index < size; index++) views.add(GOTQuestView.decode(buffer));
        return views;
    }
}
