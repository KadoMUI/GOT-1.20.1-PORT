package got.conquest.pact;

import got.conquest.GOTConquestSavedData;
import got.quest.GOTQuestGiver;
import net.minecraft.server.MinecraftServer;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

/** Query facade for a Legendary role's current political Pact, canonical or player-run. */
public final class GOTLegendaryPactService {
    private GOTLegendaryPactService() {}

    public static Collection<GOTLegendaryPactMembership> all(MinecraftServer server) {
        return GOTConquestSavedData.get(server).allLegendaryMemberships();
    }

    public static Optional<GOTLegendaryPactMembership> membership(MinecraftServer server, String roleId) {
        return GOTConquestSavedData.get(server).legendaryMembership(roleId);
    }

    public static Optional<UUID> currentPactId(MinecraftServer server, String roleId) {
        return membership(server, roleId).flatMap(GOTLegendaryPactMembership::currentPactId);
    }

    public static Optional<UUID> currentPactId(MinecraftServer server, GOTQuestGiver npc) {
        return npc == null ? Optional.empty() : currentPactId(server, npc.getQuestRoleId());
    }

    public static boolean belongsTo(MinecraftServer server, String roleId, UUID pactId) {
        return pactId != null && currentPactId(server, roleId).filter(pactId::equals).isPresent();
    }
}
