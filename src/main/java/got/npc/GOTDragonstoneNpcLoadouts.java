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

/** Equipment and trade pools translated from the original Dragonstone classes. */
final class GOTDragonstoneNpcLoadouts {
    private GOTDragonstoneNpcLoadouts() {}

    static void configure(GOTDragonstoneNpcEntity npc) {
        DragonstoneNpcRole role = npc.getRole();
        npc.clearLoadout();
        switch (role) {
            case DRAGONSTONE_MAN -> npc.setCombatWeapon(stack("got:iron_dagger"));
            case DRAGONSTONE_LEVYMAN -> {
                npc.setCombatWeapon(randomIronWeapon(npc));
                leatherArmor(npc);
            }
            case DRAGONSTONE_LEVYMAN_ARCHER -> {
                ItemStack melee = randomIronWeapon(npc);
                ItemStack bow = stack("got:longbow");
                npc.setWeapons(melee, bow);
                npc.setRangedWeapon(bow);
                leatherArmor(npc);
            }
            case DRAGONSTONE_SOLDIER -> {
                ItemStack weapon = randomIronWeapon(npc);
                npc.setWeapons(weapon, weapon);
                dragonstoneArmor(npc, true);
            }
            case DRAGONSTONE_SOLDIER_ARCHER -> {
                ItemStack melee = randomIronWeapon(npc);
                ItemStack bow = stack("got:longbow");
                npc.setWeapons(melee, bow);
                npc.setRangedWeapon(bow);
                dragonstoneArmor(npc, true);
            }
            case DRAGONSTONE_BANNER_BEARER -> {
                ItemStack dagger = stack("got:iron_dagger");
                npc.setWeapons(dagger, dagger);
                dragonstoneArmor(npc, true);
                npc.setItemSlot(EquipmentSlot.OFFHAND,
                        GOTBannerItem.createStack(GOTBannerType.byName("stannis")));
            }
            case DRAGONSTONE_CAPTAIN -> {
                ItemStack sword = new ItemStack(Items.IRON_SWORD);
                npc.setWeapons(sword, sword);
                dragonstoneArmor(npc, false);
            }
            case DRAGONSTONE_BLACKSMITH -> {
                ItemStack hammer = stack("got:blacksmith_hammer");
                npc.setWeapons(hammer, hammer);
            }
            case DRAGONSTONE_GOLDSMITH ->
                    npc.setWeapons(stack("got:iron_dagger"), stack("got:silver_ring"));
            case DRAGONSTONE_FARMER -> {
                ItemStack hoe = new ItemStack(Items.IRON_HOE);
                npc.setWeapons(hoe, hoe);
                npc.setItemSlot(EquipmentSlot.HEAD, stack("got:leather_hat"));
            }
            case DRAGONSTONE_FARMHAND -> {
                ItemStack hoe = new ItemStack(Items.IRON_HOE);
                npc.setWeapons(hoe, hoe);
            }
            case DRAGONSTONE_BARTENDER ->
                    npc.setWeapons(stack("got:iron_dagger"), stack("got:copper_goblet"));
            case DRAGONSTONE_MINER -> {
                ItemStack pickaxe = new ItemStack(Items.IRON_PICKAXE);
                npc.setWeapons(pickaxe, pickaxe);
            }
            case DRAGONSTONE_LUMBERMAN -> {
                ItemStack axe = new ItemStack(Items.IRON_AXE);
                npc.setWeapons(axe, axe);
                npc.setItemSlot(EquipmentSlot.HEAD, stack("got:leather_hat"));
            }
            case DRAGONSTONE_MASON ->
                    npc.setWeapons(new ItemStack(Items.IRON_PICKAXE), new ItemStack(Items.STONE));
            case DRAGONSTONE_BREWER ->
                    npc.setWeapons(stack("got:iron_dagger"), stack("got:mug_ale"));
            case DRAGONSTONE_FLORIST -> {
                npc.setWeapons(stack("got:iron_dagger"), new ItemStack(Items.POPPY));
                npc.setItemSlot(EquipmentSlot.HEAD, stack("got:leather_hat"));
            }
            case DRAGONSTONE_BUTCHER ->
                    npc.setWeapons(stack("got:iron_dagger"), new ItemStack(Items.BEEF));
            case DRAGONSTONE_FISHMONGER -> {
                npc.setWeapons(stack("got:iron_dagger"), new ItemStack(Items.COD));
                npc.setItemSlot(EquipmentSlot.HEAD, stack("got:leather_hat"));
            }
            case DRAGONSTONE_BAKER ->
                    npc.setWeapons(stack("got:rolling_pin"), new ItemStack(Items.BREAD));

            case ARDRIAN_CELTIGAR -> npc.setCombatWeapon(stack("got:ardrian_celtigar_axe"));
            case STANNIS_BARATHEON -> {
                npc.setCombatWeapon(stack("got:lightbringer"));
                dragonstoneArmor(npc, false);
            }
            case DAVOS_SEAWORTH, MATTHOS_SEAWORTH, AURANE_WATERS ->
                    npc.setCombatWeapon(stack("got:alloy_steel_sword"));
            case SELYSE_BARATHEON -> npc.setCombatWeapon(stack("got:alloy_steel_dagger"));
            case MONFORD_VELARYON -> {
                npc.setCombatWeapon(stack("got:cutwave"));
                npc.setItemSlot(EquipmentSlot.CHEST, stack("got:monford_velaryon_brooch"));
            }
            case MELISANDRA, SHIREEN_BARATHEON -> { }
            default -> {
                ItemStack sword = stack("got:alloy_steel_sword");
                npc.setWeapons(sword, sword);
            }
        }
        GOTNpcShieldLoadouts.equip(npc, npc.getRole(), "got:dragonstone_shield");
        npc.updateHeldItem();
    }

    static MerchantOffers createOffers(DragonstoneNpcRole role) {
        MerchantOffers legendary = got.economy.GOTLegendaryTraderOffers.forRole(role.id());
        if (legendary != null) return legendary;
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
                sell(offers, 16, "got:dragonstone_chestplate", 1);
                sell(offers, 12, "got:dragonstone_helmet", 1);
                sell(offers, 10, "minecraft:iron_sword", 1);
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

    private static void dragonstoneArmor(GOTDragonstoneNpcEntity npc, boolean helmet) {
        npc.setItemSlot(EquipmentSlot.FEET, stack("got:dragonstone_boots"));
        npc.setItemSlot(EquipmentSlot.LEGS, stack("got:dragonstone_leggings"));
        npc.setItemSlot(EquipmentSlot.CHEST, stack("got:dragonstone_chestplate"));
        if (helmet) npc.setItemSlot(EquipmentSlot.HEAD, stack("got:dragonstone_helmet"));
    }

    private static void leatherArmor(GOTDragonstoneNpcEntity npc) {
        npc.setItemSlot(EquipmentSlot.FEET, new ItemStack(Items.LEATHER_BOOTS));
        npc.setItemSlot(EquipmentSlot.LEGS, new ItemStack(Items.LEATHER_LEGGINGS));
        npc.setItemSlot(EquipmentSlot.CHEST, new ItemStack(Items.LEATHER_CHESTPLATE));
        npc.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.LEATHER_HELMET));
    }

    private static ItemStack randomIronWeapon(GOTDragonstoneNpcEntity npc) {
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
