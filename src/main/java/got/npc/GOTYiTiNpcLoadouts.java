package got.npc;

import got.GOTBannerItem;
import got.GOTBannerType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;
import net.minecraftforge.registries.ForgeRegistries;

/** Equipment and trade pools translated from the original YiTi classes. */
final class GOTYiTiNpcLoadouts {
    private GOTYiTiNpcLoadouts() {}

    static void configure(GOTYiTiNpcEntity npc) {
        YiTiNpcRole role = npc.getRole();
        npc.clearLoadout();
        switch (role) {
            case YI_TI_MAN -> npc.setCombatWeapon(stack("got:iron_dagger"));
            case YI_TI_LEVYMAN -> {
                npc.setCombatWeapon(randomIronWeapon(npc));
            }
            case YI_TI_LEVYMAN_CROSSBOWER -> {
                ItemStack melee = randomIronWeapon(npc);
                ItemStack crossbow = stack("got:iron_crossbow");
                npc.setWeapons(melee, crossbow);
                npc.setRangedWeapon(crossbow);
            }
            case YI_TI_SOLDIER -> {
                ItemStack weapon = randomIronWeapon(npc);
                npc.setWeapons(weapon, weapon);
                yi_tiArmor(npc, true);
            }
            case YI_TI_SOLDIER_CROSSBOWER -> {
                ItemStack melee = randomIronWeapon(npc);
                ItemStack crossbow = stack("got:iron_crossbow");
                npc.setWeapons(melee, crossbow);
                npc.setRangedWeapon(crossbow);
                yi_tiArmor(npc, true);
            }
            case YI_TI_BANNER_BEARER -> {
                ItemStack dagger = stack("got:iron_dagger");
                npc.setWeapons(dagger, dagger);
                yi_tiArmor(npc, true);
                npc.setItemSlot(EquipmentSlot.OFFHAND,
                        GOTBannerItem.createStack(GOTBannerType.byName("yi_ti")));
            }
            case YI_TI_CAPTAIN -> {
                ItemStack sword = new ItemStack(Items.IRON_SWORD);
                npc.setWeapons(sword, sword);
                yi_tiArmor(npc, false);
                npc.setItemSlot(EquipmentSlot.HEAD, stack("got:yi_ti_helmet_captain"));
            }
            case YI_TI_BLACKSMITH -> {
                ItemStack hammer = stack("got:blacksmith_hammer");
                npc.setWeapons(hammer, hammer);
            }
            case YI_TI_GOLDSMITH ->
                    npc.setWeapons(stack("got:iron_dagger"), stack("got:silver_ring"));
            case YI_TI_FARMER -> {
                ItemStack hoe = new ItemStack(Items.IRON_HOE);
                npc.setWeapons(hoe, hoe);
                npc.setItemSlot(EquipmentSlot.HEAD, stack("got:leather_hat"));
            }
            case YI_TI_FARMHAND -> {
                ItemStack hoe = new ItemStack(Items.IRON_HOE);
                npc.setWeapons(hoe, hoe);
            }
            case YI_TI_BARTENDER ->
                    npc.setWeapons(stack("got:iron_dagger"), stack("got:copper_goblet"));
            case YI_TI_MINER -> {
                ItemStack pickaxe = new ItemStack(Items.IRON_PICKAXE);
                npc.setWeapons(pickaxe, pickaxe);
            }
            case YI_TI_LUMBERMAN -> {
                ItemStack axe = new ItemStack(Items.IRON_AXE);
                npc.setWeapons(axe, axe);
            }
            case YI_TI_MASON ->
                    npc.setWeapons(new ItemStack(Items.IRON_PICKAXE), new ItemStack(Items.STONE));
            case YI_TI_BREWER ->
                    npc.setWeapons(stack("got:iron_dagger"), stack("got:mug_ale"));
            case YI_TI_FLORIST -> {
                npc.setWeapons(stack("got:iron_dagger"), new ItemStack(Items.POPPY));
            }
            case YI_TI_BUTCHER ->
                    npc.setWeapons(stack("got:iron_dagger"), new ItemStack(Items.BEEF));
            case YI_TI_FISHMONGER -> {
                npc.setWeapons(stack("got:iron_dagger"), new ItemStack(Items.COD));
            }
            case YI_TI_BAKER ->
                    npc.setWeapons(stack("got:rolling_pin"), new ItemStack(Items.BREAD));
            case YI_TI_SAMURAI -> {
                ItemStack sword = new ItemStack(Items.IRON_SWORD);
                npc.setWeapons(sword, sword);
                samuraiArmor(npc);
            }
            case YI_TI_SAMURAI_FLAMETHROWER -> {
                ItemStack sword = new ItemStack(Items.IRON_SWORD);
                ItemStack firePot = stack("got:fire_pot");
                npc.setWeapons(sword, firePot);
                npc.setRangedWeapon(firePot);
                samuraiArmor(npc);
            }
            case YI_TI_BOMBARDIER -> {
                ItemStack dagger = stack("got:iron_dagger");
                ItemStack bomb = new ItemStack(Items.GUNPOWDER);
                npc.setWeapons(dagger, bomb);
                npc.setRangedWeapon(bomb);
                bombardierArmor(npc);
            }
            case BU_GAI -> {
                ItemStack katana = stack("got:katana");
                npc.setWeapons(katana, katana);
            }

            default -> { }
        }
        switch (role) {
            case YI_TI_FISHMONGER, YI_TI_FLORIST, YI_TI_LUMBERMAN ->
                    npc.setItemSlot(EquipmentSlot.HEAD, stack("got:leather_hat"));
            default -> { }
        }
        GOTNpcShieldLoadouts.equip(npc, npc.getRole(), "got:yi_ti_shield");
        npc.updateHeldItem();
    }

