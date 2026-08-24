package got.network;

import got.claim.GOTBannerClaimService;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record C2SBannerClaimActionPacket(int entityId, Action action, String text,
                                         int number, float decimal) {
    public enum Action {
        REFRESH,
        SET_MODE,
        SET_SELF_PROTECTION,
        SET_ALIGNMENT,
        SET_DEFAULT_PERMISSIONS,
        ADD_ENTRY,
        REMOVE_ENTRY,
        SET_ENTRY_PERMISSIONS
    }

    public static C2SBannerClaimActionPacket simple(int id, Action action, int value) {
        return new C2SBannerClaimActionPacket(id, action, "", value, 0.0F);
    }

    public static C2SBannerClaimActionPacket alignment(int id, float value) {
        return new C2SBannerClaimActionPacket(id, Action.SET_ALIGNMENT, "", 0, value);
    }

    public static C2SBannerClaimActionPacket entry(int id, Action action, String key, int permissions) {
        return new C2SBannerClaimActionPacket(id, action, key, permissions, 0.0F);
    }

    public static void encode(C2SBannerClaimActionPacket message, FriendlyByteBuf buffer) {
        buffer.writeVarInt(message.entityId);
        buffer.writeEnum(message.action);
        buffer.writeUtf(message.text, 160);
        buffer.writeVarInt(message.number);
        buffer.writeFloat(message.decimal);
    }

    public static C2SBannerClaimActionPacket decode(FriendlyByteBuf buffer) {
        return new C2SBannerClaimActionPacket(buffer.readVarInt(), buffer.readEnum(Action.class),
                buffer.readUtf(160), buffer.readVarInt(), buffer.readFloat());
    }

    public static void handle(C2SBannerClaimActionPacket message,
                              Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        ServerPlayer player = context.getSender();
        context.enqueueWork(() -> {
            if (player != null) GOTBannerClaimService.handle(player, message);
        });
        context.setPacketHandled(true);
    }
}
