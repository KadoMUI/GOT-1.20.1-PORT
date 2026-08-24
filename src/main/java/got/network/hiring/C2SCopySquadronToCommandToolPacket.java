package got.network.hiring;

import got.npc.hiring.command.GOTCommandToolSquadron;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * Optional convenience packet for copying a squadron name into a Command Sword/Horn.
 *
 * This does not alter command semantics; it only edits the legacy GOTSquadron tag.
 */
public record C2SCopySquadronToCommandToolPacket(
    InteractionHand hand,
    String squadron
) {
    public static void encode(C2SCopySquadronToCommandToolPacket p, FriendlyByteBuf b) {
        b.writeEnum(p.hand);
        b.writeUtf(p.squadron, 200);
    }

    public static C2SCopySquadronToCommandToolPacket decode(FriendlyByteBuf b) {
        return new C2SCopySquadronToCommandToolPacket(
            b.readEnum(InteractionHand.class),
            b.readUtf(200)
        );
    }

    public static void handle(C2SCopySquadronToCommandToolPacket p, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (player == null) return;

            ItemStack stack = player.getItemInHand(p.hand);
            GOTCommandToolSquadron.set(stack, p.squadron);
        });
        ctx.get().setPacketHandled(true);
    }
}