    static MerchantOffers createOffers(YiTiNpcRole role) {
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
                buy(offers, "minecraft:oak_log", 16, 2);
                sell(offers, 2, "minecraft:oak_planks", 24);
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
                sell(offers, 16, "got:yi_ti_chestplate", 1);
                sell(offers, 12, "got:yi_ti_helmet", 1);
                sell(offers, 10, "minecraft:iron_sword", 1);
            }
            case SLAVER -> {
                buy(offers, "minecraft:wheat", 20, 2);
                buy(offers, "minecraft:carrot", 18, 2);
                sell(offers, 2, "minecraft:wheat_seeds", 12);
                sell(offers, 12, "got:command_horn", 1);
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

    private static void yi_tiArmor(GOTYiTiNpcEntity npc, boolean helmet) {
        npc.setItemSlot(EquipmentSlot.FEET, stack("got:yi_ti_boots"));
        npc.setItemSlot(EquipmentSlot.LEGS, stack("got:yi_ti_leggings"));
        npc.setItemSlot(EquipmentSlot.CHEST, stack("got:yi_ti_chestplate"));
        if (helmet) npc.setItemSlot(EquipmentSlot.HEAD, stack("got:yi_ti_helmet"));
    }

    private static void samuraiArmor(GOTYiTiNpcEntity npc) {
        npc.setItemSlot(EquipmentSlot.FEET, stack("got:yi_ti_samurai_boots"));
        npc.setItemSlot(EquipmentSlot.LEGS, stack("got:yi_ti_samurai_leggings"));
        npc.setItemSlot(EquipmentSlot.CHEST, stack("got:yi_ti_samurai_chestplate"));
        npc.setItemSlot(EquipmentSlot.HEAD, stack("got:yi_ti_samurai_helmet"));
    }

    private static void bombardierArmor(GOTYiTiNpcEntity npc) {
        npc.setItemSlot(EquipmentSlot.FEET, stack("got:yi_ti_bombardier_boots"));
        npc.setItemSlot(EquipmentSlot.LEGS, stack("got:yi_ti_bombardier_leggings"));
        npc.setItemSlot(EquipmentSlot.CHEST, stack("got:yi_ti_bombardier_chestplate"));
        npc.setItemSlot(EquipmentSlot.HEAD, stack("got:yi_ti_bombardier_helmet"));
    }

    private static ItemStack randomIronWeapon(GOTYiTiNpcEntity npc) {
        return switch (npc.getRandom().nextInt(4)) {
            case 0 -> new ItemStack(Items.IRON_SWORD);
            case 1 -> stack("got:iron_spear");
            case 2 -> stack("got:iron_battleaxe");
            default -> stack("got:iron_pike");
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
