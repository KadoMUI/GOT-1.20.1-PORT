package got.network;

import got.faction.GOTFaction;
import got.faction.GOTFactionService;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record C2SFactionMembershipPacket(Action action, GOTFaction faction) {
    public enum Action { JOIN, LEAVE }

    public static C2SFactionMembershipPacket join(GOTFaction faction) {
        return new C2SFactionMembershipPacket(Action.JOIN, faction);
    }

    public static C2SFactionMembershipPacket leave() {
        return new C2SFactionMembershipPacket(Action.LEAVE, GOTFaction.UNALIGNED);
    }

    public static void encode(C2SFactionMembershipPacket message, FriendlyByteBuf buffer) {
        buffer.writeEnum(message.action);
        buffer.writeEnum(message.faction);
    }

    public static C2SFactionMembershipPacket decode(FriendlyByteBuf buffer) {
        return new C2SFactionMembershipPacket(buffer.readEnum(Action.class), buffer.readEnum(GOTFaction.class));
    }

    public static void handle(C2SFactionMembershipPacket message,
                              Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            if (player == null) return;
            if (message.action == Action.JOIN) GOTFactionService.joinFaction(player, message.faction);
            else GOTFactionService.leaveFaction(player, true);
        });
        context.setPacketHandled(true);
    }
}
