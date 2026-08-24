package got.calendar.network;

import got.calendar.client.GOTClientCalendarState;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record S2CCalendarSyncPacket(long worldTime, int calendarDay) {
    public static void encode(S2CCalendarSyncPacket packet, FriendlyByteBuf buf) {
        buf.writeLong(packet.worldTime);
        buf.writeVarInt(packet.calendarDay);
    }

    public static S2CCalendarSyncPacket decode(FriendlyByteBuf buf) {
        return new S2CCalendarSyncPacket(buf.readLong(), buf.readVarInt());
    }

    public static void handle(S2CCalendarSyncPacket packet, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> GOTClientCalendarState.accept(packet.worldTime, packet.calendarDay));
        context.setPacketHandled(true);
    }
}
