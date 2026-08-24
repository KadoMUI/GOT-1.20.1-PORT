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
 * Chunk-safe population and fixed-waypoint placement for Westerlands NPCs.
 * Entity creation is deferred out of WorldGenRegion to avoid the same
 * unfinished-chunk deadlock corrected during the Winterfell pass.
 */
@Mod.EventBusSubscriber(modid = GOTMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class GOTWesterlandsNpcPopulation {
    private static final int MAX_DEFERRED_SPAWNS_PER_TICK = 32;
    private static final Map<PendingKey, PendingSpawn> PENDING = new ConcurrentHashMap<>();
    private static final ConcurrentLinkedQueue<PendingKey> PENDING_ORDER = new ConcurrentLinkedQueue<>();

    private static final List<FixedSpawn> FIXED_SPAWNS = List.of(
            fixed(GOTWaypoint.ASHEMARK, WesterlandsNpcRole.ADDAM_MARBRAND, 0, 2),
            fixed(GOTWaypoint.BANEFORT, WesterlandsNpcRole.QUENTEN_BANEFORT, 0, 2),
            fixed(GOTWaypoint.CASTERLY_ROCK, WesterlandsNpcRole.TYWIN_LANNISTER, 2, 0),
            fixed(GOTWaypoint.CASTERLY_ROCK, WesterlandsNpcRole.QYBURN, -2, 0),
            fixed(GOTWaypoint.CLEGANES_KEEP, WesterlandsNpcRole.GREGOR_CLEGANE, 2, 0),
            fixed(GOTWaypoint.CLEGANES_KEEP, WesterlandsNpcRole.POLLIVER, -2, 0),
            fixed(GOTWaypoint.CORNFIELD, WesterlandsNpcRole.HARYS_SWYFT, 0, 2),
            fixed(GOTWaypoint.CRAKEHALL, WesterlandsNpcRole.LYLE_CRAKEHALL, 2, 2),
            fixed(GOTWaypoint.FAIRCASTLE, WesterlandsNpcRole.SEBASTON_FARMAN, 0, 2),
            fixed(GOTWaypoint.FEASTFIRES, WesterlandsNpcRole.FORLEY_PRESTER, 0, 2),
            fixed(GOTWaypoint.GOLDEN_TOOTH, WesterlandsNpcRole.LEO_LEFFORD, 2, 2),
            fixed(GOTWaypoint.HORNVALE, WesterlandsNpcRole.TYTOS_BRAX, 0, 2),
            fixed(GOTWaypoint.LANNISPORT, WesterlandsNpcRole.KEVAN_LANNISTER, 0, 4),
            fixed(GOTWaypoint.LANNISPORT, WesterlandsNpcRole.DAVEN_LANNISTER, 0, -4),
            fixed(GOTWaypoint.LANNISPORT, WesterlandsNpcRole.AMORY_LORCH, 4, 0)
    );

    private GOTWesterlandsNpcPopulation() {}

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
            String populationKey = "westerlands:fixed:" + fixed.waypoint().name().toLowerCase(Locale.ROOT)
                    + ':' + fixed.role().id();
            prepared.add(new PreparedSpawn(marker,
                    new ParsedMarker(fixed.role().id(), null, false, 16),
                    fixed.role(), populationKey));
        }
        enqueue(level.getLevel(), prepared);
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
            WesterlandsNpcRole role = resolve(parsed.role());
            if (role == null) continue;

            String baseKey = marker.position().asLong() + ":" + parsed.serializedIdentity();
            int occurrence = occurrences.merge(baseKey, 1, Integer::sum) - 1;
            String populationKey = "westerlands:" + baseKey + ":" + occurrence;
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
        boolean exists = !level.getEntitiesOfClass(GOTWesterlandsNpcEntity.class, duplicateBox,
                npc -> spawn.populationKey().equals(npc.getPopulationKey())
                        || (spawn.role().legendary() && npc.getRole() == spawn.role())).isEmpty();
        if (exists) return false;

        BlockPos position = safePosition(level, marker.position(),
                spawn.role().collisionScale() > 1.0F);
        GOTWesterlandsNpcEntity npc = GOTEntities.WESTERLANDS_NPC.get().create(level);
        if (npc == null) return false;
        npc.moveTo(position.getX() + 0.5D, position.getY(), position.getZ() + 0.5D,
                marker.rotation() * 90.0F + 180.0F, 0.0F);
        npc.prepareForSpawn(spawn.role(), spawn.parsed().female(), spawn.parsed().child(),
                position, spawn.parsed().homeRadius(), spawn.populationKey());
        return level.noCollision(npc) && level.addFreshEntity(npc);
    }

    @Nullable
    private static WesterlandsNpcRole resolve(String markerRole) {
        String role = markerRole.toLowerCase(Locale.ROOT);
        if (role.startsWith("legendary_npc:")) {
            return WesterlandsNpcRole.findById(role.substring("legendary_npc:".length()));
        }
        if (role.startsWith("westerlands_market_trader_")) {
            try {
                return switch (Integer.parseInt(role.substring("westerlands_market_trader_".length()))) {
                    case 0 -> WesterlandsNpcRole.WESTERLANDS_GOLDSMITH;
                    case 1 -> WesterlandsNpcRole.WESTERLANDS_MINER;
                    case 2 -> WesterlandsNpcRole.WESTERLANDS_LUMBERMAN;
                    case 3 -> WesterlandsNpcRole.WESTERLANDS_MASON;
                    case 4 -> WesterlandsNpcRole.WESTERLANDS_BREWER;
                    case 5 -> WesterlandsNpcRole.WESTERLANDS_FLORIST;
                    case 6 -> WesterlandsNpcRole.WESTERLANDS_BUTCHER;
                    case 7 -> WesterlandsNpcRole.WESTERLANDS_FISHMONGER;
                    case 8 -> WesterlandsNpcRole.WESTERLANDS_FARMER;
                    case 9 -> WesterlandsNpcRole.WESTERLANDS_BLACKSMITH;
                    case 10 -> WesterlandsNpcRole.WESTERLANDS_BAKER;
                    default -> null;
                };
            } catch (NumberFormatException ignored) {
                return null;
            }
        }
        return switch (role) {
            case "westerlands_npc", "westerlands_civilian", "westerlands_patron",
                    "westerlands_bath_attendant", "westerlands_stablemaster" ->
                    WesterlandsNpcRole.WESTERLANDS_MAN;
            case "westerlands_soldier" -> WesterlandsNpcRole.WESTERLANDS_SOLDIER;
            case "westerlands_archer" -> WesterlandsNpcRole.WESTERLANDS_SOLDIER_ARCHER;
            case "westerlands_levyman" -> WesterlandsNpcRole.WESTERLANDS_LEVYMAN;
            case "westerlands_levyman_archer" -> WesterlandsNpcRole.WESTERLANDS_LEVYMAN_ARCHER;
            case "westerlands_gate_guard", "westerlands_guard", "westerlands_watchman",
                    "westerlands_sheriff" -> WesterlandsNpcRole.WESTERLANDS_GUARD;
            case "westerlands_banner_lannister" -> WesterlandsNpcRole.WESTERLANDS_BANNER_BEARER;
            case "westerlands_captain", "westerlands_commander", "westerlands_market_master" ->
                    WesterlandsNpcRole.WESTERLANDS_CAPTAIN;
            case "westerlands_blacksmith" -> WesterlandsNpcRole.WESTERLANDS_BLACKSMITH;
            case "westerlands_goldsmith" -> WesterlandsNpcRole.WESTERLANDS_GOLDSMITH;
            case "westerlands_farmer" -> WesterlandsNpcRole.WESTERLANDS_FARMER;
            case "westerlands_farmhand" -> WesterlandsNpcRole.WESTERLANDS_FARMHAND;
            case "westerlands_bartender" -> WesterlandsNpcRole.WESTERLANDS_BARTENDER;
            case "westerlands_miner" -> WesterlandsNpcRole.WESTERLANDS_MINER;
            case "westerlands_lumberman" -> WesterlandsNpcRole.WESTERLANDS_LUMBERMAN;
            case "westerlands_mason" -> WesterlandsNpcRole.WESTERLANDS_MASON;
            case "westerlands_brewer" -> WesterlandsNpcRole.WESTERLANDS_BREWER;
            case "westerlands_florist" -> WesterlandsNpcRole.WESTERLANDS_FLORIST;
            case "westerlands_butcher" -> WesterlandsNpcRole.WESTERLANDS_BUTCHER;
            case "westerlands_fishmonger" -> WesterlandsNpcRole.WESTERLANDS_FISHMONGER;
            case "westerlands_baker" -> WesterlandsNpcRole.WESTERLANDS_BAKER;
            default -> WesterlandsNpcRole.findById(role);
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

    private static FixedSpawn fixed(GOTWaypoint waypoint, WesterlandsNpcRole role,
                                    int offsetX, int offsetZ) {
        return new FixedSpawn(waypoint, role, offsetX, offsetZ);
    }

    private record FixedSpawn(GOTWaypoint waypoint, WesterlandsNpcRole role,
                              int offsetX, int offsetZ) {}

    private record ParsedMarker(String role, @Nullable Boolean female, boolean child, int homeRadius) {
        String serializedIdentity() {
            return role + ':' + (female == null ? "random" : female ? "female" : "male")
                    + ':' + child + ':' + homeRadius;
        }
    }

    private record PreparedSpawn(NorthStructureMarker marker, ParsedMarker parsed,
                                 WesterlandsNpcRole role, String populationKey) {}

    private record PendingKey(MinecraftServer server, ResourceKey<Level> dimension,
                              String populationKey) {}

    private record PendingSpawn(PreparedSpawn spawn) {}
}
