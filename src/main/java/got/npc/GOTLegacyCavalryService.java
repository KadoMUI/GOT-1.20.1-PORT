package got.npc;

import got.GOTEntities;
import got.mount.GOTMountEntity;
import got.quest.GOTQuestGiver;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Set;

/**
 * Restores the legacy 1.7.10 1-in-10 cavalry constructor roll for regular
 * faction soldiers while preserving the explicit structure/hiring behavior of
 * the 1.20.1 port.  Dothraki are handled by GOTDothrakiMountService because
 * Project Thrones intentionally gives them a much higher mounted ratio.
 */
@Mod.EventBusSubscriber(modid = "got", bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class GOTLegacyCavalryService {
    private static final String ROLL_DONE = "GOTLegacyCavalryRolled";

    /** Conventional professional melee soldiers and banner bearers. */
    private static final Set<String> HORSE_ROLES = Set.of(
            "north_soldier", "north_banner_bearer",
            "arryn_soldier", "arryn_banner_bearer",
            "riverlands_soldier", "riverlands_banner_bearer",
            "westerlands_soldier", "westerlands_banner_bearer",
            "reach_soldier", "reach_banner_bearer",
            "stormlands_soldier", "stormlands_banner_bearer",
            "dorne_soldier", "dorne_banner_bearer",
            "dragonstone_soldier", "dragonstone_banner_bearer",
            "ironborn_soldier", "ironborn_banner_bearer",
            "braavos_soldier", "braavos_banner_bearer",
            "pentos_soldier", "pentos_banner_bearer",
            "myr_soldier", "myr_banner_bearer",
            "tyrosh_soldier", "tyrosh_banner_bearer",
            "lys_soldier", "lys_banner_bearer",
            "lorath_soldier", "lorath_banner_bearer",
            "norvos_soldier", "norvos_banner_bearer",
            "qohor_soldier", "qohor_banner_bearer",
            "volantis_soldier", "volantis_banner_bearer",
            "qarth_soldier", "qarth_banner_bearer",
            "ghiscar_soldier", "ghiscar_banner_bearer",
            "yi_ti_soldier", "yi_ti_banner_bearer"
    );

    /** Jogos Nhai were the ranged cavalry exception and rode zebras. */
    private static final Set<String> ZEBRA_ROLES = Set.of(
            "jogos_nhai_man", "jogos_nhai_archer"
    );

    private GOTLegacyCavalryService() {}

    @SubscribeEvent
    public static void onEntityJoin(EntityJoinLevelEvent event) {
        if (event.getLevel().isClientSide() || !(event.getLevel() instanceof ServerLevel level)) return;
        if (!(event.getEntity() instanceof Mob rider) || !(rider instanceof GOTFactionNpc)) return;
        if (rider instanceof GOTDothrakiNpcEntity) return;
        if (!(rider instanceof GOTQuestGiver giver)) return;

        var data = rider.getPersistentData();
        if (data.getBoolean(ROLL_DONE)) return;

        // Fixed structure/respawner and creative-spawner NPCs are deliberately
        // placed as foot units in the legacy mod. Invasion mobs are persistent
        // too, but constructor cavalry rolls were still valid for them.
        if (rider.isPersistenceRequired() && !data.hasUUID("GOTInvasionId")) {
            data.putBoolean(ROLL_DONE, true);
            return;
        }

        String role = giver.getQuestRoleId();
        boolean horse = HORSE_ROLES.contains(role);
        boolean zebra = ZEBRA_ROLES.contains(role);
        if (!horse && !zebra) {
            data.putBoolean(ROLL_DONE, true);
            return;
        }

        data.putBoolean(ROLL_DONE, true);
        if (rider.getRandom().nextInt(10) != 0) return;

        spawnMount(level, rider, zebra);
    }

    private static boolean spawnMount(ServerLevel level, Mob rider, boolean zebra) {
        if (!rider.isAlive() || rider.isPassenger()) return false;
        EntityType<? extends GOTMountEntity> type = zebra ? GOTEntities.ZEBRA.get() : GOTEntities.GOT_HORSE.get();
        GOTMountEntity mount = type.create(level);
        if (mount == null) return false;

        mount.moveTo(rider.getX(), rider.getY(), rider.getZ(), rider.getYRot(), 0.0F);
        DifficultyInstance difficulty = level.getCurrentDifficultyAt(rider.blockPosition());
        mount.finalizeSpawn(level, difficulty, MobSpawnType.EVENT, null, null);
        mount.prepareAsNpcMount();

        // Conventional legacy cavalry used iron horse armor. Jogos Nhai zebra
        // riders did not use horse armor.
        if (!zebra) mount.equipHorseArmor(new ItemStack(Items.IRON_HORSE_ARMOR));

        if (!level.noCollision(mount)) {
            mount.discard();
            return false;
        }
        if (!level.addFreshEntity(mount)) {
            mount.discard();
            return false;
        }
        if (!mount.mountNpc(rider)) {
            mount.discard();
            return false;
        }
        return true;
    }
}
