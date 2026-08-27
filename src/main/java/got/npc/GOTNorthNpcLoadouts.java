package got.npc;

import got.GOTBannerItem;
import got.GOTBannerType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;
import net.minecraftforge.registries.ForgeRegistries;

/** Equipment and trade pools translated from the original North subclasses. */
final class GOTNorthNpcLoadouts {
    private GOTNorthNpcLoadouts() {}

    static void configure(GOTNorthNpcEntity npc) {
        NorthNpcRole role = npc.getRole();
        npc.clearLoadout();
        switch (role) {
            case NORTH_MAN, NORTH_HILLMAN -> npc.setCombatWeapon(stack("got:iron_dagger"));
            case NORTH_LEVYMAN -> {
                npc.setCombatWeapon(randomIronWeapon(npc));
                leatherArmor(npc, true);
            }
            case NORTH_LEVYMAN_ARCHER -> {
                npc.setCombatWeapon(randomIronWeapon(npc));
                npc.setRangedWeapon(stack("got:longbow"));
                leatherArmor(npc, true);
            }
            case NORTH_SOLDIER -> {
                npc.setCombatWeapon(randomIronWeapon(npc));
                northArmor(npc);
            }
            case NORTH_SOLDIER_ARCHER -> {
                npc.setCombatWeapon(randomIronWeapon(npc));
                npc.setRangedWeapon(stack("got:longbow"));
                northArmor(npc);
            }
            case NORTH_GUARD -> {
                npc.setCombatWeapon(stack("got:iron_pike"));
                northGuardArmor(npc);
            }
            case NORTH_BANNER_BEARER -> {
                npc.setCombatWeapon(stack("got:iron_dagger"));
                northArmor(npc);
                npc.setItemSlot(EquipmentSlot.OFFHAND, GOTBannerItem.createStack(GOTBannerType.byName("robb")));
            }
            case NORTH_CAPTAIN -> {
                npc.setCombatWeapon(new ItemStack(net.minecraft.world.item.Items.IRON_SWORD));
                northArmorWithoutHelmet(npc);
            }
            case NORTH_BLACKSMITH -> npc.setWeapons(stack("got:blacksmith_hammer"), stack("got:blacksmith_hammer"));
            case NORTH_GOLDSMITH -> npc.setWeapons(stack("got:iron_dagger"), stack("got:silver_ring"));
            case NORTH_FARMER, NORTH_FARMHAND -> {
                npc.setWeapons(new ItemStack(net.minecraft.world.item.Items.IRON_HOE), new ItemStack(net.minecraft.world.item.Items.IRON_HOE));
                if (role == NorthNpcRole.NORTH_FARMER) npc.setItemSlot(EquipmentSlot.HEAD, stack("got:leather_hat"));
            }
            case NORTH_BARTENDER -> npc.setWeapons(stack("got:iron_dagger"), stack("got:mug_ale"));
            case NORTH_MINER -> npc.setWeapons(new ItemStack(net.minecraft.world.item.Items.IRON_PICKAXE), new ItemStack(net.minecraft.world.item.Items.IRON_PICKAXE));
            case NORTH_LUMBERMAN -> {
                npc.setWeapons(new ItemStack(net.minecraft.world.item.Items.IRON_AXE), new ItemStack(net.minecraft.world.item.Items.IRON_AXE));
                npc.setItemSlot(EquipmentSlot.HEAD, stack("got:leather_hat"));
            }
            case NORTH_MASON -> npc.setWeapons(new ItemStack(net.minecraft.world.item.Items.IRON_PICKAXE), new ItemStack(net.minecraft.world.item.Items.STONE));
            case NORTH_BREWER -> npc.setWeapons(stack("got:iron_dagger"), stack("got:mug_ale"));
            case NORTH_FLORIST -> {
                npc.setWeapons(stack("got:iron_dagger"), new ItemStack(net.minecraft.world.item.Items.POPPY));
                npc.setItemSlot(EquipmentSlot.HEAD, stack("got:leather_hat"));
            }
            case NORTH_BUTCHER -> npc.setWeapons(new ItemStack(net.minecraft.world.item.Items.IRON_AXE), new ItemStack(net.minecraft.world.item.Items.BEEF));
            case NORTH_FISHMONGER -> {
                npc.setWeapons(stack("got:iron_dagger"), new ItemStack(net.minecraft.world.item.Items.COD));
                npc.setItemSlot(EquipmentSlot.HEAD, stack("got:leather_hat"));
            }
            case NORTH_BAKER -> npc.setWeapons(stack("got:rolling_pin"), new ItemStack(net.minecraft.world.item.Items.BREAD));
            case NORTH_HILLMAN_WARRIOR -> {
                npc.setCombatWeapon(randomPrimitiveWeapon(npc));
                leatherArmor(npc, false);
            }
            case NORTH_HILLMAN_ARCHER -> {
                npc.setCombatWeapon(randomPrimitiveWeapon(npc));
                npc.setRangedWeapon(stack("got:longbow"));
                leatherArmor(npc, false);
            }
            case NORTH_HILLMAN_AXE_THROWER -> {
                npc.setCombatWeapon(randomPrimitiveWeapon(npc));
                npc.setRangedWeapon(stack("got:iron_throwing_axe"));
                leatherArmor(npc, false);
            }
            case NORTH_HILLMAN_BANNER_BEARER -> {
                npc.setCombatWeapon(stack("got:iron_dagger"));
                leatherArmor(npc, false);
                npc.setItemSlot(EquipmentSlot.OFFHAND, GOTBannerItem.createStack(GOTBannerType.byName("eddard")));
            }
            case NORTH_HILLMAN_CHIEFTAIN -> {
                npc.setCombatWeapon(stack("got:trident"));
                leatherArmor(npc, false);
            }
            case BARBREY_DUSTIN, MAESTER_LUWIN, OSHA, CATELYN_STARK ->
                    npc.setCombatWeapon(stack("got:alloy_steel_dagger"));
            case RAMSAY_BOLTON -> npc.setCombatWeapon(stack("got:iron_dagger_poisoned"));
            case WYMAN_MANDERLY -> npc.setCombatWeapon(stack("got:fin"));
            case ARYA_STARK -> npc.setCombatWeapon(stack("got:needle"));
            case BRAN_STARK -> {
                npc.setCombatWeapon(stack("got:alloy_steel_dagger"));
                npc.setRangedWeapon(stack("got:longbow"));
            }
            case HODOR, RICKON_STARK -> { }
            default -> npc.setCombatWeapon(stack("got:alloy_steel_sword"));
        }
        GOTNpcShieldLoadouts.equip(npc, npc.getRole(), "got:north_shield");
        npc.updateHeldItem();
    }

