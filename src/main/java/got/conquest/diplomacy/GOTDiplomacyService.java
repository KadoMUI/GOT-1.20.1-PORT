package got.conquest.diplomacy;

import got.conquest.GOTConquestSavedData;
import net.minecraft.server.MinecraftServer;

import java.util.Optional;
import java.util.UUID;

/** Read-focused API used by future Conquest AI, war, economy, and UI layers. */
public final class GOTDiplomacyService {
    private GOTDiplomacyService() {}

    public static Optional<GOTDiplomaticRelationState> existing(MinecraftServer server, UUID first, UUID second) {
        return GOTConquestSavedData.get(server).diplomaticRelation(first, second);
    }

    public static GOTDiplomaticRelationState relation(MinecraftServer server, UUID first, UUID second) {
        return GOTConquestSavedData.get(server).diplomacy(first, second);
    }

    public static boolean areAtWar(MinecraftServer server, UUID first, UUID second) {
        return existing(server, first, second)
                .map(relation -> relation.status() == GOTDiplomaticStatus.AT_WAR)
                .orElse(false);
    }

    public static boolean areAllied(MinecraftServer server, UUID first, UUID second) {
        return existing(server, first, second)
                .map(relation -> relation.status() == GOTDiplomaticStatus.ALLIED)
                .orElse(false);
    }

    public static boolean canCommunicate(MinecraftServer server, UUID from, UUID to) {
        return existing(server, from, to)
                .map(relation -> relation.communication(from, to) != GOTCommunicationPolicy.CLOSED)
                .orElse(true);
    }

    public static boolean hasTradeAgreement(MinecraftServer server, UUID first, UUID second) {
        return existing(server, first, second).map(GOTDiplomaticRelationState::tradeAgreement).orElse(false);
    }

    public static boolean hasNonAggressionPact(MinecraftServer server, UUID first, UUID second) {
        return existing(server, first, second).map(GOTDiplomaticRelationState::nonAggressionPact).orElse(false);
    }

    public static boolean hasActiveTruce(MinecraftServer server, UUID first, UUID second) {
        long now = server.overworld().getGameTime();
        return existing(server, first, second)
                .map(relation -> relation.truceUntilGameTime() > now)
                .orElse(false);
    }
}
