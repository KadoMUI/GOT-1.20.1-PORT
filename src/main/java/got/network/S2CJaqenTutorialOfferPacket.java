package got.network;

import got.client.gui.GOTGuiJaqenTutorialOffer;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record S2CJaqenTutorialOfferPacket(int entityId) {
    public static void encode(S2CJaqenTutorialOfferPacket m, FriendlyByteBuf b){b.writeInt(m.entityId);}
    public static S2CJaqenTutorialOfferPacket decode(FriendlyByteBuf b){return new S2CJaqenTutorialOfferPacket(b.readInt());}
    public static void handle(S2CJaqenTutorialOfferPacket m, Supplier<NetworkEvent.Context> sup){
        NetworkEvent.Context c=sup.get(); c.enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> Minecraft.getInstance().setScreen(new GOTGuiJaqenTutorialOffer(m.entityId)))); c.setPacketHandled(true);
    }
}
