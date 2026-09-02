package got.conquest;

import got.GOTMod;
import got.common.world.map.GOTWaypoint;
import got.conquest.pact.GOTCanonicalPactDefinition;
import got.conquest.pact.GOTCanonicalPactState;
import got.conquest.pact.GOTCanonicalPacts;
import got.conquest.pact.GOTLegendaryPactMembership;
import got.conquest.diplomacy.GOTCanonicalDiplomacySeeds;
import got.conquest.diplomacy.GOTCommunicationPolicy;
import got.conquest.diplomacy.GOTDiplomaticRelationState;
import got.conquest.diplomacy.GOTDiplomaticStatus;
import got.conquest.economy.GOTCanonicalEconomySeeds;
import got.conquest.economy.GOTPactTreasuryState;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.saveddata.SavedData;

import java.util.Collection;
import java.util.Collections;
import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * Server-authoritative, world-global persistence root for the Conquest system.
 *
 * Pass 5 contains waypoint state, canonical NPC Pacts, stable Legendary NPC political membership,
 * persistent Pact-to-Pact diplomatic relationships, and shared Pact treasuries.
 * Capture rules, regional aggregation, claims, army simulation and conquest-driven
 * spawning remain later layers. Player-created Pacts continue to live in got.pact.
 */
public final class GOTConquestSavedData extends SavedData {
    public static final int SCHEMA_VERSION = 5;
    private static final String FILE_NAME = "got_conquest";

    private final Map<GOTWaypoint, GOTWaypointConquestState> waypoints = new EnumMap<>(GOTWaypoint.class);
    private final Map<ResourceLocation, GOTCanonicalPactState> canonicalPacts = new LinkedHashMap<>();
    private final Map<String, GOTLegendaryPactMembership> legendaryMemberships = new LinkedHashMap<>();
    private final Map<String, GOTDiplomaticRelationState> diplomaticRelations = new LinkedHashMap<>();
    private final Map<UUID, GOTPactTreasuryState> treasuries = new LinkedHashMap<>();
    private long revision;
    private int loadedSchemaVersion = SCHEMA_VERSION;

    public static GOTConquestSavedData get(MinecraftServer server) {
        GOTConquestSavedData data = server.overworld().getDataStorage().computeIfAbsent(
                GOTConquestSavedData::load,
                GOTConquestSavedData::new,
                FILE_NAME);
        data.ensureAllWaypoints();
        data.ensureCanonicalPacts();
        data.ensureLegendaryMemberships();
        data.ensureCanonicalDiplomacy();
        data.ensureCanonicalTreasuries(server.overworld().getGameTime());
        return data;
    }

    public int schemaVersion() { return SCHEMA_VERSION; }
    public int loadedSchemaVersion() { return loadedSchemaVersion; }
    public long revision() { return revision; }

    public Collection<GOTWaypointConquestState> allWaypoints() {
        return Collections.unmodifiableCollection(waypoints.values());
    }

    public Collection<GOTCanonicalPactState> allCanonicalPacts() {
        return Collections.unmodifiableCollection(canonicalPacts.values());
    }

    public Collection<GOTLegendaryPactMembership> allLegendaryMemberships() {
        return Collections.unmodifiableCollection(legendaryMemberships.values());
    }

    public Collection<GOTDiplomaticRelationState> allDiplomaticRelations() {
        return Collections.unmodifiableCollection(diplomaticRelations.values());
    }

    public Collection<GOTPactTreasuryState> allTreasuries() {
        return Collections.unmodifiableCollection(treasuries.values());
    }

    public Optional<GOTPactTreasuryState> treasury(UUID pactId) {
        return Optional.ofNullable(treasuries.get(pactId));
    }

    /** Lazily creates a zero-balance treasury for a player-created Pact. */
    public GOTPactTreasuryState treasury(UUID pactId, long gameTime) {
        if (pactId == null) throw new IllegalArgumentException("Pact id cannot be null");
        GOTPactTreasuryState treasury = treasuries.get(pactId);
        if (treasury == null) {
            treasury = new GOTPactTreasuryState(pactId, 0L, gameTime);
            treasuries.put(pactId, treasury);
            changed();
        }
        return treasury;
    }

    public void depositTreasury(UUID pactId, long amount, long gameTime) {
        if (amount <= 0L) return;
        treasury(pactId, gameTime).depositPlayer(amount, gameTime);
        changed();
    }

