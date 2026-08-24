package got.network;

import got.pact.GOTPactService;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

/** All mutations initiated from the Pact GUI. */
public record C2SPactActionPacket(Action action, String text, UUID target, boolean flag) {
    public C2SPactActionPacket(Action action) { this(action, "", null, false); }
    public static C2SPactActionPacket text(Action a, String text) { return new C2SPactActionPacket(a, text, null, false); }
    public static C2SPactActionPacket target(Action a, UUID id) { return new C2SPactActionPacket(a, "", id, false); }
    public static C2SPactActionPacket flag(Action a, boolean value) { return new C2SPactActionPacket(a, "", null, value); }

    public static void encode(C2SPactActionPacket msg, FriendlyByteBuf buf) {
        buf.writeEnum(msg.action); buf.writeUtf(msg.text == null ? "" : msg.text, 64);
        buf.writeBoolean(msg.target != null); if (msg.target != null) buf.writeUUID(msg.target);
        buf.writeBoolean(msg.flag);
    }
    public static C2SPactActionPacket decode(FriendlyByteBuf buf) {
        Action action = buf.readEnum(Action.class); String text = buf.readUtf(64);
        UUID target = buf.readBoolean() ? buf.readUUID() : null; boolean flag = buf.readBoolean();
        return new C2SPactActionPacket(action, text, target, flag);
    }

    public static void handle(C2SPactActionPacket msg, Supplier<NetworkEvent.Context> ctxSupplier) {
        NetworkEvent.Context ctx = ctxSupplier.get();
        ctx.enqueueWork(() -> {
            ServerPlayer player = ctx.getSender();
            if (player == null) return;
            GOTPactService.Result result = apply(player, msg);
            if (!result.success()) player.sendSystemMessage(Component.literal(result.error()));
            GOTPactNetworkSync.syncRelevant(player);
        });
        ctx.setPacketHandled(true);
    }

    private static GOTPactService.Result apply(ServerPlayer p, C2SPactActionPacket m) {
        return switch (m.action) {
            case CREATE -> GOTPactService.create(p, m.text);
            case INVITE -> {
                ServerPlayer target = p.server.getPlayerList().getPlayerByName(m.text);
                yield target == null ? GOTPactService.Result.fail("That player is not online.") : GOTPactService.invite(p, target);
            }
            case ACCEPT -> GOTPactService.accept(p);
            case DECLINE -> GOTPactService.decline(p);
            case LEAVE -> GOTPactService.leave(p);
            case DISBAND -> GOTPactService.disband(p);
            case RENAME -> GOTPactService.rename(p, m.text);
            case SET_ICON -> GOTPactService.setIconFromHeldItem(p);
            case TOGGLE_PVP -> GOTPactService.togglePvp(p);
            case TOGGLE_HIRED_FF -> GOTPactService.toggleHiredFriendlyFire(p);
            case TOGGLE_MAP -> GOTPactService.toggleMap(p);
            case SET_MY_MAP_SHARING -> GOTPactService.setMapSharing(p, m.flag);
            case KICK -> requireTarget(m, () -> GOTPactService.kick(p, m.target));
            case OP -> requireTarget(m, () -> GOTPactService.setAdmin(p, m.target, true));
            case DEOP -> requireTarget(m, () -> GOTPactService.setAdmin(p, m.target, false));
            case TRANSFER -> requireTarget(m, () -> GOTPactService.transfer(p, m.target));
        };
    }

    private static GOTPactService.Result requireTarget(C2SPactActionPacket m, Supplier<GOTPactService.Result> action) {
        return m.target == null ? GOTPactService.Result.fail("No Pact member was selected.") : action.get();
    }

    public enum Action {
        CREATE, INVITE, ACCEPT, DECLINE, LEAVE, DISBAND, RENAME, SET_ICON,
        TOGGLE_PVP, TOGGLE_HIRED_FF, TOGGLE_MAP, SET_MY_MAP_SHARING,
        KICK, OP, DEOP, TRANSFER
    }
}