    static MerchantOffers createOffers(NorthNpcRole role) {
        MerchantOffers offers = new MerchantOffers();
        switch (role.trade()) {
            case BAKER -> {
                buy(offers, "minecraft:wheat", 16, 2);
                sell(offers, 2, "minecraft:bread", 4);
                sell(offers, 5, "got:apple_crumble", 1);
            }
            case BARTENDER -> {
                buy(offers, "minecraft:wheat", 20, 2);
                sell(offers, 3, "got:mug_ale", 1);
                sell(offers, 4, "got:mug_cider", 1);
                sell(offers, 5, "got:mug_mead", 1);
            }
            case BLACKSMITH -> {
                sell(offers, 8, "got:blacksmith_hammer", 1);
                buy(offers, "minecraft:coal", 16, 2);
                buy(offers, "minecraft:iron_ingot", 8, 4);
                sell(offers, 8, "minecraft:iron_sword", 1);
                sell(offers, 10, "got:iron_pike", 1);
            }
            case BREWER -> {
                buy(offers, "minecraft:wheat", 20, 2);
                buy(offers, "minecraft:sugar", 16, 2);
                sell(offers, 3, "got:mug_ale", 1);
                sell(offers, 4, "got:mug_cider", 1);
            }
            case BUTCHER -> {
                buy(offers, "minecraft:beef", 12, 2);
                buy(offers, "minecraft:porkchop", 12, 2);
                sell(offers, 3, "minecraft:cooked_beef", 5);
                sell(offers, 3, "minecraft:cooked_porkchop", 5);
            }
            case FARMER -> {
                sell(offers, 4, "got:branding_iron", 1);
                // Guaranteed Farmer utility trade.
                sell(offers, 8, "got:millstone", 1);
                buy(offers, "minecraft:wheat", 20, 2);
                buy(offers, "minecraft:carrot", 18, 2);
                sell(offers, 2, "minecraft:wheat_seeds", 12);
                sell(offers, 3, "got:leek", 6);
            }
            case FISHMONGER -> {
                buy(offers, "minecraft:cod", 12, 2);
                buy(offers, "minecraft:salmon", 10, 2);
                sell(offers, 3, "minecraft:cooked_cod", 5);
                sell(offers, 3, "minecraft:cooked_salmon", 5);
            }
            case FLORIST -> {
                buy(offers, "minecraft:bone_meal", 16, 2);
                sell(offers, 2, "minecraft:poppy", 6);
                sell(offers, 2, "minecraft:dandelion", 6);
            }
            case GOLDSMITH -> {
                buy(offers, "minecraft:gold_ingot", 4, 5);
                buy(offers, "got:silver_ingot", 6, 5);
                sell(offers, 10, "got:silver_ring", 1);
                sell(offers, 12, "got:gold_ring", 1);
            }
            case LUMBERMAN -> {
                buy(offers, "minecraft:spruce_log", 16, 2);
                sell(offers, 2, "minecraft:spruce_planks", 24);
                sell(offers, 4, "minecraft:charcoal", 8);
            }
            case MASON -> {
                buy(offers, "minecraft:cobblestone", 32, 2);
                sell(offers, 2, "minecraft:stone_bricks", 16);
                sell(offers, 3, "minecraft:bricks", 12);
            }
            case MINER -> {
                buy(offers, "minecraft:coal", 16, 2);
                buy(offers, "minecraft:raw_iron", 8, 4);
                sell(offers, 4, "minecraft:torch", 16);
                sell(offers, 8, "minecraft:iron_pickaxe", 1);
            }
            case UNITS -> {
                sell(offers, 16, "got:warhorn", 1);
                sell(offers, 12, "got:command_horn", 1);
                sell(offers, 16, "got:north_chestplate", 1);
                sell(offers, 12, "got:north_helmet", 1);
                sell(offers, 10, "minecraft:iron_sword", 1);
            }
            case HILLMEN_UNITS -> {
                sell(offers, 10, "got:trident", 1);
                sell(offers, 9, "got:iron_throwing_axe", 1);
                sell(offers, 8, "got:longbow", 1);
            }
            case MAESTER -> {
                buy(offers, "minecraft:paper", 20, 3);
                sell(offers, 4, "minecraft:book", 3);
                sell(offers, 8, "minecraft:compass", 1);
                sell(offers, 8, "got:mug_poppy_milk", 1);
            }
            default -> { }
        }
        got.GOTSmithScrollAcquisition.addBlacksmithOffers(offers, role);
        return offers;
    }