    public void generateTreasuryIncome(UUID pactId, long amount, long newIncomeClock, long gameTime) {
        GOTPactTreasuryState treasury = treasury(pactId, gameTime);
        treasury.generateIncome(amount, newIncomeClock, gameTime);
        changed();
    }

    public boolean spendTreasury(UUID pactId, long amount, long gameTime) {
        GOTPactTreasuryState treasury = treasury(pactId, gameTime);
        boolean spent = treasury.spend(amount, gameTime);
        if (spent && amount > 0L) changed();
        return spent;
    }

    public void setTreasuryBalance(UUID pactId, long balance, long gameTime) {
        treasury(pactId, gameTime).setBalance(balance, gameTime);
        changed();
    }

    public void setTreasuryIncomeClock(UUID pactId, long incomeClock, long gameTime) {
        treasury(pactId, gameTime).setIncomeClock(incomeClock, gameTime);
        changed();
    }

    /** Removes orphaned player-Pact treasuries while preserving deterministic canonical accounts. */
    public int clearMissingPlayerPactTreasuries(java.util.function.Predicate<UUID> playerPactExists) {
        int before = treasuries.size();
        treasuries.entrySet().removeIf(entry ->
                GOTCanonicalPacts.byId(entry.getKey()).isEmpty() && !playerPactExists.test(entry.getKey()));
        int removed = before - treasuries.size();
        if (removed > 0) changed();
        return removed;
    }

    public Optional<GOTDiplomaticRelationState> diplomaticRelation(UUID first, UUID second) {
        if (first == null || second == null || first.equals(second)) return Optional.empty();
        return Optional.ofNullable(diplomaticRelations.get(GOTDiplomaticRelationState.key(first, second)));
    }

    public GOTDiplomaticRelationState diplomacy(UUID first, UUID second) {
        if (first == null || second == null || first.equals(second)) {
            throw new IllegalArgumentException("Diplomacy requires two different Pacts");
        }
        String key = GOTDiplomaticRelationState.key(first, second);
        GOTDiplomaticRelationState relation = diplomaticRelations.get(key);
        if (relation == null) {
            relation = new GOTDiplomaticRelationState(first, second);
            diplomaticRelations.put(key, relation);
            changed();
        }
        return relation;
    }

    public void setDiplomaticOpinion(UUID from, UUID to, int value, long gameTime) {
        diplomacy(from, to).setOpinion(from, to, value, gameTime);
        changed();
    }

    public void setDiplomaticTrust(UUID from, UUID to, int value, long gameTime) {
        diplomacy(from, to).setTrust(from, to, value, gameTime);
        changed();
    }

    public void setDiplomaticFear(UUID from, UUID to, int value, long gameTime) {
        diplomacy(from, to).setFear(from, to, value, gameTime);
        changed();
    }

    public void setDiplomaticCommunication(UUID from, UUID to, GOTCommunicationPolicy policy, long gameTime) {
        diplomacy(from, to).setCommunication(from, to, policy, gameTime);
        changed();
    }

    public void setDiplomaticStatus(UUID first, UUID second, GOTDiplomaticStatus status, long gameTime) {
        diplomacy(first, second).setStatus(status, gameTime);
        changed();
    }

    public void setTradeAgreement(UUID first, UUID second, boolean enabled, long gameTime) {
        diplomacy(first, second).setTradeAgreement(enabled, gameTime);
        changed();
    }

    public void setNonAggressionPact(UUID first, UUID second, boolean enabled, long gameTime) {
        diplomacy(first, second).setNonAggressionPact(enabled, gameTime);
        changed();
    }

    public void setTruceUntil(UUID first, UUID second, long truceUntilGameTime, long gameTime) {
        diplomacy(first, second).setTruceUntilGameTime(truceUntilGameTime, gameTime);
        changed();
    }

    public Optional<GOTLegendaryPactMembership> legendaryMembership(String roleId) {
        return Optional.ofNullable(legendaryMemberships.get(got.conquest.pact.GOTCanonicalPactMember.normalizeRole(roleId)));
    }

    public Optional<GOTCanonicalPactState> canonicalPact(ResourceLocation key) {
        return Optional.ofNullable(canonicalPacts.get(key));
    }

    public Optional<GOTCanonicalPactState> canonicalPact(UUID id) {
        return canonicalPacts.values().stream().filter(pact -> pact.id().equals(id)).findFirst();
    }

