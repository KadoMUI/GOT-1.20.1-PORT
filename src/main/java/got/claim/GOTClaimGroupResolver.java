package got.claim;

import net.minecraft.server.MinecraftServer;

import javax.annotation.Nullable;
import java.util.UUID;

/**
 * Stable integration boundary for the later Pacts pass. Banner data may carry
 * f/name entries now; a Pact provider can resolve them without rewriting claims.
 */
public final class GOTClaimGroupResolver {
    public interface Provider {
        boolean isMember(MinecraftServer server, @Nullable UUID groupId, String groupName, UUID playerId);
    }

    private static volatile Provider provider = (server, groupId, groupName, playerId) -> false;

    private GOTClaimGroupResolver() {}

    public static void register(Provider replacement) {
        provider = replacement == null ? (server, groupId, groupName, playerId) -> false : replacement;
    }

    public static boolean isMember(MinecraftServer server, GOTBannerWhitelistEntry entry, UUID playerId) {
        return entry.kind() == GOTBannerWhitelistEntry.Kind.GROUP
                && provider.isMember(server, entry.id(), entry.name(), playerId);
    }
}