    private static void northArmor(GOTNorthNpcEntity npc) {
        northArmorWithoutHelmet(npc);
        npc.setItemSlot(EquipmentSlot.HEAD, stack("got:north_helmet"));
    }

    private static void northArmorWithoutHelmet(GOTNorthNpcEntity npc) {
        npc.setItemSlot(EquipmentSlot.FEET, stack("got:north_boots"));
        npc.setItemSlot(EquipmentSlot.LEGS, stack("got:north_leggings"));
        npc.setItemSlot(EquipmentSlot.CHEST, stack("got:north_chestplate"));
    }

    private static void northGuardArmor(GOTNorthNpcEntity npc) {
        npc.setItemSlot(EquipmentSlot.FEET, stack("got:northguard_boots"));
        npc.setItemSlot(EquipmentSlot.LEGS, stack("got:northguard_leggings"));
        npc.setItemSlot(EquipmentSlot.CHEST, stack("got:northguard_chestplate"));
        npc.setItemSlot(EquipmentSlot.HEAD, stack("got:northguard_helmet"));
    }

    private static void leatherArmor(GOTNorthNpcEntity npc, boolean helmet) {
        npc.setItemSlot(EquipmentSlot.FEET, new ItemStack(net.minecraft.world.item.Items.LEATHER_BOOTS));
        npc.setItemSlot(EquipmentSlot.LEGS, new ItemStack(net.minecraft.world.item.Items.LEATHER_LEGGINGS));
        npc.setItemSlot(EquipmentSlot.CHEST, new ItemStack(net.minecraft.world.item.Items.LEATHER_CHESTPLATE));
        if (helmet) npc.setItemSlot(EquipmentSlot.HEAD, new ItemStack(net.minecraft.world.item.Items.LEATHER_HELMET));
    }

    private static ItemStack randomIronWeapon(GOTNorthNpcEntity npc) {
        return switch (npc.getRandom().nextInt(4)) {
            case 0 -> new ItemStack(net.minecraft.world.item.Items.IRON_SWORD);
            case 1 -> stack("got:iron_spear");
            case 2 -> stack("got:iron_battleaxe");
            default -> stack("got:iron_pike");
        };
    }

    private static ItemStack randomPrimitiveWeapon(GOTNorthNpcEntity npc) {
        return switch (npc.getRandom().nextInt(3)) {
            case 0 -> new ItemStack(net.minecraft.world.item.Items.IRON_AXE);
            case 1 -> stack("got:iron_spear");
            default -> new ItemStack(net.minecraft.world.item.Items.IRON_SWORD);
        };
    }

    private static void buy(MerchantOffers offers, String input, int inputCount, int coins) {
        add(offers, input, inputCount, "got:coin_1", coins);
    }

    private static void sell(MerchantOffers offers, int coins, String output, int outputCount) {
        add(offers, "got:coin_1", coins, output, outputCount);
    }

    private static void add(MerchantOffers offers, String input, int inputCount,
                            String output, int outputCount) {
        ItemStack cost = stack(input, inputCount);
        ItemStack result = stack(output, outputCount);
        if (!cost.isEmpty() && !result.isEmpty()) {
            offers.add(new MerchantOffer(cost, result, 12, 2, 0.05F));
        }
    }

    static ItemStack stack(String id) { return stack(id, 1); }

    static ItemStack stack(String id, int count) {
        Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(id));
        return item == null ? ItemStack.EMPTY : new ItemStack(item, count);
    }
}
