package got.npc.hiring;

import got.npc.GOTFactionNpc;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;

import java.util.List;

/** Server-only snapshot builder backed by the exact legacy hire table. */
public final class GOTHireSnapshotFactory {
    private GOTHireSnapshotFactory() {}

    public static GOTHireSnapshot build(ServerPlayer player, Entity entity) {
        GOTHireDefinition def = GOTHiringRuleService.definitionFor(entity);
        GOTHiringRuleService.Result result = GOTHiringRuleService.evaluate(player, entity);
        boolean mountedVariant = GOTHiringRuleService.hasMountedVariant(entity);
        GOTHiringRuleService.Result mountedResult = mountedVariant ? GOTHiringRuleService.evaluate(player, entity, true) : null;

        String role = GOTHiringRoleResolver.roleId(entity);
        if (role == null || role.isBlank()) role = entity.getType().toString();

        String faction = entity instanceof GOTFactionNpc factionNpc ? factionNpc.getFactionId() : "";

        boolean hired = GOTHiredData.isHired(entity);
        boolean owner = GOTHiredData.isOwner(entity, player.getUUID());

        GOTHiredTask task = hired ? GOTHiredData.task(entity)
                : (def != null ? def.task() : GOTHiredTask.WARRIOR);

        int requiredAlign = def != null ? Math.round(def.requiredAlignment()) : 0;
        String reqText = "";
        if (def != null) {
            if (def.pledgeExclusive()) {
                reqText = "Pledge required";
                if (requiredAlign != 0) reqText += " | Alignment: " + requiredAlign;
            } else if (requiredAlign != 0) {
                reqText = "Alignment: " + requiredAlign;
            }
        }

        List<GOTHireSnapshot.CostLine> cost = !hired && result.allowed()
                ? List.of(new GOTHireSnapshot.CostLine("got:coin_value", result.price()))
                : (def != null && entity instanceof GOTFactionNpc factionNpc
                    ? List.of(new GOTHireSnapshot.CostLine(
                        "got:coin_value",
                        GOTHiringLegacyPriceService.price(
                            player, factionNpc.getFactionId(), def.initialCost(), def.requiredAlignment()
                        )))
                    : List.of());

        return new GOTHireSnapshot(
            entity.getId(),
            entity.getDisplayName().getString(),
            role,
            faction,
            hired,
            owner,
            task,
            hired ? GOTHiredData.order(entity) : GOTHiredOrder.FOLLOW,
            hired ? GOTHiredData.squadron(entity) : "",
            hired ? GOTHiredData.guardRange(entity) : 8,
            hired && GOTHiredData.teleportAutomatically(entity),
            hired ? GOTHiredData.xpLevel(entity) : 1,
            hired ? GOTHiredData.xp(entity) : 0,
            hired ? GOTHiredData.mobKills(entity) : 0,
            !hired && result.allowed(),
            !hired && mountedVariant,
            !hired && mountedResult != null && mountedResult.allowed(),
            !hired && !result.allowed() ? result.message().getString() : "",
            requiredAlign,
            reqText,
            cost
        );
    }
}
