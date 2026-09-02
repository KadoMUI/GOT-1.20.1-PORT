package got.conquest.pact;

import got.conquest.GOTConquestSavedData;
import got.quest.GOTQuestGiver;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

/** Query facade for NPC-controlled canonical Pacts. Player Pacts remain in got.pact. */
public final class GOTCanonicalPactService {
    private GOTCanonicalPactService() {}

    public static Collection<GOTCanonicalPactState> all(MinecraftServer server) {
        return GOTConquestSavedData.get(server).allCanonicalPacts();
    }

    public static Optional<GOTCanonicalPactState> byId(MinecraftServer server, UUID id) {
        return GOTConquestSavedData.get(server).canonicalPact(id);
    }

    public static Optional<GOTCanonicalPactState> byKey(MinecraftServer server, ResourceLocation key) {
        return GOTConquestSavedData.get(server).canonicalPact(key);
    }

    public static Optional<GOTCanonicalPactState> byKey(MinecraftServer server, String key) {
        return GOTCanonicalPacts.byKey(key).flatMap(definition -> byKey(server, definition.key()));
    }

    public static Optional<GOTCanonicalPactState> forRole(MinecraftServer server, String roleId) {
        String normalized = GOTCanonicalPactMember.normalizeRole(roleId);
        if (normalized.isBlank()) return Optional.empty();
        return all(server).stream().filter(GOTCanonicalPactState::active)
                .filter(pact -> pact.containsRole(normalized)).findFirst();
    }

    public static Optional<GOTCanonicalPactState> forNpc(MinecraftServer server, GOTQuestGiver npc) {
        return npc == null ? Optional.empty() : forRole(server, npc.getQuestRoleId());
    }

    public static boolean isCanonical(MinecraftServer server, UUID pactId) {
        return byId(server, pactId).isPresent();
    }
}
