package got.network.hiring;

import got.GOTCommandHornItem;
import got.npc.hiring.command.GOTCommandHornMode;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record C2SSetCommandHornModePacket(InteractionHand hand, GOTCommandHornMode mode) {
    public static void encode(C2SSetCommandHornModePacket p, FriendlyByteBuf b) {
        b.writeEnum(p.hand);
        b.writeEnum(p.mode);
    }

    public static C2SSetCommandHornModePacket decode(FriendlyByteBuf b) {
        return new C2SSetCommandHornModePacket(
            b.readEnum(InteractionHand.class),
            b.readEnum(GOTCommandHornMode.class)
        );
    }

    public static void handle(C2SSetCommandHornModePacket p, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (player == null) return;
            ItemStack stack = player.getItemInHand(p.hand);
            if (!(stack.getItem() instanceof GOTCommandHornItem)) return;
            if (p.mode == GOTCommandHornMode.SELECT) return;
            GOTCommandHornItem.setMode(stack, p.mode);
        });
        ctx.get().setPacketHandled(true);
    }
}
