package got.client.pact;

import got.network.S2CPactDataPacket;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/** Client-only cache of the most recent server-authoritative Pact snapshot. */
public final class ClientGOTPactData {
    private static S2CPactDataPacket.Snapshot snapshot = S2CPactDataPacket.Snapshot.empty();

    private ClientGOTPactData() {}

    public static void apply(S2CPactDataPacket packet) {
        snapshot = packet.snapshot();
    }

    public static S2CPactDataPacket.Snapshot snapshot() { return snapshot; }
    public static boolean inPact() { return snapshot.inPact(); }
    public static Optional<S2CPactDataPacket.PactView> pact() { return Optional.ofNullable(snapshot.pact()); }
    public static Optional<S2CPactDataPacket.InviteView> invite() { return Optional.ofNullable(snapshot.invite()); }

    public static List<S2CPactDataPacket.MemberView> mapMembers() {
        if (!snapshot.inPact() || snapshot.pact() == null || !snapshot.pact().showMapLocations()) return List.of();
        return snapshot.pact().members().stream()
                .filter(S2CPactDataPacket.MemberView::online)
                .filter(S2CPactDataPacket.MemberView::sharingMap)
                .filter(m -> m.dimension() != null && !m.dimension().isBlank())
                .toList();
    }

    public static Optional<S2CPactDataPacket.MemberView> member(UUID id) {
        return pact().flatMap(p -> p.members().stream().filter(m -> m.id().equals(id)).findFirst());
    }

    public static void clear() { snapshot = S2CPactDataPacket.Snapshot.empty(); }
}
