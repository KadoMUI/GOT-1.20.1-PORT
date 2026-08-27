package got.npc;

import got.GOTEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.levelgen.Heightmap;

import java.util.UUID;

/**
 * Shared khalasar/camp population factory. Worldgen can call this directly once
 * the Dothraki settlement templates are placed; it is deliberately independent
 * from any one structure implementation.
 */
public final class GOTDothrakiKhalasarPopulation {
    private GOTDothrakiKhalasarPopulation() {}

    public static int spawnKhalasar(ServerLevel level, BlockPos center, int fighters, String populationKey) {
        int count = Math.max(4, Math.min(32, fighters));
        UUID groupId = UUID.randomUUID();
        GOTDothrakiNpcEntity leader = spawn(level, center, DothrakiNpcRole.DOTHRAKI_CHIEFTAIN,
                groupId, null, populationKey + ":chieftain", true);
        if (leader == null) return 0;
        int spawned = 1;
        UUID leaderId = leader.getUUID();

        for (int i = 0; i < count; i++) {
            double angle = (Math.PI * 2.0D * i / count) + level.random.nextDouble() * 0.35D;
            int radius = 5 + level.random.nextInt(8);
            int x = center.getX() + (int)Math.round(Math.cos(angle) * radius);
            int z = center.getZ() + (int)Math.round(Math.sin(angle) * radius);
            int y = level.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, x, z);
            DothrakiNpcRole role = level.random.nextInt(4) == 0
                    ? DothrakiNpcRole.DOTHRAKI_ARCHER : DothrakiNpcRole.DOTHRAKI;
            GOTDothrakiNpcEntity npc = spawn(level, new BlockPos(x, y, z), role, groupId, leaderId,
                    populationKey + ":fighter:" + i, true);
            if (npc != null) spawned++;
        }
        return spawned;
    }

    public static GOTDothrakiNpcEntity spawn(ServerLevel level, BlockPos pos, DothrakiNpcRole role,
                                              UUID groupId, UUID leaderId, String populationKey,
                                              boolean worldMounted) {
        GOTDothrakiNpcEntity npc = GOTEntities.DOTHRAKI_NPC.get().create(level);
        if (npc == null) return null;
        npc.moveTo(pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D, level.random.nextFloat() * 360.0F, 0.0F);
        npc.finalizeSpawn(level, level.getCurrentDifficultyAt(pos), MobSpawnType.STRUCTURE, null, null);
        npc.prepareForSpawn(role, null, false, pos, 48, populationKey);
        npc.assignKhalasar(groupId, leaderId);
        if (!level.noCollision(npc) || !level.addFreshEntity(npc)) {
            npc.discard();
            return null;
        }
        if (worldMounted && npc.rollWorldMount()) npc.requestDothrakiHorse();
        return npc;
    }
}
