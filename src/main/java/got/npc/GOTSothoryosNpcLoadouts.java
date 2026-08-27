package got.npc;

import got.GOTBannerItem;
import got.GOTBannerType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;

/** Primitive bronze equipment, blowgun, poison and trade pools from legacy Sothoryos. */
final class GOTSothoryosNpcLoadouts {
    private GOTSothoryosNpcLoadouts() {}

    static void configure(GOTSothoryosNpcEntity npc) {
        npc.clearLoadout();
        switch (npc.getSothoryosRole()) {
            case SOTHORYOS_MAN -> npc.setCombatWeapon(stack("got:bronze_dagger"));
            case SOTHORYOS_WARRIOR -> {
                ItemStack weapon = randomPrimitiveWeapon(npc);
                npc.setWeapons(weapon, weapon);
                armor(npc, true, false);
            }
            case SOTHORYOS_BLOWGUNNER -> {
                ItemStack blowgun = stack("got:sarbacane");
                npc.setWeapons(randomPrimitiveWeapon(npc), blowgun);
                npc.setRangedWeapon(blowgun);
                armor(npc, true, false);
            }
            case SOTHORYOS_BANNER_BEARER -> {
                ItemStack dagger = stack("got:bronze_dagger");
                npc.setWeapons(dagger, dagger);
                armor(npc, true, false);
                npc.setItemSlot(EquipmentSlot.OFFHAND,
                        GOTBannerItem.createStack(GOTBannerType.byName("sothoryos")));
            }
            case SOTHORYOS_CHIEFTAIN -> {
                ItemStack sword = stack("got:bronze_sword");
                npc.setWeapons(sword, sword);
                armor(npc, true, true);
            }
            case SOTHORYOS_SHAMAN ->
                    npc.setWeapons(stack("got:bronze_dagger_poisoned"), stack("got:bottle_poison"));
            case SOTHORYOS_FARMER, SOTHORYOS_FARMHAND -> {
                ItemStack hoe = new ItemStack(Items.STONE_HOE);
                npc.setWeapons(hoe, hoe);
            }
            case SOTHORYOS_SMITH -> {
                ItemStack hammer = stack("got:blacksmith_hammer");
                npc.setWeapons(hammer, hammer);
            }
        }
        GOTNpcShieldLoadouts.equip(npc, npc.getRole(), "got:sothoryos_shield");
        npc.updateHeldItem();
    }

    static MerchantOffers createOffers(SothoryosNpcRole role) {
        return switch (role.trade()) {
            case FARMER -> GOTNorvosNpcLoadouts.createOffers(NorvosNpcRole.NORVOS_FARMER);
            case BLACKSMITH -> GOTNorvosNpcLoadouts.createOffers(NorvosNpcRole.NORVOS_BLACKSMITH);
            case ALCHEMIST -> alchemistOffers();
            case UNITS -> unitOffers();
            default -> new MerchantOffers();
        };
    }

    private static MerchantOffers alchemistOffers() {
        MerchantOffers offers = new MerchantOffers();
        buy(offers, "minecraft:spider_eye", 8, 4);
        buy(offers, "minecraft:fermented_spider_eye", 4, 6);
        sell(offers, 8, "got:bottle_poison", 1);
        sell(offers, 4, "got:dart_poisoned", 8);
        return offers;
    }

    private static MerchantOffers unitOffers() {
        MerchantOffers offers = new MerchantOffers();
        sell(offers, 12, "got:command_horn", 1);
        sell(offers, 14, "got:sothoryos_chestplate", 1);
        sell(offers, 10, "got:sothoryos_helmet", 1);
        sell(offers, 8, "got:bronze_sword", 1);
        return offers;
    }

    private static void armor(GOTSothoryosNpcEntity npc, boolean helmet, boolean chieftain) {
        npc.setItemSlot(EquipmentSlot.FEET, stack("got:sothoryos_boots"));
        npc.setItemSlot(EquipmentSlot.LEGS, stack("got:sothoryos_leggings"));
        npc.setItemSlot(EquipmentSlot.CHEST, stack("got:sothoryos_chestplate"));
        if (helmet) npc.setItemSlot(EquipmentSlot.HEAD,
                stack(chieftain ? "got:sothoryos_helmet_chieftain" : "got:sothoryos_helmet"));
    }

    private static ItemStack randomPrimitiveWeapon(GOTSothoryosNpcEntity npc) {
        if (npc.getRandom().nextInt(5) == 0) return stack("got:bronze_spear");
        int roll = npc.getRandom().nextInt(10);
        if (roll < 5) return stack("got:bronze_sword");
        if (roll < 7) return stack("got:bronze_battleaxe");
        if (roll < 9) return stack("got:bronze_hammer");
        return stack("got:bronze_pike");
    }

    private static void buy(MerchantOffers offers, String input, int count, int coins) {
        add(offers, input, count, "got:coin_1", coins);
    }

    private static void sell(MerchantOffers offers, int coins, String output, int count) {
        add(offers, "got:coin_1", coins, output, count);
    }

    private static void add(MerchantOffers offers, String input, int inputCount,
                            String output, int outputCount) {
        ItemStack cost = stack(input, inputCount);
        ItemStack result = stack(output, outputCount);
        if (!cost.isEmpty() && !result.isEmpty()) offers.add(new MerchantOffer(cost, result, 12, 2, 0.05F));
    }

    private static ItemStack stack(String id) { return GOTNorvosNpcLoadouts.stack(id); }
    private static ItemStack stack(String id, int count) { return GOTNorvosNpcLoadouts.stack(id, count); }
}
