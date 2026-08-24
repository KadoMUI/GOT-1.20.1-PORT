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
 * Chunk-safe population and fixed-waypoint placement for Crownlands NPCs.
 * Entity creation is deferred out of WorldGenRegion to avoid the same
 * unfinished-chunk deadlock corrected during the Winterfell pass.
 */
@Mod.EventBusSubscriber(modid = GOTMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class GOTCrownlandsNpcPopulation {
    private static final int MAX_DEFERRED_SPAWNS_PER_TICK = 32;
    private static final Map<PendingKey, PendingSpawn> PENDING = new ConcurrentHashMap<>();
    private static final ConcurrentLinkedQueue<PendingKey> PENDING_ORDER = new ConcurrentLinkedQueue<>();

    private static final List<FixedSpawn> FIXED_SPAWNS = List.of(
            fixed(GOTWaypoint.KINGS_LANDING, CrownlandsNpcRole.SANSA_STARK, 0, 5),
            fixed(GOTWaypoint.KINGS_LANDING, CrownlandsNpcRole.SHAE, 1, 6),
            fixed(GOTWaypoint.KINGS_LANDING, CrownlandsNpcRole.YOREN, -1, 4),
            fixed(GOTWaypoint.KINGS_LANDING, CrownlandsNpcRole.SANDOR_CLEGANE, -14, -4),
            fixed(GOTWaypoint.KINGS_LANDING, CrownlandsNpcRole.JOFFREY_BARATHEON, -10, -4),
            fixed(GOTWaypoint.KINGS_LANDING, CrownlandsNpcRole.CERSEI_LANNISTER, -10, 4),
            fixed(GOTWaypoint.KINGS_LANDING, CrownlandsNpcRole.JAIME_LANNISTER, -14, 4),
            fixed(GOTWaypoint.KINGS_LANDING, CrownlandsNpcRole.PYCELLE, -10, 0),
            fixed(GOTWaypoint.KINGS_LANDING, CrownlandsNpcRole.JANOS_SLYNT, -14, 0),
            fixed(GOTWaypoint.KINGS_LANDING, CrownlandsNpcRole.VARYS, -12, 4),
            fixed(GOTWaypoint.KINGS_LANDING, CrownlandsNpcRole.ILYN_PAYNE, -12, -4),
            fixed(GOTWaypoint.KINGS_LANDING, CrownlandsNpcRole.HIGH_SEPTON, -13, 4),
            fixed(GOTWaypoint.KINGS_LANDING, CrownlandsNpcRole.TOMMEN_BARATHEON, -14, 2),
            fixed(GOTWaypoint.KINGS_LANDING, CrownlandsNpcRole.MYRCELLA_BARATHEON, -13, -4),
            fixed(GOTWaypoint.KINGS_LANDING, CrownlandsNpcRole.MERYN_TRANT, -13, 2),
            fixed(GOTWaypoint.KINGS_LANDING, CrownlandsNpcRole.BARRISTAN_SELMY, -13, 0),
            fixed(GOTWaypoint.KINGS_LANDING, CrownlandsNpcRole.PETYR_BAELISH, 10, 7),
            fixed(GOTWaypoint.KINGS_LANDING, CrownlandsNpcRole.TYRION_LANNISTER, 12, 7),
            fixed(GOTWaypoint.KINGS_LANDING, CrownlandsNpcRole.LANCEL_LANNISTER, 14, 7),
            fixed(GOTWaypoint.KINGS_LANDING, CrownlandsNpcRole.BRONN, 11, 8),
            fixed(GOTWaypoint.KINGS_LANDING, CrownlandsNpcRole.PODRICK_PAYNE, 13, 8),
            fixed(GOTWaypoint.KINGS_LANDING, CrownlandsNpcRole.TOBHO_MOTT, 10, -7),
            fixed(GOTWaypoint.KINGS_LANDING, CrownlandsNpcRole.GENDRY_BARATHEON, 12, -7)
    );

    private static final List<NorthFixedSpawn> NORTH_FIXED_SPAWNS = List.of();

    private GOTCrownlandsNpcPopulation() {}

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
            String populationKey = "crownlands:fixed:" + fixed.waypoint().name().toLowerCase(Locale.ROOT)
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
            CrownlandsNpcRole role = resolve(parsed.role());
            if (role == null) continue;

            String baseKey = marker.position().asLong() + ":" + parsed.serializedIdentity();
            int occurrence = occurrences.merge(baseKey, 1, Integer::sum) - 1;
            String populationKey = "crownlands:" + baseKey + ":" + occurrence;
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
        boolean exists = !level.getEntitiesOfClass(GOTCrownlandsNpcEntity.class, duplicateBox,
                npc -> spawn.populationKey().equals(npc.getPopulationKey())
                        || (spawn.role().legendary() && npc.getRole() == spawn.role())).isEmpty();
        if (exists) return false;

        BlockPos position = safePosition(level, marker.position(),
                spawn.role().collisionScale() > 1.0F);
        GOTCrownlandsNpcEntity npc = GOTEntities.CROWNLANDS_NPC.get().create(level);
        if (npc == null) return false;
        npc.moveTo(position.getX() + 0.5D, position.getY(), position.getZ() + 0.5D,
                marker.rotation() * 90.0F + 180.0F, 0.0F);
        npc.prepareForSpawn(spawn.role(), spawn.parsed().female(), spawn.parsed().child(),
                position, spawn.parsed().homeRadius(), spawn.populationKey());
        return level.noCollision(npc) && level.addFreshEntity(npc);
    }

    @Nullable
    private static CrownlandsNpcRole resolve(String markerRole) {
        String role = markerRole.toLowerCase(Locale.ROOT);
        if (role.startsWith("legendary_npc:")) {
            return CrownlandsNpcRole.findById(role.substring("legendary_npc:".length()));
        }
        if (role.startsWith("crownlands_market_trader_")) {
            try {
                return switch (Integer.parseInt(role.substring("crownlands_market_trader_".length()))) {
                    case 0 -> CrownlandsNpcRole.CROWNLANDS_GOLDSMITH;
                    case 1 -> CrownlandsNpcRole.CROWNLANDS_MINER;
                    case 2 -> CrownlandsNpcRole.CROWNLANDS_LUMBERMAN;
                    case 3 -> CrownlandsNpcRole.CROWNLANDS_MASON;
                    case 4 -> CrownlandsNpcRole.CROWNLANDS_BREWER;
                    case 5 -> CrownlandsNpcRole.CROWNLANDS_FLORIST;
                    case 6 -> CrownlandsNpcRole.CROWNLANDS_BUTCHER;
                    case 7 -> CrownlandsNpcRole.CROWNLANDS_FISHMONGER;
                    case 8 -> CrownlandsNpcRole.CROWNLANDS_FARMER;
                    case 9 -> CrownlandsNpcRole.CROWNLANDS_BLACKSMITH;
                    case 10 -> CrownlandsNpcRole.CROWNLANDS_BAKER;
                    default -> null;
                };
            } catch (NumberFormatException ignored) {
                return null;
            }
        }
        return switch (role) {
            case "crownlands_npc", "crownlands_civilian", "crownlands_patron",
                    "crownlands_bath_attendant", "crownlands_stablemaster" ->
                    CrownlandsNpcRole.CROWNLANDS_MAN;
            case "crownlands_soldier", "crownlands_archer" -> CrownlandsNpcRole.CROWNLANDS_GUARD;
            case "crownlands_levyman" -> CrownlandsNpcRole.CROWNLANDS_LEVYMAN;
            case "crownlands_levyman_archer" -> CrownlandsNpcRole.CROWNLANDS_LEVYMAN_ARCHER;
            case "crownlands_gate_guard", "crownlands_guard", "crownlands_watchman",
                    "crownlands_sheriff" -> CrownlandsNpcRole.CROWNLANDS_GUARD;
            case "kingsguard" -> CrownlandsNpcRole.KINGSGUARD;
            case "crownlands_captain", "crownlands_commander", "crownlands_market_master" ->
                    CrownlandsNpcRole.CROWNLANDS_CAPTAIN;
            case "crownlands_blacksmith" -> CrownlandsNpcRole.CROWNLANDS_BLACKSMITH;
            case "crownlands_alchemist" -> CrownlandsNpcRole.CROWNLANDS_ALCHEMIST;
            case "crownlands_goldsmith" -> CrownlandsNpcRole.CROWNLANDS_GOLDSMITH;
            case "crownlands_farmer" -> CrownlandsNpcRole.CROWNLANDS_FARMER;
            case "crownlands_farmhand" -> CrownlandsNpcRole.CROWNLANDS_FARMHAND;
            case "crownlands_bartender" -> CrownlandsNpcRole.CROWNLANDS_BARTENDER;
            case "crownlands_miner" -> CrownlandsNpcRole.CROWNLANDS_MINER;
            case "crownlands_lumberman" -> CrownlandsNpcRole.CROWNLANDS_LUMBERMAN;
            case "crownlands_mason" -> CrownlandsNpcRole.CROWNLANDS_MASON;
            case "crownlands_brewer" -> CrownlandsNpcRole.CROWNLANDS_BREWER;
            case "crownlands_florist" -> CrownlandsNpcRole.CROWNLANDS_FLORIST;
            case "crownlands_butcher" -> CrownlandsNpcRole.CROWNLANDS_BUTCHER;
            case "crownlands_fishmonger" -> CrownlandsNpcRole.CROWNLANDS_FISHMONGER;
            case "crownlands_baker" -> CrownlandsNpcRole.CROWNLANDS_BAKER;
            default -> CrownlandsNpcRole.findById(role);
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

    private static FixedSpawn fixed(GOTWaypoint waypoint, CrownlandsNpcRole role,
                                    int offsetX, int offsetZ) {
        return new FixedSpawn(waypoint, role, offsetX, offsetZ);
    }

    private static NorthFixedSpawn northFixed(GOTWaypoint waypoint, NorthNpcRole role,
                                              int offsetX, int offsetZ) {
        return new NorthFixedSpawn(waypoint, role, offsetX, offsetZ);
    }

    private record FixedSpawn(GOTWaypoint waypoint, CrownlandsNpcRole role,
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
                                 CrownlandsNpcRole role, String populationKey) {}

    private record PendingKey(MinecraftServer server, ResourceKey<Level> dimension,
                              String populationKey) {}

    private record PendingSpawn(PreparedSpawn spawn) {}
}
