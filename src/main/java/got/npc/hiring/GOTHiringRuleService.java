package got.npc.hiring;

import got.npc.GOTFactionNpc;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;

import javax.annotation.Nullable;

/**
 * Exact legacy availability gates for the current role/faction NPC framework.
 */
public final class GOTHiringRuleService {
    private GOTHiringRuleService() {}

    @Nullable
    public static GOTHireDefinition definitionFor(Entity entity) {
        String roleId = GOTHiringRoleResolver.roleId(entity);
        if (roleId == null || roleId.isBlank()) return null;

        for (GOTHireDefinition definition : GOTHiringCatalog.all()) {
            if (definition.roleIds().contains(roleId)) return definition;
        }
        return null;
    }

    public static Result evaluate(ServerPlayer player, Entity entity) {
        GOTHireDefinition definition = definitionFor(entity);
        if (definition == null) {
            return Result.fail(Component.literal("This NPC is not available for hire."));
        }

        if (GOTHiredData.isHired(entity)) {
            return Result.fail(Component.literal("This NPC is already hired."));
        }

        if (!(entity instanceof GOTFactionNpc factionNpc)) {
            return Result.fail(Component.literal("This NPC has no hireable faction data."));
        }

        String factionId = factionNpc.getFactionId();
        float alignment = GOTHiringPlayerRules.alignment(player, factionId);

        if (definition.pledgeExclusive() && !GOTHiringPlayerRules.pledgedTo(player, factionId)) {
            return Result.fail(Component.literal("You must be pledged to this faction."));
        }

        if (alignment < definition.requiredAlignment()) {
            return Result.fail(Component.literal(
                "Requires " + formatAlignment(definition.requiredAlignment()) + " alignment."
            ));
        }

        int price = GOTHiringLegacyPriceService.price(
            player,
            factionId,
            definition.initialCost(),
            definition.requiredAlignment()
        );

        if (!GOTHiringCoinService.canAfford(player, price)) {
            return Result.fail(Component.literal("You need " + price + " coin value to hire this unit."));
        }

        return Result.ok(definition, price);
    }

    private static String formatAlignment(float value) {
        return value == Math.rint(value) ? Integer.toString((int)value) : Float.toString(value);
    }

    public record Result(
        boolean allowed,
        GOTHireDefinition definition,
        int price,
        Component message
    ) {
        public static Result ok(GOTHireDefinition definition, int price) {
            return new Result(true, definition, price, Component.empty());
        }

        public static Result fail(Component message) {
            return new Result(false, null, 0, message);
        }
    }
}
