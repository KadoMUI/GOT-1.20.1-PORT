package got.npc;

import got.GOTEntities;
import got.mount.GOTMountEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.MobSpawnType;

/**
 * Central Dothraki horse creation used by settlement/khalasar population,
 * invasions and the NPC spawner item.  Hiring has its own explicit mounted
 * transaction so a player can still deliberately buy a foot unit.
 */
public final class GOTDothrakiMountService {
    private GOTDothrakiMountService() {}

    public static boolean spawnHorseFor(GOTDothrakiNpcEntity rider) {
        if (!(rider.level() instanceof ServerLevel level) || !rider.isAlive()) return false;
        if (rider.isPassenger()) return true;

        GOTMountEntity horse = GOTEntities.GOT_HORSE.get().create(level);
        if (horse == null) return false;
        horse.moveTo(rider.getX(), rider.getY(), rider.getZ(), rider.getYRot(), 0.0F);

        // EntityType#create does not run the vanilla spawn finalizer. Run it here
        // so GOT horse legacy stat clamping and vanilla horse variant generation
        // happen for dynamically-created Dothraki mounts too.
        DifficultyInstance difficulty = level.getCurrentDifficultyAt(rider.blockPosition());
        horse.finalizeSpawn(level, difficulty, MobSpawnType.EVENT, null, null);
        horse.prepareAsNpcMount();

        if (!level.noCollision(horse)) {
            horse.discard();
            return false;
        }
        if (!level.addFreshEntity(horse)) {
            horse.discard();
            return false;
        }
        if (!horse.mountNpc(rider)) {
            horse.discard();
            return false;
        }
        return true;
    }
}
