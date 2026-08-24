package got.calendar.network;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public final class GOTCalendarNetwork {
    private static final String PROTOCOL = "1";

    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
            ResourceLocation.fromNamespaceAndPath("got", "calendar"),
            () -> PROTOCOL,
            PROTOCOL::equals,
            PROTOCOL::equals
    );

    private static boolean registered;

    private GOTCalendarNetwork() {}

    /**
     * Register calendar packets explicitly during mod construction.
     *
     * This mirrors GOTNetwork.register() and avoids the Forge 47.4.x
     * registerMessage overload/access problem encountered when registration
     * was nested inside FMLCommonSetupEvent#enqueueWork.
     */
    public static void register() {
        if (registered) {
            return;
        }
        registered = true;

        CHANNEL.registerMessage(
                0,
                S2CCalendarSyncPacket.class,
                S2CCalendarSyncPacket::encode,
                S2CCalendarSyncPacket::decode,
                S2CCalendarSyncPacket::handle
        );
    }

    public static void sync(ServerPlayer player, long worldTime, int calendarDay) {
        CHANNEL.send(
                PacketDistributor.PLAYER.with(() -> player),
                new S2CCalendarSyncPacket(worldTime, calendarDay)
        );
    }
}
