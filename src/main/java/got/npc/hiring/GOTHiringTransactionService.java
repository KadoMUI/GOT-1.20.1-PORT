package got.npc.hiring;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import got.mount.GOTMountEntity;
import got.GOTEntities;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Items;

/**
 * One atomic server-side hire transaction.
 */
public final class GOTHiringTransactionService {
    private GOTHiringTransactionService() {}

    public static boolean hire(ServerPlayer player, Entity entity) { return hire(player, entity, false); }

    public static boolean hire(ServerPlayer player, Entity entity, boolean mounted) {
        GOTHiringRuleService.Result result = GOTHiringRuleService.evaluate(player, entity, mounted);
        if (!result.allowed()) {
            player.displayClientMessage(result.message(), false);
            return false;
        }

        if (!GOTHiringCoinService.take(player, result.price())) {
            player.displayClientMessage(Component.literal("You cannot afford this unit."), false);
            return false;
        }

        GOTHiredData.hire(entity, player.getUUID(), result.definition().task());
        if (mounted && entity instanceof Mob mob && !spawnMount(mob, result.definition())) {
            GOTHiredData.dismiss(entity);
            GOTHiringCoinService.giveValue(player, result.price());
            player.displayClientMessage(Component.literal("Could not create this unit's mount."), false);
            return false;
        }

        player.displayClientMessage(
            Component.literal("Hired " + entity.getDisplayName().getString()
                + " for " + result.price() + " coin value."),
            false
        );
        return true;
    }
    private static boolean spawnMount(Mob rider, GOTHireDefinition def) {
        EntityType<? extends GOTMountEntity> type = switch (def.mountType()) {
            case "GOTEntityZebra" -> GOTEntities.ZEBRA.get();
            case "GOTEntityWoolyRhino" -> GOTEntities.WOOLY_RHINO.get();
            default -> GOTEntities.GOT_HORSE.get();
        };
        GOTMountEntity mount = type.create(rider.level());
        if (mount == null) return false;
        mount.moveTo(rider.getX(), rider.getY(), rider.getZ(), rider.getYRot(), 0);
        if (!def.mountName().isBlank()) mount.setCustomName(Component.literal(def.mountName()));
        if (!def.mountArmor().isBlank()) mount.equipHorseArmor(new net.minecraft.world.item.ItemStack(Items.IRON_HORSE_ARMOR));
        rider.level().addFreshEntity(mount);
        return mount.mountNpc(rider);
    }
}
