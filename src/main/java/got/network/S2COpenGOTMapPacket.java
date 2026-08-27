package got.network;

import got.client.gui.GOTGuiMap;
import got.client.gui.GOTGuiMenu;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

/** Opens the normal GOT map screen from a server-authoritative world interaction. */
public final class S2COpenGOTMapPacket {
    public static void encode(S2COpenGOTMapPacket packet, FriendlyByteBuf buffer) {}

    public static S2COpenGOTMapPacket decode(FriendlyByteBuf buffer) {
        return new S2COpenGOTMapPacket();
    }

    public static void handle(S2COpenGOTMapPacket packet, Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> Minecraft.getInstance().setScreen(new GOTGuiMap(new GOTGuiMenu())));
        context.setPacketHandled(true);
    }
}