    public GOTWaypointConquestState state(GOTWaypoint waypoint) {
        GOTWaypointConquestState state = waypoints.get(waypoint);
        if (state == null) {
            state = new GOTWaypointConquestState(waypoint);
            waypoints.put(waypoint, state);
            changed();
        }
        return state;
    }

    public void setStrategicValue(GOTWaypoint waypoint, int value, long gameTime) {
        state(waypoint).setStrategicValue(value, gameTime);
        changed();
    }

    public void setAnchor(GOTWaypoint waypoint, boolean anchor, long gameTime) {
        state(waypoint).setAnchor(anchor, gameTime);
        changed();
    }

    public void setControlState(GOTWaypoint waypoint, GOTConquestControlState controlState, long gameTime) {
        state(waypoint).setControlState(controlState, gameTime);
        changed();
    }

    public void setControllingPact(GOTWaypoint waypoint, UUID pactId, long gameTime) {
        state(waypoint).setControllingPact(pactId, gameTime);
        changed();
    }

    public void setClaimingPact(GOTWaypoint waypoint, UUID pactId, long gameTime) {
        state(waypoint).setClaimingPact(pactId, gameTime);
        changed();
    }

    public void setConquestScore(GOTWaypoint waypoint, UUID pactId, int score, long gameTime) {
        state(waypoint).setConquestScore(pactId, score, gameTime);
        changed();
    }

    public void clearConquestScore(GOTWaypoint waypoint, UUID pactId, long gameTime) {
        state(waypoint).clearConquestScore(pactId, gameTime);
        changed();
    }

    public void resetWaypoint(GOTWaypoint waypoint, long gameTime) {
        state(waypoint).resetMutableState(gameTime);
        changed();
    }

    public void resetAll(long gameTime) {
        for (GOTWaypointConquestState state : waypoints.values()) state.resetMutableState(gameTime);
        changed();
    }

    /**
     * Moves a stable Legendary role into a player-run Pact. Canonical membership
     * is removed atomically, while the NPC's underlying faction identity remains intact.
     */
    public GOTLegendaryPactMembership recruitLegendary(String roleId, String displayName,
                                                        got.faction.GOTFaction faction, UUID playerPactId,
                                                        UUID recruitedBy, long gameTime) {
        String normalized = got.conquest.pact.GOTCanonicalPactMember.normalizeRole(roleId);
        if (normalized.isBlank()) throw new IllegalArgumentException("Legendary role id cannot be blank");

        ResourceLocation origin = null;
        for (GOTCanonicalPactState pact : canonicalPacts.values()) {
            if (pact.containsRole(normalized)) {
                origin = pact.key();
                pact.removeMember(normalized, gameTime);
                break;
            }
        }

        GOTLegendaryPactMembership membership = legendaryMemberships.get(normalized);
        if (membership == null) {
            membership = new GOTLegendaryPactMembership(normalized, displayName, faction, origin, playerPactId);
            legendaryMemberships.put(normalized, membership);
        } else {
            membership.refreshIdentity(displayName, faction);
            membership.rememberOrigin(origin);
        }
        membership.assign(playerPactId, recruitedBy, gameTime);
        changed();
        return membership;
    }

    /** Leaves a Legendary NPC politically unaffiliated without deleting its identity record. */
    public boolean releaseLegendary(String roleId, long gameTime) {
        GOTLegendaryPactMembership membership = legendaryMemberships.get(
                got.conquest.pact.GOTCanonicalPactMember.normalizeRole(roleId));
        if (membership == null || membership.currentPactId().isEmpty()) return false;
        membership.clearAssignment(gameTime);
        changed();
        return true;
    }

    /** Restores a Legendary role to its original canonical Pact when that origin is known. */
    public boolean restoreLegendaryToOrigin(String roleId, long gameTime) {
        String normalized = got.conquest.pact.GOTCanonicalPactMember.normalizeRole(roleId);
        GOTLegendaryPactMembership membership = legendaryMemberships.get(normalized);
        if (membership == null || membership.originCanonicalPact().isEmpty()) return false;
        ResourceLocation origin = membership.originCanonicalPact().get();
        GOTCanonicalPactState state = canonicalPacts.get(origin);
        GOTCanonicalPactDefinition definition = GOTCanonicalPacts.byKey(origin).orElse(null);
        if (state == null || definition == null) return false;
        got.conquest.pact.GOTCanonicalPactMember member = definition.member(normalized).orElse(null);
        if (member == null) return false;
        boolean originalLeader = definition.leaderRoleId().equals(normalized);
        if (!state.containsRole(normalized)) state.addMember(member, originalLeader, gameTime);
        membership.assign(state.id(), null, gameTime);
        changed();
        return true;
    }

