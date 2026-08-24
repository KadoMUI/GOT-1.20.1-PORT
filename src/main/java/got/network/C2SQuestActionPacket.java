package got.network;

import got.quest.GOTQuestService;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public record C2SQuestActionPacket(Action action, ResourceLocation questId, UUID referenceId) {
    public enum Action { ACCEPT, DECLINE, TRACK, ABANDON }

    public static C2SQuestActionPacket accept(ResourceLocation questId, UUID giverId) {
        return new C2SQuestActionPacket(Action.ACCEPT, questId, giverId);
    }

    public static C2SQuestActionPacket decline(ResourceLocation questId, UUID giverId) {
        return new C2SQuestActionPacket(Action.DECLINE, questId, giverId);
    }

    public static C2SQuestActionPacket track(UUID instanceId) {
        return new C2SQuestActionPacket(Action.TRACK,
                ResourceLocation.fromNamespaceAndPath("got", "none"), instanceId);
    }

    public static C2SQuestActionPacket abandon(UUID instanceId) {
        return new C2SQuestActionPacket(Action.ABANDON,
                ResourceLocation.fromNamespaceAndPath("got", "none"), instanceId);
    }

    public static void encode(C2SQuestActionPacket message, FriendlyByteBuf buffer) {
        buffer.writeEnum(message.action);
        buffer.writeResourceLocation(message.questId);
        buffer.writeUUID(message.referenceId);
    }

    public static C2SQuestActionPacket decode(FriendlyByteBuf buffer) {
        return new C2SQuestActionPacket(buffer.readEnum(Action.class),
                buffer.readResourceLocation(), buffer.readUUID());
    }

    public static void handle(C2SQuestActionPacket message,
                              Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        ServerPlayer player = context.getSender();
        context.enqueueWork(() -> {
            if (player == null) return;
            switch (message.action) {
                case ACCEPT -> GOTQuestService.acceptOffer(player, message.questId, message.referenceId);
                case DECLINE -> GOTQuestService.declineOffer(player, message.questId, message.referenceId);
                case TRACK -> GOTQuestService.track(player, message.referenceId);
                case ABANDON -> GOTQuestService.abandon(player, message.referenceId);
            }
        });
        context.setPacketHandled(true);
    }
}
