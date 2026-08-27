package got.network;

import got.client.lore.GOTLoreView;
import got.lore.GOTLorePlayerData;
import got.lore.GOTLoreRegistry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.function.Supplier;

public record C2SRequestLoreDataPacket() {
    public static void encode(C2SRequestLoreDataPacket p, FriendlyByteBuf b) {}
    public static C2SRequestLoreDataPacket decode(FriendlyByteBuf b) { return new C2SRequestLoreDataPacket(); }
    public static void handle(C2SRequestLoreDataPacket p, Supplier<NetworkEvent.Context> ctx) {
        ServerPlayer player = ctx.get().getSender();
        ctx.get().enqueueWork(() -> {
            if (player == null) return;
            var discovered = GOTLorePlayerData.snapshot(player);
            var views = new ArrayList<GOTLoreView>();
            for (String id : discovered) GOTLoreRegistry.get(id).ifPresent(e -> views.add(new GOTLoreView(
                    e.id(), e.category(), e.title(), e.author(), e.types(), e.text(), e.rewardable(), true)));
            views.sort(Comparator.comparing(GOTLoreView::title, String.CASE_INSENSITIVE_ORDER));
            GOTNetwork.CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), new S2CLoreDataPacket(views));
        });
        ctx.get().setPacketHandled(true);
    }
}