    /** Clears recruited memberships whose player Pact has been disbanded. */
    public int clearMissingPlayerPactAssignments(java.util.function.Predicate<UUID> playerPactExists, long gameTime) {
        int cleared = 0;
        for (GOTLegendaryPactMembership membership : legendaryMemberships.values()) {
            Optional<UUID> current = membership.currentPactId();
            if (current.isEmpty()) continue;
            UUID pactId = current.get();
            if (canonicalPact(pactId).isPresent()) continue;
            if (!playerPactExists.test(pactId)) {
                membership.clearAssignment(gameTime);
                cleared++;
            }
        }
        if (cleared > 0) changed();
        return cleared;
    }

    private void ensureLegendaryMemberships() {
        boolean added = false;
        for (GOTCanonicalPactState pact : canonicalPacts.values()) {
            for (got.conquest.pact.GOTCanonicalPactMember member : pact.members()) {
                String role = member.roleId();
                if (!legendaryMemberships.containsKey(role)) {
                    legendaryMemberships.put(role, new GOTLegendaryPactMembership(
                            role, member.displayName(), member.faction(), pact.key(), pact.id()));
                    added = true;
                }
            }
        }
        if (added) changed();
    }


    private void ensureCanonicalTreasuries(long gameTime) {
        boolean added = false;
        for (GOTCanonicalEconomySeeds.Seed seed : GOTCanonicalEconomySeeds.seeds()) {
            if (!treasuries.containsKey(seed.pactId())) {
                treasuries.put(seed.pactId(),
                        new GOTPactTreasuryState(seed.pactId(), seed.startingTreasury(), gameTime));
                added = true;
            }
        }
        if (added) changed();
    }

    private void ensureCanonicalDiplomacy() {
        boolean added = false;
        for (GOTCanonicalDiplomacySeeds.Seed seed : GOTCanonicalDiplomacySeeds.seeds()) {
            String key = GOTDiplomaticRelationState.key(seed.pactA(), seed.pactB());
            if (!diplomaticRelations.containsKey(key)) {
                diplomaticRelations.put(key, seed.create());
                added = true;
            }
        }
        if (added) changed();
    }

    private void ensureCanonicalPacts() {
        boolean added = false;
        for (GOTCanonicalPactDefinition definition : GOTCanonicalPacts.definitions()) {
            if (!canonicalPacts.containsKey(definition.key())) {
                canonicalPacts.put(definition.key(), new GOTCanonicalPactState(definition));
                added = true;
            }
        }
        if (added) changed();
    }

    private void ensureAllWaypoints() {
        boolean added = false;
        for (GOTWaypoint waypoint : GOTWaypoint.values()) {
            if (!waypoints.containsKey(waypoint)) {
                waypoints.put(waypoint, new GOTWaypointConquestState(waypoint));
                added = true;
            }
        }
        if (added) changed();
    }

    private void changed() {
        revision++;
        setDirty();
    }

    @Override
    public CompoundTag save(CompoundTag root) {
        root.putInt("SchemaVersion", SCHEMA_VERSION);
        root.putLong("Revision", revision);
        ListTag waypointList = new ListTag();
        for (GOTWaypoint waypoint : GOTWaypoint.values()) {
            GOTWaypointConquestState state = waypoints.get(waypoint);
            if (state != null) waypointList.add(state.save());
        }
        root.put("Waypoints", waypointList);

        ListTag canonicalPactList = new ListTag();
        for (GOTCanonicalPactState pact : canonicalPacts.values()) canonicalPactList.add(pact.save());
        root.put("CanonicalPacts", canonicalPactList);
        root.putInt("CanonicalPactDefinitionVersion", GOTCanonicalPacts.DEFINITION_VERSION);

        ListTag legendaryList = new ListTag();
        for (GOTLegendaryPactMembership membership : legendaryMemberships.values()) legendaryList.add(membership.save());
        root.put("LegendaryPactMemberships", legendaryList);

        ListTag diplomacyList = new ListTag();
        for (GOTDiplomaticRelationState relation : diplomaticRelations.values()) diplomacyList.add(relation.save());
        root.put("DiplomaticRelations", diplomacyList);
        root.putInt("CanonicalDiplomacyDefinitionVersion", GOTCanonicalDiplomacySeeds.DEFINITION_VERSION);

        ListTag treasuryList = new ListTag();
        for (GOTPactTreasuryState treasury : treasuries.values()) treasuryList.add(treasury.save());
        root.put("PactTreasuries", treasuryList);
        root.putInt("CanonicalEconomyDefinitionVersion", GOTCanonicalEconomySeeds.DEFINITION_VERSION);
        return root;
    }

