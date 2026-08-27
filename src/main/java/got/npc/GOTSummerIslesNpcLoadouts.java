package got.npc;

import got.GOTBannerItem;
import got.GOTBannerType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;

/** Summer Isles substitutions over the shared Free Cities civilian equipment pools. */
final class GOTSummerIslesNpcLoadouts {
    private GOTSummerIslesNpcLoadouts() {}

    static void configure(GOTSummerIslesNpcEntity npc) {
        GOTNorvosNpcLoadouts.configure(npc);
        switch (npc.getSummerRole()) {
            case SUMMER_SOLDIER, SUMMER_SOLDIER_ARCHER -> summerArmor(npc, true);
            case SUMMER_BANNER_BEARER -> {
                summerArmor(npc, true);
                npc.setItemSlot(EquipmentSlot.OFFHAND,
                        GOTBannerItem.createStack(GOTBannerType.byName("summer")));
            }
            case SUMMER_CAPTAIN -> summerArmor(npc, false);
            default -> { }
        }
        GOTNpcShieldLoadouts.equip(npc, npc.getRole(), "got:summer_shield");
        npc.updateHeldItem();
    }

    static MerchantOffers createOffers(SummerIslesNpcRole role) {
        if (role != SummerIslesNpcRole.SUMMER_CAPTAIN) {
            return GOTNorvosNpcLoadouts.createOffers(role.parentRole());
        }
        MerchantOffers offers = new MerchantOffers();
        sell(offers, 12, "got:command_horn", 1);
        sell(offers, 16, "got:summer_chestplate", 1);
        sell(offers, 12, "got:summer_helmet", 1);
        sell(offers, 10, "minecraft:iron_sword", 1);
        got.GOTSmithScrollAcquisition.addBlacksmithOffers(offers, role);
        return offers;
    }

    private static void summerArmor(GOTSummerIslesNpcEntity npc, boolean helmet) {
        npc.setItemSlot(EquipmentSlot.FEET, GOTNorvosNpcLoadouts.stack("got:summer_boots"));
        npc.setItemSlot(EquipmentSlot.LEGS, GOTNorvosNpcLoadouts.stack("got:summer_leggings"));
        npc.setItemSlot(EquipmentSlot.CHEST, GOTNorvosNpcLoadouts.stack("got:summer_chestplate"));
        npc.setItemSlot(EquipmentSlot.HEAD, helmet
                ? GOTNorvosNpcLoadouts.stack("got:summer_helmet") : ItemStack.EMPTY);
    }

    private static void sell(MerchantOffers offers, int coins, String output, int count) {
        ItemStack cost = GOTNorvosNpcLoadouts.stack("got:coin_1", coins);
        ItemStack result = GOTNorvosNpcLoadouts.stack(output, count);
        if (!cost.isEmpty() && !result.isEmpty()) offers.add(new MerchantOffer(cost, result, 12, 2, 0.05F));
    }
}
