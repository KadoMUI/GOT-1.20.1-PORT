package got.network.hiring;

import got.client.gui.hiring.GOTHiringClientScreens;
import got.npc.hiring.GOTHireSnapshot;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record S2CHiredNpcGuiPacket(GOTHireSnapshot snapshot) {
    public static void encode(S2CHiredNpcGuiPacket p, FriendlyByteBuf b) { GOTHireSnapshot.encode(p.snapshot,b); }
    public static S2CHiredNpcGuiPacket decode(FriendlyByteBuf b) { return new S2CHiredNpcGuiPacket(GOTHireSnapshot.decode(b)); }

    public static void handle(S2CHiredNpcGuiPacket p, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() ->
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> GOTHiringClientScreens.open(p.snapshot))
        );
        ctx.get().setPacketHandled(true);
    }
}
