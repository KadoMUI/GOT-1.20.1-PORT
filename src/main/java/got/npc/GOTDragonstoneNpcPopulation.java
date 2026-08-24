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
 * Chunk-safe population and fixed-waypoint placement for Dragonstone NPCs.
 * Entity creation is deferred out of WorldGenRegion to avoid the same
 * unfinished-chunk deadlock corrected during the Winterfell pass.
 */
@Mod.EventBusSubscriber(modid = GOTMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class GOTDragonstoneNpcPopulation {
    private static final int MAX_DEFERRED_SPAWNS_PER_TICK = 32;
    private static final Map<PendingKey, PendingSpawn> PENDING = new ConcurrentHashMap<>();
    private static final ConcurrentLinkedQueue<PendingKey> PENDING_ORDER = new ConcurrentLinkedQueue<>();

    private static final List<FixedSpawn> FIXED_SPAWNS = List.of(
            fixed(GOTWaypoint.CLAW_ISLE, DragonstoneNpcRole.ARDRIAN_CELTIGAR, 0, 2),
            fixed(GOTWaypoint.DRAGONSTONE, DragonstoneNpcRole.STANNIS_BARATHEON, -2, -2),
            fixed(GOTWaypoint.DRAGONSTONE, DragonstoneNpcRole.DAVOS_SEAWORTH, 2, 2),
            fixed(GOTWaypoint.DRAGONSTONE, DragonstoneNpcRole.MELISANDRA, -2, 2),
            fixed(GOTWaypoint.DRAGONSTONE, DragonstoneNpcRole.SHIREEN_BARATHEON, 2, -2),
            fixed(GOTWaypoint.DRAGONSTONE, DragonstoneNpcRole.SELYSE_BARATHEON, 0, 2),
            fixed(GOTWaypoint.DRAGONSTONE, DragonstoneNpcRole.MATTHOS_SEAWORTH, 0, -2),
            fixed(GOTWaypoint.DRIFTMARK, DragonstoneNpcRole.MONFORD_VELARYON, -2, -2),
            fixed(GOTWaypoint.DRIFTMARK, DragonstoneNpcRole.AURANE_WATERS, 2, 2)
    );

    private static final List<NorthFixedSpawn> NORTH_FIXED_SPAWNS = List.of();

    private GOTDragonstoneNpcPopulation() {}

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
            String populationKey = "dragonstone:fixed:" + fixed.waypoint().name().toLowerCase(Locale.ROOT)
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
            DragonstoneNpcRole role = resolve(parsed.role());
            if (role == null) continue;

            String baseKey = marker.position().asLong() + ":" + parsed.serializedIdentity();
            int occurrence = occurrences.merge(baseKey, 1, Integer::sum) - 1;
            String populationKey = "dragonstone:" + baseKey + ":" + occurrence;
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
        boolean exists = !level.getEntitiesOfClass(GOTDragonstoneNpcEntity.class, duplicateBox,
                npc -> spawn.populationKey().equals(npc.getPopulationKey())
                        || (spawn.role().legendary() && npc.getRole() == spawn.role())).isEmpty();
        if (exists) return false;

        BlockPos position = safePosition(level, marker.position(),
                spawn.role().collisionScale() > 1.0F);
        GOTDragonstoneNpcEntity npc = GOTEntities.DRAGONSTONE_NPC.get().create(level);
        if (npc == null) return false;
        npc.moveTo(position.getX() + 0.5D, position.getY(), position.getZ() + 0.5D,
                marker.rotation() * 90.0F + 180.0F, 0.0F);
        npc.prepareForSpawn(spawn.role(), spawn.parsed().female(), spawn.parsed().child(),
                position, spawn.parsed().homeRadius(), spawn.populationKey());
        return level.noCollision(npc) && level.addFreshEntity(npc);
    }

    @Nullable
    private static DragonstoneNpcRole resolve(String markerRole) {
        String role = markerRole.toLowerCase(Locale.ROOT);
        if (role.startsWith("legendary_npc:")) {
            return DragonstoneNpcRole.findById(role.substring("legendary_npc:".length()));
        }
        if (role.startsWith("dragonstone_market_trader_")) {
            try {
                return switch (Integer.parseInt(role.substring("dragonstone_market_trader_".length()))) {
                    case 0 -> DragonstoneNpcRole.DRAGONSTONE_GOLDSMITH;
                    case 1 -> DragonstoneNpcRole.DRAGONSTONE_MINER;
                    case 2 -> DragonstoneNpcRole.DRAGONSTONE_LUMBERMAN;
                    case 3 -> DragonstoneNpcRole.DRAGONSTONE_MASON;
                    case 4 -> DragonstoneNpcRole.DRAGONSTONE_BREWER;
                    case 5 -> DragonstoneNpcRole.DRAGONSTONE_FLORIST;
                    case 6 -> DragonstoneNpcRole.DRAGONSTONE_BUTCHER;
                    case 7 -> DragonstoneNpcRole.DRAGONSTONE_FISHMONGER;
                    case 8 -> DragonstoneNpcRole.DRAGONSTONE_FARMER;
                    case 9 -> DragonstoneNpcRole.DRAGONSTONE_BLACKSMITH;
                    case 10 -> DragonstoneNpcRole.DRAGONSTONE_BAKER;
                    default -> null;
                };
            } catch (NumberFormatException ignored) {
                return null;
            }
        }
        return switch (role) {
            case "dragonstone_npc", "dragonstone_civilian", "dragonstone_patron",
                    "dragonstone_bath_attendant", "dragonstone_stablemaster" ->
                    DragonstoneNpcRole.DRAGONSTONE_MAN;
            case "dragonstone_soldier" -> DragonstoneNpcRole.DRAGONSTONE_SOLDIER;
            case "dragonstone_archer" -> DragonstoneNpcRole.DRAGONSTONE_SOLDIER_ARCHER;
            case "dragonstone_levyman" -> DragonstoneNpcRole.DRAGONSTONE_LEVYMAN;
            case "dragonstone_levyman_archer" -> DragonstoneNpcRole.DRAGONSTONE_LEVYMAN_ARCHER;
            case "dragonstone_gate_guard", "dragonstone_guard", "dragonstone_watchman",
                    "dragonstone_sheriff" -> DragonstoneNpcRole.DRAGONSTONE_SOLDIER;
            case "dragonstone_banner_stannis", "dragonstone_banner_bearer" ->
                    DragonstoneNpcRole.DRAGONSTONE_BANNER_BEARER;
            case "dragonstone_captain", "dragonstone_commander", "dragonstone_market_master" ->
                    DragonstoneNpcRole.DRAGONSTONE_CAPTAIN;
            case "dragonstone_blacksmith" -> DragonstoneNpcRole.DRAGONSTONE_BLACKSMITH;
            case "dragonstone_goldsmith" -> DragonstoneNpcRole.DRAGONSTONE_GOLDSMITH;
            case "dragonstone_farmer" -> DragonstoneNpcRole.DRAGONSTONE_FARMER;
            case "dragonstone_farmhand" -> DragonstoneNpcRole.DRAGONSTONE_FARMHAND;
            case "dragonstone_bartender" -> DragonstoneNpcRole.DRAGONSTONE_BARTENDER;
            case "dragonstone_miner" -> DragonstoneNpcRole.DRAGONSTONE_MINER;
            case "dragonstone_lumberman" -> DragonstoneNpcRole.DRAGONSTONE_LUMBERMAN;
            case "dragonstone_mason" -> DragonstoneNpcRole.DRAGONSTONE_MASON;
            case "dragonstone_brewer" -> DragonstoneNpcRole.DRAGONSTONE_BREWER;
            case "dragonstone_florist" -> DragonstoneNpcRole.DRAGONSTONE_FLORIST;
            case "dragonstone_butcher" -> DragonstoneNpcRole.DRAGONSTONE_BUTCHER;
            case "dragonstone_fishmonger" -> DragonstoneNpcRole.DRAGONSTONE_FISHMONGER;
            case "dragonstone_baker" -> DragonstoneNpcRole.DRAGONSTONE_BAKER;
            default -> DragonstoneNpcRole.findById(role);
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

    private static FixedSpawn fixed(GOTWaypoint waypoint, DragonstoneNpcRole role,
                                    int offsetX, int offsetZ) {
        return new FixedSpawn(waypoint, role, offsetX, offsetZ);
    }

    private static NorthFixedSpawn northFixed(GOTWaypoint waypoint, NorthNpcRole role,
                                              int offsetX, int offsetZ) {
        return new NorthFixedSpawn(waypoint, role, offsetX, offsetZ);
    }

    private record FixedSpawn(GOTWaypoint waypoint, DragonstoneNpcRole role,
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
                                 DragonstoneNpcRole role, String populationKey) {}

    private record PendingKey(MinecraftServer server, ResourceKey<Level> dimension,
                              String populationKey) {}

    private record PendingSpawn(PreparedSpawn spawn) {}
}
