package got.npc;

import got.GOTEntities;
import got.GOTMod;
import got.quest.GOTJaqenQuestSequence;
import got.world.GOTDimensions;
import net.minecraft.core.BlockPos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** Modern equivalent of GOTJaqenHgharTracker's once-per-world legendary tutorial spawn. */
@Mod.EventBusSubscriber(modid = GOTMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class GOTJaqenHgharSpawner {
    private static int cooldown = 2400;
    private GOTJaqenHgharSpawner() {}

    @SubscribeEvent
    public static void serverTick(TickEvent.ServerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        MinecraftServer server = event.getServer();
        if (hasActiveJaqen(server)) return;
        if (--cooldown > 0) return;
        cooldown = 2400;

        List<ServerPlayer> players = new ArrayList<>(server.getPlayerList().getPlayers());
        Collections.shuffle(players);
        for (ServerPlayer player : players) {
            if (player.level().dimension() != GOTDimensions.PLANETOS) continue;
            if (GOTJaqenQuestSequence.stage(player) != 0 || GOTJaqenQuestSequence.completed(player)) continue;
            if (trySpawn(player)) return;
        }
    }

    private static boolean hasActiveJaqen(MinecraftServer server) {
        for (ServerLevel level : server.getAllLevels()) {
            for (Entity entity : level.getAllEntities()) {
                if (entity instanceof GOTJaqenHgharEntity jaqen && jaqen.isAlive()) return true;
            }
        }
        return false;
    }

    private static boolean trySpawn(ServerPlayer player) {
        ServerLevel level = player.serverLevel();
        for (int attempt = 0; attempt < 32; attempt++) {
            float angle = level.random.nextFloat() * Mth.TWO_PI;
            int radius = Mth.nextInt(level.random, 4, 16);
            int x = Mth.floor(player.getX() + Mth.cos(angle) * radius);
            int z = Mth.floor(player.getZ() + Mth.sin(angle) * radius);
            int y = level.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, x, z);
            if (y <= 62) continue;
            BlockPos pos = new BlockPos(x, y, z);
            if (!level.getBlockState(pos.below()).isCollisionShapeFullBlock(level, pos.below())) continue;
            if (!level.getBlockState(pos).getCollisionShape(level, pos).isEmpty()) continue;
            if (!level.getBlockState(pos.above()).getCollisionShape(level, pos.above()).isEmpty()) continue;

            GOTJaqenHgharEntity jaqen = GOTEntities.JAQEN_HGHAR.get().create(level);
            if (jaqen == null) return false;
            jaqen.moveTo(x + 0.5D, y, z + 0.5D, level.random.nextFloat() * 360.0F, 0.0F);
            jaqen.finalizeSpawn(level, level.getCurrentDifficultyAt(pos), MobSpawnType.EVENT, null, null);
            if (!level.noCollision(jaqen)) continue;
            level.addFreshEntity(jaqen);
            jaqen.announceArrival(player);
            return true;
        }
        return false;
    }
}
