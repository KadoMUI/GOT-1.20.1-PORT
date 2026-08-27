package got.npc;

import got.GOTMod;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/** Durable respawn anchors for fixed legendary/named NPCs. */
@Mod.EventBusSubscriber(modid = GOTMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class GOTNamedNpcRespawnData extends SavedData {
    private static final String DATA_NAME = "got_named_npc_respawns";
    private static final long RESPAWN_DELAY = 20L * 60L * 5L;
    private static final int MAX_CHECKS = 16;
    private final List<Anchor> anchors = new ArrayList<>();
    private int cursor;

    public static GOTNamedNpcRespawnData get(ServerLevel level) {
        return level.getDataStorage().computeIfAbsent(GOTNamedNpcRespawnData::load,
                GOTNamedNpcRespawnData::new, DATA_NAME);
    }

    public void register(Entity entity, String populationKey, BlockPos home) {
        if (populationKey == null || populationKey.isEmpty()) return;
        for (Anchor a : anchors) if (a.key.equals(populationKey)) return;
        CompoundTag tag = new CompoundTag();
        if (!entity.save(tag)) return;
        stripUuid(tag);
        anchors.add(new Anchor(populationKey, home.immutable(), tag, 0L));
        setDirty();
    }

    @SubscribeEvent
    public static void onJoin(EntityJoinLevelEvent event) {
        if (!(event.getLevel() instanceof ServerLevel level)) return;
        Entity entity = event.getEntity();
        String key = populationKey(entity);
        if (key.isEmpty() || !isLegendary(entity)) return;
        get(level).register(entity, key, entity.blockPosition());
    }

    @SubscribeEvent
    public static void onTick(TickEvent.ServerTickEvent event) {
        if (event.phase != TickEvent.Phase.END || event.getServer().getTickCount() % 100 != 0) return;
        for (ServerLevel level : event.getServer().getAllLevels()) get(level).tick(level);
    }

    private void tick(ServerLevel level) {
        if (anchors.isEmpty()) return;
        int checks = Math.min(MAX_CHECKS, anchors.size());
        for (int i = 0; i < checks; i++) {
            if (cursor >= anchors.size()) cursor = 0;
            Anchor a = anchors.get(cursor++);
            int cx = Math.floorDiv(a.home.getX(), 16), cz = Math.floorDiv(a.home.getZ(), 16);
            if (level.getChunkSource().getChunkNow(cx, cz) == null || level.getGameTime() < a.next) continue;
            if (exists(level, a)) { a.next = level.getGameTime() + RESPAWN_DELAY; continue; }
            CompoundTag copy = a.entityTag.copy();
            stripUuid(copy);
            Entity spawned = EntityType.loadEntityRecursive(copy, level, e -> e);
            if (spawned != null) {
                spawned.moveTo(a.home.getX() + 0.5D, a.home.getY(), a.home.getZ() + 0.5D,
                        spawned.getYRot(), spawned.getXRot());
                if (level.noCollision(spawned) && level.addFreshEntity(spawned)) {
                    a.next = level.getGameTime() + RESPAWN_DELAY;
                    setDirty();
                }
            }
        }
    }

    private static boolean exists(ServerLevel level, Anchor a) {
        AABB box = new AABB(a.home).inflate(48.0D);
        return !level.getEntities((Entity)null, box, e -> a.key.equals(populationKey(e))).isEmpty();
    }

    private static String populationKey(Entity entity) {
        try {
            Method m = entity.getClass().getMethod("getPopulationKey");
            Object value = m.invoke(entity);
            return value instanceof String s ? s : "";
        } catch (ReflectiveOperationException ignored) { return ""; }
    }

    private static boolean isLegendary(Entity entity) {
        try {
            Method getRole = entity.getClass().getMethod("getRole");
            Object role = getRole.invoke(entity);
            Method legendary = role.getClass().getMethod("legendary");
            return Boolean.TRUE.equals(legendary.invoke(role));
        } catch (ReflectiveOperationException ignored) { return false; }
    }

    private static void stripUuid(CompoundTag tag) {
        tag.remove("UUID"); tag.remove("UUIDMost"); tag.remove("UUIDLeast");
        if (tag.contains("Passengers", Tag.TAG_LIST)) {
            ListTag list = tag.getList("Passengers", Tag.TAG_COMPOUND);
            for (int i = 0; i < list.size(); i++) stripUuid(list.getCompound(i));
        }
    }

    public static GOTNamedNpcRespawnData load(CompoundTag tag) {
        GOTNamedNpcRespawnData data = new GOTNamedNpcRespawnData();
        ListTag list = tag.getList("Anchors", Tag.TAG_COMPOUND);
        for (int i = 0; i < list.size(); i++) data.anchors.add(Anchor.load(list.getCompound(i)));
        return data;
    }

    @Override public CompoundTag save(CompoundTag tag) {
        ListTag list = new ListTag();
        for (Anchor a : anchors) list.add(a.save());
        tag.put("Anchors", list);
        return tag;
    }

    private static final class Anchor {
        final String key; final BlockPos home; final CompoundTag entityTag; long next;
        Anchor(String key, BlockPos home, CompoundTag entityTag, long next) {
            this.key=key; this.home=home; this.entityTag=entityTag; this.next=next;
        }
        CompoundTag save() { CompoundTag t=new CompoundTag(); t.putString("Key",key); t.putLong("Home",home.asLong()); t.put("Entity",entityTag.copy()); t.putLong("Next",next); return t; }
        static Anchor load(CompoundTag t) { return new Anchor(t.getString("Key"), BlockPos.of(t.getLong("Home")), t.getCompound("Entity"), t.getLong("Next")); }
    }
}
