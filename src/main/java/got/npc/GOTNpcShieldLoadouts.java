package got.npc;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * Shared shield assignment for humanoid military NPC loadouts.
 *
 * Shields are intentionally limited to roles that fight as one-handed line
 * infantry/guards/knights/captains.  Ranged troops, pikes/spears, banner
 * bearers and civilians are excluded.  Banner bearers are additionally
 * protected by the OFFHAND-empty check.
 */
final class GOTNpcShieldLoadouts {
    private GOTNpcShieldLoadouts() {}

    static void equip(Mob npc, Enum<?> role, String defaultShieldId) {
        if (npc == null || role == null || defaultShieldId == null || defaultShieldId.isBlank()) return;
        if (!npc.getItemBySlot(EquipmentSlot.OFFHAND).isEmpty()) return;

        String name = role.name();
        if (!eligible(name) || excluded(name)) return;

        String shieldId = shieldForRole(name, defaultShieldId);
        Item item = ForgeRegistries.ITEMS.getValue(ResourceLocation.parse(shieldId));
        if (item == null) return;

        npc.setItemSlot(EquipmentSlot.OFFHAND, new ItemStack(item));
        npc.setDropChance(EquipmentSlot.OFFHAND, 0.0F);
    }

    private static boolean eligible(String name) {
        return name.contains("LEVYMAN")
                || name.contains("SOLDIER")
                || name.contains("GUARD")
                || name.contains("CAPTAIN")
                || name.contains("WARRIOR")
                || name.contains("UNSULLIED")
                || name.equals("GREY_WORM")
                || name.equals("YI_TI_SAMURAI")
                || name.equals("YI_TI_BOMBARDIER");
    }

    private static boolean excluded(String name) {
        return name.contains("ARCHER")
                || name.contains("CROSSBOW")
                || name.contains("CROSSBOWER")
                || name.contains("SPEAR")
                || name.contains("PIKE")
                || name.contains("BANNER")
                || name.contains("FLAMETHROWER");
    }

    private static String shieldForRole(String role, String fallback) {
        return switch (role) {
            case "NORTH_GUARD" -> "got:northguard_shield";
            case "ARRYN_GUARD" -> "got:arrynguard_shield";
            case "WESTERLANDS_GUARD" -> "got:westerlandsguard_shield";
            case "REACH_GUARD" -> "got:reachguard_shield";

            case "NORTH_HILLMAN_WARRIOR", "HILLMAN_WARRIOR" -> "got:hillmen_shield";

            case "GHISCAR_UNSULLIED", "GREY_WORM" -> "got:unsullied_shield";

            case "YI_TI_SAMURAI" -> "got:yi_ti_samurai_shield";
            case "YI_TI_BOMBARDIER" -> "got:yi_ti_bombardier_shield";

            default -> fallback;
        };
    }
}
