package got.network.hiring;

import got.GOTSquadronItem;
import got.npc.hiring.GOTHiredOrder;
import got.npc.hiring.squadron.GOTSquadronService;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record C2SSquadronActionPacket(
    InteractionHand hand,
    Action action,
    String squadron,
    GOTHiredOrder order,
    int radius
) {
    public enum Action {
        SET_ITEM_SQUADRON,
        ASSIGN_NEARBY,
        CLEAR_NEARBY,
        COMMAND_GROUP
    }

    public static void encode(C2SSquadronActionPacket p, FriendlyByteBuf b) {
        b.writeEnum(p.hand);
        b.writeEnum(p.action);
        b.writeUtf(p.squadron, 200);
        b.writeBoolean(p.order != null);
        if (p.order != null) b.writeEnum(p.order);
        b.writeVarInt(p.radius);
    }

    public static C2SSquadronActionPacket decode(FriendlyByteBuf b) {
        InteractionHand hand = b.readEnum(InteractionHand.class);
        Action action = b.readEnum(Action.class);
        String squadron = b.readUtf(200);
        GOTHiredOrder order = b.readBoolean() ? b.readEnum(GOTHiredOrder.class) : null;
        int radius = b.readVarInt();
        return new C2SSquadronActionPacket(hand, action, squadron, order, radius);
    }

    public static void handle(C2SSquadronActionPacket p, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (player == null) return;

            ItemStack held = player.getItemInHand(p.hand);
            if (!(held.getItem() instanceof GOTSquadronItem)) return;

            String squad = p.squadron == null ? "" : p.squadron.trim();
            if (squad.length() > 200) squad = squad.substring(0, 200);

            int radius = Math.max(1, Math.min(64, p.radius));

            switch (p.action) {
                case SET_ITEM_SQUADRON -> GOTSquadronService.setItemSquadron(held, squad);
                case ASSIGN_NEARBY -> GOTSquadronService.assignNearby(player, squad, radius);
                case CLEAR_NEARBY -> GOTSquadronService.clearNearby(player, radius);
                case COMMAND_GROUP -> {
                    if (p.order != null) GOTSquadronService.commandNearby(player, squad, p.order, radius);
                }
            }
        });

        ctx.get().setPacketHandled(true);
    }
}
