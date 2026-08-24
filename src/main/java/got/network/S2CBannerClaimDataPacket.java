package got.network;

import got.claim.GOTBannerClaimSnapshot;
import got.client.claim.GOTClientBannerClaimState;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record S2CBannerClaimDataPacket(GOTBannerClaimSnapshot snapshot, boolean openGui) {
    public static void encode(S2CBannerClaimDataPacket message, FriendlyByteBuf buffer) {
        GOTBannerClaimSnapshot.encode(message.snapshot, buffer);
        buffer.writeBoolean(message.openGui);
    }

    public static S2CBannerClaimDataPacket decode(FriendlyByteBuf buffer) {
        return new S2CBannerClaimDataPacket(GOTBannerClaimSnapshot.decode(buffer), buffer.readBoolean());
    }

    public static void handle(S2CBannerClaimDataPacket message,
                              Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT,
                () -> () -> GOTClientBannerClaimState.accept(message.snapshot, message.openGui)));
        context.setPacketHandled(true);
    }
}
