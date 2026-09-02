package got.conquest;

import got.common.world.map.GOTWaypoint;
import got.conquest.pact.GOTCanonicalPactState;
import got.conquest.economy.GOTPactTreasuryState;
import net.minecraft.server.MinecraftServer;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/** Stable API facade for current and future Conquest gameplay layers. */
public final class GOTConquestManager {
    private GOTConquestManager() {}

    public static GOTConquestSavedData data(MinecraftServer server) {
        return GOTConquestSavedData.get(server);
    }

    public static WaypointSnapshot snapshot(MinecraftServer server, GOTWaypoint waypoint) {
        return snapshot(data(server).state(waypoint));
    }

    public static List<WaypointSnapshot> snapshots(MinecraftServer server) {
        GOTConquestSavedData data = data(server);
        return data.allWaypoints().stream().map(GOTConquestManager::snapshot).toList();
    }

    public static List<CanonicalPactSnapshot> canonicalPacts(MinecraftServer server) {
        return data(server).allCanonicalPacts().stream().map(GOTConquestManager::snapshot).toList();
    }

    public static List<TreasurySnapshot> treasuries(MinecraftServer server) {
        return data(server).allTreasuries().stream().map(GOTConquestManager::snapshot).toList();
    }

    public static Optional<TreasurySnapshot> treasury(MinecraftServer server, UUID pactId) {
        return data(server).treasury(pactId).map(GOTConquestManager::snapshot);
    }

    private static TreasurySnapshot snapshot(GOTPactTreasuryState treasury) {
        return new TreasurySnapshot(
                treasury.pactId(),
                treasury.balance(),
                treasury.seededCapital(),
                treasury.totalPlayerDeposits(),
                treasury.totalGeneratedIncome(),
                treasury.totalSpent(),
                treasury.lastIncomeGameTime(),
                treasury.lastUpdatedGameTime());
    }

    private static CanonicalPactSnapshot snapshot(GOTCanonicalPactState pact) {
        return new CanonicalPactSnapshot(
                pact.key().toString(),
                pact.id(),
                pact.name(),
                pact.leaderRoleId(),
                pact.active(),
                pact.members().stream().map(member -> new CanonicalPactMemberSnapshot(
                        member.roleId(), member.displayName(), member.faction().id())).toList(),
                pact.lastUpdatedGameTime());
    }

    private static WaypointSnapshot snapshot(GOTWaypointConquestState state) {
        GOTWaypoint waypoint = state.waypoint();
        String primaryRegion = waypoint.getRegions().isEmpty() ? "UNASSIGNED" : waypoint.getRegions().get(0).name();
        return new WaypointSnapshot(
                waypoint.name(),
                waypoint.getDisplayName().getString(),
                primaryRegion,
                waypoint.getFactionCode(),
                waypoint.getCoordX(),
                waypoint.getCoordZ(),
                state.strategicValue(),
                state.anchor(),
                state.controlState(),
                state.controllingPact(),
                state.claimingPact(),
                Map.copyOf(state.conquestScores()),
                state.lastUpdatedGameTime());
    }

    public record TreasurySnapshot(
            UUID pactId,
            long balance,
            long seededCapital,
            long totalPlayerDeposits,
            long totalGeneratedIncome,
            long totalSpent,
            long lastIncomeGameTime,
            long lastUpdatedGameTime) {}

    public record CanonicalPactMemberSnapshot(String roleId, String displayName, String faction) {}

    public record CanonicalPactSnapshot(
            String key,
            UUID id,
            String name,
            String leaderRoleId,
            boolean active,
            List<CanonicalPactMemberSnapshot> members,
            long lastUpdatedGameTime) {}

    public record WaypointSnapshot(
            String waypointKey,
            String displayName,
            String primaryRegion,
            String nativeFaction,
            int x,
            int z,
            int strategicValue,
            boolean anchor,
            GOTConquestControlState controlState,
            Optional<UUID> controllingPact,
            Optional<UUID> claimingPact,
            Map<UUID, Integer> conquestScores,
            long lastUpdatedGameTime) {}
}
