package got.network;

import got.client.achievement.GOTClientAchievementState;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;

public record S2CAchievementDataPacket(Set<String> awarded) {
    public static void encode(S2CAchievementDataPacket p, FriendlyByteBuf b) {
        b.writeVarInt(p.awarded.size());
        for (String id : p.awarded) b.writeUtf(id);
    }
    public static S2CAchievementDataPacket decode(FriendlyByteBuf b) {
        int n=b.readVarInt(); Set<String> ids=new HashSet<>();
        for (int i=0;i<n;i++) ids.add(b.readUtf());
        return new S2CAchievementDataPacket(ids);
    }
    public static void handle(S2CAchievementDataPacket p, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> GOTClientAchievementState.set(p.awarded));
        ctx.get().setPacketHandled(true);
    }
}
