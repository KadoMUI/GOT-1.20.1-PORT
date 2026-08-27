package got.npc;

import got.GOTEntities;
import got.GOTMod;
import got.common.world.map.GOTWaypoint;
import got.world.terrain.PlanetosTerrainSampler;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Chunk-safe fixed-site placement for named Free Cities and eastern Essos characters
 * registered by the final 1.7.10 waypoint fixer.
 */
@Mod.EventBusSubscriber(modid = GOTMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class GOTFreeCitiesNpcPopulation {
    private static final int MAX_DEFERRED_SPAWNS_PER_TICK = 16;
    private static final Map<PendingKey, PendingSpawn> PENDING = new ConcurrentHashMap<>();
    private static final ConcurrentLinkedQueue<PendingKey> PENDING_ORDER =
            new ConcurrentLinkedQueue<>();

    private static final List<FixedSpawn> FIXED_SPAWNS = List.of(
            fixed(GOTWaypoint.BRAAVOS, City.BRAAVOS, "tycho_nestoris", 0, 1),
            fixed(GOTWaypoint.PENTOS, City.PENTOS, "illyrio_mopatis", 3, 0),
            fixed(GOTWaypoint.LYS, City.LYS, "salladhor_saan", 0, -1),
            fixed(GOTWaypoint.TYROSH, City.TYROSH, "jon_connington", 0, -1),
            // The two Tyroshi characters shared the same legacy offset. Keep
            // them together without forcing their 1.20.1 collision boxes to overlap.
            fixed(GOTWaypoint.TYROSH, City.TYROSH, "young_griff", 1, -1)
            ,fixed(GOTWaypoint.ASTAPOR, City.GHISCAR, "kraznys_mo_nakloz", -1, 0)
            ,fixed(GOTWaypoint.ASTAPOR, City.GHISCAR, "missandei", -1, -1)
            ,fixed(GOTWaypoint.ASTAPOR, City.GHISCAR, "grey_worm", -1, 1)
            ,fixed(GOTWaypoint.MEEREEN, City.GHISCAR, "hizdahr_zo_loraq", -1, -1)
            ,fixed(GOTWaypoint.YUNKAI, City.GHISCAR, "daario_naharis", -1, 0)
            ,fixed(GOTWaypoint.YUNKAI, City.GHISCAR, "razdal_mo_eraz", -1, 1)
            ,fixed(GOTWaypoint.VAES_EFE, City.DOTHRAKI, "daenerys_targaryen", 0, 3)
            // Legacy placed both at one block; keep Jorah beside Daenerys in 1.20.1.
            ,fixed(GOTWaypoint.VAES_EFE, City.DOTHRAKI, "jorah_mormont", 1, 3)
            ,fixed(GOTWaypoint.YIN, City.YI_TI, "bu_gai", 12, 0)
            ,fixed(GOTWaypoint.ASSHAI, City.ASSHAI, "asshai_archmag", 0, 0)
            ,fixed(GOTWaypoint.VOLANTIS, City.ASSHAI, "moqorro", -1, 0)
            ,fixed(GOTWaypoint.QARTH, City.QARTH, "xaro_xhoan_daxos", 3, 0)
            ,fixed(GOTWaypoint.HOJDBAATAR, City.JOGOS_NHAI, "tugar_khan", 0, 3)
            ,fixed(GOTWaypoint.MYR, City.GOLDEN_COMPANY, "harry_strickland", -1, -1)
            ,fixed(GOTWaypoint.NORVOS, City.NORVOS, "mellario", 0, 1)
    );

    private GOTFreeCitiesNpcPopulation() {}

    /** Queues only the fixed characters whose target positions belong to this chunk. */
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
            String populationKey = "free_cities:fixed:"
                    + fixed.waypoint().name().toLowerCase(Locale.ROOT) + ':' + fixed.roleId();
            prepared.add(new PreparedSpawn(fixed.city(), fixed.roleId(),
                    new BlockPos(x, y, z), populationKey));
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
            BlockPos position = pending.spawn().position();
            if (level.getChunkSource().getChunkNow(Math.floorDiv(position.getX(), 16),
                    Math.floorDiv(position.getZ(), 16)) == null) {
                PENDING_ORDER.add(key);
                continue;
            }
            spawnOne(level, pending.spawn());
            PENDING.remove(key, pending);
        }
    }

    private static boolean spawnOne(ServerLevel level, PreparedSpawn spawn) {
        BlockPos position = safePosition(level, spawn.position());
        return switch (spawn.city()) {
            case BRAAVOS -> spawnBraavosi(level, position, spawn);
            case PENTOS -> spawnPentoshi(level, position, spawn);
            case LYS -> spawnLysene(level, position, spawn);
            case TYROSH -> spawnTyroshi(level, position, spawn);
            case GHISCAR -> spawnGhiscari(level, position, spawn);
            case DOTHRAKI -> spawnDothraki(level, position, spawn);
            case YI_TI -> spawnYiTi(level, position, spawn);
            case ASSHAI -> spawnAsshai(level, position, spawn);
            case QARTH -> spawnQarth(level, position, spawn);
            case JOGOS_NHAI -> spawnJogosNhai(level, position, spawn);
            case GOLDEN_COMPANY -> spawnGoldenCompany(level, position, spawn);
            case NORVOS -> spawnNorvos(level, position, spawn);
        };
    }

    private static boolean spawnBraavosi(ServerLevel level, BlockPos position,
                                          PreparedSpawn spawn) {
        BraavosNpcRole role = BraavosNpcRole.findById(spawn.roleId());
        if (role == null || existsBraavosi(level, position, spawn.populationKey(), role)) return false;
        GOTBraavosNpcEntity npc = GOTEntities.BRAAVOS_NPC.get().create(level);
        if (npc == null) return false;
        npc.moveTo(position.getX() + 0.5D, position.getY(), position.getZ() + 0.5D, 180.0F, 0.0F);
        npc.prepareForSpawn(role, false, false, position, 16, spawn.populationKey());
        return level.noCollision(npc) && level.addFreshEntity(npc);
    }

    private static boolean existsBraavosi(ServerLevel level, BlockPos position, String key,
                                           BraavosNpcRole role) {
        return !level.getEntitiesOfClass(GOTBraavosNpcEntity.class,
                new AABB(position).inflate(32.0D), npc -> key.equals(npc.getPopulationKey())
                        || (role.legendary() && npc.getRole() == role)).isEmpty();
    }

    private static boolean spawnPentoshi(ServerLevel level, BlockPos position,
                                          PreparedSpawn spawn) {
        PentosNpcRole role = PentosNpcRole.findById(spawn.roleId());
        if (role == null || existsPentoshi(level, position, spawn.populationKey(), role)) return false;
        GOTPentosNpcEntity npc = GOTEntities.PENTOS_NPC.get().create(level);
        if (npc == null) return false;
        npc.moveTo(position.getX() + 0.5D, position.getY(), position.getZ() + 0.5D, 180.0F, 0.0F);
        npc.prepareForSpawn(role, false, false, position, 16, spawn.populationKey());
        return level.noCollision(npc) && level.addFreshEntity(npc);
    }

    private static boolean existsPentoshi(ServerLevel level, BlockPos position, String key,
                                           PentosNpcRole role) {
        return !level.getEntitiesOfClass(GOTPentosNpcEntity.class,
                new AABB(position).inflate(32.0D), npc -> key.equals(npc.getPopulationKey())
                        || (role.legendary() && npc.getRole() == role)).isEmpty();
    }

    private static boolean spawnLysene(ServerLevel level, BlockPos position,
                                        PreparedSpawn spawn) {
        LysNpcRole role = LysNpcRole.findById(spawn.roleId());
        if (role == null || existsLysene(level, position, spawn.populationKey(), role)) return false;
        GOTLysNpcEntity npc = GOTEntities.LYS_NPC.get().create(level);
        if (npc == null) return false;
        npc.moveTo(position.getX() + 0.5D, position.getY(), position.getZ() + 0.5D, 180.0F, 0.0F);
        npc.prepareForSpawn(role, false, false, position, 16, spawn.populationKey());
        return level.noCollision(npc) && level.addFreshEntity(npc);
    }

    private static boolean existsLysene(ServerLevel level, BlockPos position, String key,
                                         LysNpcRole role) {
        return !level.getEntitiesOfClass(GOTLysNpcEntity.class,
                new AABB(position).inflate(32.0D), npc -> key.equals(npc.getPopulationKey())
                        || (role.legendary() && npc.getRole() == role)).isEmpty();
    }

    private static boolean spawnTyroshi(ServerLevel level, BlockPos position,
                                         PreparedSpawn spawn) {
        TyroshNpcRole role = TyroshNpcRole.findById(spawn.roleId());
        if (role == null || existsTyroshi(level, position, spawn.populationKey(), role)) return false;
        GOTTyroshNpcEntity npc = GOTEntities.TYROSH_NPC.get().create(level);
        if (npc == null) return false;
        npc.moveTo(position.getX() + 0.5D, position.getY(), position.getZ() + 0.5D, 180.0F, 0.0F);
        npc.prepareForSpawn(role, false, false, position, 16, spawn.populationKey());
        return level.noCollision(npc) && level.addFreshEntity(npc);
    }

    private static boolean existsTyroshi(ServerLevel level, BlockPos position, String key,
                                          TyroshNpcRole role) {
        return !level.getEntitiesOfClass(GOTTyroshNpcEntity.class,
                new AABB(position).inflate(32.0D), npc -> key.equals(npc.getPopulationKey())
                        || (role.legendary() && npc.getRole() == role)).isEmpty();
    }

    private static boolean spawnGhiscari(ServerLevel level, BlockPos position, PreparedSpawn spawn) {
        GhiscarNpcRole role = GhiscarNpcRole.findById(spawn.roleId());
        if (role == null || !level.getEntitiesOfClass(GOTGhiscarNpcEntity.class,
                new AABB(position).inflate(32.0D), npc -> spawn.populationKey().equals(npc.getPopulationKey())
                        || (role.legendary() && npc.getRole() == role)).isEmpty()) return false;
        GOTGhiscarNpcEntity npc = GOTEntities.GHISCAR_NPC.get().create(level);
        if (npc == null) return false;
        npc.moveTo(position.getX() + 0.5D, position.getY(), position.getZ() + 0.5D, 180.0F, 0.0F);
        npc.prepareForSpawn(role, null, false, position, 16, spawn.populationKey());
        return level.noCollision(npc) && level.addFreshEntity(npc);
    }

    private static boolean spawnDothraki(ServerLevel level, BlockPos position, PreparedSpawn spawn) {
        DothrakiNpcRole role = DothrakiNpcRole.findById(spawn.roleId());
        if (role == null || !level.getEntitiesOfClass(GOTDothrakiNpcEntity.class,
                new AABB(position).inflate(32.0D), npc -> spawn.populationKey().equals(npc.getPopulationKey())
                        || (role.legendary() && npc.getRole() == role)).isEmpty()) return false;
        GOTDothrakiNpcEntity npc = GOTEntities.DOTHRAKI_NPC.get().create(level);
        if (npc == null) return false;
        npc.moveTo(position.getX() + 0.5D, position.getY(), position.getZ() + 0.5D, 180.0F, 0.0F);
        npc.prepareForSpawn(role, null, false, position, 16, spawn.populationKey());
        if (!level.noCollision(npc) || !level.addFreshEntity(npc)) return false;
        if (npc.rollWorldMount()) npc.requestDothrakiHorse();
        return true;
    }

    private static boolean spawnYiTi(ServerLevel level, BlockPos position, PreparedSpawn spawn) {
        YiTiNpcRole role = YiTiNpcRole.findById(spawn.roleId());
        if (role == null || !level.getEntitiesOfClass(GOTYiTiNpcEntity.class,
                new AABB(position).inflate(32.0D), npc -> spawn.populationKey().equals(npc.getPopulationKey())
                        || (role.legendary() && npc.getRole() == role)).isEmpty()) return false;
        GOTYiTiNpcEntity npc = GOTEntities.YI_TI_NPC.get().create(level);
        if (npc == null) return false;
        npc.moveTo(position.getX() + 0.5D, position.getY(), position.getZ() + 0.5D, 180.0F, 0.0F);
        npc.prepareForSpawn(role, null, false, position, 16, spawn.populationKey());
        return level.noCollision(npc) && level.addFreshEntity(npc);
    }

    private static boolean spawnAsshai(ServerLevel level, BlockPos position, PreparedSpawn spawn) {
        AsshaiNpcRole role = AsshaiNpcRole.findById(spawn.roleId());
        if (role == null || !level.getEntitiesOfClass(GOTAsshaiNpcEntity.class,
                new AABB(position).inflate(32.0D), npc -> spawn.populationKey().equals(npc.getPopulationKey())
                        || (role.legendary() && npc.getRole() == role)).isEmpty()) return false;
        GOTAsshaiNpcEntity npc = GOTEntities.ASSHAI_NPC.get().create(level);
        if (npc == null) return false;
        npc.moveTo(position.getX() + 0.5D, position.getY(), position.getZ() + 0.5D, 180.0F, 0.0F);
        npc.prepareForSpawn(role, null, false, position, 16, spawn.populationKey());
        return level.noCollision(npc) && level.addFreshEntity(npc);
    }

    private static boolean spawnQarth(ServerLevel level, BlockPos position, PreparedSpawn spawn) {
        QarthNpcRole role = QarthNpcRole.findById(spawn.roleId());
        if (role == null || !level.getEntitiesOfClass(GOTQarthNpcEntity.class,
                new AABB(position).inflate(32.0D), npc -> spawn.populationKey().equals(npc.getPopulationKey())
                        || (role.legendary() && npc.getRole() == role)).isEmpty()) return false;
        GOTQarthNpcEntity npc = GOTEntities.QARTH_NPC.get().create(level);
        if (npc == null) return false;
        npc.moveTo(position.getX() + 0.5D, position.getY(), position.getZ() + 0.5D, 180.0F, 0.0F);
        npc.prepareForSpawn(role, null, false, position, 16, spawn.populationKey());
        return level.noCollision(npc) && level.addFreshEntity(npc);
    }

    private static boolean spawnJogosNhai(ServerLevel level, BlockPos position, PreparedSpawn spawn) {
        JogosNhaiNpcRole role = JogosNhaiNpcRole.findById(spawn.roleId());
        if (role == null || !level.getEntitiesOfClass(GOTJogosNhaiNpcEntity.class,
                new AABB(position).inflate(32.0D), npc -> spawn.populationKey().equals(npc.getPopulationKey())
                        || (role.legendary() && npc.getRole() == role)).isEmpty()) return false;
        GOTJogosNhaiNpcEntity npc = GOTEntities.JOGOS_NHAI_NPC.get().create(level);
        if (npc == null) return false;
        npc.moveTo(position.getX() + 0.5D, position.getY(), position.getZ() + 0.5D, 180.0F, 0.0F);
        npc.prepareForSpawn(role, null, false, position, 16, spawn.populationKey());
        return level.noCollision(npc) && level.addFreshEntity(npc);
    }

    private static boolean spawnNorvos(ServerLevel level, BlockPos position, PreparedSpawn spawn) {
        NorvosNpcRole role = NorvosNpcRole.findById(spawn.roleId());
        if (role == null || !level.getEntitiesOfClass(GOTNorvosNpcEntity.class,
                new AABB(position).inflate(32.0D), npc -> spawn.populationKey().equals(npc.getPopulationKey())
                        || (role.legendary() && npc.getRole() == role)).isEmpty()) return false;
        GOTNorvosNpcEntity npc = GOTEntities.NORVOS_NPC.get().create(level);
        if (npc == null) return false;
        npc.moveTo(position.getX() + 0.5D, position.getY(), position.getZ() + 0.5D, 180.0F, 0.0F);
        npc.prepareForSpawn(role, true, false, position, 16, spawn.populationKey());
        return level.noCollision(npc) && level.addFreshEntity(npc);
    }

    private static boolean spawnGoldenCompany(ServerLevel level, BlockPos position,
                                               PreparedSpawn spawn) {
        GoldenCompanyNpcRole role = GoldenCompanyNpcRole.findById(spawn.roleId());
        if (role == null || !level.getEntitiesOfClass(GOTGoldenCompanyNpcEntity.class,
                new AABB(position).inflate(32.0D), npc -> spawn.populationKey().equals(npc.getPopulationKey())
                        || (role.legendary() && npc.getRole() == role)).isEmpty()) return false;
        GOTGoldenCompanyNpcEntity npc = GOTEntities.GOLDEN_COMPANY_NPC.get().create(level);
        if (npc == null) return false;
        npc.moveTo(position.getX() + 0.5D, position.getY(), position.getZ() + 0.5D, 180.0F, 0.0F);
        npc.prepareForSpawn(role, position, 16, spawn.populationKey());
        return level.noCollision(npc) && level.addFreshEntity(npc);
    }

    private static BlockPos safePosition(ServerLevel level, BlockPos requested) {
        BlockPos.MutableBlockPos cursor = requested.mutable();
        for (int offset = 0; offset <= 24; offset++) {
            cursor.set(requested.getX(), requested.getY() + offset, requested.getZ());
            if (isStandingSpace(level, cursor)) return cursor.immutable();
        }
        for (int offset = 1; offset <= 12; offset++) {
            cursor.set(requested.getX(), requested.getY() - offset, requested.getZ());
            if (isStandingSpace(level, cursor)) return cursor.immutable();
        }
        int surface = level.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                requested.getX(), requested.getZ());
        return new BlockPos(requested.getX(), surface, requested.getZ());
    }

    private static boolean isStandingSpace(ServerLevel level, BlockPos position) {
        return level.getBlockState(position).getCollisionShape(level, position).isEmpty()
                && level.getBlockState(position.above()).getCollisionShape(level, position.above()).isEmpty()
                && !level.getBlockState(position.below()).getCollisionShape(level, position.below()).isEmpty();
    }

    private static FixedSpawn fixed(GOTWaypoint waypoint, City city, String roleId,
                                    int offsetX, int offsetZ) {
        return new FixedSpawn(waypoint, city, roleId, offsetX, offsetZ);
    }

    private enum City {
        BRAAVOS, PENTOS, LYS, TYROSH, GHISCAR, DOTHRAKI, YI_TI, ASSHAI, QARTH,
        JOGOS_NHAI, GOLDEN_COMPANY, NORVOS
    }

    private record FixedSpawn(GOTWaypoint waypoint, City city, String roleId,
                              int offsetX, int offsetZ) {}

    private record PreparedSpawn(City city, String roleId, BlockPos position,
                                 String populationKey) {}

    private record PendingKey(MinecraftServer server, ResourceKey<Level> dimension,
                              String populationKey) {}

    private record PendingSpawn(PreparedSpawn spawn) {}
}
