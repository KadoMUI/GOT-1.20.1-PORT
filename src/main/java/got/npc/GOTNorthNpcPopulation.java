package got.npc;

import got.GOTMod;
import got.GOTEntities;
import got.world.structure.north.NorthStructureMarker;
import got.common.world.map.GOTWaypoint;
import got.world.terrain.PlanetosTerrainSampler;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.WorldGenLevel;
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

/** Resolves the population markers preserved by the northern structure port. */
@Mod.EventBusSubscriber(modid = GOTMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class GOTNorthNpcPopulation {
    private static final int MAX_DEFERRED_SPAWNS_PER_TICK = 32;
    private static final Map<PendingKey, PendingSpawn> PENDING = new ConcurrentHashMap<>();
    private static final ConcurrentLinkedQueue<PendingKey> PENDING_ORDER = new ConcurrentLinkedQueue<>();

    private GOTNorthNpcPopulation() {}
    private static final List<FixedNamedSpawn> FIXED_NAMED = List.of(
            new FixedNamedSpawn(GOTWaypoint.BARROWTOWN, NorthNpcRole.BARBREY_DUSTIN, 0, 3),
            new FixedNamedSpawn(GOTWaypoint.GREYWATER_WATCH, NorthNpcRole.HOWLAND_REED, 0, 5),
            new FixedNamedSpawn(GOTWaypoint.WHITE_HARBOUR, NorthNpcRole.WYMAN_MANDERLY, 0, 5)
    );

    /** Restores the three legacy North fixer characters not attached to authored structure markers. */
    public static void queueFixedSites(WorldGenLevel level, ChunkAccess chunk, long seed) {
        ArrayList<PreparedSpawn> prepared = new ArrayList<>();
        PlanetosTerrainSampler terrain = null;
        for (FixedNamedSpawn fixed : FIXED_NAMED) {
            int x = fixed.waypoint().getCoordX() + fixed.offsetX();
            int z = fixed.waypoint().getCoordZ() + fixed.offsetZ();
            if (Math.floorDiv(x, 16) != chunk.getPos().x || Math.floorDiv(z, 16) != chunk.getPos().z) continue;
            if (terrain == null) terrain = new PlanetosTerrainSampler(seed);
            int y = terrain.surfaceHeight(x, z) + 1;
            NorthStructureMarker marker = new NorthStructureMarker(
                    "legendary_npc:" + fixed.role().id(), new BlockPos(x, y, z), 0);
            ParsedMarker parsed = parse(marker.role());
            prepared.add(new PreparedSpawn(marker, parsed, fixed.role(),
                    "north:fixed:" + fixed.waypoint().name().toLowerCase(Locale.ROOT) + ':' + fixed.role().id()));
        }
        enqueue(level.getLevel(), prepared);
    }


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

    /**
     * WorldGenRegion runs feature placement on chunk worker threads.  Reading a
     * block or adding an entity through its backing ServerLevel from that path
     * can synchronously request the same unfinished chunk and deadlock chunk
     * generation.  Population is therefore delayed until the server tick sees
     * the target chunk as a fully loaded LevelChunk.
     */
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
            NorthNpcRole role = resolve(parsed.role());
            if (role == null) continue;

            String baseKey = marker.position().asLong() + ":" + parsed.serializedIdentity();
            int occurrence = occurrences.merge(baseKey, 1, Integer::sum) - 1;
            String populationKey = "north:" + baseKey + ":" + occurrence;
            prepared.add(new PreparedSpawn(marker, parsed, role, populationKey));
        }
        return List.copyOf(prepared);
    }

    private static int spawnPrepared(ServerLevel level, List<PreparedSpawn> spawns) {
        int spawned = 0;
        for (PreparedSpawn spawn : spawns) {
            if (spawnOne(level, spawn)) spawned++;
        }
        return spawned;
    }

    private static boolean spawnOne(ServerLevel level, PreparedSpawn spawn) {
        NorthStructureMarker marker = spawn.marker();
        AABB duplicateBox = new AABB(marker.position()).inflate(24.0D);
        boolean exists = !level.getEntitiesOfClass(GOTNorthNpcEntity.class, duplicateBox,
                npc -> spawn.populationKey().equals(npc.getPopulationKey())).isEmpty();
        if (exists) return false;

        BlockPos position = safePosition(level, marker.position());
        GOTNorthNpcEntity npc = GOTEntities.NORTH_NPC.get().create(level);
        if (npc == null) return false;
        npc.moveTo(position.getX() + 0.5D, position.getY(), position.getZ() + 0.5D,
                marker.rotation() * 90.0F + 180.0F, 0.0F);
        npc.prepareForSpawn(spawn.role(), spawn.parsed().female(), spawn.parsed().child(), position,
                spawn.parsed().homeRadius(), spawn.populationKey());
        boolean added = level.noCollision(npc) && level.addFreshEntity(npc);
        if (added && spawn.role().legendary()) {
            GOTNamedNpcRespawnData.get(level).register(npc, spawn.populationKey(), position);
        }
        return added;
    }

    @Nullable
    private static NorthNpcRole resolve(String markerRole) {
        String role = markerRole.toLowerCase(Locale.ROOT);
        if (role.startsWith("legendary_npc:")) {
            String legendary = role.substring("legendary_npc:".length());
            if (legendary.equals("rodrik_ryswel")) return NorthNpcRole.RODRIK_RYSWELL;
            return NorthNpcRole.findById(legendary);
        }
        if (role.startsWith("north_market_trader_")) {
            try {
                return switch (Integer.parseInt(role.substring("north_market_trader_".length()))) {
                    case 0 -> NorthNpcRole.NORTH_GOLDSMITH;
                    case 1 -> NorthNpcRole.NORTH_MINER;
                    case 2 -> NorthNpcRole.NORTH_LUMBERMAN;
                    case 3 -> NorthNpcRole.NORTH_MASON;
                    case 4 -> NorthNpcRole.NORTH_BREWER;
                    case 5 -> NorthNpcRole.NORTH_FLORIST;
                    case 6 -> NorthNpcRole.NORTH_BUTCHER;
                    case 7 -> NorthNpcRole.NORTH_FISHMONGER;
                    case 8 -> NorthNpcRole.NORTH_FARMER;
                    case 9 -> NorthNpcRole.NORTH_BLACKSMITH;
                    case 10 -> NorthNpcRole.NORTH_BAKER;
                    default -> null;
                };
            } catch (NumberFormatException ignored) {
                return null;
            }
        }
        return switch (role) {
            case "north_npc", "north_civilian", "north_patron", "north_bath_attendant",
                    "north_stablemaster" -> NorthNpcRole.NORTH_MAN;
            case "north_soldier" -> NorthNpcRole.NORTH_SOLDIER;
            case "north_archer" -> NorthNpcRole.NORTH_SOLDIER_ARCHER;
            case "north_levyman" -> NorthNpcRole.NORTH_LEVYMAN;
            case "north_levyman_archer" -> NorthNpcRole.NORTH_LEVYMAN_ARCHER;
            case "north_gate_guard", "north_guard", "north_watchman", "north_sheriff" -> NorthNpcRole.NORTH_GUARD;
            case "north_banner_stark" -> NorthNpcRole.NORTH_BANNER_BEARER;
            case "north_captain", "north_commander", "north_market_master" -> NorthNpcRole.NORTH_CAPTAIN;
            case "north_blacksmith" -> NorthNpcRole.NORTH_BLACKSMITH;
            case "north_goldsmith" -> NorthNpcRole.NORTH_GOLDSMITH;
            case "north_farmer" -> NorthNpcRole.NORTH_FARMER;
            case "north_farmhand" -> NorthNpcRole.NORTH_FARMHAND;
            case "north_bartender" -> NorthNpcRole.NORTH_BARTENDER;
            case "north_miner" -> NorthNpcRole.NORTH_MINER;
            case "north_lumberman" -> NorthNpcRole.NORTH_LUMBERMAN;
            case "north_mason" -> NorthNpcRole.NORTH_MASON;
            case "north_brewer" -> NorthNpcRole.NORTH_BREWER;
            case "north_florist" -> NorthNpcRole.NORTH_FLORIST;
            case "north_butcher" -> NorthNpcRole.NORTH_BUTCHER;
            case "north_fishmonger" -> NorthNpcRole.NORTH_FISHMONGER;
            case "north_baker" -> NorthNpcRole.NORTH_BAKER;
            case "north_hillman", "north_hillman_npc" -> NorthNpcRole.NORTH_HILLMAN;
            case "north_hillman_warrior" -> NorthNpcRole.NORTH_HILLMAN_WARRIOR;
            case "north_hillman_archer" -> NorthNpcRole.NORTH_HILLMAN_ARCHER;
            case "north_hillman_axe_thrower" -> NorthNpcRole.NORTH_HILLMAN_AXE_THROWER;
            case "north_hillman_banner_bearer" -> NorthNpcRole.NORTH_HILLMAN_BANNER_BEARER;
            case "north_hillman_chieftain", "north_hillman_chieftain_npc" -> NorthNpcRole.NORTH_HILLMAN_CHIEFTAIN;
            default -> null;
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

    private static BlockPos safePosition(ServerLevel level, BlockPos requested) {
        BlockPos.MutableBlockPos cursor = requested.mutable();
        for (int i = 0; i < 8; i++) {
            if (level.getBlockState(cursor).getCollisionShape(level, cursor).isEmpty()
                    && level.getBlockState(cursor.above()).getCollisionShape(level, cursor.above()).isEmpty()) {
                return cursor.immutable();
            }
            cursor.move(0, 1, 0);
        }
        return requested;
    }

    private record ParsedMarker(String role, @Nullable Boolean female, boolean child, int homeRadius) {
        String serializedIdentity() {
            return role + ':' + (female == null ? "random" : female ? "female" : "male")
                    + ':' + child + ':' + homeRadius;
        }
    }

    private record PreparedSpawn(NorthStructureMarker marker, ParsedMarker parsed,
                                 NorthNpcRole role, String populationKey) {}

    private record PendingKey(MinecraftServer server, ResourceKey<Level> dimension,
                              String populationKey) {}

    private record PendingSpawn(PreparedSpawn spawn) {}
    private record FixedNamedSpawn(GOTWaypoint waypoint, NorthNpcRole role, int offsetX, int offsetZ) {}
}
