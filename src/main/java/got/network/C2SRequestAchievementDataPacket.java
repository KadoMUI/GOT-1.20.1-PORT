package got.network;

import got.achievement.GOTAchievementHooks;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;

import java.util.function.Supplier;

public record C2SRequestAchievementDataPacket() {
    public static void encode(C2SRequestAchievementDataPacket p, FriendlyByteBuf b) {}
    public static C2SRequestAchievementDataPacket decode(FriendlyByteBuf b) { return new C2SRequestAchievementDataPacket(); }
    public static void handle(C2SRequestAchievementDataPacket p, Supplier<NetworkEvent.Context> ctx) {
        ServerPlayer player=ctx.get().getSender();
        if (player != null) ctx.get().enqueueWork(() ->
            GOTNetwork.CHANNEL.send(PacketDistributor.PLAYER.with(() -> player),
                new S2CAchievementDataPacket(GOTAchievementHooks.snapshot(player))));
        ctx.get().setPacketHandled(true);
    }
}
