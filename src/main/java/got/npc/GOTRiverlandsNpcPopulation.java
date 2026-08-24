package got.npc;

import got.GOTEntities;
import got.GOTMod;
import got.common.world.map.GOTWaypoint;
import got.world.structure.north.NorthStructureMarker;
import got.world.terrain.PlanetosTerrainSampler;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Chunk-safe population and fixed-waypoint placement for Riverlands NPCs.
 * Entity creation is deferred out of WorldGenRegion to avoid the same
 * unfinished-chunk deadlock corrected during the Winterfell pass.
 */
@Mod.EventBusSubscriber(modid = GOTMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class GOTRiverlandsNpcPopulation {
    private static final int MAX_DEFERRED_SPAWNS_PER_TICK = 32;
    private static final Map<PendingKey, PendingSpawn> PENDING = new ConcurrentHashMap<>();
    private static final ConcurrentLinkedQueue<PendingKey> PENDING_ORDER = new ConcurrentLinkedQueue<>();

    private static final List<FixedSpawn> FIXED_SPAWNS = List.of(
            fixed(GOTWaypoint.MAIDENPOOL, RiverlandsNpcRole.WILLIAM_MOOTON, 0, 2),
            fixed(GOTWaypoint.PINKMAIDEN_CASTLE, RiverlandsNpcRole.CLEMENT_PIPER, 0, 2),
            fixed(GOTWaypoint.RAVENTREE_HALL, RiverlandsNpcRole.TYTOS_BLACKWOOD, 2, 0),
            fixed(GOTWaypoint.RIVERRUN, RiverlandsNpcRole.BRYNDEN_TULLY, 2, 2),
            fixed(GOTWaypoint.RIVERRUN, RiverlandsNpcRole.EDMURE_TULLY, -2, -2),
            fixed(GOTWaypoint.RIVERRUN, RiverlandsNpcRole.HOSTER_TULLY, 2, -2),
            fixed(GOTWaypoint.SEAGARD, RiverlandsNpcRole.JASON_MALLISTER, 0, 3),
            fixed(GOTWaypoint.STONE_HEDGE, RiverlandsNpcRole.JONOS_BRACKEN, 0, 2),
            fixed(GOTWaypoint.TWINS_LEFT, RiverlandsNpcRole.BLACK_WALDER_FREY, 0, -15),
            // The legacy fixer used the same block for both men. One-block separation
            // preserves the placement while avoiding modern entity collision rejection.
            fixed(GOTWaypoint.TWINS_LEFT, RiverlandsNpcRole.LOTHAR_FREY, 1, -15),
            fixed(GOTWaypoint.TWINS_RIGHT, RiverlandsNpcRole.WALDER_FREY, 0, -15)
    );

    private static final List<NorthFixedSpawn> NORTH_FIXED_SPAWNS = List.of(
            northFixed(GOTWaypoint.RIVERRUN, NorthNpcRole.RODRIK_CASSEL, -2, 2),
            northFixed(GOTWaypoint.RIVERRUN, NorthNpcRole.CATELYN_STARK, 2, 0)
    );

    private GOTRiverlandsNpcPopulation() {}

    public static int spawnMarkers(LevelAccessor accessor, List<NorthStructureMarker> markers) {
        if (markers.isEmpty()) return 0;
        if (accessor instanceof ServerLevel level) {
            return spawnPrepared(level, prepare(markers));
        }
        if (accessor instanceof WorldGenLevel worldGen) {
            enqueue(worldGen.getLevel(), prepare(markers));
        }
        return 0;
    }

    /** Queues only the fixed NPCs whose exact target positions belong to this chunk. */
    public static void queueFixedSites(WorldGenLevel level, ChunkAccess chunk, long seed) {
        int chunkX = chunk.getPos().x;
        int chunkZ = chunk.getPos().z;
        PlanetosTerrainSampler terrain = null;
        ArrayList<PreparedSpawn> prepared = new ArrayList<>();
        for (FixedSpawn fixed : FIXED_SPAWNS) {
            int x = fixed.waypoint().getCoordX() + fixed.offsetX();
            int z = fixed.waypoint().getCoordZ() + fixed.offsetZ();
            if (Math.floorDiv(x, 16) != chunkX || Math.floorDiv(z, 16) != chunkZ) continue;
            if (terrain == null) terrain = new PlanetosTerrainSampler(seed);
            int y = terrain.surfaceHeight(x, z) + 1;
            NorthStructureMarker marker = new NorthStructureMarker(
                    "legendary_npc:" + fixed.role().id(), new BlockPos(x, y, z), 0);
            String populationKey = "riverlands:fixed:" + fixed.waypoint().name().toLowerCase(Locale.ROOT)
                    + ':' + fixed.role().id();
            prepared.add(new PreparedSpawn(marker,
                    new ParsedMarker(fixed.role().id(), null, false, 16),
                    fixed.role(), populationKey));
        }
        enqueue(level.getLevel(), prepared);

        ArrayList<NorthStructureMarker> northMarkers = new ArrayList<>();
        for (NorthFixedSpawn fixed : NORTH_FIXED_SPAWNS) {
            int x = fixed.waypoint().getCoordX() + fixed.offsetX();
            int z = fixed.waypoint().getCoordZ() + fixed.offsetZ();
            if (Math.floorDiv(x, 16) != chunkX || Math.floorDiv(z, 16) != chunkZ) continue;
            if (terrain == null) terrain = new PlanetosTerrainSampler(seed);
            int y = terrain.surfaceHeight(x, z) + 1;
            northMarkers.add(new NorthStructureMarker(
                    "legendary_npc:" + fixed.role().id(), new BlockPos(x, y, z), 0));
        }
        GOTNorthNpcPopulation.spawnMarkers(level, northMarkers);
    }

    private static void enqueue(ServerLevel level, List<PreparedSpawn> spawns) {
        MinecraftServer server = level.getServer();
        for (PreparedSpawn spawn : spawns) {
            PendingKey key = new PendingKey(server, level.dimension(), spawn.populationKey());
            if (PENDING.putIfAbsent(key, new PendingSpawn(spawn)) == null) {
                PENDING_ORDER.add(key);
            }
        }
    }

    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase != TickEvent.Phase.END || PENDING.isEmpty()) return;
        MinecraftServer server = event.getServer();
        int budget = Math.min(MAX_DEFERRED_SPAWNS_PER_TICK, PENDING.size());
        for (int i = 0; i < budget; i++) {
            PendingKey key = PENDING_ORDER.poll();
            if (key == null) return;
            PendingSpawn pending = PENDING.get(key);
            if (pending == null) continue;
            if (key.server() != server) {
                PENDING.remove(key, pending);
                continue;
            }

            ServerLevel level = server.getLevel(key.dimension());
            if (level == null) {
                PENDING.remove(key, pending);
                continue;
            }
            BlockPos position = pending.spawn().marker().position();
            int chunkX = Math.floorDiv(position.getX(), 16);
            int chunkZ = Math.floorDiv(position.getZ(), 16);
            if (level.getChunkSource().getChunkNow(chunkX, chunkZ) == null) {
                PENDING_ORDER.add(key);
                continue;
            }

            spawnOne(level, pending.spawn());
            PENDING.remove(key, pending);
        }
    }

    private static List<PreparedSpawn> prepare(List<NorthStructureMarker> markers) {
        ArrayList<PreparedSpawn> prepared = new ArrayList<>();
        Map<String, Integer> occurrences = new HashMap<>();
        for (NorthStructureMarker marker : markers) {
            ParsedMarker parsed = parse(marker.role());
            RiverlandsNpcRole role = resolve(parsed.role());
            if (role == null) continue;

            String baseKey = marker.position().asLong() + ":" + parsed.serializedIdentity();
            int occurrence = occurrences.merge(baseKey, 1, Integer::sum) - 1;
            String populationKey = "riverlands:" + baseKey + ":" + occurrence;
            prepared.add(new PreparedSpawn(marker, parsed, role, populationKey));
        }
        return List.copyOf(prepared);
    }

    private static int spawnPrepared(ServerLevel level, List<PreparedSpawn> spawns) {
        int spawned = 0;
        for (PreparedSpawn spawn : spawns) if (spawnOne(level, spawn)) spawned++;
        return spawned;
    }

    private static boolean spawnOne(ServerLevel level, PreparedSpawn spawn) {
        NorthStructureMarker marker = spawn.marker();
        AABB duplicateBox = new AABB(marker.position()).inflate(32.0D);
        boolean exists = !level.getEntitiesOfClass(GOTRiverlandsNpcEntity.class, duplicateBox,
                npc -> spawn.populationKey().equals(npc.getPopulationKey())
                        || (spawn.role().legendary() && npc.getRole() == spawn.role())).isEmpty();
        if (exists) return false;

        BlockPos position = safePosition(level, marker.position(),
                spawn.role().collisionScale() > 1.0F);
        GOTRiverlandsNpcEntity npc = GOTEntities.RIVERLANDS_NPC.get().create(level);
        if (npc == null) return false;
        npc.moveTo(position.getX() + 0.5D, position.getY(), position.getZ() + 0.5D,
                marker.rotation() * 90.0F + 180.0F, 0.0F);
        npc.prepareForSpawn(spawn.role(), spawn.parsed().female(), spawn.parsed().child(),
                position, spawn.parsed().homeRadius(), spawn.populationKey());
        return level.noCollision(npc) && level.addFreshEntity(npc);
    }

    @Nullable
    private static RiverlandsNpcRole resolve(String markerRole) {
        String role = markerRole.toLowerCase(Locale.ROOT);
        if (role.startsWith("legendary_npc:")) {
            return RiverlandsNpcRole.findById(role.substring("legendary_npc:".length()));
        }
        if (role.startsWith("riverlands_market_trader_")) {
            try {
                return switch (Integer.parseInt(role.substring("riverlands_market_trader_".length()))) {
                    case 0 -> RiverlandsNpcRole.RIVERLANDS_GOLDSMITH;
                    case 1 -> RiverlandsNpcRole.RIVERLANDS_MINER;
                    case 2 -> RiverlandsNpcRole.RIVERLANDS_LUMBERMAN;
                    case 3 -> RiverlandsNpcRole.RIVERLANDS_MASON;
                    case 4 -> RiverlandsNpcRole.RIVERLANDS_BREWER;
                    case 5 -> RiverlandsNpcRole.RIVERLANDS_FLORIST;
                    case 6 -> RiverlandsNpcRole.RIVERLANDS_BUTCHER;
                    case 7 -> RiverlandsNpcRole.RIVERLANDS_FISHMONGER;
                    case 8 -> RiverlandsNpcRole.RIVERLANDS_FARMER;
                    case 9 -> RiverlandsNpcRole.RIVERLANDS_BLACKSMITH;
                    case 10 -> RiverlandsNpcRole.RIVERLANDS_BAKER;
                    default -> null;
                };
            } catch (NumberFormatException ignored) {
                return null;
            }
        }
        return switch (role) {
            case "riverlands_npc", "riverlands_civilian", "riverlands_patron",
                    "riverlands_bath_attendant", "riverlands_stablemaster" ->
                    RiverlandsNpcRole.RIVERLANDS_MAN;
            case "riverlands_soldier" -> RiverlandsNpcRole.RIVERLANDS_SOLDIER;
            case "riverlands_archer" -> RiverlandsNpcRole.RIVERLANDS_SOLDIER_ARCHER;
            case "riverlands_levyman" -> RiverlandsNpcRole.RIVERLANDS_LEVYMAN;
            case "riverlands_levyman_archer" -> RiverlandsNpcRole.RIVERLANDS_LEVYMAN_ARCHER;
            case "riverlands_gate_guard", "riverlands_guard", "riverlands_watchman",
                    "riverlands_sheriff" -> RiverlandsNpcRole.RIVERLANDS_SOLDIER;
            case "riverlands_banner_tully", "riverlands_banner_bearer" ->
                    RiverlandsNpcRole.RIVERLANDS_BANNER_BEARER;
            case "riverlands_captain", "riverlands_commander", "riverlands_market_master" ->
                    RiverlandsNpcRole.RIVERLANDS_CAPTAIN;
            case "riverlands_blacksmith" -> RiverlandsNpcRole.RIVERLANDS_BLACKSMITH;
            case "riverlands_goldsmith" -> RiverlandsNpcRole.RIVERLANDS_GOLDSMITH;
            case "riverlands_farmer" -> RiverlandsNpcRole.RIVERLANDS_FARMER;
            case "riverlands_farmhand" -> RiverlandsNpcRole.RIVERLANDS_FARMHAND;
            case "riverlands_bartender" -> RiverlandsNpcRole.RIVERLANDS_BARTENDER;
            case "riverlands_miner" -> RiverlandsNpcRole.RIVERLANDS_MINER;
            case "riverlands_lumberman" -> RiverlandsNpcRole.RIVERLANDS_LUMBERMAN;
            case "riverlands_mason" -> RiverlandsNpcRole.RIVERLANDS_MASON;
            case "riverlands_brewer" -> RiverlandsNpcRole.RIVERLANDS_BREWER;
            case "riverlands_florist" -> RiverlandsNpcRole.RIVERLANDS_FLORIST;
            case "riverlands_butcher" -> RiverlandsNpcRole.RIVERLANDS_BUTCHER;
            case "riverlands_fishmonger" -> RiverlandsNpcRole.RIVERLANDS_FISHMONGER;
            case "riverlands_baker" -> RiverlandsNpcRole.RIVERLANDS_BAKER;
            default -> RiverlandsNpcRole.findById(role);
        };
    }

    private static ParsedMarker parse(String serialized) {
        String[] fields = serialized.split("#");
        Boolean female = null;
        boolean child = false;
        int home = 16;
        for (int i = 1; i < fields.length; i++) {
            if (fields[i].equals("female")) female = true;
            else if (fields[i].equals("male")) female = false;
            else if (fields[i].equals("child")) child = true;
            else if (fields[i].startsWith("home=")) {
                try { home = Integer.parseInt(fields[i].substring(5)); }
                catch (NumberFormatException ignored) { }
            }
        }
        return new ParsedMarker(fields[0], female, child, Math.max(4, home));
    }

    private static BlockPos safePosition(ServerLevel level, BlockPos requested, boolean needsThirdBlock) {
        BlockPos.MutableBlockPos cursor = requested.mutable();
        for (int offset = 0; offset <= 24; offset++) {
            cursor.set(requested.getX(), requested.getY() + offset, requested.getZ());
            if (isStandingSpace(level, cursor, needsThirdBlock)) return cursor.immutable();
        }
        for (int offset = 1; offset <= 12; offset++) {
            cursor.set(requested.getX(), requested.getY() - offset, requested.getZ());
            if (isStandingSpace(level, cursor, needsThirdBlock)) return cursor.immutable();
        }
        int surface = level.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                requested.getX(), requested.getZ());
        return new BlockPos(requested.getX(), surface, requested.getZ());
    }

    private static boolean isStandingSpace(ServerLevel level, BlockPos position, boolean needsThirdBlock) {
        return level.getBlockState(position).getCollisionShape(level, position).isEmpty()
                && level.getBlockState(position.above()).getCollisionShape(level, position.above()).isEmpty()
                && (!needsThirdBlock || level.getBlockState(position.above(2))
                        .getCollisionShape(level, position.above(2)).isEmpty())
                && !level.getBlockState(position.below()).getCollisionShape(level, position.below()).isEmpty();
    }

    private static FixedSpawn fixed(GOTWaypoint waypoint, RiverlandsNpcRole role,
                                    int offsetX, int offsetZ) {
        return new FixedSpawn(waypoint, role, offsetX, offsetZ);
    }

    private static NorthFixedSpawn northFixed(GOTWaypoint waypoint, NorthNpcRole role,
                                              int offsetX, int offsetZ) {
        return new NorthFixedSpawn(waypoint, role, offsetX, offsetZ);
    }

    private record FixedSpawn(GOTWaypoint waypoint, RiverlandsNpcRole role,
                              int offsetX, int offsetZ) {}

    private record NorthFixedSpawn(GOTWaypoint waypoint, NorthNpcRole role,
                                   int offsetX, int offsetZ) {}

    private record ParsedMarker(String role, @Nullable Boolean female, boolean child, int homeRadius) {
        String serializedIdentity() {
            return role + ':' + (female == null ? "random" : female ? "female" : "male")
                    + ':' + child + ':' + homeRadius;
        }
    }

    private record PreparedSpawn(NorthStructureMarker marker, ParsedMarker parsed,
                                 RiverlandsNpcRole role, String populationKey) {}

    private record PendingKey(MinecraftServer server, ResourceKey<Level> dimension,
                              String populationKey) {}

    private record PendingSpawn(PreparedSpawn spawn) {}
}