    public static GOTConquestSavedData load(CompoundTag root) {
        GOTConquestSavedData data = new GOTConquestSavedData();
        data.loadedSchemaVersion = root.contains("SchemaVersion", Tag.TAG_INT)
                ? root.getInt("SchemaVersion")
                : 0;
        data.revision = Math.max(0L, root.getLong("Revision"));

        if (data.loadedSchemaVersion > SCHEMA_VERSION) {
            GOTMod.LOGGER.warn("Conquest save schema {} is newer than supported schema {}; preserving known fields only",
                    data.loadedSchemaVersion, SCHEMA_VERSION);
        }

        ListTag waypointList = root.getList("Waypoints", Tag.TAG_COMPOUND);
        int ignored = 0;
        for (int i = 0; i < waypointList.size(); i++) {
            GOTWaypointConquestState state = GOTWaypointConquestState.load(waypointList.getCompound(i));
            if (state == null) {
                ignored++;
                continue;
            }
            data.waypoints.put(state.waypoint(), state);
        }
        if (ignored > 0) GOTMod.LOGGER.warn("Ignored {} conquest waypoint records whose waypoint names no longer exist", ignored);

        ListTag canonicalPactList = root.getList("CanonicalPacts", Tag.TAG_COMPOUND);
        int ignoredPacts = 0;
        for (int i = 0; i < canonicalPactList.size(); i++) {
            GOTCanonicalPactState pact = GOTCanonicalPactState.load(canonicalPactList.getCompound(i));
            if (pact == null) {
                ignoredPacts++;
                continue;
            }
            data.canonicalPacts.put(pact.key(), pact);
        }
        if (ignoredPacts > 0) GOTMod.LOGGER.warn("Ignored {} malformed canonical Pact records", ignoredPacts);

        ListTag legendaryList = root.getList("LegendaryPactMemberships", Tag.TAG_COMPOUND);
        int ignoredLegendary = 0;
        for (int i = 0; i < legendaryList.size(); i++) {
            GOTLegendaryPactMembership membership = GOTLegendaryPactMembership.load(legendaryList.getCompound(i));
            if (membership == null) {
                ignoredLegendary++;
                continue;
            }
            data.legendaryMemberships.put(membership.roleId(), membership);
        }
        if (ignoredLegendary > 0) GOTMod.LOGGER.warn("Ignored {} malformed Legendary Pact membership records", ignoredLegendary);

        ListTag diplomacyList = root.getList("DiplomaticRelations", Tag.TAG_COMPOUND);
        int ignoredDiplomacy = 0;
        for (int i = 0; i < diplomacyList.size(); i++) {
            GOTDiplomaticRelationState relation = GOTDiplomaticRelationState.load(diplomacyList.getCompound(i));
            if (relation == null) {
                ignoredDiplomacy++;
                continue;
            }
            data.diplomaticRelations.put(GOTDiplomaticRelationState.key(relation.pactA(), relation.pactB()), relation);
        }
        if (ignoredDiplomacy > 0) GOTMod.LOGGER.warn("Ignored {} malformed Conquest diplomatic relationship records", ignoredDiplomacy);

        ListTag treasuryList = root.getList("PactTreasuries", Tag.TAG_COMPOUND);
        int ignoredTreasuries = 0;
        for (int i = 0; i < treasuryList.size(); i++) {
            GOTPactTreasuryState treasury = GOTPactTreasuryState.load(treasuryList.getCompound(i));
            if (treasury == null) {
                ignoredTreasuries++;
                continue;
            }
            data.treasuries.put(treasury.pactId(), treasury);
        }
        if (ignoredTreasuries > 0) GOTMod.LOGGER.warn("Ignored {} malformed Conquest Pact treasury records", ignoredTreasuries);
        return data;
    }
}
