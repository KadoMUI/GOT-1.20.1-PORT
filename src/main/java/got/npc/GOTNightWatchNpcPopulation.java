package got.npc;

import got.GOTMod;
import got.GOTEntities;
import got.world.structure.north.NorthStructureMarker;
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

/** Resolves the population markers preserved by the Night Watch structure port. */
@Mod.EventBusSubscriber(modid = GOTMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class GOTNightWatchNpcPopulation {
    private static final int MAX_DEFERRED_SPAWNS_PER_TICK = 32;
    private static final Map<PendingKey, PendingSpawn> PENDING = new ConcurrentHashMap<>();
    private static final ConcurrentLinkedQueue<PendingKey> PENDING_ORDER = new ConcurrentLinkedQueue<>();

    private GOTNightWatchNpcPopulation() {}

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
            NightWatchNpcRole role = resolve(parsed.role());
            if (role == null) continue;
            int count = marker.role().equals("gift_population_respawner") ? 4
                    : marker.role().equals("gift_archer_respawner") ? 2 : 1;
            for (int i=0;i<count;i++) {
                int dx=count==1?0:(i%3)*2-2, dz=count==1?0:(i/3)*2;
                NorthStructureMarker placed=new NorthStructureMarker(marker.role(),marker.position().offset(dx,0,dz),marker.rotation());
                String baseKey=marker.position().asLong()+":"+parsed.serializedIdentity();
                int occurrence=occurrences.merge(baseKey,1,Integer::sum)-1;
                String populationKey="night_watch:"+baseKey+":"+occurrence;
                prepared.add(new PreparedSpawn(placed,parsed,role,populationKey));
            }
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
        boolean exists = !level.getEntitiesOfClass(GOTNightWatchNpcEntity.class, duplicateBox,
                npc -> spawn.populationKey().equals(npc.getPopulationKey())).isEmpty();
        if (exists) return false;

        BlockPos position = safePosition(level, marker.position());
        GOTNightWatchNpcEntity npc = GOTEntities.NIGHT_WATCH_NPC.get().create(level);
        if (npc == null) return false;
        npc.moveTo(position.getX() + 0.5D, position.getY(), position.getZ() + 0.5D,
                marker.rotation() * 90.0F + 180.0F, 0.0F);
        npc.prepareForSpawn(spawn.role(), spawn.parsed().female(), spawn.parsed().child(), position,
                spawn.parsed().homeRadius(), spawn.populationKey());
        boolean added=level.noCollision(npc)&&level.addFreshEntity(npc);
        if(added&&marker.role().endsWith("_respawner"))GOTNpcRespawnerData.get(level).register(
                "night_watch",spawn.role().id(),spawn.populationKey(),position,
                marker.rotation()*90.0F+180.0F,spawn.parsed().female(),spawn.parsed().child(),spawn.parsed().homeRadius());
        return added;
    }

    @Nullable
    private static NightWatchNpcRole resolve(String markerRole) {
        String role = markerRole.toLowerCase(Locale.ROOT);
        return switch (role) {
            case "gift_civilian","gift_child","gift_stablemaster" -> NightWatchNpcRole.GIFT_MAN;
            case "gift_guard","gift_population_respawner" -> NightWatchNpcRole.GIFT_GUARD;
            case "gift_archer","gift_archer_respawner" -> NightWatchNpcRole.GIFT_ARCHER;
            case "gift_banner_night" -> NightWatchNpcRole.GIFT_BANNER_BEARER;
            case "gift_blacksmith" -> NightWatchNpcRole.GIFT_BLACKSMITH;
            case "jeor_mormont" -> NightWatchNpcRole.JEOR_MORMONT;
            case "jon_snow" -> NightWatchNpcRole.JON_SNOW;
            case "aemon_targaryen" -> NightWatchNpcRole.AEMON_TARGARYEN;
            case "alliser_thorne" -> NightWatchNpcRole.ALLISER_THORNE;
            case "edd" -> NightWatchNpcRole.EDD;
            case "samwell_tarly" -> NightWatchNpcRole.SAMWELL_TARLY;
            case "cotter_pyke" -> NightWatchNpcRole.COTTER_PYKE;
            case "harmune" -> NightWatchNpcRole.HARMUNE;
            case "denys_mallister" -> NightWatchNpcRole.DENYS_MALLISTER;
            case "mullin" -> NightWatchNpcRole.MULLIN;
            case "benjen_stark" -> NightWatchNpcRole.BENJEN_STARK;
            case "yoren" -> NightWatchNpcRole.YOREN;
            default -> NightWatchNpcRole.findById(role);
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
        if (fields[0].equals("gift_child")) child = true;
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
                                 NightWatchNpcRole role, String populationKey) {}

    private record PendingKey(MinecraftServer server, ResourceKey<Level> dimension,
                              String populationKey) {}

    private record PendingSpawn(PreparedSpawn spawn) {}
}
