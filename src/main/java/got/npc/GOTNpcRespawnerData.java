package got.npc;

import got.GOTEntities;
import got.GOTMod;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;

/**
 * Durable population anchors for legacy respawner markers. Anchors are saved
 * with the dimension and only repopulate while their chunk is already loaded.
 */
@Mod.EventBusSubscriber(modid = GOTMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class GOTNpcRespawnerData extends SavedData {
    private static final String DATA_NAME = "got_npc_respawners";
    private static final long RESPAWN_DELAY = 20L * 60L * 5L;
    private static final int MAX_CHECKS_PER_LEVEL = 16;
    private final List<Anchor> anchors = new ArrayList<>();
    private int cursor;

    public static GOTNpcRespawnerData get(ServerLevel level) {
        return level.getDataStorage().computeIfAbsent(GOTNpcRespawnerData::load,
                GOTNpcRespawnerData::new, DATA_NAME);
    }

    public void register(String family, String role, String populationKey, BlockPos position,
                         float yaw, Boolean female, boolean child, int homeRadius) {
        for (Anchor anchor : anchors) if (anchor.populationKey.equals(populationKey)) return;
        anchors.add(new Anchor(family, role, populationKey, position, yaw,
                female == null ? -1 : female ? 1 : 0, child, homeRadius, 0L));
        setDirty();
    }

    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase != TickEvent.Phase.END || event.getServer().getTickCount() % 100 != 0) return;
        for (ServerLevel level : event.getServer().getAllLevels()) get(level).tick(level);
    }

    private void tick(ServerLevel level) {
        if (anchors.isEmpty()) return;
        int checks = Math.min(MAX_CHECKS_PER_LEVEL, anchors.size());
        for (int i = 0; i < checks; i++) {
            if (cursor >= anchors.size()) cursor = 0;
            Anchor anchor = anchors.get(cursor++);
            int chunkX = Math.floorDiv(anchor.position.getX(), 16);
            int chunkZ = Math.floorDiv(anchor.position.getZ(), 16);
            if (level.getChunkSource().getChunkNow(chunkX, chunkZ) == null
                    || level.getGameTime() < anchor.nextCheck) continue;
            if (exists(level, anchor)) {
                anchor.nextCheck = level.getGameTime() + RESPAWN_DELAY;
                continue;
            }
            if (spawn(level, anchor)) {
                anchor.nextCheck = level.getGameTime() + RESPAWN_DELAY;
                setDirty();
            }
        }
    }

    private static boolean exists(ServerLevel level, Anchor anchor) {
        AABB box = new AABB(anchor.position).inflate(Math.max(24, anchor.homeRadius + 8));
        return switch (anchor.family) {
            case "wildling" -> !level.getEntitiesOfClass(GOTWildlingNpcEntity.class, box,
                    npc -> anchor.populationKey.equals(npc.getPopulationKey())).isEmpty();
            case "night_watch" -> !level.getEntitiesOfClass(GOTNightWatchNpcEntity.class, box,
                    npc -> anchor.populationKey.equals(npc.getPopulationKey())).isEmpty();
            default -> !level.getEntitiesOfClass(GOTWhiteWalkerNpcEntity.class, box,
                    npc -> anchor.populationKey.equals(npc.getPopulationKey())).isEmpty();
        };
    }

    private static boolean spawn(ServerLevel level, Anchor anchor) {
        Boolean female = anchor.gender < 0 ? null : anchor.gender > 0;
        if (anchor.family.equals("wildling")) {
            GOTWildlingNpcEntity npc = GOTEntities.WILDLING_NPC.get().create(level);
            if (npc == null) return false;
            npc.moveTo(anchor.position.getX()+0.5D,anchor.position.getY(),anchor.position.getZ()+0.5D,anchor.yaw,0.0F);
            npc.prepareForSpawn(WildlingNpcRole.byId(anchor.role),female,anchor.child,anchor.position,anchor.homeRadius,anchor.populationKey);
            return level.noCollision(npc) && level.addFreshEntity(npc);
        }
        if (anchor.family.equals("night_watch")) {
            GOTNightWatchNpcEntity npc = GOTEntities.NIGHT_WATCH_NPC.get().create(level);
            if (npc == null) return false;
            npc.moveTo(anchor.position.getX()+0.5D,anchor.position.getY(),anchor.position.getZ()+0.5D,anchor.yaw,0.0F);
            npc.prepareForSpawn(NightWatchNpcRole.byId(anchor.role),female,anchor.child,anchor.position,anchor.homeRadius,anchor.populationKey);
            return level.noCollision(npc) && level.addFreshEntity(npc);
        }
        GOTWhiteWalkerNpcEntity npc = GOTEntities.WHITE_WALKER_NPC.get().create(level);
        if (npc == null) return false;
        npc.moveTo(anchor.position.getX()+0.5D,anchor.position.getY(),anchor.position.getZ()+0.5D,anchor.yaw,0.0F);
        npc.prepareForSpawn(WhiteWalkerNpcRole.byId(anchor.role),female,anchor.child,anchor.position,anchor.homeRadius,anchor.populationKey);
        return level.noCollision(npc) && level.addFreshEntity(npc);
    }

    public static GOTNpcRespawnerData load(CompoundTag tag) {
        GOTNpcRespawnerData data = new GOTNpcRespawnerData();
        ListTag list = tag.getList("Anchors", Tag.TAG_COMPOUND);
        for (int i=0;i<list.size();i++) data.anchors.add(Anchor.load(list.getCompound(i)));
        return data;
    }

    @Override public CompoundTag save(CompoundTag tag) {
        ListTag list = new ListTag();
        for (Anchor anchor : anchors) list.add(anchor.save());
        tag.put("Anchors", list);
        return tag;
    }

    private static final class Anchor {
        private final String family,role,populationKey;
        private final BlockPos position;
        private final float yaw;
        private final int gender,homeRadius;
        private final boolean child;
        private long nextCheck;
        private Anchor(String family,String role,String populationKey,BlockPos position,float yaw,
                       int gender,boolean child,int homeRadius,long nextCheck){this.family=family;this.role=role;
            this.populationKey=populationKey;this.position=position;this.yaw=yaw;this.gender=gender;
            this.child=child;this.homeRadius=homeRadius;this.nextCheck=nextCheck;}
        private CompoundTag save(){CompoundTag t=new CompoundTag();t.putString("Family",family);t.putString("Role",role);
            t.putString("Key",populationKey);t.putLong("Pos",position.asLong());t.putFloat("Yaw",yaw);t.putInt("Gender",gender);
            t.putBoolean("Child",child);t.putInt("Home",homeRadius);t.putLong("Next",nextCheck);return t;}
        private static Anchor load(CompoundTag t){return new Anchor(t.getString("Family"),t.getString("Role"),t.getString("Key"),
                BlockPos.of(t.getLong("Pos")),t.getFloat("Yaw"),t.getInt("Gender"),t.getBoolean("Child"),t.getInt("Home"),t.getLong("Next"));}
    }
}
