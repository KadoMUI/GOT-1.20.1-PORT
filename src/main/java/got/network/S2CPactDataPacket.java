package got.network;

import got.pact.GOTPact;
import got.pact.GOTPactInvite;
import got.pact.GOTPactSavedData;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;

/** Complete player-facing Pact snapshot. The server is always authoritative. */
public record S2CPactDataPacket(Snapshot snapshot) {
    public static S2CPactDataPacket forPlayer(ServerPlayer viewer) {
        MinecraftServer server = viewer.server;
        GOTPactSavedData data = GOTPactSavedData.get(server);
        GOTPact pact = data.pactFor(viewer.getUUID()).orElse(null);
        GOTPactInvite invite = data.inviteFor(viewer.getUUID()).orElse(null);

        PactView pactView = pact == null ? null : pactView(server, viewer, pact);
        InviteView inviteView = null;
        if (invite != null && !invite.expired(server.overworld().getGameTime())) {
            GOTPact invitedPact = data.pact(invite.pactId()).orElse(null);
            if (invitedPact != null) {
                inviteView = new InviteView(invitedPact.id(), invitedPact.name(),
                        playerName(server, invite.inviterId()), invite.expiresAtGameTime());
            }
        }
        return new S2CPactDataPacket(new Snapshot(pactView, inviteView));
    }

    private static PactView pactView(MinecraftServer server, ServerPlayer viewer, GOTPact pact) {
        List<MemberView> members = new ArrayList<>();
        for (UUID id : pact.members()) {
            ServerPlayer online = server.getPlayerList().getPlayer(id);
            String dimension = "";
            double x = 0, y = 0, z = 0;
            if (online != null) {
                dimension = online.level().dimension().location().toString();
                x = online.getX(); y = online.getY(); z = online.getZ();
            }
            members.add(new MemberView(id, playerName(server, id), pact.title(id), pact.isOwner(id),
                    pact.isAdmin(id), online != null, pact.mapSharers().contains(id), dimension, x, y, z));
        }
        String icon = pact.icon().map(ResourceLocation::toString).orElse("");
        return new PactView(pact.id(), pact.name(), icon,
                pact.isOwner(viewer.getUUID()), pact.isAdmin(viewer.getUUID()), pact.canManage(viewer.getUUID()),
                pact.preventPvp(), pact.preventHiredFriendlyFire(), pact.showMapLocations(), members);
    }

    private static String playerName(MinecraftServer server, UUID id) {
        ServerPlayer online = server.getPlayerList().getPlayer(id);
        if (online != null) return online.getGameProfile().getName();
        return server.getProfileCache().get(id).map(p -> p.getName()).orElse(id.toString().substring(0, 8));
    }

    public static void encode(S2CPactDataPacket msg, FriendlyByteBuf buf) {
        Snapshot s = msg.snapshot;
        buf.writeBoolean(s.pact != null);
        if (s.pact != null) writePact(buf, s.pact);
        buf.writeBoolean(s.invite != null);
        if (s.invite != null) {
            buf.writeUUID(s.invite.pactId); buf.writeUtf(s.invite.pactName, 32);
            buf.writeUtf(s.invite.inviterName, 32); buf.writeLong(s.invite.expiresAtGameTime);
        }
    }

    public static S2CPactDataPacket decode(FriendlyByteBuf buf) {
        PactView pact = buf.readBoolean() ? readPact(buf) : null;
        InviteView invite = null;
        if (buf.readBoolean()) invite = new InviteView(buf.readUUID(), buf.readUtf(32), buf.readUtf(32), buf.readLong());
        return new S2CPactDataPacket(new Snapshot(pact, invite));
    }

    private static void writePact(FriendlyByteBuf buf, PactView p) {
        buf.writeUUID(p.id); buf.writeUtf(p.name, 32); buf.writeUtf(p.icon, 128);
        buf.writeBoolean(p.viewerOwner); buf.writeBoolean(p.viewerAdmin); buf.writeBoolean(p.viewerCanManage);
        buf.writeBoolean(p.preventPvp); buf.writeBoolean(p.preventHiredFriendlyFire); buf.writeBoolean(p.showMapLocations);
        buf.writeVarInt(p.members.size());
        for (MemberView m : p.members) {
            buf.writeUUID(m.id); buf.writeUtf(m.name, 32); buf.writeUtf(m.title, 32);
            buf.writeBoolean(m.owner); buf.writeBoolean(m.admin); buf.writeBoolean(m.online); buf.writeBoolean(m.sharingMap);
            buf.writeUtf(m.dimension == null ? "" : m.dimension, 128);
            buf.writeDouble(m.x); buf.writeDouble(m.y); buf.writeDouble(m.z);
        }
    }

    private static PactView readPact(FriendlyByteBuf buf) {
        UUID id = buf.readUUID(); String name = buf.readUtf(32); String icon = buf.readUtf(128);
        boolean owner = buf.readBoolean(), admin = buf.readBoolean(), manage = buf.readBoolean();
        boolean pvp = buf.readBoolean(), hired = buf.readBoolean(), map = buf.readBoolean();
        int count = Math.min(buf.readVarInt(), GOTPact.DEFAULT_MAX_MEMBERS);
        List<MemberView> members = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            members.add(new MemberView(buf.readUUID(), buf.readUtf(32), buf.readUtf(32), buf.readBoolean(),
                    buf.readBoolean(), buf.readBoolean(), buf.readBoolean(), buf.readUtf(128),
                    buf.readDouble(), buf.readDouble(), buf.readDouble()));
        }
        return new PactView(id, name, icon, owner, admin, manage, pvp, hired, map, List.copyOf(members));
    }

    public static void handle(S2CPactDataPacket msg, Supplier<NetworkEvent.Context> ctxSupplier) {
        NetworkEvent.Context ctx = ctxSupplier.get();
        ctx.enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT,
                () -> () -> got.client.pact.ClientGOTPactData.apply(msg)));
        ctx.setPacketHandled(true);
    }

    public record Snapshot(PactView pact, InviteView invite) {
        public boolean inPact() { return pact != null; }
        public static Snapshot empty() { return new Snapshot(null, null); }
    }
    public record PactView(UUID id, String name, String icon, boolean viewerOwner, boolean viewerAdmin,
                           boolean viewerCanManage, boolean preventPvp, boolean preventHiredFriendlyFire,
                           boolean showMapLocations, List<MemberView> members) {}
    public record MemberView(UUID id, String name, String title, boolean owner, boolean admin, boolean online,
                             boolean sharingMap, String dimension, double x, double y, double z) {}
    public record InviteView(UUID pactId, String pactName, String inviterName, long expiresAtGameTime) {}
}
