package got.npc.hiring;

import got.npc.GOTFactionNpc;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.Entity;

/**
 * Core hiring rules. Payment/alignment price tables are intentionally separated
 * so the next pass can reproduce the legacy unit-trade catalog without changing
 * the ownership model.
 */
public final class GOTHiringService {
    private GOTHiringService() {}

    public static boolean canBeHired(Entity entity) {
        if (!(entity instanceof PathfinderMob)) return false;
        if (!(entity instanceof GOTFactionNpc npc)) return false;
        return npc.isActiveCombatant() && !npc.isCivilian() && !GOTHiredData.isHired(entity);
    }

    public static boolean hire(ServerPlayer player, Entity entity, GOTHiredTask task) {
        if (!canBeHired(entity)) return false;
        GOTHiredData.hire(entity, player.getUUID(), task);
        var stats=player.getPersistentData().getCompound("GOTAchievementStats");
        int hires=stats.getInt("UnitsHired")+1; stats.putInt("UnitsHired",hires); player.getPersistentData().put("GOTAchievementStats",stats);
        if (hires >= 100) got.achievement.GOTAchievementHooks.award(player, "HUNDREDS");
        if (entity instanceof GOTFactionNpc factionNpc && "golden_company".equals(factionNpc.getFactionId()))
            got.achievement.GOTAchievementHooks.award(player, "HIRE_GOLDEN_COMPANY");
        player.displayClientMessage(
            Component.literal("Hired " + entity.getDisplayName().getString() + "."),
            false
        );
        return true;
    }

    public static boolean dismiss(ServerPlayer player, Entity entity) {
        if (!GOTHiredData.isOwner(entity, player.getUUID())) return false;
        GOTHiredData.dismiss(entity);
        player.displayClientMessage(
            Component.literal("Dismissed " + entity.getDisplayName().getString() + "."),
            false
        );
        return true;
    }

    public static boolean command(ServerPlayer player, Entity entity, GOTHiredOrder order) {
        if (!GOTHiredData.isOwner(entity, player.getUUID())) return false;
        GOTHiredData.setOrder(entity, order);
        return true;
    }

    public static boolean setSquadron(ServerPlayer player, Entity entity, String squadron) {
        if (!GOTHiredData.isOwner(entity, player.getUUID())) return false;
        GOTHiredData.setSquadron(entity, squadron);
        return true;
    }
}
